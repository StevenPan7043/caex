package com.pmzhongguo.ex.business.service.manager;

import com.contract.dto.MoneyDto;
import com.contract.entity.CWallet;
import com.pmzhongguo.contract.dto.ContractAccountDto;
import com.pmzhongguo.ex.business.scheduler.UsdtCnyPriceScheduler;
import com.pmzhongguo.ex.business.service.ContractAccountDetailService;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import com.pmzhongguo.otc.entity.dto.OTCAccountDetailDTO;
import com.pmzhongguo.otc.service.OTCAccountDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@Transactional
public class ContractAccountDetailManager extends BaseServiceSupport  {

	@Autowired
	private ContractAccountDetailService contractAccountDetailService;
	

	public List<MoneyDto> selectUsdtPage(Map<String, Object> params) {
		return contractAccountDetailService.selectUsdtPage(params);
	}

	public List<MoneyDto> selectZcPage(Map<String, Object> params) {
		return contractAccountDetailService.selectZcPage(params);
	}

	public ContractAccountDto getAccounts(Integer memberId){
		CWallet accounts = contractAccountDetailService.getAccounts(memberId);
		return CWalletToContractAccountDto(accounts);
	}

	public ContractAccountDto CWalletToContractAccountDto(CWallet accounts){
		ContractAccountDto contractAccountDto = new ContractAccountDto();
		contractAccountDto.setCurrency(accounts.getType());
		contractAccountDto.setCid(accounts.getCid());
		contractAccountDto.setBalance(accounts.getBalance());
		contractAccountDto.setZcbalance(accounts.getZcbalance());
		contractAccountDto.setCnyBalance(accounts.getBalance().multiply(UsdtCnyPriceScheduler.PRICE));
		contractAccountDto.setCnyZcbalance(accounts.getZcbalance().multiply(UsdtCnyPriceScheduler.PRICE));
		return contractAccountDto;
	}
}
