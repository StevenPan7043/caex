package com.contract.enums;

public enum SysParamsEnums {

    /**
     *
     */
	appdownload_url("appdownload_url","APP下载地址"),
	app_version("app_version","APP版本号"),
	desc_val("desc_val","更新版本提示语"),
	android_url("android_url","安卓下载地址"),
	ios_url("ios_url","IOS下载地址"),
	app_host("app_host","APP域名"),
	usdt_cash_min("usdt_cash_min","USDT最低提现数量"),
	usdt_cash_max("usdt_cash_max","USDT最高提币数量"),
	usdt_cash_scale("usdt_cash_scale","USDT提币手续费数量"),
	service_phone("service_phone","客服电话"),
	contract_up("contract_up","合约开多手续费比例"),
	contract_down("contract_down","合约开空手续费比例"),
	contract_auto_out("contract_auto_out","合约自动爆仓比例"),
	about_us("about_us","关于我们"),
	out_scale("out_scale","平仓手续费"),
	open_push("open_push","是否开启推荐奖励0开启，-1关闭"),
	push_scale("push_scale","推荐奖励比例"),
	gd_scale("gd_scale","跟单日收益范围");
	
	private String key;
	private String remark;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	private SysParamsEnums(String key,String remark) {
		this.key=key;
		this.remark=remark;
	}
}
