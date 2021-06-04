package com.contract.dto;

public class UserDto {

	private Integer userid;
	private Integer manageid;
	private String rolename;
	private String name;
	private String account;
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public Integer getManageid() {
		return manageid;
	}
	public void setManageid(Integer manageid) {
		this.manageid = manageid;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	
}
