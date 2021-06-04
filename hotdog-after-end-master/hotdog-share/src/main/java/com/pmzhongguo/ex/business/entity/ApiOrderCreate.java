package com.pmzhongguo.ex.business.entity;


import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class ApiOrderCreate extends ReqBaseSecret
{
    @ApiModelProperty(value = "订单编号，在当前交易对需唯一", required = true)
    private String o_no;
    @ApiModelProperty(value = "交易对名称，如BTCCNY、LTCCNY、ETHCNY", required = true)
    private String symbol;
    @ApiModelProperty(value = "订单类型：buy、sell，分别代表卖出、买入", required = true)
    private String o_type;
    @ApiModelProperty(value = "价格类型：limit、market，分别代表限价单、市价单", required = true)
    private String o_price_type;
    @ApiModelProperty(value = "价格，对限价单，表示买入/卖出价格，对于市价单，请填0", required = true)
    private BigDecimal price;
    @ApiModelProperty(value = "数量，对限价单，表示买入/卖出数量，对于市价买单，表示买入多少计价货币(如CNY)，市价卖单表示卖出多少基础货币(如BTC)", required = true)
    private BigDecimal volume;


    public String getO_no()
    {
        return o_no;
    }

    public void setO_no(String o_no)
    {
        this.o_no = o_no;
    }

    public String getSymbol()
    {
        return symbol;
    }

    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }

    public String getO_type()
    {
        return o_type;
    }

    public void setO_type(String o_type)
    {
        this.o_type = o_type;
    }

    public String getO_price_type()
    {
        return o_price_type;
    }

    public void setO_price_type(String o_price_type)
    {
        this.o_price_type = o_price_type;
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
}