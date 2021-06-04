package com.pmzhongguo.ex.aspect;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.pmzhongguo.ex.business.dto.OTCOrderReq;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.entity.OTCAds;
import com.pmzhongguo.ex.business.service.OTCService;
import com.pmzhongguo.ex.core.utils.JedisUtilMember;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;

/**
 * 卖出通知
 * 
 * @author zengweixiong
 *
 */
@Aspect
@Component
public class SellNotice {

	/**
	 * 广告id
	 */
	private Integer adId;

	/**
	 * 买入卖出类型: 1表示买入订单，2表示卖出订单,
	 * 
	 */
	private Integer type;

	/**
	 * 账号类型
	 */
	private String accountTypes;

	private HttpServletRequest request;

	@Resource
	private OTCService otcService;

	/**
	 * 提交OTC订单接口
	 */
	@Pointcut("execution(public * com.pmzhongguo.ex.api.controller.OTCApiController.createOTCOrder(..))")
	public void sellNoticeCut() {
	}

	/**
	 * 获取接口请求参数
	 * 
	 * @param joinPoint
	 */
	@Before("sellNoticeCut()")
	public void before(JoinPoint joinPoint) {
		// 获取切面 方法的第三个参数
		this.request = (HttpServletRequest) joinPoint.getArgs()[0];
		OTCOrderReq orderReq = (OTCOrderReq) joinPoint.getArgs()[2];
		// System.err.println("买卖下单请求参数 : " + JsonUtil.toJson(orderReq));
		this.type = 1;
		// 卖出请求
		if (orderReq.getAd_type() == 2) {
			this.adId = orderReq.getAds_id();
			this.type = 2;
			// bank,alipay,wxpay
			this.accountTypes = orderReq.getAccount_types();
		}
	}

	/**
	 * 接口调用后 判断是否发送通知
	 * 
	 * @param ref
	 */
	@AfterReturning(returning = "ref", pointcut = "sellNoticeCut()")
	public void afterReturning(Object ref) {
		// 是卖出下单请求
		if (null == this.adId) {
			return;
		}
		if (2 != this.type) {
			return;
		}
		// System.err.println("响应返回值: " + JsonUtil.toJson(ref));
		ObjResp resp = (ObjResp) ref;
		// 接口请求响应成功, 获取短信发送内容数据, 发送短信
		if (Resp.SUCCESS == resp.getState()) {
			String tel = getTel(this.adId, this.accountTypes);
			if (null != tel) {
				// 发送短信
				// 获取登录用户的手机号
				Member member = JedisUtilMember.getInstance().getMember(this.request, null);
				String account = member.getM_name();
				String content = new StringBuilder("【币根网】您有一条 ").append(account).append(" 的用户，类型为卖的订单，请及时处理。")
						.toString();
				// System.err.println(content);
				NoticeUtils.sendSms(content, tel);
			}
		}
	}

	/**
	 * 根据广告ID获取广告主手机号
	 * 
	 * @param id
	 * @return
	 */
	private String getTel(int id, String type) {
		OTCAds ads = otcService.findOTCAdsById(id);
		String tel = null;
		if ("bank".equals(type)) {
			tel = ads.getBank_info().split("▲")[3];
		}
		if ("alipay".equals(type)) {
			tel = ads.getAlipay_info().split("▲")[1];
		}
		if ("wxpay".equals(type)) {
			tel = ads.getWxpay_info().split("▲")[1];
		}
		return tel;
	}

}
