package com.pmzhongguo.otc.entity.resp;

import com.pmzhongguo.zzextool.utils.JsonUtil;
import com.pmzhongguo.otc.entity.dto.OTCTradeDTO;
import com.pmzhongguo.otc.otcenum.OrderTypeEnum;
import com.pmzhongguo.otc.otcenum.PayTypeEnum;
import com.pmzhongguo.otc.otcenum.TakerEnum;
import com.pmzhongguo.otc.otcenum.TradeStatusEnum;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

public class OTCTradeResp {
	private Integer id;

    private Integer memberId;
    
    private String mName;

    private String baseCurrency;

    private String quoteCurrency;

    private OrderTypeEnum tType;

    private Integer oId;

    private Integer oppositeOId;
    
    private Integer oppositeTId;

    private BigDecimal price;

    private BigDecimal volume;
    
    private TakerEnum taker;
    
    private TradeStatusEnum status;
    
    private String createTime;
    
    private String tNumber;
    
    private BigDecimal done;
    
    //账户信息
    private PayTypeEnum payType;
    
    private String payAccount;
    
    private String memo;

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

	public OrderTypeEnum gettType() {
		return tType;
	}

	public void settType(OrderTypeEnum tType) {
		this.tType = tType;
	}

	public Integer getoId() {
		return oId;
	}

	public void setoId(Integer oId) {
		this.oId = oId;
	}

	public Integer getOppositeOId() {
		return oppositeOId;
	}

	public void setOppositeOId(Integer oppositeOId) {
		this.oppositeOId = oppositeOId;
	}

	public Integer getOppositeTId() {
		return oppositeTId;
	}

	public void setOppositeTId(Integer oppositeTId) {
		this.oppositeTId = oppositeTId;
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

	public TakerEnum getTaker() {
		return taker;
	}

	public void setTaker(TakerEnum taker) {
		this.taker = taker;
	}

	public TradeStatusEnum getStatus() {
		return status;
	}

	public void setStatus(TradeStatusEnum status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public PayTypeEnum getPayType() {
		return payType;
	}

	public void setPayType(PayTypeEnum payType) {
		this.payType = payType;
	}

	public String getPayAccount() {
		return payAccount;
	}

	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}
    
	public String gettNumber() {
		return tNumber;
	}

	public void settNumber(String tNumber) {
		this.tNumber = tNumber;
	}

	public BigDecimal getDone() {
		return done;
	}

	public void setDone(BigDecimal done) {
		this.done = done;
	}
	
	public void fromOTCTradeDTO(OTCTradeDTO dto) {
		BeanUtils.copyProperties(dto, this);
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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
