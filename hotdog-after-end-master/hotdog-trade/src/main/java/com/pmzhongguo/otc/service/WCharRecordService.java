package com.pmzhongguo.otc.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.otc.dao.WCharRecordMapper;
import com.pmzhongguo.otc.entity.convertor.WCharRecordConvertor;
import com.pmzhongguo.otc.entity.dataobject.WCharRecordDO;
import com.pmzhongguo.otc.entity.dto.WCharRecordDTO;
import com.pmzhongguo.otc.otcenum.TakerEnum;
import com.pmzhongguo.otc.otcenum.WhetherEnum;

@Service
@Transactional
public class WCharRecordService {

	@Autowired
	private WCharRecordMapper wCharRecordMapper;
	
	public int insert(WCharRecordDTO dto) {
		WCharRecordDO wCharRecordDO = WCharRecordConvertor.DTO2DO(dto);
		wCharRecordMapper.insert(wCharRecordDO);
		return wCharRecordDO.getId() == null ? 0 : wCharRecordDO.getId();
	}

	public List<WCharRecordDTO> findByConditionPage(Map<String, Object> param){
		WCharRecordConvertor.mapEnumName2type(param);
		List<WCharRecordDO> list = wCharRecordMapper.findByConditionPage(param);
		return list == null ? null : WCharRecordConvertor.DO2DTO(list);
	}

	public int updateIsRead(WCharRecordDTO dto) {
		WCharRecordDO wCharRecordDO = WCharRecordConvertor.DTO2DO(dto);
		return wCharRecordMapper.updateByPrimaryKeySelective(wCharRecordDO);
	}
}
