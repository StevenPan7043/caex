package com.pmzhongguo.otc.otcenum;

import java.util.HashMap;
import java.util.Map;

public enum OTCOrderOperateTypeEnum {
	

	LOCKTRADE(1, "lockTrade"), DONETRADE(2, "doneTrade"), CANCELTRADE(3, "cancelTrade"), CANCELORDER(4, "cancelOrder");
	private int type;
	private String code;
	private static Map<String, OTCOrderOperateTypeEnum> stringMap = new HashMap<String, OTCOrderOperateTypeEnum>();
	static {
		for (OTCOrderOperateTypeEnum a : OTCOrderOperateTypeEnum.values()) {
			stringMap.put(a.toString(), a);
		}
	}

	private OTCOrderOperateTypeEnum(int type, String code) {
		this.type = type;
		this.code = code;
	}

	public int getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public static OTCOrderOperateTypeEnum getOrderStatusEnum(String key) {
		return stringMap.get(key);
	}

	public static OTCOrderOperateTypeEnum getEnumByType(int type) {
		for (OTCOrderOperateTypeEnum a : OTCOrderOperateTypeEnum.values()) {
			if (a.getType() == type) {
				return a;
			}
		}
		return null;
	}

	public static OTCOrderOperateTypeEnum getEnumByCode(String code) {
		for (OTCOrderOperateTypeEnum a : OTCOrderOperateTypeEnum.values()) {
			if (a.getCode().equals(code)) {
				return a;
			}
		}
		return null;
	}
}
