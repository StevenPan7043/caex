package com.pmzhongguo.ex.business.scheduler;

import com.pmzhongguo.ex.core.utils.DaoUtil;
import com.pmzhongguo.zzextool.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @description: 用来检测用户冻结，移除session
 * @date:        2019-09-23
 * @author:      十一
 */
@Component
public class FrozenMemberScheduler {

    private static Logger logger = LoggerFactory.getLogger(FrozenMemberScheduler.class);

    @Autowired
    private DaoUtil daoUtil;

    public static ConcurrentHashMap<Integer,Integer> memberIdMap = new ConcurrentHashMap<Integer,Integer>();



    /**
     * 一分钟一次
     * 定时刷新获取冻结的用户id
     */
    @Scheduled(cron = " * */1 * * * ?")
    public void quotationResult() {
        // 查找冻结用户
        String sql = "select id from m_member where m_status = 2";
        List<Map<String,Integer>> list = daoUtil.queryForList(sql);
        // 清空集合
        memberIdMap.clear();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        try {
            // 重新添加
            for (Map<String,Integer> map : list) {
                if (!StringUtil.isNullOrBank(map.get("id"))) {
                    memberIdMap.put(map.get("id"),map.get("id"));
                }

            }
        }catch (Exception e) {
            logger.error("<=== 定时刷新获取冻结的用户异常：【{}】",e.fillInStackTrace());
        }
    }

}
