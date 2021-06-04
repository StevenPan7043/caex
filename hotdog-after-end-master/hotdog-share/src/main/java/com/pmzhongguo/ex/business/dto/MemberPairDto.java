package com.pmzhongguo.ex.business.dto;

import io.swagger.annotations.ApiModelProperty;

public class MemberPairDto {

	@ApiModelProperty(value = "交易对展示名称，如BTC/CNY、LTC/CNY、ETH/CNY")
	private String pair_dsp_name;	

	public String getPair_dsp_name() {
		return pair_dsp_name;
	}

	public void setPair_dsp_name(String pair_dsp_name) {
		this.pair_dsp_name = pair_dsp_name;
	}
	
	
}
