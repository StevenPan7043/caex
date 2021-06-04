package com.pmzhongguo.ex.datalab.service.impl;

import com.pmzhongguo.ex.datalab.entity.AccountFeeDetail;
import com.pmzhongguo.ex.datalab.mapper.AccountFeeDetailMapper;
import com.pmzhongguo.ex.datalab.service.AccountFeeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jary
 * @creatTime 2019/12/7 10:33 AM
 */
@Service
@Transactional
public class AccountFeeDetailServiceImpl implements AccountFeeDetailService {

    @Autowired private AccountFeeDetailMapper accountFeeDetailMapper;

    @Override
    public Integer save(AccountFeeDetail pojo) {
        return accountFeeDetailMapper.insert(pojo);
    }

    @Override
    public Integer update(AccountFeeDetail pojo) {
        return accountFeeDetailMapper.updateById(pojo);
    }
}
