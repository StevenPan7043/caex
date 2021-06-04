package com.contract.service.wallet.btc.dto;

import java.math.BigDecimal;

public class Fees {

	private BigDecimal fastestFee;
	private BigDecimal halfHourFee;
	private BigDecimal hourFee;
	public BigDecimal getFastestFee() {
		return fastestFee;
	}
	public void setFastestFee(BigDecimal fastestFee) {
		this.fastestFee = fastestFee;
	}
	public BigDecimal getHalfHourFee() {
		return halfHourFee;
	}
	public void setHalfHourFee(BigDecimal halfHourFee) {
		this.halfHourFee = halfHourFee;
	}
	public BigDecimal getHourFee() {
		return hourFee;
	}
	public void setHourFee(BigDecimal hourFee) {
		this.hourFee = hourFee;
	}
	
	
}
