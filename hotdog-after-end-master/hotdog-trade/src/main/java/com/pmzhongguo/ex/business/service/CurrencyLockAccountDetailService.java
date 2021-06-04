package com.pmzhongguo.ex.business.service;


import com.pmzhongguo.ex.business.entity.LockAccountDetail;
import com.pmzhongguo.ex.business.mapper.LockAccountDetailMapper;
import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.ex.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CurrencyLockAccountDetailService extends BaseService<LockAccountDetail> {
	@Autowired
	private LockAccountDetailMapper lockAccountDetailMapper;

	@Override
	public SuperMapper<LockAccountDetail> getMapper() {
		return lockAccountDetailMapper;
	}
}
