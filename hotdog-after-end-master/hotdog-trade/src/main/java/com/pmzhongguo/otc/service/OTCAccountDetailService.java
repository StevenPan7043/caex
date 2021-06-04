package com.pmzhongguo.otc.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import com.pmzhongguo.otc.dao.OTCAccountDetailMapper;
import com.pmzhongguo.otc.entity.convertor.OTCAccountDetailConvertor;
import com.pmzhongguo.otc.entity.dataobject.OTCAccountDetailDO;
import com.pmzhongguo.otc.entity.dto.OTCAccountDetailDTO;


@Service
@Transactional
public class OTCAccountDetailService extends BaseServiceSupport  {

	@Autowired
	private OTCAccountDetailMapper oTCAccountDetailMapper;
	
	public int insert(OTCAccountDetailDTO record) {
		OTCAccountDetailDO oTCAccountDetailDO = OTCAccountDetailConvertor.DTO2DO(record);
		oTCAccountDetailMapper.insert(oTCAccountDetailDO);
		return oTCAccountDetailDO.getId() == null ? 0 : oTCAccountDetailDO.getId();
	}
		
	/**
	 * 	查询会员OTC账户操作明细
	 * where memberId currency
	 * @param params
	 * @return
	 */
	public List<OTCAccountDetailDTO> selectPage(Map<String, Object> params) {
		OTCAccountDetailConvertor.initMap(params);
		List<OTCAccountDetailDO> list = oTCAccountDetailMapper.selectPage(params);
		return list == null ? null : OTCAccountDetailConvertor.DO2DTO(list);
	}
	
	public List<Map<String, Object>> selectMemberDetailsPage(Map<String, Object> params) {
		OTCAccountDetailConvertor.initMap(params);
		List<Map<String, Object>> list = oTCAccountDetailMapper.selectMemberDetailsPage(params);
		return list;
	}
	
}
