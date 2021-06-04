package com.pmzhongguo.ex.transfer.service;

import com.pmzhongguo.ex.business.entity.Account;
import com.pmzhongguo.ex.business.entity.CoinRecharge;
import com.pmzhongguo.ex.business.entity.CoinWithdraw;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.MacMD5;
import com.pmzhongguo.ex.core.web.OptSourceEnum;
import com.pmzhongguo.ex.core.web.RechargeTypeEnum;
import com.pmzhongguo.ex.transfer.entity.CTransferReqVo;
import com.pmzhongguo.otc.entity.dto.AccountDTO;
import com.qiniu.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/6/22 5:09 PM
 */
@Service
@Transactional
public class AccountManager {

    @Autowired
    private MemberService memberService;

    public AccountDTO getAccount(Map accountParam) {
        AccountDTO accountDTO = new AccountDTO();
        Account account = memberService.getAccount(accountParam);
        BeanUtil.copy(account, accountDTO);
        return accountDTO;
    }


    /**
     * 交易所转出
     * @param cTransferReqVo
     * @param member
     * @param account
     */
    public void transfer(CTransferReqVo cTransferReqVo, Member member, Account account) {
        memberService.accountProc(cTransferReqVo.getNum().negate(), cTransferReqVo.getCurrency(), member.getId()
                , 3, OptSourceEnum.ZZEX);
        CoinWithdraw coinWithdraw = new CoinWithdraw();
        coinWithdraw.setMember_id(member.getId());
        coinWithdraw.setCurrency(account.getCurrency());
        coinWithdraw.setCurrency_id(HelpUtils.getCurrencyMap().get(account.getCurrency()).getId());
        coinWithdraw.setW_amount(cTransferReqVo.getNum());
        coinWithdraw.setW_fee(BigDecimal.ZERO);
        coinWithdraw.setW_create_time(HelpUtils.formatDate8(new Date()));
        coinWithdraw.setMember_coin_addr("→bank▲0000▲0000▲0000");
        coinWithdraw.setMember_coin_addr_id(0);
        coinWithdraw.setMember_coin_addr_label("-");
        coinWithdraw.setOper_ip(cTransferReqVo.getIp());
        coinWithdraw.setPlatform_coin_memo(cTransferReqVo.getToaddress());
        coinWithdraw.setW_status(1);
        coinWithdraw.setOtc_ads_id(0);
        coinWithdraw.setOtc_price(BigDecimal.ZERO);
        coinWithdraw.setOtc_volume(BigDecimal.ZERO);
        coinWithdraw.setAuditor(member.getM_name());
        coinWithdraw.setAudit_time(HelpUtils.formatDate8(new Date()));
        memberService.addCoinWithdraw(coinWithdraw);
    }

    /**
     * 交易所转入
     */
    public void rollOut(CTransferReqVo cTransferReqVo, Member member, Account account) {
        memberService.accountProc(cTransferReqVo.getNum(), cTransferReqVo.getCurrency(), member.getId()
                , 3, OptSourceEnum.ZZEX);
        CoinRecharge coinRecharge = new CoinRecharge();
        coinRecharge.setMember_id(member.getId());
        coinRecharge.setCurrency(cTransferReqVo.getCurrency());
        coinRecharge.setCurrency_id(HelpUtils.getCurrencyMap().get(cTransferReqVo.getCurrency()).getId());
        coinRecharge.setR_amount(cTransferReqVo.getNum());
        coinRecharge.setR_create_time(HelpUtils.formatDate8(new Date()));
        coinRecharge.setR_address(RechargeTypeEnum.COMMUNITY_RECHARGE.getType());
        coinRecharge.setR_txid(MacMD5.CalcMD5(member.getId() +""+ new Date().getTime()));
        coinRecharge.setR_status(1);
        coinRecharge.setR_gas(1);
        coinRecharge.setR_confirmations("1");
        coinRecharge.setR_guiji(1);
        coinRecharge.setOtc_ads_id(0);
        coinRecharge.setOtc_price(BigDecimal.ZERO);
        coinRecharge.setOtc_money(BigDecimal.ZERO);
        coinRecharge.setAuditor(member.getM_name());
        coinRecharge.setAudit_time(HelpUtils.formatDate8(new Date()));
        coinRecharge.setR_source(RechargeTypeEnum.COMMUNITY_RECHARGE.getType());
        memberService.addCoinRechargeByMember(coinRecharge);
    }
}
