package com.pmzhongguo.otc.otcenum;

import java.util.HashMap;
import java.util.Map;

public enum ComplainTypeEnum {
//	申诉状态 1-non-payment 对方未付款  2-unconfirmed  对方未放行   3-canceled  恶意取消订单    99- other 其他 
	NP(1, "non-payment"), UNCONFIRMED(2, "unconfirmed"), CANCELED(3, "canceled"),NORMAL(98, "normal"), OTHER(99, "other");
	private int type;
	private String code;

	private ComplainTypeEnum(int type, String code) {
		this.type = type;
		this.code = code;
	}

	private static Map<String, ComplainTypeEnum> stringMap = new HashMap<String, ComplainTypeEnum>();
	static {
		for (ComplainTypeEnum t : ComplainTypeEnum.values()) {
			stringMap.put(t.toString(), t);
		}
	}

	public int getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public static ComplainTypeEnum getComplainTypeEnum(String key) {
		return stringMap.get(key);
	}

	public static ComplainTypeEnum getEnumByType(int type) {
		for (ComplainTypeEnum t : ComplainTypeEnum.values()) {
			if (t.getType() == type) {
				return t;
			}
		}
		return null;
	}

	public static ComplainTypeEnum getEnumByCode(String code) {
		for (ComplainTypeEnum t : ComplainTypeEnum.values()) {
			if (t.getCode().equals(code)) {
				return t;
			}
		}
		return null;
	}
}
