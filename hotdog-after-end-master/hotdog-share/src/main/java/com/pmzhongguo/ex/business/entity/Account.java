package com.pmzhongguo.ex.business.entity;

import com.pmzhongguo.zzextool.utils.JsonUtil;

import java.math.BigDecimal;


public class Account
{
    private Integer id;//
    private Integer member_id;//会员ID
    private String currency;//货币类型，关联d_currency表的currency字段
    private BigDecimal total_balance;//总余额，可交易余额 = 总余额 - 冻结余额
    private BigDecimal frozen_balance;//冻结余额，如有挂单未成交，则有冻结余额
    private BigDecimal available_balance; // 可用余额，总余额-冻结余额

    public BigDecimal getAvailable_balance()
    {
        return available_balance;
    }

    public void setAvailable_balance(BigDecimal available_balance)
    {
        this.available_balance = available_balance;
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

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public BigDecimal getTotal_balance()
    {
        return total_balance;
    }

    public void setTotal_balance(BigDecimal total_balance)
    {
        this.total_balance = total_balance;
    }

    public BigDecimal getFrozen_balance()
    {
        return frozen_balance;
    }

    public void setFrozen_balance(BigDecimal frozen_balance)
    {
        this.frozen_balance = frozen_balance;
    }

    @Override
    public String toString()
    {
        String result = "";
        try
        {
            result = JsonUtil.beanToJson(this);
        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}
