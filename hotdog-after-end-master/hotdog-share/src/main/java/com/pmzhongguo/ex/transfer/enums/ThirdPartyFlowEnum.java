package com.pmzhongguo.ex.transfer.enums;

/**
 * @author jary
 * @creatTime 2019/10/21 2:45 PM
 */
public enum ThirdPartyFlowEnum {

    PRO_DEDUCTION(1, "项目方扣款"),
    PRO_WRITE_BACK(2, "项目方回写");
    private int type;
    private String code;

    private ThirdPartyFlowEnum(int type, String code) {
        this.type = type;
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public String getCode() {
        return code;
    }
}
