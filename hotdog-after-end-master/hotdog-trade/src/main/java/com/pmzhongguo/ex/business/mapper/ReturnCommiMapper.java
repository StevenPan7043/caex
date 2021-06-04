package com.pmzhongguo.ex.business.mapper;

import com.pmzhongguo.ex.business.entity.ReturnCommi;
import com.pmzhongguo.ex.core.mapper.SuperMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ReturnCommiMapper extends SuperMapper<ReturnCommi> {



    void insertReturnCommi(ReturnCommi returnCommi);

    ReturnCommi findHasVaildReturnCommiByMemberIdAndIntroduceId(Map<String, Object> param);

    void insertReturnCommiCurrAmount(Map<String, Object> param);



    List<Map<String,Object>> findCurrReturnCommiOfDay(Map<String, Object> param);

    void batchInsertAmountReturnCommiOfDay(List<Map<String,Object>> list);

    List<Map<String,Object>> findReturnCommiAmountWithCurrencyByPage(Map<String, Object> param);

    List<Integer> findReturnCommiByTimeAndGroupByMemberId(String curr_time);


    List<Map<String,Object>> findMgrReturnCommiAmountByPage(Map param);

    List<Map<String,Object>> findMgrReturnCommiCurrAmountByPage(Map param);

    BigDecimal findReturnCommiAmountTotalByMemberId(Integer member_id);

    int isExistByMemberIdAndIntroduceId(Map<String, Object> map);
}