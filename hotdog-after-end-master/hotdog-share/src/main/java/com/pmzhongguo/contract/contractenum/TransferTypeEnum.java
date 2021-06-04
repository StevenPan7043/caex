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
public enum TransferTypeEnum {

	ADDBALANCE(101, "addBalance","合约账户","币币账户"),
	OUTBALNCE(102, "outBalance","币币账户","合约账户"),
	ADDZCBALANCE(103, "addZcBalance","逐仓账户","币币账户"),
	OUTZCBALANCE(104, "outZcBalance","币币账户","逐仓账户"),
	OTCADDBALANCE(105, "otcAddBalance","合约账户","法币账户"),
	OTCOUTBALANCE(106, "otcOutBalance","法币账户","合约账户"),
	OTCADDZCBALANCE(107, "otcAddZcBalance","逐仓账户","法币账户"),
	OTCOUTZCBALANCE(108, "otcOutZcBalance","法币账户","逐仓账户"),
//	HANDLETRANSBALANCE(109, "handleTransBalance"),
//	HANDLETRANSZCBALANCE(110, "handleTransZcBalance")
	;

	private int type;
	private String code;
	private String transIn;
	private String transOut;
	private static Map<String, TransferTypeEnum> stringMap = new HashMap<String, TransferTypeEnum>();
	static {
		for (TransferTypeEnum a : TransferTypeEnum.values()) {
			stringMap.put(a.toString(), a);
		}
	}

	TransferTypeEnum(int type, String code, String transIn, String transOut) {
		this.type = type;
		this.code = code;
		this.transIn = transIn;
		this.transOut = transOut;
	}


	public int getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public String getTransIn() {
		return transIn;
	}

	public String getTransOut() {
		return transOut;
	}

	public static TransferTypeEnum getAccountOperateTypeEnum(String key) {
		return stringMap.get(key);
	}

	public static TransferTypeEnum getEnumByType(int type) {
		for (TransferTypeEnum a : TransferTypeEnum.values()) {
			if (a.getType() == type) {
				return a;
			}
		}
		return null;
	}

	public static TransferTypeEnum getEnumByCode(String code) {
		for (TransferTypeEnum a : TransferTypeEnum.values()) {
			if (a.getCode().equals(code)) {
				return a;
			}
		}
		return null;
	}
}
