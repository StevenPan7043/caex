package com.pmzhongguo.ex.datalab.mapper;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.ex.datalab.entity.AccountFee;
import com.pmzhongguo.ex.datalab.entity.dto.AccountFeeDto;

import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/12/5 10:07 AM
 */
public interface AccountFeeMapper extends SuperMapper<AccountFee> {

    List<AccountFee> getAccountFeeByMemberId(Map<String,Object> reqMap);

    List<AccountFeeDto> getAccountFeeByPage(Map<String, Object> reqMap);


}
