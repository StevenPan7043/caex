package com.pmzhongguo.ex.business.vo;

import java.math.BigDecimal;

/**
 * @author jary
 * @creatTime 2019/5/15 2:56 PM
 */
public class AccountVo {
    private Integer id;//
    private Integer memberId;//会员ID
    private String currency;//货币类型，关联d_currency表的currency字段
    private BigDecimal totalBalance;//总余额，可交易余额 = 总余额 - 冻结余额
    private BigDecimal frozenBalance;//冻结余额，如有挂单未成交，则有冻结余额
    private BigDecimal availableBalance; // 可用余额，总余额-冻结余额

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
}
