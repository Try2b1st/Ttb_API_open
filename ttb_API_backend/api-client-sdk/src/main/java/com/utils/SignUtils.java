package com.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

import java.util.Map;

/**
 * 生成签名工具
 *
 * @author 下水道的小老鼠
 */
public class SignUtils {
    /**
     * 生成签名
     * @param hashMap
     * @param secretKey
     * @return
     */
    public static String getSign(Map<String, String> hashMap, String secretKey) {
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        String content = hashMap.toString() + "." + secretKey;
        return md5.digestHex(content);
    }
}
