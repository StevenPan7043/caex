package com.pmzhongguo.crowd.config.rediskey;

/**
 * @description: 项目币中的库存key
 * @date: 2019-03-04 09:37
 * @author: 十一
 */
public class CrowdProjectKey extends BaseKey {

    /**
     * 单位秒
     */
    public static final int expire = 3600 * 2;

    public CrowdProjectKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static CrowdProjectKey crowdProjectKey = new CrowdProjectKey(expire,"crdprok");
}
