package com.pmzhongguo.ex.core.web;

public enum IdentificationTypeEnum {
    PASSPORT(2,"PASSPORT","护照"),
    DRIVING_LICENSE(3,"DRIVING_LICENSE","驾照"),
    OTHER(99,"OTHER","其他身份证明证件类型");

    private int type;
    private String codeEn;
    private String codeCn;

    IdentificationTypeEnum(Integer type, String codeEn, String codeCn) {
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
}
