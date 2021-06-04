package com.pmzhongguo.ex.validate.transfer.inter;

import com.contract.entity.CWallet;
import com.contract.enums.CoinEnums;
import com.pmzhongguo.ex.business.dto.AccountDto;
import com.pmzhongguo.ex.business.entity.CoinWithdraw;
import com.pmzhongguo.ex.business.mapper.ContractWalletMapper;
import com.pmzhongguo.ex.business.resp.TickerResp;
import com.pmzhongguo.ex.business.scheduler.UsdtCnyPriceScheduler;
import com.pmzhongguo.ex.business.service.MarketService;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.utils.DaoUtil;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daily
 * @date 2020/7/1 23:39
 */

@Component
public class AccountValidate {

    private Logger logger = LoggerFactory.getLogger(AccountValidate.class);

    //5 USDT
    private static final BigDecimal   QUOTA_CNY = new BigDecimal("5").multiply(UsdtCnyPriceScheduler.PRICE);

    @Resource
    protected MemberService memberService;

    @Autowired
    private ContractWalletMapper cWalletMapper;

    @Autowired
    public DaoUtil daoUtil;

    /**
     * 是否是模拟账号
     * @param memberId
     * @return true是  false 不是
     */
    public boolean isVirtualAccount(int memberId){
        boolean result = false;
        Map<String, Object> map =  daoUtil.queryForMap("SELECT t.`identity` FROM `c_customer` t WHERE t.`id` = ?", memberId);
        if(map != null && map.get("identity") != null){
            result = "3".equals(String.valueOf(map.get("identity")));
            return result;
        }
        return result;
    }


    /**
     * 判断是否能提现
     * @param memberId
     * @param currency
     * @param num
     * @return true 能  false 不能
     */
    public boolean isTransfer(Integer memberId, String currency, BigDecimal num){
        //获取用户人民币总资产
        BigDecimal total = getTotalAccount(memberId);
        //扣除已生成提币记录但未处理的记录
        BigDecimal delayWithdraw = getWithdraw(memberId);
        BigDecimal balance = total.subtract(delayWithdraw);
        //提币人民币资产
        BigDecimal withdraw = withdrawCNY(currency, num);
        //计算提币后的余额
        balance = balance.subtract(withdraw);
        //比较提币后人民币资产余额是否大于要求预留的余额
        boolean result = balance.compareTo(QUOTA_CNY) >= 0;
        if(!result){
            logger.warn("==>memberId:" + memberId + " currency:" + currency
                    + " num:" + num + " total:" + total
                    + " delayWithdraw:" + delayWithdraw
            + " withdraw:" + withdraw);
        }
        return result;
    }

    /**
     * 获取用户总资产
     * @param memberId
     * @return
     */
    private BigDecimal getTotalAccount(Integer memberId){
        BigDecimal total = BigDecimal.ZERO;
        //计算币币人民币总资产
        List<AccountDto> accountDtoList = memberService.getAccounts(memberId);
        for (AccountDto a : accountDtoList){
            total = total.add(a.getZc_balance());
        }
        //计算全仓逐仓资产
        CWallet wallet = cWalletMapper.selectByPrimaryKey(memberId, CoinEnums.USDT.name());
        BigDecimal balance = wallet.getBalance().multiply(UsdtCnyPriceScheduler.PRICE);
        total = total.add(balance);
        BigDecimal zcBalance = wallet.getZcbalance().multiply(UsdtCnyPriceScheduler.PRICE);
        total = total.add(zcBalance);
        return total;
    }

    /**
     * 计算某币种一定数量的人民币价值
     * @param currency  币种名称
     * @param num   数量
     * @return
     */
    private BigDecimal withdrawCNY(String currency, BigDecimal num){
        //USDT 直接计算返回
        if (currency.equalsIgnoreCase(HelpUtils.OFFICIAL_CURRENCY)) {
            return num.multiply(UsdtCnyPriceScheduler.PRICE);
        }
        //其他的币种需要转成USDT再计算
        Map<String, TickerResp> ticker = MarketService.getTicker();
        AccountDto accountDto = new AccountDto();
        accountDto.setCurrency(currency);
        //获取币种当前USDT市价
        BigDecimal usdtClose = memberService.getCurrentPairCloseForZC(accountDto, ticker);
        //将USDT市价转换成CNY市价
        BigDecimal cnyClose = usdtClose.multiply(UsdtCnyPriceScheduler.PRICE).setScale(8, BigDecimal.ROUND_HALF_UP);
        //算出cny价值
        BigDecimal balance = num.multiply(cnyClose).setScale(8, BigDecimal.ROUND_HALF_UP);
        return balance;
    }

    //获取已提币尚未划转的待提币资产
    private BigDecimal getWithdraw(Integer memberId){
        BigDecimal total = BigDecimal.ZERO;
        try {
//            HashMap<String, Object> param = new HashMap<>();
//            param.put("member_id", memberId);
//            param.put("w_status", 0);
//            List<CoinWithdraw> CoinWithdrawList = memberService.getAllCoinWithdraw(param);
            List<Map> mapList = daoUtil.queryForList("select w.currency, w.w_amount from m_coin_withdraw w LEFT JOIN m_member m ON m.`id` = w.`member_id` where w.w_status = 0 and w.member_id = ?", memberId);
            for(Map m : mapList){
                total = total.add(withdrawCNY(m.get("currency").toString(), new BigDecimal(m.get("w_amount").toString())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
}
