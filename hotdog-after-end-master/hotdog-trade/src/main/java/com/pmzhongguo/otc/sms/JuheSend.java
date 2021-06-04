package com.pmzhongguo.otc.sms;

import com.contract.common.mail.NewLingKaiUtil;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

@Component
public class JuheSend implements Runnable {

	private static final String sign = "【CAEX】";
	
	String mobile;
	int tpl_id;
	String tpl_value;
	private WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();

	public JuheSend() {
	}

	public JuheSend(String mobile, int tpl_id, String tpl_value) {
		super();
		if (mobile.contains("@")) {
            MemberService memberService = (MemberService) wac.getBean("memberService");
            Member member = memberService.getMemberBy(HelpUtils.newHashMap("m_name", mobile));
			mobile = member.getPhone();
		}
		this.mobile = mobile;
		this.tpl_id = tpl_id;
		this.tpl_value = tpl_value + sign;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
//		JuheUtil.send(mobile, tpl_id, tpl_value);
//		 LingKaiUtil.send(mobile,tpl_value);
//		SubMailUtil.send(mobile,tpl_value);
		try {
			NewLingKaiUtil.sendSMSGet(mobile, tpl_value, "");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
