package com.pmzhongguo.lockposition.base;


/**
 * @author jary
 * @creatTime 2019/7/19 3:27 PM
 */
public class BasePram  {

    private Integer id;

//    @ParamValidate(value = "创建人")
    private String createBy;

    //    @ParamValidate(value = "创建时间")
    private String createTime;

    //    @ParamValidate(value = "更新人")
    private String updateBy;

    //    @ParamValidate(value = "更新时间")
    private String updateTime;

    private Integer isDeleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
