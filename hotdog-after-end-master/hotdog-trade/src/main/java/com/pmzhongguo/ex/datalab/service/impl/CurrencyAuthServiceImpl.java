package com.pmzhongguo.ex.datalab.service.impl;

import com.pmzhongguo.ex.business.dto.ReturnCommiCurrAmountDto;
import com.pmzhongguo.ex.business.mapper.OrderMapper;
import com.pmzhongguo.ex.datalab.entity.CurrencyAuth;
import com.pmzhongguo.ex.datalab.entity.dto.CurrencyAuthDto;
import com.pmzhongguo.ex.datalab.entity.dto.TradeDto;
import com.pmzhongguo.ex.datalab.mapper.CurrencyAuthMapper;
import com.pmzhongguo.ex.datalab.service.CurrencyAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/11/30 1:40 PM
 */
@Service
@Transactional
public class CurrencyAuthServiceImpl implements CurrencyAuthService {

    @Autowired private CurrencyAuthMapper currencyAuthMapper;
    @Autowired private OrderMapper orderMapper;


    @Override
    public List<CurrencyAuthDto> getAllCurrencyAuth(Map<String, Object> reqMap) {
        return currencyAuthMapper.getAllCurrencyAuthByPage(reqMap);
    }

    @Override
    public List<String> getAllSymbol(Map<String, Object> reqMap) {
        return currencyAuthMapper.getAllSymbol(reqMap);
    }

    @Override
    public List<CurrencyAuthDto> getCurrencyAuthMap(Map<String, Object> reqMap) {
        return currencyAuthMapper.getCurrencyAuthMap(reqMap);
    }

    @Override
    public List<TradeDto> getTradeListByPage(Map<String, Object> reqMap) {
        return orderMapper.getTradeListByPage(reqMap);
    }

    @Override
    public List<ReturnCommiCurrAmountDto> getTradeFeeList(Map<String, Object> param) {
        return orderMapper.getTradeFeeList(param);
    }

    @Override
    public Integer save(CurrencyAuth pojo) {
        return currencyAuthMapper.insert(pojo);
    }

    @Override
    public Integer update(CurrencyAuth pojo) {
        return currencyAuthMapper.updateById(pojo);
    }
}
