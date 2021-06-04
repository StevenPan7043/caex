package com.pmzhongguo.otc.entity.dto;

/**
 * @author jary
 * @creatTime 2019/6/17 3:43 PM
 */
public class BankInfoDTO {

    private String bankName;

    private String bankType;

    private String id;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
