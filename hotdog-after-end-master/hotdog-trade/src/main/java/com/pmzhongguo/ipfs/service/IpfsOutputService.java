package com.pmzhongguo.ipfs.service;

import com.pmzhongguo.ipfs.entity.IpfsOutput;
import com.pmzhongguo.ipfs.mapper.IpfsOutputMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Daily
 * @date 2020/7/13 16:20
 */
@Service
public class IpfsOutputService {

    @Autowired
    private IpfsOutputMapper ipfsOutputMapper;

    /**
     * 插入一条当天产量记录
     *
     * @param ipfsOutput
     * @return
     */
    public int insert(IpfsOutput ipfsOutput) {
        return ipfsOutputMapper.insert(ipfsOutput);
    }

    List<IpfsOutput> findByConditionPage(Map<String, Object> param) {
        return ipfsOutputMapper.findByConditionPage(param);
    }

    List<IpfsOutput> findByCondition(Map<String, Object> param) {
        return ipfsOutputMapper.findByCondition(param);
    }

    int reduceCapacity(List<IpfsOutput> list) {
        return ipfsOutputMapper.reduceCapacity(list);
    }

    int updateIpfsOutput(IpfsOutput ipfsOutput) {
        return ipfsOutputMapper.updateIpfsOutput(ipfsOutput);
    }

    //获取某天的总分红
    BigDecimal queryCapacityDay(Map<String, Object> param) {
        return ipfsOutputMapper.queryCapacityDay(param);
    }
}
