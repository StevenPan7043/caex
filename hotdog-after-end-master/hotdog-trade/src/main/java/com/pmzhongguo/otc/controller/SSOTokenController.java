package com.pmzhongguo.otc.controller;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.entity.ReqBaseSecret;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtilMember;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.transfer.entity.RespUserSecret;
import com.pmzhongguo.ex.transfer.service.SSOUsersTokenManager;
import com.pmzhongguo.otc.service.SSOTokenManager;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@ApiIgnore
@Api(value = "会员接口", description = "会员注册、账号、密码相关", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("m")
public class SSOTokenController extends TopController {

	@Autowired
	private SSOTokenManager sSOTokenManager;
	@Autowired
	private SSOUsersTokenManager ssoUsersTokenManager;
	
//	@ApiOperation(value = "获取当前用户token", notes = "获取当前用户token", httpMethod = "GET")
	@ApiIgnore
	@RequestMapping(value = "getSSOToken", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp getSSOToken(HttpServletRequest request) {
		Member member = JedisUtilMember.getInstance().getMember(request, null);
		if (null == member) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}
//		ReqBaseSecret reqBaseSecret = sSOTokenManager.getTokenSign(member.getId());
		ReqBaseSecret reqBaseSecret = sSOTokenManager.insert(member.getId());
		reqBaseSecret.setSign_type(null);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, reqBaseSecret);
	}
	
	@ApiOperation(value = "登录", notes = "输入字段：token", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "tokenLogin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ObjResp login(HttpServletRequest request,
						 HttpServletResponse response, @RequestBody ReqBaseSecret token) {
		token.setSign_type("MD5");
		ObjResp objResp = sSOTokenManager.validateToken(token);
		if(objResp.getState().intValue() == Resp.FAIL.intValue()) {
			return objResp;
		}
		Member member = memberService.getMemberById((Integer) objResp.getData());
		if(member != null) {
			member.setGoogle_auth_key(null);
		}
		memberService.loginCommon(request, member, true, null);
		if(objResp.getState() != 1) {
			return objResp;
		}
		return memberService.hideMemberInfo(request,member,true);
	}

	/**
	 * 用户token信息
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getUserSSOToken", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp getUserSSOToken(HttpServletRequest request) {
		Member member = JedisUtilMember.getInstance().getMember(request, null);
		if (null == member) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}
		// 登录校验
		ObjResp objResp = memberService.loginCommon(request, member, false);
		if (objResp.getState() != 1) {
			return objResp;
		}
		if (HelpUtils.isEmail(member.getM_name())) {
			if (StringUtils.isBlank(member.getPhone())) {
				return new ObjResp(Resp.FAIL, ErrorInfoEnum.PSL_SET_PHONE.getErrorENMsg(), null);
			}
		} else if (HelpUtils.isMobile(member.getArea_code() + "" + HelpUtils.removeZero(member.getM_name())) && HelpUtils.startsWithAreaCode(member.getArea_code()+"", HelpUtils.removeZero(member.getM_name()))) {
			member.setPhone(member.getM_name());
			member.setGoogle_auth_key(null);
		} else {
			member.setPhone(member.getM_name());
			member.setGoogle_auth_key(null);
		}

		RespUserSecret reqBaseSecret = ssoUsersTokenManager.getUserSSOToken(member, "lastWinner");
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, reqBaseSecret);
	}
}
