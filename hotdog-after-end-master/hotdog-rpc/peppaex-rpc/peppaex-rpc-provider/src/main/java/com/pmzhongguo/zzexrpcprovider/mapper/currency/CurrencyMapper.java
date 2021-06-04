package com.pmzhongguo.zzexrpcprovider.mapper.currency;

import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.CurrencyPair;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/10 15:58
 * @description：币种 mapper
 * @version: $
 */
@Mapper
public interface CurrencyMapper {

    CurrencyPair getCurrencyPair(Integer id);

    List<CurrencyPair> listCurrencyPairPage(Map param);

    List<CurrencyPair> listCurrencyPairPageNeedToCache(Map param);

    void editCurrencyPair(CurrencyPair currencyPair);

    void addCurrencyPair(CurrencyPair currencyPair);

    Currency getCurrency(Integer id);

    List<Currency> listCurrencyPage(Map param);

    void editCurrency(Currency currency);

    void addCurrency(Currency currency);

    Currency findByName(String currency_name);

    List<CurrencyPair> findAllCurrencyPair();

}
