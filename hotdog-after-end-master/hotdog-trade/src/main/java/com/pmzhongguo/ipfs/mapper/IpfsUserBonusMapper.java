package com.pmzhongguo.ipfs.mapper;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.ipfs.entity.IpfsUserBonus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IpfsUserBonusMapper extends SuperMapper<IpfsUserBonus> {
    int deleteByPrimaryKey(Integer id);

    int insert(IpfsUserBonus record);

    IpfsUserBonus selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IpfsUserBonus record);

    int updateByPrimaryKey(IpfsUserBonus record);

    List<IpfsUserBonus> findByConditionPage(Map<String, Object> param);

    List<IpfsUserBonus> findByCondition(Map<String, Object> param);

    BigDecimal sumBonus(Map<String, Object> param);

    List<Map<String, Object>> sumBonusGroupByType(Map<String, Object> param);
}