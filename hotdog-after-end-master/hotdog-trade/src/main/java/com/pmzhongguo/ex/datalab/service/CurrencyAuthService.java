package com.pmzhongguo.ex.datalab.service;

import com.pmzhongguo.ex.business.dto.ReturnCommiCurrAmountDto;
import com.pmzhongguo.ex.datalab.entity.CurrencyAuth;
import com.pmzhongguo.ex.datalab.entity.dto.CurrencyAuthDto;
import com.pmzhongguo.ex.datalab.entity.dto.TradeDto;

import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/11/30 11:21 AM
 */
public interface CurrencyAuthService extends IService<CurrencyAuth> {


    /**
     * 获取所有绑定用户交易对权限
     * @param reqMap
     * @return
     */
    List<CurrencyAuthDto> getAllCurrencyAuth(Map<String,Object> reqMap);

    /**
     * 获取所有支持的交易对
     * @param reqMap
     * @return
     */
    List<String> getAllSymbol(Map<String, Object> reqMap);

    List<CurrencyAuthDto> getCurrencyAuthMap(Map<String, Object> reqMap);


    /**
     * 获取交易数据
     * @param orderMap
     * @return
     */
    List<TradeDto> getTradeListByPage(Map<String, Object> orderMap);

    /**
     * 获取交易对手续费
     * @param param
     * @return
     */
    List<ReturnCommiCurrAmountDto> getTradeFeeList(Map<String, Object> param);


}
