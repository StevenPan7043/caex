package com.pmzhongguo.otc.service;

import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.entity.RespUserSecret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pmzhongguo.ex.business.entity.ReqBaseSecret;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.otc.entity.dto.SSOTokenDTO;

@Service
@Transactional
public class SSOTokenManager {
	
	@Autowired
	private SSOTokenService sSOTokenService;
	
//	public int insert(Integer memberId) {
//		SSOTokenDTO sSOTokenDTO = sSOTokenService.create(memberId);
//		int id = insert(sSOTokenDTO);
//		return id;
//	}
	
	private int insert(SSOTokenDTO sSOTokenDTO) {
		int id = sSOTokenService.insert(sSOTokenDTO);
		return id;
	}
	
	/**
	 * 	获取token
	 * @param memberId
	 * @return
	 */
	public ReqBaseSecret insert(Integer memberId) {
		SSOTokenDTO dto = sSOTokenService.findBycondision(HelpUtils.newHashMap("memberId", memberId));
		if(dto == null) {
			dto = sSOTokenService.insert(memberId);
			insert(dto);
		}
		ReqBaseSecret token = new ReqBaseSecret();
		token.setApi_key(dto.getApiKey());
		token.setSign_type("MD5");
		String timestamp = String.valueOf(HelpUtils.getNowTimeStampInt());
		token.setTimestamp(Long.valueOf(timestamp));
		String sign = HelpUtils.createSign(HelpUtils.objToMap(token), dto.getApiSecret());
		token.setSign(sign);
		return token;
	}

	/**
	 * 获取usertoken
	 * @param member
	 * @return
	 */
	public RespUserSecret getUserSSOToken(Member member) {
		SSOTokenDTO dto = sSOTokenService.findBycondision(HelpUtils.newHashMap("memberId", member.getId()));
		if(dto == null) {
			dto = sSOTokenService.insert(member.getId());
			insert(dto);
		}
		RespUserSecret token = new RespUserSecret();
		token.setApi_key(dto.getApiKey());
		token.setSign_type("MD5");
		String timestamp = String.valueOf(HelpUtils.getNowTimeStampInt());
		token.setTimestamp(Long.valueOf(timestamp));
		token.setMemberId(member.getId());
		token.setM_name(member.getM_name());
		String sign = HelpUtils.createSign(HelpUtils.objToMap(token), dto.getApiSecret());
		token.setSign(sign);
		return token;
	}

	/**
	 * 	校验token是否正确
	 * @param token
	 * @return
	 */
	public ObjResp validateToken(ReqBaseSecret token){
		String validateRet = HelpUtils.preValidateBaseSecret(token);
		if (!"".equals(validateRet)) {
			return new ObjResp(Resp.FAIL, validateRet, null);
		}
		SSOTokenDTO apiToken = sSOTokenService.findBycondision(HelpUtils.newHashMap("apiKey", token.getApi_key()));
		if (null == apiToken) {
			return new ObjResp(Resp.FAIL, "TOKEN_NOT_EXISTS", null);
		}
		validateRet = HelpUtils.validateBaseSecret(HelpUtils.objToMap(token), apiToken.getApiSecret());
		if (!"".equals(validateRet)) {
			return new ObjResp(Resp.FAIL, validateRet, null);
		}
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, apiToken.getMemberId());
	}
}
