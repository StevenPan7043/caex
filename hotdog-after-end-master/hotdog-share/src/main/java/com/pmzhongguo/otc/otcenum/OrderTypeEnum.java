package com.pmzhongguo.otc.otcenum;

import java.util.HashMap;
import java.util.Map;

public enum OrderTypeEnum {
//	1-buy、2-sell，即卖单、买单	
	BUY(1, "buy"), SELL(2, "sell");
	private int type;
	private String code;
	private static Map<String, OrderTypeEnum> stringMap = new HashMap<String, OrderTypeEnum>();
	static {
		for (OrderTypeEnum o : OrderTypeEnum.values()) {
			stringMap.put(o.toString(), o);
		}
	}

	private OrderTypeEnum(int type, String code) {
		this.type = type;
		this.code = code;
	}

	public int getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public static OrderTypeEnum getOrderTypeEnum(String key) {
		return stringMap.get(key);
	}

	public static OrderTypeEnum getEnumByType(int type) {
		for (OrderTypeEnum o : OrderTypeEnum.values()) {
			if (o.getType() == type) {
				return o;
			}
		}
		return null;
	}

	public static OrderTypeEnum getEnumByCode(String code) {
		for (OrderTypeEnum o : OrderTypeEnum.values()) {
			if (o.getCode().equals(code)) {
				return o;
			}
		}
		return null;
	}
}
