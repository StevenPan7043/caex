package com.pmzhongguo.otc.otcenum;

import java.util.HashMap;
import java.util.Map;

public enum PriceTypeEnum {
//	1-limit、2-market，即限价单、市价单
	LIMIT(1, "limit"), MARKET(2, "market");
	private int type;
	private String code;
	private static Map<String, PriceTypeEnum> stringMap = new HashMap<String, PriceTypeEnum>();
	static {
		for (PriceTypeEnum p : PriceTypeEnum.values()) {
			stringMap.put(p.toString(), p);
		}
	}

	private PriceTypeEnum(int type, String code) {
		this.type = type;
		this.code = code;
	}

	public int getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public static PriceTypeEnum getPriceTypeEnum(String key) {
		return stringMap.get(key);
	}

	public static PriceTypeEnum getEnumByType(int type) {
		for (PriceTypeEnum p : PriceTypeEnum.values()) {
			if (p.getType() == type) {
				return p;
			}
		}
		return null;
	}

	public static PriceTypeEnum getEnumByCode(String code) {
		for (PriceTypeEnum p : PriceTypeEnum.values()) {
			if (p.getCode().equals(code)) {
				return p;
			}
		}
		return null;
	}
}
