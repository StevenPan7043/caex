package com.pmzhongguo.otc.otcenum;

import java.util.HashMap;
import java.util.Map;

public enum PriceChangeTypeEnum {
//	1-unchange 固定价格、2-float，浮动价格
	UNCHANGE(1, "unchange"), FLOAT(2, "float");
	private int type;
	private String code;
	private static Map<String, PriceChangeTypeEnum> stringMap = new HashMap<String, PriceChangeTypeEnum>();
	static {
		for (PriceChangeTypeEnum p : PriceChangeTypeEnum.values()) {
			stringMap.put(p.toString(), p);
		}
	}

	private PriceChangeTypeEnum(int type, String code) {
		this.type = type;
		this.code = code;
	}

	public int getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public static PriceChangeTypeEnum getPriceTypeEnum(String key) {
		return stringMap.get(key);
	}

	public static PriceChangeTypeEnum getEnumByType(int type) {
		for (PriceChangeTypeEnum p : PriceChangeTypeEnum.values()) {
			if (p.getType() == type) {
				return p;
			}
		}
		return null;
	}

	public static PriceChangeTypeEnum getEnumByCode(String code) {
		for (PriceChangeTypeEnum p : PriceChangeTypeEnum.values()) {
			if (p.getCode().equals(code)) {
				return p;
			}
		}
		return null;
	}
}
