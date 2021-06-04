package com.contract.service;

import java.io.Serializable;

public class Page implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1235007684101888024L;

	private Integer page=1;//当前页
	
	private Integer rows=20;//每页显示条数
	
	private boolean ispage=true;//是否分页 0不分页 1分页
	
	private Integer pageNum;// 页数
	
	private Integer total;// 条数
	
	private String starttime;
    
    private String endtime;
	
	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public boolean isIspage() {
		return ispage;
	}

	public void setIspage(boolean ispage) {
		this.ispage = ispage;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	
	
}
