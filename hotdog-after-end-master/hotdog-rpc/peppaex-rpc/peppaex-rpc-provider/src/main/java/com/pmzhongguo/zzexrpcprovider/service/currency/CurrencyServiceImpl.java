/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/10 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.service.currency;

import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.zzexrpcprovider.mapper.currency.CurrencyMapper;
import com.pmzhongguo.zzexrpcprovider.utils.HelpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/10 15:56
 * @description：币种service impl
 * @version: $
 */
@Service
@Transactional
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired(required = false)
    private CurrencyMapper currencyMapper;

    @Override
    public CurrencyPair getCurrencyPair(Integer id) {
        return null;
    }

    @Override
    public List<CurrencyPair> getAllCurrencyPair(Map param) {
        return null;
    }

    @Override
    public void editCurrencyPair(CurrencyPair currencyPair) {

    }

    @Override
    public void addCurrencyPair(CurrencyPair currencyPair) {

    }

    @Override
    public Map<String, CurrencyPair> getCurrencyPairMap() {
        Map param = new HashMap();
        param.put("p_status", 1);
        param.put("sortorder", "asc,asc");
        param.put("sortname", "area_id,p_order");
        param.put("needPage", "1");
        List<CurrencyPair> cpLst = currencyMapper.listCurrencyPairPage(param);
        Map<String, CurrencyPair> retMap = new HashMap<String, CurrencyPair>();
        for (int i = 0; i < cpLst.size(); i++) {
            retMap.put(cpLst.get(i).getKey_name(), cpLst.get(i));
        }
        return retMap;
    }

    @Override
    public List<CurrencyPair> getCurrencyPairLst() {
        Map param = new HashMap();
//        param.put("p_status", 1);
        param.put("sortorder", "asc,asc");
        param.put("sortname", "area_id,p_order");
        param.put("needPage", "1");
        Integer random = (int) (Math.random() * 1000);
        param.put("random", random);
        List<CurrencyPair> cpLst = currencyMapper.listCurrencyPairPageNeedToCache(param);
//        param.put("p_status", 3);
//        List<CurrencyPair> cpLst3 =currencyMapper.listCurrencyPairPage(param);
//        cpLst.addAll(cpLst3);
        return cpLst;
    }

    @Override
    public void cacheCurrencyPair(ServletContext servletContext) {

    }

    @Override
    public Currency getCurrency(Integer id) {
        return currencyMapper.getCurrency(id);
    }

    @Override
    public List<Currency> getAllCurrency(Map param) {
        return currencyMapper.listCurrencyPage(param);
    }

    @Override
    public void editCurrency(Currency currency) {
        currencyMapper.editCurrency(currency);
    }

    @Override
    public void addCurrency(Currency currency) {
        currencyMapper.addCurrency(currency);
    }

    @Override
    public void cacheCurrency(ServletContext servletContext) {
        Map param = new HashMap();
        param.put("is_show", "1");
        param.put("sortname", "c_order");
        param.put("sortorder", "asc");

        // 存储List格式
        List<Currency> currencyLst = getAllCurrency(param);
        servletContext.setAttribute(HelpUtils.CURRENCYLST, currencyLst);

        // 存储Map格式
        //以currency为Key，存储CurrencyMap，并存储到内存中
        //后续对Currency的获取，都到内存中获取，修改了Currency重新刷新内存
        Map<String, Currency> retMap = new HashMap<String, Currency>();

        // 存储可用于otc交易的币种,list
        List<Currency> currencyIsOtcLst = new ArrayList<Currency>();
        // 存储可用于otc交易的币种,map
        Map<String, Currency> currencyIsOtcMap = new HashMap<String, Currency>();

        for (int i = 0; i < currencyLst.size(); i++) {
            retMap.put(currencyLst.get(i).getCurrency(), currencyLst.get(i));

            // 存储可用于otc交易的币种
            if (currencyLst.get(i).getIs_otc() == 1) {
                currencyIsOtcLst.add(currencyLst.get(i));
                currencyIsOtcMap.put(currencyLst.get(i).getCurrency(), currencyLst.get(i));
            }
        }
        servletContext.setAttribute(HelpUtils.CURRENCYMAP, retMap);
        servletContext.setAttribute(HelpUtils.CURRENCY_IS_OTC_LST, currencyIsOtcLst);
        servletContext.setAttribute(HelpUtils.CURRENCY_IS_OTC_MAP, currencyIsOtcMap);
    }

    public Map<String, Object> getCurrencyMap() {
        Map param = new HashMap();
        param.put("is_show", "1");
        param.put("sortname", "c_order");
        param.put("sortorder", "asc");
        List<Currency> currencyLst = getAllCurrency(param);
        // 存储Map格式
        //以currency为Key，存储CurrencyMap，并存储到内存中
        //后续对Currency的获取，都到内存中获取，修改了Currency重新刷新内存
        Map<String, Currency> retMap = new HashMap<String, Currency>();

        // 存储可用于otc交易的币种,list
        List<Currency> currencyIsOtcLst = new ArrayList<Currency>();
        // 存储可用于otc交易的币种,map
        Map<String, Currency> currencyIsOtcMap = new HashMap<String, Currency>();
        for (int i = 0; i < currencyLst.size(); i++) {
            retMap.put(currencyLst.get(i).getCurrency(), currencyLst.get(i));
            // 存储可用于otc交易的币种
            if (currencyLst.get(i).getIs_otc() == 1) {
                currencyIsOtcLst.add(currencyLst.get(i));
                currencyIsOtcMap.put(currencyLst.get(i).getCurrency(), currencyLst.get(i));
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("retMap", retMap);
        result.put("currencyIsOtcLst", currencyIsOtcLst);
        result.put("currencyIsOtcMap", currencyIsOtcMap);
        return result;
    }

    @Override
    public Currency findByName(String currency) {
        return null;
    }

    @Override
    public List<CurrencyPair> findAllCurrencyPair() {
        return null;
    }
}
