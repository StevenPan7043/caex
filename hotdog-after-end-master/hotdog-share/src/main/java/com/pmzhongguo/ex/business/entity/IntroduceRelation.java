package com.pmzhongguo.ex.business.entity;

public class IntroduceRelation {
    private Integer id;

    private Integer memberId;

    private Integer introduceMId;

    private String createTime;

    private String memo;

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

    public Integer getIntroduceMId() {
        return introduceMId;
    }

    public void setIntroduceMId(Integer introduceMId) {
        this.introduceMId = introduceMId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}