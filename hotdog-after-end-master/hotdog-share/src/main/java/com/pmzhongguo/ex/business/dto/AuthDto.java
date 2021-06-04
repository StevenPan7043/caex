package com.pmzhongguo.ex.business.dto;

import com.pmzhongguo.ex.business.entity.CurrencyPair;


public class AuthDto {
	
	private boolean success; //token 校验结果
	private String msg;     //校验不通过时错误描述
	private Integer tokenId; //tokenId
	private Integer memberId; //用户ID
	private CurrencyPair currencyPair; //交易对
	
	
	public Integer getTokenId() {
		return tokenId;
	}
	public void setTokenId(Integer tokenId) {
		this.tokenId = tokenId;
	}
	public CurrencyPair getCurrencyPair() {
		return currencyPair;
	}
	public void setCurrencyPair(CurrencyPair currencyPair) {
		this.currencyPair = currencyPair;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Integer getMemberId() {
		return memberId;
	}
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}
	
}