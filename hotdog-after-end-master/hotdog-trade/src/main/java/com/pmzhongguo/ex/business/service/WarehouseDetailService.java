package com.pmzhongguo.ex.business.service;

import com.pmzhongguo.ex.business.entity.WarehouseDetail;
import com.pmzhongguo.ex.business.mapper.WarehouseDetailMapper;
import com.pmzhongguo.ex.business.vo.WarehouseAccountVo;
import com.qiniu.util.BeanUtil;
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
 * @creatTime 2019/8/29 11:48 AM
 */
@Service
@Transactional
public class WarehouseDetailService {

    @Autowired
    private WarehouseDetailMapper warehouseDetailMapper;

    public List<WarehouseDetail> selectWarehouseDetail(Map params){
        return warehouseDetailMapper.selectWarehouseDetailPage(params);
    }

    public void insetWarehouseDetail(WarehouseAccountVo warehouseAccountVo) {
        WarehouseDetail warehouseDetail = new WarehouseDetail();
        warehouseDetail.setMemberId(warehouseAccountVo.getMemberId());
        warehouseDetail.setWhAccountId(warehouseAccountVo.getId());
        warehouseDetail.setUniqueDetail(BeanUtil.isEmpty(warehouseAccountVo.getUniqueDetail())?"NaN":warehouseAccountVo.getUniqueDetail());
        warehouseDetail.setCurrency(warehouseAccountVo.getCurrency());
        warehouseDetail.setType(warehouseAccountVo.getType());
        if (warehouseAccountVo.getWarehouseReleaseAmount().compareTo(BigDecimal.ZERO) == 0) {
            warehouseDetail.setPreAccount(warehouseAccountVo.getWarehouseAmount());
            warehouseDetail.setFlotAccount(warehouseAccountVo.getWarehouseAmount());
        } else {
            warehouseDetail.setPreAccount(warehouseAccountVo.getWarehouseAmount().subtract(warehouseAccountVo.getWarehouseReleaseAmount()));
            warehouseDetail.setFlotAccount(BeanUtil.isEmpty(warehouseAccountVo.getEveryAmount()) ? BigDecimal.ZERO : warehouseAccountVo.getEveryAmount());
        }
        warehouseDetail.setCreateBy(warehouseAccountVo.getCreateBy());
        warehouseDetail.setCreateTime(DateUtil.dateToString(new Date(), DateStyleEnum.YYYY_MM_DD_HH_MM_SS));
        warehouseDetail.setUpdateBy(warehouseAccountVo.getCreateBy());
        warehouseDetail.setUpdateTime(warehouseDetail.getCreateTime());
        warehouseDetail.setIsDeleted(0);
        warehouseDetailMapper.insetWarehouseDetail(warehouseDetail);
    }
}
