package com.contract.common.mail;

import com.contract.common.GetRest;
import com.contract.common.RestResponse;
import org.apache.commons.lang3.StringUtils;


public class SendMail {

	/**
	 *  * 发送验证码
	 * @param mail 邮箱
	 * @param mail  接收方邮箱地址
	 * @param validcode 验证码
	 * @return
	 */
	public static RestResponse sendValid(String mail, String validcode) {
		if(StringUtils.isBlank(mail)) {
			return GetRest.getFail("接收邮箱不能为空");
		}
		String content="您的验证码："+validcode+",本次操作验证码如下，请在10分钟内输入。如非本人操作，请忽略。";
		RestResponse result=MailUtils.getInstance().sendEmail(mail, content);
		return result;
	}
	
	/**
	 *  * 发送验证码
	 * @param phone 手机号
	 * @return
	 */
	public static RestResponse sendPhoneValid(String phone,String validcode) {
		if(StringUtils.isBlank(phone)) {
			return GetRest.getFail("接收手机号不能为空");
		}
		RestResponse result=MailUtils.getInstance().sendSms(phone,validcode);
		return result;
	}
}
