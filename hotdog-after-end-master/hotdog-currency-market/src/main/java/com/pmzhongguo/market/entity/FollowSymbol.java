package com.pmzhongguo.market.entity;

import java.math.BigDecimal;

/**
 * 跟随交易对实体类
 *
 * @author jary
 * @creatTime 2020/5/15 11:56 AM
 */
public class FollowSymbol {

    /**
     * 跟随交易对
     */
    private String followSymbol;

    /**
     * 上一次跟随价格
     */
    private BigDecimal followPrePrice;

    /**
     * 目标交易对
     */
    private String targetSymbol;

    /**
     * 目标交易对行情上一次价格
     */
    private BigDecimal targetPrePrice;

    /**
     * 目标涨跌幅
     */
    private BigDecimal targetUpAndDown;

    /**
     * 启动价格
     */
    private BigDecimal followStartPrice;

    /**
     * 倍数
     */
    private BigDecimal mul;

    /**
     * 加密串
     */
    private String encryption;

    public String getFollowSymbol() {
        return followSymbol;
    }

    public void setFollowSymbol(String followSymbol) {
        this.followSymbol = followSymbol;
    }

    public String getTargetSymbol() {
        return targetSymbol;
    }

    public void setTargetSymbol(String targetSymbol) {
        this.targetSymbol = targetSymbol;
    }

    public BigDecimal getFollowStartPrice() {
        return followStartPrice;
    }

    public void setFollowStartPrice(BigDecimal followStartPrice) {
        this.followStartPrice = followStartPrice;
    }

    public BigDecimal getMul() {
        return mul;
    }

    public void setMul(BigDecimal mul) {
        this.mul = mul;
    }

    public BigDecimal getTargetPrePrice() {
        return targetPrePrice;
    }

    public void setTargetPrePrice(BigDecimal targetPrePrice) {
        this.targetPrePrice = targetPrePrice;
    }

    public BigDecimal getTargetUpAndDown() {
        return targetUpAndDown;
    }

    public void setTargetUpAndDown(BigDecimal targetUpAndDown) {
        this.targetUpAndDown = targetUpAndDown;
    }

    public BigDecimal getFollowPrePrice() {
        return followPrePrice;
    }

    public void setFollowPrePrice(BigDecimal followPrePrice) {
        this.followPrePrice = followPrePrice;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }
}
