package com.pmzhongguo.ex.business.entity;

/**
 * @author jary
 * @creatTime 2020/5/26 2:48 PM
 */
public class ApiAuth2Validate extends ReqBaseSecret {

    /**
     * 加密穿
     */
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
