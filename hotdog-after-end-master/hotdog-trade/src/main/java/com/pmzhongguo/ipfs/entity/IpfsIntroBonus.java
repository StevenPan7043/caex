package com.pmzhongguo.ipfs.entity;

import java.math.BigDecimal;
import java.util.Date;

public class IpfsIntroBonus {
    private Integer id;

    private Integer projectId;

    private String projectCode;

    private Integer hashrateId;

    private Integer memberId;

    private Integer introId;

    private String bonusCurrency;

    private BigDecimal bonusNum;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getHashrateId() {
        return hashrateId;
    }

    public void setHashrateId(Integer hashrateId) {
        this.hashrateId = hashrateId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getIntroId() {
        return introId;
    }

    public void setIntroId(Integer introId) {
        this.introId = introId;
    }

    public String getBonusCurrency() {
        return bonusCurrency;
    }

    public void setBonusCurrency(String bonusCurrency) {
        this.bonusCurrency = bonusCurrency == null ? null : bonusCurrency.trim();
    }

    public BigDecimal getBonusNum() {
        return bonusNum;
    }

    public void setBonusNum(BigDecimal bonusNum) {
        this.bonusNum = bonusNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}