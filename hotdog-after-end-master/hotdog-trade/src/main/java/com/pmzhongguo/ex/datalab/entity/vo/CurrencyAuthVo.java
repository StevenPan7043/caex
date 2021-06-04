package com.pmzhongguo.ex.datalab.entity.vo;

import com.pmzhongguo.ex.datalab.pojoAnnotation.FieldCheckPojo;
import com.pmzhongguo.ex.datalab.pojoAnnotation.ParamValidate;

import java.math.BigDecimal;

/**
 * @author jary
 * @creatTime 2019/11/30 10:46 AM
 */
public class CurrencyAuthVo extends FieldCheckPojo {

    private Integer id;
    /**
     * 基础货币
     */
    @ParamValidate(value = "基础货币",errorCode = "LANG_BASE_CURRENCY_NOT_BLANK")
    private String baseCurrencye;
    /**
     * 计价货币
     */
    @ParamValidate(value = "计价货币",errorCode = "LANG_QUOTE_CURRENCY_NOT_BLANK")
    private String valuationCurrency;

    /**
     * 用户名
     */
    @ParamValidate(value = "用户名",errorCode = "LANG_MEMBER_NAME_NOT_BLANK")
    private String mName;

    /**
     * 用户id
     */
    @ParamValidate(value = "用户id",errorCode = "LANG_MEMBER_ID_NOT_BLANK")
    private Integer memberId;

    /**
     * 基础货币提币上限
     */
    @ParamValidate(value = "基础货币提币上限",errorCode = "LANG_BASE_CURRENCY_UPPER_LIMIT_NOT_BLANK")
    private BigDecimal baseWQuota;

    /**
     * 计价货币提币上限
     */
    @ParamValidate(value = "计价货币提币上限",errorCode = "LANG_QUOTE_CURRENCY_UPPER_LIMIT_NOT_BLANK")
    private BigDecimal valuationWQuota;


    /**
     * 充提数据
     */
    private String dataCollection;
    /**
     * 实验室
     */

    private String dataLab;

    /**
     * 交易数据
     */
    private String dataTrade;

    /**
     * 我的钱包
     */
    private String dataWallet;


    private String remark;

    /**
     * 手续费比例
     */
    @ParamValidate(value = "手续费比例",errorCode = "LANG_FEE_RATE_NOT_BLANK")
    private BigDecimal feeScale;

    /**
     * 是否冻结
     */
    @ParamValidate(value = "是否冻结",errorCode = "LANG_FREE_NOT_BLANK")
    private int isFree;

    private int authority;

    public String getBaseCurrencye() {
        return baseCurrencye;
    }

    public void setBaseCurrencye(String baseCurrencye) {
        this.baseCurrencye = baseCurrencye;
    }

    public String getValuationCurrency() {
        return valuationCurrency;
    }

    public void setValuationCurrency(String valuationCurrency) {
        this.valuationCurrency = valuationCurrency;
    }

    public String getMName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
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

    public String getDataCollection() {
        return dataCollection;
    }

    public void setDataCollection(String dataCollection) {
        this.dataCollection = dataCollection;
    }

    public String getDataLab() {
        return dataLab;
    }

    public void setDataLab(String dataLab) {
        this.dataLab = dataLab;
    }

    public String getDataTrade() {
        return dataTrade;
    }

    public void setDataTrade(String dataTrade) {
        this.dataTrade = dataTrade;
    }

    public String getDataWallet() {
        return dataWallet;
    }

    public void setDataWallet(String dataWallet) {
        this.dataWallet = dataWallet;
    }

    public BigDecimal getFeeScale() {
        return feeScale;
    }

    public void setFeeScale(BigDecimal feeScale) {
        this.feeScale = feeScale;
    }

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
