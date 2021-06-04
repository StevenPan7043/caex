package com.pmzhongguo.ex.business.entity;

import java.math.BigDecimal;

/**
 * @description: 锁仓账号
 * @date: 2019-06-03 10:22
 * @author: 十一
 */
public class LockAccount
{

    private Integer id;//
    private Integer member_id;//会员ID
    private String currency;//货币类型，关联d_currency表的currency字段
    private BigDecimal lock_num;//锁仓数量
    private String create_time;
    private String update_time;

    public LockAccount(Integer member_id, String currency, BigDecimal lock_num, String create_time, String update_time)
    {
        this.member_id = member_id;
        this.currency = currency;
        this.lock_num = lock_num;
        this.create_time = create_time;
        this.update_time = update_time;
    }

    public LockAccount()
    {
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

    public BigDecimal getLock_num()
    {
        return lock_num;
    }

    public void setLock_num(BigDecimal lock_num)
    {
        this.lock_num = lock_num;
    }

    public String getCreate_time()
    {
        return create_time;
    }

    public void setCreate_time(String create_time)
    {
        this.create_time = create_time;
    }

    public String getUpdate_time()
    {
        return update_time;
    }

    public void setUpdate_time(String update_time)
    {
        this.update_time = update_time;
    }
}
