/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/16 All Rights Reserved.
 */
package com.pmzhongguo.ex.core.web.resp;

import java.io.Serializable;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/16 13:55
 * @description：接受返回参数
 * @version: $
 */
public class ResponseModel implements Serializable
{
    private static final long serialVersionUID = -351766684410896081L;
    private Integer code;

    private String message;

    private Object data;

    private String body;

    private Integer statusCode;

    public Integer getCode()
    {
        return code;
    }

    public void setCode(Integer code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
