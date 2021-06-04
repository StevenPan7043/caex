package com.pmzhongguo.otc.otcenum;

import java.util.HashMap;
import java.util.Map;

public enum TakerEnum {
//	self-自己 opposite-对方 system-系统 (原计划在聊天记录里使用这个枚举的，现暂不使用)
	SELF(1, "self"), OPPOSITE(2, "opposite"), SYSTEM(3, "system");
	private int type;
	private String code;

	private TakerEnum(int type, String code) {
		this.type = type;
		this.code = code;
	}

	private static Map<String, TakerEnum> stringMap = new HashMap<String, TakerEnum>();
	static {
		for (TakerEnum t : TakerEnum.values()) {
			stringMap.put(t.toString(), t);
		}
	}

	public int getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public static TakerEnum getTakerEnum(String key) {
		return stringMap.get(key);
	}

	public static TakerEnum getEnumByType(int type) {
		for (TakerEnum t : TakerEnum.values()) {
			if (t.getType() == type) {
				return t;
			}
		}
		return null;
	}

	public static TakerEnum getEnumByCode(String code) {
		for (TakerEnum t : TakerEnum.values()) {
			if (t.getCode().equals(code)) {
				return t;
			}
		}
		return null;
	}
}
