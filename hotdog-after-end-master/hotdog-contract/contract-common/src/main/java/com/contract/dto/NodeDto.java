package com.contract.dto;

import java.util.List;

public class NodeDto {

	
	private String name;//节点名称
	private String mainAddr;//主钱包
	private String totalmoney;//总额
	private String waitmoney;//待同步金额
	
	private List<NodeDetailDto> list;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMainAddr() {
		return mainAddr;
	}

	public void setMainAddr(String mainAddr) {
		this.mainAddr = mainAddr;
	}

	public List<NodeDetailDto> getList() {
		return list;
	}

	public void setList(List<NodeDetailDto> list) {
		this.list = list;
	}

	public String getTotalmoney() {
		return totalmoney;
	}

	public void setTotalmoney(String totalmoney) {
		this.totalmoney = totalmoney;
	}

	public String getWaitmoney() {
		return waitmoney;
	}

	public void setWaitmoney(String waitmoney) {
		this.waitmoney = waitmoney;
	}
	
	
}
