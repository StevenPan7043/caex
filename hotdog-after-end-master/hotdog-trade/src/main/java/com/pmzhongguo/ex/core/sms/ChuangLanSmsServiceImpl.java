package com.pmzhongguo.ex.core.sms;

import com.google.common.base.Joiner;
import com.pmzhongguo.crowd.config.support.BeanContext;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.otc.sms.SmsSendPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

/**
 * @description: 创蓝短信实现
 * @date: 2019-05-30 14:28
 * @author: 十一
 */
public class ChuangLanSmsServiceImpl extends AbstractChuangLanSmsService {

    private static Logger logger = LoggerFactory.getLogger(AbstractChuangLanSmsService.class);

    @Override
    public void juHeSend() {

    }

    /**
     *
     * @param args 构建参数数组，[0]=短信接收方号码,[1]=短信动态变量组逗号分隔,[2]=短信发送模版
     * @return
     */
    @Override
    public ISmsService builder(Object... args) {
        if (args == null) {
            return this;
        }
        try {
            String mobile = args[0].toString();
            // otc中可能是邮箱，要从数据库中查找手机号码
            if (mobile.contains("@")) {
                MemberService memberService = (MemberService) BeanContext.getBeanByType(MemberService.class);
                Member member = memberService.getMemberBy(HelpUtils.newHashMap("m_name", mobile));
                mobile = member.getPhone();
            }
            this.params = mobile + "," + args[1];
            this.template  = args[2].toString();
            this.sendTime = HelpUtils.formatDateOrdreNo(new Date());
            this.receiver = mobile;
        }catch (Exception e) {
            logger.error("<=============== 构建短信异常,参数：{}, \t异常信息：{}", Arrays.toString(args),e);
            return this;
        }
        return this;
    }



    public static void main(String[] args) {
        // 1. CLT_CODE_TIME
//        String m_name = "13097368626";
//        String smsCode = HelpUtils.randomNum(6);
//        int expireTime = 5;
//        ISmsService smsService = new ChuangLanSmsServiceImpl()
//                .builder(m_name, Joiner.on(",").join(Arrays.asList(smsCode,expireTime)),AbstractChuangLanSmsService.CLT_CODE_TIME);
//        SmsSendPool.getInstance().send(smsService);
        // 2. CLT_MEMBERAPPEAL_MEMBER_ORDER
//        String m_name = "13097368626";
//        String order = "223432";
//        ISmsService smsService = new ChuangLanSmsServiceImpl()
//                .builder(m_name, Joiner.on(",").join(Arrays.asList(m_name,order)),AbstractChuangLanSmsService.CLT_MEMBERAPPEAL_MEMBER_ORDER);
//        SmsSendPool.getInstance().send(smsService);
        // 3. CLT_CODE
//        String m_name = "13097368626";
//        String smsCode = HelpUtils.randomNum(6);
//        ISmsService smsService = new ChuangLanSmsServiceImpl()
//                .builder(m_name, smsCode,AbstractChuangLanSmsService.CLT_CODE);
//        SmsSendPool.getInstance().send(smsService);
        // 4. CLT_ZZEXAPPEAL_MEMBER_ORDER_
//        String m_name = "13097368626";
//        String order = "223432";
//        ISmsService smsService = new ChuangLanSmsServiceImpl()
//                .builder(m_name, Joiner.on(",").join(Arrays.asList(m_name,order)),AbstractChuangLanSmsService.CLT_ZZEXAPPEAL_MEMBER_ORDER_);
//        SmsSendPool.getInstance().send(smsService);
//        // 5. CLT_CANCEL_MEMBER_ORDER
//        String m_name = "13097368626";
//        String order = "223432";
//        ISmsService smsService = new ChuangLanSmsServiceImpl()
//                .builder(m_name, Joiner.on(",").join(Arrays.asList(m_name,order)),AbstractChuangLanSmsService.CLT_CANCEL_MEMBER_ORDER);
//        SmsSendPool.getInstance().send(smsService);
//        // 6. CLT_MEMBER_CURRENCY
//        String m_name = "13097368626";
//        String currency = "ETH";
//        ISmsService smsService = new ChuangLanSmsServiceImpl()
//                .builder(m_name, Joiner.on(",").join(Arrays.asList(m_name,currency)),AbstractChuangLanSmsService.CLT_MEMBER_CURRENCY);
//        SmsSendPool.getInstance().send(smsService);
        // 7. CLT_PAY_MEMBER_ORDER
//        String m_name = "13097368626";
//        String order = "223432";
//        ISmsService smsService = new ChuangLanSmsServiceImpl()
//                .builder(m_name, Joiner.on(",").join(Arrays.asList(m_name,order)),AbstractChuangLanSmsService.CLT_PAY_MEMBER_ORDER);
//        SmsSendPool.getInstance().send(smsService);
        // 8. CLT_BUY_MEMBE_NUM_CURRENCY_PRICE_NUM_ORDER_TIME
        // 13097368626, 110,USDT,7,CNY,T190610205918HTX,30,
        // 13097368626, 13097368626,23.88,ETH,1788.2,23.88,223432,10
        String m_name = "13097368626";
        String nick_name = "赵德柱";
        BigDecimal num = new BigDecimal("23.88");
        BigDecimal num2 = new BigDecimal("23.88");
        BigDecimal price = new BigDecimal("1788.2");
        String currency = "ETH";
        String quoteCurrency = "USDT";
        String order = "B22OT34SX320KS7";
        int expireTime = 10;

        String buySmsParams = Joiner.on(",").join(Arrays.asList(nick_name
                , num , currency
                , price, quoteCurrency
                , order, expireTime));
//        ISmsService buySmsService = new ChuangLanSmsServiceImpl().builder(m_name,buySmsParams
//                , AbstractChuangLanSmsService.CLT_BUY_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER_TIME);
//        SmsSendPool.getInstance().send(buySmsService);
        // 9. CLT_SELL_MEMBE_NUM_CURRENCY_PRICE_NUM_ORDER_TIME
        // 【ZZEX】您已向{$var}用户出售{$var} {$var}，出售单价为{$var} {$var}，订单号为{$var}，等待对方支付。
//        String m_name = "13097368626";
//        String nick_name = "赵德柱";
//        BigDecimal num = new BigDecimal("23.88");
//        BigDecimal num2 = new BigDecimal("23.88");
//        String currency = "ETH";
//        String quoteCurrency = "ETH";
//        String order = "B22OT34SX320KS7";
//        int payTime = 10;
//
//        String sellSmsParams = Joiner.on(",").join(Arrays.asList(nick_name
//                , num , currency
//                , num2, quoteCurrency
//                , order,payTime));
//        ISmsService sellSmsService = new ChuangLanSmsServiceImpl().builder(m_name,sellSmsParams
//                , AbstractChuangLanSmsService.CLT_SELL_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER);
//        SmsSendPool.getInstance().send(sellSmsService);
    }
}
