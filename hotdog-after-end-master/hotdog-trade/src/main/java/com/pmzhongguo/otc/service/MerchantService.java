package com.pmzhongguo.otc.service;

import java.util.List;
import java.util.Map;

import com.pmzhongguo.otc.dao.MerchantLogMapper;
import com.pmzhongguo.otc.entity.dataobject.MerchantLogDO;
import com.qiniu.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.otc.dao.MerchantMapper;
import com.pmzhongguo.otc.entity.convertor.MerchantConvertor;
import com.pmzhongguo.otc.entity.dataobject.MerchantDO;
import com.pmzhongguo.otc.entity.dto.MerchantDTO;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class MerchantService {

	@Autowired
	private MerchantMapper merchantMapper;
	
	/**
	 * 	添加一个商家
	 * @param record
	 * @return
	 */
	public int insert(MerchantDTO record) {
		MerchantDO merchantDO = MerchantConvertor.DTO2DO(record);
		merchantMapper.insert(merchantDO);
		return merchantDO.getId() == null ? 0 : merchantDO.getId() ;
	}
	
	/**
	 * 	根据id查找商家
	 * @param id
	 * @return
	 */
	public MerchantDTO findById(int id) {
		MerchantDO merchantDO = merchantMapper.findById(id);
		return merchantDO == null ? null : MerchantConvertor.DO2DTO(merchantDO);
	}
	
	public List<MerchantDTO> findByConditionPage(Map<String, Object> param){
		MerchantConvertor.initMap(param);
		List<MerchantDO> list = merchantMapper.findByConditionPage(param);
		return list == null ? null : MerchantConvertor.DO2DTO(list);
	}
	@Autowired
	private MerchantLogMapper merchantLogMapper;
	/**
	 * 	根据id  或 会员ID 删除商家
	 * @param params
	 * @return
	 */
	public int deleteById(Map<String, Object> params) {
		int i = 0;
		if (HelpUtils.isMapNullValue(params, "id") && HelpUtils.isMapNullValue(params, "memberId")) {
			return i;
		}
		MerchantLogDO merchantLogDO = new MerchantLogDO();
		List<MerchantDO> byConditionPage = merchantMapper.findByConditionPage(params);
		if (!CollectionUtils.isEmpty(byConditionPage)) {
			MerchantDO merchantDO = byConditionPage.get(0);
			BeanUtil.copy(merchantDO, merchantLogDO);
			merchantLogDO.setId(null);
			merchantLogDO.setMemo(BeanUtil.isEmpty(params.get("delRemark")) ? "" : String.valueOf(params.get("delRemark")));
		}
		i = merchantMapper.deleteById(params);
		merchantLogMapper.insert(merchantLogDO);
		return i;
	}
	
	/**
	 * 	根据 id 或 会员ID 修改商家信息
	 * 	只会更新 status 和 memo 两个字段
	 * @param record
	 * @return
	 */
	public int updateStatus(MerchantDTO record) {
		int i = 0;
		if(record.getId() == null && record.getMemberId() == null) {
			return i;
		}
		MerchantDO merchantDO = MerchantConvertor.DTO2DO(record);
		i = merchantMapper.updateStatus(merchantDO);
		return i;
	}
}
