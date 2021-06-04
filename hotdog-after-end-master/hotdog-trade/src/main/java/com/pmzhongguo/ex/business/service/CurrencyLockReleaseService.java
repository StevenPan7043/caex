package com.pmzhongguo.ex.business.service;


import com.pmzhongguo.ex.business.dto.CurrencyLockReleaseDto;
import com.pmzhongguo.ex.business.entity.*;
import com.pmzhongguo.ex.business.mapper.LockReleaseMapper;
import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.ex.core.service.BaseService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@Transactional
public class CurrencyLockReleaseService extends BaseService<LockRelease> {

	@Autowired
	private LockReleaseMapper lockReleaseMapper;



	private static Logger logger = LoggerFactory.getLogger(CurrencyLockReleaseService.class);

	@Override
	public SuperMapper<LockRelease> getMapper() {
		return lockReleaseMapper;
	}

	/**
	 * 根据是否释放和释放时间查找所有需要释放的记录
	 * @param params
	 * @return
	 */
	public List<CurrencyLockReleaseDto> findByIsReleaseAndReleaseTimeGtNow(Map<String,Object> params) {
		return lockReleaseMapper.findByIsReleaseAndReleaseTimeGtNow(params);
	}


	/**
	 * 找到待释放的币数量
	 * @param memberId
	 * @return
	 */
    public List<CurrencyLockReleaseDto> findWaitReleaseByMemberId(Integer memberId) {
		return lockReleaseMapper.findWaitReleaseByMemberId(memberId);
    }

	public List<CurrencyLockReleaseDto> findWaitReleaseByMemberIdAndCurrency(Map<String,Object> params) {
		return lockReleaseMapper.findWaitReleaseByMemberIdAndCurrency(params);
	}

	/**
	 * 通过id和状态来更新
	 * @param lockRelease
	 */
    public int updateByIdAndIsRelease(LockRelease lockRelease) {
		return lockReleaseMapper.updateByIdAndIsRelease(lockRelease);
    }
}
