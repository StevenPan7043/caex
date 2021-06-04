package com.contract.service.wallet.btc;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.contract.common.HttpUtil;
import com.contract.exception.ThrowJsonException;

public class UsdtPrice {


    /**
     * 获取1USDT=X CNY
     *
     * @return
     */
    public static BigDecimal getUsdtToCny() {
        return new BigDecimal("7");
        // todo 这里要动态获取 usdt 价格，暂时先写死
//        BigDecimal moneyResult = BigDecimal.ZERO;
//        String result = HttpUtil.getInstance().get("http://data.block.cc/api/v1/exchange_rate?base=usdt&symbols=cny");
//        if (StringUtils.isEmpty(result)) {
//            throw new ThrowJsonException("获取USDT币价错误");
//        }
//        JSONObject jsonObject = JSONObject.parseObject(result);
//        if (jsonObject == null) {
//            throw new ThrowJsonException("获取USDT币价错误");
//        }
//        String code = jsonObject.getString("code");
//        if ("0".equals(code)) {
//            JSONObject data = jsonObject.getJSONObject("data");
//            if (data == null) {
//                throw new ThrowJsonException("获取USDT币价错误");
//            }
//            JSONObject obj = data.getJSONObject("rates");
//            if (obj == null) {
//                throw new ThrowJsonException("获取USDT币价错误");
//            }
//            moneyResult = obj.getBigDecimal("CNY");
//        }
//        return moneyResult;
    }

    public static void main(String[] args) {
        System.out.println(UsdtPrice.getUsdtToCny());
    }
}
