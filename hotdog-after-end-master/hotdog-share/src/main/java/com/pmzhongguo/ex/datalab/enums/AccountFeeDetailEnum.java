package com.pmzhongguo.ex.datalab.enums;

/**
 * @author jary
 * @creatTime 2019/12/5 11:09 AM
 */
public enum  AccountFeeDetailEnum {

    /**
     * 增加
     */
    INCREASE(1, "increase", "增加"),
    /**
     * 还原
     */
    REDUCTION(2, "reduction", "还原"),
    /**
     * 冻结
     */
    FROZEN(3, "frozen", "冻结"),
    /**
     * 解冻
     */
    THAW(4, "thaw", "解冻");
    private Integer type;

    private String codeEn;

    private String codeCn;

    AccountFeeDetailEnum(Integer type, String codeEn, String codeCn) {
        this.type = type;
        this.codeEn = codeEn;
        this.codeCn = codeCn;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCodeEn() {
        return codeEn;
    }

    public void setCodeEn(String codeEn) {
        this.codeEn = codeEn;
    }

    public String getCodeCn() {
        return codeCn;
    }

    public void setCodeCn(String codeCn) {
        this.codeCn = codeCn;
    }
}
