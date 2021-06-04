package com.pmzhongguo.ex.transfer.entity;

import java.math.BigDecimal;

/**
 * @author ：yukai
 * @date ：Created in 2019/5/6 14:27
 * @description：划转日志记录表
 * @version: $
 */
public class FundOperationLog
{
    private Integer id;

    private Integer memberId;

    private String mName;

    private String tradeId;

    private String currency;

    private String transferTime;

    private BigDecimal currentNum;

    private BigDecimal transferNum;

    private String transferStatus;

    /**
     * 划转类型
     */
    private String transferType;

    /**
     * 划转手续费
     */
    private BigDecimal transferFee;

    private String body;

    private String remark;

    private Integer delFlag;

    // 查询条件
    private String tableName;


    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getMemberId()
    {
        return memberId;
    }

    public void setMemberId(Integer memberId)
    {
        this.memberId = memberId;
    }

    public String getmName()
    {
        return mName;
    }

    public void setmName(String mName)
    {
        this.mName = mName;
    }

    public String getTradeId()
    {
        return tradeId;
    }

    public void setTradeId(String tradeId)
    {
        this.tradeId = tradeId;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public String getTransferTime()
    {
        return transferTime;
    }

    public void setTransferTime(String transferTime)
    {
        this.transferTime = transferTime;
    }

    public BigDecimal getCurrentNum()
    {
        return currentNum;
    }

    public void setCurrentNum(BigDecimal currentNum)
    {
        this.currentNum = currentNum;
    }

    public BigDecimal getTransferNum()
    {
        return transferNum;
    }

    public void setTransferNum(BigDecimal transferNum)
    {
        this.transferNum = transferNum;
    }

    public String getTransferStatus()
    {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus)
    {
        this.transferStatus = transferStatus;
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

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public Integer getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }
}