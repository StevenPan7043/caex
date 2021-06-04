package com.pmzhongguo.ex.aspect;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.qiniu.util.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.pmzhongguo.ex.business.dto.OTCOrderConfirmReq;
import com.pmzhongguo.ex.business.entity.CoinRecharge;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.entity.OTCAds;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.business.service.OTCService;
import com.pmzhongguo.ex.core.utils.JedisUtilMember;
import com.pmzhongguo.ex.core.web.resp.Resp;

/**
 * 买入通知
 * 
 * @author zengweixiong
 *
 */
@Aspect
@Component
public class BuyNotice {

	/**
	 * 请求参数
	 */
	private Long id;

	@Resource
	protected MemberService memberService;

	@Resource
	private OTCService otcService;

	private HttpServletRequest request;

	/**
	 * 提交OTC订单接口
	 */
	@Pointcut("execution(public * com.pmzhongguo.ex.api.controller.OTCApiController.confirmOTCOrder(..))")
	public void buyNoticeCut() {
	}

	/**
	 * 获取接口请求参数
	 * 
	 * @param joinPoint
	 */
	@Before("buyNoticeCut()")
	public void before(JoinPoint joinPoint) {
		OTCOrderConfirmReq otcOrderConfirmReq = (OTCOrderConfirmReq) joinPoint.getArgs()[2];
		this.id = otcOrderConfirmReq.getId();
		this.request = (HttpServletRequest) joinPoint.getArgs()[0];
		// System.err.println("OTC买单已付款请求参数 : " + JsonUtil.toJson(otcOrderConfirmReq));
	}

	/**
	 * 接口调用后 判断是否发送通知
	 * 
	 * @param ref
	 */
	@AfterReturning(returning = "ref", pointcut = "buyNoticeCut()")
	public void afterReturning(Object ref) {
		// // 是卖出下单请求
		// System.err.println("响应返回值: " + JsonUtil.toJson(ref));
		Resp resp = (Resp) ref;
		// 接口请求响应成功, 获取短信发送内容数据, 发送短信
		if (Resp.SUCCESS == resp.getState()) {
			CoinRecharge coinRecharge = memberService.getCoinRecharge(id);
			String tel = getTel(coinRecharge.getOtc_ads_id());
			if (null != tel) {
				// 发送短信
				// 获取登录用户的手机号
				Member member = JedisUtilMember.getInstance().getMember(this.request, null);
				String account = member.getM_name();
				String content = new StringBuilder("【币根网】您有一条 ").append(account).append(" 的用户，类型为买的订单，请及时处理。")
						.toString();
				// System.err.println(content);
				NoticeUtils.sendSms(content, tel);
			}
		}
	}

	/**
	 * 根据广告ID获取广告主手机号
	 * 644023
	 * @param id
	 * @return
	 */
	private String getTel(int id) {
		OTCAds ads = otcService.findOTCAdsById(id);
		String bankInfo = ads.getBank_info();
		String alipayInfo = ads.getAlipay_info();
		String wxpayInfo = ads.getWxpay_info();
		if (!StringUtils.isNullOrEmpty(bankInfo)) {
			return bankInfo.split("▲")[3];
		}
		if (!StringUtils.isNullOrEmpty(alipayInfo)) {
			return alipayInfo.split("▲")[1];
		}
		if (!StringUtils.isNullOrEmpty(wxpayInfo)) {
			return wxpayInfo.split("▲")[1];
		}
		return null;
	}
}
