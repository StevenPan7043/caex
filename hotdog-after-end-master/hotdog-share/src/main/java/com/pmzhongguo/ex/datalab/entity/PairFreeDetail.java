package com.pmzhongguo.ex.datalab.entity;

import java.math.BigDecimal;

/**
 * 交易对手续费记录
 *
 * @author jary
 * @creatTime 2019/11/29 3:48 PM
 */
public class PairFreeDetail {

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
     * 交易对
     */
    private String currencyPair;

    /**
     * 交易类型
     */
    private String tType;

    /**
     * 手续费比例
     */
    private BigDecimal feeScale;

    /**
     * 手续费币种
     */
    private String feeCurrency;

    /**
     * 手续费总量
     */
    private BigDecimal feeTotalAmount;

    /**
     * 实际资产
     */
    private BigDecimal realAmount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 统计日期
     */
    private String searchDate;

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

    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }

    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    public String gettType() {
        return tType;
    }

    public void settType(String tType) {
        this.tType = tType;
    }

    public BigDecimal getFeeScale() {
        return feeScale;
    }

    public void setFeeScale(BigDecimal feeScale) {
        this.feeScale = feeScale;
    }

    public String getFeeCurrency() {
        return feeCurrency;
    }

    public void setFeeCurrency(String feeCurrency) {
        this.feeCurrency = feeCurrency;
    }

    public BigDecimal getFeeTotalAmount() {
        return feeTotalAmount;
    }

    public void setFeeTotalAmount(BigDecimal feeTotalAmount) {
        this.feeTotalAmount = feeTotalAmount;
    }



    public void setReaAmount(BigDecimal reaAmount) {
        this.realAmount = reaAmount;
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

    public String getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(String searchDate) {
        this.searchDate = searchDate;
    }
}
