package com.pmzhongguo.ex.business.entity;

import java.math.BigDecimal;

/**
 * 我的锁仓
 */
public class LockList
{
    private Long id;//订单ID
    private Integer member_id;//会员ID
    private Integer currency_id;//d_currency的id，CNY不会出现在此
    private String currency;//d_currency的currency
    private BigDecimal amount;// 锁仓数量
    private int undata; //到期时间，时间戳，精确到秒
    private int locktime; //锁仓时间，时间戳，精确到秒
    private int l_type; //锁仓时长，1：1个月，3：3个月
    private int l_status; //是否已释放，0：未释放，1：已释放


    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getMember_id()
    {
        return member_id;
    }

    public void setMember_id(Integer member_id)
    {
        this.member_id = member_id;
    }

    public Integer getCurrency_id()
    {
        return currency_id;
    }

    public void setCurrency_id(Integer currency_id)
    {
        this.currency_id = currency_id;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }


    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public int getUndata()
    {
        return undata;
    }

    public void setUndata(int undata)
    {
        this.undata = undata;
    }

    public int getLocktime()
    {
        return locktime;
    }

    public void setLocktime(int locktime)
    {
        this.locktime = locktime;
    }

    public int getL_type()
    {
        return l_type;
    }

    public void setL_type(int l_type)
    {
        this.l_type = l_type;
    }

    public int getL_status()
    {
        return l_status;
    }

    public void setL_status(int l_status)
    {
        this.l_status = l_status;
    }
}
