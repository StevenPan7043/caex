package com.pmzhongguo.ex.datalab.manager;

import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.datalab.entity.AccountFee;
import com.pmzhongguo.ex.datalab.entity.AccountFeeDetail;
import com.pmzhongguo.ex.datalab.enums.AccountFeeDetailEnum;
import com.pmzhongguo.ex.datalab.service.AccountFeeService;
import com.qiniu.util.DateStyleEnum;
import com.qiniu.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 手续费资产解冻
 *
 * @author jary
 * @creatTime 2019/12/6 9:56 AM
 */
@Component
public class AccountFeeThawManager implements AccountFeeChangeService {

    @Autowired private AccountFeeIncreaseManager AccountFeeIncreaseManager;
    @Autowired private AccountFeeService accountFeeService;

    @Override
    public ObjResp executeAccountFeeChange(AccountFee accountFeeChane, AccountFee accountFeeDB) {
        if (accountFeeDB.getId() == null){
            return new ObjResp(Resp.FAIL,ErrorInfoEnum.SYMBOL_FEE_ACCOUNT_NOT_EXIST.getErrorENMsg(),null);
        }
        // 先判断冻结资产是否正常
        BigDecimal dbRemain = accountFeeDB.getTotalAmount()
                .subtract(accountFeeDB.getForzenAmount());
        if (dbRemain.compareTo(BigDecimal.ZERO) < 0) {
            return AccountFeeIncreaseManager.compareAccountFeeAmount();
        }
        // 资产变动后，判断是否正常
        accountFeeDB.setTotalAmount(accountFeeDB.getTotalAmount().add(accountFeeChane.getForzenAmount().abs().negate()));
        BigDecimal subtract = accountFeeDB.getForzenAmount().add(accountFeeChane.getForzenAmount().abs().negate());
        if (subtract.compareTo(BigDecimal.ZERO) < 0) {
            return compareAccountFeeAmount();
        }
        accountFeeDB.setForzenAmount(subtract);
        accountFeeService.accountFeeFrozen(accountFeeDB, executeAccountFeeChange(accountFeeDB, accountFeeChane.getForzenAmount()));
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }

    /**
     * 获取手续费账户变更详情
     * @param accountFeeDB
     * @param floatAmount  浮动资产（变更的资产）
     * @return
     */
    @Override
    public AccountFeeDetail executeAccountFeeChange(AccountFee accountFeeDB, BigDecimal floatAmount) {
        return new AccountFeeDetail(
                accountFeeDB.getMemberId(),
                accountFeeDB.getFeeCurrency(),
                AccountFeeDetailEnum.THAW.getType(),
                accountFeeDB.getTotalAmount(),
                floatAmount.abs().negate(),
                DateUtil.dateToString(new Date(), DateStyleEnum.YYYY_MM_DD_HH_MM_SS), null);
    }

    @Override
    public ObjResp compareAccountFeeAmount() {
        return new ObjResp(Resp.FAIL, ErrorInfoEnum.NOT_SUFFICIENT_FROZEN_FUNDS.getErrorENMsg(), null);
    }
}
