package com.pmzhongguo.ex.business.resp;

import com.pmzhongguo.ex.business.dto.OrderRespDto;
import com.pmzhongguo.ex.core.web.resp.Resp;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


public class OrdersResp extends Resp
{
    @ApiModelProperty(value = "订单信息")
    public List<OrderRespDto> orders;
    @ApiModelProperty(value = "总记录数")
    public Integer total;


    public OrdersResp(Integer _state, String _msg, List<OrderRespDto> _orders, Integer _total)
    {
        super(_state, _msg);
        orders = _orders;
        total = _total;
    }
}
