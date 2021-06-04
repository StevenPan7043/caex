package com.pmzhongguo.otc.service;

import com.pmzhongguo.otc.dao.MerchantLogMapper;
import com.pmzhongguo.otc.entity.convertor.MerchantLogConvertor;
import com.pmzhongguo.otc.entity.dataobject.MerchantLogDO;
import com.pmzhongguo.otc.entity.dto.MerchantLogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/5/30 9:20 AM
 */
@Service
@Transactional
public class MerchantLogService {

    @Autowired
    MerchantLogMapper merchantLogMapper;

    public int addMerchantLog(MerchantLogDO merchantLogDO) {
        return merchantLogMapper.insert(merchantLogDO);
    }

    public List<MerchantLogDTO> getCancelBusPage(Map<String, Object> param) {
        MerchantLogConvertor.initMap(param);
        List<MerchantLogDO> list = merchantLogMapper.getCancelBusPage(param);
        return list == null ? null : MerchantLogConvertor.DO2DTO(list);
    }
}
