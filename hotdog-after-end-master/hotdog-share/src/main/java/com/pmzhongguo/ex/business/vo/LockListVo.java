package com.pmzhongguo.ex.business.vo;

import java.math.BigDecimal;

/**
 * @description: 锁仓划转视图对象
 * @date: 2018-12-25 16:00
 * @author: 十一
 */
public class LockListVo {

    private String currency;//d_currency的currency
    private BigDecimal amount;// 锁仓数量
    private int l_type; //锁仓时长，1：1个月，3：3个月
    private String secPwd; //用户资金密码，是md5加密

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getL_type() {
        return l_type;
    }

    public void setL_type(int l_type) {
        this.l_type = l_type;
    }

    public String getSecPwd() {
        return secPwd;
    }

    public void setSecPwd(String secPwd) {
        this.secPwd = secPwd;
    }
}
