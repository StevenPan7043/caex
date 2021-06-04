package com.pmzhongguo.crowd.service;

import com.pmzhongguo.crowd.config.rediskey.CrowdProjectKey;
import com.pmzhongguo.crowd.dao.CrowdProjectMapper;
import com.pmzhongguo.crowd.dto.CrowdProjectDto;
import com.pmzhongguo.crowd.entity.CrowdProject;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 一定要写注释啊
 * @date: 2019-03-03 10:15
 * @author: 十一
 */
@Transactional
@Service
public class CrowdProjectService {

    private static Logger logger = LoggerFactory.getLogger(CrowdProjectService.class);

    @Autowired
    private CrowdProjectMapper crowdProjectMapper;





    public List<Map<String,Object>> findByPage(Map<String,Object> map) {

        return crowdProjectMapper.findByPage(map);
    }

    public CrowdProject findById(Integer id) {
        return crowdProjectMapper.findById(id);
    }

    public CrowdProjectDto findCrowdProjectInfoById(Integer id) {
        return crowdProjectMapper.findCrowdProjectInfoById(id);
    }

    public Map<String,Object> findMgrCrowdProjectInfoById(Integer id) {
        return crowdProjectMapper.findMgrCrowdProjectInfoById(id);
    }



    public Map<String,Object> findProjectBuyInfoById(Integer id) {

        return crowdProjectMapper.findProjectBuyInfoById(id);
    }

    public List<Map<String,Object>> findMgrByPage(Map<String,Object> map) {

        return crowdProjectMapper.findMgrByPage(map);
    }

    public void addCrowdProject(Map<String,Object> params) {
        params.put("create_time",HelpUtils.formatDate8(new Date()));
        params.put("update_time",HelpUtils.formatDate8(new Date()));
        Map<String,Object> projectMap = new HashMap<String,Object>();
        projectMap.put("rush_price",params.get("rush_price"));
        projectMap.put("is_lock",params.get("is_lock"));
        projectMap.put("is_show",params.get("is_show"));
        projectMap.put("c_precision",params.get("c_precision"));
        projectMap.put("p_precision",params.get("p_precision"));
        projectMap.put("name",params.get("name"));
        projectMap.put("currency",params.get("currency"));
        projectMap.put("quote_currency",params.get("quote_currency"));
        projectMap.put("buy_lower_limit",params.get("buy_lower_limit"));
        projectMap.put("buy_upper_limit",params.get("buy_upper_limit"));
        projectMap.put("release_amount",params.get("release_amount"));
        projectMap.put("remain_amount",params.get("remain_amount"));
        projectMap.put("buy_person_count",params.get("buy_person_count"));
        projectMap.put("rush_begin_time",params.get("rush_begin_time"));
        projectMap.put("rush_end_time",params.get("rush_end_time"));
        crowdProjectMapper.insertCrowdProject(projectMap);
        params.put("id",projectMap.get("id"));
        crowdProjectMapper.insertCrowdProjectInfo(params);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateCrowdProject(Map<String, Object> params) {
        params.put("update_time",HelpUtils.formatDate8(new Date()));
        crowdProjectMapper.updateCrowdProject(params);
        crowdProjectMapper.updateCrowdProjectInfo(params);
        // 如果更新就删除redis中的缓存
        String key = CrowdProjectKey.crowdProjectKey.getPrefix() + ":" + params.get("id");
        JedisUtil.getInstance().del(key);
    }

    public void updateCrowdProjectBuyInfo(CrowdProject crowdProject) {
        crowdProject.setUpdate_time(HelpUtils.formatDate8(new Date()));
        crowdProjectMapper.updateCrowdProjectBuyInfo(crowdProject);
    }

    public void delCrowdProject(Integer id) {
        crowdProjectMapper.deleteCrowdProjectById(id);
        crowdProjectMapper.deleteCrowdProjectInfoById(id);
    }

    public CrowdProject findOrderInfoById(Integer project_id) {
        return crowdProjectMapper.findOrderInfoById(project_id);
    }

    public List<CrowdProject> findAllProjectIdsAndName() {
        return crowdProjectMapper.findAllProjectIdsAndName();
    }
}
