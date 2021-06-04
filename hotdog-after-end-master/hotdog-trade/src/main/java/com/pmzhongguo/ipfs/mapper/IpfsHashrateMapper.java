package com.pmzhongguo.ipfs.mapper;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.ipfs.entity.IpfsHashrate;

import java.util.List;
import java.util.Map;

public interface IpfsHashrateMapper extends SuperMapper<IpfsHashrate> {

    int deleteByPrimaryKey(Integer id);

    int insert(IpfsHashrate record);

    IpfsHashrate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IpfsHashrate record);

    int updateByPrimaryKey(IpfsHashrate record);

    List<IpfsHashrate> findByConditionPage(Map<String, Object> param);

    List<IpfsHashrate> findHashrate(Map<String, Object> param);
}