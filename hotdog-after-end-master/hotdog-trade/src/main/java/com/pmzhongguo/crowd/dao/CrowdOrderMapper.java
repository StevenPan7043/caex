package com.pmzhongguo.crowd.dao;

import com.pmzhongguo.crowd.entity.CrowdOrder;
import com.pmzhongguo.ex.core.mapper.SuperMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * @description: 一定要写注释啊
 * @date: 2019-03-03 10:17
 * @author: 十一
 */
public interface CrowdOrderMapper extends SuperMapper<CrowdOrder>{


    void insertCrowdOrder(CrowdOrder crowdOrder);

    List<CrowdOrder> findMemberIdByPage(Map<String,Object> params);

    List<Map<String,Object>>  findMgrByPage(Map<String,Object> map);


    Integer findCountMemberIdAndProjectId(Map map);

    BigDecimal findCurrencyAmountByMemberIdAndProjectId(Map map);


    void updateOrderStatusById(Map<String, Object> params);
}
