package com.contract.common.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.contract.common.GetRest;
import com.contract.common.HttpUtil;
import com.contract.common.RestResponse;
import com.contract.common.mail.yun.SmsSingleSender;
import com.contract.common.mail.yun.SmsSingleSenderResult;
import com.contract.exception.ThrowJsonException;

public class MailUtils {

	public static MailUtils mailUtils = null;

	public static MailUtils getInstance() {
		if (mailUtils == null) {
			synchronized (MailUtils.class) {
				if (mailUtils == null) {
					mailUtils = new MailUtils();
				}
			}
		}
		return mailUtils;
	}
	
	public static String accesskey = "5ca1b5d687b65f7cf39f8e09";
	public static String secretkey = "f22ee26413e448a192749abb433be08d";
	 
	 public RestResponse sendEmail(String email, String content) {
	    	try {
	    		//请根据实际 accesskey 和 secretkey 进行开发，以下只作为演示 sdk 使用
	    		// 邮件类型，0 事务投递，其他的为商业投递量
	    		int type=0;
	    		// 拓展字段
	    		String ext="";
	    		// 必须 管理控制台中配置的发信地址(登陆后台查看发信地址)
	    		String fromEmail="mail@service.shuojianghu.com";
	    		//// 必须 ,如果为true是的时候，replyEmail必填，false的时候replyEmail可以为空
	    		Boolean needToReply=false;
	    		 // 如果needToReply为true是的时候,则为必填,false的时候replyEmail可以为空
	    		String replyEmail=""; 
	    		// 必须 目标邮件地址
	    		String toEmail=email; 
	    		//// 可选 发信人昵称,
	    		String fromAlias="BTXcion";
	    		// 必须 邮件主题。
	    		String subject="验证码";
	    		// 必须 邮件 html 正文。
	    		String htmlBody=content;
	    		// 可选 取值范围 0~1: 1 为打开数据跟踪功能; 0 为关闭数据跟踪功能。该参数默认值为
	    		String clickTrace="1";
	    		String readTrace="1";
		    	EmailSingleSender singleSender = new EmailSingleSender("5c7a3bc987b65f03f99dbff1", "49fee4a34e4e47dcb730f94f46068315");
		    	EmailSingleSenderResult singleSenderResult=singleSender.send(type, fromEmail, toEmail, fromAlias, needToReply, replyEmail, subject, htmlBody, clickTrace,readTrace, ext);
		    if(singleSenderResult.getResult()!=0) {
		    		return GetRest.getFail(singleSenderResult.getErrmsg());
		    }
		    return GetRest.getSuccess("发送成功");
	    	} catch (Exception e) {
			throw new ThrowJsonException("发送邮件失败");
		}
	}
	 
	 
	 public RestResponse mobileQuery(String phoneNumber,String validcode) {
	    	try {
	    		//type:0普通短信 1营销短信
	    		int type=0;
	    		//国家区号
	    		String nationcode="86";
	    		//短信模板的变量值 ，将短信模板中的变量{0},{1}替换为参数中的值，如果短信模板中没有变量，则这个值填null
	    		List<String> params=new ArrayList<String>();
	    		//模板中存在多个可变参数，可以添加对应的值。
	    		params.add(validcode);
	    		//自定义字段，用户可以根据自己的需要来使用
	    		String ext="";
	    		 //初始化单发
		    	SmsSingleSender singleSender = new SmsSingleSender(accesskey, secretkey);
		    	 //普通单发,注意前面必须为【】符号包含，置于头或者尾部。
		    	SmsSingleSenderResult singleSenderResult = singleSender.send(type, nationcode, phoneNumber, "5c9f30e987b65f7cf39e1f5d", "5a9599b66fcafe461546ba55", params, ext);
		    	if(singleSenderResult.result!=0) {
		    		return GetRest.getFail(singleSenderResult.errMsg);
		    }
		    return GetRest.getSuccess("发送成功");
	    	} catch (Exception e) {
			throw new ThrowJsonException("发送短信失败");
		}
	}
	 
	 /**
		 * 聚合发送短信
		 * 
		 * @param phone
		 * @param code
		 * @return
		 */
		public RestResponse sendSms(String phone, String code) {
			String result = null;
			String url = "http://v.juhe.cn/sms/send";// 请求接口地址
			Map<String, String> params = new HashMap<String, String>();// 请求参数
			params.put("mobile", phone);// 接受短信的用户手机号码
			params.put("tpl_id","174466");// 您申请的短信模板ID，根据实际情况修改
			params.put("tpl_value", "#code#=" + code);// 您设置的模板变量，根据实际情况修改
			params.put("key","0aab8076019c85a874d3bb804c216ea6");// 应用APPKEY(应用详细页查询)
			try {
				result = HttpUtil.getInstance().get(url, params);
				JSONObject object = JSONObject.parseObject(result);
				if (object.getIntValue("error_code") == 0) {
					JSONObject object2 = object.getJSONObject("result");
					return GetRest.getSuccess("发送成功", object2.getString("sid"));
				} else {
					return GetRest.getFail(object.getString("reason"));
				}
			} catch (Exception e) {
				return GetRest.getFail("发送失败");
			}
		}
}
