package com.pmzhongguo.ex.business.resp;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;


public class OTCOrder
{

    @ApiModelProperty(value = "id")
    public String id;
    @ApiModelProperty(value = "广告ID")
    public Integer ad_id;
    @ApiModelProperty(value = "类型 1表示买入订单，2表示卖出订单")
    public Integer ad_type;
    @ApiModelProperty(value = "价格")
    public BigDecimal price;
    @ApiModelProperty(value = "数量")
    public BigDecimal volume;
    @ApiModelProperty(value = "金额")
    public BigDecimal amount;
    @ApiModelProperty(value = "状态：-1 待付款（仅对买入有效），0待处理，1已完成，2已取消")
    public Integer status;
    @ApiModelProperty(value = "下单时间")
    public String create_time;
    @ApiModelProperty(value = "对方姓名")
    public String owner_name;
    @ApiModelProperty(value = "基础货币")
    public String base_currency;
    @ApiModelProperty(value = "计价货币")
    public String quote_currency;


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

    public Integer getAd_id()
    {
        return ad_id;
    }

    public void setAd_id(Integer ad_id)
    {
        this.ad_id = ad_id;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Integer getAd_type()
    {
        return ad_type;
    }

    public void setAd_type(Integer ad_type)
    {
        this.ad_type = ad_type;
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

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getCreate_time()
    {
        return create_time;
    }

    public void setCreate_time(String create_time)
    {
        this.create_time = create_time;
    }

    public String getOwner_name()
    {
        return owner_name;
    }

    public void setOwner_name(String owner_name)
    {
        this.owner_name = owner_name;
    }
}
