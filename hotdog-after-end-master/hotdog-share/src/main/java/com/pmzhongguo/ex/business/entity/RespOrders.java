package com.pmzhongguo.ex.business.entity;

import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.zzextool.utils.DateUtils;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class RespOrders extends Resp
{
    @ApiModelProperty(value = "交易对")
    public String symbol;
    @ApiModelProperty(value = "时间戳")
    public Long timestamp;
    @ApiModelProperty(value = "订单信息")
    public List<RespOrderBase> orders;
    @ApiModelProperty(value = "总记录数")
    public Long total;


    public RespOrders(Integer _state, String _msg, String _symbol, List<RespOrderBase> _orders, Long _total)
    {
        super(_state, _msg);
        symbol = _symbol;
        timestamp = DateUtils.getNowTimeStampMillisecond();
        orders = _orders;
        total = _total;
    }
}
