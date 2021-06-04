package com.pmzhongguo.ex.datalab.mapper;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.ex.datalab.entity.CurrencyAuth;
import com.pmzhongguo.ex.datalab.entity.dto.CurrencyAuthDto;

import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/11/30 1:43 PM
 */
public interface CurrencyAuthMapper extends SuperMapper<CurrencyAuth> {

    List<CurrencyAuthDto> getAllCurrencyAuthByPage(Map<String, Object> reqMap);

    List<CurrencyAuthDto> getCurrencyAuthMap(Map<String, Object> reqMap);


    List<String> getAllSymbol(Map<String, Object> reqMap);
}
