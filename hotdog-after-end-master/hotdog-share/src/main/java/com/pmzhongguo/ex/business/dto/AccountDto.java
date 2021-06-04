package com.pmzhongguo.ex.business.dto;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class AccountDto {

	@ApiModelProperty(value = "货币代码，如BTC")
	private String currency;
	@ApiModelProperty(value = "总余额。可交易余额 = 总余额 - 冻结余额 ")
	private BigDecimal total_balance;
	@ApiModelProperty(value = "冻结余额，如有挂单未成交，则有冻结余额")
	private BigDecimal frozen_balance;
	@ApiModelProperty(value = "可用余额，总余额-冻结余额")
	private BigDecimal balance;
	@ApiModelProperty(value = "兑换成ZC后的资产余额")
	private BigDecimal zc_balance;
	@ApiModelProperty(value = "锁仓冻结的资产")
	private BigDecimal lock_num;
	@ApiModelProperty(value = "待释放锁仓资产")
	private BigDecimal wait_release_num;


	private Integer uid;

	private String  mName;

	public BigDecimal getWait_release_num() {
		return wait_release_num;
	}

	public BigDecimal getBalance() {
		return balance;
	}


	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}


	public String getCurrency() {
		return currency;
	}


	public void setCurrency(String currency) {
		this.currency = currency;
	}


	public BigDecimal getTotal_balance() {
		return total_balance;
	}


	public void setTotal_balance(BigDecimal total_balance) {
		this.total_balance = total_balance;
	}


	public BigDecimal getFrozen_balance() {
		return frozen_balance;
	}


	public void setFrozen_balance(BigDecimal frozen_balance) {
		this.frozen_balance = frozen_balance;
	}

	public BigDecimal getZc_balance() {
		return zc_balance;
	}

	public void setZc_balance(BigDecimal zc_balance) {
		this.zc_balance = zc_balance;
	}

	public BigDecimal getLock_num() {
		return lock_num;
	}

	public void setLock_num(BigDecimal lock_num) {
		this.lock_num = lock_num;
	}

    public void setWait_release_num(BigDecimal wait_release_num) {
        this.wait_release_num = wait_release_num;
    }

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}
}
