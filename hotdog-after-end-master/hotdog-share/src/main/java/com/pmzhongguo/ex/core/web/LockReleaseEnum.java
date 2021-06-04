package com.pmzhongguo.ex.core.web;

/**
 * @author jary
 * @creatTime 2019/7/20 11:05 AM
 */
public enum LockReleaseEnum {
    /**
     * 释放标识
     */
    ALL(null, "ALL" , "所有"),
    RELEASING(0, "RELEASING", "正在释放"),
    RELEASING_END(1, "RELEASING_END", "释放结束"),
    UNRELEASED(2, "UNRELEASED", "待释放"),
    DELETED(3, "DELETED", "作废"),
    ;

    private Integer type;

    private String codeEn;

    private String codeCn;


    LockReleaseEnum(Integer type, String codeEn, String codeCn) {
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

    public static LockReleaseEnum getEnumByType(int type) {
        for (LockReleaseEnum t : LockReleaseEnum.values()) {
            if (t.getType() == null){
                continue;
            }
            if (t.getType().equals(type)) {
                return t;
            }
        }
        return null;
    }

    public static LockReleaseEnum getEnumByCode(String codeEn) {
        for (LockReleaseEnum t : LockReleaseEnum.values()) {
            if (t.codeEn.equals(codeEn)) {
                return t;
            }
        }
        return null;
    }

}
