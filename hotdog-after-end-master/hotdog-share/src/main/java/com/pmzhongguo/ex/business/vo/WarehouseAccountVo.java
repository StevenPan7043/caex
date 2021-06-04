package com.pmzhongguo.ex.business.vo;


import com.pmzhongguo.lockposition.base.BasePram;

import java.math.BigDecimal;

/**
 * @author jary
 * @creatTime 2019/7/19 3:26 PM
 */

public class WarehouseAccountVo extends BasePram {
    /**
     * 会员id
     */
    private Integer memberId;
    /**
     * 账户id
     */
    private Integer accountId;
    /**
     * 币种
     */
    private String currency;


    /**
     * 充值地址
     */
    private String rAddress;


    private Integer type;
    /**
     * 锁仓总资产
     */
    private BigDecimal warehouseAmount;

    /**
     * 锁仓已释放资产
     */
    private BigDecimal warehouseReleaseAmount;

    /**
     * 释放次数
     */
    private Integer warehouseCount;


    /**
     * 用户充值id
     */
    private Integer coinRechargeId;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 已释放次数
     */
    private Integer warehouseReleaseCount;

    /**
     * 下次释放时间，时间戳字符串
     */
    private String nextReleaseTime;

    /**
     * 释放规则
     */
    private String ruleIds;

    /**
     * 是否停止释放，0：否，1:是
     */
    private Integer isRelease;


    private BigDecimal everyAmount;

    /**
     * 释放标识，释放成功后保存该字段，其他状态为空。
     */
    private String uniqueDetail;

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getWarehouseAmount() {
        return warehouseAmount;
    }

    public void setWarehouseAmount(BigDecimal warehouseAmount) {
        this.warehouseAmount = warehouseAmount;
    }

    public BigDecimal getWarehouseReleaseAmount() {
        return warehouseReleaseAmount;
    }

    public void setWarehouseReleaseAmount(BigDecimal warehouseReleaseAmount) {
        this.warehouseReleaseAmount = warehouseReleaseAmount;
    }

    public Integer getWarehouseCount() {
        return warehouseCount;
    }

    public void setWarehouseCount(Integer warehouseCount) {
        this.warehouseCount = warehouseCount;
    }

    public Integer getWarehouseReleaseCount() {
        return warehouseReleaseCount;
    }

    public void setWarehouseReleaseCount(Integer warehouseReleaseCount) {
        this.warehouseReleaseCount = warehouseReleaseCount;
    }

    public String getNextReleaseTime() {
        return nextReleaseTime;
    }

    public void setNextReleaseTime(String nextReleaseTime) {
        this.nextReleaseTime = nextReleaseTime;
    }

    public String getRuleIds() {
        return ruleIds;
    }

    public void setRuleIds(String ruleIds) {
        this.ruleIds = ruleIds;
    }

    public Integer getIsRelease() {
        return isRelease;
    }

    public void setIsRelease(Integer isRelease) {
        this.isRelease = isRelease;
    }

    public String getRAddress() {
        return rAddress;
    }

    public void setRAddress(String rAddress) {
        this.rAddress = rAddress;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public BigDecimal getEveryAmount() {
        return everyAmount;
    }

    public void setEveryAmount(BigDecimal everyAmount) {
        this.everyAmount = everyAmount;
    }

    public Integer getType() {
        return type;
    }

    public Integer getCoinRechargeId() {
        return coinRechargeId;
    }

    public void setCoinRechargeId(Integer coinRechargeId) {
        this.coinRechargeId = coinRechargeId;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUniqueDetail() {
        return uniqueDetail;
    }

    public void setUniqueDetail(String uniqueDetail) {
        this.uniqueDetail = uniqueDetail;
    }

    @Override
    public String toString() {
        return "WarehouseAccountVo{" +
                "memberId=" + memberId +
                ", accountId=" + accountId +
                ", currency='" + currency + '\'' +
                ", rAddress='" + rAddress + '\'' +
                ", type=" + type +
                ", warehouseAmount=" + warehouseAmount +
                ", warehouseReleaseAmount=" + warehouseReleaseAmount +
                ", warehouseCount=" + warehouseCount +
                ", createBy='" + createBy + '\'' +
                ", warehouseReleaseCount=" + warehouseReleaseCount +
                ", nextReleaseTime='" + nextReleaseTime + '\'' +
                ", ruleIds='" + ruleIds + '\'' +
                ", isRelease=" + isRelease +
                ", everyAmount=" + everyAmount +
                ", uniqueDetail=" + uniqueDetail +
                '}';
    }
}
