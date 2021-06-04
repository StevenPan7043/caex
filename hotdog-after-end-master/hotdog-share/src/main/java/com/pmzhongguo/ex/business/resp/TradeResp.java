package com.pmzhongguo.ex.business.resp;

import com.pmzhongguo.ex.business.dto.TradeDto;
import com.pmzhongguo.ex.core.web.resp.Resp;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 用于返回所有交易数据
 *
 * @author Administrator
 */
public class TradeResp extends Resp
{

    /**
     *
     */
    private static final long serialVersionUID = 7520097826702200129L;
    @ApiModelProperty(value = "交易对名称，如BTCCNY、LTCCNY、ETHCNY")
    public String symbol;
    @ApiModelProperty(value = "毫秒级生成时间")
    public Long timestamp;
    @ApiModelProperty(value = "交易数据, 按时间倒叙")
    public List<TradeDto> trades;

    public TradeResp(Integer _state, String _msg, String _symbol, Long _timestamp, List<TradeDto> _trades)
    {
        super(_state, _msg);
        trades = _trades;
        symbol = _symbol;
        timestamp = _timestamp;
    }
}
