package com.pmzhongguo.ipfs.service;

import com.pmzhongguo.ipfs.entity.IpfsTeamBonusDetail;
import com.pmzhongguo.ipfs.mapper.IpfsTeamBonusDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Daily
 * @date 2020/7/23 14:06
 */
@Service
public class IpfsTeamBonusDetailService {

    @Autowired
    private IpfsTeamBonusDetailMapper ipfsTeamBonusDetailMapper;

    int insert(IpfsTeamBonusDetail record){
        return ipfsTeamBonusDetailMapper.insert(record);
    }
}
