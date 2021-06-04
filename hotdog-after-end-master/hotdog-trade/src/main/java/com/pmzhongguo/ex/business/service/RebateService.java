package com.pmzhongguo.ex.business.service;

import com.pmzhongguo.ex.business.mapper.AccountRebateMapper;
import com.pmzhongguo.ex.business.scheduler.AccountRebate;
import com.pmzhongguo.ex.core.utils.DaoUtil;
import com.pmzhongguo.ex.core.web.OptSourceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RebateService {

    @Autowired
    private AccountRebateMapper rebateMapper;

    @Autowired
    private DaoUtil daoUtil;

    @Autowired
    private MemberService memberService;

    public void transfer(AccountRebate accountRebate) throws Exception {
        if (null == accountRebate) {
            return;
        }
        //把奖励状态更新为1
        daoUtil.update("update m_account_rebate set r_status = 1 , update_time = now() where id = ?", accountRebate.getId());
//        if (666210 == accountRebate.getMemberId()){
//            throw new Exception();
//        }
        //0为静态奖励，获益者就是这个member 1为邀请奖励，获益人为邀请人
        if (accountRebate.getrType() == 0) {
            memberService.accountProc(accountRebate.getRebateBalance(), accountRebate.getCurrency(), accountRebate.getMemberId(), 3, OptSourceEnum.ZZEX);
        } else if (accountRebate.getrType() == 1) {
            memberService.accountProc(accountRebate.getRebateBalance(), accountRebate.getCurrency(), accountRebate.getIntroduceMId(), 3, OptSourceEnum.ZZEX);
        }
    }
}
