package com.pmzhongguo.ex.business.entity;

import java.math.BigDecimal;


public class CoinWithdraw
{
	private Long id;//订单ID
	private Integer member_id;//会员ID
	private Integer currency_id;//d_currency的id，CNY不会出现在此
	private String currency;//d_currency的currency
	private BigDecimal w_amount;//提现数量
	private BigDecimal w_fee;//手续费
	private String w_create_time;//订单创建时间
	private Integer member_coin_addr_id;//会员的币地址ID
	private String member_coin_addr;//会员的币地址
	private String member_coin_addr_label;//会员的币地址说明
	private String oper_ip;//会员的操作IP
	private String platform_coin_memo;//平台的付币说明
	private Integer w_status;//状态：0：待处理，1：已完成，2：已取消
	private String reject_reason;//拒绝原因
	private String w_txid;//交易ID
	private String w_from_addr; //转出地址（从哪个地址给他转的，目前主要用于玩客币）
	private String auditor;//处理人账号，管理frm_user表
	private String audit_time;//处理时间
	
	private Integer otc_ads_id; //OTC广告ID
	private String otc_oppsite_currency; //OTC对方币种 
	private BigDecimal otc_price;//OTC价格
	private BigDecimal otc_volume;//OTC数量
	private String otc_owner_name;//OTC对方姓名
	private int transfer_no;  //划转标识
	// 附加字段
	private String m_name;// 提现人
	
	
	
	public String getOtc_oppsite_currency() {
		return otc_oppsite_currency;
	}
	public void setOtc_oppsite_currency(String otc_oppsite_currency) {
		this.otc_oppsite_currency = otc_oppsite_currency;
	}
	public Integer getOtc_ads_id() {
		return otc_ads_id;
	}
	public void setOtc_ads_id(Integer otc_ads_id) {
		this.otc_ads_id = otc_ads_id;
	}
	public BigDecimal getOtc_price() {
		return otc_price;
	}
	public void setOtc_price(BigDecimal otc_price) {
		this.otc_price = otc_price;
	}
	public BigDecimal getOtc_volume() {
		return otc_volume;
	}
	public void setOtc_volume(BigDecimal otc_volume) {
		this.otc_volume = otc_volume;
	}
	public String getOtc_owner_name() {
		return otc_owner_name;
	}
	public void setOtc_owner_name(String otc_owner_name) {
		this.otc_owner_name = otc_owner_name;
	}
	public String getW_from_addr() {
		return w_from_addr;
	}
	public void setW_from_addr(String w_from_addr) {
		this.w_from_addr = w_from_addr;
	}
	public String getW_txid() {
		return w_txid;
	}
	public void setW_txid(String w_txid) {
		this.w_txid = w_txid;
	}
	public String getM_name() {
		return m_name;
	}
	public void setM_name(String m_name) {
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
	public BigDecimal getW_amount() {
		return w_amount;
	}
	public void setW_amount(BigDecimal w_amount) {
		this.w_amount = w_amount;
	}
	public BigDecimal getW_fee() {
		return w_fee;
	}
	public void setW_fee(BigDecimal w_fee) {
		this.w_fee = w_fee;
	}
	public String getW_create_time() {
		return w_create_time;
	}
	public void setW_create_time(String w_create_time) {
		this.w_create_time = w_create_time;
	}
	public Integer getMember_coin_addr_id() {
		return member_coin_addr_id;
	}
	public void setMember_coin_addr_id(Integer member_coin_addr_id) {
		this.member_coin_addr_id = member_coin_addr_id;
	}
	public String getMember_coin_addr() {
		return member_coin_addr;
	}
	public void setMember_coin_addr(String member_coin_addr) {
		this.member_coin_addr = member_coin_addr;
	}
	public String getMember_coin_addr_label() {
		return member_coin_addr_label;
	}
	public void setMember_coin_addr_label(String member_coin_addr_label) {
		this.member_coin_addr_label = member_coin_addr_label;
	}
	public String getOper_ip() {
		return oper_ip;
	}
	public void setOper_ip(String oper_ip) {
		this.oper_ip = oper_ip;
	}
	public String getPlatform_coin_memo() {
		return platform_coin_memo;
	}
	public void setPlatform_coin_memo(String platform_coin_memo) {
		this.platform_coin_memo = platform_coin_memo;
	}
	public Integer getW_status() {
		return w_status;
	}
	public void setW_status(Integer w_status) {
		this.w_status = w_status;
	}
	public String getReject_reason() {
		return reject_reason;
	}
	public void setReject_reason(String reject_reason) {
		this.reject_reason = reject_reason;
	}
	public String getAuditor() {
		return auditor;
	}
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	public String getAudit_time() {
		return audit_time;
	}
	public void setAudit_time(String audit_time) {
		this.audit_time = audit_time;
	}

	public int getTransfer_no() {
		return transfer_no;
	}

	public void setTransfer_no(int transfer_no) {
		this.transfer_no = transfer_no;
	}

	@Override
	public String toString() {
		return "CoinWithdraw [id=" + id + ", member_id=" + member_id + ", currency_id=" + currency_id + ", currency="
				+ currency + ", w_amount=" + w_amount + ", w_fee=" + w_fee + ", w_create_time=" + w_create_time
				+ ", member_coin_addr_id=" + member_coin_addr_id + ", member_coin_addr=" + member_coin_addr
				+ ", member_coin_addr_label=" + member_coin_addr_label + ", oper_ip=" + oper_ip
				+ ", platform_coin_memo=" + platform_coin_memo + ", w_status=" + w_status + ", reject_reason="
				+ reject_reason + ", w_txid=" + w_txid + ", w_from_addr=" + w_from_addr + ", auditor=" + auditor
				+ ", audit_time=" + audit_time + ", otc_ads_id=" + otc_ads_id + ", otc_oppsite_currency="
				+ otc_oppsite_currency + ", otc_price=" + otc_price + ", otc_volume=" + otc_volume + ", otc_owner_name="
				+ otc_owner_name + ", m_name=" + m_name + ", transfer_no=" + transfer_no + "]";
	}
	
}
