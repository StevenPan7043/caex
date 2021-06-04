package com.contract.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 正则表达式工具类
 * @author Arno
 *
 */
public class RpxUtils {

	/**
	 * 密码校验正则表达式
	 */
	private static final String passwor_rpx="[a-zA-Z0-9]{6,14}";
	
	private static final String login_rpx="[a-zA-Z0-9]{6,11}";
	
	private static final String nickname_rpx="[`~!@#$%^&*()+=|{}':;'_,\\[\\].<>/?~！@#￥%……&*（）— —+|{}【】‘；：”“’。，、？-]";
	
	private static final String url_rpx="((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
	
	private static final String paypwd_rpx="[\\d]{6}";
	
	 /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	/**
	 * url校验
	 * @param url
	 * @return
	 */
	public static String valid_url(String url){
		if(url.matches(url_rpx)){
			if(url.lastIndexOf("/")!=url.length()-1){
				return "URL地址必须以/结束";
			}
			return "";
		}else{
			return "请输入正确的URL地址";
		}
	}
	/**
	 * 校验账号 只能为字母加数字的6-20位
	 * @param login
	 * @return
	 */
	public static String valid_login(String login){
		if(login.matches(login_rpx)){
			return "";
		}else{
			return "用户名只能为英文加数字,且在6-11位之间";
		}
	}
	/**
	 * 校验密码 只能为字母加数字的6-20位
	 * @param password
	 * @return
	 */
	public static String valid_password(String password){
		if(password.matches(passwor_rpx)){
			return "";
		}else{
			return "密码必须为6-14位的数字加字母";
		}
	}
	
	/**
     * 校验邮箱
     * 
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static String isEmail(String email) {
    		boolean flag=Pattern.matches(REGEX_EMAIL, email);
    		if(!flag) {
    			return "请输入正确邮箱格式";
    		}
        return "";
    }
	
	/**
	 * 校验昵称只能在20位以内，且不能包干特殊字符
	 * @param nickname
	 * @return
	 */
	public static String valid_nickname(String nickname){
		nickname=nickname.trim();
		Pattern p = Pattern.compile(nickname_rpx);
		Matcher m = p.matcher(nickname);
		if(nickname.length()>20){
			return "昵称不能包含特殊字符且不能超过20位";
		}
		if(m.find()){
			return "昵称不能包含特殊字符且不能超过20位";
		}else{
			return "";
		}
	}
	
	/**
	 * 校验手机号
	 * @param phone
	 * @return
	 */
	public static String valid_phone(String phone){
		if(StringUtils.isEmpty(phone)) {
			return "	请输入手机号";
		}
		if(phone.length()!=11) {
			return "	手机号长度不正确";
		}
		 String regex = "^[1](([3][0-9])|([4][5,7,9])|([5][0-9])|([6][0-9])|([7][0-9])|([8][0-9])|([9][0-9]))[0-9]{8}$";
		 Pattern p = Pattern.compile(regex);
         Matcher m = p.matcher(phone);
         boolean isMatch = m.matches();
		if(isMatch){
			return "";
		}else{
			return "手机号格式不正确";
		}
	}
	
	public static void main(String[] args) {
		System.out.println(valid_phone("15913779941"));
	}
	/**
	 * 校验交易密码
	 * @param paypwd
	 * @param ext
	 * @return
	 */
	public static String valid_paypwd(String paypwd) {
		Pattern p = Pattern.compile(paypwd_rpx);
        Matcher m = p.matcher(paypwd);
        boolean isMatch = m.matches();
        if(StringUtils.isEmpty(paypwd)) {
        		return "交易密码为6位数字";
        }else {
	        	if(isMatch){
	    			return "";
	    		}else{
	    			return "交易密码为6位数字";
	    		}
        }
	}
	
	/***
	 * 验证字符串中是否有非法字符
	 * @param content
	 * @return  true:不包含非法字符 false:包含非法字符
	 */
	public static  boolean verification(String content) {
		String regEx = "[`!@#$%^=|{}':;',\\[\\]<>/?！@#￥%……|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(content);
        return !m.find();
	}
}
