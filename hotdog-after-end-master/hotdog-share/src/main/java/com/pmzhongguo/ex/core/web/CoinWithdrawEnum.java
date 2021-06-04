package com.pmzhongguo.ex.core.web;

/**
 * @description: 币种提现枚举
 * @date: 2019-12-27 09:58
 * @author: 十一
 */
public enum  CoinWithdrawEnum {


    /**
     *
     */
    PENDING(0,"待处理"),
    COMPLETED(1,"已完成"),
    CANCELED(2,"已取消"),
    PROCESSING(3,"处理中")
            ;

    private int type;


    private String codeCn;


    CoinWithdrawEnum(int type, String codeCn) {
        this.type = type;
        this.codeCn = codeCn;
    }

    public int getType() {
        return type;
    }


    public String getCodeCn() {
        return codeCn;
    }




}
