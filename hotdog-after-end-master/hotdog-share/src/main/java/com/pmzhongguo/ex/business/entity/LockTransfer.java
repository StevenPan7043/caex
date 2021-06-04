package com.pmzhongguo.ex.business.entity;

import java.math.BigDecimal;

/**
 * 锁仓划转
 */
public class LockTransfer
{
    private Long id;//订单ID
    private Integer member_id;//会员ID
    private String currency;//d_currency的currency
    private BigDecimal transfer_num;//数量
    private String create_time;//充值时间
    private String origin;// 该交易转入账户
    private String target;// 该交易转出账户
    private String t_txid;//交易ID

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

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public BigDecimal getTransfer_num()
    {
        return transfer_num;
    }

    public void setTransfer_num(BigDecimal transfer_num)
    {
        this.transfer_num = transfer_num;
    }


    public String getCreate_time()
    {
        return create_time;
    }

    public void setCreate_time(String create_time)
    {
        this.create_time = create_time;
    }

    public String getOrigin()
    {
        return origin;
    }

    public void setOrigin(String origin)
    {
        this.origin = origin;
    }

    public String getTarget()
    {
        return target;
    }

    public void setTarget(String target)
    {
        this.target = target;
    }

    public String getT_txid()
    {
        return t_txid;
    }

    public void setT_txid(String t_txid)
    {
        this.t_txid = t_txid;
    }
}
