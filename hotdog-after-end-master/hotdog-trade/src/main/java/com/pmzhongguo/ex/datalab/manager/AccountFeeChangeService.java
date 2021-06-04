package com.pmzhongguo.ex.datalab.manager;

import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.datalab.entity.AccountFee;
import com.pmzhongguo.ex.datalab.entity.AccountFeeDetail;

import java.math.BigDecimal;

/**
 * @author jary
 * @creatTime 2019/12/6 9:47 AM
 */
public interface AccountFeeChangeService {

    /**
     * 手续费资产变动
     *
     * @param accountFeeChane 需变动资产
     * @param accountFeeDB    数据库查询资产
     */
    ObjResp executeAccountFeeChange(AccountFee accountFeeChane, AccountFee accountFeeDB);

    /**
     * 生成变动资产明细
     *
     * @param accountFeeDB
     * @param floatAmount  浮动资产
     * @return
     */
    AccountFeeDetail executeAccountFeeChange(AccountFee accountFeeDB, BigDecimal floatAmount);

    /**
     * 手续费资产比较异常信息
     *
     * @return
     */
    ObjResp compareAccountFeeAmount();

}
