package com.pmzhongguo.ex.business.entity;

/**
 * @author jary
 * @creatTime 2019/7/19 3:35 PM
 */
public class Rule {

    private Integer id;

    /**
     * 父id
     */
    private Integer parentId;

    private String currency;

    /**
     * 释放比例
     */
    private String ruleReleaseScale;
    /**
     * 规则类型
     */
    private String ruleType;
    /**
     * 规则明细
     */
    private String ruleDetail;
    /**
     * 0:启用；1：禁用
     */
    private Integer enable;
    /**
     * 释放间隔时长
     */
    private Integer lockReleaseTime;

    /**
     * 释放时间单位
     */
    private String ruleTimeType;

    public Integer getLockReleaseTime() {
        return lockReleaseTime;
    }

    public void setLockReleaseTime(Integer lockReleaseTime) {
        this.lockReleaseTime = lockReleaseTime;
    }

    /**
     * 锁仓币 0：否；1：是
     */
    private Integer isLock;

    public Rule() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getRuleDetail() {
        return ruleDetail;
    }

    public void setRuleDetail(String ruleDetail) {
        this.ruleDetail = ruleDetail;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRuleReleaseScale() {
        return ruleReleaseScale;
    }

    public void setRuleReleaseScale(String ruleReleaseScale) {
        this.ruleReleaseScale = ruleReleaseScale;
    }

    public Integer getIsLock() {
        return isLock;
    }

    public void setIsLock(Integer isLock) {
        this.isLock = isLock;
    }

    public String getRuleTimeType() {
        return ruleTimeType;
    }

    public void setRuleTimeType(String ruleTimeType) {
        this.ruleTimeType = ruleTimeType;
    }
}
