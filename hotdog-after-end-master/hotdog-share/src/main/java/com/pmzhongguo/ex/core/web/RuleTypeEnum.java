package com.pmzhongguo.ex.core.web;

/**
 * @author jary
 * @creatTime 2019/7/20 11:05 AM
 * 锁仓单位时间类型
 */
public enum RuleTypeEnum {
    /**
     * 分钟
     */
    MIN(0,"MIN","分钟"),
    /**
     * 小时,按小时整点释放
     */
    HOUR(0,"HOUR","小时"),
    /**
     * 天，按每日下午3点释放,3点前点n释放，3点后点n+1释放；
     */
    DAY(0,"DAY","天"),
    /**
     * 月，按每月1号下午3点释放
     */
    MONTH(0,"MONTH","月")
    ;

    private Integer type;

    private String codeEn;

    private String codeCn;

    RuleTypeEnum(Integer type, String codeEn, String codeCn) {
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

    public static RuleTypeEnum getEnumByType(int type) {
        for (RuleTypeEnum t : RuleTypeEnum.values()) {
            if (t.getType() == type) {
                return t;
            }
        }
        return null;
    }

    public static RuleTypeEnum getEnumByCode(String code) {
        for (RuleTypeEnum t : RuleTypeEnum.values()) {
            if (t.codeEn.equals(code)) {
                return t;
            }
        }
        return null;
    }
}
