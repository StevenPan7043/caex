package com.pmzhongguo.otc.otcenum;

import java.util.HashMap;
import java.util.Map;

/**
 * 	
 * @author admin
 *
 */
public enum MailInfoTemplateEnum implements TemplateEnum{


//	买信息：【ZZEX】您已向#name#用户购买#volu# #base#，出售单价为#pric# #quote#，订单号为#id#，请于#m#分钟内付款。
	MAIL_TRADE_BOUGHT(127972, "【ZZEX】您已向%1$s用户购买%2$s %3$s，出售单价为%4$s %5$s，订单号为%6$s，请于%7$s分钟内付款。"),
//	卖方信息：【ZZEX】您已向#name#用户出售#volu# #base#，出售单价为#pric# #quote#， 订单号为#id#，等待对方支付。
	MAIL_TRADE_SOLD(127970, "【ZZEX】您已向%1$s用户出售%2$s %3$s，出售单价为%4$s %5$s， 订单号为%6$s，等待对方支付。"),
//	“已付款” 买方信息：【ZZEX】#name#将订单#id#标记为“已付款”状态，请及时登录收款账户查看并确认放行。
	MAIL_TRADE_PAID(127973, "【ZZEX】%1$s将订单%2$s标记为“已付款”状态，请及时登录收款账户查看并确认放行。"),
//	“确认放行” 商家信息：【ZZEX】#name#已确认收到您的付款并放行，您所购买的#base#已发放到您的法币账户，请登录账户查收。
	MAIL_TRADE_CONFIRMED(127974, "【ZZEX】%1$s已确认收到您的付款并放行，您所购买的%2$s已发放到您的法币账户，请登录账户查收。"),
//	【ZZEX】#name#已将订单#id#取消，如有疑问请联系平台客服。
	MAIL_CANCELED_TRADE(127975, "【ZZEX】%1$s已将订单%2$s取消，如有疑问请联系平台客服。"),
//	【ZZEX】#name#已将订单#id#取消，如有疑问请联系平台客服。
	MAIL_COMPLAINING_TRADE(129999, "【ZZEX】%1$s已将订单%2$s发起申诉，平台客服将介入处理，请及时关注。"),
	//	【ZZEX】用户#name#对订单#id#提出申诉，请及时处理。
	MAIL_COMPLAINING_TRADE_WARN(132989, "【ZZEX】用户%1$s对订单%2$s提出申诉，请及时处理。"),
	//	【ZZEX】您的验证码是#code#，有效期#m#分钟
	MAIL_SECURITY_CODE(135867, "【ZZEX】您的验证码是%1$s，有效期%2$s分钟"),
	;

	private int type;
	private String code;
	private static Map<String, MailInfoTemplateEnum> stringMap = new HashMap<String, MailInfoTemplateEnum>();
	static {
		for (MailInfoTemplateEnum a : MailInfoTemplateEnum.values()) {
			stringMap.put(a.toString(), a);
		}
	}

	private MailInfoTemplateEnum(int type, String code) {
		this.type = type;
		this.code = code;
	}

	public int getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public static MailInfoTemplateEnum getAccountOperateTypeEnum(String key) {
		return stringMap.get(key);
	}

	public static MailInfoTemplateEnum getEnumByType(int type) {
		for (MailInfoTemplateEnum a : MailInfoTemplateEnum.values()) {
			if (a.getType() == type) {
				return a;
			}
		}
		return null;
	}
}
