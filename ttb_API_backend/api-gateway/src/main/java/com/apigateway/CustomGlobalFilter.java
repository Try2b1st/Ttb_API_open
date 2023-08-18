package com.apigateway;

import com.apicommon.model.entity.InterfaceInfo;
import com.apicommon.model.entity.User;
import com.apicommon.service.InnerInterfaceInfoService;
import com.apicommon.service.InnerUserInterfaceInfoService;
import com.apicommon.service.InnerUserService;
import com.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author 下水道的小老鼠
 */
@Component
@Slf4j
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");
    //1.用户发送请求到 API 网关

    /**
     * 全局过滤
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("custom global filter");
        // 2.请求日志
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        String url = request.getURI().toString();
        String method = request.getMethod().toString();
        log.info("请求的唯一标识:" + request.getId());
        log.info("请求的路径:" + path);
        log.info("请求的方法:" + method);
        log.info("请求的参数:" + request.getQueryParams());
        log.info("请求的来源地址:" + request.getLocalAddress().getHostString());

        // 3.（黑白名单）
        String sourceAddress = request.getLocalAddress().getHostString();
        ServerHttpResponse response = exchange.getResponse();
        if (!IP_WHITE_LIST.contains(sourceAddress)) {
            return handleNoAuth(response);
        }
        // 4.用户鉴权（判断 ak、sk 是否合法）
        Map<String, String> hashMap = new HashMap<>();
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String body = headers.getFirst("body");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");

        //在数据库中查询accessKey
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.info("getInvokeUser error" + e);
        }

        if (invokeUser == null) {
            return handleNoAuth(response);
        }


        if (nonce != null && Long.parseLong(nonce) > 10000) {
            return handleNoAuth(response);
        }

        //时间戳不超过3分钟
        long now = System.currentTimeMillis() / 1000;
        long requestTime = 0;
        if (timestamp != null) {
            requestTime = Long.parseLong(timestamp);
        }
        final long THREE_MIN = 60 * 3L;
        if (now - requestTime > THREE_MIN) {
            return handleNoAuth(response);
        }

        //生成签名与发过来的签名比较
        hashMap.put("accessKey", accessKey);
        hashMap.put("nonce", nonce);
        hashMap.put("body", body);
        hashMap.put("timestamp", timestamp);
        String secretKey = invokeUser.getSecretKey();
        String newSign = SignUtils.getSign(hashMap, secretKey);
        if (!Objects.equals(newSign, sign)) {
            throw new RuntimeException("无权限");
        }

        //5.请求的模拟接口是否存在？
        InterfaceInfo invokeInterfaceInfo = null;
        try {
            invokeInterfaceInfo = innerInterfaceInfoService.getInterfaceInfo(url, method);
        } catch (Exception e) {
            log.info("getInterfaceInfo error");
        }
        if (invokeInterfaceInfo == null) {
            return handleNoAuth(response);
        }
        // todo 用户是否还有对该接口的调用次数


        // 6.请求转发，调用模拟接口
        return handelResponseLog(exchange, chain, invokeInterfaceInfo.getId(), invokeUser.getId());

    }

    @Override
    public int getOrder() {
        return -1;
    }

    /**
     * 处理响应
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handelResponseLog(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceId, long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();

            HttpStatus statusCode = originalResponse.getStatusCode();

            if (statusCode == HttpStatus.OK) {
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    /**
                     * 增强Response 等调用完转发接口后才执行
                     * @param body
                     * @return
                     */
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                //从数据流缓存中获取到接口调用的返回结果
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                //释放掉数据流缓存的内存
                                DataBufferUtils.release(dataBuffer);
                                // 构建日志 获取响应数据
                                String data = new String(content, StandardCharsets.UTF_8);
                                //7.输出响应日志
                                log.info("响应码:" + originalResponse.getStatusCode() + "\n" + "响应数据:" + data);

                                //8.调用成功，接口调用次数 + 1
                                try {
                                    innerUserInterfaceInfoService.invokeCount(userId, interfaceId);
                                } catch (Exception e) {
                                    log.info("invokeCount error" + e);
                                }

                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            // 9.调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            //降级处理返回数据
            return chain.filter(exchange);
        } catch (Exception e) {
            log.error("网关处理响应异常:" + e);
            return chain.filter(exchange);
        }
    }

    /**
     * 返回403错误页面
     *
     * @param response
     * @return
     */
    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> invokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }
}