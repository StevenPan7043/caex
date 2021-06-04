package com.pmzhongguo.ex.business.mapper;

import com.pmzhongguo.ex.business.entity.LockAccount;
import com.pmzhongguo.ex.core.mapper.SuperMapper;

import java.util.List;
import java.util.Map;

/**
 * @description: 一定要写注释啊
 * @date: 2019-06-03 10:24
 * @author: 十一
 */
public interface LockAccountMapper extends SuperMapper<LockAccount> {


    /**
     * 查找锁仓账号
     * @param params
     * @return
     */
    LockAccount findByMemberIdAndCurrency(Map<String, Object> params);

    List<LockAccount> findByMemberId(Integer member_id);
}
