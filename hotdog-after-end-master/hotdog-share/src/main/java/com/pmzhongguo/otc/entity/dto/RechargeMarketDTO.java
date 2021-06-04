package com.pmzhongguo.otc.entity.dto;

import java.util.List;

/**
 * @author jary
 * @creatTime 2019/6/17 3:08 PM
 */
public class RechargeMarketDTO {

    /**
     * 返回状态
     */
    private int code;

    /**
     * 返回信息描述
     */
    private String message;

    /**
     * 总数
     */
    private int totalCount;

    /**
     * 每页数量
     */
    private int pageSize;

    /**
     * 总页数
     */
    private int totalPage;

    /**
     * 当前页数
     */
    private int currPage;

    private boolean success;

    private List<EosProTraderDTO> data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public List<EosProTraderDTO> getData() {
        return data;
    }

    public void setData(List<EosProTraderDTO> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
