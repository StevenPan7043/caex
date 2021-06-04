package com.pmzhongguo.ex.core.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KeySecretUtil {
	/**
	 * @description:随机获取key值
	 * @return
	 */
	public static String guid() {
		UUID uuid = UUID.randomUUID();
		String key = uuid.toString();
		return key;
	}

	/**
	 * 这是其中一个url的参数，是GUID的，全球唯一标志符
	 * 
	 * @param product
	 * @return
	 */
	public static String App_key() {
		return guid();
	}

	/**
	 * 根据md5加密
	 * 
	 * @param product
	 * @return
	 */
	public static String App_screct() {
		String app_sign = MacMD5.CalcMD5(guid(), 32).toUpperCase();// 得到以后还要用MD5加密。
		return app_sign;
	}

	/**
	 * 获得秘钥对
	 * @return
	 */
	public static Map<String, String> genKeyScrectPair() {
		Map<String, String> pair = new HashMap<String, String>();
		
		String apiKey = KeySecretUtil.App_key();
		String apiScrect = KeySecretUtil.App_screct();
		
		pair.put("api_key", apiKey);
		pair.put("api_secret", apiScrect);
		
		return pair;
	}
	
	public static void main(String[] args) {
		System.out.println(genKeyScrectPair());
	}
}
