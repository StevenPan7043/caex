package com.pmzhongguo.otc.otcenum;

import java.util.HashMap;
import java.util.Map;

public enum TradeStatusEnum {
//	交易状态：1-non-payment 待付款、2-unconfirmed 待确认、3-done已完成、4-canceled 已取消、 5－complaining 申诉中
	NP(1, "non-payment"), UNCONFIRMED(2, "unconfirmed"), DONE(3, "done"), CANCELED(4, "canceled"), COMPLAINING(5, "complaining");
	private int type;
	private String code;

	private TradeStatusEnum(int type, String code) {
		this.type = type;
		this.code = code;
	}

	private static Map<String, TradeStatusEnum> stringMap = new HashMap<String, TradeStatusEnum>();
	static {
		for (TradeStatusEnum t : TradeStatusEnum.values()) {
			stringMap.put(t.toString(), t);
		}
	}

	public int getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public static TradeStatusEnum getTradeStatusEnum(String key) {
		return stringMap.get(key);
	}

	public static TradeStatusEnum getEnumByType(int type) {
		for (TradeStatusEnum t : TradeStatusEnum.values()) {
			if (t.getType() == type) {
				return t;
			}
		}
		return null;
	}

	public static TradeStatusEnum getEnumByCode(String code) {
		for (TradeStatusEnum t : TradeStatusEnum.values()) {
			if (t.getCode().equals(code)) {
				return t;
			}
		}
		return null;
	}
}
