package com.pmzhongguo.ex.business.dto;

import io.swagger.annotations.ApiModelProperty;

public class ReqModifyApiKey extends ReqGenApiKey
{
    @ApiModelProperty(value = "id", required = true)
    private Integer id;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }
}