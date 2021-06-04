package com.contract.dto;

import java.math.BigDecimal;

/**
 * 交易行情价格
 * @author arno
 *
 */
public class SymbolDto implements Comparable<SymbolDto>{

	/**
	 * 名称
	 */
	public String name;
	
	private String coin;
	
	private String unit;
	
	/**
	 * 交易对
	 */
	public String symbol;
	
	/**
	 * 等值usdt价格
	 */
	public BigDecimal usdtPrice=BigDecimal.ZERO;
	
	/**
	 * 0跌 1涨 2平不涨不跌
	 */
	private int isout=1;
	
	/**
	 * 涨跌比例
	 */
	private BigDecimal scale=BigDecimal.ZERO;
	
	/**
	 * 等值人民币
	 */
	private BigDecimal cny=BigDecimal.ZERO;
	
	/**
	 * 开盘价
	 */
	private BigDecimal openVal;
	
	/**
	 * 高
	 */
	private BigDecimal high;
	/**
	 * 低
	 */
	private BigDecimal low;
	
	/**
	 * 成交量
	 */
	private BigDecimal vol;
	
	private int sort=1;//排序
	
	/**
	 * 默认不可以进行逐仓  0可以逐仓
	 */
	private Integer type=-1;
	
	  /** 逐仓自动平仓usdt金额 */
    private BigDecimal zcstopscale;

    /** 逐仓奖励倍数 */
    private BigDecimal zcscale;
    
    /**
     * 逐仓手续费
     */
    private BigDecimal zctax;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getUsdtPrice() {
		return usdtPrice;
	}

	public void setUsdtPrice(BigDecimal usdtPrice) {
		this.usdtPrice = usdtPrice;
	}

	public int getIsout() {
		return isout;
	}

	public void setIsout(int isout) {
		this.isout = isout;
	}

	public BigDecimal getScale() {
		return scale;
	}

	public void setScale(BigDecimal scale) {
		this.scale = scale;
	}

	public BigDecimal getCny() {
		return cny;
	}

	public void setCny(BigDecimal cny) {
		this.cny = cny;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getCoin() {
		return coin;
	}

	public void setCoin(String coin) {
		this.coin = coin;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public BigDecimal getOpenVal() {
		return openVal;
	}

	public void setOpenVal(BigDecimal openVal) {
		this.openVal = openVal;
	}

	public BigDecimal getHigh() {
		return high;
	}

	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	public BigDecimal getLow() {
		return low;
	}

	public void setLow(BigDecimal low) {
		this.low = low;
	}

	public BigDecimal getVol() {
		return vol;
	}

	public void setVol(BigDecimal vol) {
		this.vol = vol;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	@Override
	public int compareTo(SymbolDto o) {
		return this.sort - o.getSort();
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getZcstopscale() {
		return zcstopscale;
	}

	public void setZcstopscale(BigDecimal zcstopscale) {
		this.zcstopscale = zcstopscale;
	}

	public BigDecimal getZcscale() {
		return zcscale;
	}

	public void setZcscale(BigDecimal zcscale) {
		this.zcscale = zcscale;
	}

	public BigDecimal getZctax() {
		return zctax;
	}

	public void setZctax(BigDecimal zctax) {
		this.zctax = zctax;
	}
	
	
	
}
