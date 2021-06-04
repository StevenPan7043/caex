package com.pmzhongguo.ex.business.dto;

import java.math.BigDecimal;

/**
 * 我的锁仓
 */
public class LockListDto {
	private Long id;//订单ID
	private Integer member_id;//会员ID
	private Integer currency_id;//d_currency的id，CNY不会出现在此
	private String currency;//d_currency的currency
	private BigDecimal u_num;//释放数量
	private int undata; //释放时间，用来判断当天是否释放，格式：20181010
	private String untime; //释放时间，用来判断当天是否释放，格式：20181010
	private Integer lk_id;//我的锁仓id
	private Integer qishu;//期数
	private Integer m_name;//用户账号

	public Integer getM_name() {
		return m_name;
	}

	public void setM_name(Integer m_name) {
		this.m_name = m_name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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


	public Integer getLk_id() {
		return lk_id;
	}

	public void setLk_id(Integer lk_id) {
		this.lk_id = lk_id;
	}

	public BigDecimal getU_num() {
		return u_num;
	}

	public void setU_num(BigDecimal u_num) {
		this.u_num = u_num;
	}

	public int getUndata() {
		return undata;
	}

	public void setUndata(int undata) {
		this.undata = undata;
	}

	public String getUntime() {
		return untime;
	}

	public void setUntime(String untime) {
		this.untime = untime;
	}

	public Integer getQishu() {
		return qishu;
	}

	public void setQishu(Integer qishu) {
		this.qishu = qishu;
	}
}
