package com.pmzhongguo.otc.service;

import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.otc.entity.dto.OTCOrderDTO;
import com.pmzhongguo.otc.entity.dto.OTCTradeDTO;
import com.pmzhongguo.otc.otcenum.TemplateEnum;

/**
 * @description: 短信/邮件交易通知
 * @date: 2019-04-09 15:42
 * @author: 十一
 */
public interface NoticeService {

    /**
     *
     * @param templateEnum 短信模版类型
     * @param m1     m1获取昵称
     * @param selfDTO   我方交易dto
     * @param opposite 反方交易dot
     * @param m2    m2获取发送信息账号
     */
    void send(TemplateEnum templateEnum, Member m1, OTCTradeDTO selfDTO, OTCOrderDTO opposite, Member m2);


}
