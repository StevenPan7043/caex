package com.pmzhongguo.otc.dao;

import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.otc.entity.dataobject.WCharRecordDO;

public interface WCharRecordMapper extends SuperMapper<WCharRecordDO>  {

    List<WCharRecordDO> findByConditionPage(Map<String, Object> param);

    int updateByPrimaryKeySelective(WCharRecordDO record);
}