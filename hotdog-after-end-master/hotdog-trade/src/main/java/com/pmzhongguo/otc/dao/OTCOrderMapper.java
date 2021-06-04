package com.pmzhongguo.otc.dao;

import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.otc.entity.dataobject.OTCOrderDO;

public interface OTCOrderMapper extends SuperMapper<OTCOrderDO> {
	
    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OTCOrderDO record);
    
    List<OTCOrderDO> findByConditionPage(Map<String, Object> param);
    
    List<OTCOrderDO> getCancelOrderList(Map<String, Object> param);
    
    int lockTrade(OTCOrderDO record);
    int doneTrade(OTCOrderDO record);
    int cancelTrade(OTCOrderDO record);
    int cancelOrder(OTCOrderDO record);
}