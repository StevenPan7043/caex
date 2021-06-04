package com.pmzhongguo.otc.service;

import com.pmzhongguo.otc.entity.dto.MerchantLogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MerchantLogManager {

	@Autowired
	private MerchantLogService merchantLogService;
	/**
	 * 获取退商列表
	 *
	 * @param param
	 * @return
	 */
	public List<MerchantLogDTO> getCancelBusPage(Map<String, Object> param) {
		return merchantLogService.getCancelBusPage(param);
	}
}
