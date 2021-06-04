package com.pmzhongguo.ex.core.web;

/**
 * @author jary
 * @creatTime 2019/9/5 1:53 PM
 * 锁仓规则类型
 */
public enum LockRuleTypeEnum {

    AUTO_RELEASE(1,"AUTO_RELEASE","自动锁仓释放"),
    TRADE(2,"TRADE","交易锁仓");
    private int type;
    private String codeEn;
    private String codeCn;

    LockRuleTypeEnum(int type, String codeEn, String codeCn) {
        this.type = type;
        this.codeEn = codeEn;
        this.codeCn = codeCn;
    }

    public int getType() {
        return type;
    }

    public String getCodeEn() {
        return codeEn;
    }

    public String getCodeCn() {
        return codeCn;
    }
}
