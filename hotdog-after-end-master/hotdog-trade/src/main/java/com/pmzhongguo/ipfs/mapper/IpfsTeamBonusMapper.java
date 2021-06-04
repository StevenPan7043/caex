package com.pmzhongguo.ipfs.mapper;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.ipfs.entity.IpfsTeamBonus;

import java.util.List;
import java.util.Map;

public interface IpfsTeamBonusMapper  extends SuperMapper<IpfsTeamBonus> {
    int deleteByPrimaryKey(Integer id);

    int insert(IpfsTeamBonus record);

    IpfsTeamBonus selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IpfsTeamBonus record);

    int updateByPrimaryKey(IpfsTeamBonus record);

    List<IpfsTeamBonus> findByConditionPage(Map<String, Object> param);

    List<IpfsTeamBonus> findByCondition(Map<String, Object> param);
}