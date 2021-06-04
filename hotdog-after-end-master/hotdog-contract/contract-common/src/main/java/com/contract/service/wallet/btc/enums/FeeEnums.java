package com.contract.service.wallet.btc.enums;

public enum FeeEnums {

	fee1("1","1","feeaddr1"),//要修改为自己的
	fee2("2","1","feeaddr2"),
	fee3("3","1","feeaddr3");
	
	private String key;
	private String nodeid;
	private String feeaddr;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getFeeaddr() {
		return feeaddr;
	}
	public void setFeeaddr(String feeaddr) {
		this.feeaddr = feeaddr;
	}
	public String getNodeid() {
		return nodeid;
	}
	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}
	private FeeEnums(String key,String nodeid,String feeaddr) {
		this.key=key;
		this.nodeid=nodeid;
		this.feeaddr=feeaddr;
	}
}
