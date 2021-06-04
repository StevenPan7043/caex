package com.pmzhongguo.ex.business.dto;


import io.swagger.annotations.ApiModelProperty;

public class WithdrawCancelDto
{

    @ApiModelProperty(value = "提现申请ID", required = true)
    private long id;
    @ApiModelProperty(value = "token，APP需提供", required = true)
    private String token;


    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }
}