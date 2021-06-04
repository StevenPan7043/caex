package com.pmzhongguo.ipfs.service;

import com.pmzhongguo.ipfs.entity.IpfsTeamBonusDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Daily
 * @date 2020/7/23 14:12
 */
@Service
public class IpfsTeamBonusDetailManager {

    @Autowired
    private IpfsTeamBonusDetailService ipfsTeamBonusDetailService;

    public int insert(IpfsTeamBonusDetail record){
        return ipfsTeamBonusDetailService.insert(record);
    }

}
