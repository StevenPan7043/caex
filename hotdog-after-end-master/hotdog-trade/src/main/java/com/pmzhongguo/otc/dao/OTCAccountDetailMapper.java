package com.pmzhongguo.otc.dao;

import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.otc.entity.dataobject.OTCAccountDetailDO;

public interface OTCAccountDetailMapper extends SuperMapper<OTCAccountDetailDO> {
    int deleteByPrimaryKey(Long id);

    OTCAccountDetailDO selectByPrimaryKey(Long id);
    
    List<OTCAccountDetailDO> selectPage(Map<String, Object> params);
    
    List<Map<String, Object>> selectMemberDetailsPage(Map<String, Object> params);

    int updateByPrimaryKeySelective(OTCAccountDetailDO record);

    int updateByPrimaryKey(OTCAccountDetailDO record);
}