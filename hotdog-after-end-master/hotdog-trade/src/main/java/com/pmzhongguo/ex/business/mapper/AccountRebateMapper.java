package com.pmzhongguo.ex.business.mapper;
import com.pmzhongguo.ex.business.scheduler.AccountRebate;
import com.pmzhongguo.ex.core.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AccountRebateMapper extends SuperMapper {
    public List<Map> selectStaticRewardList(Map param);
    public List<Map> selectInviteRewardList(Map param);
    public void insertRebateForeach(@Param("list") List<AccountRebate> list);
    public List<AccountRebate> getUnRewardedAccountRebateList();
    public List<AccountRebate> getAccountRebateListPage(Map params);
}
