package com.contract.common.sms;

import com.contract.common.mail.LingKaiUtil;
import com.contract.common.mail.MailUtils;
import com.contract.common.mail.NewLingKaiUtil;
import com.contract.common.mail.SubMailUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

@Component
public class SmsSend implements Runnable {

	private static final String sign = "【CAEX】";

	String mobile;
	int tpl_id;
	String tpl_value;
	private WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();

	public SmsSend() {
	}

	public SmsSend(String mobile, int tpl_id, String tpl_value) {
		super();

		this.mobile = mobile;
		this.tpl_id = tpl_id;
		this.tpl_value = tpl_value + sign;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (mobile.contains("@")) {
			MailUtils.getInstance().sendEmail(mobile, tpl_value);
		} else {
//			LingKaiUtil.send(mobile, tpl_value);
//			SubMailUtil.send(mobile, tpl_value);
			try {
				NewLingKaiUtil.sendSMSGet(mobile, tpl_value, "");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

	}

}
