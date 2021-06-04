package com.pmzhongguo.ex.business.entity;

import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.zzextool.utils.DateUtils;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class RespMemberTrades extends Resp
{
    @ApiModelProperty(value = "交易对")
    public String symbol;
    @ApiModelProperty(value = "时间戳")
    public Long timestamp;
    @ApiModelProperty(value = "成交记录")
    public List<RespMemberTrade> trades;
    @ApiModelProperty(value = "总记录数")
    public Long total;


    public RespMemberTrades(Integer _state, String _msg, String _symbol, List<RespMemberTrade> _trades, Long _total)
    {
        super(_state, _msg);
        symbol = _symbol;
        timestamp = DateUtils.getNowTimeStampMillisecond();
        trades = _trades;
        total = _total;
    }
}
