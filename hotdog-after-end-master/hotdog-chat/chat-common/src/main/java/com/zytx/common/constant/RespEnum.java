package com.zytx.common.constant;

public enum RespEnum {

    FAIL(0, "失败"),
    SUCCESS(1, "成功"),
    NO_USER(2, "非法用户"),
    MESSAGE_EMPTY(3, "无数据"),
    NO_IN_GROUP(4, "不在聊天室"),
    ON_BAN(5, "被禁言"),
    OUT_BAN(6, "解禁");

    private Integer code;
    private String desc;

    RespEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
