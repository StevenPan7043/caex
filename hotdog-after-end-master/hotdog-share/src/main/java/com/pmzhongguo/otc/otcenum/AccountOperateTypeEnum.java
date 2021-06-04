package com.pmzhongguo.otc.otcenum;

import java.util.HashMap;
import java.util.Map;

/**
 * 	1 增加总额[+total_balance]
 * 	2 冻结[+frozen_balance]
 * 	3 还原冻结[-frozen_balance]
 * 	4 解冻[-frozen_balance，同时-total_balance]
 * 	还原冻结和解冻的区别是：解冻是成交，还原冻结是取消
 * 	5 转入 6 转出
 * 	7 申请商家 (冻结)
 * 	8 申请通过 (解冻)
 * 	9 申请驳回 (还原冻结)
 * 	10 退出商家资格(转入)
 * 	11 项目方扣款失败
 * @author admin
 *
 */
public enum AccountOperateTypeEnum {
	
	ADDTOTAL(1, "addTotal"), 
	FROZEN(2, "addFrozen"), 
	RETURNFROZEN(3, "returnFrozen"), 
	REDUCEFROZEN(4, "reduceFrozen"), 
	INTO(5, "into"), 
	OUT(6, "out"),
	APPLY(7, "apply"),
	APPLY_PASSED(8, "apply_passed"),
	APPLY_REJECT(9, "apply_reject"),
	SECEDE(10, "secede"),
	FAIL(11,"fail");
	
	private int type;
	private String code;
	private static Map<String, AccountOperateTypeEnum> stringMap = new HashMap<String, AccountOperateTypeEnum>();
	static {
		for (AccountOperateTypeEnum a : AccountOperateTypeEnum.values()) {
			stringMap.put(a.toString(), a);
		}
	}

	private AccountOperateTypeEnum(int type, String code) {
		this.type = type;
		this.code = code;
	}

	public int getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public static AccountOperateTypeEnum getAccountOperateTypeEnum(String key) {
		return stringMap.get(key);
	}

	public static AccountOperateTypeEnum getEnumByType(int type) {
		for (AccountOperateTypeEnum a : AccountOperateTypeEnum.values()) {
			if (a.getType() == type) {
				return a;
			}
		}
		return null;
	}

	public static AccountOperateTypeEnum getEnumByCode(String code) {
		for (AccountOperateTypeEnum a : AccountOperateTypeEnum.values()) {
			if (a.getCode().equals(code)) {
				return a;
			}
		}
		return null;
	}
}
