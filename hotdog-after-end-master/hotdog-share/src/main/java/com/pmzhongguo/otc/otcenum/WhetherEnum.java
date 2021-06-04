package com.pmzhongguo.otc.otcenum;

import java.util.HashMap;
import java.util.Map;

public enum WhetherEnum {
	NO(0, "no"), YES(1, "yes");
	private int type;
	private String code;

	private WhetherEnum(int type, String code) {
		this.type = type;
		this.code = code;
	}

	private static Map<String, WhetherEnum> stringMap = new HashMap<String, WhetherEnum>();
	static {
		for (WhetherEnum w : WhetherEnum.values()) {
			stringMap.put(w.toString(), w);
		}
	}

	public int getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public static WhetherEnum getWhetherEnum(String key) {
		return stringMap.get(key);
	}

	public static WhetherEnum getEnumByType(int type) {
		for (WhetherEnum w : WhetherEnum.values()) {
			if (w.getType() == type) {
				return w;
			}
		}
		return null;
	}

	public static WhetherEnum getEnumByCode(String code) {
		for (WhetherEnum w : WhetherEnum.values()) {
			if (w.getCode().equals(code)) {
				return w;
			}
		}
		return null;
	}
}
