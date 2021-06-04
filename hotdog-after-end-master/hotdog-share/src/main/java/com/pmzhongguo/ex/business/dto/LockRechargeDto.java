package com.pmzhongguo.ex.business.dto;

import java.math.BigDecimal;

/**
 * 我的锁仓
 */
public class LockRechargeDto {
	private Integer id;//订单ID
	private Integer member_id;//会员ID
	private String unlockpre;//百分比
	private Integer currency_id;//d_currency的id，CNY不会出现在此
	private String currency;//d_currency的currency
	private BigDecimal r_amount;//充值数量
	private String r_update_time;//数据更新时间
	private BigDecimal r_transfer;//划转数量
	private BigDecimal r_unnum;
	private Integer locktime;
	private BigDecimal r_release_num;
	private String r_create_time;//充值时间
	private String r_address;//充值地址（会员在平台的唯一地址）
	private String r_txid;//交易ID
	private String r_confirmations; //确认数
	private Integer r_status;//状态：-1待付款[OTC]，0未确认，1已确认，2已取消
	private Integer r_gas;	//是否转手续费
	private Integer c_precision;	//小数点


	public Integer getC_precision() {
		return c_precision;
	}

	public void setC_precision(Integer c_precision) {
		this.c_precision = c_precision;
	}

	public BigDecimal getR_transfer() {
		return r_transfer;
	}


	public void setR_transfer(BigDecimal r_transfer) {
		this.r_transfer = r_transfer;
	}



	public BigDecimal getR_release_num() {
		return r_release_num;
	}

	public void setR_release_num(BigDecimal r_release_num) {
		this.r_release_num = r_release_num;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMember_id() {
		return member_id;
	}

	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}

	public Integer getCurrency_id() {
		return currency_id;
	}

	public void setCurrency_id(Integer currency_id) {
		this.currency_id = currency_id;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getR_amount() {
		return r_amount;
	}

	public void setR_amount(BigDecimal r_amount) {
		this.r_amount = r_amount;
	}

	public BigDecimal getR_unnum() {
		return r_unnum;
	}

	public void setR_unnum(BigDecimal r_unnum) {
		this.r_unnum = r_unnum;
	}

	public Integer getLocktime() {
		return locktime;
	}

	public void setLocktime(Integer locktime) {
		this.locktime = locktime;
	}

	public String getR_create_time() {
		return r_create_time;
	}

	public void setR_create_time(String r_create_time) {
		this.r_create_time = r_create_time;
	}

	public String getR_address() {
		return r_address;
	}

	public void setR_address(String r_address) {
		this.r_address = r_address;
	}

	public String getR_txid() {
		return r_txid;
	}

	public void setR_txid(String r_txid) {
		this.r_txid = r_txid;
	}

	public String getR_confirmations() {
		return r_confirmations;
	}

	public void setR_confirmations(String r_confirmations) {
		this.r_confirmations = r_confirmations;
	}

	public Integer getR_status() {
		return r_status;
	}

	public void setR_status(Integer r_status) {
		this.r_status = r_status;
	}

	public Integer getR_gas() {
		return r_gas;
	}
	public String getUnlockpre() {
		return unlockpre;
	}

	public void setUnlockpre(String unlockpre) {
		this.unlockpre = unlockpre;
	}

	public void setR_gas(Integer r_gas) {
		this.r_gas = r_gas;
	}

	public Integer getR_guiji() {
		return r_guiji;
	}

	public void setR_guiji(Integer r_guiji) {
		this.r_guiji = r_guiji;
	}

	private Integer r_guiji;	//是否归集

	public String getR_update_time() {
		return r_update_time;
	}

	public void setR_update_time(String r_update_time) {
		this.r_update_time = r_update_time;
	}
}
