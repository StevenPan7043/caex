package com.pmzhongguo.ex.business.entity;


import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class RespMemberTrade
{

    @ApiModelProperty(value = "基础货币 如ETH")
    private String base_currency;

    @ApiModelProperty(value = "计价货币 如BTC")
    private String quote_currency;

    @ApiModelProperty(value = "订单类型：buy 买入、sell 卖出")
    private String t_type;

    @ApiModelProperty(value = "成交价格")
    private BigDecimal price;

    @ApiModelProperty(value = "成交数量")
    private BigDecimal volume;

    @ApiModelProperty(value = "成交时间")
    private String done_time;

    @ApiModelProperty(value = "吃单方，取值为self(当前用户为吃单方)和opposite(对方用户为吃单方)")
    private String taker;

    @ApiModelProperty(value = "成交手续费")
    private BigDecimal fee;

    @ApiModelProperty(value = "手续费货币")
    private String fee_currency;

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
