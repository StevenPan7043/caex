package com.pmzhongguo.ex.core.web;


/**
 * 港澳台
 * @author jary
 * @creatTime 2019/7/2 2:07 PM
 */
public enum InternationalSmsHKEnum {
//    CLT_CODE_TIME(1001, "您的驗證碼是%1$s，有效期%2$s分鐘。"),
//    CLT_MEMBERAPPEAL_MEMBER_ORDER(1002, "【ZZEX】用戶%1$s對訂單%2$s提出申訴，請及時處理。"),
//    CLT_CODE(1003, "【ZZEX】您的驗證碼是%1$s。如非本人操作，請忽略本簡訊。"),
//    CLT_ZZEXAPPEAL_MEMBER_ORDER(1004, "【ZZEX】%1$s已將訂單%2$s發起申訴，平臺客服將介入處理，請及時關注。"),
//    CLT_CANCEL_MEMBER_ORDER(1005, "【ZZEX】%1$s已將訂單%2$s取消，如有疑問請聯系平臺客服。"),
//    CLT_MEMBER_CURRENCY(1006, "【ZZEX】%1$s已確認收到您的付款並放行，您所購買的%2$s已發放到您的法幣帳戶，請登入帳戶查收。"),
//    CLT_PAY_MEMBER_ORDER(1007, "【ZZEX】%1$s將訂單%2$s標記為“已付款”狀態，請及時登入收款帳戶查看並確認放行。"),
//    CLT_BUY_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER_TIME(1008, "【ZZEX】您已向%1$s用戶購買%2$s %3$s，購買單價為%4$s %5$s，訂單號為%6$s，請於%7$s分鐘內付款。"),
//    CLT_SELL_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER(1009, "【ZZEX】您已向%1$s用戶出售%2$s %3$s，出售單價為%4$s %5$s，訂單號為%6$s，等待對方支付。");

    JUHE_CODE_TIME(10998, "您的驗證碼是#code#，有效期#m#分鐘。","#code#=%1$s&#m#=%2$s");

    private Integer type;

    private String cnCode;
    private String formatCode;

    InternationalSmsHKEnum(Integer type, String cnCode, String formatCode) {
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
    public static InternationalSmsHKEnum getEnumByType(int type) {
        for (InternationalSmsHKEnum o : InternationalSmsHKEnum.values()) {
            if (o.getType() == type) {
                return o;
            }
        }
        return null;
    }

    public static InternationalSmsHKEnum getEnumByCode(String code) {
        for (InternationalSmsHKEnum o : InternationalSmsHKEnum.values()) {
            if (o.getCnCode().equals(code)) {
                return o;
            }
        }
        return null;
    }
}
