package com.pmzhongguo.ex.core.web;

public enum LoginDeviceEnum {

    APP(0, "手机端登录", "APP"),
    ANDROID(1, "安卓端登录", "ANDROID"),
    IOS(2, "IOS端登录", "IOS"),
    PC(3, "PC端登录", "PC");

    private Integer type;
    private String codeCn;
    private String codeEn;

    LoginDeviceEnum(Integer type, String codeCn, String codeEn) {
        this.type = type;
        this.codeCn = codeCn;
        this.codeEn = codeEn;
    }

    public Integer getType() {
        return type;
    }

    public String getCodeCn() {
        return codeCn;
    }

    public String getCodeEn() {
        return codeEn;
    }

    public static LoginDeviceEnum findByType(Integer type) {

        switch (type) {
            case 0:
                return APP;
            case 1:
                return ANDROID;
            case 2:
                return IOS;
            case 3:
                return PC;
            default:
                return APP;
        }
    }
}
