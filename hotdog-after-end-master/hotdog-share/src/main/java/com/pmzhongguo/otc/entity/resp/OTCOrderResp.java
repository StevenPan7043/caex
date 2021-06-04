package com.pmzhongguo.otc.entity.resp;

import com.pmzhongguo.zzextool.utils.JsonUtil;
import com.pmzhongguo.otc.entity.dto.OTCOrderDTO;
import com.pmzhongguo.otc.otcenum.OrderStatusEnum;
import com.pmzhongguo.otc.otcenum.OrderTypeEnum;
import com.pmzhongguo.otc.otcenum.PriceTypeEnum;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

public class OTCOrderResp {
	
	//订单ID
    private Integer id;
    
    //商家ID
    private Integer memberId;
    
    //会员名
    private String mName;
    
    //基础货币，如ZC
    private String baseCurrency;
    
    //计价货币，如CNY
    private String quoteCurrency;
    
    //订单号，订单号不允许重复
    private String number;
    
    //订单类型：1-buy、2-sell，即卖单、买单
    private OrderTypeEnum type;
    
    //价格类型：1-limit、2-market，即限价单、市价单
    private PriceTypeEnum priceType;
    
    //价格，对限价单，表示会员指定的价格，对于市价单，默认为0
    private BigDecimal price;
    
    //数量，对限价单，表示会员指定的数量，对于市价买单，表示买多少钱(计价货币)，市价卖单表示卖多少币(基础货币)
    private BigDecimal volume;
    
    //最小计价货币交易额。限价单创建时直接设置最小计价货币交易金额
    private BigDecimal minQuote;
    
    //最大计价货币交易额。限价单创建时直接设置最大计价货币交易金额
    private BigDecimal maxQuote;
    
    //已成交数量，单位和含义，同volume
    private BigDecimal doneVolume;
    
    //锁定数量，等待确认成交的数量，单位和含义，同volume
    private BigDecimal lockVolume;
    
    //创建时间
    private String createTime;
    
    //订单状态 0-watting, 1-trading, 2-done, 3-partial-canceled, 4-canceled
    private OrderStatusEnum status;
    
    //账户信息 
//    1-bank 银行卡,2-alipay 支付宝,3-wxpay 微信  account
    private String bankAccount;
    
    private String alipayAccount;
    
    private String wxAccount;
    
    //临时用字段
    private String temp;

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

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public String getQuoteCurrency() {
		return quoteCurrency;
	}

	public void setQuoteCurrency(String quoteCurrency) {
		this.quoteCurrency = quoteCurrency;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public OrderTypeEnum getType() {
		return type;
	}

	public void setType(OrderTypeEnum type) {
		this.type = type;
	}

	public PriceTypeEnum getPriceType() {
		return priceType;
	}

	public void setPriceType(PriceTypeEnum priceType) {
		this.priceType = priceType;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public BigDecimal getMinQuote() {
		return minQuote;
	}

	public void setMinQuote(BigDecimal minQuote) {
		this.minQuote = minQuote;
	}

	public BigDecimal getMaxQuote() {
		return maxQuote;
	}

	public void setMaxQuote(BigDecimal maxQuote) {
		this.maxQuote = maxQuote;
	}

	public BigDecimal getDoneVolume() {
		return doneVolume;
	}

	public void setDoneVolume(BigDecimal doneVolume) {
		this.doneVolume = doneVolume;
	}

	public BigDecimal getLockVolume() {
		return lockVolume;
	}

	public void setLockVolume(BigDecimal lockVolume) {
		this.lockVolume = lockVolume;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public OrderStatusEnum getStatus() {
		return status;
	}

	public void setStatus(OrderStatusEnum status) {
		this.status = status;
	}

	public void fromOTCOrderDTO(OTCOrderDTO dto) {
		BeanUtils.copyProperties(dto, this);
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
