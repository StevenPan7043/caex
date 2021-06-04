package com.pmzhongguo.ex.core.sms;

import com.google.common.base.Joiner;
import com.pmzhongguo.crowd.config.support.BeanContext;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.SmsUtil;
import com.pmzhongguo.ex.core.web.InternationalSmsCNEnum;
import com.pmzhongguo.otc.sms.SmsSendPool;
import com.qiniu.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

/**
 * @description: 国际短信实现
 * @date: 2019-07-02 14:28
 * @author: Jary
 */
public class InterSmsServiceImpl extends AbstractInternationalSmsService {

    private static Logger logger = LoggerFactory.getLogger(AbstractInternationalSmsService.class);


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
        Member member = new Member();
        try {
            String mobile = args[args.length-2].toString();
            // otc中可能是邮箱，要从数据库中查找手机号码
            if (mobile.contains("@")) {
                MemberService memberService = (MemberService) BeanContext.getBeanByType(MemberService.class);
                member = memberService.getMemberBy(HelpUtils.newHashMap("m_name", mobile));
                mobile = member.getPhone();
            }
//            this.params = mobile + "," + args[1];
            if (BeanUtil.isEmpty(member.getArea_code())){
                member.setArea_code(Integer.valueOf(args[args.length-3].toString()));
            }
            String areaCode = BeanUtil.isEmpty(member.getArea_code()) ? args[args.length-3].toString():member.getArea_code().toString();
            this.template = SmsUtil.getSms(areaCode, args);
            this.sendTime = HelpUtils.formatDateOrdreNo(new Date());
            this.receiver = mobile;
            this.areaCode = Integer.valueOf(areaCode);
        } catch (Exception e) {
            logger.error("<=============== 构建短信异常,参数：{}, \t异常信息：{}", Arrays.toString(args), e);
            return this;
        }
        return this;
    }



    public static void main(String[] args) {

        String m_name = "68576133";
        String nick_name = "张津瑞";
        BigDecimal num = new BigDecimal("23.88");
        BigDecimal num2 = new BigDecimal("23.88");
        BigDecimal price = new BigDecimal("1788.2");
        String currency = "ETH";
        String quoteCurrency = "USDT";
        String order = "B22OT34SX320KS7";
        int expireTime = 10;

//        ISmsService buySmsService = new InterSmsServiceImpl().builder(nick_name,num,currency,price,quoteCurrency,order,expireTime,m_name
//                , InternationalSmsCNEnum.CLT_BUY_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER_TIME.getType());
//        SmsSendPool.getInstance().send(buySmsService);
    }
}
