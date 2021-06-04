package com.pmzhongguo.ipfs.entity;

import java.math.BigDecimal;
import java.util.Date;

public class IpfsTeamBonus {
    private Integer id;

    private Integer memberId;

    private String level;

    private BigDecimal teamBonus;

    private BigDecimal bonusRate;

    private BigDecimal subBonusBase;

    private BigDecimal subHashrateBase;

    private String bonusDate;

    private String memo;

    private Date createTime;

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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public BigDecimal getTeamBonus() {
        return teamBonus;
    }

    public void setTeamBonus(BigDecimal teamBonus) {
        this.teamBonus = teamBonus;
    }

    public BigDecimal getBonusRate() {
        return bonusRate;
    }

    public void setBonusRate(BigDecimal bonusRate) {
        this.bonusRate = bonusRate;
    }

    public BigDecimal getSubBonusBase() {
        return subBonusBase;
    }

    public void setSubBonusBase(BigDecimal subBonusBase) {
        this.subBonusBase = subBonusBase;
    }

    public String getBonusDate() {
        return bonusDate;
    }

    public void setBonusDate(String bonusDate) {
        this.bonusDate = bonusDate == null ? null : bonusDate.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getSubHashrateBase() {
        return subHashrateBase;
    }

    public void setSubHashrateBase(BigDecimal subHashrateBase) {
        this.subHashrateBase = subHashrateBase;
    }
}