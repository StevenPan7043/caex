package com.pmzhongguo.zzextool.consts;

/**
 * 计划任务频道加在这里
 * 若zzex和计划任务都需监听，则两边都加，且保持频道一致。
 * @author ：zn
 * @date ：Created in 2019/9/26
 */
public class JedisChannelConst {

    //配置同步频道

    public static final String JEDIS_CHANNEL_SYNC_CONFIG = "JEDIS_CHANNEL_SYNC_CONFIG";

    //交易对同步频道

    public static final String JEDIS_CHANNEL_SYNC_CURRENCY_PAIR = "JEDIS_CHANNEL_SYNC_CURRENCY_PAIR";

    //币种同步频道

    public static final String JEDIS_CHANNEL_CURRENCY = "JEDIS_CHANNEL_SYNC_CURRENCY";

    //新闻同步频道

    public static final String JEDIS_CHANNEL_SYNC_NEWS = "JEDIS_CHANNEL_SYNC_NEWS";

    //币种规则同步频道

    public static final String JEDIS_CHANNEL_SYNC_CURRENCY_RULE = "JEDIS_CHANNEL_SYNC_CURRENCY_RULE";

    //APP同步频道

    public static final String JEDIS_CHANNEL_SYNC_APP = "JEDIS_CHANNEL_SYNC_APP";

    /**
     * 国际语言同步频道
     */
    public static final String JEDIS_CHANNEL_COUNTRY = "JEDIS_CHANNEL_COUNTRY";

    //同步消息

    public static final String SYNC_MESSAGE = "SYNC_MESSAGE";

    /**
     * 机器人列表同步频道
     */
    public static final String ROBOT_CHANNEL_SYNC = "ROBOT_CHANNEL_SYNC";

}
