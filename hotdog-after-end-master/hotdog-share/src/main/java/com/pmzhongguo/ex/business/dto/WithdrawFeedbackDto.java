package com.pmzhongguo.ex.business.dto;

import com.pmzhongguo.ex.core.web.req.BaseSecretReq;
import io.swagger.annotations.ApiModelProperty;

public class WithdrawFeedbackDto extends BaseSecretReq
{
    @ApiModelProperty(value = "提现订单的唯一标志", required = true)
    private Long id;
    @ApiModelProperty(value = "提现状态，取值1表示完成，2表示取消", required = true)
    private Integer w_status;
    @ApiModelProperty(value = "TXID", required = true)
    private String w_txid;
    @ApiModelProperty(value = "取消原因，取值1时，填''，取值2时，填取消原因", required = true)
    private String reject_reason;


    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getW_status()
    {
        return w_status;
    }

    public void setW_status(Integer w_status)
    {
        this.w_status = w_status;
    }

    public String getW_txid()
    {
        return w_txid;
    }

    public void setW_txid(String w_txid)
    {
        this.w_txid = w_txid;
    }

    public String getReject_reason()
    {
        return reject_reason;
    }

    public void setReject_reason(String reject_reason)
    {
        this.reject_reason = reject_reason;
    }
}