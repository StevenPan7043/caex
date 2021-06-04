package com.pmzhongguo.crowd.crowdenum;

/**
 * @description: 项目状态枚举
 * @date: 2019-03-02 13:49
 * @author: 十一
 */
public enum  CrowdProjectStatusEnum {
    /**
     * 预热中
     */
    PRE_HOT(0,"PRE_HOT"),
    /**
     * 进行中
     */
    ONGOING(1,"ONGOING"),
    /**
     * 已结束
     */
    OVER(2,"OVER");


    private Integer status;
    private String status_info;

    private CrowdProjectStatusEnum(Integer status, String status_info) {
        this.status = status;
        this.status_info = status_info;
    }

    public Integer getStatus() {
        return status;
    }

    public String getStatus_info() {
        return status_info;
    }

    public static CrowdProjectStatusEnum statusOf(int status) {
        for (CrowdProjectStatusEnum statusEnum : values()) {
            if(statusEnum.getStatus() == status) {
                return statusEnum;
            }
        }
        return null;
    }
}
