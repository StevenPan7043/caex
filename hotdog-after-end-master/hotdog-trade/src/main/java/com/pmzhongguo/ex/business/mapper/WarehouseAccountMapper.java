package com.pmzhongguo.ex.business.mapper;

import com.pmzhongguo.ex.business.vo.WarehouseAccountVo;
import com.pmzhongguo.ex.core.mapper.SuperMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/8/29 11:29 AM
 */
public interface WarehouseAccountMapper extends SuperMapper {

    List<WarehouseAccountVo> getWarehouseAccountListPage(Map<String, Object> param);

    BigDecimal getSumWarehouseAccount(Map<String, Object> param);

    /**
     * 纯新增
     * @param warehouseAccountVo
     * @return
     */
    int insertWarehouseAccount(WarehouseAccountVo warehouseAccountVo);

    List<WarehouseAccountVo> getWarehouseAccountList(WarehouseAccountVo warehouseAccountVo);




}
