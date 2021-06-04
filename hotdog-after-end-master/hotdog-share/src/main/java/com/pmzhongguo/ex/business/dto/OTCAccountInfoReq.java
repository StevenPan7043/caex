package com.pmzhongguo.ex.business.dto;

import io.swagger.annotations.ApiModelProperty;

public class OTCAccountInfoReq
{
    @ApiModelProperty(value = "资金密码")
    private String sec_pwd;//广告ID
    @ApiModelProperty(value = "类型：bank/alipay/wxpay 只能取以上三项，含义： bank银行卡,alipay支付宝,wxpay微信")
    private String a_type;//
    @ApiModelProperty(value = "真实姓名")
    private String a_name;//姓名
    @ApiModelProperty(value = "账号（银行卡号或微信支付宝账号）")
    private String a_account;//账号（银行卡号或微信支付宝账号）
    @ApiModelProperty(value = "银行卡开户行或微信支付宝的二维码图片")
    private String a_bank_or_img;//银行卡开户行或微信支付宝的二维码图片


    public String getSec_pwd()
    {
        return sec_pwd;
    }

    public void setSec_pwd(String sec_pwd)
    {
        this.sec_pwd = sec_pwd;
    }

    public String getA_type()
    {
        return a_type;
    }

    public void setA_type(String a_type)
    {
        this.a_type = a_type;
    }

    public String getA_name()
    {
        return a_name;
    }

    public void setA_name(String a_name)
    {
        this.a_name = a_name;
    }

    public String getA_account()
    {
        return a_account;
    }

    public void setA_account(String a_account)
    {
        this.a_account = a_account;
    }

    public String getA_bank_or_img()
    {
        return a_bank_or_img;
    }

    public void setA_bank_or_img(String a_bank_or_img)
    {
        this.a_bank_or_img = a_bank_or_img;
    }
}
