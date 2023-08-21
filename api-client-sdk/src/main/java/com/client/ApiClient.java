package com.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.model.User;
import com.utils.SignUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用第三方接口
 *
 * @author 下水道的小老鼠
 */
public class ApiClient {

    private static final String REQUEST_ADDRESS = "http://localhost:8090";
    private String accessKey;
    private String secretKey;

    public ApiClient(String accessKey, String secretKry) {
        this.accessKey = accessKey;
        this.secretKey = secretKry;
    }


    /**
     * 身份验证
     *
     * @param body
     * @return
     */
    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> hashMap = new HashMap<>();
        //用于服务端确定身份，生成签名，进行比较
        hashMap.put("accessKey", accessKey);

        //2.不能直接发送
        //hashMap.put("secretKey",secretKey);

        //3.随机数
        hashMap.put("nonce", RandomUtil.randomNumbers(4));

        //4.参数
        hashMap.put("body", body);

        //5.时间戳
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));

        //6.签名
        hashMap.put("sign", SignUtils.getSign(hashMap, secretKey));

        return hashMap;
    }

    public String getNameByGet(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);

        String result = HttpUtil.get(REQUEST_ADDRESS + "/api/name", paramMap);
        System.out.println(result);
        return result;
    }

    public String getNameByPost(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);

        String result = HttpUtil.post(REQUEST_ADDRESS + "/api/name", paramMap);
        System.out.println(result);
        return result;
    }

    public String getNameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);
        HttpResponse execute = HttpRequest.post(REQUEST_ADDRESS + "/api/name/user")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        System.out.println(execute.getStatus());
        String result = execute.body();
        System.out.println(result);
        return result;
    }

}
