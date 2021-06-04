package com.pmzhongguo.ex.business.entity;


import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class OTCAds
{
    @ApiModelProperty(value = "广告ID")
    private Integer id;//广告ID
    @ApiModelProperty(value = "广告主ID")
    private Integer owner_id;//广告主ID
    @ApiModelProperty(value = "广告主姓名")
    private String o_name;//广告主姓名
    @ApiModelProperty(value = "广告类型，1买入，2卖出，这个都是从普通用户角度出发的买入卖出")
    private Integer ad_type;//广告类型，1买入，2卖出
    @ApiModelProperty(value = "基础货币，如BTC")
    private String base_currency;//基础货币，如BTC
    @ApiModelProperty(value = "计价货币，如CNY")
    private String quote_currency;//计价货币，如CNY
    @ApiModelProperty(value = "价格")
    private BigDecimal price;//价格
    @ApiModelProperty(value = "最小交易额")
    private BigDecimal min_quote;//最小交易额
    @ApiModelProperty(value = "最大交易额")
    private BigDecimal max_quote;//最大交易额
    @ApiModelProperty(value = "银行卡信息，为空表示不支持，格式：户名▲卡号▲开户行")
    private String bank_info;//银行卡信息
    @ApiModelProperty(value = "支付宝信息，为空表示不支持，格式：户名▲卡号")
    private String alipay_info;//支付宝信息
    @ApiModelProperty(value = "微信信息，为空表示不支持，格式：户名▲卡号")
    private String wxpay_info;//微信信息
    @ApiModelProperty(value = "总成交数量")
    private BigDecimal total_volume;//总成交数量
    @ApiModelProperty(value = "总成交金额")
    private BigDecimal total_amount;//总成交金额
    @ApiModelProperty(value = "平均放行时间")
    private Integer avg_time;//平均放行时间
    @ApiModelProperty(value = "最后在线时间")
    private String last_time;//最后在线时间
    @ApiModelProperty(value = "状态：1：正常，0：暂停显示，已经显示到前台的均为正常")
    private Integer c_status;//状态：1：正常，0：暂停显示
    private Integer a_order; //排序，越小越前


    public Integer getA_order()
    {
        return a_order;
    }

    public void setA_order(Integer a_order)
    {
        this.a_order = a_order;
    }

    public String getLast_time()
    {
        return last_time;
    }

    public void setLast_time(String last_time)
    {
        this.last_time = last_time;
    }

    public Integer getAvg_time()
    {
        return avg_time;
    }

    public void setAvg_time(Integer avg_time)
    {
        this.avg_time = avg_time;
    }

    public String getO_name()
    {
        return o_name;
    }

    public void setO_name(String o_name)
    {
        this.o_name = o_name;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getOwner_id()
    {
        return owner_id;
    }

    public void setOwner_id(Integer owner_id)
    {
        this.owner_id = owner_id;
    }

    public Integer getAd_type()
    {
        return ad_type;
    }

    public void setAd_type(Integer ad_type)
    {
        this.ad_type = ad_type;
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

    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public BigDecimal getMin_quote()
    {
        return min_quote;
    }

    public void setMin_quote(BigDecimal min_quote)
    {
        this.min_quote = min_quote;
    }

    public BigDecimal getMax_quote()
    {
        return max_quote;
    }

    public void setMax_quote(BigDecimal max_quote)
    {
        this.max_quote = max_quote;
    }

    public String getBank_info()
    {
        return bank_info;
    }

    public void setBank_info(String bank_info)
    {
        this.bank_info = bank_info;
    }

    public String getAlipay_info()
    {
        return alipay_info;
    }

    public void setAlipay_info(String alipay_info)
    {
        this.alipay_info = alipay_info;
    }

    public String getWxpay_info()
    {
        return wxpay_info;
    }

    public void setWxpay_info(String wxpay_info)
    {
        this.wxpay_info = wxpay_info;
    }

    public BigDecimal getTotal_volume()
    {
        return total_volume;
    }

    public void setTotal_volume(BigDecimal total_volume)
    {
        this.total_volume = total_volume;
    }

    public BigDecimal getTotal_amount()
    {
        return total_amount;
    }

    public void setTotal_amount(BigDecimal total_amount)
    {
        this.total_amount = total_amount;
    }

    public Integer getC_status()
    {
        return c_status;
    }

    public void setC_status(Integer c_status)
    {
        this.c_status = c_status;
    }
}
