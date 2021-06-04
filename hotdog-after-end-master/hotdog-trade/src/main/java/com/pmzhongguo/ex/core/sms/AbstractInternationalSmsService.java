package com.pmzhongguo.ex.core.sms;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.pmzhongguo.ex.business.dto.JuHeSendResp;
import com.pmzhongguo.ex.core.utils.HttpUtils;
import com.pmzhongguo.ex.core.utils.JsonUtil;
import com.pmzhongguo.ex.core.utils.PropertiesUtil;
import com.pmzhongguo.otc.sms.JuheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * @date: 2019-07-02 10:21
 * @author: Jary
 */
public abstract class AbstractInternationalSmsService implements ISmsService{


    private static Logger logger = LoggerFactory.getLogger(AbstractInternationalSmsService.class);

    /**
     * 国内服务器请求地址
     */
    private static final String INNER_COUNTRY_SMS_API_URL = PropertiesUtil.getPropValByKey("inner_country_sms_api_url");

    /**
     * 海外服务器请求地址
     */
    private static final String INTERNATIONAL_SMS_API_URL = PropertiesUtil.getPropValByKey("international_sms_api_url");

    /**
     * 账号
     */
    private static final String INNER_ACCOUNT = PropertiesUtil.getPropValByKey("international_sms_api_account");

    /**
     * 密码
     */
    private static final String INNER_PASSWORD = PropertiesUtil.getPropValByKey("international_sms_api_password");

    private static final String JUHE_GJ_APIKEY = "f270b36e3f7183a4fee47da56d16ce02";

    /**
     * 发送时间,2019-06-03 11:33:52
     */
    protected String sendTime;

    /**
     * 请求参数，英文逗号分隔，例如：ETH,23.5
     */
    protected String params;

    /**
     * 短信接收方，例如：13097368626
     */
    protected String receiver;

    /**
     * 短信内容模版，例如：【253云通讯】您的验证码是:{$var}
     */
    protected String template;

    /**
     * 区号
     */
    protected Integer areaCode;

    /**
     * 创蓝api通用发送
     */
    @Override
    public void send() {

        //组装请求参数
        JSONObject paramsMap = new JSONObject();
        paramsMap.put("account", INNER_ACCOUNT);
        paramsMap.put("password", INNER_PASSWORD);
        paramsMap.put("msg", this.template);
        paramsMap.put("mobile", areaCode +""+receiver);
        paramsMap.put("senderId", "");
        String jsonString = JSON.toJSONString(paramsMap);
//        logger.warn("国际短信请求前，请求参数,url:{},模版：{}", INNER_COUNTRY_SMS_API_URL, paramsMap);
        String response = HttpUtils.postWithJSON(INNER_COUNTRY_SMS_API_URL, jsonString);
        if (response == null) {
            return;
        }
//        logger.warn("国际短信返回值：{}", new Gson().toJson(response));
        Map map = JsonUtil.jsonToMap(response);

        // code:0 提交成功
        if (map.get("code") != null && !map.get("code").toString().equals("0")) {
            logger.warn("<================ 国际短信发送短信请求参数：{},\t请求结果：{}", params + "\t模版：" + template, response);
        }
    }

    @Override
    public void juHeSend() {
        String response = getTplId();
        JuHeSendResp juHeSendResp = JsonUtil.fromJson(response, JuHeSendResp.class);
        if (juHeSendResp.getError_code() != null && !juHeSendResp.getError_code().equals("0")) {
            logger.warn("<================ 国际短信发送短信请求参数：{},\t请求结果：{}", params + "\t模版：" + template, response);
        }
        //组装请求参数
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("key", JUHE_GJ_APIKEY);
        paramsMap.put("tplId", juHeSendResp.getResult().getTplId());
        paramsMap.put("mobile", receiver);
        paramsMap.put("areaNum", areaCode+"");
        String post = HttpUtils.post("http://v.juhe.cn/smsInternational/send.php", paramsMap);
        System.out.println(post);

        Map map = JsonUtil.jsonToMap(response);
//         code:0 提交成功
        if (map.get("error_code") != null && !map.get("error_code").toString().equals("0")) {
            logger.warn("<================ 国际短信发送短信请求参数：{},\t请求结果：{}", params + "\t模版：" + template, response);
        }
    }

    private String getTplId() {
        //组装请求参数
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("key", JUHE_GJ_APIKEY);
        paramsMap.put("signature", "ZZEX");
        paramsMap.put("content", this.template);
        String string = JSON.toJSONString(paramsMap);
        System.out.println(string);
        return HttpUtils.post("http://v.juhe.cn/smsInternational/submitTpl.php", paramsMap);
    }

    /**
     * 发送短信
     */
    @Override
    public void run() {
//        send();
        juHeSend();
    }


}
