package com.pmzhongguo.ex.core.web;

/**
 * 美国
 * @author jary
 * @creatTime 2019/7/2 2:07 PM
 */
public enum InternationalSmsENEnum {

    // "您的验证码是#code#，有效期#m#分钟"
//    CLT_CODE_TIME(1001, "Your validation code is %1$s, valid for %2$s minutes."),
//    CLT_MEMBERAPPEAL_MEMBER_ORDER(1002, "[ZZEX] Customer %1$s appeals to order %2$s, please handle it in time."),
//    CLT_CODE(1003, "[ZZEX] Your verification code is %1$s. If not operated by myself, please ignore this short message."),
//    CLT_ZZEXAPPEAL_MEMBER_ORDER(1004, "[ZZEX] %1$s has appealed the order %2$s and the platform customer service will intervene. Please pay attention to it in time."),
//    CLT_CANCEL_MEMBER_ORDER(1005, "[ZZEX] %1$s has cancelled the order %2$s. If you have any questions, please contact the platform customer service."),
//    CLT_MEMBER_CURRENCY(1006, "[ZZEX] %1$s has confirmed receipt of your payment and released. The %2$s you purchased has been issued to your French currency account. Please login to the account for checking."),
//    CLT_PAY_MEMBER_ORDER(1007, "[ZZEX] %1$s marks the order %2$s as \"paid\" status. Please login to the account in time to check and confirm the release."),
//    CLT_BUY_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER_TIME(1008, "[ZZEX] You have purchased %2$s %3$s from %1$s user. The unit price is %4$s %5$s and the order number is %6$s. Please pay within minutes of %7$s."),
//    CLT_SELL_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER(1009, "[ZZEX] You have sold %1$s %2$s %3$s to %4$s users at a unit price of %5$s %6$s and an order number of %7$s waiting for payment.");

    JUHE_CODE_TIME(10997, "Your validation code is #code#, valid for #m# minutes.","#code#=%1$s&#m#=%2$s");

    private Integer type;

    private String cnCode;

    private String formatCode;


    InternationalSmsENEnum(Integer type, String cnCode, String formatCode) {
        this.type = type;
        this.cnCode = cnCode;
        this.formatCode = formatCode;
    }

    public String getFormatCode() {
        return formatCode;
    }

    public Integer getType() {
        return type;
    }

    public String getCnCode() {
        return cnCode;
    }

    public static InternationalSmsENEnum getEnumByType(int type) {
        for (InternationalSmsENEnum o : InternationalSmsENEnum.values()) {
            if (o.getType() == type) {
                return o;
            }
        }
        return null;
    }

    public static InternationalSmsENEnum getEnumByCode(String code) {
        for (InternationalSmsENEnum o : InternationalSmsENEnum.values()) {
            if (o.getCnCode().equals(code)) {
                return o;
            }
        }
        return null;
    }
}
