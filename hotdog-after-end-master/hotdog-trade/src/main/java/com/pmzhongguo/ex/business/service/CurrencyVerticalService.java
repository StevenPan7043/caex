package com.pmzhongguo.ex.business.service;

import com.pmzhongguo.ex.business.dto.CurrencyVerticalDto;
import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.CurrencyVertical;
import com.pmzhongguo.ex.business.mapper.CurrencyVerticalMapper;
import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.ex.core.service.BaseService;
import com.qiniu.util.DateStyleEnum;
import com.qiniu.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author jary
 * @creatTime 2019/11/25 2:11 PM
 */
@Service
@Transactional
public class CurrencyVerticalService extends BaseService<CurrencyVertical> {

    @Autowired
    private CurrencyVerticalMapper currencyVerticalMapper;

    @Override
    public SuperMapper<CurrencyVertical> getMapper() {
        return currencyVerticalMapper;
    }

    public List<CurrencyVerticalDto> getCurrencyVerticalList(CurrencyVerticalDto currencyVerticalDto) {
        return currencyVerticalMapper.getCurrencyVerticalList(currencyVerticalDto);
    }

    public void editCurrencyVertical(List<CurrencyVerticalDto> verticalDtoList, Currency currency) {
        for (CurrencyVerticalDto c : verticalDtoList) {
            editCurrencyVertical(c, currency);
        }
    }

    public void editCurrencyVertical(CurrencyVerticalDto verticalDto, Currency currency) {
        String value = verticalDto.getValue();
        verticalDto.setValue("");
        List<CurrencyVerticalDto> currencyVerticalList = this.getCurrencyVerticalList(verticalDto);
        if (CollectionUtils.isEmpty(currencyVerticalList)) {
            CurrencyVertical recharge = createCurrencyVerticalParam(currency.getId(), verticalDto.getColumn(), verticalDto.getCategoryKey(), value);
            currencyVerticalMapper.insert(recharge);
        } else {
            CurrencyVerticalDto currencyVerticalDto = currencyVerticalList.get(0);
            currencyVerticalDto.setValue(value);
            currencyVerticalDto.setUpdateTime(DateUtil.dateToString(new Date(),DateStyleEnum.YYYY_MM_DD_HH_MM_SS));
            currencyVerticalMapper.updateById(currencyVerticalDto);
        }
    }


    /**
     * 生成币种纵表信息
     * @param currencyId    币种id
     * @param column		币种表横标字段
     * @param categoryKey	币种表行表类型
     * @param value			币种表行表value
     * @return
     */
    private CurrencyVertical createCurrencyVerticalParam(Integer currencyId,String column,String categoryKey,String value){
        CurrencyVertical currencyVerticalParam = new CurrencyVertical();
        currencyVerticalParam.setCurrencyId(currencyId);
        currencyVerticalParam.setColumn(column);
        currencyVerticalParam.setCategoryKey(categoryKey);
        currencyVerticalParam.setValue(value);
        currencyVerticalParam.setCreateTime(DateUtil.dateToString(new Date(), DateStyleEnum.YYYY_MM_DD_HH_MM_SS));
        currencyVerticalParam.setUpdateTime(currencyVerticalParam.getCreateTime());
        return currencyVerticalParam;
    }

}
