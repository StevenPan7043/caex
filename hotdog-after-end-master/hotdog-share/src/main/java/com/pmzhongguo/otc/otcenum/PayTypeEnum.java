package com.pmzhongguo.otc.otcenum;

import java.util.HashMap;
import java.util.Map;

public enum PayTypeEnum {
//	类型：1-bank 银行卡,2-alipay 支付宝,3-wxpay 微信
	BANK(1, "bank"), ALIPAY(2, "alipay"), WXPAY(3, "wxpay");
	private int type;
	private String code;
	private static Map<String, PayTypeEnum> stringMap = new HashMap<String, PayTypeEnum>();
	static {
		for (PayTypeEnum p : PayTypeEnum.values()) {
			stringMap.put(p.toString(), p);
		}
	}

	private PayTypeEnum(int type, String code) {
		this.type = type;
		this.code = code;
	}

	public int getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public static PayTypeEnum getPayTypeEnum(String key) {
		return stringMap.get(key);
	}

	public static PayTypeEnum getEnumByType(int type) {
		for (PayTypeEnum p : PayTypeEnum.values()) {
			if (p.getType() == type) {
				return p;
			}
		}
		return null;
	}

	public static PayTypeEnum getEnumByCode(String code) {
		for (PayTypeEnum p : PayTypeEnum.values()) {
			if (p.getCode().equals(code)) {
				return p;
			}
		}
		return null;
	}
}
