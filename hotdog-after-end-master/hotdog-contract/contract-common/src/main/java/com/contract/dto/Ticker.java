package com.contract.dto;

import java.math.BigDecimal;

public class Ticker {
	private BigDecimal last;//实时价
	private BigDecimal buy;//买一价
	private BigDecimal sell;//卖一价
	private BigDecimal high;//最高
	private BigDecimal low;//最低
	private BigDecimal vol;//成交量
	public BigDecimal getLast() {
		return last;
	}
	public void setLast(BigDecimal last) {
		this.last = last;
	}
	public BigDecimal getBuy() {
		return buy;
	}
	public void setBuy(BigDecimal buy) {
		this.buy = buy;
	}
	public BigDecimal getSell() {
		return sell;
	}
	public void setSell(BigDecimal sell) {
		this.sell = sell;
	}
	public BigDecimal getHigh() {
		return high;
	}
	public void setHigh(BigDecimal high) {
		this.high = high;
	}
	public BigDecimal getLow() {
		return low;
	}
	public void setLow(BigDecimal low) {
		this.low = low;
	}
	public BigDecimal getVol() {
		return vol;
	}
	public void setVol(BigDecimal vol) {
		this.vol = vol;
	}
	
}
