package com.pmzhongguo.otc.dao;

import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.otc.entity.dataobject.MerchantDO;

public interface MerchantMapper extends SuperMapper<MerchantDO> {
    int deleteById(Map<String, Object> param);

    int updateStatus(MerchantDO record);
    
    List<MerchantDO> findByConditionPage(Map<String, Object> param);
}