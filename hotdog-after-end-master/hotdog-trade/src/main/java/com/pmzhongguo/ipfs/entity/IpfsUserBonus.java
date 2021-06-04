package com.pmzhongguo.ipfs.entity;

import java.math.BigDecimal;
import java.util.Date;

public class IpfsUserBonus {
    private Integer id;

    private Integer hashrateId;

    private Integer projectId;

    private String projectCode;

    private Integer memberId;

    private String outputCurrency;

    private BigDecimal bonusNum;

    private BigDecimal fee;

    private String bonusDate;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHashrateId() {
        return hashrateId;
    }

    public void setHashrateId(Integer hashrateId) {
        this.hashrateId = hashrateId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode == null ? null : projectCode.trim();
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getOutputCurrency() {
        return outputCurrency;
    }

    public void setOutputCurrency(String outputCurrency) {
        this.outputCurrency = outputCurrency == null ? null : outputCurrency.trim();
    }

    public BigDecimal getBonusNum() {
        return bonusNum;
    }

    public void setBonusNum(BigDecimal bonusNum) {
        this.bonusNum = bonusNum;
    }

    public String getBonusDate() {
        return bonusDate;
    }

    public void setBonusDate(String bonusDate) {
        this.bonusDate = bonusDate == null ? null : bonusDate.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    @Override
    public String toString() {
        return "IpfsUserBonus{" +
                "id=" + id +
                ", hashrateId=" + hashrateId +
                ", projectCode='" + projectCode + '\'' +
                ", memberId=" + memberId +
                ", outputCurrency='" + outputCurrency + '\'' +
                ", bonusNum=" + bonusNum +
                ", bonusDate='" + bonusDate + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}