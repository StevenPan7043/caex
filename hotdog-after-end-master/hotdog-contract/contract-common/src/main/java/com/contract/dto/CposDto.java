package com.contract.dto;

import java.math.BigDecimal;

public class CposDto {

	
	private String toaddr;
	
	private BigDecimal cpos;
	
	private String transcode;

	public String getToaddr() {
		return toaddr;
	}

	public void setToaddr(String toaddr) {
		this.toaddr = toaddr;
	}

	public BigDecimal getCpos() {
		return cpos;
	}

	public void setCpos(BigDecimal cpos) {
		this.cpos = cpos;
	}

	public String getTranscode() {
		return transcode;
	}

	public void setTranscode(String transcode) {
		this.transcode = transcode;
	}
	
	
}
