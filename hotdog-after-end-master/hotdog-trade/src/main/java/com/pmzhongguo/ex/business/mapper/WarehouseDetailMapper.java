package com.pmzhongguo.ex.business.mapper;

import com.pmzhongguo.ex.business.entity.WarehouseDetail;
import com.pmzhongguo.ex.core.mapper.SuperMapper;

import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/8/29 11:50 AM
 */
public interface WarehouseDetailMapper extends SuperMapper {

    List<WarehouseDetail> selectWarehouseDetailPage(Map<String, Object> param);

    int insetWarehouseDetail(WarehouseDetail warehouseDetail);


}
