package com.contract.common;

import com.contract.entity.CContractOrder;

import java.util.ArrayList;
import java.util.List;

public class ListUtils{

	
	public static <T> List<T> getList(List<T> val) {
		if(val==null || val.size()<1) {
			val=new ArrayList<T>();
		}
		return val;
	}

	public static List<CContractOrder> winList = new ArrayList<>();

	public static void addWinOrder(CContractOrder order){
		if(winList.size()>=20){
			winList.remove(0);
		}
		winList.add(order);
	}
}
