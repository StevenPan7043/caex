package com.contract.common.number;

import java.util.Random;

import com.contract.enums.RandNumType;

/**
 * 随机数生成工具类
 * @author wangjiahao
 * 时间：2016-3-17 15:27:47
 */
public class RandNumUtils {
	/**
	 * 获取随机数
	 * @param randNumType 获取类型枚举
	 * @param count 位数
	 * @return 随机数字符串
	 */
	public static String get(RandNumType randNumType,int count){
		if(count<=0){
			throw new RuntimeException("随机数生成位数不能小于等于0！");
		}
		Random random = new Random();
		StringBuffer buffer = new StringBuffer();
		String[] randomStr = null;
		switch(randNumType){
		case NUMBER:
			randomStr = new String[]{"0","1","2","3","4","5","6","7","8","9"};
			break;
		case LETTER:
			randomStr = new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
			break;
		case SYMBOL:
			randomStr = new String[]{"_","/","=","-","+","!","\\",",",".","\""};
			break;
		case NUMBER_LETTER:
			randomStr = new String[]{"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
			break;
		case NUMBER_LETTER_SYMBOL:
			randomStr = new String[]{"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","_","/","=","-","+","!","\\",",",".","\""};
			break;
		}
		while(count>0){
			buffer.append(randomStr[random.nextInt(randomStr.length)]);
			count--;
		}
		return buffer.toString();
	}
	
	public static void main(String[] args) {
		String string = RandNumUtils.get(RandNumType.NUMBER, 6);
		System.out.println(string);
	}
}
