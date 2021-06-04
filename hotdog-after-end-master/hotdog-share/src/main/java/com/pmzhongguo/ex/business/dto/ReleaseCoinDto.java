package com.pmzhongguo.ex.business.dto;

import io.swagger.annotations.ApiModelProperty;

public class ReleaseCoinDto
{

    @ApiModelProperty(value = "货币id")
    private Integer currencyId;
    @ApiModelProperty(value = "释放计划，动态拼接，例如：第一个月:10%▲第二个月:20%，月与月之间用'▲'隔开，月与比例之间用':'隔开")
    private String releasePlan;

    public Integer getCurrencyId()
    {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId)
    {
        this.currencyId = currencyId;
    }

    public String getReleasePlan()
    {
        return releasePlan;
    }

    public void setReleasePlan(String releasePlan)
    {
        this.releasePlan = releasePlan;
    }
}
