package com.pmzhongguo.ex.business.mapper;

import com.pmzhongguo.ex.business.dto.AccountHistoryDto;
import com.pmzhongguo.ex.core.mapper.SuperMapper;

import java.util.List;
import java.util.Map;

public interface AccountHistoryMapper extends SuperMapper {

    /**
     * 获取用户每日总资产：合约
     *
     * startTime 开始时间
     * endTime   结束时间
     * m_name    用户账号
     * @return
     */
    List<AccountHistoryDto> queryCWallentHistory(Map map);
    /**
     * 获取用户每日总资产：币币
     *
     * startTime 开始时间
     * endTime   结束时间
     * m_name    用户账号
     * @return
     */
    List<AccountHistoryDto> queryMAccountHistory(Map map);
    /**
     * 获取用户每日总资产：法币
     *
     * startTime 开始时间
     * endTime   结束时间
     * m_name    用户账号
     * @return
     */
    List<AccountHistoryDto> queryOAccountHistory(Map map);

    /**
     * 获取用户每日总资产：合约+币币+法币
     *
     * startTime 开始时间
     * endTime   结束时间
     * m_name    用户账号
     * @return
     */
    List<AccountHistoryDto> queryAllHistoryPage(Map map);

}
