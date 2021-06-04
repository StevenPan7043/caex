package com.pmzhongguo.ipfs.mapper;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.ipfs.entity.IpfsTeamBonusDetail;

import java.util.List;
import java.util.Map;

public interface IpfsTeamBonusDetailMapper  extends SuperMapper<IpfsTeamBonusDetail> {
    int deleteByPrimaryKey(Integer id);

    int insert(IpfsTeamBonusDetail record);

    IpfsTeamBonusDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IpfsTeamBonusDetail record);

    int updateByPrimaryKey(IpfsTeamBonusDetail record);

    List<IpfsTeamBonusDetail> findByConditionPage(Map<String, Object> param);
}