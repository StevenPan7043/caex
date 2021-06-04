package com.pmzhongguo.crowd.config.rediskey;



/**
 * @description: 众筹项目订单，进入定时任务次数统计
 * @date: 2019-03-19 15:55
 * @author: 十一
 */
public class CrowdOrderSchedulerCountKey extends BaseKey {
    public static final int expire = 3600 * 4;

    public CrowdOrderSchedulerCountKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public  String OrderLimit(String jobGroup,int projectId,int orderLimit) {

        return getPrefix() + ":" + jobGroup + ":" + projectId + ":" + orderLimit;
    }

    public static CrowdOrderSchedulerCountKey crowdOrderSchedulerCountKey = new CrowdOrderSchedulerCountKey(expire,"crowdorderschedulercountkey");
}
