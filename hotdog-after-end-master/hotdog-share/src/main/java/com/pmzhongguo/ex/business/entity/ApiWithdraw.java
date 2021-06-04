package com.pmzhongguo.ex.business.entity;


import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class ApiWithdraw extends ReqBaseSecret
{
    @ApiModelProperty(value = "币类型，如BTC、LTC、ETH", required = true)
    private String currency;
    @ApiModelProperty(value = "提币地址", required = true)
    private String addr;
    @ApiModelProperty(value = "提币数量", required = true)
    private BigDecimal amount;


    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public String getAddr()
    {
        return addr;
    }

    public void setAddr(String addr)
    {
        this.addr = addr;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }
}