/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/16 All Rights Reserved.
 */
package com.pmzhongguo.ex.business.entity;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/16 10:22
 * @description：api 资金划转 bean
 * @version: $
 */
public class ApiFundTransfer extends ReqBaseSecret
{
    @ApiModelProperty(value = "用户名", required = true)
    private String m_name;

    @ApiModelProperty(value = "币种", required = true)
    private String currency;

    @ApiModelProperty(value = "划转数量", required = true)
    private BigDecimal transferNum;

    @ApiModelProperty(value = "客户端交易 ID", required = true)
    private String tradeID;

    @ApiModelProperty(value = "资金密码", required = true)
    private String fundPwd;

    private String transferType;

    private BigDecimal transferFee;

    /**
     * 冲提币地址
     */
    private String addr;

    public String getM_name()
    {
        return m_name;
    }

    public void setM_name(String m_name)
    {
        this.m_name = m_name;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public BigDecimal getTransferNum()
    {
        return transferNum;
    }

    public void setTransferNum(BigDecimal transferNum)
    {
        this.transferNum = transferNum;
    }

    public String getTradeID()
    {
        return tradeID;
    }

    public void setTradeID(String tradeID)
    {
        this.tradeID = tradeID;
    }

    public String getFundPwd()
    {
        return fundPwd;
    }

    public void setFundPwd(String fundPwd)
    {
        this.fundPwd = fundPwd;
    }

    public String getTransferType()
    {
        return transferType;
    }

    public void setTransferType(String transferType)
    {
        this.transferType = transferType;
    }

    public BigDecimal getTransferFee()
    {
        return transferFee;
    }

    public void setTransferFee(BigDecimal transferFee)
    {
        this.transferFee = transferFee;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
