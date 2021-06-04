package com.pmzhongguo.ex.business.entity;

/**
 * 设置交易返还手续费
 *
 * @author zengweixiong
 */
public class SetTradeReturnFee
{

    private Integer id;
    private String rate;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getRate()
    {
        return rate;
    }

    public void setRate(String rate)
    {
        this.rate = rate;
    }

    @Override
    public String toString()
    {
        return "SetTradeReturnFee [id=" + id + ", rate=" + rate + "]";
    }

}
