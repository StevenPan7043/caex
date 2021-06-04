package com.contract.service.cms;

import com.contract.dao.CCustomerMapper;
import com.contract.dao.CUsdtDetailMapper;
import com.contract.dto.UsdtTransferDto;
import com.contract.entity.CCustomer;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2020/2/22 9:53 AM
 */
@Service
public class CUsdtDetailService {
    @Autowired
    private CUsdtDetailMapper cUsdtDetailMapper;

    /**
     * 获取划转信息
     * @param usdtTransferDto
     * @return
     */
    public List<UsdtTransferDto> queryUsdtRechargeList(UsdtTransferDto usdtTransferDto) {
        List<UsdtTransferDto> usdtTransferDtos = null;
        try {
            PageHelper.startPage(usdtTransferDto.getPage(), usdtTransferDto.getRows());
            usdtTransferDtos = cUsdtDetailMapper.queryUsdtTransferList(usdtTransferDto);
        }catch (Exception e){
            System.out.println(e.fillInStackTrace());
        }

        return usdtTransferDtos;
    }

    /**
     * 获取转入转出笔数
     * @param usdtTransferDto
     * @return
     */
    public Map<String, Object> queryUsdtTransferMap(UsdtTransferDto usdtTransferDto) {
        return cUsdtDetailMapper.queryUsdtTransferMap(usdtTransferDto);
    }
}
