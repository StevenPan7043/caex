package com.pmzhongguo.ex.business.service;

import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.CurrencyIntroduce;
import com.pmzhongguo.ex.business.mapper.CurrencyIntroduceMapper;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class CurrencyIntroduceService extends BaseServiceSupport {

	@Autowired
	private CurrencyIntroduceMapper introduceMapper;

	@Autowired
	CurrencyService currencyService;

	public CurrencyIntroduce getCurrencyIntroduce(Integer id) {
		return introduceMapper.getCurrencyIntroduce(id);
	}

	public CurrencyIntroduce getCurrencyIntroduceByCurrency(String name) {
		return introduceMapper.getCurrencyIntroduceByCurrency(name);
	}

	public List<CurrencyIntroduce> getAllCurrencyIntroduce(Map param) {
		return introduceMapper.listCurrencyIntroducePage(param);
	}

	public void updateCurrencyIntroduce(Map<String,Object> map) {
		// 设置时间
		map.put("update_time",HelpUtils.formatDate(new Date()));
		introduceMapper.updateCurrencyIntroduce(map);

	}

	/**
	 *
	 * @param map
	 */
	public void addCurrencyIntroduce(Map<String,Object> map) {
		String currency_name = (String)map.get("currency");
		Currency currency = currencyService.findByName(currency_name);
		if(null != currency && currency.getId() != null) {
			map.put("d_currency_id",currency.getId());
			// 设置时间
			map.put("create_time",HelpUtils.formatDate(new Date()));
			map.put("update_time",HelpUtils.formatDate(new Date()));
			introduceMapper.addCurrencyIntroduce(map);
		}else {
			throw new BusinessException(-1, ErrorInfoEnum.PLEASE_SELECT_CURRENCY.getErrorCNMsg());
		}

	}
	

}
