package com.pmzhongguo.ex.core.web;

/**
 * @description: 手动归集回写币种枚举
 * @date: 2019-10-24 17:36
 * @author: zn
 */
public enum ManualOperationCurrencyEnum {
    /**
     * eth
     */
    ETH("eth",1,1);
    //EOS("eos",1,0),
    //GPEI("gpei",1,0),
    //XRP("xrp",1,0);


    /**
     * 币种名
     */
    private String currency;
    /**
     * 是否能手动回写：0否，1是
     */
    private int recharge;
    /**
     * 是否能手动归集: 0否，1是
     */
    private int guiji;


    private ManualOperationCurrencyEnum(String currency, int recharge, int guiji) {
        this.currency = currency;
        this.recharge = recharge;
        this.guiji = guiji;
    }

    public String getCurrency() {
        return currency;
    }

    public int getRecharge() {
        return recharge;
    }

    public int getGuiji() {
        return guiji;
    }

}
