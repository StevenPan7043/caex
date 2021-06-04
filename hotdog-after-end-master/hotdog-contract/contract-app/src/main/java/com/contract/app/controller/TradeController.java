package com.contract.app.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.contract.app.task.WalletTask;
import com.contract.common.GetRest;
import com.contract.common.HttpUtil;
import com.contract.common.HttpUtils;
import com.contract.common.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.contract.app.common.MappingUtil;
import com.contract.entity.CContractOrder;
import com.contract.entity.CEntrustOrder;
import com.contract.entity.CZcOrder;
import com.contract.entity.Coins;
import com.contract.enums.GearEnums;
import com.contract.enums.ZCMoneyEnums;
import com.contract.service.api.ApiTradeService;
import com.contract.service.redis.RedisUtilsService;

import javax.servlet.http.HttpServletRequest;


@Controller
public class TradeController {


    @Autowired
    private ApiTradeService apiTradeService;

    @Autowired
    private RedisUtilsService redisUtilsService;

    @Autowired
    private WalletTask walletTask;

    @RequestMapping(value = MappingUtil.queryCoins)
    @ResponseBody
    public RestResponse queryCoins() {
        List<Coins> coins = redisUtilsService.queryCoins();
        return GetRest.getSuccess("成功", coins);
    }

    @RequestMapping(value = MappingUtil.queryGearing)
    @ResponseBody
    public RestResponse queryGearing() {
        List<Integer> enums = GearEnums.queryGearing();
        return GetRest.getSuccess("成功", enums);
    }

    @RequestMapping(value = MappingUtil.queryZCmoney)
    @ResponseBody
    public RestResponse queryZCmoney() {
        List<BigDecimal> enums = ZCMoneyEnums.getMoneys();
        return GetRest.getSuccess("成功", enums);
    }

    /**
     * 查询订单
     *
     * @param token
     * @param order
     * @return
     */
    @RequestMapping(value = MappingUtil.queryContractOrder)
    @ResponseBody
    public RestResponse queryContractOrder(String token, CContractOrder order) {
        RestResponse result = apiTradeService.queryContractOrder(token, order);
        return result;
    }

    @RequestMapping(value = MappingUtil.queryZCorder)
    @ResponseBody
    public RestResponse queryZCorder(String token, CZcOrder zcOrder) {
        RestResponse result = apiTradeService.queryZCorder(token, zcOrder);
        return result;
    }

    /**
     * 处理合约下单
     *
     * @param token
     * @param type    1开多  2开空
     * @param coinnum 持仓量
     * @param gearing 杠杆倍数
     * @param symbol  交易对 btc_usdt eth_usdt等等
     * @param time    持仓时长 单位s
     * @return
     */
    @RequestMapping(value = MappingUtil.handlContractOrder)
    @ResponseBody
    public RestResponse handlContractOrder(String token, Integer type, BigDecimal coinnum, Integer gearing, String symbol, BigDecimal usdtPrice, Integer time) {
        String a = "stop";
        if(a.equals("stop")){
            return GetRest.getFail("暂停交易");
        }
        if(time != null){
            gearing = 1;
        }
        if (type == null) {
            return GetRest.getFail("请选择开多/开空");
        }
        if (coinnum == null || coinnum.compareTo(BigDecimal.ZERO) <= 0) {
            return GetRest.getFail("请填写持仓量");
        }
        if (coinnum.toPlainString().indexOf(".") > -1) {
            return GetRest.getFail("持仓量只能为整数");
        }
        if (StringUtils.isEmpty(symbol)) {
            return GetRest.getFail("请选择货币类型");
        }
        //time == null 表示合约正常下单
        if(time == null){
            time = 1000;
        }
        if (gearing == null) {
            return GetRest.getFail("请选择杠杆倍数");
        }
        boolean flag = GearEnums.isExist(gearing);
        if (!flag) {
            return GetRest.getFail("杠杆倍数错误");
        }
        RestResponse result = apiTradeService.handleContractOrder(token, type, coinnum, gearing, symbol, usdtPrice, time);
        return result;
    }

    /**
     * 修改 止盈 止损
     *
     * @param token
     * @param ordercode
     * @param stopwin   止盈金额
     * @param stopdonat 止损金额
     * @return
     */
    @RequestMapping(value = MappingUtil.handleEditOrder)
    @ResponseBody
    public RestResponse handleEditOrder(String token, String ordercode, BigDecimal stopwin, BigDecimal stopdonat) {

        if (StringUtils.isEmpty(ordercode)) {
            return GetRest.getFail("订单不存在");
        }
        if (stopwin == null || stopwin.compareTo(BigDecimal.ZERO) <= 0) {
            return GetRest.getFail("止盈价格不得小于0");
        }
        if (stopdonat == null || stopdonat.compareTo(BigDecimal.ZERO) <= 0) {
            return GetRest.getFail("止损价格不得小于0");
        }
        RestResponse result = apiTradeService.handleEditOrder(token, ordercode, stopwin, stopdonat);
        return result;
    }

    /**
     * 处理平仓
     *
     * @param token
     * @param ordercode 订单号
     * @return
     */
    @RequestMapping(value = MappingUtil.handleCloseOrder)
    @ResponseBody
    public RestResponse handleCloseOrder(String token, String ordercode) {
        RestResponse result = apiTradeService.validCloseOrder(token, ordercode);
        return result;
    }

    /**
     * 查询我的委托
     *
     * @return
     */
    @RequestMapping(value = MappingUtil.queryMyEntrust)
    @ResponseBody
    public RestResponse queryMyEntrust(String token, CEntrustOrder entrustOrder) {
        RestResponse result = apiTradeService.queryMyEntrust(token, entrustOrder);
        return result;
    }

    /**
     * 处理委托
     *
     * @param token
     * @param type
     * @param coinnum
     * @param gearing
     * @param symbol
     * @param price
     * @param time    持仓时长 单位s
     * @return
     */
    @RequestMapping(value = MappingUtil.handleEntrust)
    @ResponseBody
    public RestResponse handleEntrust(String token, Integer type, BigDecimal coinnum, Integer gearing, String symbol, BigDecimal price, Integer time) {
        String a = "stop";
        if(a.equals("stop")){
            return GetRest.getFail("暂停交易");
        }
        if (type == null) {
            return GetRest.getFail("请选择开多/开空");
        }
        if (coinnum == null || coinnum.compareTo(BigDecimal.ZERO) <= 0) {
            return GetRest.getFail("请填写持仓量");
        }
        if (coinnum.toPlainString().indexOf(".") > -1) {
            return GetRest.getFail("持仓量只能为整数");
        }
        if (StringUtils.isEmpty(symbol)) {
            return GetRest.getFail("请选择货币类型");
        }
        if (gearing == null) {
            return GetRest.getFail("请选择杠杆倍数");
        }
        boolean flag = GearEnums.isExist(gearing);
        if (!flag) {
            return GetRest.getFail("杠杆倍数错误");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            return GetRest.getFail("请填写委托价");
        }
        if (time == null) {
            return GetRest.getFail("请选择持仓时间");
        }
        RestResponse result = apiTradeService.handleEntrust(token, type, coinnum, gearing, symbol, price, time);
        return result;
    }

    /**
     * 取消
     *
     * @param token
     * @param ordercode
     * @return
     */
    @RequestMapping(value = MappingUtil.handleCancelEntrust)
    @ResponseBody
    public RestResponse handleCancelEntrust(String token, String ordercode) {
        RestResponse result = apiTradeService.handleCancelEntrust(token, ordercode);
        return result;
    }

    /**
     * 处理逐仓下单
     *
     * @param token
     * @param price
     * @param type    1开多  2开空
     * @param coinnum 持仓量
     * @param symbol  交易对 btc_usdt eth_usdt等等
     * @return
     */
    @RequestMapping(value = MappingUtil.handlZCOrder)
    @ResponseBody
    public RestResponse handlZCOrder(String token, Integer type, BigDecimal coinnum, BigDecimal price, String symbol) {

        String a = "stop";
        if(a.equals("stop")){
            return GetRest.getFail("暂停交易");
        }
        if (type == null) {
            return GetRest.getFail("请选择开多/开空");
        }
        if (coinnum == null || coinnum.compareTo(BigDecimal.ZERO) <= 0) {
            return GetRest.getFail("请填写持仓量");
        }
        if (coinnum.toPlainString().indexOf(".") > -1) {
            return GetRest.getFail("持仓量只能为整数");
        }
        if (StringUtils.isEmpty(symbol)) {
            return GetRest.getFail("请选择货币类型");
        }
        if (price == null) {
            return GetRest.getFail("请选择逐仓金额");
        }
        BigDecimal[] bigDecimals = price.divideAndRemainder(new BigDecimal(100));
        if (bigDecimals[1].compareTo(BigDecimal.ZERO) != 0) {
            return GetRest.getFail("逐仓金额错误,必须为100的整数倍");
        }
        RestResponse result = apiTradeService.handleZCOrder(token, type, coinnum, price, symbol);
        return result;
    }

    /**
     * 设置逐仓止盈止损
     *
     * @param token
     * @param ordercode 订单号
     * @param stopwin   止盈比例 25% 50% 75% 100%
     * @param stopfail  止损比例 25% 50% 75% 100%
     * @return
     */
    @RequestMapping(value = MappingUtil.handleZCStop)
    @ResponseBody
    public RestResponse handleZCStop(String token, String ordercode, BigDecimal stopwin, BigDecimal stopfail) {

        if (stopwin == null && stopfail == null) {
            return GetRest.getFail("请选择止盈或止损");
        }
//        if (stopwin.compareTo(BigDecimal.ZERO) < 0 || stopfail.compareTo(BigDecimal.ZERO) < 0) {
//            return GetRest.getFail("请选择正确止盈或止损");
//        }
//        if (stopwin.compareTo(BigDecimal.ONE) > 0 || stopfail.compareTo(BigDecimal.ONE) > 0) {
//            return GetRest.getFail("请选择正确止盈或止损");
//        }
        RestResponse result = apiTradeService.handleZCStop(token, ordercode, stopwin, stopfail);
        return result;
    }

    /**
     * 处理平仓 逐仓订单
     *
     * @param token
     * @param ordercode
     * @return
     */
    @RequestMapping(value = MappingUtil.handleCloseZc)
    @ResponseBody
    public RestResponse handleCloseZc(String token, String ordercode) {
        RestResponse result = apiTradeService.handleCloseZc(token, ordercode);
        return result;
    }

    @RequestMapping(value = MappingUtil.historyKline)
    @ResponseBody
    public RestResponse getHistoryKline(String contract_code, String period, String from, String to) {
        Map<String, String> map = new HashMap<>();
        map.put("contract_code", contract_code);
        map.put("period", period);
        map.put("from", from);
        map.put("to", to);
        String result = HttpUtils.get("https://futures.huobi.me/swap-ex/market/history/kline", map);
        return GetRest.getSuccess("成功", result);
    }


    /**
     * 根据条件查询订单
     *
     * @param token
     * @param type  1盈利订单 2成交订单
     * @return
     */
    @RequestMapping(value = MappingUtil.queryContractOrderByType)
    @ResponseBody
    public RestResponse queryContractOrderByType(String token, Integer type) {
        RestResponse result = apiTradeService.queryContractOrderByType(token, type);
        return result;
    }

    /**
     * 跟单交易
     * @param token
     * @param type
     * @return
     */
    @RequestMapping(value = MappingUtil.documentary)
    @ResponseBody
    public RestResponse documentary(String token, Integer type) {
        RestResponse result = apiTradeService.documentary(token, type);
        return result;
    }


    /**
     * 根据memberId查询用户历史订单
     */
    @RequestMapping(value = MappingUtil.gdHistory)
    @ResponseBody
    public RestResponse gdHistory(Integer memberId){
        return apiTradeService.gdHistory(memberId);
    }

    /**
     * 查询账户资金  累计收益  收益明细
     * @param token
     * @return
     */
    @RequestMapping(value = MappingUtil.assets)
    @ResponseBody
    public RestResponse assets(String token,int page){

        return apiTradeService.assets(token,page);
    }

    /**
     * 转入
     * @return
     */
    @RequestMapping(value = MappingUtil.into)
    @ResponseBody
    public RestResponse into(String token,Integer isout,Integer page){
            return apiTradeService.into(token,isout,page);
    }

    @RequestMapping("/testauto")
    @ResponseBody
    public void testauto(){
        walletTask.autoCycle();
    }
}
