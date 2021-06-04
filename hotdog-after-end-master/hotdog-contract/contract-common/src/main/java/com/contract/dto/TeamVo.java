package com.contract.dto;

import java.math.BigDecimal;

import com.contract.service.Page;

public class TeamVo extends Page{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3927676387508961149L;

	private Integer id;
	private String phone;
	private String timestr;
	private BigDecimal money;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getTimestr() {
		return timestr;
	}
	public void setTimestr(String timestr) {
		this.timestr = timestr;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	
	
}
