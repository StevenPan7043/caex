package com.pmzhongguo.ex.core.web;

/**
 * @description: 充值类型枚举
 * @date: 2019-04-01 15:01
 * @author: 十一
 */
public enum  RechargeTypeEnum {

    /**
     * 充值类型枚举
     */
    INVITE_REWARD("INVITE_REWARD","推荐奖励"),
    MAN_RECHARGE("MAN_RECHARGE","平台人工充值"),
    COMMUNITY_RECHARGE("COMMUNITY_RECHARGE","社区划转-充值"),
    REG_REWARD("REG_REWARD","注册奖励"),
    SYS_REWARD("SYS_REWARD","系统奖励"),
    TRADE_RANKING_REWARD("TRADE_RANKING_REWARD","交易排名奖励"),
    TRADE_RETURN_FEE_REWARD("TRADE_RETURN_FEE_REWARD","交易返还手续费"),
    WXPAY("wxpay","微信支付"),
    ALIPAY("alipay","支付宝"),
    BANK("bank","银行卡"),
    RETURN_COMMISSION("RETURN_COMMISSION","返佣"),
    TRANSFER("TRANSFER","通道划转"),
    DATALAB("DATALAB","数据实验室"),
    INTERNAL_TRANSFER("INTERNAL_TRANSFER","内部划转"),
    CHAIN("chain","链充值"),


    ;


    private String type;
    private String msg;

    RechargeTypeEnum(String type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }



}
