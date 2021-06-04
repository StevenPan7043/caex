package com.pmzhongguo.ex.business.mapper;

import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.business.dto.AccountDetailDto;
import com.pmzhongguo.ex.core.mapper.SuperMapper;

public interface AccountDetailMapper extends SuperMapper<AccountDetailDto> {
    int deleteByPrimaryKey(Long id);

    int insert(AccountDetailDto record);

    AccountDetailDto selectByPrimaryKey(Long id);
    
    List<AccountDetailDto> selectPage(Map<String, Object> params);

    List<AccountDetailDto> selectOTCPage(Map<String, Object> params);

    int updateByPrimaryKeySelective(AccountDetailDto record);

    int updateByPrimaryKey(AccountDetailDto record);
}