package com.contract.common.mail;


import java.net.URLEncoder;

/**
 * @description: 凌凯短信 网站：http://yzm.mb345.com/SmsGet.aspx
 * xx已将订单xx发起投诉，平台客户将介入处理，请及时关注。
 * 用户xx对于订单xx发起投诉，请及时处理。
 * xx已将订单xx取消，如有疑问请联系平台客户。
 * xx已收到您的付款，您所购买的xx已发送到您的账户，请查收。
 * xx将订单xx确认为“已付款”状态，请及时登陆账户查看并确认。
 * 您已向xx用户xx xx，出售单价为xx xx，订单好为xx，等待支付。
 * 您已向xx用户购买xx xx，购买单价为xx xx，订单号为xx，请于xx分钟内付款。
 * 您的xx于xx充值xx成功。
 * 您的xx于xx提现xx成功。
 * 您的验证码是xx。如非本人操作，请忽略本短信
 * @date: 2019-12-27 19:15
 * @author: 十一
 */
public class LingKaiUtil {

    /**
     * 账号
     */
    private static final String CORP_ID = "LKRDY00792";
    /**
     * 密码
     */
    private static final String PWD = "ss1103@";
    /**
     * 请求url
     */
    private static final String URL = "http://yzm.mb345.com/ws/BatchSend2.aspx?CorpID="+CORP_ID+"&Pwd="+PWD+"&Mobile=%s&Content=%s&SendTime=%s&cell=";


    public static void main(String[] args) {
        String mobile = "13097368626";
//        String content = String.format(MobiInfoTemplateEnum.LK_CANCELED_TRADE.getCode(),"刘十一","T0011234001");
//        String content = String.format(MobiInfoTemplateEnum.LK_COMPLAINING_TRADE.getCode(),"刘十一@163.com","T0011234001");
//        String content = String.format(MobiInfoTemplateEnum.LK_COMPLAINING_TRADE_WARN.getCode(),"刘十一","T0011234001");
//        String content = String.format(MobiInfoTemplateEnum.LK_RECHARGE_CODE.getCode(),"ETH",HelpUtils.formatDate8(new Date()),18.88);
//        String content = String.format(MobiInfoTemplateEnum.LK_SECURITY_CODE.getCode(),"092743",5);
        // 您已向%s用户购买%s %s，购买单价为%s %s，订单号为%s，请于%s分钟内付款
//        String content = String.format(MobiInfoTemplateEnum.LK_TRADE_BOUGHT.getCode(),"刘十一","ETH","12.5","ETH","987.99","TD011101",15);
//        String content = String.format(MobiInfoTemplateEnum.LK_TRADE_CONFIRMED.getCode(),"刘十一","ETH");
//        String content = String.format(MobiInfoTemplateEnum.LK_TRADE_PAID.getCode(),"刘十一","TD111101201");
        // 您已向%s用户出售%s %s，出售单价为%s %s，订单号为%s，等待支付。============================= 未调试
//        String content = String.format(MobiInfoTemplateEnum.LK_TRADE_SOLD.getCode(),"刘十一","ETH",9.2,973,"ETH","TD01111811");
//        String content = String.format(MobiInfoTemplateEnum.LK_WITHDRAWAL_CODE.getCode(),"ETH",HelpUtils.formatDate8(new Date()),888);
//        send(mobile,content);
    }

    /**
     * 返回值的意义：
     * 大于0的数字	提交成功
     * –1	账号未注册
     * –2	网络访问超时，请稍后再试
     * –3	帐号或密码错误
     * -4	只支持单发
     * –5	余额不足，请充值
     * –6	定时发送时间不是有效的时间格式
     * -7	提交信息末尾未签名，请添加中文的企业签名【 】或未采用gb2312编码
     * –8	发送内容需在1到300字之间
     * -9	发送号码为空
     * -10	定时时间不能小于系统当前时间
     * -11	屏蔽手机号码
     * -100	限制IP访问
     *
     * @param mobile  手机号
     * @param content 内容
     */
    public static void send(String mobile, String content) {
        String sendTime = HelpUtils.getCurrTime();
        String result = null;
        String sendUrl = null;
        try {
            content = URLEncoder.encode(content, "GBK");
            sendUrl = String.format(URL,  mobile, content, sendTime);
            result = HttpUtil.get(sendUrl);
            if (result == null) {
                System.out.println(String.format("短信发送异常，手机号：%s，发送url：%s", mobile, sendUrl));
                return;
            }
            if (Integer.valueOf(result.trim()) < 0) {
                System.out.println(String.format("短信发送异常，手机号：%s，发送url：%s", mobile, sendUrl));
            }

        } catch (Exception e) {
            System.out.println(String.format("短信发送异常，手机号：%s，发送url：%s", mobile, sendUrl));
            e.printStackTrace();
        }
    }
}
