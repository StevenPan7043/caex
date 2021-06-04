package com.pmzhongguo.otc.entity.dto;

import com.pmzhongguo.zzextool.utils.JsonUtil;
import com.pmzhongguo.otc.otcenum.ComplainTypeEnum;
import com.pmzhongguo.otc.otcenum.OrderTypeEnum;
import com.pmzhongguo.otc.otcenum.TakerEnum;
import com.pmzhongguo.otc.otcenum.TradeStatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;

public class OTCTradeDTO implements Serializable {
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

    private String modifyTime;

    private TakerEnum taker;

    private BigDecimal fee;

    private String feeCurrency;

    private TradeStatusEnum status;
    
    private String payTime;

    private String createTime;
    
    private Integer paymentTime;
    
    private String doneTime;
    
    private Integer consumingTime;
    
    private Integer acountId;
    
    private String memo;
    
    private String tNumber;
    
    private ComplainTypeEnum complainType;
    
    private AccountInfoDTO selfAccount;
    
    private AccountInfoDTO oppositeAccount;
    
    private String opposite_nick_name;
    
    private Integer opposite_member_id;

    private String  memoStr;

	private String oppositeStatus;

	private boolean appealFlag;
    
    private static final long serialVersionUID = 1L;

	public String getMemoStr() {
		return memoStr;
	}

	public void setMemoStr(String memoStr) {
		this.memoStr = memoStr;
	}

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
        this.baseCurrency = baseCurrency == null ? null : baseCurrency.trim().toUpperCase();
    }

    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public void setQuoteCurrency(String quoteCurrency) {
        this.quoteCurrency = quoteCurrency == null ? null : quoteCurrency.trim().toUpperCase();
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

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public TakerEnum getTaker() {
		return taker;
	}

	public void setTaker(TakerEnum taker) {
		this.taker = taker;
	}

	public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getFeeCurrency() {
        return feeCurrency;
    }

    public void setFeeCurrency(String feeCurrency) {
        this.feeCurrency = feeCurrency == null ? null : feeCurrency.trim();
    }

	public TradeStatusEnum getStatus() {
		return status;
	}

	public void setStatus(TradeStatusEnum status) {
		this.status = status;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(Integer paymentTime) {
		this.paymentTime = paymentTime;
	}

	public Integer getConsumingTime() {
		return consumingTime;
	}

	public void setConsumingTime(Integer consumingTime) {
		this.consumingTime = consumingTime;
	}

	public String getDoneTime() {
		return doneTime;
	}

	public void setDoneTime(String doneTime) {
		this.doneTime = doneTime;
	}

	public Integer getAcountId() {
		return acountId;
	}

	public void setAcountId(Integer acountId) {
		this.acountId = acountId;
	}

	public AccountInfoDTO getSelfAccount() {
		return selfAccount;
	}

	public void setSelfAccount(AccountInfoDTO selfAccount) {
		this.selfAccount = selfAccount;
	}

	public AccountInfoDTO getOppositeAccount() {
		return oppositeAccount;
	}

	public void setOppositeAccount(AccountInfoDTO oppositeAccount) {
		this.oppositeAccount = oppositeAccount;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getOpposite_nick_name() {
		return opposite_nick_name;
	}

	public void setOpposite_nick_name(String opposite_nick_name) {
		this.opposite_nick_name = opposite_nick_name;
	}

	public Integer getOpposite_member_id() {
		return opposite_member_id;
	}

	public void setOpposite_member_id(Integer opposite_member_id) {
		this.opposite_member_id = opposite_member_id;
	}

	public ComplainTypeEnum getComplainType() {
		return complainType;
	}

	public void setComplainType(ComplainTypeEnum complainType) {
		this.complainType = complainType;
	}

	public String gettNumber() {
		return tNumber;
	}

	public void settNumber(String tNumber) {
		this.tNumber = tNumber;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getOppositeStatus() {
		return oppositeStatus;
	}

	public void setOppositeStatus(String oppositeStatus) {
		this.oppositeStatus = oppositeStatus;
	}

	public boolean isAppealFlag() {
		return appealFlag;
	}

	public void setAppealFlag(boolean appealFlag) {
		this.appealFlag = appealFlag;
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