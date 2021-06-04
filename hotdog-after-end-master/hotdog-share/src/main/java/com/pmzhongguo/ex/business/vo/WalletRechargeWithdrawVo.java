package com.pmzhongguo.ex.business.vo;

import lombok.*;

import java.math.BigDecimal;

/**
 * @description: 钱包充值提现
 * @date: 2019-12-31 21:46
 * @author: 十一
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletRechargeWithdrawVo {

    /**
     * 用户id
     */
    private Integer memberId;


    /**
     * 消息内容、参数
     */
    private String currency;

    /**
     * 时间
     */
    private String time;

    /**
     * 数量
     */
    private BigDecimal amount;

    /**
     * 消息模板类型
     */
    private int msgType;

//    public WalletRechargeWithdrawVo() {
//    }
//
//    public WalletRechargeWithdrawVo(Integer memberId, String currency, String time, BigDecimal amount, int msgType) {
//        this.memberId = memberId;
//        this.currency = currency;
//        this.time = time;
//        this.amount = amount;
//        this.msgType = msgType;
//    }
}
