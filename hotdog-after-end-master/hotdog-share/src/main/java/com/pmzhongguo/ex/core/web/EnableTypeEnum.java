package com.pmzhongguo.ex.core.web;

/**
 * @author jary
 * @creatTime 2019/7/20 11:05 AM
 */
public enum EnableTypeEnum {

    /**
     * 启用标识
     */
    ENABLE(0,"ENABLE","启用"),
    UN_ENABLE(1,"UN_ENABLE","未启用"),
    ;

    private Integer type;

    private String codeEn;

    private String codeCn;


    EnableTypeEnum(Integer type, String codeEn, String codeCn) {
        this.type = type;
        this.codeEn = codeEn;
        this.codeCn = codeCn;
    }

    public Integer getType() {
        return type;
    }

    public String getCodeEn() {
        return codeEn;
    }

    public String getCodeCn() {
        return codeCn;
    }

    public static EnableTypeEnum getEnumByType(int type) {
        for (EnableTypeEnum t : EnableTypeEnum.values()) {
            if (t.getType() == type) {
                return t;
            }
        }
        return null;
    }

    public static EnableTypeEnum getEnumByCode(String codeEn) {
        for (EnableTypeEnum t : EnableTypeEnum.values()) {
            if (t.codeEn.equals(codeEn)) {
                return t;
            }
        }
        return null;
    }

}
