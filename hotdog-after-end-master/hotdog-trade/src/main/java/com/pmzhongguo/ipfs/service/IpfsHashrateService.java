package com.pmzhongguo.ipfs.service;

import com.pmzhongguo.ipfs.entity.IpfsHashrate;
import com.pmzhongguo.ipfs.mapper.IpfsHashrateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Daily
 * @date 2020/7/15 14:53
 */
@Service
public class IpfsHashrateService {

    @Autowired
    private IpfsHashrateMapper ipfsHashrateMapper;

    /**
     * 插入一条算力购买记录到数据库，并返回算力记录id
     * @param record
     * @return
     */
    public int insert(IpfsHashrate record){
        ipfsHashrateMapper.insert(record);
        return record.getId();
    }

    List<IpfsHashrate> findByConditionPage(Map<String, Object> param){
        return ipfsHashrateMapper.findByConditionPage(param);
    };

    public IpfsHashrate selectByPrimaryKey(Integer id) {
        return ipfsHashrateMapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(IpfsHashrate record) {
        return ipfsHashrateMapper.updateByPrimaryKeySelective(record);
    }

    public List<IpfsHashrate> findHashrate(Map<String, Object> param){
        return ipfsHashrateMapper.findHashrate(param);
    }

}
