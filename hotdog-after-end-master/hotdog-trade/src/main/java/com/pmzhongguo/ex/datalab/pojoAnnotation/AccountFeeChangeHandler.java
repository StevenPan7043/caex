package com.pmzhongguo.ex.datalab.pojoAnnotation;

import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.datalab.entity.AccountFee;
import com.pmzhongguo.ex.datalab.manager.AccountFeeChangeService;
import com.pmzhongguo.zzextool.exception.BusinessException;

/**
 * 手续费资产变动处理器
 * 数据实验室用户所有手续费资产账户的资产变动必须得走这里。
 *
 * @author jary
 * @creatTime 2019/12/6 9:52 AM
 */
public class AccountFeeChangeHandler {

    /**
     * 资产变动接口实现类
     */
    private AccountFeeChangeService accountFeeChangeService;

    /**
     * 需变动资产
     */
    private AccountFee accountFeeChane;

    /**
     * 数据库查询资产
     */
    private AccountFee accountFeeDB;

    public AccountFeeChangeHandler(AccountFeeChangeService accountFeeChangeService, AccountFee accountFeeChane, AccountFee accountFeeDB) {
        this.accountFeeChangeService = accountFeeChangeService;
        this.accountFeeChane = accountFeeChane;
        this.accountFeeDB = accountFeeDB;
    }

    public void executeChange() {
        ObjResp objResp = accountFeeChangeService.executeAccountFeeChange(accountFeeChane, accountFeeDB);
        if (objResp.getState().equals(Resp.FAIL)) {
            throw new BusinessException(Resp.FAIL, objResp.getMsg());
        }
    }
}
