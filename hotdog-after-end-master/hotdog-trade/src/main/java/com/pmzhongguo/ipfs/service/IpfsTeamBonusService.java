package com.pmzhongguo.ipfs.service;

import com.pmzhongguo.ipfs.entity.IpfsTeamBonus;
import com.pmzhongguo.ipfs.mapper.IpfsTeamBonusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Daily
 * @date 2020/7/23 14:07
 */
@Service
public class IpfsTeamBonusService {

    @Autowired
    private IpfsTeamBonusMapper ipfsTeamBonusMapper;

    int insert(IpfsTeamBonus record){
        ipfsTeamBonusMapper.insert(record);
        return record.getId();
    }

    List<IpfsTeamBonus> findByCondition(Map<String, Object> param){
        return ipfsTeamBonusMapper.findByCondition(param);
    };

    List<IpfsTeamBonus> findByConditionPage(Map<String, Object> param){
        return ipfsTeamBonusMapper.findByConditionPage(param);
    };

}
