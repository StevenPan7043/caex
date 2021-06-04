package com.pmzhongguo.otc.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.validate.transfer.inter.AccountValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtilMember;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.otc.entity.dto.AccountDTO;
import com.pmzhongguo.otc.otcenum.AccountOperateTypeEnum;
import com.pmzhongguo.otc.service.OTCAccountDetailManager;
import com.pmzhongguo.otc.service.OTCAccountManager;
import com.pmzhongguo.otc.service.OTCAccountService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = "NEW OTC会员资产操作", description = "资产账户操作相关", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("otc/Account")
public class OTCAccountController extends TopController {

	@Autowired
	private OTCAccountManager oTCAccountManager;
	
	@Autowired
	private OTCAccountDetailManager oTCAccountDetailManager;
	
	@Autowired
	private OTCAccountService OTCAccountService;

	@Autowired
	private AccountValidate accountValidate;
	
//	@ApiOperation(value = "会员资产从币币区转到法币区", notes = "输入字段： 会员ID:memberId  币种:currency  划转数量:num", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiIgnore
	@RequestMapping(value = "addChange/{memberId}/{num}/{currency}", method = RequestMethod.POST)
	@ResponseBody
	public ObjResp addChange( HttpServletRequest request,
			HttpServletResponse response, @PathVariable Integer memberId, @PathVariable String currency, @PathVariable BigDecimal num) {
		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}
		if(memberId.intValue() != m.getId().intValue()) {
			return new ObjResp(Resp.FAIL, "LANG_ERROR_OPERATION_OBJECT", null);
		}
		
		currency = currency.toUpperCase();
		Map<String, Currency> currencyMap = HelpUtils.getCurrencyIsOtcMap();
		if(!currencyMap.containsKey(currency) && !"ZZEX".equals(currency)) {
			return new ObjResp(Resp.FAIL, "ERROR_OTC_CURRENCY", null);
		}
		/**
		 *  检查剩余资产是否大于等于5 USDT
		 *  added by Daily on 2002/7/2 13:41
		 */
		if(!accountValidate.isTransfer(m.getId(), currency, num)){
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.INSUFFICIENT_RESIDUAL_ASSETS.getErrorENMsg(), null);
		}
		oTCAccountManager.accountTransfer(memberId, currency, num, AccountOperateTypeEnum.INTO);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
	}
	
//	@ApiOperation(value = "会员资产从法币区转到币币区", notes = "输入字段： 会员ID:memberId  币种:currency  划转数量:num", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiIgnore
	@RequestMapping(value = "subtChange/{memberId}/{num}/{currency}", method = RequestMethod.POST)
	@ResponseBody
	public ObjResp subtChange( HttpServletRequest request,
			HttpServletResponse response, @PathVariable Integer memberId, @PathVariable String currency, @PathVariable BigDecimal num) {
		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}
		if(memberId.intValue() != m.getId().intValue()) {
			return new ObjResp(Resp.FAIL, "LANG_ERROR_OPERATION_OBJECT", null);
		}
		oTCAccountManager.accountTransfer(memberId, currency, num.negate(), AccountOperateTypeEnum.OUT);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
	}
	
	@ApiOperation(value = "申请商家押金信息", notes = "", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "getDepositInfo", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp getDepositInfo( HttpServletRequest request,
			HttpServletResponse response) {
		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}
		String otcDepositCurrency = HelpUtils.getMgrConfig().getOtc_deposit_currency();
		BigDecimal otcDepositVolume = HelpUtils.getMgrConfig().getOtc_deposit_volume();
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, HelpUtils.newHashMap("otcDepositCurrency", otcDepositCurrency, "otcDepositVolume", otcDepositVolume));
	}
	
	@ApiOperation(value = "查询会员OTC账户操作明细", notes = "输入字段： 会员ID:memberId  币种:currency  操作:procType(ADDTOTAL|REDUCEFROZEN|INTO|OUT)", httpMethod = "GET")
	@RequestMapping(value = "getMemberDetailsPage", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp getMemberDetailsPage( HttpServletRequest request,
			HttpServletResponse response) {
		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}
		Map<String, Object> params = $params(request);
		if(HelpUtils.isMapNullValue(params, "memberId")) {
			params.put("memberId", m.getId());
		}
		if(HelpUtils.isMapNullValue(params, "sortname")) {
			params.put("sortname", "create_time");
			params.put("sortorder", "desc");
		}
		if(HelpUtils.isMapNullValue(params, "procType")) {
			params.put("procTypeAll", 1);
		}
		List<Map<String, Object>> list = oTCAccountDetailManager.getMemberDetailsPage(params);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, HelpUtils.newHashMap("rows", list, "total", params.get("total")));
	}
	
//	@ApiOperation(value = "创建会员资产账户", notes = "输入字段： 账户ID:id ", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiIgnore
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public ObjResp createAccount( HttpServletRequest request,
			HttpServletResponse response, @RequestBody AccountDTO record) {
		if(record.getFrozenBalance() == null) {
			record.setFrozenBalance(BigDecimal.ZERO);
		}
		OTCAccountService.insert(record);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, record);
	}
	
	@ApiOperation(value = "根据会员id和币种查询会员资产账户", notes = "输入字段： 会员ID:memberId  币种:currency", httpMethod = "GET")
	@RequestMapping(value = "select/{memberId}/{currency}", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp selectAccount( HttpServletRequest request,
			HttpServletResponse response, @PathVariable Integer memberId, @PathVariable String currency) {
		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}
		if(memberId.intValue() != m.getId().intValue()) {
			return new ObjResp(Resp.FAIL, "LANG_ERROR_OPERATION_OBJECT", null);
		}
		AccountDTO record = OTCAccountService.findBymerchantIdAndCurrency(HelpUtils.newHashMap("memberId", memberId, "currency", currency));
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, record);
	}
	
	@ApiOperation(value = "查询会员所有资产账户", notes = "输入字段： 会员ID:memberId  币种:currency", httpMethod = "GET")
	@RequestMapping(value = "select/{memberId}", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp selectByCondition(HttpServletRequest request, @PathVariable Integer memberId) {
		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}
		if(memberId.intValue() != m.getId().intValue()) {
			return new ObjResp(Resp.FAIL, "LANG_ERROR_OPERATION_OBJECT", null);
		}
		Map<String, Object> condition =  HelpUtils.newHashMap("memberId", memberId);
		List<AccountDTO> record = OTCAccountService.findByConditionPage(condition);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, HelpUtils.newHashMap("rows", record, "total", condition.get("total")));
	}
}
