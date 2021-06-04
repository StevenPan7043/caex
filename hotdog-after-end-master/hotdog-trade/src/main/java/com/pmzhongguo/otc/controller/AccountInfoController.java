package com.pmzhongguo.otc.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.entity.AuthIdentity;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.IPAddressPortUtil;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.utils.JedisUtilMember;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.otc.entity.dto.AccountInfoDTO;
import com.pmzhongguo.otc.otcenum.WhetherEnum;
import com.pmzhongguo.otc.service.OTCAccountInfoManager;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

//@ApiIgnore
@Api(value = "NEW OTC收款账户操作", description = "账户操作相关", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("otc/AccountInfo")
public class AccountInfoController extends TopController {

	@Autowired
	private OTCAccountInfoManager oTCAccountInfoManager;

//	@ApiOperation(value = "创建收款账户信息", notes = "输入字段： 会员ID:memberId 类型:type(BANK|ALIPAY|WXPAY) 姓名:name 账号:account（银行卡号或微信支付宝账号） 银行卡开户行或微信支付宝的二维码图片: bankOrImg 资金密码:securityPwd", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiIgnore
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public ObjResp create(HttpServletRequest request, HttpServletResponse response,
			@RequestBody AccountInfoDTO record) {
		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}
		if (m.getId().intValue() != record.getMemberId()) {
			logger.warn("OTCAccountInfoManager. create() " + ErrorInfoEnum.LANG_ERROR_OPERATION_OBJECT.getErrorCNMsg()
					+ " \n record:" + record.toString() + " login objec:" + m.getId());
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_OPERATION_OBJECT.getErrorENMsg(), null);
		}
		//旧平台实名认证名字是星号的需要财务修改认证名字
		if(record.getName().indexOf("*") > -1) {
			logger.warn("memberId:" + record.getMemberId() + " name:" + record.getName() + " 旧平台实名认证名字是星号的需要财务修改认证名字后才能绑定。");
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_INVALID_BINDNAME.getErrorENMsg(), "姓名中不能有星号");
		}
		AuthIdentity authIdentity = memberService.getAuthIdentityById(m.getId());
		String name = authIdentity.getFamily_name() + authIdentity.getGiven_name();
		if(!record.getName().equals(name)) {
			logger.warn("OTCAccountInfoManager. create() " + ErrorInfoEnum.LANG_INVALID_BINDNAME.getErrorCNMsg()
					+ " \n record:" + record.toString() + " login objec:" + m.getId() + " authIdentity_name:" + name);
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_INVALID_BINDNAME.getErrorENMsg(), null);
		}
		if (!"1".equals(m.getM_security_pwd()) || !validateSecurityPwd(m.getId(), record.getSecurityPwd())) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorENMsg(), null);
		}
		// 加把锁
		String lockKey = "otc_account_info_exist_prefix" + record.getMemberId() + "_" + record.getType();
		boolean isLock = JedisUtil.getInstance().getLock(lockKey, IPAddressPortUtil.IP_ADDRESS, 4 * 1000);
		if (isLock) {
			try {
				Map<String, Object> condition = HelpUtils.newHashMap("memberId", record.getMemberId(), "type",
						record.getType(), "isDelete", WhetherEnum.NO);
				List<AccountInfoDTO> list = oTCAccountInfoManager.findByConditionPage(condition);
				if (!CollectionUtils.isEmpty(list)) {
					AccountInfoDTO existDto = list.get(0);
					if (record.getAccount().equals(existDto.getAccount())) {
						return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, list.get(0));
					}
					return new ObjResp(Resp.FAIL, ErrorInfoEnum.ACCOUNTTYPE_ONLY_ONE.getErrorENMsg(), null);
				}
				int id = oTCAccountInfoManager.insert(record);
				record.setId(id);
				return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, record);
			} finally {
				boolean isRelease = JedisUtil.getInstance().releaseLock(lockKey, IPAddressPortUtil.IP_ADDRESS);
			}
		}
		return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_REPEAT_ERROR.getErrorENMsg(), null);
	}

	@ApiIgnore
	@RequestMapping(value = "updateAccountInfo", method = RequestMethod.PUT)
	@ResponseBody
	public ObjResp updateAccountInfo(AccountInfoDTO record) {
		int i = oTCAccountInfoManager.updateAcountInfo(record);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, i);
	}

//	@ApiOperation(value = "删除收款账户信息", notes = "输入字段：账户ID:id 资金密码:securityPwd", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiIgnore
	@RequestMapping(value = "delete/{id}/{securityPwd}", method = RequestMethod.DELETE)
	@ResponseBody
	public ObjResp delete(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id,
			@PathVariable String securityPwd) {
		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}
		if (!"1".equals(m.getM_security_pwd()) || !validateSecurityPwd(m.getId(), securityPwd)) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorENMsg(), null);
		}
		int i = oTCAccountInfoManager.deleteById(id);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, i);
	}

	@ApiOperation(value = "根据id获取账户信息", notes = "输入字段： 账户ID:id ", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "select/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp selectAccountInfo(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) {
		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}
		AccountInfoDTO record = oTCAccountInfoManager.findById(id);
		if (m.getId().intValue() != record.getMemberId().intValue()) {
			return new ObjResp(Resp.FAIL, "LANG_ERROR_OPERATION_OBJECT", null);
		}
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, record);
	}

	@ApiOperation(value = "根据条件获取账户信息", notes = "输入字段： 类型:type(BANK|ALIPAY|WXPAY) 姓名:name 账号:account（银行卡号或微信支付宝账号）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "selectByCondition", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp selectByCondition(HttpServletRequest request) {
		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}
		Map<String, Object> condition = $params(request);
		condition.put("memberId", m.getId());
		condition.put("isDelete", WhetherEnum.NO);
		List<AccountInfoDTO> list = oTCAccountInfoManager.findByConditionPage(condition);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG,
				HelpUtils.newHashMap("rows", list, "total", condition.get("total")));
	}
}
