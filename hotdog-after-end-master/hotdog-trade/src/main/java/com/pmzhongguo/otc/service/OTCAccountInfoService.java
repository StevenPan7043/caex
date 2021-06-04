package com.pmzhongguo.otc.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.otc.dao.AccountInfoMapper;
import com.pmzhongguo.otc.entity.convertor.AccountInfoConvertor;
import com.pmzhongguo.otc.entity.dataobject.AccountInfoDO;
import com.pmzhongguo.otc.entity.dto.AccountInfoDTO;
import com.pmzhongguo.otc.otcenum.OrderStatusEnum;
import com.pmzhongguo.otc.otcenum.OrderTypeEnum;
import com.pmzhongguo.otc.otcenum.PayTypeEnum;
import com.pmzhongguo.otc.otcenum.PriceTypeEnum;
import com.pmzhongguo.otc.otcenum.WhetherEnum;

@Service
@Transactional
public class OTCAccountInfoService {

	@Autowired
	private AccountInfoMapper accountInfoMapper;

	/**
	 * 为商家添加一条收款账户信息
	 * 
	 * @param record
	 * @return
	 */
	public int insert(AccountInfoDTO record) {
		AccountInfoDO accountInfoDO = AccountInfoConvertor.DTO2DO(record);
		accountInfoMapper.insert(accountInfoDO);
		return accountInfoDO.getId() == null ? 0 : accountInfoDO.getId();
	}

	/**
	 * 根据id查找一条收款账户信息
	 * 
	 * @param id
	 * @return
	 */
	public AccountInfoDTO findById(Integer id) {
		AccountInfoDO accountInfoDO = accountInfoMapper.findById(id);
		return accountInfoDO == null ? null : AccountInfoConvertor.DO2DTO(accountInfoDO);
	}
	
	/**
	 * 根据id查找一条收款账户信息
	 * 
	 * @param id
	 * @return
	 */
	public AccountInfoDTO findByIdAll(Integer id) {
		AccountInfoDO accountInfoDO = accountInfoMapper.findByIdAll(id);
		return accountInfoDO == null ? null : AccountInfoConvertor.DO2DTO(accountInfoDO);
	}

	public List<AccountInfoDTO> findByConditionPage(Map<String, Object> param) {
		AccountInfoConvertor.mapEnumName2type(param);
		List<AccountInfoDO> list = accountInfoMapper.findByConditionPage(param);
		return list == null ? null : AccountInfoConvertor.DO2DTO(list);
	}

	/**
	 * 	根据id删除一条收款账户信息
	 * 	物理删除
	 * @param id
	 * @return
	 */
	public int deleteByPrimaryKey(Integer id) {
		int i = 0;
		if (id == null) {
			return i;
		}
		i = accountInfoMapper.deleteByPrimaryKey(id);
		return i;
	}
	
	/**
	 * 	根据id删除一条收款账户信息
	 * 	逻辑删除
	 * @param id
	 * @return
	 */
	public int deleteById(Integer id) {
		int i = 0;
		if (id == null) {
			return i;
		}
		AccountInfoDO record = new AccountInfoDO();
		record.setId(id);
		record.setIsDelete(WhetherEnum.YES.getType());
		i = accountInfoMapper.updateByPrimaryKeySelective(record);
		return i;
	}

	/**
	 *	 修改账户信息
	 * 	只能修改 type name account bankOrImg 这四个字段
	 * @param record
	 * @return
	 */
	public int updateAcountInfo(AccountInfoDTO record) {
		int i = 0;
		if (record.getId() == null) {
			return i;
		}
		if (!isModifyInfo(record)) {
			return i;
		}
		record.setIsDelete(null);
		AccountInfoDO accountInfoDO = AccountInfoConvertor.DTO2DO(record);
		i = accountInfoMapper.updateByPrimaryKeySelective(accountInfoDO);
		return i;
	}
	
	/**
	 * 	判断是否需要修改用户账户信息
	 * @param record
	 * @return
	 */
	private boolean isModifyInfo(AccountInfoDTO record) {
		boolean b = record.getType() != null || !HelpUtils.nullOrBlank(record.getName())
				|| !HelpUtils.nullOrBlank(record.getAccount()) || !HelpUtils.nullOrBlank(record.getBankOrImg());
		return b;
	}
}
