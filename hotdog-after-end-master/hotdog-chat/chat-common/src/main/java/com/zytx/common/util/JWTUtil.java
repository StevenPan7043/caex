package com.zytx.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {

    // 过期时间5分钟
    private static final long EXPIRE_TIME = 5 * 60 * 1000;

    /**
     * 生成token
     *
     * @param id     用户ID
     * @param phone  用户手机或邮箱
     * @param secret 用户密码
     * @return
     */
    public static String sign(Integer id, String phone, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create()
                .withClaim("id", id)
                .withClaim("phone", phone)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 生成socket token
     *
     * @param id     用户ID
     * @param client 登录设备 APP或WEB
     * @param phone  用户账号
     * @return
     */
    public static String getSocketToken(Integer id, String client, String phone) {
        Algorithm algorithm = Algorithm.HMAC256(phone);
        // 附带username信息
        return JWT.create()
                .withClaim("id", id.toString())
                .withClaim("client", client)
                .sign(algorithm);
    }

    /**
     * 解析token
     *
     * @param token
     * @return
     */
    public static Map<String, String> parseToken(String token) {
        Map<String, String> map = new HashMap<>();
        try {
            JWT.decode(token).getClaims().forEach((k, v) -> map.put(k, v.asString()));
        } catch (Exception e) {
        }
        return map;
    }

    /**
     * 校验token
     *
     * @param token
     * @param id
     * @param secret
     * @return
     */
    public static Object verify(String token, Integer id, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("id", id)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            Map<String, Object> map = new HashMap<>();
            map.put("id", jwt.getClaim("id").asString());
            map.put("phone", jwt.getClaim("phone").asString());
            System.out.println(jwt.getHeader());
            return true;
        } catch (Exception exception) {
            //token过期时运行
            exception.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
//        String sign = JWTUtil.sign(11, "2222", "123");
//        Object verify = JWTUtil.verify(sign, 111, "123");
//        System.out.println(Algorithm.HMAC256("1234").getName());
        String app = JWTUtil.getSocketToken(111, "APP", "1111");
    }
}
