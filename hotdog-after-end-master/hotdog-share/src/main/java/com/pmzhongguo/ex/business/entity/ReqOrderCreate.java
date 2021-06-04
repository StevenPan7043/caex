package com.pmzhongguo.ex.business.entity;


import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class ReqOrderCreate
{

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
    @ApiModelProperty(value = "来源，固定值，分别取web、app、api", required = true)
    private String source;
    @ApiModelProperty(value = "资金密码", required = true)
    private String security_pwd;
    @ApiModelProperty(value = "是否2小时内无需再次输入资金密码, 1 是  0否", required = true)
    private Integer is_sec_pwd_effective;

    public Integer getIs_sec_pwd_effective()
    {
        return is_sec_pwd_effective;
    }

    public void setIs_sec_pwd_effective(Integer is_sec_pwd_effective)
    {
        this.is_sec_pwd_effective = is_sec_pwd_effective;
    }

    public String getSecurity_pwd()
    {
        return security_pwd;
    }

    public void setSecurity_pwd(String security_pwd)
    {
        this.security_pwd = security_pwd;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
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