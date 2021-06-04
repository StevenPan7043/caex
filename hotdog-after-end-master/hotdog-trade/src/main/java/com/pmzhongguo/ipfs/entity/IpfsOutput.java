package com.pmzhongguo.ipfs.entity;

import java.math.BigDecimal;
import java.util.Date;

public class IpfsOutput {
    private Integer id;

    private Integer projectId;

    private String outputCurrency;

    private BigDecimal capacity;

    private BigDecimal capacityOut;

    private Integer outputStatus;

    private String outputDate;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getOutputCurrency() {
        return outputCurrency;
    }

    public void setOutputCurrency(String outputCurrency) {
        this.outputCurrency = outputCurrency == null ? null : outputCurrency.trim();
    }

    public BigDecimal getCapacity() {
        return capacity;
    }

    public void setCapacity(BigDecimal capacity) {
        this.capacity = capacity;
    }

    public String getOutputDate() {
        return outputDate;
    }

    public void setOutputDate(String outputDate) {
        this.outputDate = outputDate == null ? null : outputDate.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getCapacityOut() {
        return capacityOut;
    }

    public void setCapacityOut(BigDecimal capacityOut) {
        this.capacityOut = capacityOut;
    }

    public Integer getOutputStatus() {
        return outputStatus;
    }

    public void setOutputStatus(Integer outputStatus) {
        this.outputStatus = outputStatus;
    }

    @Override
    public String toString() {
        return "IpfsOutput{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", outputCurrency='" + outputCurrency + '\'' +
                ", capacity=" + capacity +
                ", outputDate='" + outputDate + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}