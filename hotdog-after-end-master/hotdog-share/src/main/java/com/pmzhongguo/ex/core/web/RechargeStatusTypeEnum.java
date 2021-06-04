package com.pmzhongguo.ex.core.web;

/**
 * @description: 充值表状态确认
 * @date: 2019-09-24
 * @author: 十一
 */
public enum RechargeStatusTypeEnum {


    /**
     * 待付款[OTC]
     */
    WAIT_PAY(-1),
    /**
     * 未确认
     */
    UNCONFIRMED(0),
    /**
     * 已确认
     */
    CONFIRMED(1),
    /**
     * 已取消
     */
    CANCELLED(2),
    /**
     * 回写待确认
     */
    RECHARGE_WAIT_CONFIRM(3),
    ;

    private Integer code;

    RechargeStatusTypeEnum(Integer type) {
        this.code = type;
    }

    public Integer getCode() {
        return code;
    }
}
