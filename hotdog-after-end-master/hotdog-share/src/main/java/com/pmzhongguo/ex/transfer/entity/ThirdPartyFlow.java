package com.pmzhongguo.ex.transfer.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 第三方流水
 * @author jary
 * @creatTime 2019/10/21 2:13 PM
 */
public class ThirdPartyFlow implements Serializable {

    private static final long serialVersionUID = 2096976492597087364L;

    private Long id;

    /**
     * 流水类型：1：项目方扣款；2：项目方回写'
     * 项目方扣款：即用户充币，项目方账户扣款
     * 项目方回写：即用户提币，项目方账户回写。
     */
    private Integer flowType;

    /**
     *项目方流水id
     */
    private Long proFlowId;

    /**
     *客户流水ID
     */
    private Long userFlowId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     *备注
     */
    private String  remark;


    public ThirdPartyFlow(Integer flowType, Long proFlowId, Long userFlowId, String createTime, String remark) {
        this.flowType = flowType;
        this.proFlowId = proFlowId;
        this.userFlowId = userFlowId;
        this.createTime = createTime;
        this.remark = remark;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFlowType() {
        return flowType;
    }

    public void setFlowType(Integer flowType) {
        this.flowType = flowType;
    }

    public Long getProFlowId() {
        return proFlowId;
    }

    public void setProFlowId(Long proFlowId) {
        this.proFlowId = proFlowId;
    }

    public Long getUserFlowId() {
        return userFlowId;
    }

    public void setUserFlowId(Long userFlowId) {
        this.userFlowId = userFlowId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
