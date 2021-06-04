package com.pmzhongguo.ex.business.scheduler;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccountRebate implements Serializable {
    //主键
    private Integer id;
    //用户id
    private Integer memberId;
    //币种名
    private String currency;
    //总资产
    private BigDecimal totalBalance;
    //冻结资产
    private BigDecimal frozenBalance;
    //可用资产 = 总资产 - 冻结资产
    private BigDecimal availableBalance;
    //返利资产 = 可用资产 * 倍率
    private BigDecimal rebateBalance;
    //状态，0表示未返利 1表示已返利
    private Integer rStatus;
    //返利种类 0表示静态返利 1表示动态邀请返利
    private Integer rType;
    //介绍人ID 无则为0
    private Integer introduceMId;
    //创建时间
    private String createTime;
    //修改时间
    private String updateTime;

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public BigDecimal getFrozenBalance() {
        return frozenBalance;
    }

    public void setFrozenBalance(BigDecimal frozenBalance) {
        this.frozenBalance = frozenBalance;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public BigDecimal getRebateBalance() {
        return rebateBalance;
    }

    public void setRebateBalance(BigDecimal rebateBalance) {
        this.rebateBalance = rebateBalance;
    }

    public Integer getrStatus() {
        return rStatus;
    }

    public void setrStatus(Integer rStatus) {
        this.rStatus = rStatus;
    }

    public Integer getrType() {
        return rType;
    }

    public void setrType(Integer rType) {
        this.rType = rType;
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
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AccountRebate{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", currency='" + currency + '\'' +
                ", totalBalance=" + totalBalance +
                ", frozenBalance=" + frozenBalance +
                ", availableBalance=" + availableBalance +
                ", rebateBalance=" + rebateBalance +
                ", rStatus=" + rStatus +
                ", rType=" + rType +
                ", introduceMId=" + introduceMId +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
