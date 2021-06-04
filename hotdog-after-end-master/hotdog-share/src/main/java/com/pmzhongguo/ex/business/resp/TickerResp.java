package com.pmzhongguo.ex.business.resp;

import com.pmzhongguo.ex.core.web.resp.Resp;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Arrays;

public class TickerResp extends Resp
{
    /**
     *
     */
    private static final long serialVersionUID = -7102382357831885068L;
    @ApiModelProperty(value = "交易对显示名称，如BTC/CNY、LTC/CNY、ETH/CNY")
    public String dspName;
    @ApiModelProperty(value = "交易对名称，如BTCCNY、LTCCNY、ETHCNY")
    public String symbol;
    @ApiModelProperty(value = "毫秒级生成时间")
    public Long timestamp;
    @ApiModelProperty(value = "24小时内开盘价")
    public BigDecimal open;
    @ApiModelProperty(value = "24小时内最高价")
    public BigDecimal high;
    @ApiModelProperty(value = "24小时内最低价")
    public BigDecimal low;
    @ApiModelProperty(value = "现价")
    public BigDecimal close;

    @ApiModelProperty(value = "CNY现价")
    public BigDecimal cnyClose;

    @ApiModelProperty(value = "24小时内成交量")
    public BigDecimal volume;
    @ApiModelProperty(value = "买一[价格, 数量]")
    public BigDecimal[] bis1;
    @ApiModelProperty(value = "卖一[价格, 数量]")
    public BigDecimal[] ask1;
    @ApiModelProperty(value = "相对于最近一次价格的涨跌，1涨，-1跌")
    public Integer upOrDown;
    @ApiModelProperty(value = "价格小数位，如人民币，最多到分，即2位")
    public Integer pricePrecision;
    @ApiModelProperty(value = "买卖数量小数位，如比特币，最小可交易0.0001个，即4位")
    public Integer volumePrecision;
    @ApiModelProperty(value = "排序")
    public Integer order;
    @ApiModelProperty(value = "交易区")
    public Integer areaId;
    @ApiModelProperty(value = "是否可买入")
    public Integer canBuy;
    @ApiModelProperty(value = "是否可卖出")
    public Integer canSell;
    @ApiModelProperty(value = "暂停原因")
    public String stopDesc;
    @ApiModelProperty(value = "计价货币名")
    public String quoteCurrencyName;
    @ApiModelProperty(value = "基础货币名")
    public String baseCurrencyName;
    @ApiModelProperty(value = "最小买入量")
    public BigDecimal minBuyVolume;
    @ApiModelProperty(value = "最小买入金额")
    public BigDecimal minBuyAmount;
    @ApiModelProperty(value = "最小卖出量")
    public BigDecimal minSellVolume;
    @ApiModelProperty(value = "固定买入价")
    public BigDecimal fixedBuyPrice;
    @ApiModelProperty(value = "是否可充值")
    public Integer canRecharge;
    @ApiModelProperty(value = "状态码：1可用 2 已关闭 3即将开启")
    public Integer pStatus;

    public TickerResp()
    {
        super(null, null);
    }

    public TickerResp(Integer _state, String _msg, String _symbol, Long _timestamp,
                      BigDecimal _open,
                      BigDecimal _high,
                      BigDecimal _low,
                      BigDecimal _close,
                      BigDecimal _cnyClose,
                      BigDecimal _volume,
                      BigDecimal[] _bis1,
                      BigDecimal[] _ask1,
                      String _dspName,
                      Integer _upOrDown,
                      Integer _pricePrecision,
                      Integer _volumePrecision,
                      Integer _order,
                      Integer _areaId,
                      Integer _canBuy,
                      Integer _canSell,
                      String _stopDesc,
                      String _quoteCurrencyName,
                      String _baseCurrencyName,
                      BigDecimal _minBuyVolume,
                      BigDecimal _minBuyAmount,
                      BigDecimal _minSellVolume,
                      BigDecimal _fixedBuyPrice,
                      Integer _canRecharge,
                      Integer p_status
    )
    {
        super(_state, _msg);
        symbol = _symbol;
        timestamp = _timestamp;
        open = _open;
        high = _high;
        low = _low;
        close = _close;
        cnyClose = _cnyClose;
        volume = _volume;
        bis1 = _bis1;
        ask1 = _ask1;
        dspName = _dspName;
        upOrDown = _upOrDown;
        pricePrecision = _pricePrecision;
        volumePrecision = _volumePrecision;
        order = _order;
        areaId = _areaId;
        canBuy = _canBuy;
        canSell = _canSell;
        stopDesc = _stopDesc;
        quoteCurrencyName = _quoteCurrencyName;
        baseCurrencyName = _baseCurrencyName;
        minBuyVolume = _minBuyVolume;
        minBuyAmount = _minBuyAmount;
        minSellVolume = _minSellVolume;
        fixedBuyPrice = _fixedBuyPrice;
        canRecharge = _canRecharge;
        this.pStatus = p_status;
    }

    public BigDecimal getClose()
    {
        return close;
    }

    public Integer getpStatus() {
        return pStatus;
    }

    public void setpStatus(Integer pStatus) {
        this.pStatus = pStatus;
    }

    @Override
    public String toString() {
        return "TickerResp{" +
                "dspName='" + dspName + '\'' +
                ", symbol='" + symbol + '\'' +
                ", timestamp=" + timestamp +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", cnyClose=" + cnyClose +
                ", volume=" + volume +
                ", bis1=" + Arrays.toString(bis1) +
                ", ask1=" + Arrays.toString(ask1) +
                ", upOrDown=" + upOrDown +
                ", pricePrecision=" + pricePrecision +
                ", volumePrecision=" + volumePrecision +
                ", order=" + order +
                ", areaId=" + areaId +
                ", canBuy=" + canBuy +
                ", canSell=" + canSell +
                ", stopDesc='" + stopDesc + '\'' +
                ", quoteCurrencyName='" + quoteCurrencyName + '\'' +
                ", baseCurrencyName='" + baseCurrencyName + '\'' +
                ", minBuyVolume=" + minBuyVolume +
                ", minBuyAmount=" + minBuyAmount +
                ", minSellVolume=" + minSellVolume +
                ", fixedBuyPrice=" + fixedBuyPrice +
                ", canRecharge=" + canRecharge +
                ", pStatus=" + pStatus +
                '}';
    }
}
