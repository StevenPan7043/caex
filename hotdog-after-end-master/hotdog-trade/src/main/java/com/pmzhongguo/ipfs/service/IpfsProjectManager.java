package com.pmzhongguo.ipfs.service;

import com.pmzhongguo.ipfs.entity.IpfsProject;
import com.pmzhongguo.ipfs.entity.IpfsProjectWithBLOBs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Daily
 * @date 2020/7/15 17:21
 */
@Service
public class IpfsProjectManager {

    @Autowired
    private IpfsProjectService ipfsProjectService;

    /**
     * 插入一条项目记录
     * @param record
     * @return
     */
    public int insert(IpfsProjectWithBLOBs record){
        return ipfsProjectService.insert(record);
    }

    /**
     * 根据id update 非空字段
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(IpfsProjectWithBLOBs record){
        return ipfsProjectService.updateByPrimaryKeySelective(record);
    }

    /**
     *
     * @param record
     * @return
     */
    public int updateByPrimaryKeyWithBLOBs(IpfsProjectWithBLOBs record){
        return ipfsProjectService.updateByPrimaryKeyWithBLOBs(record);
    };

    /**
     * 查询项目列表
     * @param param
     * @return
     */
    public  List<IpfsProjectWithBLOBs> findWithBLOBsByConditionPage(Map<String, Object> param){
        return ipfsProjectService.findWithBLOBsByConditionPage(param);
    }

    public  List<IpfsProject> findByConditionPage(Map<String, Object> param){
        return ipfsProjectService.findByConditionPage(param);
    }

    /**
     * 根据主键查询项目记录
     * @param id
     * @return
     */
    public IpfsProjectWithBLOBs selectByPrimaryKey(Integer id){
        return ipfsProjectService.selectByPrimaryKey(id);
    };

    /**
     * 根据条件查询项目记录
     * @param param
     * @return
     */
    public List<IpfsProject> findIpfsProject(Map<String, Object> param){
        return ipfsProjectService.findIpfsProject(param);
    }
}
