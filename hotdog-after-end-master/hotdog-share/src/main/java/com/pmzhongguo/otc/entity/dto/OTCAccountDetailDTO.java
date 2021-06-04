package com.pmzhongguo.otc.entity.dto;

import com.pmzhongguo.zzextool.utils.JsonUtil;
import com.pmzhongguo.otc.otcenum.AccountOperateTypeEnum;

import java.io.Serializable;
import java.math.BigDecimal;

public class OTCAccountDetailDTO implements Serializable {
    private Integer id;

    private Integer memberId;

    private String currency;

    private BigDecimal num;

    private AccountOperateTypeEnum procType;

    private String beforeInfo;

    private String createTime;
    
    private String operResult;

    private static final long serialVersionUID = 1L;

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public BigDecimal getNum() {
        return num;
    }

    public void setNum(BigDecimal num) {
        this.num = num;
    }

    public AccountOperateTypeEnum getProcType() {
		return procType;
	}

	public void setProcType(AccountOperateTypeEnum procType) {
		this.procType = procType;
	}

	public String getBeforeInfo() {
        return beforeInfo;
    }

    public void setBeforeInfo(String beforeInfo) {
        this.beforeInfo = beforeInfo == null ? null : beforeInfo.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }
    
    public OTCAccountDetailDTO() {
	}
    
	public String getOperResult() {
		return operResult;
	}

	public void setOperResult(String operResult) {
		this.operResult = operResult;
	}

	public OTCAccountDetailDTO(Integer memberId, String currency, BigDecimal num, AccountOperateTypeEnum procType,
			String beforeInfo) {
		super();
		this.memberId = memberId;
		this.currency = currency;
		this.num = num;
		this.procType = procType;
		this.beforeInfo = beforeInfo;
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