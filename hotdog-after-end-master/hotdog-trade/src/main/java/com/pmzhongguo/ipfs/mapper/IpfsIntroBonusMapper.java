package com.pmzhongguo.ipfs.mapper;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.ipfs.entity.IpfsIntroBonus;

import java.util.List;
import java.util.Map;

public interface IpfsIntroBonusMapper extends SuperMapper<IpfsIntroBonus> {
    int deleteByPrimaryKey(Integer id);

    int insert(IpfsIntroBonus record);

    IpfsIntroBonus selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IpfsIntroBonus record);

    int updateByPrimaryKey(IpfsIntroBonus record);

    List<IpfsIntroBonus> findByConditionPage(Map<String, Object> param);
}