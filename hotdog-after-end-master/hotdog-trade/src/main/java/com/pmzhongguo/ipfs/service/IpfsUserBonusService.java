package com.pmzhongguo.ipfs.service;

import com.pmzhongguo.ipfs.entity.IpfsOutput;
import com.pmzhongguo.ipfs.entity.IpfsUserBonus;
import com.pmzhongguo.ipfs.mapper.IpfsUserBonusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Daily
 * @date 2020/7/14 13:40
 */
@Service
public class IpfsUserBonusService {

    @Autowired
    private IpfsUserBonusMapper ipfsUserBonusMapper;

    public int insert(IpfsUserBonus record){
        return ipfsUserBonusMapper.insert(record);
    }

    List<IpfsUserBonus> findByCondition(Map<String, Object> param){
        return ipfsUserBonusMapper.findByCondition(param);
    };

    List<IpfsUserBonus> findByConditionPage(Map<String, Object> param){
        return ipfsUserBonusMapper.findByConditionPage(param);
    };

    public BigDecimal sumBonus(Map<String, Object> param){
        return ipfsUserBonusMapper.sumBonus(param);
    }

    List<Map<String, Object>> sumBonusGroupByType(Map<String, Object> param){
        return ipfsUserBonusMapper.sumBonusGroupByType(param);
    }
}
