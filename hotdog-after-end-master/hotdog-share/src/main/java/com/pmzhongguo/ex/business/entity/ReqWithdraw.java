package com.pmzhongguo.ex.business.entity;


import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class ReqWithdraw
{
    @ApiModelProperty(value = "币类型，如BTC、LTC、ETH", required = true)
    private String currency;
    @ApiModelProperty(value = "提币地址", required = true)
    private String addr;
    @ApiModelProperty(value = "提币地址标签，如果填写了，就存储到提现地址列表，否则不存储", required = false)
    private String addr_label;
    @ApiModelProperty(value = "提币数量", required = true)
    private BigDecimal amount;
    @ApiModelProperty(value = "邮箱/短信验证码", required = true)
    private String sms_code;
    @ApiModelProperty(value = "资金密码", required = true)
    private String security_pwd;
    @ApiModelProperty(value = "谷歌验证码(如果启用)", required = false)
    private Integer google_auth_code;

    public String getAddr_label()
    {
        return addr_label;
    }

    public void setAddr_label(String addr_label)
    {
        this.addr_label = addr_label;
    }

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

    public String getSms_code()
    {
        return sms_code;
    }

    public void setSms_code(String sms_code)
    {
        this.sms_code = sms_code;
    }

    public String getSecurity_pwd()
    {
        return security_pwd;
    }

    public void setSecurity_pwd(String security_pwd)
    {
        this.security_pwd = security_pwd;
    }

    public Integer getGoogle_auth_code()
    {
        return google_auth_code;
    }

    public void setGoogle_auth_code(Integer google_auth_code)
    {
        this.google_auth_code = google_auth_code;
    }
}