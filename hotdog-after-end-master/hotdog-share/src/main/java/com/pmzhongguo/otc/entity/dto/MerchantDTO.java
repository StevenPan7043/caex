package com.pmzhongguo.otc.entity.dto;

import com.pmzhongguo.zzextool.utils.JsonUtil;
import com.pmzhongguo.otc.otcenum.AuditStatusEnum;
import com.pmzhongguo.otc.otcenum.WhetherEnum;

import java.io.Serializable;
import java.math.BigDecimal;

public class MerchantDTO implements Serializable {
    private Integer id;

    private Integer memberId;

    private String mName;
    
    private String depositCurrency;
    
    private BigDecimal depositVolume;

    private AuditStatusEnum status;

    private String createTime;

    private String modifyTime;

    private String memo;
    
    private WhetherEnum isDeposit;

    private Integer vip;
    
    //最后登录时间
  	private String last_login_time;
    
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

	public String getDepositCurrency() {
		return depositCurrency;
	}

	public void setDepositCurrency(String depositCurrency) {
		this.depositCurrency = depositCurrency;
	}

	public BigDecimal getDepositVolume() {
		return depositVolume;
	}

	public void setDepositVolume(BigDecimal depositVolume) {
		this.depositVolume = depositVolume;
	}

	public AuditStatusEnum getStatus() {
		return status;
	}

	public void setStatus(AuditStatusEnum status) {
		this.status = status;
	}

	public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime == null ? null : modifyTime.trim();
    }

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public WhetherEnum getIsDeposit() {
		return isDeposit;
	}

	public void setIsDeposit(WhetherEnum isDeposit) {
		this.isDeposit = isDeposit;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getLast_login_time() {
		return last_login_time;
	}

	public void setLast_login_time(String last_login_time) {
		this.last_login_time = last_login_time;
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