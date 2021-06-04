package com.pmzhongguo.ex.core.web.resp;


import com.wordnik.swagger.annotations.ApiModelProperty;

public class WithdrawAddrResp extends Resp {
	@ApiModelProperty(value = "返回数据")
	private Object data;

	@ApiModelProperty(value = "是否为以太坊架构的币,0,1是一代币，2是二代币")
	private Integer is_in_eth;

	public WithdrawAddrResp(Integer _state, String _msg, Integer is_eth, Object _data) {
		super(_state, _msg);
		this.data = _data;
		this.is_in_eth = is_eth;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Integer getIs_in_eth() {
		return is_in_eth;
	}

	public void setIs_in_eth(Integer is_in_eth) {
		this.is_in_eth = is_in_eth;
	}
}
