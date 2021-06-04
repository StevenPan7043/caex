/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/16 All Rights Reserved.
 */
package com.pmzhongguo.ex.transfer.service;

import com.pmzhongguo.ex.transfer.entity.FundOperationLog;
import com.pmzhongguo.ex.transfer.mapper.FundOperationLogMapper;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/16 20:34
 * @description：资金划转操作日志service
 * @version: $
 */
@Service
@Transactional
public class FundOperationLogService extends BaseServiceSupport
{

    @Autowired
    private FundOperationLogMapper fundOperationLogMapper;

    /**
     * 插入资金划转操作日志
     *
     * @param fundOperationLog
     * @return
     */
    public int insert(FundOperationLog fundOperationLog)
    {
        if(StringUtils.isEmpty(fundOperationLog.getTableName()))
        {
            return -1;
        }
        fundOperationLog.setTableName(fundOperationLog.getTableName().toLowerCase());
        return fundOperationLogMapper.insert(fundOperationLog);
    }

    List<FundOperationLog> getFundOperationLogList(Map param) {
        return fundOperationLogMapper.getFundOperationLogList(param);
    }
    List<FundOperationLog> getFundOperationLogListByPage(Map param) {
        return fundOperationLogMapper.getFundOperationLogListByPage(param);
    }
}
