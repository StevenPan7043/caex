package com.pmzhongguo.otc.otcenum;

import java.util.HashMap;
import java.util.Map;

public enum OrderStatusEnum {
//	订单状态 1-watting, 2-trading, 3-done, 4-partial-canceled, 5-canceled
	WATTING(1, "watting"), TRADING(2, "trading"), DONE(3, "done"), PC(4, "partial-canceled"), CANCELED(5, "canceled");
	private int type;
	private String code;
	private static Map<String, OrderStatusEnum> stringMap = new HashMap<String, OrderStatusEnum>();
	static {
		for (OrderStatusEnum o : OrderStatusEnum.values()) {
			stringMap.put(o.toString(), o);
		}
	}

	private OrderStatusEnum(int type, String code) {
		this.type = type;
		this.code = code;
	}

	public int getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public static OrderStatusEnum getOrderStatusEnum(String key) {
		return stringMap.get(key);
	}

	public static OrderStatusEnum getEnumByType(int type) {
		for (OrderStatusEnum o : OrderStatusEnum.values()) {
			if (o.getType() == type) {
				return o;
			}
		}
		return null;
	}

	public static OrderStatusEnum getEnumByCode(String code) {
		for (OrderStatusEnum o : OrderStatusEnum.values()) {
			if (o.getCode().equals(code)) {
				return o;
			}
		}
		return null;
	}
}
