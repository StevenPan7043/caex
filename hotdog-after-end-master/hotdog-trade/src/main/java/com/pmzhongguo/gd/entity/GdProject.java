package com.pmzhongguo.gd.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 这是MyBatis Generator自动生成的Model Class.
 * 对应的数据表是 : gd_project
 * @author ldx
 * @date 2020-12-21 14:26:40
 */
public class GdProject implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 算力每日产出的币种
     */
    private String outputCurrency;

    /**
     * 购买算力的货币
     */
    private String quoteCurrency;

    /**
     * 跟单价格
     */
    private BigDecimal price;

    /**
     * 每日产出下限
     */
    private BigDecimal outputFloor;

    /**
     * 每日产出上限
     */
    private BigDecimal outputUpper;

    /**
     * 运行状态 1未开始2启动3停止
     */
    private String runStatus;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String modifyTime;

    /**
     * 跟单详情描述
     */
    private String particular;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(String runStatus) {
        this.runStatus = runStatus == null ? null : runStatus.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getParticular() {
        return particular;
    }

    public void setParticular(String particular) {
        this.particular = particular == null ? null : particular.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", outputCurrency=").append(outputCurrency);
        sb.append(", quoteCurrency=").append(quoteCurrency);
        sb.append(", price=").append(price);
        sb.append(", outputFloor=").append(outputFloor);
        sb.append(", outputUpper=").append(outputUpper);
        sb.append(", runStatus=").append(runStatus);
        sb.append(", createTime=").append(createTime);
        sb.append(", modifyTime=").append(modifyTime);
        sb.append(", particular=").append(particular);
        sb.append("]");
        return sb.toString();
    }
}