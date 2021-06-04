package com.pmzhongguo.zzexrpcprovider.service.currency;

import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.CurrencyPair;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.Map;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/10 15:48
 * @description：currency service interface
 * @version: $
 */
public interface CurrencyService {
    CurrencyPair getCurrencyPair(Integer id);

    List<CurrencyPair> getAllCurrencyPair(Map param);

    void editCurrencyPair(CurrencyPair currencyPair);

    void addCurrencyPair(CurrencyPair currencyPair);

    /**
     * 以key_name为Key，存储CurrencyPairMap，并存储到内存中
     * 后续对CurrencyPair的获取，都到内存中获取，修改了CurrencyPair重新刷新内存
     *
     * @return
     */
    Map<String, CurrencyPair> getCurrencyPairMap();

    /**
     * 获取List并存储到内存中
     *
     * @return
     */
    List<CurrencyPair> getCurrencyPairLst();

    void cacheCurrencyPair(ServletContext servletContext);

    Currency getCurrency(Integer id);

    List<Currency> getAllCurrency(Map param);

    void editCurrency(Currency currency);

    void addCurrency(Currency currency);

    void cacheCurrency(ServletContext servletContext);

    Map<String, Object> getCurrencyMap();

    Currency findByName(String currency);

    List<CurrencyPair> findAllCurrencyPair();

}
