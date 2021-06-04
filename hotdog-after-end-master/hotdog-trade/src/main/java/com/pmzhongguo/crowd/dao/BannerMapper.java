package com.pmzhongguo.crowd.dao;

import com.pmzhongguo.crowd.entity.Banner;
import com.pmzhongguo.ex.core.mapper.SuperMapper;

import java.util.List;
import java.util.Map;

/**
 * @description: banner dao
 * @date: 2019-03-02 16:09
 * @author: 十一
 */
public interface BannerMapper extends SuperMapper<Banner> {


    List<Map<String, Object>> findByScene(String scene);

    List<Banner> findMgrBannerByPage(Map<String,Object> map);

    void addBanner(Map<String, Object> map);

    void updateBanner(Map<String, Object> map);
}
