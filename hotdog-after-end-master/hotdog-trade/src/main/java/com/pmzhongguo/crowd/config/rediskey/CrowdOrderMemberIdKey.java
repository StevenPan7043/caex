package com.pmzhongguo.crowd.config.rediskey;



/**
 * @description: 众筹项目订单，模拟生成用户id 的key
 * @date: 2019-03-19 15:55
 * @author: 十一
 */
public class CrowdOrderMemberIdKey extends BaseKey {
    public static final int expire = 3600 * 4;

    public CrowdOrderMemberIdKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static CrowdOrderMemberIdKey crowdOrderMemberIdKey = new CrowdOrderMemberIdKey(expire,"crowdordermemberidkey");


    public String getMemberIdKey(String job_group, Integer project_id) {
        return getPrefix() + ":" + job_group + ":" + project_id ;
    }
}
