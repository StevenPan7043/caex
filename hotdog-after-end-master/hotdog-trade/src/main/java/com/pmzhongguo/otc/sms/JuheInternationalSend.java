package com.pmzhongguo.otc.sms;

import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

@Component
public class JuheInternationalSend implements Runnable {

	String mobile;
	int tpl_id;
	String tpl_value;
	String areaNum;
	private WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();

	public JuheInternationalSend() {
	}

	public JuheInternationalSend(String areaNum,String mobile, int tpl_id, String tpl_value) {
		super();
		MemberService memberService = (MemberService) wac.getBean("memberService");
		if (mobile.contains("@")) {
			Member member = memberService.getMemberBy(HelpUtils.newHashMap("m_name", mobile));
			mobile = member.getPhone();
		}
		this.mobile = mobile;
		this.tpl_id = tpl_id;
		this.tpl_value = tpl_value;
		this.areaNum = areaNum;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		JuheUtil.internationalSend(areaNum,mobile, tpl_id, tpl_value);
	}

}
