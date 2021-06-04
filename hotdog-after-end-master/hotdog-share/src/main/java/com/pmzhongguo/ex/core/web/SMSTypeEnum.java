package com.pmzhongguo.ex.core.web;

/**
 * @description: 短信发送类型枚举
 * @date: 2018-12-19 15:03
 * @author: 十一
 */
public enum SMSTypeEnum {


    /**
     * 登录使用
     */
    LOGIN("login"),

    /**
     * 注册使用
     */
    REG("reg"),

    /**
     * 忘记密码使用
     */
    FORGOT("forgot"),

    /**
     * 短信校验使用
     */
    CHECK("check"),

    ;


    private String msg;

    SMSTypeEnum(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

}
