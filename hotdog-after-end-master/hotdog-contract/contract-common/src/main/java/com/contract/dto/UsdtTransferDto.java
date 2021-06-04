package com.contract.dto;

import com.contract.enums.TransferTypeEnum;
import com.contract.service.Page;

import java.math.BigDecimal;
import java.util.Date;

/**
 * usdt划转列表
 * @author jary
 * @creatTime 2020/2/22 9:34 AM
 */
public class UsdtTransferDto extends Page {

    /**
     * 会员账号
     */
    private String pName;

    /**
     * 会员cid
     */
    private int cid;

    /**
     * 推荐人账号
     */
    private String parentName;

    /**
     * 推荐人cid
     */
    private int parentCid;

    /**
     * 业务员账号
     */
    private String salesmanName;
    /**
     * 业务员id
     */
    private int salesman;

    /**
     * 划转类型
     */
    private int typeid;

    /**
     * 转入账户
     */
    private String transIn;

    /**
     * 转出账户
     */
    private String transOut;
    /**
     * 转入转出
     */
    private int isout;

    private String isoutCode;

    /**
     * 转入转出金额
     */
    private BigDecimal cost;

    /**
     * 子公司
     */
    private String salemanname;

    /**
     * 创建时间
     */
    private Date createtime;
    /** 操作员id */
    private Integer userid;

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public int getParentCid() {
        return parentCid;
    }

    public void setParentCid(int parentCid) {
        this.parentCid = parentCid;
    }

    public String getSalesmanName() {
        return salesmanName;
    }

    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName;
    }

    public int getSalesman() {
        return salesman;
    }

    public void setSalesman(int salesman) {
        this.salesman = salesman;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
        TransferTypeEnum enumByType = TransferTypeEnum.getEnumByType(typeid);
        if (enumByType != null) {
            transIn = enumByType.getTransIn();
            transOut = enumByType.getTransOut();
        }
    }

    public int getIsout() {
        return isout;
    }

    public void setIsout(int isout) {
        this.isout = isout;
        if (isout ==0){
            isoutCode = "转出";
        }if (isout ==1){
            isoutCode = "转入";
        }

    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getSalemanname() {
        return salemanname;
    }

    public void setSalemanname(String salemanname) {
        this.salemanname = salemanname;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getTransIn() {
        return transIn;
    }

    public void setTransIn(String transIn) {
        this.transIn = transIn;
    }

    public String getTransOut() {
        return transOut;
    }

    public void setTransOut(String transOut) {
        this.transOut = transOut;
    }

    public String getIsoutCode() {
        return isoutCode;
    }

    public void setIsoutCode(String isoutCode) {
        this.isoutCode = isoutCode;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
