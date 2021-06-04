package com.pmzhongguo.otc.dao;

import java.util.Map;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.otc.entity.dataobject.SSOTokenDO;

public interface SSOTokenMapper extends SuperMapper<SSOTokenDO> {

    SSOTokenDO findBycondision(Map<String, Object> params);

    int updateByIdOrMemberId(SSOTokenDO record);

}