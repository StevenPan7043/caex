package com.pmzhongguo.ex.transfer.service;

import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.transfer.entity.RespUserSecret;
import com.pmzhongguo.ex.transfer.entity.ThirdPartyInfo;
import com.pmzhongguo.otc.entity.dto.SSOTokenDTO;
import com.pmzhongguo.otc.service.SSOTokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SSOUsersTokenManager {
	
	@Autowired
	private SSOTokenService sSOTokenService;

	@Autowired
	private ThirdPartyService thirdPartyService;
	
	private int insert(SSOTokenDTO sSOTokenDTO) {
		int id = sSOTokenService.insert(sSOTokenDTO);
		return id;
	}

	/**
	 * 获取usertoken
	 * @param member
	 * @return
	 */
	public RespUserSecret getUserSSOToken(Member member, String c_name) {
		List<ThirdPartyInfo> list = thirdPartyService.getAllThirdPartty(HelpUtils.newHashMap("c_name", c_name));
		if (list == null) {
			throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_THIRDPARTTY_NOT_EXIST.getErrorENMsg());
		}
		ThirdPartyInfo thirdPartyInfo = list.get(0);
		RespUserSecret token = new RespUserSecret();
		token.setApi_key(thirdPartyInfo.getS_apiKey());
		token.setSign_type("MD5");
		String timestamp = String.valueOf(HelpUtils.getNowTimeStampInt());
		token.setTimestamp(Long.valueOf(timestamp));
		token.setMemberId(member.getId());
		token.setM_name(member.getM_name());
		token.setPhone(StringUtils.isBlank(member.getPhone()) ? "" : member.getPhone());
		String sign = HelpUtils.createSign(HelpUtils.objToMap(token), thirdPartyInfo.getS_secretKey());
		token.setSign(sign);
		return token;
	}
}
