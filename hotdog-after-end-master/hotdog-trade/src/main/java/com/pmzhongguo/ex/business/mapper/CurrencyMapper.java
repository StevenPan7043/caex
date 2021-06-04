package com.pmzhongguo.ex.business.mapper;

import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.ex.core.mapper.SuperMapper;


public interface CurrencyMapper extends SuperMapper {

	public CurrencyPair getCurrencyPair(Integer id);

	public List<CurrencyPair> listCurrencyPairPage(Map param);

	public void editCurrencyPair(CurrencyPair currencyPair);

	public void addCurrencyPair(CurrencyPair currencyPair);
	
	
	
	public Currency getCurrency(Integer id);

	public List<Currency> listCurrencyPage(Map param);

	public void editCurrency(Currency currency);

	public void addCurrency(Currency currency);

    Currency findByName(String currency_name);

	List<CurrencyPair> findAllCurrencyPair();
}
