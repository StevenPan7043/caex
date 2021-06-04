package com.pmzhongguo.ex.business.entity;

import java.math.BigDecimal;


public class Trade
{
    private Long id;//
    private Integer token_id;//api_token表的ID，从网站或APP来的订单填0，其他填token_id，便于用户做统计
    private Integer member_id;//会员ID
    private Integer pair_id;//交易对ID
    private String base_currency;//基础货币，如BTC
    private String quote_currency;//计价货币，如CNY
    private String t_type;//订单类型：buy买入、sell卖出
    private Long o_id;//自身订单ID
    private Long opposite_o_id;//对方订单ID
    private BigDecimal price;//成交价格
    private BigDecimal volume;//成交数量
    private String done_time;//成交时间
    private String taker;//吃单方，取值为self和opposite
    private BigDecimal fee;//成交手续费，卖出方为计价货币，买入方为基础货币
    private String fee_currency;//手续费货币


    private String table_name; //表名
    private String m_name;


    public String getM_name()
    {
        return m_name;
    }

    public void setM_name(String m_name)
    {
        this.m_name = m_name;
    }

    public Integer getToken_id()
    {
        return token_id;
    }

    public void setToken_id(Integer token_id)
    {
        this.token_id = token_id;
    }

    public String getTable_name()
    {
        return table_name;
    }

    public void setTable_name(String table_name)
    {
        this.table_name = table_name;
    }

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

    public Integer getPair_id()
    {
        return pair_id;
    }

    public void setPair_id(Integer pair_id)
    {
        this.pair_id = pair_id;
    }

    public String getBase_currency()
    {
        return base_currency;
    }

    public void setBase_currency(String base_currency)
    {
        this.base_currency = base_currency;
    }

    public String getQuote_currency()
    {
        return quote_currency;
    }

    public void setQuote_currency(String quote_currency)
    {
        this.quote_currency = quote_currency;
    }

    public String getT_type()
    {
        return t_type;
    }

    public void setT_type(String t_type)
    {
        this.t_type = t_type;
    }

    public Long getO_id()
    {
        return o_id;
    }

    public void setO_id(Long o_id)
    {
        this.o_id = o_id;
    }

    public Long getOpposite_o_id()
    {
        return opposite_o_id;
    }

    public void setOpposite_o_id(Long opposite_o_id)
    {
        this.opposite_o_id = opposite_o_id;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public BigDecimal getVolume()
    {
        return volume;
    }

    public void setVolume(BigDecimal volume)
    {
        this.volume = volume;
    }

    public String getDone_time()
    {
        return done_time;
    }

    public void setDone_time(String done_time)
    {
        this.done_time = done_time;
    }

    public String getTaker()
    {
        return taker;
    }

    public void setTaker(String taker)
    {
        this.taker = taker;
    }

    public BigDecimal getFee()
    {
        return fee;
    }

    public void setFee(BigDecimal fee)
    {
        this.fee = fee;
    }

    public String getFee_currency()
    {
        return fee_currency;
    }

    public void setFee_currency(String fee_currency)
    {
        this.fee_currency = fee_currency;
    }

}
