package com.pmzhongguo.ex.business.mapper;

import com.pmzhongguo.ex.business.entity.CurrencyIntroduce;
import com.pmzhongguo.ex.core.mapper.SuperMapper;

import java.util.List;
import java.util.Map;


public interface CurrencyIntroduceMapper extends SuperMapper {

	public CurrencyIntroduce getCurrencyIntroduce(Integer id);
	public CurrencyIntroduce getCurrencyIntroduceByCurrency(String currency);

	public List<CurrencyIntroduce> listCurrencyIntroducePage(Map param);

	public void updateCurrencyIntroduce(Map param);

	public void addCurrencyIntroduce(Map param);
	
	

}
