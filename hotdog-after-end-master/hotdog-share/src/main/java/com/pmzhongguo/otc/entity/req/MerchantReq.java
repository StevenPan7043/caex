package com.pmzhongguo.otc.entity.req;

import com.pmzhongguo.zzextool.utils.JsonUtil;
import com.pmzhongguo.otc.otcenum.AuditStatusEnum;

public class MerchantReq {
	
    private Integer id;

    private Integer memberId;
    
    private AuditStatusEnum status;
    
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

	public AuditStatusEnum getStatus() {
		return status;
	}

	public void setStatus(AuditStatusEnum status) {
		this.status = status;
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
