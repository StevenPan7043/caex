package com.pmzhongguo.ex.business.dto;

import io.swagger.annotations.ApiModelProperty;

public class WithdrawCreateDtoInner extends WithdrawCreateDto
{
    @ApiModelProperty(value = "邮箱/短信验证码", required = true)
    private String sms_code;
    @ApiModelProperty(value = "资金密码", required = true)
    private String m_security_pwd;
    @ApiModelProperty(value = "谷歌验证码", required = false)
    private Integer google_auth_code;
    @ApiModelProperty(value = "token，APP需提供", required = false)
    private String token;


    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public Integer getGoogle_auth_code()
    {
        return google_auth_code;
    }

    public void setGoogle_auth_code(Integer google_auth_code)
    {
        this.google_auth_code = google_auth_code;
    }

    public String getSms_code()
    {
        return sms_code;
    }

    public void setSms_code(String sms_code)
    {
        this.sms_code = sms_code;
    }

    public String getM_security_pwd()
    {
        return m_security_pwd;
    }

    public void setM_security_pwd(String m_security_pwd)
    {
        this.m_security_pwd = m_security_pwd;
    }

}