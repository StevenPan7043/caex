package com.pmzhongguo.ex.business.service;

import com.contract.dto.MoneyDto;
import com.contract.entity.CWallet;
import com.pmzhongguo.contract.dto.ContractAccountDto;
import com.pmzhongguo.ex.business.mapper.CUsdtDetailMapper;
import com.pmzhongguo.ex.business.mapper.CZcDetailMapper;
import com.pmzhongguo.ex.business.mapper.ContractWalletMapper;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import com.pmzhongguo.otc.dao.OTCAccountDetailMapper;
import com.pmzhongguo.otc.entity.convertor.OTCAccountDetailConvertor;
import com.pmzhongguo.otc.entity.dataobject.OTCAccountDetailDO;
import com.pmzhongguo.otc.entity.dto.OTCAccountDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@Transactional
public class ContractAccountDetailService extends BaseServiceSupport  {

	@Autowired
	private CUsdtDetailMapper cUsdtDetailMapper;

	@Autowired
	private CZcDetailMapper cZcDetailMapper;


	@Autowired
	private ContractWalletMapper contractWalletMapper;


	public List<MoneyDto> selectUsdtPage(Map<String, Object> params) {
		OTCAccountDetailConvertor.initMap(params);
		List<MoneyDto> list = cUsdtDetailMapper.selectPage(params);
		return list;
	}

	public List<MoneyDto> selectZcPage(Map<String, Object> params) {
		OTCAccountDetailConvertor.initMap(params);
		List<MoneyDto> list = cZcDetailMapper.selectPage(params);
		return list;
	}


    public CWallet getAccounts(Integer memberId) {
		CWallet wallet = contractWalletMapper.getByCid(memberId);
		return wallet;
	}
}
