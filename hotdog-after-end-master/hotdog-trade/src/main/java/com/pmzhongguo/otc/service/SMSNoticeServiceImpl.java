package com.pmzhongguo.otc.service;

import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.otc.entity.dto.OTCOrderDTO;
import com.pmzhongguo.otc.entity.dto.OTCTradeDTO;
import com.pmzhongguo.otc.otcenum.MobiInfoTemplateEnum;
import com.pmzhongguo.otc.otcenum.TemplateEnum;
import com.pmzhongguo.otc.sms.JuheSend;
import com.pmzhongguo.otc.sms.SmsSendPool;

/**
 * @description: 短信发送
 * @date: 2019-04-09 15:45
 * @author: 十一
 */
public class SMSNoticeServiceImpl implements NoticeService{


    /**
     *
     * @param templateEnum 短信模版类型
     * @param m1     m1获取昵称
     * @param selfDTO   我方交易dto
     * @param opposite 反方交易dot
     * @param m2    m2获取发送信息账号
     */
    @Override
    public void send(TemplateEnum templateEnum, Member m1, OTCTradeDTO selfDTO, OTCOrderDTO opposite, Member m2) {
        MobiInfoTemplateEnum mobiInfoTemplateEnum = (MobiInfoTemplateEnum)templateEnum;

        String template = String.format(mobiInfoTemplateEnum.getCode(), m1.getM_nick_name(),
                selfDTO.getVolume().stripTrailingZeros().toPlainString(), selfDTO.getBaseCurrency(),
                selfDTO.getPrice().stripTrailingZeros().toPlainString(), selfDTO.getQuoteCurrency(),
                selfDTO.gettNumber(), opposite.getPaymentTime());
        System.out.println("--------------------------- 短信发送内容：" + template);
        SmsSendPool.getInstance().send(new JuheSend(m2.getM_name(), mobiInfoTemplateEnum.getType(), template));
    }
}
