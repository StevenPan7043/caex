package com.pmzhongguo.ex.core.gt;

/**
 * GeetestWeb配置文件
 * 
 *id：74f2c2e74de4cdb1be1ab8388f74f857
 key：f7b931a6da32a62327d0e637f0362486
 */
public class GeetestConfig {

	// 填入自己的captcha_id和private_key
	private static final String geetest_id = "54199d9b8252319281cfe0311595b1f6";
	private static final String geetest_key = "fab72102a4b205ed807933f0da805a09";
	private static final boolean newfailback = true;

	public static final String getGeetest_id() {
		return geetest_id;
	}

	public static final String getGeetest_key() {
		return geetest_key;
	}
	
	public static final boolean isnewfailback() {
		return newfailback;
	}

}
