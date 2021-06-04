package com.pmzhongguo.ex.business.resp;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @author jary
 * @creatTime 2020/4/20 9:38 AM
 */
public class UpsDownResp {
    @ApiModelProperty(value = "涨跌幅限制是否开启")
    private Integer isUpsDown;
    @ApiModelProperty(value = "最高价")
    private BigDecimal highPrice;
    @ApiModelProperty(value = "最低价")
    private BigDecimal lowPrice;

    public UpsDownResp(Integer isUpsDown, BigDecimal highPrice, BigDecimal lowPrice) {
        this.isUpsDown = isUpsDown;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
    }

    public Integer getIsUpsDown() {
        return isUpsDown;
    }

    public void setIsUpsDown(Integer isUpsDown) {
        this.isUpsDown = isUpsDown;
    }

    public BigDecimal getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(BigDecimal highPrice) {
        this.highPrice = highPrice;
    }

    public BigDecimal getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(BigDecimal lowPrice) {
        this.lowPrice = lowPrice;
    }
}
