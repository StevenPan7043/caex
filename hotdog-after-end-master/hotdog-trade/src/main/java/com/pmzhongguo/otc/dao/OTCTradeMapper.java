package com.pmzhongguo.otc.dao;

import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.otc.entity.dataobject.OTCTradeDO;

public interface OTCTradeMapper extends SuperMapper<OTCTradeDO> {
    int deleteByPrimaryKey(Integer id);

    List<OTCTradeDO> findByConditionPage(Map<String, Object> param);
    
    List<OTCTradeDO> findBytradingPage(Map<String, Object> param);
    List<OTCTradeDO> findByComplainTradePage(Map<String, Object> param);

    int updateByPrimaryKeySelective(OTCTradeDO record);
    
    /**
     * 	条件 memberId|startDate|endDate|status 
     * 	数据库时间字段 dbDate 
     * @param param
     * @return
     */
    int countDone(Map<String, Object> param);
    
    /**
     * 	根据memberId查询申诉记录数
     * @param param
     * @return
     */
    int countComplain(Map<String, Object> param);
    
    /**
     * 	memberId and status
     * @param param
     * @return count(*) AS total, sum(consuming_time) AS consumingTime
     */
    Map<String, Object> getConsumingTime(Map<String, Object> param);
}