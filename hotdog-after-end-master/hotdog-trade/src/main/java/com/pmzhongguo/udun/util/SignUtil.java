package com.pmzhongguo.udun.util;


import org.apache.commons.codec.digest.DigestUtils;

/**
 * 签名工具类
 */
public class SignUtil {

    public static String sign(String key, String timestamp, String nonce, String type, String body) throws Exception {
        return DigestUtils.md5Hex(body + key + nonce + timestamp + type).toLowerCase();
    }

    public static String sign(String key, String timestamp, String nonce, String body) throws Exception {
        String raw = body + key + nonce + timestamp;
        String sign = DigestUtils.md5Hex(raw).toLowerCase();
        return sign;
    }

    public static String sign(String key, String timestamp, String nonce) throws Exception {
        return DigestUtils.md5Hex(key + nonce + timestamp).toLowerCase();
    }

    public static boolean checkSign(String key, String timestamp, String nonce, String body, String sign) throws Exception {
        String checkSign = SignUtil.sign(key, timestamp, nonce, body);
        return checkSign.equals(sign);
    }
}
