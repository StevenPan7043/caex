package com.contract.enums;

/**
 * K线图
 * @author arno
 *
 */
public enum KlineTypeEnums {
	min1("1min"),
	min5("5min"),
	min15("15min"),
//	min30("30min"),
	min60("60min"),
	day1("1day"),
	week1("1week");
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	private KlineTypeEnums(String name) {
		this.name=name;
	}
}
