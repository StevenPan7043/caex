package com.pmzhongguo.notice.service;

import cn.hutool.cache.impl.TimedCache;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.notice.dto.NoticeDto;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 钉钉发送工具类
 * @date: 2020-02-20 15:36
 * @author: 十一
 */
@Slf4j
public class DingDingNoticeUtil  {

    /**
     * 过期缓存，5分钟
     */
    static TimedCache<String, String> NOTICED_CACHE_5_MIN
            = new TimedCache<String, String>(1000 * 60 * 5);


    /**
     * 不使用缓存
     * @param noticeDto
     * @return
     */
    public static Resp send(NoticeDto noticeDto) {
        return DingDingService.send(noticeDto.getContent());
    }

    /**
     * 有缓存
     * @param noticeDto
     * @return
     */
    public static Resp sendCache(NoticeDto noticeDto) {
        if (NOTICED_CACHE_5_MIN.containsKey(noticeDto.getCacheKey())) {
            String lastContent = NOTICED_CACHE_5_MIN.get(noticeDto.getCacheKey(), false);
            log.info("已发送过通知, cacheKey: {},content: {},scene: {}"
                    , noticeDto.getCacheKey(),noticeDto.getContent()
                    ,noticeDto.getSubject());
            log.info("上一次发送过通知内容,content: {}",lastContent);
            return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
        }

        DingDingService.send(noticeDto.getContent());
        NOTICED_CACHE_5_MIN.put(noticeDto.getCacheKey(),noticeDto.getContent());
        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

}
