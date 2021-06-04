package com.pmzhongguo.crowd.config.rediskey;


/**
 * @description: 一定要写注释啊
 * @date: 2019-03-04 09:37
 * @author: 十一
 */
public class CrowdOrderKey extends BaseKey {

    public static final int expire = 3600 * 4;

    public CrowdOrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static CrowdOrderKey crowdOrderKey = new CrowdOrderKey(expire,"crdodk");
}
