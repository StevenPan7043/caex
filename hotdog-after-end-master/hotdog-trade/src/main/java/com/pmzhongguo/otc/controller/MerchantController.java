package com.pmzhongguo.otc.controller;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pmzhongguo.ex.core.web.Constants;
import com.pmzhongguo.ex.framework.entity.FrmUser;
import com.qiniu.util.DateStyleEnum;
import com.qiniu.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtilMember;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.otc.entity.dto.MerchantDTO;
import com.pmzhongguo.otc.entity.req.MerchantReq;
import com.pmzhongguo.otc.otcenum.AuditStatusEnum;
import com.pmzhongguo.otc.otcenum.WhetherEnum;
import com.pmzhongguo.otc.service.MerchantManager;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = "NEW OTC商家接口", description = "商家认证，绑定手机相关", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("otc/merchant")
public class MerchantController extends TopController {

	@Autowired
	private MerchantManager merchantManager;

	@ApiOperation(value = "商家申请", notes = "输入字段：memberId(用户ID) 是否缴纳押金:isDeposit (NO|YES)", httpMethod = "POST")
	@RequestMapping(value = "create/{memberId}/{isDeposit}", method = RequestMethod.POST)
	@ResponseBody
	public ObjResp createMerchant(HttpServletRequest request, HttpServletResponse response,
			@PathVariable Integer memberId, @PathVariable WhetherEnum isDeposit) {
		// 判断商家申请的ID与当前登录用户是否一致
		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
		}
//		if(!HelpUtils.isMobile(m.getM_name())) {
//			return new ObjResp(Resp.FAIL, ErrorInfoEnum.ONLY_MOBILE_CUSTOMER.getErrorENMsg(), null);
//		}
		if (memberId.intValue() != m.getId().intValue()) {
			logger.warn("login_member_id:" + m.getId() + " apply_memberid:" + memberId);
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_OPERATION_OBJECT.getErrorENMsg(), null);
		}
		MerchantDTO resp = merchantManager.getMerchant(memberId);
		if (resp != null) {
			if (resp.getStatus().getType() == AuditStatusEnum.APPLY_AUDITING.getType()
					|| resp.getStatus().getType() == AuditStatusEnum.APPLY_PASSED.getType()
					|| resp.getStatus().getType() == AuditStatusEnum.SECEDE_AUDITING.getType()) {
				return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_REPEAT_ERROR.getErrorENMsg(), null);
			}
			if (resp.getStatus().getType() == AuditStatusEnum.APPLY_REJECT.getType()) {
				merchantManager.deleteById(HelpUtils.newHashMap("id", resp.getId(),"delRemark",StringUtils.isBlank(resp.getMemo())?"管理员驳回后商家重新申请时删除驳回记录":resp.getMemo()+"▲管理员驳回后商家重新申请时删除驳回记录"));
			}
		}

		String otcDepositCurrency = HelpUtils.getMgrConfig().getOtc_deposit_currency();
		BigDecimal otcDepositVolume = BigDecimal.ZERO;
		if(isDeposit.getType() == WhetherEnum.YES.getType()) {
			otcDepositVolume = HelpUtils.getMgrConfig().getOtc_deposit_volume();
		}
		if (HelpUtils.nullOrBlank(otcDepositCurrency) || otcDepositVolume.compareTo(BigDecimal.ZERO) < 0) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.RELATION_CUSTOMER_SERVICE.getErrorENMsg(), null);
		}
		merchantManager.auditMerchant(memberId, otcDepositCurrency, otcDepositVolume, AuditStatusEnum.APPLY_AUDITING,
				null, isDeposit);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
	}
	
	@ApiOperation(value = "商家申请退出", notes = "输入字段：资金密码:securityPwd 申诉理由:memo", httpMethod = "POST")
	@RequestMapping(value = "secede/{securityPwd}/{memo}", method = RequestMethod.POST)
	@ResponseBody
	public ObjResp secede(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String securityPwd, @PathVariable String memo) {
		// 判断商家申请的ID与当前登录用户是否一致
		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
		}
		if(!"1".equals(m.getM_security_pwd()) || !validateSecurityPwd(m.getId(), securityPwd)) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorENMsg() , null);
		}
		MerchantDTO resp = merchantManager.getMerchant(m.getId());
		if (resp != null) {
			if (!(resp.getStatus().getType() == AuditStatusEnum.APPLY_PASSED.getType())) {
				return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_STATUS.getErrorENMsg(), null);
			}
		}else {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_STATUS.getErrorENMsg(), null);
		}
		MerchantDTO record = new MerchantDTO();
		record.setMemo(memo);
		record.setStatus(AuditStatusEnum.SECEDE_AUDITING);
		record.setMemberId(m.getId());
		merchantManager.update(record);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
	}

//	@ApiOperation(value = "商家审核", notes = "输入字段：id（商家ID） 审核状态:status 驳回原因:Memo", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiIgnore
	@RequestMapping(value = "audit", method = RequestMethod.POST)
	@ResponseBody
	public ObjResp auditMerchant(HttpServletRequest request, HttpServletResponse response) {
//			@RequestBody MerchantReq req) {
		
		ObjResp resp = new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_STATUS.getErrorENMsg(), null);
		MerchantReq req = new MerchantReq();
		req.setId($int("id"));
		req.setStatus(AuditStatusEnum.getAuditStatusEnum($("status")));
		req.setMemo($("memo"));
		if (req.getStatus().getType() != AuditStatusEnum.APPLY_PASSED.getType()
				&& req.getStatus().getType() != AuditStatusEnum.APPLY_REJECT.getType()
				&& req.getStatus().getType() != AuditStatusEnum.SECEDE_REJECT.getType()
				&& req.getStatus().getType() != AuditStatusEnum.SECEDE_PASSED.getType()) {
			logger.warn("merchantId:" + req.getId() + " 操作状态不对！提交审核状态为：" + req.getStatus() + " ip:" + HelpUtils.getIpAddr(request));
			return resp;
		}

		MerchantDTO dto = merchantManager.findById(req.getId());
		if (dto == null) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_RECORD.getErrorENMsg(), null);
		}

		if ((req.getStatus().getType() == AuditStatusEnum.APPLY_PASSED.getType()
				|| req.getStatus().getType() == AuditStatusEnum.APPLY_REJECT.getType())
				&& dto.getStatus().getType() != AuditStatusEnum.APPLY_AUDITING.getType()) {
			logger.warn("merchantId:" + req.getId() + "操作状态不对！提交审核状态为：" + req.getStatus() + " 商家状态为"+ dto.getStatus() + " ip:" + HelpUtils.getIpAddr(request));
			return resp;
		}

		if ((req.getStatus().getType() == AuditStatusEnum.SECEDE_REJECT.getType()
				|| req.getStatus().getType() == AuditStatusEnum.SECEDE_PASSED.getType())
				&& dto.getStatus().getType() != AuditStatusEnum.SECEDE_AUDITING.getType()) {
			logger.warn("merchantId:" + req.getId() + "操作状态不对！提交审核状态为：" + req.getStatus() + " 商家状态为"+ dto.getStatus() + " ip:" + HelpUtils.getIpAddr(request));
			return resp;
		}
		
		if (req.getStatus().getType() == AuditStatusEnum.APPLY_PASSED.getType()
				|| req.getStatus().getType() == AuditStatusEnum.APPLY_REJECT.getType()
				|| req.getStatus().getType() == AuditStatusEnum.SECEDE_PASSED.getType()) {
			resp = merchantManager.auditMerchant(dto.getMemberId(), dto.getDepositCurrency(), dto.getDepositVolume(),
					req.getStatus(), dto.getMemo(), null);
		}else if(req.getStatus().getType() == AuditStatusEnum.SECEDE_REJECT.getType()){
			MerchantDTO record = new MerchantDTO();
			record.setId(dto.getId());
			record.setMemo(req.getMemo());
			record.setStatus(AuditStatusEnum.APPLY_PASSED);
			int count = merchantManager.update(record);
			if(count == 1) {
				resp = new ObjResp();
			}else {
				resp.setMsg(ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorENMsg());
			}
		}
		
		return resp;
	}


	@ApiOperation(value = "商家信息", notes = "默认返回当前登录用户信息，带memberId参数就返回指定会员商家信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "getInfo", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp merchant(HttpServletRequest request, HttpServletResponse response) {
		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
		}
		String strId = $("memberId");
		Integer memberId = m.getId();
		if(!HelpUtils.nullOrBlank(strId)) {
			memberId = Integer.valueOf(strId);
		}
		MerchantDTO dto = merchantManager.getMerchant(memberId);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, dto);
	}

}
