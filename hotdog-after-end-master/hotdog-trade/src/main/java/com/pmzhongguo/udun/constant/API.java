package com.pmzhongguo.udun.constant;

import com.pmzhongguo.ex.core.utils.PropertiesUtil;

public class API {
    public static final String API_KEY = PropertiesUtil.getPropValByKey("udun_api_key");
    public static final String MERCHANT_ID = PropertiesUtil.getPropValByKey("udun_merchantId");//商户号
    public static final String WALLET_ID = PropertiesUtil.getPropValByKey("udun_wallet_id");//钱包编号

    public static final String CREATE_ADDRESS = "/mch/address/create";//创建地址
    public static final String WITHDRAW = "/mch/withdraw";//提币申请
    public static final String TRANSACTION = "/mch/transaction";//查询交易记录
    public static final String AUTO_WITHDRAW = "/mch/withdraw/proxypay";//代付,发送自动付款申请，未设置代付信息或代付失败则进入审核状态
    public static final String SUPPORT_COIN = "/mch/support-coins";//获取商户支持的币种信息
    public static final String CHECK_PROXY = "/mch/check-proxy";//
    public static final String CHECK_ADDRESS = "/mch/check/address";//校验地址合法性
    public static final String CREATE_BATCH_ADDRESS = "/mch/address/create/batch";//批量创建地址
    public static final String IP = PropertiesUtil.getPropValByKey("udun_ip");

    public static String getUrl(String api) {
        return IP + api;
    }
}
