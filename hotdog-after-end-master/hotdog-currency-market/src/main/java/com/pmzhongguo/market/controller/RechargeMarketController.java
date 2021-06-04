package com.pmzhongguo.market.controller;

import com.alibaba.fastjson.JSONObject;
import com.pmzhongguo.market.base.AbstractEosMarket;
import com.pmzhongguo.market.constants.InitConstant;
import com.pmzhongguo.market.entity.FollowSymbol;
import com.pmzhongguo.market.service.EOSMarketService;
import com.pmzhongguo.market.utils.*;
import com.pmzhongguo.market.web.ObjResp;
import com.pmzhongguo.market.web.Resp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author jary
 * @creatTime 2019/6/17 2:04 PM
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class RechargeMarketController extends AbstractEosMarket {



    @Autowired
    private EOSMarketService eosMarketService;

    /**
     * 获取火币网行情数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/market", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, BigDecimal[]> getTradeMarket(HttpServletRequest request, HttpServletResponse response) {
        if (BeanUtil.isEmpty(marketResp)) {
            return eosMarketService.getEosMarket();
        } else {
            return marketResp;
        }
    }

    /**
     * 获取火币网行情数据
     * /market/index/t/1589786097/sign/33faad271899878285256287c6d84904/symbol/eos_usdt
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/market/index/t/{time}/sign/{secret}/symbol/{symbol}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BigDecimal getContractMarket1(HttpServletRequest request, HttpServletResponse response,
                                         @PathVariable long time, @PathVariable String secret, @PathVariable String symbol) {
        if (contractMarket.get(symbol.toUpperCase()) == null) {
            return new BigDecimal("0");
        } else {
            return contractMarket.get(symbol.toUpperCase());
        }
    }

    /**
     * 获取火币网行情数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/contract/market", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getContractMarket(HttpServletRequest request, HttpServletResponse response) {
       return JsonUtil.bean2JsonString(contractMarket);
    }
    /**
     * 获取火币网单个交易对行情数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/contract/market/{symbolKey}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getContractMarket(HttpServletRequest request, HttpServletResponse response, @PathVariable String symbolKey) {
        Map<String, BigDecimal> symbolMarket = new HashMap<>();
        if (!BeanUtil.isEmpty(contractMarket)) {
            symbolMarket.put(symbolKey, contractMarket.get(symbolKey));
        }
        return JsonUtil.bean2JsonString(symbolMarket);
    }
    /**
     * 获取已设置的交易币种价格
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getSymbolPrice", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp getSymbolPrice(HttpServletRequest request, HttpServletResponse response) {
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, JedisUtil.getInstance().get(SYMBOL_PRICE, false));
    }

    /**
     * 设置交易币种价格
     * followSymbol  跟随交易对   启动价格  followStartPrice 倍数 mul
     * targetSymbol  目标交易对
     *
     * @return
     */
    @RequestMapping(value = "/settingSymbolPrice", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp settingSymbolPrice(HttpServletRequest request, HttpServletResponse response, FollowSymbol followSymbolReq) {
        try {

            String encryption = followSymbolReq.getEncryption();
            if (StringUtils.isEmpty(encryption)) {
                return new ObjResp(Resp.FAIL, "加密串不能为空", null);
            }
            encryption = DESEncodeUtil.decrypt(encryption);
            if (StringUtils.isEmpty(encryption)) {
                return new ObjResp(Resp.FAIL, "无效的加密串", null);
            }
            encryption = StringNetherSwap.swap(encryption);
            if (!encryption.equalsIgnoreCase(followSymbolReq.getFollowSymbol() + "+" + followSymbolReq.getMul())) {
                return new ObjResp(Resp.FAIL, "无效的加密串", null);
            }
            BigDecimal price = followSymbolReq.getFollowStartPrice();
            if (price == null || price.compareTo(BigDecimal.ZERO) == 0) {
                return new ObjResp(Resp.FAIL, "所设置的价格为空", null);
            }

            String followSymbol = followSymbolReq.getFollowSymbol();
            if (StringUtils.isBlank(followSymbol)) {
                return new ObjResp(Resp.FAIL, "所设置的跟随交易对不能为空", null);
            }
            if (!followSymbol.contains("_")) {
                return new ObjResp(Resp.FAIL, "所设置的跟随交易对格式错误", null);
            }
            followSymbolReq.setFollowSymbol(followSymbol.toUpperCase());
            if (InitConstant.MARKET_SYMBOL_LIST.contains(followSymbolReq.getFollowSymbol())) {
                return new ObjResp(Resp.FAIL, "当前交易对禁止设置", null);
            }

            String targetSymbol = followSymbolReq.getTargetSymbol();
            if (StringUtils.isBlank(targetSymbol)) {
                return new ObjResp(Resp.FAIL, "所设置的目标交易对不能为空", null);
            }
            if (!followSymbol.contains("_")) {
                return new ObjResp(Resp.FAIL, "所设置的目标交易对格式错误", null);
            }
            followSymbolReq.setTargetSymbol(targetSymbol.toUpperCase());

            BigDecimal mul = followSymbolReq.getMul();
            if (mul == null || mul.compareTo(BigDecimal.ZERO) == 0) {
                return new ObjResp(Resp.FAIL, "所设置的倍数不能为空", null);
            }

            Object symbolPriceObject = JedisUtil.getInstance().get(SYMBOL_PRICE, true);
            if (symbolPriceObject != null) {
                Map<String, Object> jsonObject = JSONObject.parseObject(symbolPriceObject.toString());
                jsonObject.put(followSymbol, followSymbolReq);
                JedisUtil.getInstance().set(SYMBOL_PRICE, JsonUtil.bean2JsonString(jsonObject), true);
            } else {
                Map<String, FollowSymbol> symbolPriceMap = new HashMap<>();
                symbolPriceMap.put(followSymbol, followSymbolReq);
                JedisUtil.getInstance().set(SYMBOL_PRICE, JsonUtil.bean2JsonString(symbolPriceMap), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ObjResp(Resp.SUCCESS, "价格设置成功", null);
    }
}
