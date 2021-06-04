package com.pmzhongguo.contract.contractenum;

import java.util.HashMap;
import java.util.Map;

/**
 * 合约划转类型枚举
 * 1 币币转合约
 * 2 合约转币币
 * 3 币币转逐仓
 * 4 逐仓转币币
 * 5 法币转合约
 * 6 合约转法币
 * 7 法币转逐仓
 * 8 逐仓转法币
 * @author zn
 *
 */
public enum ContractOperateTypeEnum {

	ADDBALANCE(101, "addBalance"),
	OUTBALNCE(102, "outBalance"),
	ADDZCBALANCE(103, "addZcBalance"),
	OUTZCBALANCE(104, "outZcBalance"),
	OTCADDBALANCE(105, "otcAddBalance"),
	OTCOUTBALANCE(106, "otcOutBalance"),
	OTCADDZCBALANCE(107, "otcAddZcBalance"),
	OTCOUTZCBALANCE(108, "otcOutZcBalance"),
	HANDLETRANSBALANCE(109, "handleTransBalance"),
	HANDLETRANSZCBALANCE(110, "handleTransZcBalance"),
	ADDGDBALANCE(111, "addGdBalance"),
	OUTGDBALNCE(112, "outGdBalance");

	private int type;
	private String code;
	private static Map<String, ContractOperateTypeEnum> stringMap = new HashMap<String, ContractOperateTypeEnum>();
	static {
		for (ContractOperateTypeEnum a : ContractOperateTypeEnum.values()) {
			stringMap.put(a.toString(), a);
		}
	}

	private ContractOperateTypeEnum(int type, String code) {
		this.type = type;
		this.code = code;
	}

	public int getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public static ContractOperateTypeEnum getAccountOperateTypeEnum(String key) {
		return stringMap.get(key);
	}

	public static ContractOperateTypeEnum getEnumByType(int type) {
		for (ContractOperateTypeEnum a : ContractOperateTypeEnum.values()) {
			if (a.getType() == type) {
				return a;
			}
		}
		return null;
	}

	public static ContractOperateTypeEnum getEnumByCode(String code) {
		for (ContractOperateTypeEnum a : ContractOperateTypeEnum.values()) {
			if (a.getCode().equals(code)) {
				return a;
			}
		}
		return null;
	}
}
