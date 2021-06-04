package com.pmzhongguo.ex.transfer.mapper;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.ex.transfer.entity.FundOperationLog;
import com.pmzhongguo.ex.transfer.entity.ThirdPartyInfo;

import java.util.List;
import java.util.Map;

public interface ThirdPartyMapper extends SuperMapper<ThirdPartyInfo>
{
    int deleteByPrimaryKey(Integer id);

    int insert(ThirdPartyInfo record);

    int insertSelective(ThirdPartyInfo record);

    ThirdPartyInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ThirdPartyInfo record);

    int updateByPrimaryKey(ThirdPartyInfo record);

    List<ThirdPartyInfo> findList(ThirdPartyInfo thirdPartyInfo);

    List<ThirdPartyInfo> getAllThirdPartyPage(Map param);
}