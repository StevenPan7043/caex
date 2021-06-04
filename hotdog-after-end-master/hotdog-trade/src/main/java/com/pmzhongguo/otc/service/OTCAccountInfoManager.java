package com.pmzhongguo.otc.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pmzhongguo.otc.entity.convertor.AccountInfoConvertor;
import com.pmzhongguo.otc.entity.dataobject.AccountInfoDO;
import com.pmzhongguo.otc.entity.dto.AccountInfoDTO;

@Service
@Transactional
public class OTCAccountInfoManager {
	
	@Autowired
	private OTCAccountInfoService oTCAccountInfoService;
	
	/**
	 * 为商家添加一条收款账户信息
	 * 
	 * @param record
	 * @return
	 */
	public int insert(AccountInfoDTO record) {
		return oTCAccountInfoService.insert(record);
	}
	
	/**
	 *	 修改账户信息
	 * 	只能修改 type name account bankOrImg 这四个字段
	 * @param record
	 * @return
	 */
	public int updateAcountInfo(AccountInfoDTO record) {
		return oTCAccountInfoService.updateAcountInfo(record);
	}
	
	/**
	 * 	根据id删除一条收款账户信息
	 * 	逻辑删除
	 * @param id
	 * @return
	 */
	public int deleteById(Integer id) {
		return oTCAccountInfoService.deleteById(id);
	}
	
	/**
	 * 根据id查找一条收款账户信息 isDelete = 0
	 * 
	 * @param id
	 * @return
	 */
	public AccountInfoDTO findById(Integer id) {
		return oTCAccountInfoService.findById(id);
	}
	
	/**
	 * 根据id查找一条收款账户信息 
	 * 
	 * @param id
	 * @return
	 */
	public AccountInfoDTO findByIdAll(Integer id) {
		return oTCAccountInfoService.findByIdAll(id);
	}
	
	public List<AccountInfoDTO> findByConditionPage(Map<String, Object> param) {
		List<AccountInfoDTO> list = oTCAccountInfoService.findByConditionPage(param);
		return list;
	} 
	
}
