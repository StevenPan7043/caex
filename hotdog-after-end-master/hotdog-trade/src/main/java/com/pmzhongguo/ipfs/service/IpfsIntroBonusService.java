package com.pmzhongguo.ipfs.service;

import com.pmzhongguo.ipfs.entity.IpfsIntroBonus;
import com.pmzhongguo.ipfs.mapper.IpfsIntroBonusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Daily
 * @date 2020/7/15 18:07
 */
@Service
public class IpfsIntroBonusService {

    @Autowired
    private IpfsIntroBonusMapper ipfsIntroBonusMapper;

    public int insert(IpfsIntroBonus record){
        return ipfsIntroBonusMapper.insert(record);
    }

    public List<IpfsIntroBonus> findByConditionPage(Map<String, Object> param){
        return ipfsIntroBonusMapper.findByConditionPage(param);
    };
}
