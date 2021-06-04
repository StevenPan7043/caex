package com.pmzhongguo.ex.business.entity;

import com.pmzhongguo.lockposition.base.BasePram;

import java.math.BigDecimal;

/**
 * 锁仓账户实体类
 * @author jary
 * @creatTime 2019/7/19 3:26 PM
 */
public class WarehouseAccount extends BasePram {
    /**
     * 会员id
     */
        private Integer memberId;
    /**
     * 账户id
     *
     */
    private Integer accountId;
    /**
     * 币种
     */

    private String currency;

    /**
     *
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
     * 已释放次数
     */
    private Integer warehouseReleaseCount;

    /**
     * 下次释放时间
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
}
