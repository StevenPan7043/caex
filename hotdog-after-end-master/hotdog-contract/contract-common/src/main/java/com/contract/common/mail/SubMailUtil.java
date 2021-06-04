package com.contract.common.mail;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2020/6/23 2:39 PM
 */
public class SubMailUtil {

    /**
     * 账号
     */
    private static final String APP_ID = "51119";
    /**
     * 密码
     */
    private static final String SIGNATURE = "e4de6ae85d3be2218135ba6a23d73386";
    /**
     * 请求url
     */
    private static final String URL = "https://api.mysubmail.com/message/send.json";

    private static final String sign = "【CAEX】";


    /**
     * @param mobile
     * @param content
     */
    public static void send(String mobile, String content) {
        content = sign + content;
        String result = null;
        try {
            Map<String, Object> sendParam = new HashMap<>();
            sendParam.put("appid", APP_ID);
            sendParam.put("to", mobile);
            sendParam.put("content", content);
            sendParam.put("signature", SIGNATURE);
            result = HttpUtil.jsonPost(URL, new Gson().toJson(sendParam));
            JSONObject object = JSONObject.parseObject(result);
            if (!object.get("status").equals("success")) {
                System.out.println(String.format("短信发送异常，手机号：%s，发送url：%s", mobile, URL));
            }
        } catch (Exception e) {
            System.out.println(String.format("短信发送异常，手机号：%s，发送url：%s", mobile, URL));
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        String send = "【CAEX】短信发送异常，手机号";
        String phone = "18738082201";
        send(phone,send);
    }
}
