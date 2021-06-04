package com.pmzhongguo.ex.business.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pmzhongguo.ex.business.dto.AccountDetailDto;
import com.pmzhongguo.ex.business.mapper.AccountDetailMapper;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;


@Service
@Transactional
public class AccountDetailService extends BaseServiceSupport  {

	@Autowired
	private AccountDetailMapper accountDetailMapper;
	
	public void insert(AccountDetailDto record) {
		accountDetailMapper.insert(record);
	}
		
	public List<AccountDetailDto> selectPage(Map<String, Object> params) {
		return accountDetailMapper.selectPage(params);
	}
	public List<AccountDetailDto> selectOTCPage(Map<String, Object> params) {
		return accountDetailMapper.selectOTCPage(params);
	}
}
