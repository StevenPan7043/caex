package com.pmzhongguo.ex.business.dto;

import io.swagger.annotations.ApiModelProperty;

public class OrderRespDto {
	@ApiModelProperty(value = "id")
	private String id;
	
	@ApiModelProperty(value = "交易对名称，如BTCCNY、LTCCNY、ETHCNY")
	private String symbol;
	
	@ApiModelProperty(value = "基础货币")
	private String base_currency;
	@ApiModelProperty(value = "计价货币")
	private String quote_currency;
	
	@ApiModelProperty(value = "订单号")
	private String o_no;
	
	@ApiModelProperty(value = "订单类型：buy、sell，即卖单、买单")
	private String o_type;
	
	@ApiModelProperty(value = "价格类型：limit、market，即限价单、市价单")
	private String o_price_type;
	
	@ApiModelProperty(value = "价格，对限价单，表示会员指定的价格，对于市价单，默认为0")
	private String price;
	
	@ApiModelProperty(value = "数量，对限价单，表示会员指定的数量，对于市价买单，表示买多少计价货币，市价卖单表示卖多少基础货币")
	private String volume;
	
	@ApiModelProperty(value = "已成交数量")
	private String done_volume;
	
	@ApiModelProperty(value = "已成交总额")
	private String done_amount;
	
	@ApiModelProperty(value = "已成交均价")
	private String done_avg_price;
	
	@ApiModelProperty(value = "已成交手续费，卖出方为计价货币，买入方为基础货币")
	private String done_fee;
	
	@ApiModelProperty(value = "已成交手续费单位")
	private String fee_currency;
	
	@ApiModelProperty(value = "订单来源：api、WEB、Wap、Android、iOS")
	private String source;
	
	@ApiModelProperty(value = "创建时间")
	private String create_time;
	
	@ApiModelProperty(value = "撤销时间")
	private String cancel_time;
	
	@ApiModelProperty(value = "watting 等待中,partial-done 部分成交,done 成交,partial-canceled 部分成交撤单,canceled 撤单")
	private String o_status;
	
	

	public String getBase_currency() {
		return base_currency;
	}

	public void setBase_currency(String base_currency) {
		this.base_currency = base_currency;
	}

	public String getQuote_currency() {
		return quote_currency;
	}

	public void setQuote_currency(String quote_currency) {
		this.quote_currency = quote_currency;
	}

	public String getFee_currency() {
		return fee_currency;
	}

	public void setFee_currency(String fee_currency) {
		this.fee_currency = fee_currency;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getO_no() {
		return o_no;
	}

	public void setO_no(String o_no) {
		this.o_no = o_no;
	}

	public String getO_type() {
		return o_type;
	}

	public void setO_type(String o_type) {
		this.o_type = o_type;
	}

	public String getO_price_type() {
		return o_price_type;
	}

	public void setO_price_type(String o_price_type) {
		this.o_price_type = o_price_type;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getDone_volume() {
		return done_volume;
	}

	public void setDone_volume(String done_volume) {
		this.done_volume = done_volume;
	}

	public String getDone_amount() {
		return done_amount;
	}

	public void setDone_amount(String done_amount) {
		this.done_amount = done_amount;
	}

	public String getDone_avg_price() {
		return done_avg_price;
	}

	public void setDone_avg_price(String done_avg_price) {
		this.done_avg_price = done_avg_price;
	}

	public String getDone_fee() {
		return done_fee;
	}

	public void setDone_fee(String done_fee) {
		this.done_fee = done_fee;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getCancel_time() {
		return cancel_time;
	}

	public void setCancel_time(String cancel_time) {
		this.cancel_time = cancel_time;
	}

	public String getO_status() {
		return o_status;
	}

	public void setO_status(String o_status) {
		this.o_status = o_status;
	}
}
