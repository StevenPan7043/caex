package com.contract.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ReportDto implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1443559994015897468L;
	private Integer num;
	private BigDecimal money;
	private BigDecimal rates;//盈亏
	private BigDecimal spreadmoney;//点差
	private BigDecimal tax;//手续费
	private BigDecimal balance;
	private BigDecimal zcbalance;
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public BigDecimal getRates() {
		return rates;
	}
	public void setRates(BigDecimal rates) {
		this.rates = rates;
	}
	public BigDecimal getSpreadmoney() {
		return spreadmoney;
	}
	public void setSpreadmoney(BigDecimal spreadmoney) {
		this.spreadmoney = spreadmoney;
	}
	public BigDecimal getTax() {
		return tax;
	}
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getZcbalance() {
		return zcbalance;
	}
	public void setZcbalance(BigDecimal zcbalance) {
		this.zcbalance = zcbalance;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	
	
	
	
}
