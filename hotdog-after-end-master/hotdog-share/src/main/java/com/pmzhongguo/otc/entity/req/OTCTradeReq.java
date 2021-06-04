package com.pmzhongguo.otc.entity.req;

import com.pmzhongguo.zzextool.utils.JsonUtil;
import com.pmzhongguo.otc.otcenum.OrderTypeEnum;
import com.pmzhongguo.otc.otcenum.TradeStatusEnum;

public class OTCTradeReq {
	
    //交易id
    private Integer id;
    
    private Integer memberId;
    
    private OrderTypeEnum tType;
    
    private TradeStatusEnum status;
    
    private int page;
    
    private int pagesize;
    
    private String sortname;
    
    private String sortorder;

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

	public OrderTypeEnum gettType() {
		return tType;
	}

	public void settType(OrderTypeEnum tType) {
		this.tType = tType;
	}

	public TradeStatusEnum getStatus() {
		return status;
	}

	public void setStatus(TradeStatusEnum status) {
		this.status = status;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public String getSortname() {
		return sortname;
	}

	public void setSortname(String sortname) {
		this.sortname = sortname;
	}

	public String getSortorder() {
		return sortorder;
	}

	public void setSortorder(String sortorder) {
		this.sortorder = sortorder;
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
