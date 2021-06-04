package com.pmzhongguo.market.entity.vo;


import com.pmzhongguo.market.dto.EosMarketDetailDTO;

import java.io.Serializable;
import java.util.List;

/**
 * @author jary
 * @creatTime 2019/6/18 5:56 PM
 */
public class BaseReqVo implements Serializable {

    private String status;

    private String ts;

    private List<EosMarketDetailDTO> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public List<EosMarketDetailDTO> getData() {
        return data;
    }

    public void setData(List<EosMarketDetailDTO> data) {
        this.data = data;
    }
}
