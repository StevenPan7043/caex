package com.pmzhongguo.crowd.dao;

import com.pmzhongguo.crowd.dto.CrowdProjectDto;
import com.pmzhongguo.crowd.entity.CrowdProject;
import com.pmzhongguo.ex.core.mapper.SuperMapper;

import java.util.List;
import java.util.Map;

/**
 * @description: 一定要写注释啊
 * @date: 2019-03-03 10:17
 * @author: 十一
 */
public interface CrowdProjectMapper extends SuperMapper<CrowdProject>{

    List<Map<String,Object>>  findByPage(Map<String,Object> map);



    CrowdProjectDto findCrowdProjectInfoById(Integer id);
    Map<String,Object> findMgrCrowdProjectInfoById(Integer id);

    List<Map<String,Object>> findMgrByPage(Map<String, Object> map);

    int insertCrowdProject(Map<String, Object> params);

    void insertCrowdProjectInfo(Map<String, Object> params);

    void updateCrowdProjectBuyInfo(CrowdProject crowdProject);

    void updateCrowdProject(Map<String, Object> params);

    void updateCrowdProjectInfo(Map<String, Object> params);

    void deleteCrowdProjectById(Integer id);

    void deleteCrowdProjectInfoById(Integer id);

    Map<String,Object> findProjectBuyInfoById(Integer id);

    CrowdProject findOrderInfoById(Integer id);

    List<CrowdProject> findAllProjectIdsAndName();
}
