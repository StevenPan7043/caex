package com.pmzhongguo.ex.business.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;


public class TradeDto implements Serializable
{

    private static final long serialVersionUID = -2805354388611265742L;

    @ApiModelProperty(value = "ID")
    public Long id;
    @ApiModelProperty(value = "时间戳")
    public Long timestamp;
    @ApiModelProperty(value = "成交数量")
    public BigDecimal volume;
    @ApiModelProperty(value = "成交价")
    public BigDecimal price;
    @ApiModelProperty(value = "吃单方")
    public String taker;
    @ApiModelProperty(value = "相对于最近一次价格的涨跌，1涨，-1跌")
    public Integer upOrDown;
    @ApiModelProperty(value = "卖方id,MQ发消息会用到")
    public Long sell_Id;
    @ApiModelProperty(value = "买方订单类型,MQ发消息会用到")
    public Long buy_Id;
    @ApiModelProperty(value = "卖方订单类型,MQ发消息会用到")
    public String sell_Taker;
    @ApiModelProperty(value = "买方订单类型,MQ发消息会用到")
    public String buy_Taker;

//    public String toString()
//    {
//        return id + "-" + timestamp + "-" + volume + "-" + price + "-" + taker + "-" + upOrDown;
//    }


    @Override
    public String toString() {
        return "TradeDto{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", volume=" + volume +
                ", price=" + price +
                ", taker='" + taker + '\'' +
                ", upOrDown=" + upOrDown +
                ", sell_Id=" + sell_Id +
                ", buy_Id=" + buy_Id +
                ", sell_Taker='" + sell_Taker + '\'' +
                ", buy_Taker='" + buy_Taker + '\'' +
                '}';
    }
}