package com.pmzhongguo.ex.core.web;

/**
 * @author jary
 * @creatTime 2019/7/20 11:05 AM
 */
public enum ReleaseTypeEnum {


    RECHARGE(1, "RECHARGE", "充值"),
    RELEASE(2, "RELEASE", "释放"),
    REDUCTION(3, "REDUCTION", "还原"),
    DELETED(4, "DELETED", "作废"),
    ;

    private Integer type;

    private String codeEn;
    private String codeCn;

    ReleaseTypeEnum(Integer type, String codeEn, String codeCn) {
        this.type = type;
        this.codeEn = codeEn;
        this.codeCn = codeCn;
    }

    public Integer getType() {
        return type;
    }


    public static ReleaseTypeEnum getEnumByType(int type) {
        for (ReleaseTypeEnum t : ReleaseTypeEnum.values()) {
            if (t.getType() == type) {
                return t;
            }
        }
        return null;
    }

    public static ReleaseTypeEnum getEnumByCodeEn(String code) {
        for (ReleaseTypeEnum t : ReleaseTypeEnum.values()) {
            if (t.codeEn.equals(code)) {
                return t;
            }
        }
        return null;
    }

    public String getCodeEn() {
        return codeEn;
    }

    public String getCodeCn() {
        return codeCn;
    }
}
