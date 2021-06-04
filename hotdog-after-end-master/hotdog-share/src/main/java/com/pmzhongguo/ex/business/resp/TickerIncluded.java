package com.pmzhongguo.ex.business.resp;

import java.math.BigDecimal;

/**
 * 行情收录类，禁止做他用。
 * @author jary
 * @creatTime 2020/5/14 10:15 AM
 */
public class TickerIncluded {

    /**
     * 交易对（格式：例如BTC_USDT）
     */
    private String symbol;

    /**
     * 买1价
     */
    private BigDecimal buy;

    /**
     * 最高价
     */
    private BigDecimal high;


    /**
     * 最新成交价
     */
    private BigDecimal last;


    /**
     * 最低价
     */
    private BigDecimal low;


    /**
     * 卖1价
     */
    private BigDecimal sell;


    /**
     * 涨跌幅
     */
    private BigDecimal change;

    /**
     * 成交量
     */
    private BigDecimal vol;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getBuy() {
        return buy;
    }

    public void setBuy(BigDecimal buy) {
        this.buy = buy;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLast() {
        return last;
    }

    public void setLast(BigDecimal last) {
        this.last = last;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getSell() {
        return sell;
    }

    public void setSell(BigDecimal sell) {
        this.sell = sell;
    }

    public BigDecimal getChange() {
        return change;
    }

    public void setChange(BigDecimal change) {
        this.change = change;
    }

    public BigDecimal getVol() {
        return vol;
    }

    public void setVol(BigDecimal vol) {
        this.vol = vol;
    }


}
