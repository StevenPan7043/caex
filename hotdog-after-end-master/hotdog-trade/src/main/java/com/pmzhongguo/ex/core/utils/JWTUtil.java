package com.pmzhongguo.ex.core.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.Maps;
import com.pmzhongguo.ex.core.web.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: jwt token加密，验证
 * @date: 2019-01-12 10:15
 * @author: 十一
 */
public class JWTUtil {

    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    // 秘钥随机16位
    private static final String SECRET = "zjs8s2ex_88etmas";
    // 签发人
    private static final String ISSUER = "jszzextc";



    /**
     * 生成token
     * @param claims 那些参数参与签名加密
     * @return
     */
    public static String genToken(Map<String,String> claims) throws UnsupportedEncodingException {
        // 使用hmac256算法
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        // 签发人+过期时间+用户信息=token
        JWTCreator.Builder builder = JWT.create().withIssuer(ISSUER).withExpiresAt(HelpUtils.dateAddSecondInt(new Date(), Constants.MEMBER_TOKEN_TIME_OUT));
        claims.forEach((k,v) -> builder.withClaim(k,v));
        return builder.sign(algorithm).toString();
    }

    /**
     * 验证token
     * @param token
     * @return
     */
    public static Map<String,String> verifyToken(String token) throws UnsupportedEncodingException {
        Algorithm algorithm = null;
        Map<String,String> resultMap = Maps.newHashMap();
        // token的逆运算
        algorithm = Algorithm.HMAC256(SECRET);
        JWTVerifier build = JWT.require(algorithm).withIssuer(ISSUER).build();
        DecodedJWT verify = build.verify(token);

        // 获得用户信息
        Map<String, Claim> claims = verify.getClaims();
        claims.forEach((k,v)->resultMap.put(k,v.asString()));
        return resultMap;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        Map<String,String> map  = new HashMap<String,String>();
        map.put("m_name","13097368626");
        String token = genToken(map);
        System.out.println(token);
        Map<String, String> resultMap = verifyToken(token);
        String s = JsonUtil.toJson(resultMap);
        System.out.println(s);
    }
}
