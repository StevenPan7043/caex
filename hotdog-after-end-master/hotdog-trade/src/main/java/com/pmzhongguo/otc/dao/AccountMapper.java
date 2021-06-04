package com.pmzhongguo.otc.dao;

import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.otc.entity.dataobject.AccountDO;
import com.pmzhongguo.otc.entity.dto.AccountDTO;

public interface AccountMapper extends SuperMapper<AccountDO> {
    int deleteByPrimaryKey(Integer id);

    List<AccountDO> findByConditionPage(Map<String, Object> param);

    int updateByPrimaryKeySelective(AccountDO record);
    
    /**
     * 	增加总额[+total_balance]
     * @param account
     * @return
     */
    int addTotalBalance(AccountDO account);
    
    /**
     * 	冻结[+frozen_balance]
     * @param account
     * @return
     */
    int addFrozenBalance(AccountDO account);
    
    /**
     * 	还原冻结[-frozen_balance]
     * 	也就是取消冻结
     * @param account
     * @return
     */
    int returnFrozenBalance(AccountDO account);
    
    /**
     * 	解冻[-frozen_balance，同时-total_balance]
     * 	还原冻结和解冻的区别是：解冻是成交，还原冻结是取消
     * @param account
     * @return
     */
    int reduceFrozenBalance(AccountDO account);
}