package com.contract.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TeamNode {

    private Integer memberId;//用户ID
    private List<Integer> childId = new ArrayList<>();//子ID
    private BigDecimal bonus;//团队消费金额
    private Integer count;//推荐人数
    private Integer status;//是否已叠加 0否 1是
    private Integer parentId;//父ID
    private BigDecimal rate;//团队总收益

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public List<Integer> getChildId() {
        return childId;
    }

    public void setChildId(List<Integer> childId) {
        this.childId = childId;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
