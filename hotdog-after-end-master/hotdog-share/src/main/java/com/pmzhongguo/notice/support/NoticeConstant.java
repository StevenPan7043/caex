package com.pmzhongguo.notice.support;

/**
 * @description: 自定义消息文本
 * @date: 2020-02-19 10:18
 * @author: 十一
 */
public interface NoticeConstant {

    String WALLET_WARN_SUBJECT = "CAEX 钱包告警";
    String RECHARGE_NOTICE_SUBJECT = "CAEX 充币通知";
    String WITHDRAW_NOTICE_SUBJECT = "CAEX 提币通知";

    String BALANCE_WARN_MSG = WALLET_WARN_SUBJECT + "，币种：【%s】，地址：【%s】，余额不足，会影响正常业务使用，请查看！";

    String WITHDRAW_NOTICE_MSG = WITHDRAW_NOTICE_SUBJECT + "，币种：【%s】待提币，数量：【%s】";
    String RECHARGE_NOTICE_MSG = RECHARGE_NOTICE_SUBJECT + "，币种：【%s】待充币，数量：【%s】";

}
