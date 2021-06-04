package com.pmzhongguo.ex.business.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

public class SymbolDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8047982511010111046L;
	@ApiModelProperty(value = "交易对名称，如BTCCNY、LTCCNY、ETHCNY")
	public String symbol;
	@ApiModelProperty(value = "基础货币，如BTC")
	public String base_currency;
	@ApiModelProperty(value = "计价货币，如CNY")
	public String quote_currency;
	@ApiModelProperty(value = "价格最大小数位，如人民币，最多到分，即2")
	public Integer price_precision;
	@ApiModelProperty(value = "买卖数量最大小数位，如比特币，最小可交易0.0001个，即4位")
	public Integer volume_precision;
	@ApiModelProperty(value = "吃单手续费费率")
	public BigDecimal taker_fee;
	@ApiModelProperty(value = "挂单手续费费率")
	public BigDecimal maker_fee;
	

	public SymbolDto(String _symbol, String _base_currency, String _quote_currency, 
			Integer _price_precision, Integer _volume_precision, BigDecimal _taker_fee, BigDecimal _maker_fee) {
		symbol = _symbol;
		base_currency = _base_currency;
		quote_currency = _quote_currency;
		price_precision = _price_precision;
		volume_precision = _volume_precision;
		taker_fee = _taker_fee;
		maker_fee = _maker_fee;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getBase_currency() {
		return base_currency;
	}

	public String getQuote_currency() {
		return quote_currency;
	}

	public Integer getPrice_precision() {
		return price_precision;
	}

	public Integer getVolume_precision() {
		return volume_precision;
	}

	public BigDecimal getTaker_fee() {
		return taker_fee;
	}

	public BigDecimal getMaker_fee() {
		return maker_fee;
	}
}
