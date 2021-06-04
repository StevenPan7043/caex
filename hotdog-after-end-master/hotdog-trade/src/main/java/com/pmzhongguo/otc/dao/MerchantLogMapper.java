package com.pmzhongguo.otc.dao;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.otc.entity.dataobject.MerchantLogDO;

import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/5/30 9:22 AM
 */
public interface MerchantLogMapper extends SuperMapper<MerchantLogDO> {
    public List<MerchantLogDO> getCancelBusPage(Map<String, Object> param);
}
