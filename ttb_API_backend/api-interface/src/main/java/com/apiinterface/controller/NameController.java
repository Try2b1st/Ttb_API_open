package com.apiinterface.controller;

import com.model.User;
import com.utils.SignUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 三种方式获取名字（测试）
 *
 * @author 下水道的小老鼠
 */
@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/")
    public String getNameByGet(String name) {
        return "GET 你的名字是" + name;
    }

    @PostMapping("/")
    public String getNameByPost(@RequestParam String name) {
        return "POST 你的名字是" + name;
    }

    @PostMapping("/user")
    public String getNameByPost(@RequestBody User user, HttpServletRequest request) {
        Map<String, String> hashMap = new HashMap<>();

        String accessKey = request.getHeader("accessKey");
        String nonce = request.getHeader("nonce");
        String body = request.getHeader("body");
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");

        // todo 在数据库中查询
        if (!"yupi".equals(accessKey)) {
            throw new RuntimeException("无权限");
        }

        if(Long.parseLong(nonce) > 10000){
            throw new RuntimeException("无权限");
        }

        //时间戳不超过3分钟
        long now = System.currentTimeMillis() / 1000;
        long requestTime = Long.parseLong(timestamp);
        if(now - requestTime > 180){
            throw new RuntimeException("无权限");

        }

        //生成签名与发过来的签名比较
        hashMap.put("accessKey", accessKey);
        hashMap.put("nonce", nonce);
        hashMap.put("body", body);
        hashMap.put("timestamp", timestamp);
        String newSign = SignUtils.getSign(hashMap, "qwerqwer");

        if(!Objects.equals(newSign, sign)){
            throw new RuntimeException("无权限");
        }
        return "POST 你的用户名是" + user.getUsername();
    }
}
