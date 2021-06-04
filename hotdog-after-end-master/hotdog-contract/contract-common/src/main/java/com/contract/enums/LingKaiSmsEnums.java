package com.contract.enums;

/**
 * @author jary
 * @creatTime 2020/3/13 10:12 AM
 */
public enum LingKaiSmsEnums {
    LK_SMS_CODE(100001, "您的验证码是%s，有效期5分钟。如非本人操作，请忽略本短信。"),
    LK_SMS_FULL_LOSS(100002, "您的全仓合约总保证金亏损已达到%s，请注意仓位风险控制。"),
    LK_SMS_FULL_BURST(100003, "因行情剧烈波动，您的全仓合约已爆仓。"),
    LK_SMS_ZC_LOSS(100004, "您的%s %s逐仓合约保证金亏损已达到100%%,已自动平仓。"),
    LK_SMS_ZC_PROFIT(100005, "您的%s %s逐仓合约保证金盈利已达到100%%,已自动平仓。"),

    ;
    private int type;
    private String code;

    LingKaiSmsEnums(int type, String code) {
        this.type = type;
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
