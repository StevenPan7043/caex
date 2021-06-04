package com.pmzhongguo.otc.sms;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.*;
import com.pmzhongguo.otc.otcenum.MobiInfoTemplateEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 亚马逊短信发送 如果信息内容前缀加【xxx】亚马逊默认会在后面加上[aws]由于短信文本不会超过140 个 ASCII
 *               字符，可以用空格挤掉
 *               参考链接：https://amazonaws-china.com/tr/sdk-for-java/?nc1=h_ls，https://docs.aws.amazon.com/zh_cn/sns/latest/dg/sms_preferences.html
 * @date: 2018-12-14 10:14
 * @author: 十一
 */
@Service
@EnableAsync
public class AwsSnsUtil {
	
	protected Logger logger = LoggerFactory.getLogger(AwsSnsUtil.class);

	private Map<String, MessageAttributeValue> smsAttributes;
	private static MyAWSCredentialsProvider provider = new MyAWSCredentialsProvider();
	private static MyAWSCredentials credentials = new MyAWSCredentials();

	public Map<String, MessageAttributeValue> getDefaultSMSAttributes() {
		if (smsAttributes == null) {
			smsAttributes = new HashMap<>();
			smsAttributes.put("AWS.SNS.SMS.SenderID",
					new MessageAttributeValue().withStringValue("1").withDataType("String"));
			smsAttributes.put("AWS.SNS.SMS.MaxPrice",
					new MessageAttributeValue().withStringValue("99.00").withDataType("Number"));
			smsAttributes.put("AWS.SNS.SMS.SMSType",
					new MessageAttributeValue().withStringValue("Transactional").withDataType("String"));
		}
		return smsAttributes;
	}

	@Async
	public PublishResult sendSMSMessage(String phoneNumber, String message) {
		return sendSMSMessage(phoneNumber, message, getDefaultSMSAttributes());
	}

	private static class MyAWSCredentials implements AWSCredentials {

		@Override
		public String getAWSAccessKeyId() {
			// 带有发短信权限的 IAM 的 ACCESS_KEY，在IAM控制台获得
			return "AKIAJRI5LWRW4CE3BOKQ";
		}

		@Override
		public String getAWSSecretKey() {
			// 带有发短信权限的 IAM 的 SECRET_KEY，在IAM控制台获得
			return "5vxwIfiksGgLhQ5eTl88AT2pR7ABRwFchRgkVRO6";
		}
	}

	private static class MyAWSCredentialsProvider implements AWSCredentialsProvider {
		@Override
		public AWSCredentials getCredentials() {
			return credentials;
		}

		@Override
		public void refresh() {
		}
	}

//    ap-northeast-1  us-east-1
	public PublishResult sendSMSMessage(String phoneNumber, String message,
			Map<String, MessageAttributeValue> smsAttributes) {
		AmazonSNS amazonSNS = null;
		try {
			amazonSNS = AmazonSNSClientBuilder.standard().withCredentials(provider).withRegion("us-east-1")
					.build();
		} catch (Exception e) {
			logger.error("AmazonSNS Exception! cause:{}", e.getCause(), e);
			return null;
		}
		message = checkStr(message, 70);
		return amazonSNS.publish(new PublishRequest().withMessage(message)
				.withPhoneNumber("+86" + phoneNumber).withMessageAttributes(smsAttributes));
	}

	public static void main(String[] args) {
		AwsSnsUtil awsSMS = new AwsSnsUtil();
		// 15174026277 这是美国的号
		// 手机格式：+ 国家地区代码 手机号码。例如：+8613097368626
		// 如果信息内容前缀加【xxx】亚马逊默认会在后面加上[aws]由于短信文本不会超过140，可以用空格挤掉
		//1
//		String template = MobiInfoTemplateEnum.TRADE_BOUGHT.getCode();
//		template = template.format(template, "天下无双", "50", "USDT", "20", "CNY", "T201901101616418E9");
//		System.out.println(template);
		//2
//		String template = MobiInfoTemplateEnum.TRADE_SOLD.getCode();
//		template = template.format(template, "天下无双", "50", "USDT", "20", "CNY", "T201901101616418E9", "30");
		//3
//		String template = MobiInfoTemplateEnum.TRADE_PAID.getCode();
//		template = template.format(template, "天下无双",  "T201901101616418E9");
		//4
//		String template = MobiInfoTemplateEnum.TRADE_CONFIRMED.getCode();
//		template = template.format(template, "天下无双",  "USTD");
		//5
//		String template = MobiInfoTemplateEnum.AWS_CANCELED_TRADE.getCode();
//		template = template.format(template, "天下无双",  "T201901101616418E9");
//		template = checkStr(template, 70);
//		PublishResult publishResult = awsSMS.sendSMSMessage("13916155453", template);
//		System.out.println(publishResult);
	}
	
	/**
	 * 	将短信长度补到 70个字符以免出现AWS的签名
	 * @param str
	 * @param length
	 * @return
	 */
	private static String checkStr(String str, int length) {
		int strLen;
		if (str == null) {
			strLen = 0;
		}else{
			strLen= str.length();
		}
		if (strLen >= length) {
			return str;
		} else {
			int temp = length - strLen;
			String tem = "";
			for (int i = 0; i < temp; i++) {
				tem = tem + " ";
			}
			return str + tem;
		}
	}
}
