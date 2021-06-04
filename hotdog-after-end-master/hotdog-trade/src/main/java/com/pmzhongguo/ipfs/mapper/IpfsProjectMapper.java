package com.pmzhongguo.ipfs.mapper;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.ipfs.entity.IpfsProject;
import com.pmzhongguo.ipfs.entity.IpfsProjectWithBLOBs;

import java.util.List;
import java.util.Map;

public interface IpfsProjectMapper extends SuperMapper<IpfsProjectWithBLOBs> {
    int deleteByPrimaryKey(Integer id);

    int insert(IpfsProjectWithBLOBs record);

    IpfsProjectWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IpfsProjectWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(IpfsProjectWithBLOBs record);

    int updateByPrimaryKey(IpfsProject record);

    List<IpfsProject> findByConditionPage(Map<String, Object> param);

    List<IpfsProjectWithBLOBs> findWithBLOBsByConditionPage(Map<String, Object> param);

    List<IpfsProject> findIpfsProject(Map<String, Object> param);
}