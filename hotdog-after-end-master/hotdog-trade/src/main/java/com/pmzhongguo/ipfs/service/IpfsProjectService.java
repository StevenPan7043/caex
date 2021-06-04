package com.pmzhongguo.ipfs.service;

import com.pmzhongguo.ipfs.entity.IpfsProject;
import com.pmzhongguo.ipfs.entity.IpfsProjectWithBLOBs;
import com.pmzhongguo.ipfs.mapper.IpfsProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Daily
 * @date 2020/7/15 17:09
 */
@Service
public class IpfsProjectService {

    @Autowired
    private IpfsProjectMapper ipfsProjectMapper;

    /**
     * 插入一条项目记录
     * @param record
     * @return
     */
    public int insert(IpfsProjectWithBLOBs record){
        return ipfsProjectMapper.insert(record);
    }

    /**
     * 根据id update 非空字段
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(IpfsProjectWithBLOBs record){
        return ipfsProjectMapper.updateByPrimaryKeySelective(record);
    }

    int updateByPrimaryKeyWithBLOBs(IpfsProjectWithBLOBs record){
        return ipfsProjectMapper.updateByPrimaryKeyWithBLOBs(record);
    };

    /**
     * 查询项目列表
     * @param param
     * @return
     */
    public List<IpfsProjectWithBLOBs> findWithBLOBsByConditionPage(Map<String, Object> param){
        return ipfsProjectMapper.findWithBLOBsByConditionPage(param);
    }

    public List<IpfsProject> findByConditionPage(Map<String, Object> param){
        return ipfsProjectMapper.findByConditionPage(param);
    }

    /**
     * 根据主键查询项目记录
     * @param id
     * @return
     */
    IpfsProjectWithBLOBs selectByPrimaryKey(Integer id){
        return ipfsProjectMapper.selectByPrimaryKey(id);
    };

    /**
     * 根据条件查询项目记录
     * @param param
     * @return
     */
    public List<IpfsProject> findIpfsProject(Map<String, Object> param){
        return ipfsProjectMapper.findIpfsProject(param);
    }
}
