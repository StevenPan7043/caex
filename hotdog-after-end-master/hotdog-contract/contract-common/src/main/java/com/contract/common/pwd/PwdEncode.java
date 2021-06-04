package com.contract.common.pwd;

/**
 * 会员加密密码
 * @author arno
 *
 */
public class PwdEncode {

	/**
	 * 加密密码
	 * @param pwd
	 * @return
	 */
	public static String encodePwd(String pwd) {
		String p=Encode.encode(pwd);
		p=Encode.encode(p).toLowerCase();
		return p;
	}
	
	public static void main(String[] args) {
		System.out.println(encodePwd("123456"));
	}
}
