package com.pmzhongguo.ex.business.dto;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class OTCOrderReq
{

    @ApiModelProperty(value = "类型 1表示买入订单，2表示卖出订单")
    public Integer ad_type;
    @ApiModelProperty(value = "数量")
    public BigDecimal volume;
    @ApiModelProperty(value = "广告ID")
    private Integer ads_id;//广告ID
    @ApiModelProperty(value = "资金密码")
    private String sec_pwd;
    @ApiModelProperty(value = "【请仔细阅读说明】账户信息，通过otc/account_info的GET接口获得。<br>用户买入时，只能选一个支付方式，即他付款是什么方式付款的，如bank或alipay或wxpay。<br>用户卖出时，可以选多个方式。取值格式为（即逗号分割多个付款方式，特别注意，如果用户没有绑定该类型账户，不能传入）：bank,alipay,wxpay")
    private String account_types;


    public String getAccount_types()
    {
        return account_types;
    }

    public void setAccount_types(String account_types)
    {
        this.account_types = account_types;
    }

    public Integer getAd_type()
    {
        return ad_type;
    }

    public void setAd_type(Integer ad_type)
    {
        this.ad_type = ad_type;
    }

    public String getSec_pwd()
    {
        return sec_pwd;
    }

    public void setSec_pwd(String sec_pwd)
    {
        this.sec_pwd = sec_pwd;
    }

    public BigDecimal getVolume()
    {
        return volume;
    }

    public void setVolume(BigDecimal volume)
    {
        this.volume = volume;
    }

    public Integer getAds_id()
    {
        return ads_id;
    }

    public void setAds_id(Integer ads_id)
    {
        this.ads_id = ads_id;
    }
}
