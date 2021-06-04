package com.pmzhongguo.ex.core.web.resp;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


public class Resp implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 2425719337037524406L;
    /**
     *
     */
    public static final Integer SUCCESS = 1;
    public static final Integer FAIL = -1;
    public static final String SUCCESS_MSG = "success";
    public static final String SUBMIT_SUCCESS_MSG = "submit-success";

    @ApiModelProperty(value = "状态值，1表示成功，其他值表示失败")
    private Integer state;
    @ApiModelProperty(value = "状态说明")
    private String msg;

    public Resp(Integer _state, String _msg) {
        state = _state;
        msg = _msg;
    }

    public static Resp SUCCESS_STATE = new Resp(SUCCESS,SUCCESS_MSG);

    public static Resp successMsg (String _msg) {
        return new Resp(SUCCESS, _msg);
    }

    public static Resp failMsg (String _msg) {
        return new Resp(FAIL, _msg);
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
