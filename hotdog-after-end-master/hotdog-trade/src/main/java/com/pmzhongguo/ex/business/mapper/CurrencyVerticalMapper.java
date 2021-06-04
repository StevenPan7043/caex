package com.pmzhongguo.ex.business.mapper;

import com.pmzhongguo.ex.business.dto.CurrencyVerticalDto;
import com.pmzhongguo.ex.business.entity.CurrencyVertical;
import com.pmzhongguo.ex.core.mapper.SuperMapper;

import java.util.List;

/**
 * @author jary
 * @creatTime 2019/11/25 2:13 PM
 */
public interface CurrencyVerticalMapper extends SuperMapper<CurrencyVertical> {

    List<CurrencyVerticalDto> getCurrencyVerticalList(CurrencyVerticalDto currencyVerticalDto);


    int updateById(CurrencyVerticalDto currencyVerticalDto);
}
