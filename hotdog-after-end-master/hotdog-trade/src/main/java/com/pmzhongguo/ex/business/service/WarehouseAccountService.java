package com.pmzhongguo.ex.business.service;

import com.pmzhongguo.ex.business.mapper.WarehouseAccountMapper;
import com.pmzhongguo.ex.business.vo.WarehouseAccountVo;
import com.pmzhongguo.ex.core.web.LockReleaseEnum;
import com.pmzhongguo.ex.core.web.ReleaseTypeEnum;
import com.qiniu.util.DateStyleEnum;
import com.qiniu.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/8/29 11:29 AM
 */
@Service
@Transactional
public class WarehouseAccountService {

    @Autowired
    private WarehouseAccountMapper warehouseAccountMapper;

    @Autowired
    private WarehouseDetailService warehouseDetailService;

    public List<WarehouseAccountVo> getWarehouseAccountListPage(Map params ) {
        return warehouseAccountMapper.getWarehouseAccountListPage(params);
    }


    /**
     * 获取锁仓未释放总额
     * @Auth Jary
     * @param param
     * @return
     */
    public BigDecimal getSumWarehouseAccount(Map<String, Object> param){
        return warehouseAccountMapper.getSumWarehouseAccount(param);
    }

    /**
     * 充值待锁仓锁仓资产
     * @param warehouseAccountVo
     */
    public void returnAssets(WarehouseAccountVo warehouseAccountVo) {
        WarehouseAccountVo wavParam = new WarehouseAccountVo();
        wavParam.setMemberId(warehouseAccountVo.getMemberId());
        wavParam.setAccountId(0);
        wavParam.setCurrency(warehouseAccountVo.getCurrency());
        wavParam.setWarehouseAmount(warehouseAccountVo.getWarehouseAmount());
        wavParam.setWarehouseReleaseAmount(new BigDecimal("0"));
        wavParam.setWarehouseCount(0);
        wavParam.setWarehouseReleaseCount(0);
        wavParam.setNextReleaseTime("1000000000000000");
        wavParam.setRuleIds(0 + "");
        wavParam.setIsRelease(LockReleaseEnum.UNRELEASED.getType());
        wavParam.setRAddress(warehouseAccountVo.getRAddress());
        wavParam.setEveryAmount(new BigDecimal("0"));
        wavParam.setType(ReleaseTypeEnum.RECHARGE.getType());
        wavParam.setCoinRechargeId(warehouseAccountVo.getCoinRechargeId());
        wavParam.setId(0);
        wavParam.setCreateBy(warehouseAccountVo.getCreateBy());
        wavParam.setCreateTime(DateUtil.dateToString(new Date(), DateStyleEnum.YYYY_MM_DD_HH_MM_SS));
        wavParam.setUpdateBy(wavParam.getCreateBy());
        wavParam.setUpdateTime(wavParam.getCreateTime());
        wavParam.setIsDeleted(0);

        insertWarehouseById(wavParam);
    }

    public void insertWarehouseById(WarehouseAccountVo warehouseAccountVo) {
        warehouseAccountMapper.insertWarehouseAccount(warehouseAccountVo);
//        List<WarehouseAccountVo> warehouseAccountList = warehouseAccountMapper.getWarehouseAccountList(warehouseAccountVo);
//        warehouseAccountVo.setId(warehouseAccountList.get(0).getId());
//        warehouseDetailService.insetWarehouseDetail(warehouseAccountVo);
    }
}
