package com.pmzhongguo.crowd.dao;

import com.pmzhongguo.crowd.entity.CrowdJob;
import com.pmzhongguo.ex.core.mapper.SuperMapper;

import java.util.List;
import java.util.Map;

/**
 * @description: 定时调度任务
 * @date: 2019-03-02 16:09
 * @author: 十一
 */
public interface JobMapper extends SuperMapper<CrowdJob> {


    void update(CrowdJob crowdJob);

    void add(CrowdJob crowdJob);

    List<CrowdJob> findByPage(Map<String, Object> param);


    /**
     * 统计job_name
     * @param job_name
     */
    int countJobGroupName(String job_name);

    int countJob();
}
