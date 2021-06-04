package com.pmzhongguo.ipfs.entity;

import java.math.BigDecimal;
import java.util.Date;

public class IpfsProject {
    private Integer id;

    private String projectName;

    private String projectNameE;

    private String code;

    private Integer periods;

    private Integer publishNum;

    private Integer boughtNum;

    private String outputCurrency;

    private String quoteCurrency;

    private BigDecimal price;

    private BigDecimal outputFloor;

    private BigDecimal outputUpper;

    private BigDecimal rOutputFloor;

    private BigDecimal rOutputUpper;

    private BigDecimal fee;

    private String saleStartTime;

    private String saleEndTime;

    private String runTime;

    private Integer equityCycle;

    private String saleStatus;

    private String runStatus;

    private BigDecimal introBonus;

    private String type;

    private String isDisplayNum;

    private BigDecimal exchangeRate;

    private BigDecimal discount;

    private Integer userBuyLimit;

    private String equityDesc;

    private String equityDescE;

    private String priceDesc;

    private String priceDescE;

    private Date createTime;

    private Date modifyTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getProjectNameE() {
        return projectNameE;
    }

    public void setProjectNameE(String projectNameE) {
        this.projectNameE = projectNameE == null ? null : projectNameE.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Integer getPeriods() {
        return periods;
    }

    public void setPeriods(Integer periods) {
        this.periods = periods;
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

    public String getOutputCurrency() {
        return outputCurrency;
    }

    public void setOutputCurrency(String outputCurrency) {
        this.outputCurrency = outputCurrency == null ? null : outputCurrency.trim();
    }

    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public void setQuoteCurrency(String quoteCurrency) {
        this.quoteCurrency = quoteCurrency == null ? null : quoteCurrency.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public BigDecimal getrOutputFloor() {
        return rOutputFloor;
    }

    public void setrOutputFloor(BigDecimal rOutputFloor) {
        this.rOutputFloor = rOutputFloor;
    }

    public BigDecimal getrOutputUpper() {
        return rOutputUpper;
    }

    public void setrOutputUpper(BigDecimal rOutputUpper) {
        this.rOutputUpper = rOutputUpper;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
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

    public Integer getEquityCycle() {
        return equityCycle;
    }

    public void setEquityCycle(Integer equityCycle) {
        this.equityCycle = equityCycle;
    }

    public String getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(String saleStatus) {
        this.saleStatus = saleStatus == null ? null : saleStatus.trim();
    }

    public String getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(String runStatus) {
        this.runStatus = runStatus == null ? null : runStatus.trim();
    }

    public BigDecimal getIntroBonus() {
        return introBonus;
    }

    public void setIntroBonus(BigDecimal introBonus) {
        this.introBonus = introBonus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getIsDisplayNum() {
        return isDisplayNum;
    }

    public void setIsDisplayNum(String isDisplayNum) {
        this.isDisplayNum = isDisplayNum == null ? null : isDisplayNum.trim();
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getUserBuyLimit() {
        return userBuyLimit;
    }

    public void setUserBuyLimit(Integer userBuyLimit) {
        this.userBuyLimit = userBuyLimit;
    }

    public String getEquityDesc() {
        return equityDesc;
    }

    public void setEquityDesc(String equityDesc) {
        this.equityDesc = equityDesc == null ? null : equityDesc.trim();
    }

    public String getEquityDescE() {
        return equityDescE;
    }

    public void setEquityDescE(String equityDescE) {
        this.equityDescE = equityDescE == null ? null : equityDescE.trim();
    }

    public String getPriceDesc() {
        return priceDesc;
    }

    public void setPriceDesc(String priceDesc) {
        this.priceDesc = priceDesc == null ? null : priceDesc.trim();
    }

    public String getPriceDescE() {
        return priceDescE;
    }

    public void setPriceDescE(String priceDescE) {
        this.priceDescE = priceDescE == null ? null : priceDescE.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}