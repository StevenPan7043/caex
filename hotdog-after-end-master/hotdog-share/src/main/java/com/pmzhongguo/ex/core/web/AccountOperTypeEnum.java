package com.pmzhongguo.ex.core.web;

/**
 * @description: 账户资产操作类型 枚举
 * @date: 2019-06-03 11:51
 * @author: 十一
 */
public enum AccountOperTypeEnum
{


    /**
     * 冻结资产
     */
    FROZEN_BALANCE(1),
    /**
     * 解冻资产
     */
    UNFROZEN_BALANCE(2),
    /**
     * 添加资产
     */
    ADD_BALANCE(3),
    /**
     * 还原冻结资产
     */
    RETURN_FROZEN_BALANCE(4),
    ;

    private Integer code;

    AccountOperTypeEnum(Integer type) {
        this.code = type;
    }

    public Integer getCode() {
        return code;
    }
}
