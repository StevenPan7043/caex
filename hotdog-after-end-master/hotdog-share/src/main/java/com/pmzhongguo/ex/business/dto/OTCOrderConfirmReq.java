package com.pmzhongguo.ex.business.dto;

import io.swagger.annotations.ApiModelProperty;

public class OTCOrderConfirmReq
{

    @ApiModelProperty(value = "订单ID，")
    public Long id;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }
}
