package com.pmzhongguo.ex.business.dto;

import io.swagger.annotations.ApiModelProperty;

public class ReqGenApiKey
{
    @ApiModelProperty(value = "标签", required = true)
    private String label;
    @ApiModelProperty(value = "IP地址白名单", required = false)
    private String trusted_ip;
    @ApiModelProperty(value = "API权限，以逗号分割多个(前端使用复选框展示给用户，若无需该功能，可不展示，但该参数需传递后面的目前支持中指定的字符串)，目前支持 Accounts,Order,Withdraw，分别对应 查询账户资产/委托交易/充值提币", required = true)
    private String api_privilege;
    @ApiModelProperty(value = "邮箱/短信验证码", required = true)
    private String sms_code;
    @ApiModelProperty(value = "资金密码", required = true)
    private String security_pwd;
    @ApiModelProperty(value = "谷歌验证码(如果启用了谷歌验证码，需要传此值)", required = false)
    private Integer google_auth_code;

    public String getApi_privilege()
    {
        return api_privilege;
    }

    public void setApi_privilege(String api_privilege)
    {
        this.api_privilege = api_privilege;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getTrusted_ip()
    {
        return trusted_ip;
    }

    public void setTrusted_ip(String trusted_ip)
    {
        this.trusted_ip = trusted_ip;
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