package com.contract.enums;

import java.util.ArrayList;
import java.util.List;

import com.contract.common.FunctionUtils;

/**
 * 杠杆倍数
 * @author arno
 *
 */
public enum GearEnums {
	p5(5),
	p10(10),
	p20(20),
	p30(30),
	p50(50),
	p100(100);
	
	private Integer gearing;

	public Integer getGearing() {
		return gearing;
	}

	public void setGearing(Integer gearing) {
		this.gearing = gearing;
	}
	
	private GearEnums(Integer gearing) {
		this.gearing=gearing;
	}

	
	public static boolean isExist(Integer gearing) {
		boolean flag=false;
		for(GearEnums e:GearEnums.values()) {
			if(FunctionUtils.isEquals(gearing, e.getGearing())) {
				return true;
			}
		}
		return flag;
	}
	
	public static List<Integer> queryGearing(){
		List<Integer> list=new ArrayList<>();
		for(GearEnums e:GearEnums.values()) {
			list.add(e.getGearing());
		}
		return list;
	}
}
