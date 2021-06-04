package com.pmzhongguo.ex.business.resp;

import com.pmzhongguo.ex.core.web.resp.Resp;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class DepthResp extends Resp
{

    /**
     *
     */
    private static final long serialVersionUID = -5968748715301713860L;
    @ApiModelProperty(value = "交易对名称，如BTCCNY、LTCCNY、ETHCNY")
    public String symbol;
    @ApiModelProperty(value = "毫秒级生成时间")
    public Long timestamp;
    @ApiModelProperty(value = "买方报价, 按价格降序[[价格1, 数量1], [价格2, 数量2], ...")
    public BigDecimal[][] bids;
    @ApiModelProperty(value = "卖方报价, 按价格升序[[价格1, 数量1], [价格2, 数量2], ...")
    public BigDecimal[][] asks;

    public DepthResp(Integer _state, String _msg, String _symbol, Long _timestamp, BigDecimal[][] _bids, BigDecimal[][] _asks)
    {
        super(_state, _msg);
        bids = _bids;
        asks = _asks;
        symbol = _symbol;
        timestamp = _timestamp;
    }
}
