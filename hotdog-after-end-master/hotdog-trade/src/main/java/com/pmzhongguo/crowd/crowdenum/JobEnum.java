package com.pmzhongguo.crowd.crowdenum;

/**
 * @description: 任务状态
 * @date: 2019-04-12 14:35
 * @author: 十一
 */
public enum JobEnum {
    // NONE, NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED
    /**
     * 未开始
     */
    NONE(0,"未开始"),
    /**
     * 运行中
     */
    NORMAL(1,"运行中"),
    /**
     * 暂停
     */
    PAUSED(2,"暂停"),

    /**
     * 已结束
     */
    COMPLETE(3,"已完成"),


    ERROR(4,"异常"),

    ;


    private Integer status;
    private String status_info;

    private JobEnum(Integer status, String status_info) {
        this.status = status;
        this.status_info = status_info;
    }

    public Integer getStatus() {
        return status;
    }

    public String getStatus_info() {
        return status_info;
    }

    public static Integer statusOf(String status) {
        if (status == null) {
            return null;
        }
        for (JobEnum statusEnum : values()) {
            if(status.equals(statusEnum.name())) {
                return statusEnum.getStatus();
            }
        }
        return null;
    }
}
