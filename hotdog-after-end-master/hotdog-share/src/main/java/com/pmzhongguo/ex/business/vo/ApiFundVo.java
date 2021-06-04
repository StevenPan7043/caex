package com.pmzhongguo.ex.business.vo;

import com.pmzhongguo.ex.business.entity.ApiFundTransfer;

/**
 * @author jary
 * @creatTime 2019/11/14 9:39 AM
 */
public class ApiFundVo extends ApiFundTransfer {

    /**
     * 订单号
     */
    private String o_no ;

    /**
     * 交易对
     */
    private String symbol;

    public String getO_no() {
        return o_no;
    }

    public void setO_no(String o_no) {
        this.o_no = o_no;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
