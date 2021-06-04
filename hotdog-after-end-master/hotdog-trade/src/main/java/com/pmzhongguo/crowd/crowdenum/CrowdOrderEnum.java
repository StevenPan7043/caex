package com.pmzhongguo.crowd.crowdenum;

/**
 * @description: 项目状态枚举
 * @date: 2019-03-02 13:49
 * @author: 十一
 */
public enum CrowdOrderEnum {
    /**
     * web
     */
    WEB(0,"web"),
    /**
     * ios
     */
    IOS(1,"ios"),
    /**
     * android
     */
    ANDROID(2,"android"),
    /**
     * api
     */
    API(3,"api");


    private Integer status;
    private String status_info;

    private CrowdOrderEnum(Integer status, String status_info) {
        this.status = status;
        this.status_info = status_info;
    }

    public Integer getStatus() {
        return status;
    }

    public String getStatus_info() {
        return status_info;
    }

    public static CrowdOrderEnum statusOf(int status) {
        for (CrowdOrderEnum statusEnum : values()) {
            if(statusEnum.getStatus() == status) {
                return statusEnum;
            }
        }
        return null;
    }
}
