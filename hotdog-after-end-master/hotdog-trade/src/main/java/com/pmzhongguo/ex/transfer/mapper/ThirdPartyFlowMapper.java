package com.pmzhongguo.ex.transfer.mapper;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.ex.transfer.entity.ThirdPartyFlow;

/**
 * @author jary
 * @creatTime 2019/10/21 2:12 PM
 */
public interface ThirdPartyFlowMapper extends SuperMapper<ThirdPartyFlow> {

    @Override
    int insert(ThirdPartyFlow entity);
}
