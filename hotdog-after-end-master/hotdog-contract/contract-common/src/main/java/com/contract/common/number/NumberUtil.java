package com.contract.common.number;


/**
 * 
 * 数字处理
 * @author WahYee
 * @date 2018年5月12日 下午4:20:54
 */
public class NumberUtil {
	
	
	/**
	 * 
	 * 数字精简处理
	 * 10000 转 1万+
	 * 10001 转 1万
	 * @param i
	 * @return
	 * @author WahYee
	 * @date 2018年5月12日 下午4:26:42
	 */
	public static String numToMore(int i){
		if(i>10000){
			int r = i % 10000;
			if(r > 1000){
				return (i/10000) + "." + (r/1000) +"万+";
			}else{
				return (i/10000) +"万+";
			}
		}else if(i>1000){
			int r = i % 100;
			if(r > 0){
				return (i-r)+"+";
			}
		}else if(i>100){
			int r = i % 100;
			if(r > 0){
				return (i-r)+"+";
			}
		}else if(i>50){
			int r = i % 10;
			if(r > 0){
				return (i-r)+"+";
			}
		}
		return String.valueOf(i);
	}
	
	

}
