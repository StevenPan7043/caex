package com.pmzhongguo.ex.business.entity;

import com.pmzhongguo.lockposition.base.BasePram;

import java.math.BigDecimal;

/**
 * 锁仓明细实体类
 *
 * @author jary
 * @creatTime 2019/7/22 9:52 AM
 */
public class WarehouseDetail extends BasePram {

    private Integer memberId;

    private Integer whAccountId;

    private String currency;

    private Integer type;


    /**
     * 释放标识，释放成功后保存该字段，其他状态为空。
     */
    private String uniqueDetail;

    /**
     * 锁仓未释放资产
     */
    private BigDecimal preAccount;

    /**
     * 本次释放资产
     */
    private BigDecimal flotAccount;


    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getWhAccountId() {
        return whAccountId;
    }

    public void setWhAccountId(Integer whAccountId) {
        this.whAccountId = whAccountId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getPreAccount() {
        return preAccount;
    }

    public void setPreAccount(BigDecimal preAccount) {
        this.preAccount = preAccount;
    }

    public BigDecimal getFlotAccount() {
        return flotAccount;
    }

    public void setFlotAccount(BigDecimal flotAccount) {
        this.flotAccount = flotAccount;
    }

    public String getUniqueDetail() {
        return uniqueDetail;
    }

    public void setUniqueDetail(String uniqueDetail) {
        this.uniqueDetail = uniqueDetail;
    }
}
