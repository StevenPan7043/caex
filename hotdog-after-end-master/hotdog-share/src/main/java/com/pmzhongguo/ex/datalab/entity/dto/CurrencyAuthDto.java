package com.pmzhongguo.ex.datalab.entity.dto;

import com.pmzhongguo.ex.datalab.enums.DateAuthEnum;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/11/30 3:28 PM
 */
public class CurrencyAuthDto {
    private Integer id;
    /**
     * 用户id
     */
    private Integer memberId;
    /**
     * 交易对id
     */
    private Integer currencyPairId;

    /**
     * 用户名
     */
    private String mName;

    /**
     * 手续费比例
     */
    private BigDecimal feeScale;

    /**
     * 权限
     */
    private Integer authority;

    /**
     * 基础货币提现限额
     */
    private BigDecimal baseWQuota;

    /**
     * 计价货币提现限额
     */
    private BigDecimal valuationWQuota;


    /**
     * 是否冻结：1：冻结，2:正常
     */
    private Integer isFree;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 扩展字段
     */
    private String extend1;

    /**
     * 扩展字段
     */
    private String extend2;

    private List<Map<String, Boolean>> authorityList;

    private DateAuthEnum[] authorityEnums;

    private String symbol;

    private String currencyPair;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getCurrencyPairId() {
        return currencyPairId;
    }

    public void setCurrencyPairId(Integer currencyPairId) {
        this.currencyPairId = currencyPairId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public BigDecimal getFeeScale() {
        return feeScale;
    }

    public void setFeeScale(BigDecimal feeScale) {
        this.feeScale = feeScale;
    }

    public Integer getAuthority() {
        return authority;
    }

    public void setAuthority(Integer authority) {
        this.authority = authority;
        this.authorityList = DateAuthEnum.getDateAuth(authority);
    }

    public BigDecimal getBaseWQuota() {
        return baseWQuota;
    }

    public void setBaseWQuota(BigDecimal baseWQuota) {
        this.baseWQuota = baseWQuota;
    }

    public BigDecimal getValuationWQuota() {
        return valuationWQuota;
    }

    public void setValuationWQuota(BigDecimal valuationWQuota) {
        this.valuationWQuota = valuationWQuota;
    }

    public Integer getIsFree() {
        return isFree;
    }

    public void setIsFree(Integer isFree) {
        this.isFree = isFree;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getExtend1() {
        return extend1;
    }

    public void setExtend1(String extend1) {
        this.extend1 = extend1;
    }

    public String getExtend2() {
        return extend2;
    }

    public void setExtend2(String extend2) {
        this.extend2 = extend2;
    }

    public List<Map<String, Boolean>> getAuthorityList() {
        return authorityList;
    }

    public void setAuthorityList(List<Map<String, Boolean>> authorityList) {
        this.authorityList = authorityList;
    }

    public DateAuthEnum[] getAuthorityEnums() {
        return authorityEnums;
    }

    public void setAuthorityEnums(DateAuthEnum[] authorityEnums) {
        this.authorityEnums = authorityEnums;
    }
}
