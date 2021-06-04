package com.pmzhongguo.ex.business.dto;

import com.pmzhongguo.zzextool.utils.JsonUtil;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccountDetailDto implements Serializable {
    private Integer id;

    private Integer memberId;

    private String currency;

    private BigDecimal num;

    private Integer procType;

    private String beforeInfo;

    private String createTime;
    
    private String operResult;

    private String optSource;

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

    public Integer getProcType() {
        return procType;
    }

    public void setProcType(Integer procType) {
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

    public String getOptSource() {
        return optSource;
    }

    public void setOptSource(String optSource) {
        this.optSource = optSource;
    }

    public AccountDetailDto() {
	}
    
	public String getOperResult() {
		return operResult;
	}

	public void setOperResult(String operResult) {
		this.operResult = operResult;
	}

	public AccountDetailDto(Integer memberId, String currency, BigDecimal num, Integer procType,
			String beforeInfo,String optSource) {
		super();
		this.memberId = memberId;
		this.currency = currency;
		this.num = num;
		this.procType = procType;
		this.beforeInfo = beforeInfo;
		this.optSource = optSource;
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