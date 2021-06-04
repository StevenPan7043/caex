package com.pmzhongguo.ex.business.entity;


import java.math.BigDecimal;

/**
 * @description: 锁仓账号明细
 * @date: 2019-06-03 10:22
 * @author: 十一
 */
public class LockAccountDetail
{

    private Integer id;//
    private Integer member_id;//会员ID
    private String currency;//货币类型，关联d_currency表的currency字段
    private BigDecimal oper_num_before;//锁仓账号操作前数量
    private BigDecimal oper_num;//操作数量
    private String create_time;
    private Integer m_type;//操作类型,0:释放，1锁住

    public LockAccountDetail(Integer member_id, String currency, BigDecimal oper_num_before, BigDecimal oper_num, String create_time, Integer m_type)
    {
        this.member_id = member_id;
        this.currency = currency;
        this.oper_num_before = oper_num_before;
        this.oper_num = oper_num;
        this.create_time = create_time;
        this.m_type = m_type;
    }

    public LockAccountDetail()
    {
    }

    public Integer getType()
    {
        return m_type;
    }

    public void setType(Integer type)
    {
        this.m_type = type;
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

    public BigDecimal getOper_num_before()
    {
        return oper_num_before;
    }

    public void setOper_num_before(BigDecimal oper_num_before)
    {
        this.oper_num_before = oper_num_before;
    }

    public BigDecimal getOper_num()
    {
        return oper_num;
    }

    public void setOper_num(BigDecimal oper_num)
    {
        this.oper_num = oper_num;
    }

    public String getCreate_time()
    {
        return create_time;
    }

    public void setCreate_time(String create_time)
    {
        this.create_time = create_time;
    }
}
