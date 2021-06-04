package com.pmzhongguo.ipfs.entity;

import java.util.Date;

public class IpfsTeamBonusDetail {
    private Integer id;

    private Integer teamBonusId;

    private Integer sonMemeberId;

    private String sonLevel;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTeamBonusId() {
        return teamBonusId;
    }

    public void setTeamBonusId(Integer teamBonusId) {
        this.teamBonusId = teamBonusId;
    }

    public Integer getSonMemeberId() {
        return sonMemeberId;
    }

    public void setSonMemeberId(Integer sonMemeberId) {
        this.sonMemeberId = sonMemeberId;
    }

    public String getSonLevel() {
        return sonLevel;
    }

    public void setSonLevel(String sonLevel) {
        this.sonLevel = sonLevel == null ? null : sonLevel.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}