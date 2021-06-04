package com.pmzhongguo.ex.business.dto;

import io.swagger.annotations.ApiModelProperty;

public class ReqDelApiKey
{
    private Long id;
    @ApiModelProperty(value = "资金密码", required = true)
    private String security_pwd;
    @ApiModelProperty(value = "谷歌验证码(如果启用了谷歌验证码，需要传此值)", required = false)
    private Integer google_auth_code;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
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