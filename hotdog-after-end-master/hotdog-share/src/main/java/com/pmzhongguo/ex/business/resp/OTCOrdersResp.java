package com.pmzhongguo.ex.business.resp;

import com.pmzhongguo.ex.core.web.resp.Resp;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


public class OTCOrdersResp extends Resp
{
    @ApiModelProperty(value = "订单信息")
    public List<OTCOrder> orders;
    @ApiModelProperty(value = "总记录数")
    public Integer total;


    public OTCOrdersResp(Integer _state, String _msg, List<OTCOrder> _orders, Integer _total)
    {
        super(_state, _msg);
        orders = _orders;
        total = _total;
    }
}
