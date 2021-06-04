package com.pmzhongguo.ex.business.dto;

import io.swagger.annotations.ApiModelProperty;

public class TradeRespDto
{
    @ApiModelProperty(value = "交易对名称，如BTCCNY、LTCCNY、ETHCNY")
    private String symbol;

    @ApiModelProperty(value = "基础货币")
    private String base_currency;
    @ApiModelProperty(value = "计价货币")
    private String quote_currency;

    @ApiModelProperty(value = "订单ID")
    private String o_id;

    @ApiModelProperty(value = "订单类型：buy、sell，即卖单、买单")
    private String t_type;

    @ApiModelProperty(value = "成交数量")
    private String volume;

    @ApiModelProperty(value = "成交价格")
    private String price;

    @ApiModelProperty(value = "成交额 = 成交数量 * 成交价格")
    private String amount;

    @ApiModelProperty(value = "手续费，卖出方为计价货币，买入方为基础货币")
    private String fee;

    @ApiModelProperty(value = "手续费货币，如BTC/LTC")
    private String fee_currency;

    @ApiModelProperty(value = "成交时间")
    private String done_time;

    @ApiModelProperty(value = "吃单方，self表示本单为吃单方，opposite表示对方为吃单方")
    private String taker;

    @ApiModelProperty(value = "订单号")
    private String o_no;


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

    public String getFee_currency()
    {
        return fee_currency;
    }

    public void setFee_currency(String fee_currency)
    {
        this.fee_currency = fee_currency;
    }

    public String getSymbol()
    {
        return symbol;
    }

    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }

    public String getO_id()
    {
        return o_id;
    }

    public void setO_id(String o_id)
    {
        this.o_id = o_id;
    }

    public String getT_type()
    {
        return t_type;
    }

    public void setT_type(String t_type)
    {
        this.t_type = t_type;
    }

    public String getVolume()
    {
        return volume;
    }

    public void setVolume(String volume)
    {
        this.volume = volume;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getAmount()
    {
        return amount;
    }

    public void setAmount(String amount)
    {
        this.amount = amount;
    }

    public String getFee()
    {
        return fee;
    }

    public void setFee(String fee)
    {
        this.fee = fee;
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

    public String getO_no() {
        return o_no;
    }

    public void setO_no(String o_no) {
        this.o_no = o_no;
    }
}
