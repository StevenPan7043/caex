package com.pmzhongguo.zzextool.exception;

public class BusinessException extends RuntimeException
{

    private static final long serialVersionUID = -6566569926958436584L;
    private Integer status; // 异常编码
    private String msg; // 异常信息
    private String detail; // 具体信息

    public BusinessException(Integer status, String eMsg, String detail)
    {
        super(eMsg);
        this.setStatus(status);
        this.setMsg(eMsg);
        this.setDetail(detail);
    }

    public BusinessException(Integer status, String eMsg)
    {
        super(eMsg);
        this.setStatus(status);
        this.setMsg(eMsg);
    }

    public BusinessException(String eMsg)
    {
        super(eMsg);
        this.setStatus(1000);
        this.setMsg(eMsg);
    }

    public String getDetail()
    {
        return detail;
    }

    public void setDetail(String detail)
    {
        this.detail = detail;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }
}
