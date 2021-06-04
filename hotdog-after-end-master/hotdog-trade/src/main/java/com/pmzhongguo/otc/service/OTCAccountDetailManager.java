package com.pmzhongguo.otc.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.otc.entity.dto.OTCAccountDetailDTO;


@Service
@Transactional
public class OTCAccountDetailManager extends BaseServiceSupport  {

	@Autowired
	private OTCAccountDetailService oTCAccountDetailService;
	
	public int insert(OTCAccountDetailDTO record) {
		return oTCAccountDetailService.insert(record);
	}
	
	public List<OTCAccountDetailDTO> selectPage(Map<String, Object> params) {
		return oTCAccountDetailService.selectPage(params);
	}
	
	public List<Map<String, Object>> getMemberDetailsPage(Map<String, Object> params) {
		List<Map<String, Object>> list = oTCAccountDetailService.selectMemberDetailsPage(params);
		return list;
	}
}
