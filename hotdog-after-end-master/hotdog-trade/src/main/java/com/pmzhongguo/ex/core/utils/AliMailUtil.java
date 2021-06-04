package com.pmzhongguo.ex.core.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: 使用<SPAN class=hilite1>java</SPAN>mail发送邮件 Description: 演示如何使用<SPAN
 * class=hilite1>java</SPAN>mail包发送电子邮件。这个实例可发送多附件
 * 
 * @version 1.0
 */
public class AliMailUtil {

	private static Logger logger = LoggerFactory.getLogger(AliMailUtil.class);

	public static boolean sendMail(String toAddress, String title, String content) {
		boolean flag = true;

		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI9bhNVLbgJ3qb", "gvUl93OKFMuv8G9yu4DJ2K7fWJi3jB");
		IAcsClient client = new DefaultAcsClient(profile);
		SingleSendMailRequest request = new SingleSendMailRequest();
		try {
			request.setAccountName("wecoin@wb.im");
			request.setAddressType(1);
			request.setReplyToAddress(true);
			request.setToAddress(toAddress);
			request.setSubject(title);
			request.setHtmlBody(content);
			SingleSendMailResponse httpResponse = client.getAcsResponse(request);
		} catch (ServerException e) {
			logger.error("send mail error,error info:{}, ---- toAddress-title-content:{}",e.getMessage(),toAddress+"-"+title+"-"+content);
			flag = false;
			e.printStackTrace();
		} catch (ClientException e) {
			logger.error("send mail error,error info:{}, ---- toAddress-title-content:{}",e.getMessage(),toAddress+"-"+title+"-"+content);
			flag = false;
			e.printStackTrace();
		}

		return flag;
	}
	
	public static void main(String[] args) {
		StringBuffer mailContent = new StringBuffer();
		mailContent.append("<table align='left' border='0' cellpadding='0' cellspacing='0' style='border-collapse:collapse;margin:0;padding:0;'>");
		mailContent.append("<tbody>");
		mailContent.append("<tr>");
		mailContent.append("<td valign='top' style='margin:0;padding:0;border-top:0;height:100%!important;width:100%!important'>");
		mailContent.append("<div style='padding: 20px; margin: 20px; width: 500px; border: 1px solid #D4D4D4;'>");
		mailContent.append("尊敬的用户： 您的验证码是：" + 88888 + "。请不要把验证码泄露给其他人。 感谢您对ZZEX的支持,祝您生活愉快！ 【ZZEX】");
		mailContent.append("</div></td></tr></tbody></table>");
		sendMail("873236275@qq.com", "【ZZEX】验证码", mailContent.toString());
	}
}