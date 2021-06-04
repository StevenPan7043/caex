package com.pmzhongguo.crowd.vo;

import java.math.BigDecimal;

/**
 * @description: 一定要写注释啊
 * @date: 2019-03-03 14:27
 * @author: 十一
 */
public class CrowdOrderVo {

    /**
     * 接收地址
     */
    private String address;

    /**
     * 项目币种id
     */
    private Integer project_id;

    /**
     * 购买数量
     */
    private BigDecimal buy_amount;

    /**
     * 资金密码
     */
    private String fund_pwd;

    /**
     * 时间戳
     */
    private Integer ts;

    /**
     * 加密签名
     */
    private String key;

    private String operIp;

    /**
     * 订单来源
     */
    private Integer source;

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getTs() {
        return ts;
    }

    public void setTs(Integer ts) {
        this.ts = ts;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getProject_id() {
        return project_id;
    }

    public void setProject_id(Integer project_id) {
        this.project_id = project_id;
    }

    public BigDecimal getBuy_amount() {
        return buy_amount;
    }

    public void setBuy_amount(BigDecimal buy_amount) {
        this.buy_amount = buy_amount;
    }

    public String getFund_pwd() {
        return fund_pwd;
    }

    public void setFund_pwd(String fund_pwd) {
        this.fund_pwd = fund_pwd;
    }

    public String getOperIp() {
        return operIp;
    }

    public void setOperIp(String operIp) {
        this.operIp = operIp;
    }
}
