package com.pmzhongguo.ex.business.entity;

import java.math.BigDecimal;

/**
 * 锁仓总资产
 */
public class LockAmount
{
    private Integer id;//订单ID
    private Integer member_id;//会员ID
    private Integer currency_id;//d_currency的id，CNY不会出现在此
    private String currency;//d_currency的currency
    private BigDecimal amount;//总资产=释放的+冻结的

    private BigDecimal unnum;
    private BigDecimal r_release;
    private Integer price_precision;
    private Integer volume_precision;

    public BigDecimal getR_release()
    {
        return r_release;
    }

    public void setR_release(BigDecimal r_release)
    {
        this.r_release = r_release;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
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

    public BigDecimal getUnnum()
    {
        return unnum;
    }

    public void setUnnum(BigDecimal unnum)
    {
        this.unnum = unnum;
    }

    public Integer getPrice_precision()
    {
        return price_precision;
    }

    public void setPrice_precision(Integer price_precision)
    {
        this.price_precision = price_precision;
    }

    public Integer getVolume_precision()
    {
        return volume_precision;
    }

    public void setVolume_precision(Integer volume_precision)
    {
        this.volume_precision = volume_precision;
    }
}
