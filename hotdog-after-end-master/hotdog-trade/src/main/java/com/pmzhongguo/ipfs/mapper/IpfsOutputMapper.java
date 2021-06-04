package com.pmzhongguo.ipfs.mapper;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.ipfs.entity.IpfsOutput;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IpfsOutputMapper extends SuperMapper<IpfsOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(IpfsOutput record);

    IpfsOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IpfsOutput record);

    int updateByPrimaryKey(IpfsOutput record);

    List<IpfsOutput> findByConditionPage(Map<String, Object> param);

    List<IpfsOutput> findByCondition(Map<String, Object> param);

    int reduceCapacity(List<IpfsOutput> list);

    int updateIpfsOutput(IpfsOutput ipfsOutput);

    BigDecimal queryCapacityDay(Map<String, Object> param);
}