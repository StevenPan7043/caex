/**
 * 说明：MD5非摘要算法
 * 日期：Apr 10, 2014 创建文件
 */
package com.pmzhongguo.market.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class MacMD5 {
	private static byte[] digesta;

	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param myinfo
	 *            需要加密的字符串
	 * @return 通过MD5加密后的字符串
	 */
	public static String CalcMD5(String myinfo) {
		return CalcMD5(myinfo, 15);
	}
	
	public static String CalcMD5Member(String myinfo) {
		return CalcMD5(myinfo, 25);
	}

	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param myinfo
	 *            需要加密的字符串
	 * @return 通过MD5加密后的字符串
	 */
	public static String CalcMD5(String myinfo, int length) {
		try {
			MessageDigest alga = MessageDigest.getInstance("MD5");
			alga.update(myinfo.getBytes());
			digesta = alga.digest();
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return byte2hex(digesta, length);

	}

	private static String byte2hex(byte[] b, int length) { // 二行制转字符串
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}

		// return hs;
		// 2012.11.12
		return hs.substring(0, length);
	}

	public static void main(String[] args) {
		Map map = new HashMap();
		map.put("symbol","USDTZC");
		map.put("o_no","");
		map.put("api_key","24977c3e-8820-411b-8657-af537774652b");
		map.put("sign","7ea0bf58c7ba45d83063885f64e2");
		map.put("id","1");
		map.put("sign_type","MD5");
		map.put("timestamp","1551284058");
		String apiSecret = "EE10AA25BDDAB726ABDAEB70518ABD63";
		String serveSign = "7ea0bf58c7ba45d83063885f64e2";
//		String sign = HelpUtils.createSign(map, apiSecret);
//		String signIncludeValueNull = HelpUtils.createSignIncludeValueNull(map, apiSecret);
//		System.out.println("sign: " + sign);
//		System.out.println("signIncludeValueNull: " + signIncludeValueNull);
//		String doublesign = HelpUtils.createSignExcludeValNull(map, apiSecret);
//		System.out.println("doublesign: " + doublesign);
		// 客户端sign：9ca747e90e138aa55002f9733e30
		// 服务端sign：fa8082e19fd7e812f192f0d253bb
//		MacMD5 md5 = new MacMD5();
//		String encype = ""
//		System.out.println(md5.CalcMD5("api_key=ac181497-4866-4ed8-803b-93887c2c6ac8", 32));
//		map.put("symbol","BTCZC");
//		map.put("o_no","");
//		map.put("api_key","cdc74b97-3e92-4277-9460-cae1083865b9");
//		map.put("sign","fa8082e19fd7e812f192f0d253bb");
//		map.put("id","2");
//		map.put("sign_type","MD5");
//		map.put("timestamp",1551024389);
//		String apiSecret = "91CFB72205E9DBC1906C7E2179EF8E7E";
//		String sign = HelpUtils.createSign(map, apiSecret);
//		String doublesign = HelpUtils.createSignIncludeValueNull(map, apiSecret);
//		System.out.println("去除null："+sign);
//		System.out.println("加上null："+doublesign);
	}

}
