package com.pmzhongguo.ex.business.resp;


import com.pmzhongguo.ex.core.web.resp.Resp;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @description: 钱包响应结果
 * @date:        2019-12-17
 * @author:      十一
 */
public class WalletResp extends Resp implements Serializable {

    private static final long serialVersionUID = -6848140919218772358L;


    @ApiModelProperty(value = "响应值")
    public Object data;

    public static WalletResp SUCCESS = new WalletResp(Resp.SUCCESS,Resp.SUCCESS_MSG);



    public WalletResp(Integer _state, String _msg) {
        super(_state, _msg);
    }

    public static WalletResp FAIL(String _msg,Object data) {
        return new WalletResp(Resp.FAIL, _msg, data);
    }

    public static WalletResp FAIL(String _msg) {
        return new WalletResp(Resp.FAIL, _msg, null);
    }

    public WalletResp(Integer _state, String _msg,Object data) {
        super(_state, _msg);
        this.data = data;
    }
}