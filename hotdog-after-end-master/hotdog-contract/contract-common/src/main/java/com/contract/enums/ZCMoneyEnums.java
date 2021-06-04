package com.contract.enums;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 逐仓金额枚举
 * @author arno
 *
 */
public enum ZCMoneyEnums {

	
	
	p300(new BigDecimal(300)  ),p500(new BigDecimal(500) ),p1000(new BigDecimal(1000) ),p3000(new BigDecimal(3000) );
	private BigDecimal money;

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	private ZCMoneyEnums(BigDecimal money) {
		this.money=money;
	}
	
	public static List<BigDecimal> getMoneys(){
		List<BigDecimal> list=new ArrayList<>();
		for(ZCMoneyEnums m:ZCMoneyEnums.values()) {
			list.add(m.getMoney());
		}
		return list;
	}
	
	public static boolean isExist(BigDecimal money) {
		for(ZCMoneyEnums m:ZCMoneyEnums.values()) {
			if(money.compareTo(m.getMoney())==0) {
				return true;
			}
		}
		return false;
	}
}
