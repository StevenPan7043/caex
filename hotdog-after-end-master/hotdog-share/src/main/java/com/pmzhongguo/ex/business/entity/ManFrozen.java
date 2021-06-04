package com.pmzhongguo.ex.business.entity;


import java.math.BigDecimal;


public class ManFrozen
{
    private Integer id;//
    private Integer member_id;//会员ID
    private Integer currency_id;//d_currency的id，CNY不会出现在此
    private String currency;//货币类型，关联d_currency表的currency字段
    private BigDecimal frozen_balance;//冻结金额
    private String unfrozen_time;//解冻时间
    private Integer f_status;//状态：0未解冻，1已解冻

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

    public BigDecimal getFrozen_balance()
    {
        return frozen_balance;
    }

    public void setFrozen_balance(BigDecimal frozen_balance)
    {
        this.frozen_balance = frozen_balance;
    }

    public String getUnfrozen_time()
    {
        return unfrozen_time;
    }

    public void setUnfrozen_time(String unfrozen_time)
    {
        this.unfrozen_time = unfrozen_time;
    }

    public Integer getF_status()
    {
        return f_status;
    }

    public void setF_status(Integer f_status)
    {
        this.f_status = f_status;
    }
}
