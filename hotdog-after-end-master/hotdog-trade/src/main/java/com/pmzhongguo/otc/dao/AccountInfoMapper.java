package com.pmzhongguo.otc.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.otc.entity.dataobject.AccountInfoDO;

public interface AccountInfoMapper extends SuperMapper<AccountInfoDO> {
    int deleteByPrimaryKey(Integer id);

    List<AccountInfoDO> findByConditionPage(Map<String, Object> param);

    int updateByPrimaryKeySelective(AccountInfoDO record);
    
    public AccountInfoDO findByIdAll(Integer id);
}