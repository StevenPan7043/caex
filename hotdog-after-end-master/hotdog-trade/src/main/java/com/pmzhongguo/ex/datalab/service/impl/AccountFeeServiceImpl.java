package com.pmzhongguo.ex.datalab.service.impl;

import com.pmzhongguo.ex.business.entity.CoinRecharge;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.datalab.entity.AccountFee;
import com.pmzhongguo.ex.datalab.entity.AccountFeeDetail;
import com.pmzhongguo.ex.datalab.entity.dto.AccountFeeDto;
import com.pmzhongguo.ex.datalab.mapper.AccountFeeMapper;
import com.pmzhongguo.ex.datalab.service.AccountFeeDetailService;
import com.pmzhongguo.ex.datalab.service.AccountFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/12/5 10:05 AM
 */
@Service
@Transactional
public class AccountFeeServiceImpl implements AccountFeeService {

    @Autowired private MemberService memberService;
    @Autowired private AccountFeeDetailService accountFeeDetailService;

    @Autowired private AccountFeeMapper accountFeeMapper;


    @Override
    public Integer save(AccountFee pojo) {
        return accountFeeMapper.insert(pojo);
    }

    @Override
    public Integer update(AccountFee pojo) {
        return accountFeeMapper.updateById(pojo);
    }

    @Override
    public List<AccountFee> getAccountFeeByMemberId(Map<String, Object> reqMap) {
        return accountFeeMapper.getAccountFeeByMemberId(reqMap);
    }

    @Override
    public List<AccountFeeDto> getAccountFeeByPage(Map<String, Object> reqMap) {
        return accountFeeMapper.getAccountFeeByPage(reqMap);
    }

    @Override
    public void accountFeeFrozen(AccountFee accountFeeDB, AccountFeeDetail accountFeeDetail, CoinRecharge coinRecharge) {
        accountFeeFrozen(accountFeeDB, accountFeeDetail);
        memberService.addCoinRechargeByMember(coinRecharge);
    }

    @Override
    public void accountFeeFrozen(AccountFee accountFeeDB, AccountFeeDetail accountFeeDetail) {
        if (accountFeeDB.getId() == null) {
            save(accountFeeDB);
        } else {
            update(accountFeeDB);
        }
        accountFeeDetailService.save(accountFeeDetail);
    }



}
