package com.pmzhongguo.ex.business.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pmzhongguo.ex.business.entity.SetTradeReturnFee;
import com.pmzhongguo.ex.business.mapper.SetTradeReturnFeeMapper;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;

/**
 * 交易返手续费模块
 * 
 * @author zengweixiong
 *
 */
@Service
public class SetTradeReturnFeeService extends BaseServiceSupport {

	@Resource
	private SetTradeReturnFeeMapper setTradeReturnFeeMapper;

	/**
	 * 查询设置信息
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getSet() {
		return setTradeReturnFeeMapper.getSet();
	}

	/**
	 * 编辑投票设置信息
	 * 
	 * @param list
	 */
	public void editSet(List<SetTradeReturnFee> list) {
		setTradeReturnFeeMapper.editSet(list);
	}

	/**
	 * 根据用户ID获取用户3代父级数据
	 * 
	 * @param memberId
	 * @return
	 */
	public List<Map<String, Object>> getFastMembers(Integer memberId) {
		return setTradeReturnFeeMapper.getAll(memberId);
	}

}
