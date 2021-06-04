package com.pmzhongguo.ex.business.scheduler;

import com.google.common.collect.ImmutableMap;
import com.pmzhongguo.ex.business.dto.CurrencyLockReleaseDto;
import com.pmzhongguo.ex.business.entity.LockRelease;
import com.pmzhongguo.ex.business.service.CurrencyLockReleaseService;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.rediskey.CurrencyLockReleaseKey;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.IPAddressPortUtil;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.web.AccountOperTypeEnum;
import com.pmzhongguo.ex.core.web.CommonEnum;
import com.pmzhongguo.ex.core.web.OptSourceEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;


import java.util.Date;
import java.util.List;

/**
 * @description: 购买释放功能币定时任务
 * @date: 2019-06-12 11:49
 * @author: 十一
 */
@Component
public class CurrencyLockReleaseScheduler {

    private static Logger logger = LoggerFactory.getLogger(CurrencyLockReleaseScheduler.class);

    @Autowired
    private CurrencyLockReleaseService currencyLockReleaseService;


    @Autowired
    private MemberService memberService;

    /**
     * 每15分钟执行一次
     */
    @Scheduled(cron = "0 */15 * * * ?")
    public void lockRelease() {
        String lockKey = CurrencyLockReleaseKey.currencyLockReleaseKey.getKey();
        int lockKeyExpireTime = CurrencyLockReleaseKey.currencyLockReleaseKey.getExpireSeconds();
        // 任务15min执行一次，锁的有效时间设为8m
        boolean isLock = JedisUtil.getInstance().getLock(lockKey, IPAddressPortUtil.IP_ADDRESS,lockKeyExpireTime);
        if (isLock) {
            try {
                List<CurrencyLockReleaseDto> lockReleaseList = currencyLockReleaseService
                        .findByIsReleaseAndReleaseTimeGtNow(ImmutableMap.of("is_release",CommonEnum.NO.getCode(),
                                                            "release_time", HelpUtils.formatDate8(new Date())));
                if (CollectionUtils.isEmpty(lockReleaseList)) {
                    return;
                }
                for (CurrencyLockReleaseDto lockReleaseDto : lockReleaseList) {
                    LockRelease lockRelease = new LockRelease();
                    lockRelease.setIs_release(CommonEnum.YES.getCode());
                    lockRelease.setId(lockReleaseDto.getId());
                    lockRelease.setUpdate_time(HelpUtils.formatDate8(new Date()));
                    // 更新状态和时间,通过id和状态来更新
                    int result = currencyLockReleaseService.updateByIdAndIsRelease(lockRelease);
                    if (result > 0) {
                        // 币币账号添加资产
                        memberService.accountProc(lockReleaseDto.getReleaseVolume(),lockReleaseDto.getCurrency()
                                ,lockReleaseDto.getMemberId(), AccountOperTypeEnum.ADD_BALANCE.getCode(), OptSourceEnum.CURRENCYLOCK);
                    }else {
                        logger.warn("<=== 【{}】购买释放更新用户资产失败：【{}】",lockReleaseDto.getCurrency(),lockReleaseDto.toString());
                    }

                }
            } finally {
                JedisUtil.getInstance().releaseLock(lockKey, IPAddressPortUtil.IP_ADDRESS);
            }
        } else {
            logger.info("<============= 购买释放锁仓币获取锁失败 =============>");
        }

    }


    public static void main(String[] args) {
        System.out.println(HelpUtils.formatDate8(new Date()));
    }

}
