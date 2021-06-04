package com.pmzhongguo.ex.api.controller;

import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(value = "交易中心", description = "交易规则", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/tc")
public class TradeCentreController extends TopController {

    @ApiOperation(value = "获取交易规则", notes = "获取如抢购规则,symbol:交易对名", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/tradeRule",method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getTradeRule(HttpServletRequest request){
        String currencySymbol = $("symbol");
        Map<String, CurrencyPair> currencyPairMap = HelpUtils.getCurrencyPairMap();
        CurrencyPair cp = currencyPairMap.get(currencySymbol.toUpperCase());
        if (cp == null){
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.PLEASE_SELECT_CURRENCY.getErrorCNMsg(),null);
        }
        Map<String,Object> info = HelpUtils.newHashMap("symbol", currencySymbol);
        if (cp.getIs_flash_sale_open() == 1 && cp.getP_status() == 1) {
            info.put("fixed_buy_price", cp.getFixed_buy_price());
        }else {
            info.put("fixed_buy_price", 0);
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, info);
    }
}
