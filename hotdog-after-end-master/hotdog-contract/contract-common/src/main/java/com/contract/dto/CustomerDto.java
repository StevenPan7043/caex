package com.contract.dto;

import java.math.BigDecimal;

public class CustomerDto {
	private Integer id;

	/** 账号 */
	private String login;

	/** 邀请码 */
	private String invitationcode;

	/** 手机号 */
	private String phone;

	/** 分享二维码 */
	private String qrurl;
	
	private String levelname;
	
	/**
	 * 1未认证 2审核中 3审核通过
	 */
	private Integer authflag;
	
	/**
	 * usdt钱包
	 */
	private BigDecimal usdt;
	
	/**
	 * usdt等值人民币
	 */
	private BigDecimal usdtCny;
	
	/**
	 * zcusdt
	 */
	private BigDecimal zcUsdt;
	
	/**
	 * 逐仓CNY
	 */
	private BigDecimal zcUsdtCny;
	
	private String realname;
	
	private BigDecimal totalUsdt;
	private BigDecimal totalCny;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getInvitationcode() {
		return invitationcode;
	}

	public void setInvitationcode(String invitationcode) {
		this.invitationcode = invitationcode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQrurl() {
		return qrurl;
	}

	public void setQrurl(String qrurl) {
		this.qrurl = qrurl;
	}

	public String getLevelname() {
		return levelname;
	}

	public void setLevelname(String levelname) {
		this.levelname = levelname;
	}

	public Integer getAuthflag() {
		return authflag;
	}

	public void setAuthflag(Integer authflag) {
		this.authflag = authflag;
	}

	public BigDecimal getUsdt() {
		return usdt;
	}

	public void setUsdt(BigDecimal usdt) {
		this.usdt = usdt;
	}

	public BigDecimal getUsdtCny() {
		return usdtCny;
	}

	public void setUsdtCny(BigDecimal usdtCny) {
		this.usdtCny = usdtCny;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public BigDecimal getZcUsdt() {
		return zcUsdt;
	}

	public void setZcUsdt(BigDecimal zcUsdt) {
		this.zcUsdt = zcUsdt;
	}

	public BigDecimal getZcUsdtCny() {
		return zcUsdtCny;
	}

	public void setZcUsdtCny(BigDecimal zcUsdtCny) {
		this.zcUsdtCny = zcUsdtCny;
	}

	public BigDecimal getTotalUsdt() {
		return totalUsdt;
	}

	public void setTotalUsdt(BigDecimal totalUsdt) {
		this.totalUsdt = totalUsdt;
	}

	public BigDecimal getTotalCny() {
		return totalCny;
	}

	public void setTotalCny(BigDecimal totalCny) {
		this.totalCny = totalCny;
	}
	
	
}
