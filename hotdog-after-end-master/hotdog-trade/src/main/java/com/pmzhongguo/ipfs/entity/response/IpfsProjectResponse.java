package com.pmzhongguo.ipfs.entity.response;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Daily
 * @date 2020/7/30 10:53
 */
public class IpfsProjectResponse implements Serializable {

    private Integer id;

    private Integer publishNum;

    private String projectNameE;

    private Integer boughtNum;

    private Integer equityCycle;

    private BigDecimal outputFloor;

    private BigDecimal outputUpper;

    private BigDecimal price;

    private String projectName;

    private String saleStartTime;

    private String saleEndTime;

    private String runTime;

    private String saleStatus;

    private String outputCurrency;

    private String quoteCurrency;

    private BigDecimal exchangeRate;

    private String type;

    private String isDisplayNum;

    private BigDecimal fee;

    private String equityDesc;

    private String priceDesc;

    private String equityDescE;

    private String priceDescE;

    private Integer userBuyLimit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPublishNum() {
        return publishNum;
    }

    public void setPublishNum(Integer publishNum) {
        this.publishNum = publishNum;
    }

    public Integer getBoughtNum() {
        return boughtNum;
    }

    public void setBoughtNum(Integer boughtNum) {
        this.boughtNum = boughtNum;
    }

    public Integer getEquityCycle() {
        return equityCycle;
    }

    public void setEquityCycle(Integer equityCycle) {
        this.equityCycle = equityCycle;
    }

    public BigDecimal getOutputFloor() {
        return outputFloor;
    }

    public void setOutputFloor(BigDecimal outputFloor) {
        this.outputFloor = outputFloor;
    }

    public BigDecimal getOutputUpper() {
        return outputUpper;
    }

    public void setOutputUpper(BigDecimal outputUpper) {
        this.outputUpper = outputUpper;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSaleStartTime() {
        return saleStartTime;
    }

    public void setSaleStartTime(String saleStartTime) {
        this.saleStartTime = saleStartTime;
    }

    public String getSaleEndTime() {
        return saleEndTime;
    }

    public void setSaleEndTime(String saleEndTime) {
        this.saleEndTime = saleEndTime;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(String saleStatus) {
        this.saleStatus = saleStatus;
    }

    public String getOutputCurrency() {
        return outputCurrency;
    }

    public void setOutputCurrency(String outputCurrency) {
        this.outputCurrency = outputCurrency;
    }

    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public void setQuoteCurrency(String quoteCurrency) {
        this.quoteCurrency = quoteCurrency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsDisplayNum() {
        return isDisplayNum;
    }

    public void setIsDisplayNum(String isDisplayNum) {
        this.isDisplayNum = isDisplayNum == null ? null : isDisplayNum.trim();
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getEquityDesc() {
        return equityDesc;
    }

    public void setEquityDesc(String equityDesc) {
        this.equityDesc = equityDesc;
    }

    public String getPriceDesc() {
        return priceDesc;
    }

    public void setPriceDesc(String priceDesc) {
        this.priceDesc = priceDesc;
    }

    public String getEquityDescE() {
        return equityDescE;
    }

    public void setEquityDescE(String equityDescE) {
        this.equityDescE = equityDescE;
    }

    public String getPriceDescE() {
        return priceDescE;
    }

    public void setPriceDescE(String priceDescE) {
        this.priceDescE = priceDescE;
    }

    public Integer getUserBuyLimit() {
        return userBuyLimit;
    }

    public void setUserBuyLimit(Integer userBuyLimit) {
        this.userBuyLimit = userBuyLimit;
    }

    public String getProjectNameE() {
        return projectNameE;
    }

    public void setProjectNameE(String projectNameE) {
        this.projectNameE = projectNameE;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
