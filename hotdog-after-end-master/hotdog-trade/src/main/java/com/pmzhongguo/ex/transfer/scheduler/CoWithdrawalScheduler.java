package com.pmzhongguo.ex.transfer.scheduler;

import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.transfer.service.ThirdPartyService;
import com.qiniu.util.DateStyleEnum;
import com.qiniu.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author jary
 * @creatTime 2019/10/8 3:00 PM
 */
@Component
public class CoWithdrawalScheduler {

    @Autowired
    private ThirdPartyService thirdPartyService;
    /**
     * 项目方通道提现，若有项目方账户，则通过此定时任务进行提现币种归集
     * 定时任务每日凌晨2点执行币种归集
     */
//    @Scheduled(cron = "0 0 2 * * ?")
//    @Scheduled(cron = "* */1 * * * ?")
//    public void executeTask() {
//        long yesterday = HelpUtils.getAfterSomeDayLongTime(-1);
//        String yyyyMMdd = HelpUtils.TimeStamp2StringDate(yesterday, DateStyleEnum.YYYY_MM_DD.getValue());
//        String timeStart = yyyyMMdd+" 00:00:00";
//        String timeEnd = yyyyMMdd+" 23:59:59";
//        thirdPartyService.execute(timeStart,timeEnd,null);
//    }
}
