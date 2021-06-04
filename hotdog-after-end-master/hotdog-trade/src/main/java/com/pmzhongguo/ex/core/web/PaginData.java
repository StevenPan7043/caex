package com.pmzhongguo.ex.core.web;

import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.core.utils.PagingUtil;


public class PaginData {
	private Integer count;
	private List list;
	private Map stats; // 统计数据，例如发货单管理底部的统计
	private Integer needPagin;

	public PaginData() {
	}

	/**
	 * 
	 * @param params
	 * @param needPagin
	 *            1表示分页，0表示不需要分页
	 */
	public PaginData(Map params, Integer needPagin) {
		PagingUtil.paging(params, needPagin);
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public Map getStats() {
		return stats;
	}

	public void setStats(Map stats) {
		this.stats = stats;
	}

	public Integer getNeedPagin() {
		return needPagin;
	}

	public void setNeedPagin(Integer needPagin) {
		this.needPagin = needPagin;
	}
}
