package com.pmzhongguo.ex.datalab.service;

        import com.pmzhongguo.ex.business.entity.CoinRecharge;
        import com.pmzhongguo.ex.datalab.entity.AccountFee;
        import com.pmzhongguo.ex.datalab.entity.AccountFeeDetail;
        import com.pmzhongguo.ex.datalab.entity.dto.AccountFeeDto;

        import java.util.List;
        import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/11/30 11:20 AM
 */
public interface AccountFeeService extends IService<AccountFee> {
    /**
     * 获取手续费资产
     *
     * @param reqMap
     * @return
     */
    List<AccountFee> getAccountFeeByMemberId(Map<String, Object> reqMap);

    /**
     * 获取手续费资产分页
     * @param reqMap
     * @return
     */
    List<AccountFeeDto> getAccountFeeByPage(Map<String, Object> reqMap);

    /**
     * 资产变动
     * @param accountFeeDB          更新参数
     * @param accountFeeDetail      明细
     * @param coinRecharge          充值记录
     */
    void accountFeeFrozen(AccountFee accountFeeDB, AccountFeeDetail accountFeeDetail, CoinRecharge coinRecharge);

    /**
     * 资产变动
     * @param accountFeeDB          更新参数
     * @param accountFeeDetail      明细
     */
    void accountFeeFrozen(AccountFee accountFeeDB, AccountFeeDetail accountFeeDetail);
}
