package com.contract.enums;

import java.util.ArrayList;
import java.util.List;

import com.contract.common.FunctionUtils;

public enum HandleTypeEnums {

	recharge(1,"充值","recharge"),
	cash(2,"提币","cash"),
	sys(3,"系统操作","sys"),
	refund(4,"提币驳回","refund"),
	contract(5,"合约交易","contract"),
	orderout(6,"平仓","orderout"),
	trans(7,"转账","trans"),
	contract_tax(8,"交易手续费","contract_tax"),
	entrust(9,"委托交易","entrust"),
	entrust_cancel(10,"取消委托","entrust_cancel"),
	order_tax(11,"平仓手续费","order_tax"),
	transfer(12,"划转","transfer"),
	push(13,"推荐奖励","push"),
	zcorder(14,"逐仓交易","zcorder"),
	gdbouns(15,"跟单日收益","gdbouns"),
	buygd(16,"购买跟单","buygd"),
	gdteam(17,"跟单团队奖励","gdteam"),

	ADDBALANCE(101,"币币转合约", "addBalance"),
	OUTBALNCE(102,"合约转币币", "outBalance"),
	ADDZCBALANCE(103,"币币转逐仓", "addZcBalance"),
	OUTZCBALANCE(104,"逐仓转币币" ,"outZcBalance"),
	OTCADDBALANCE(105,"法币转合约", "otcAddBalance"),
	OTCOUTBALANCE(106,"合约转法币", "otcOutBalance"),
	OTCADDZCBALANCE(107,"法币转逐仓", "otcAddZcBalance"),
	OTCOUTZCBALANCE(108,"逐仓转法币", "otcOutZcBalance"),
	ADMIN_MODIFY_BALANCE(109,"管理员修改全仓", "admin_modify_balance"),
	ADMIN_MODIFY_ZCBALANCE(110,"管理员修改逐仓", "admin_modify_zcbalance"),;
	
	private Integer id;
	private String name;
	private String nameEn;
	HandleTypeEnums(Integer id, String name, String nameCn) {
		this.id = id;
		this.name = name;
		this.nameEn = nameCn;
	}



	
	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}




	private HandleTypeEnums(Integer id,String name) {
		this.id=id;
		this.name=name;
	}

	
	
	public static List<HandleTypeEnums> queryList(){
		List<HandleTypeEnums> list=new ArrayList<HandleTypeEnums>();
		for(HandleTypeEnums o:HandleTypeEnums.values()){
			list.add(o);
		}
		return list;
	}
	
	public static String getName(Integer id){
		String str="";
		for(HandleTypeEnums o:HandleTypeEnums.values()){
			if(FunctionUtils.isEquals(id, o.getId())){
				str= o.getName();
				break;
			}
		}
		return str;
	}

	public static String getNameCn(String nameCn) {
		String str = "";
		for (HandleTypeEnums o : HandleTypeEnums.values()) {
			if (nameCn.equalsIgnoreCase(o.getNameEn())) {
				str = o.getName();
				break;
			}
		}
		return str;
	}
	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
}
