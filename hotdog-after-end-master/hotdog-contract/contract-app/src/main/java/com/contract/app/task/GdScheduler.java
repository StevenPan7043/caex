package com.contract.app.task;

import com.contract.common.DateUtil;
import com.contract.service.api.GdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GdScheduler {
    private static final Logger logger = LoggerFactory.getLogger(GdScheduler.class);


    @Autowired
    private GdService gdService;

    /**
     * 跟单收益
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void grantUserBonus() {
        Date date = DateUtil.dateAddInt(new Date(), -1);
        String time = DateUtil.toDateString(date);
        gdService.personReward(time);
        gdService.teamReward();
    }

}
