package com.pmzhongguo.ex.core.web;


/**
 * 中国
 * @author jary
 * @creatTime 2019/7/2 2:07 PM
 */
public enum InternationalSmsCNEnum {
//    CLT_CODE_TIME(1001, "您的验证码是%1$s，有效期%2$s分钟"),
//    CLT_MEMBERAPPEAL_MEMBER_ORDER(1002, "【ZZEX】用户%1$s对订单%2$s提出申诉，请及时处理。"),
//    CLT_CODE(1003, "【ZZEX】您的验证码是%1$s。如非本人操作，请忽略本短信"),
//    CLT_ZZEXAPPEAL_MEMBER_ORDER(1004, "【ZZEX】%1$s已将订单%2$s发起申诉，平台客服将介入处理，请及时关注。"),
//    CLT_CANCEL_MEMBER_ORDER(1005, "【ZZEX】%1$s已将订单%2$s取消，如有疑问请联系平台客服。"),
//    CLT_MEMBER_CURRENCY(1006, "【ZZEX】%1$s已确认收到您的付款并放行，您所购买的%2$s已发放到您的法币账户，请登录账户查收。"),
//    CLT_PAY_MEMBER_ORDER(1007, "【ZZEX】%1$s将订单%2$s标记为“已付款”状态，请及时登录收款账户查看并确认放行。"),
//    CLT_BUY_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER_TIME(1008, "【ZZEX】您已向%1$s用户购买%2$s %3$s，购买单价为%4$s %5$s，订单号为%6$s，请于%7$s分钟内付款。"),
//    CLT_SELL_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER(1009, "【ZZEX】您已向%1$s用户出售%2$s %3$s，出售单价为%4$s %5$s，订单号为%6$s，等待对方支付。");

    JUHE_CODE_TIME(10996, "您的验证码是#code#，有效期#m#分钟","#code#=%1$s&#m#=%2$s");

    private Integer type;

    private String cnCode;

    private String formatCode;


    InternationalSmsCNEnum(Integer type, String cnCode, String formatCode) {
        this.type = type;
        this.cnCode = cnCode;
        this.formatCode = formatCode;
    }

    public Integer getType() {
        return type;
    }

    public String getCnCode() {
        return cnCode;
    }

    public String getFormatCode() {
        return formatCode;
    }

    public static InternationalSmsCNEnum getEnumByType(int type) {
        for (InternationalSmsCNEnum o : InternationalSmsCNEnum.values()) {
            if (o.getType() == type) {
                return o;
            }
        }
        return null;
    }

    public static InternationalSmsCNEnum getEnumByCode(String code) {
        for (InternationalSmsCNEnum o : InternationalSmsCNEnum.values()) {
            if (o.getCnCode().equals(code)) {
                return o;
            }
        }
        return null;
    }
}
