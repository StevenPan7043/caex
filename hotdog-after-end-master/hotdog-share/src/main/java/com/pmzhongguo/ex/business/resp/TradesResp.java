package com.pmzhongguo.ex.business.resp;

import com.pmzhongguo.ex.business.dto.TradeRespDto;
import com.pmzhongguo.ex.core.web.resp.Resp;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


/**
 * 用于返回某个账号的交易shuju
 *
 * @author Administrator
 */
public class TradesResp extends Resp
{
    @ApiModelProperty(value = "订单信息")
    public List<TradeRespDto> trades;
    @ApiModelProperty(value = "总记录数")
    public Integer total;

    public TradesResp(Integer _state, String _msg, List<TradeRespDto> _trades, Integer _total)
    {
        super(_state, _msg);
        trades = _trades;
        total = _total;
    }
}
