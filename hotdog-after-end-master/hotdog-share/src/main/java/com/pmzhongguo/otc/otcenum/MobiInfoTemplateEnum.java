package com.pmzhongguo.otc.otcenum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author admin
 */
public enum MobiInfoTemplateEnum implements TemplateEnum {

//
//    //	买信息：【ZZEX】您已向#name#用户购买#volu# #base#，出售单价为#pric# #quote#，订单号为#id#，请于#m#分钟内付款。
//    AWS_TRADE_BOUGHT(1, "【ZZEX】您已向%1$s用户购买%2$s %3$s，出售单价为%4$s %5$s，订单号为%6$s，请于%7$s分钟内付款。"),
//    //	卖方信息：【ZZEX】您已向#name#用户出售#volu# #base#，出售单价为#pric# #quote#， 订单号为#id#，等待对方支付。
//    AWS_TRADE_SOLD(2, "【ZZEX】您已向%1$s用户出售%2$s %3$s，出售单价为%4$s %5$s， 订单号为%6$s，等待对方支付。"),
//    //	“已付款” 买方信息：【ZZEX】#name#将订单#id#标记为“已付款”状态，请及时登录收款账户查看并确认放行。
//    AWS_TRADE_PAID(3, "【ZZEX】%1$s将订单%2$s标记为“已付款”状态，请及时登录收款账户查看并确认放行。"),
//    //	“确认放行” 商家信息：【ZZEX】#name#已确认收到您的付款并放行，您所购买的#base#已发放到您的法币账户，请登录账户查收。
//    AWS_TRADE_CONFIRMED(4, "【ZZEX】%1$s已确认收到您的付款并放行，您所购买的%2$s已发放到您的法币账户，请登录账户查收。"),
//    //	【ZZEX】#name#已将订单#id#取消，如有疑问请联系平台客服。
//    AWS_CANCELED_TRADE(5, "【ZZEX】%1$s已将订单%2$s取消，如有疑问请联系平台客服。"),
//    //	【ZZEX】#name#已将订单#id#取消，如有疑问请联系平台客服。
//    AWS_COMPLAINING_TRADE(5, "【ZZEX】%1$s已将订单%2$s发起申诉，平台客服将介入处理，请及时关注。"),
//    AWS_RECHARGE_TRADE(6, "【ZZEX】您的 %1$s 于 %2$s 充值 %3$s 成功。"),
//    AWS_WITHDRAWAL_TRADE(7, "【ZZEX】您的 %1$s 于 %2$s 提现 %3$s 成功。"),


    /**
     * 凌凯短信
//     * xx已将订单xx发起投诉，平台客服将介入处理，请及时关注。
//     * 用户xx对于订单xx发起投诉，请及时处理。
//     * xx已将订单xx取消，如有疑问请联系平台客服。
//     * xx已收到您的付款，您所购买的xx已发送到您的账户，请查收。
//     * xx将订单xx确认为“已付款”状态，请及时登陆账户查看并确认。
//     * 您已向xx用户xx xx，出售单价为xx xx，订单好为xx，等待支付。
//     * 您已向xx用户购买xx xx，购买单价为xx xx，订单号为xx，请于xx分钟内付款。
//     * 您的xx于xx充值xx成功。
//     * 您的xx于xx提现xx成功。
//     * 您的验证码是xx。如非本人操作，请忽略本短信
     */

    JH_TRADE_BOUGHT(100001, "您已向%s用户购买%s %s，购买单价为%s %s，订单号为%s，请于%s分钟内付款。"),
    JH_TRADE_SOLD(100002, "您已向%s用户出售%s %s，出售单价为%s %s，订单号为%s，等待支付。"),
    JH_TRADE_PAID(100003, "%s将订单%s确认为“已付款”状态，请及时登陆账户查看并确认。"),
    JH_TRADE_CONFIRMED(100004, "%s已收到您的付款，您所购买的%s已发送到您的账户，请查收。"),
    JH_CANCELED_TRADE(100005, "%s已将订单%s取消，如有疑问请联系平台客服。"),
    JH_COMPLAINING_TRADE(100006, "%s已将订单%s发起投诉，平台客服将介入处理，请及时关注。"),
    JH_COMPLAINING_TRADE_WARN(100007, "用户%s对于订单%s发起投诉，请及时处理。"),
    JH_SECURITY_CODE(100008, "您的验证码是%s，有效期%s分钟。如非本人操作，请忽略本短信。"),
    //充值短信 【ZZEX】您的 #base# 于 #date# 充值 #volu# 成功。
    JH_RECHARGE_CODE(100009, "您的%s于%s充值%s成功。"),
    //提现短信  【ZZEX】您的 #base# 于 #date# 提现 #volu# 成功。
    JH_WITHDRAWAL_CODE(100010, "您的%s于%s提现%s成功。"),


    //聚合短信模板
    //type就是模板id
//    JH_TRADE_BOUGHT(127972, "#name#=%1$s&#volu#=%2$s&#base#=%3$s&#pric#=%4$s&#quote#=%5$s&#id#=%6$s&#m#=%7$s"),
//    JH_TRADE_SOLD(127970, "#name#=%1$s&#volu#=%2$s&#base#=%3$s&#pric#=%4$s&#quote#=%5$s&#id#=%6$s"),
//    JH_TRADE_PAID(127973, "#name#=%1$s&#id#=%2$s"),
//    JH_TRADE_CONFIRMED(127974, "#name#=%1$s&#base#=%2$s"),
//    JH_CANCELED_TRADE(127975, "#name#=%1$s&#id#=%2$s"),
//    JH_COMPLAINING_TRADE(129999, "#name#=%1$s&#id#=%2$s"),
//    JH_COMPLAINING_TRADE_WARN(132989, "#name#=%1$s&#id#=%2$s"),
//    JH_SECURITY_CODE(135867, "#code#=%1$s&#m#=%2$s"),
//    //充值短信 【ZZEX】您的 #base# 于 #date# 充值 #volu# 成功。
//    JH_RECHARGE_CODE(192267, "#base#=%1$s&#date#=%2$s&#volu#=%3$s"),
//    //提现短信  【ZZEX】您的 #base# 于 #date# 提现 #volu# 成功。
//    JH_WITHDRAWAL_CODE(192268, "#base#=%1$s&#date#=%2$s&#volu#=%3$s"),


    ;

    private int type;
    private String code;
    private static Map<String, MobiInfoTemplateEnum> stringMap = new HashMap<String, MobiInfoTemplateEnum>();

    static {
        for (MobiInfoTemplateEnum a : MobiInfoTemplateEnum.values()) {
            stringMap.put(a.toString(), a);
        }
    }

    private MobiInfoTemplateEnum(int type, String code) {
        this.type = type;
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public static MobiInfoTemplateEnum getAccountOperateTypeEnum(String key) {
        return stringMap.get(key);
    }

    public static MobiInfoTemplateEnum getEnumByType(int type) {
        for (MobiInfoTemplateEnum a : MobiInfoTemplateEnum.values()) {
            if (a.getType() == type) {
                return a;
            }
        }
        return null;
    }
}
