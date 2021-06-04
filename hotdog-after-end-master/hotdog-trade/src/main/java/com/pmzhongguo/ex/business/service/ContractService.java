package com.pmzhongguo.ex.business.service;

import com.pmzhongguo.contract.dto.UsdtTransferDto;
import com.pmzhongguo.contract.entity.MtCliqueUser;
import com.pmzhongguo.ex.business.mapper.ContractMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2020/4/16 9:25 AM
 */
@Service
@Transactional
public class ContractService {

    @Autowired
    private ContractMapper contractMapper;

    public List<MtCliqueUser> querySysUsers(Map map){
        return contractMapper.querySysUsers(map);
    }
    /**
     * 获取转入转出笔数
     * @param map
     * @return
     */
    public List<UsdtTransferDto> queryUsdtTransferMapByPage(Map map) {
        return contractMapper.queryUsdtTransferListByPage(map);
    }

    public List<UsdtTransferDto> queryUsdtTransferTeam(Map map) {
        return contractMapper.queryUsdtTransferTeam(map);
    }

    public BigDecimal queryUsdtTransferSum(Map map){
        return contractMapper.queryUsdtTransferSum(map);
    }

}
