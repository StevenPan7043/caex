package com.pmzhongguo.ex.business.entity;


import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class RespAccount
{

    @ApiModelProperty(value = "货币代码，如BTC")
    private String currency;
    @ApiModelProperty(value = "总余额。可交易余额 = 总余额 - 冻结余额 ")
    private BigDecimal total_balance;
    @ApiModelProperty(value = "冻结余额，如有挂单未成交，则有冻结余额")
    private BigDecimal frozen_balance;
    @ApiModelProperty(value = "可用余额，总余额-冻结余额")
    private BigDecimal available_balance;

    public BigDecimal getAvailable_balance()
    {
        return available_balance;
    }

    public void setAvailable_balance(BigDecimal available_balance)
    {
        this.available_balance = available_balance;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public BigDecimal getTotal_balance()
    {
        return total_balance;
    }

    public void setTotal_balance(BigDecimal total_balance)
    {
        this.total_balance = total_balance;
    }

    public BigDecimal getFrozen_balance()
    {
        return frozen_balance;
    }

    public void setFrozen_balance(BigDecimal frozen_balance)
    {
        this.frozen_balance = frozen_balance;
    }
}
