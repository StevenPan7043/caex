package com.pmzhongguo.zzextool.enums;

/**
 * @description: 异常提示错误码
 * @date: 2018-12-19 15:03
 * @author: 十一
 */
public enum CommonEnum
{
    // 通用 5000xx
    LANG_REDIS_ERROR(500003, "获取redis失败", "LANG_REDIS_ERROR"),
    LANG_SYSTEM_BUSY(500017, "系统繁忙！", "LANG_SYSTEM_BUSY"),

    // 会员相关 5001xx

    // 交易相关 5002xx

    // 后台管理端 5003xx

    // OTC相关 5004xx
    //商家状态不是 APPLY_PASSED(2, "apply_passed")

    ;

    private Integer errorCode;

    private String errorCNMsg;

    private String errorENMsg;

    CommonEnum(Integer errorCode, String errorCNMsg, String errorENMsg)
    {
        this.errorCode = errorCode;
        this.errorCNMsg = errorCNMsg;
        this.errorENMsg = errorENMsg;
    }

    public Integer getErrorCode()
    {
        return errorCode;
    }

    public String getErrorCNMsg()
    {
        return errorCNMsg;
    }

    public String getErrorENMsg()
    {
        return errorENMsg;
    }
}
