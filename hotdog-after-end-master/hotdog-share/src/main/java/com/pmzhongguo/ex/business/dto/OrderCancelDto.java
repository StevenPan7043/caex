package com.pmzhongguo.ex.business.dto;


import io.swagger.annotations.ApiModelProperty;

public class OrderCancelDto {
	
	@ApiModelProperty(value = "交易对名称，如BTCCNY、LTCCNY、ETHCNY", required = true)
	private String symbol;
	@ApiModelProperty(value = "订单ID", required = true)
	private Long id;
	@ApiModelProperty(value = "订单号", required = true)
	private String o_no;
	@ApiModelProperty(value = "token，APP需提供", required = true)
	private String token;
	
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getO_no() {
		return o_no;
	}
	public void setO_no(String o_no) {
		this.o_no = o_no;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}	
}