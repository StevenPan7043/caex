package com.pmzhongguo.otc.otcenum;

import java.util.HashMap;
import java.util.Map;

public enum ResultStatusEnum {
//	跟com.pmzhongguo.ex.core.web.resp保持一致
	FAIL(-1, "fail"), SUCCESS(1, "success");
	private int type;
	private String code;

	private ResultStatusEnum(int type, String code) {
		this.type = type;
		this.code = code;
	}

	private static Map<String, ResultStatusEnum> stringMap = new HashMap<String, ResultStatusEnum>();
	static {
		for (ResultStatusEnum r : ResultStatusEnum.values()) {
			stringMap.put(r.toString(), r);
		}
	}

	public int getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public static ResultStatusEnum getWhetherEnum(String key) {
		return stringMap.get(key);
	}

	public static ResultStatusEnum getEnumByType(int type) {
		for (ResultStatusEnum r : ResultStatusEnum.values()) {
			if (r.getType() == type) {
				return r;
			}
		}
		return null;
	}

	public static ResultStatusEnum getEnumByCode(String code) {
		for (ResultStatusEnum r : ResultStatusEnum.values()) {
			if (r.getCode().equals(code)) {
				return r;
			}
		}
		return null;
	}
}
