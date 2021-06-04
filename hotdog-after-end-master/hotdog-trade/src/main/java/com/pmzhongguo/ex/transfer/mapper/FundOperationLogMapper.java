package com.pmzhongguo.ex.transfer.mapper;

import com.pmzhongguo.ex.transfer.entity.FundOperationLog;
import com.pmzhongguo.ex.core.mapper.SuperMapper;

import java.util.List;
import java.util.Map;

public interface FundOperationLogMapper extends SuperMapper<FundOperationLog>
{
    int deleteByPrimaryKey(Integer id, String tableName);

    int insert(FundOperationLog record);

    int insertSelective(FundOperationLog record);

    FundOperationLog selectByPrimaryKey(Integer id, String tableName);

    List<FundOperationLog> getFundOperationLogList(Map param);

    List<FundOperationLog> getFundOperationLogListByPage(Map param);

    int updateByPrimaryKeySelective(FundOperationLog record);

    int updateByPrimaryKey(FundOperationLog record);
}