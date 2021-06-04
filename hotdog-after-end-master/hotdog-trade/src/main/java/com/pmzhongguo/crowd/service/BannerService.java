package com.pmzhongguo.crowd.service;

import com.pmzhongguo.crowd.dao.BannerMapper;
import com.pmzhongguo.crowd.entity.Banner;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description: banner服务
 * @date: 2019-03-02 16:06
 * @author: 十一
 */
@Service
public class BannerService {

    @Autowired
    private BannerMapper bannerMapper;

    public List<Map<String,Object>> findByScene(String scene) {
        return bannerMapper.findByScene(scene);
    }

    public List<Banner> findMgrBannerByPage(Map<String,Object> param) {
        List<Banner> bannerList = bannerMapper.findMgrBannerByPage(param);
        return bannerList;
    }

    public Banner findById(Integer id) {
        return bannerMapper.findById(id);
    }

    public void deleteById(Integer id) {
        bannerMapper.deleteById(id);
    }

    public void addBanner(Map<String, Object> map) {
        map.put("update_time", HelpUtils.formatDate8(new Date()));
        map.put("create_time", HelpUtils.formatDate8(new Date()));
        bannerMapper.addBanner(map);
    }

    public void updateBanner(Map<String, Object> map) {
        map.put("update_time",new Date());
        bannerMapper.updateBanner(map);
    }
}
