package com.pmzhongguo.otc.entity.resp;

import com.pmzhongguo.zzextool.utils.JsonUtil;
import com.pmzhongguo.otc.entity.dto.MerchantDTO;
import org.springframework.beans.BeanUtils;

public class OTCMerchantResp {

	private Integer id;

	//商家ID
	private Integer memberId;

	//用户名
	private String mName;
	
	//平均放放时间
	private Integer ConsumingTime;
	
	//最后登录时间
	private String last_login_time;
	
	// 账户信息
//  1-bank 银行卡,2-alipay 支付宝,3-wxpay 微信  account
	private String bankAccount;

	private String alipayAccount;

	private String wxAccount;

	private Integer vip;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public Integer getConsumingTime() {
		return ConsumingTime;
	}

	public void setConsumingTime(Integer consumingTime) {
		ConsumingTime = consumingTime;
	}

	public String getLast_login_time() {
		return last_login_time;
	}

	public void setLast_login_time(String last_login_time) {
		this.last_login_time = last_login_time;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public String getWxAccount() {
		return wxAccount;
	}

	public void setWxAccount(String wxAccount) {
		this.wxAccount = wxAccount;
	}
	
	public void fromMerchantDTO(MerchantDTO dto) {
		BeanUtils.copyProperties(dto, this);
	}

	public Integer getVip() {
		return vip;
	}

	public void setVip(Integer vip) {
		this.vip = vip;
	}

	@Override
	public String toString() {
		String result = "";
		try {
			result = JsonUtil.beanToJson(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
