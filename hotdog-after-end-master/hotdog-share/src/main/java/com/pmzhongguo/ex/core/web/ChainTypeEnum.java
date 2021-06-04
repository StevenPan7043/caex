package com.pmzhongguo.ex.core.web;

/**
 * @description: 链的类型
 * @date: 2019/8/21
 * @author: 十一
 */
public enum ChainTypeEnum {

    /**
     * OMNI链
     */
    OMNI("OMNI"),
    /**
     * ERC20链
     */
    ERC20("ERC20");


    private String type;

    ChainTypeEnum(String type) {
        this.type = type;
    }


    public String getType() {
        return type;
    }

}
