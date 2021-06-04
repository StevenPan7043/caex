package com.pmzhongguo.ex.core.sms;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.pmzhongguo.ex.core.utils.HttpUtils;
import com.pmzhongguo.ex.core.utils.JsonUtil;
import com.pmzhongguo.ex.core.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.HashMap;
import java.util.Map;


/**
 * @description: 创蓝短信抽象类
 * @date: 2019-05-30 14:21
 * @author: 十一
 */
public abstract class AbstractChuangLanSmsService implements ISmsService{

    /**
     * 通知模版【测试】
     */
    public static String CLT_TEST_VERIFY_CODE = "【253云通讯】您的验证码是:{$var}";
    public static String CLT_TEST_VERIFY_CODE_NAME_CODE_TIME = "【253云通讯】尊敬的{$var},您好,您的验证码是{$var},{$var}分钟内有效";

    /**
     * 模版
     */
    public static String CLT_CODE_TIME = "【ZZEX】您的验证码是{$var}，有效期{$var}分钟";
    public static String CLT_MEMBERAPPEAL_MEMBER_ORDER = "【ZZEX】用户{$var}对订单{$var}提出申诉，请及时处理。";
    public static String CLT_CODE = "【ZZEX】您的验证码是{$var}。如非本人操作，请忽略本短信";
    public static String CLT_ZZEXAPPEAL_MEMBER_ORDER = "【ZZEX】{$var}已将订单{$var}发起申诉，平台客服将介入处理，请及时关注。";
    public static String CLT_CANCEL_MEMBER_ORDER = "【ZZEX】{$var}已将订单{$var}取消，如有疑问请联系平台客服。";
    public static String CLT_MEMBER_CURRENCY = "【ZZEX】{$var}已确认收到您的付款并放行，您所购买的{$var}已发放到您的法币账户，请登录账户查收。";
    public static String CLT_PAY_MEMBER_ORDER = "【ZZEX】{$var}将订单{$var}标记为“已付款”状态，请及时登录收款账户查看并确认放行。";
    public static String CLT_BUY_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER_TIME = "【ZZEX】您已向{$var}用户购买{$var} {$var}，购买单价为{$var} {$var}，订单号为{$var}，请于{$var}分钟内付款。";
    public static String CLT_SELL_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER = "【ZZEX】您已向{$var}用户出售{$var} {$var}，出售单价为{$var} {$var}，订单号为{$var}，等待对方支付。";

    private static Logger logger = LoggerFactory.getLogger(AbstractChuangLanSmsService.class);

    /**
     * api发送url
     */
    private static final String SEND_API_URL = PropertiesUtil.getPropValByKey("chuang_lan_sms_api_url");

    /**
     * 账号
     */
    private static final String ACCOUNT = PropertiesUtil.getPropValByKey("chuang_lan_sms_api_account");

    /**
     * 密码
     */
    private static final String PASSWORD = PropertiesUtil.getPropValByKey("chuang_lan_sms_api_password");

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
     * 创蓝api通用发送
     */
    @Override
    public void send() {
        HashMap<String, String> paramsMap = Maps.newHashMap();
        paramsMap.put("account",ACCOUNT);
        paramsMap.put("password",PASSWORD);
        paramsMap.put("msg",template);
        paramsMap.put("params",params);
        String jsonString = JSON.toJSONString(paramsMap);
        String response = HttpUtils.postWithJSON(SEND_API_URL, jsonString);
        if (response == null) {
            logger.warn("<================ 发送短信失败或超时，请求参数：{},\t请求结果：{}",params+"\t模版："+template,response);
            return;
        }
        Map map = JsonUtil.jsonToMap(response);
        // code:0 提交成功
        if(map.get("code") != null && !map.get("code").toString().equals("0")) {
            logger.warn("<================ 发送短信请求参数：{},\t请求结果：{}",params+"\t模版："+template,response);
        }
    }

    /**
     * 发送短信
     */
    @Override
    public void run() {
        send();
    }
}
