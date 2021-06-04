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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 手续费资产增加
 *
 * @author jary
 * @creatTime 2019/12/6 9:54 AM
 */
@Component
public class AccountFeeIncreaseManager implements AccountFeeChangeService {

    @Autowired private AccountFeeService accountFeeService;

    @Override
    public ObjResp executeAccountFeeChange(AccountFee accountFeeChane, AccountFee accountFeeDB) {
        if (accountFeeDB == null || accountFeeDB.getId() == null) {
            accountFeeDB = accountFeeChane;
        } else {
            BigDecimal add = accountFeeDB.getTotalAmount().subtract(accountFeeDB.getForzenAmount()).add(accountFeeChane.getTotalAmount());
            if (add.compareTo(BigDecimal.ZERO) < 0) {
                return compareAccountFeeAmount();
            }
            accountFeeDB.setTotalAmount(accountFeeDB.getTotalAmount().add(accountFeeChane.getTotalAmount()));
        }
        accountFeeService.accountFeeFrozen(accountFeeDB, executeAccountFeeChange(accountFeeDB, accountFeeChane.getTotalAmount()));
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }

    @Override
    public AccountFeeDetail executeAccountFeeChange(AccountFee accountFeeDB, BigDecimal floatAmount) {
        return new AccountFeeDetail(
                accountFeeDB.getMemberId(),
                accountFeeDB.getFeeCurrency(),
                AccountFeeDetailEnum.INCREASE.getType(),
                accountFeeDB.getTotalAmount(),
                floatAmount.abs(),
                DateUtil.dateToString(new Date(), DateStyleEnum.YYYY_MM_DD_HH_MM_SS), null);
    }
    @Override
    public ObjResp compareAccountFeeAmount() {
        return new ObjResp(Resp.FAIL, ErrorInfoEnum.NOT_SUFFICIENT_FUNDS.getErrorENMsg(), null);
    }

}
