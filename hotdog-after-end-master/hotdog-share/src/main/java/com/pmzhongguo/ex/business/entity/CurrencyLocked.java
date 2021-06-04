package com.pmzhongguo.ex.business.entity;

/**
 * 我的锁仓
 */
public class CurrencyLocked
{
    private Long id;//订单ID

    private Integer currency_name;//d_currency的name

    private String currency;//d_currency的currency

    private String lockdata;

    private String unlockpre;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getCurrency_name()
    {
        return currency_name;
    }

    public void setCurrency_name(Integer currency_name)
    {
        this.currency_name = currency_name;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public String getLockdata()
    {
        return lockdata;
    }

    public void setLockdata(String lockdata)
    {
        this.lockdata = lockdata;
    }

    public String getUnlockpre()
    {
        return unlockpre;
    }

    public void setUnlockpre(String unlockpre)
    {
        this.unlockpre = unlockpre;
    }
}
