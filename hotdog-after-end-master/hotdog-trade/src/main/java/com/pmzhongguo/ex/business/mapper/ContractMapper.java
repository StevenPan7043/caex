package com.pmzhongguo.ex.business.mapper;


import com.pmzhongguo.contract.dto.UsdtTransferDto;
import com.pmzhongguo.contract.entity.MtCliqueUser;
import com.pmzhongguo.ex.core.mapper.SuperMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2020/4/16 9:26 AM
 */
public interface ContractMapper extends SuperMapper<UsdtTransferDto> {

    List<UsdtTransferDto> queryUsdtTransferListByPage(Map map);

    List<UsdtTransferDto> queryUsdtTransferTeam(Map map);

    BigDecimal queryUsdtTransferSum(Map map);

    List<MtCliqueUser> querySysUsers(Map map);

}
