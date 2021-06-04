package com.pmzhongguo.ex.core.web;

/**
 * @description: 锁仓操作类型枚举
 * @date: 2019-06-11 19:14
 * @author: 十一
 */
public enum  LockOperTypeEnum {

    /**
     *充值/锁住
     */
    LOCK(1),
    /**
     * 释放
     */
    RELEASE(0),
    /**
     * 从释放返回到锁仓
     */
    RETURN(2)

    ;


    private Integer code;

    LockOperTypeEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
