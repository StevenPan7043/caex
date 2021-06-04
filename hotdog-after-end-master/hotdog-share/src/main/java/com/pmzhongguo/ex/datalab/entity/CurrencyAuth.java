package com.pmzhongguo.ex.datalab.entity;

import java.math.BigDecimal;

/**
 * 用户交易对权限
 *
 * @author jary
 * @creatTime 2019/11/29 3:41 PM
 */
public class CurrencyAuth {

    private Integer id;
    /**
     * 用户id
     */
    private Integer memberId;
    /**
     * 交易对id
     */
    private Integer currencyPairId;

    /**
     * 用户名
     */
    private String mName;

    /**
     * 手续费比例
     */
    private BigDecimal feeScale;

    /**
     * 权限
     */
    private Integer authority;

    /**
     * 基础货币提现限额
     */
    private BigDecimal baseWQuota;

    /**
     * 计价货币提现限额
     */
    private BigDecimal valuationWQuota;


    /**
     * 是否冻结：1：冻结，2:正常
     */
    private Integer isFree;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 扩展字段
     */
    private String extend1;

    /**
     * 扩展字段
     */
    private String extend2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getCurrencyPairId() {
        return currencyPairId;
    }

    public void setCurrencyPairId(Integer currencyPairId) {
        this.currencyPairId = currencyPairId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public BigDecimal getFeeScale() {
        return feeScale;
    }

    public void setFeeScale(BigDecimal feeScale) {
        this.feeScale = feeScale;
    }

    public Integer getAuthority() {
        return authority;
    }

    public void setAuthority(Integer authority) {
        this.authority = authority;
    }

    public BigDecimal getBaseWQuota() {
        return baseWQuota;
    }

    public void setBaseWQuota(BigDecimal baseWQuota) {
        this.baseWQuota = baseWQuota;
    }

    public BigDecimal getValuationWQuota() {
        return valuationWQuota;
    }

    public void setValuationWQuota(BigDecimal valuationWQuota) {
        this.valuationWQuota = valuationWQuota;
    }

    public Integer getIsFree() {
        return isFree;
    }

    public void setIsFree(Integer isFree) {
        this.isFree = isFree;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getExtend1() {
        return extend1;
    }

    public void setExtend1(String extend1) {
        this.extend1 = extend1;
    }

    public String getExtend2() {
        return extend2;
    }

    public void setExtend2(String extend2) {
        this.extend2 = extend2;
    }
}
