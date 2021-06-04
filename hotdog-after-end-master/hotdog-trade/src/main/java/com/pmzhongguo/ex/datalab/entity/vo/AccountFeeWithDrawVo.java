package com.pmzhongguo.ex.datalab.entity.vo;

import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.datalab.pojoAnnotation.FieldCheckPojo;
import com.pmzhongguo.ex.datalab.pojoAnnotation.ParamValidate;

import java.math.BigDecimal;

/**
 * 手续费账户提现vo
 * @author jary
 * @creatTime 2019/12/6 11:38 AM
 */
public class AccountFeeWithDrawVo extends FieldCheckPojo {

    /**
     * 交易所账号
     */
    private String mName;

    /**
     * 提币币种
     */
    @ParamValidate(value = "提币币种",errorCode = "LANG_CURRENCY_NOT_BLANK")
    private String currency;


    /**
     * 交易对
     */
    @ParamValidate(value = "交易对",errorCode = "LANG_CURRENCY_PAIR_NOT_BLANK")
    private String symbol;

    /**
     * 提币数
     */
    @ParamValidate(value = "提币数",errorCode = "LANG_AMOUNT_NOT_BLANK")
    private BigDecimal currencyNo;


    /**
     * 短信
     */
    @ParamValidate(value = "短信",errorCode = "LANG_VERIFY_CODE_NOT_BLANK")
    private String sendCode;

    /**
     * 资金密码
     */
    @ParamValidate(value = "资金密码",errorCode = "LANG_SECURITY_PWD_NOT_BLANK")
    private String fundPassword;

    private Member member;

    public String getMName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getCurrencyNo() {
        return currencyNo;
    }

    public void setCurrencyNo(BigDecimal currencyNo) {
        this.currencyNo = currencyNo;
    }

    public String getmName() {
        return mName;
    }

    public String getSendCode() {
        return sendCode;
    }

    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }

    public String getFundPassword() {
        return fundPassword;
    }

    public void setFundPassword(String fundPassword) {
        this.fundPassword = fundPassword;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
