package com.pmzhongguo.ex.transfer.mapper;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.otc.entity.dataobject.SSOTokenDO;

import java.util.Map;

public interface SSOUsersTokenMapper extends SuperMapper<SSOTokenDO> {

    SSOTokenDO findBycondision(Map<String, Object> params);

    int updateByIdOrMemberId(SSOTokenDO record);

}