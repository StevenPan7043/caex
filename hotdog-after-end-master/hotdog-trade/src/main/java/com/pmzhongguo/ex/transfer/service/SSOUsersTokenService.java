package com.pmzhongguo.ex.transfer.service;

import com.pmzhongguo.ex.core.utils.KeySecretUtil;
import com.pmzhongguo.ex.transfer.mapper.SSOUsersTokenMapper;
import com.pmzhongguo.otc.entity.convertor.SSOTokenConvertor;
import com.pmzhongguo.otc.entity.dataobject.SSOTokenDO;
import com.pmzhongguo.otc.entity.dto.SSOTokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class SSOUsersTokenService {

	@Autowired
	private SSOUsersTokenMapper sSOTokenMapper;
	
	
	/**
	 * 	
	 * @param record
	 * @return
	 */
	public int insert(SSOTokenDTO record) {
		SSOTokenDO ssoTokenDO = SSOTokenConvertor.DTO2DO(record);
		sSOTokenMapper.insert(ssoTokenDO);
		return ssoTokenDO.getId() == null ? 0 : ssoTokenDO.getId();
	}
	
	public SSOTokenDTO insert(Integer memberId) {
		SSOTokenDTO sSOTokenDTO = new SSOTokenDTO();
		Map<String, String> keyPair = KeySecretUtil.genKeyScrectPair();
		sSOTokenDTO.setMemberId(memberId);
		sSOTokenDTO.setApiKey(keyPair.get("api_key"));
		sSOTokenDTO.setApiSecret(keyPair.get("api_secret"));
		return sSOTokenDTO;
	}
	
	/**
	 * 	
	 * @param id
	 * @return
	 */
	public int delete(Integer id) {
		int count = sSOTokenMapper.deleteById(id);
		return count;
	}
	
	/**
	 * 	where id or MemberId
	 *  update apiKey apiSecret
	 * @param record
	 * @return
	 */
	public int updateByIdOrMemberId(SSOTokenDTO record) {
		SSOTokenDO ssoTokenDO = SSOTokenConvertor.DTO2DO(record);
		int count = sSOTokenMapper.updateById(ssoTokenDO);
		return count;
	}
	
	/**
	 * 	where id or MemberId or apiKey
	 * @param params
	 * @return
	 */
	public SSOTokenDTO findBycondision(Map<String, Object> params) {
		SSOTokenDO ssoTokenDO = sSOTokenMapper.findBycondision(params);
		return ssoTokenDO == null ? null : SSOTokenConvertor.DO2DTO(ssoTokenDO);
	};

}
