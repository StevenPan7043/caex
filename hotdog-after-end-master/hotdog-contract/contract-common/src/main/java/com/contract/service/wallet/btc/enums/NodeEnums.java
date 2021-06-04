package com.contract.service.wallet.btc.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 节点
 * @author arno
 *
 */
public enum NodeEnums {


	node_1("1","ip","","","端口","地址");
	
	private String id;
	private String url;
	private String user;
	private String password;
	private String port;
	private String addr;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	
	private NodeEnums( String id, String url,String user,String password,String port, String addr) {
		this.id=id;
		this.url=url;
		this.user=user;
		this.password=password;
		this.port=port;
		this.addr=addr;
	}
	
	public static NodeEnums getById(String id) {
		if(StringUtils.isEmpty(id)) {
			return node_1;
		}
		for(NodeEnums e:NodeEnums.values()) {
			if(e.getId().equals(id)) {
				return e;
			}
		}
		return null;
	}
	
	public static NodeEnums getRandom() {
		int i=(int)(Math.random()*NodeEnums.values().length);
		NodeEnums n=NodeEnums.values()[i];
		return n;
	}
}
