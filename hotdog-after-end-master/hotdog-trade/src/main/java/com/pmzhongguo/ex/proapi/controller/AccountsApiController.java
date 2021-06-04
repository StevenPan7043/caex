package com.pmzhongguo.ex.proapi.controller;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pmzhongguo.ex.business.entity.Account;
import com.pmzhongguo.ex.business.entity.ApiToken;
import com.pmzhongguo.ex.business.entity.ReqBaseSecret;
import com.pmzhongguo.ex.business.entity.RespAccount;
import com.pmzhongguo.ex.business.entity.RespAccountSingle;
import com.pmzhongguo.ex.business.entity.RespAccounts;
import com.pmzhongguo.ex.business.entity.RespObj;
import com.pmzhongguo.ex.business.service.ApiAccessLimitService;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = "程序交易API|账户查询接口", description = "程序交易API|账户查询接口")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("a/api")
public class AccountsApiController extends TopController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private ApiAccessLimitService apiAccessLimitService;


	@ApiOperation(value = "获得会员资产列表", notes = "获得资产列表。<br>api_key、timestamp、sign_type和sign详见签名方法", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "accounts/{api_key}/{timestamp}/{sign_type}/{sign}", method = RequestMethod.GET)
	@ResponseBody
	public Resp getAccounts(HttpServletRequest request, @PathVariable("api_key") String apikey,
			@PathVariable("timestamp") Long timestamp, @PathVariable("sign_type") String signType, @PathVariable("sign") String sign) {
		return this.getAccountsComm(request, apikey, timestamp, signType, sign);
	}
	
	
	private Resp getAccountsComm(HttpServletRequest request, String apikey, Long timestamp, String signType, String sign) {
		apiAccessLimitService.check("getAccounts", apikey);
		
		ReqBaseSecret reqBaseSecret = new ReqBaseSecret();
		reqBaseSecret.setApi_key(apikey);
		reqBaseSecret.setSign_type(signType);
		reqBaseSecret.setTimestamp(timestamp);
		reqBaseSecret.setSign(sign);
		String validateRet = HelpUtils.preValidateBaseSecret(reqBaseSecret);
		if (!"".equals(validateRet)) {
			if("ILLEGAL_TIMESTAMP".equals(validateRet)) {
				return new RespObj(Resp.FAIL, validateRet, HelpUtils.getNowTimeStampInt());
			}
			return new RespAccounts(Resp.FAIL, validateRet, null, null);
		}
		
		String ip = HelpUtils.getIpAddr(request);		
		
		// 校验合法性，非法情况直接抛出异常
		ApiToken apiToken = memberService.getApiToken(apikey);
		if (null == apiToken) {
//			logger.warn("apikey:" + apikey + " redis ApiToken is null!");
			return new Resp(Resp.FAIL, "error apikey!");
		}
		validateApiToken(HelpUtils.objToMap(reqBaseSecret), apiToken, ip, "Accounts");

		return new RespAccounts(Resp.SUCCESS, Resp.SUCCESS_MSG, memberService.getAccounts(apiToken.getMember_id()), null);
	}
	


	@ApiOperation(value = "获得单个币资产", notes = "获得单个币资产<br>currency:币种，如BTC<br>api_key、timestamp、sign_type和sign详见签名方法", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "account/{currency}/{api_key}/{timestamp}/{sign_type}/{sign}", method = RequestMethod.GET)
	@ResponseBody
	public Resp getAccount(HttpServletRequest request, @PathVariable("currency") String currency, @PathVariable("api_key") String apikey,
			@PathVariable("timestamp") Long timestamp, @PathVariable("sign_type") String signType, @PathVariable("sign") String sign) {

		apiAccessLimitService.check("getAccount", apikey);
		
		ReqBaseSecret reqBaseSecret = new ReqBaseSecret();
		reqBaseSecret.setApi_key(apikey);
		reqBaseSecret.setSign_type(signType);
		reqBaseSecret.setTimestamp(timestamp);
		reqBaseSecret.setSign(sign);
		String validateRet = HelpUtils.preValidateBaseSecret(reqBaseSecret);
		if (!"".equals(validateRet)) {
			if("ILLEGAL_TIMESTAMP".equals(validateRet)) {
				return new RespObj(Resp.FAIL,validateRet,HelpUtils.getNowTimeStampInt());
			}
			return new RespAccountSingle(Resp.FAIL, validateRet, null);
		}
		
		String ip = HelpUtils.getIpAddr(request);		
		
		// 校验合法性，非法情况直接抛出异常
		ApiToken apiToken = memberService.getApiToken(apikey);
		validateApiToken(HelpUtils.objToMap(reqBaseSecret), apiToken, ip, "Accounts");
		
		Account account = memberService.getAccount(HelpUtils.newHashMap("member_id", apiToken.getMember_id(),"currency",currency));
		
		RespAccount rAccount = new RespAccount();
		if (null == account || account.getId() <= 0) {
			rAccount.setAvailable_balance(BigDecimal.ZERO);
			rAccount.setCurrency(currency);
			rAccount.setFrozen_balance(BigDecimal.ZERO);
			rAccount.setTotal_balance(BigDecimal.ZERO);
		} else {
			rAccount.setAvailable_balance(account.getTotal_balance().subtract(account.getFrozen_balance()).setScale(18));
			rAccount.setCurrency(account.getCurrency());
			rAccount.setFrozen_balance(account.getFrozen_balance());
			rAccount.setTotal_balance(account.getTotal_balance());
		}

		return new RespAccountSingle(Resp.SUCCESS, Resp.SUCCESS_MSG, rAccount);
	}

}
