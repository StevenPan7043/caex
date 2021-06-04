package com.contract.dto;

import java.math.BigDecimal;

public class TxDto {

	private String toaddr;
	private BigDecimal coin;
	private String tarhash;
	private String hash;
	public String getToaddr() {
		return toaddr;
	}
	public void setToaddr(String toaddr) {
		this.toaddr = toaddr;
	}
	public BigDecimal getCoin() {
		return coin;
	}
	public void setCoin(BigDecimal coin) {
		this.coin = coin;
	}
	public String getTarhash() {
		return tarhash;
	}
	public void setTarhash(String tarhash) {
		this.tarhash = tarhash;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	
	
}
