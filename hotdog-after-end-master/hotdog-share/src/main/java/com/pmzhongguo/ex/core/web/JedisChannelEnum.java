package com.pmzhongguo.ex.core.web;

/**
 * zzex项目频道加在这里。
 * 若zzex和计划任务都需监听，则两边都加，且保持频道一致。
 * @author jary
 * @creatTime 2019/11/20 4:20 PM
 */
public enum JedisChannelEnum {
    /**
     * 配置同步频道
     */
    JEDIS_CHANNEL_SYNC_CONFIG(1001, "JEDIS_CHANNEL_SYNC_CONFIG", "配置同步频道"),
    /**
     * 交易对同步频道
     */
    JEDIS_CHANNEL_SYNC_CURRENCY_PAIR(1002, "JEDIS_CHANNEL_SYNC_CURRENCY_PAIR", "交易对同步频道"),
    /**
     * 币种同步频道
     */
    JEDIS_CHANNEL_CURRENCY(1003, "JEDIS_CHANNEL_SYNC_CURRENCY", "币种同步频道"),
    /**
     * 新闻同步频道
     */
    JEDIS_CHANNEL_SYNC_NEWS(1004, "JEDIS_CHANNEL_SYNC_NEWS", "新闻同步频道"),
    /**
     * 币种规则同步频道
     */
//    JEDIS_CHANNEL_SYNC_CURRENCY_RULE(1005, "JEDIS_CHANNEL_SYNC_CURRENCY_RULE", "币种规则同步频道"),
    /**
     * APP同步频道
     */
    JEDIS_CHANNEL_SYNC_APP(1006, "JEDIS_CHANNEL_SYNC_APP", "APP同步频道"),
    /**
     * 国际语言同步频道
     */
    JEDIS_CHANNEL_COUNTRY(1007, "JEDIS_CHANNEL_COUNTRY", "国际语言同步频道")
    ;

    private Integer type;

    private String codeEn;

    private String codeCn;

    JedisChannelEnum(Integer type, String codeEn, String codeCn) {
        this.type = type;
        this.codeEn = codeEn;
        this.codeCn = codeCn;
    }

    public Integer getType() {
        return type;
    }

    public String getCodeEn() {
        return codeEn;
    }

    public String getCodeCn() {
        return codeCn;
    }

    public static JedisChannelEnum getEnumByType(int type) {
        for (JedisChannelEnum t : JedisChannelEnum.values()) {
            if (t.getType() == type) {
                return t;
            }
        }
        return null;
    }

    public static JedisChannelEnum getEnumByCode(String code) {
        for (JedisChannelEnum t : JedisChannelEnum.values()) {
            if (t.codeEn.equals(code)) {
                return t;
            }
        }
        return null;
    }

    public static String[] getEnumCodeEn() {
        JedisChannelEnum[] jedisChannelEnums = JedisChannelEnum.values();
        String[] codeEns = new String[jedisChannelEnums.length];
        for (int i = 0; i < jedisChannelEnums.length; i++) {
            codeEns[i] = jedisChannelEnums[i].getCodeEn();
        }

        return codeEns;
    }
}
