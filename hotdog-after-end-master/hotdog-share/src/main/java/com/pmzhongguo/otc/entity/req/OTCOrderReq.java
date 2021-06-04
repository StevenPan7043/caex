package com.pmzhongguo.otc.entity.req;

import com.pmzhongguo.zzextool.utils.JsonUtil;
import com.pmzhongguo.otc.entity.dto.OTCOrderDTO;
import com.pmzhongguo.otc.entity.dto.OTCTradeDTO;
import com.pmzhongguo.otc.otcenum.OrderTypeEnum;
import com.pmzhongguo.otc.otcenum.PriceChangeTypeEnum;
import com.pmzhongguo.otc.otcenum.PriceTypeEnum;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;

public class OTCOrderReq implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	挂单方初始数据 baseCurrency quoteCurrency type price priceType volume minQuote 可不传 merchantId operIp 
//	吃单方数据 type volume priceType oppositeId
	
    //基础货币，如ZC
    private String baseCurrency;
    
    //计价货币，如CNY
    private String quoteCurrency;

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
    
    //付款限制时间， 单位分钟
    private Integer paymentTime;
    
    //商家ID
    private Integer memberId;
    
    //对方订单id，交易时用
    private Integer oppositeId;
    
    //订单有效时间，单位天
    private Integer effectiveTime;
    
    //订单留言
    private String remark;
    
    //收款账号，有多个就以逗号分隔，对应o_account_info表的id，只有卖单才有值
    private String acountId;
    
    //资金密码，订单操作时用
    private String securityPwd;
    
    //1-unchange 固定价格、2-float，浮动价格
    private PriceChangeTypeEnum priceChangeType;

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

	public Integer getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(Integer paymentTime) {
		this.paymentTime = paymentTime;
	}

	public Integer getOppositeId() {
		return oppositeId;
	}

	public void setOppositeId(Integer oppositeId) {
		this.oppositeId = oppositeId;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public OTCOrderDTO toOTCOrderDTO() {
		OTCOrderDTO oTCOrderDTO = new OTCOrderDTO();
		BeanUtils.copyProperties(this, oTCOrderDTO);
		return oTCOrderDTO;
	}

	public Integer getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(Integer effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAcountId() {
		return acountId;
	}

	public void setAcountId(String acountId) {
		this.acountId = acountId;
	}

	public String getSecurityPwd() {
		return securityPwd;
	}

	public void setSecurityPwd(String securityPwd) {
		this.securityPwd = securityPwd;
	}
	
	public void fromOTCTradeDTO(OTCTradeDTO dto) {
		BeanUtils.copyProperties(dto, this);
	}

	public PriceChangeTypeEnum getPriceChangeType() {
		return priceChangeType;
	}

	public void setPriceChangeType(PriceChangeTypeEnum priceChangeType) {
		this.priceChangeType = priceChangeType;
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
