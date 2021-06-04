package com.pmzhongguo.ex.business.dto;

/**
 * @author jary
 * @creatTime 2019/12/10 5:46 PM
 */
public class JuHeSendResp {

    private String reason;

    private JuHeParam result;
    private String error_code;



    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public JuHeParam getResult() {
        return result;
    }

    public void setResult(JuHeParam result) {
        this.result = result;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }
}
