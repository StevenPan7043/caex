package com.pmzhongguo.otc.otcenum;

import java.util.HashMap;
import java.util.Map;

public enum AuditStatusEnum {
	APPLY_AUDITING(1, "apply_auditing"), 
	APPLY_PASSED(2, "apply_passed"), 
	APPLY_REJECT(3, "apply_reject"),
	SECEDE_AUDITING(4, "secede_auditing"), 
	SECEDE_PASSED(5, "secede_passed"),
	//SECEDE_REJECT状态应该没用，SECEDE_AUDITING --> SECEDE_PASSED || APPLY_PASSED
	SECEDE_REJECT(6, "secede_reject");
	private int type;
	private String code;

	private AuditStatusEnum(int type, String code) {
		this.type = type;
		this.code = code;
	}

	private static Map<String, AuditStatusEnum> stringMap = new HashMap<String, AuditStatusEnum>();
	static {
		for (AuditStatusEnum a : AuditStatusEnum.values()) {
			stringMap.put(a.toString(), a);
		}
	}

	public int getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public static AuditStatusEnum getAuditStatusEnum(String key) {
		return stringMap.get(key);
	}

	public static AuditStatusEnum getEnumByType(int type) {
		for (AuditStatusEnum a : AuditStatusEnum.values()) {
			if (a.getType() == type) {
				return a;
			}
		}
		return null;
	}

	public static AuditStatusEnum getEnumByCode(String code) {
		for (AuditStatusEnum a : AuditStatusEnum.values()) {
			if (a.getCode().equals(code)) {
				return a;
			}
		}
		return null;
	}

}
