package com.pmzhongguo.crowd.entity;

import com.pmzhongguo.ex.core.utils.JsonUtil;

import java.math.BigDecimal;

/**
 * @description: 项目众筹调度任务
 * @date: 2019-04-11 15:03
 * @author: 十一
 */
public class CrowdJob {

    private Integer id;
    /**
     * 任务名称
     */
    private String job_name;

    /**
     * 任务组
     * @see org.quartz.JobKey
     * 【任务名称 + 任务组】 需要保持唯一，由于一般是一个【任务名称】重复执行，所以【任务组】要保持唯一
     */
    private String job_group;

    /**
     * 项目id
     */
    private Integer project_id;

    /**
     * 任务执行的类名称
     */
    private String job_class_name;

    /**
     * 触发器名称
     */
    private String trigger_name;

    /**
     * 触发器组
     */
    private String trigger_group;

    /**
     * corn表达式
     */
    private String cron_expression;

    /**
     * 任务运行状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private String create_time;

    private String update_time;

    /**
     * 用户自增id，起始值
     */
    private Integer start_member_id;

    /**
     *  下单次数
     */
    private Integer order_num;

    /**
     * 单笔购买数量
     */
    private BigDecimal order_volume;

    /**
     * 每秒下单次数
     */
    private Integer sec_num;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getJob_group() {
        return job_group;
    }

    public void setJob_group(String job_group) {
        this.job_group = job_group;
    }

    public String getJob_class_name() {
        return job_class_name;
    }

    public void setJob_class_name(String job_class_name) {
        this.job_class_name = job_class_name;
    }

    public String getTrigger_name() {
        return trigger_name;
    }

    public void setTrigger_name(String trigger_name) {
        this.trigger_name = trigger_name;
    }

    public String getTrigger_group() {
        return trigger_group;
    }

    public void setTrigger_group(String trigger_group) {
        this.trigger_group = trigger_group;
    }

    public String getCron_expression() {
        return cron_expression;
    }

    public void setCron_expression(String cron_expression) {
        this.cron_expression = cron_expression;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getProject_id() {
        return project_id;
    }

    public void setProject_id(Integer project_id) {
        this.project_id = project_id;
    }

    public Integer getStart_member_id() {
        return start_member_id;
    }

    public void setStart_member_id(Integer start_member_id) {
        this.start_member_id = start_member_id;
    }

    public Integer getSec_num() {
        return sec_num;
    }

    public void setSec_num(Integer sec_num) {
        this.sec_num = sec_num;
    }

    public Integer getOrder_num() {
        return order_num;
    }

    public void setOrder_num(Integer order_num) {
        this.order_num = order_num;
    }

    public BigDecimal getOrder_volume() {
        return order_volume;
    }

    public void setOrder_volume(BigDecimal order_volume) {
        this.order_volume = order_volume;
    }

    @Override
    public String toString() {
        String result = "";
        try {
            result = JsonUtil.objectToJackson(this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}
