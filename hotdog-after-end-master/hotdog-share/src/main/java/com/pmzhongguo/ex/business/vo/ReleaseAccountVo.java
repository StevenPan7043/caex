package com.pmzhongguo.ex.business.vo;

import com.pmzhongguo.ex.business.entity.ApiFundTransfer;

/**
 * @author jary
 * @creatTime 2019/8/2 9:45 AM
 */
public class ReleaseAccountVo extends ApiFundTransfer {


    /**
     * 充值id
     */
    private Integer coinRechargeId;


    private Integer memberId;



    public Integer getCoinRechargeId() {
        return coinRechargeId;
    }

    public void setCoinRechargeId(Integer coinRechargeId) {
        this.coinRechargeId = coinRechargeId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }
}
