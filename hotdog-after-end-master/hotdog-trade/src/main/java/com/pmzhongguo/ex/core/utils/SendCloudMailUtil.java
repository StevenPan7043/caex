package com.pmzhongguo.ex.core.utils;



import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @description: sendcloud发送邮件
 * @date: 2019-01-09 11:40
 * @author: 十一
 */
@Component
@EnableAsync
public class SendCloudMailUtil {

    private static final String MAIL_API;
    private static final String API_USER;
    private static final String API_KEY;
    private static final String FROM;
    private static Properties properties;
    private static final String FROM_NAME = "ZZEX";
    private static final String SUBJECT = "【ZZEX】验证码";

    // 加载
    static {
        properties = PropertiesUtil.loadProperties("spring/mail.properties");
        MAIL_API = properties.getProperty("mail.sendcloud.api");
        API_USER = properties.getProperty("mail.sendcloud.user");
        API_KEY = properties.getProperty("mail.sendcloud.key");
        FROM = properties.getProperty("mail.sendcloud.from");
    }

    @Async
    public String sendMail(String toAccount,String code) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("apiUser", API_USER);
        map.put("apiKey", API_KEY);
        map.put("from", FROM);
        map.put("fromName", FROM_NAME);
        map.put("subject", SUBJECT);
        map.put("plain", String.format("尊敬的用户，您的验证码是：%s。请不要把验证码泄露给其他人。 感谢您对ZZEX的支持,祝您生活愉快! 【ZZEX】",code));
        map.put("respEmailId", "true");
        map.put("useNotification", "false");
        map.put("to", toAccount);
        String response = HelpUtils.post(MAIL_API, map);
        System.out.println(response);
        return response;
    }
    @Async
    public String sendMailInfo(String toAccount,String format) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("apiUser", API_USER);
        map.put("apiKey", API_KEY);
        map.put("from", FROM);
        map.put("fromName", FROM_NAME);
        map.put("subject", SUBJECT);
        map.put("plain", format);
        map.put("respEmailId", "true");
        map.put("useNotification", "false");
        map.put("to", toAccount);
        String response = HelpUtils.post(MAIL_API, map);
        System.out.println(response);
        return response;
    }

    /**
     * otc 发送邮件
     * @param toAccount
     * @param template
     * @return
     */
    public static String  sendMail4Otc(String toAccount,String template) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("apiUser", API_USER);
        map.put("apiKey", API_KEY);
        map.put("from", FROM);
        map.put("fromName", FROM_NAME);
        map.put("subject", SUBJECT);
        map.put("plain", template);
        map.put("respEmailId", "true");
        map.put("useNotification", "false");
        map.put("to", toAccount);
        String response = HelpUtils.post(MAIL_API, map);
        System.out.println(response);
        return response;
    }


    public static void main(String[] args) {
        SendCloudMailUtil mail = new SendCloudMailUtil();
        String result = mail.sendMail("1473919563@qq.com", "999111");
//        String result = sendMail("liueleven@aliyun.com", "999111");
//        String result = mail.sendMail("1473919563@qq.com", "999888");
//        System.out.println(result);
    }
}
