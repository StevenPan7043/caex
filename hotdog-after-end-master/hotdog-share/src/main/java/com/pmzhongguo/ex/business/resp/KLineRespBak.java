package com.pmzhongguo.ex.business.resp;

import com.pmzhongguo.ex.core.web.resp.Resp;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;


public class KLineRespBak extends Resp
{

    @ApiModelProperty(value = "交易对名称，如BTCCNY、LTCCNY、ETHCNY")
    public String symbol;
    @ApiModelProperty(value = "K线类型，取值1min, 5min, 15min, 30min, 60min, 1day, 1mon, 1week, 1year")
    public String type;
    @ApiModelProperty(value = "毫秒级生成时间")
    public Long timestamp;
    @ApiModelProperty(value = "K线数据, 按时间倒叙[[时间戳1, 开盘价1, 最高价1, 最低价1, 收盘价1, 成交数量1], [时间戳2, 开盘价2, 最高价2, 最低价2, 收盘价2, 成交数量2], ...")
    public List<BigDecimal[]> datas;


    public KLineRespBak(Integer _state, String _msg, String _symbol, String _type, Long _timestamp, List<BigDecimal[]> _datas)
    {
        super(_state, _msg);
        datas = _datas;
        symbol = _symbol;
        type = _type;
        timestamp = _timestamp;
    }
}
