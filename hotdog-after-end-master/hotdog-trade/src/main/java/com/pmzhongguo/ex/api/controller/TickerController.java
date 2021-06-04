package com.pmzhongguo.ex.api.controller;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.service.manager.TickerManager;
import com.pmzhongguo.ex.core.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.pmzhongguo.ex.business.service.manager.TickerManager.SYMBOL_TICKER_MAP;

/**
 * @author jary
 * @creatTime 2020/5/14 11:02 AM
 */
@ApiIgnore
@Controller
@RequestMapping("api")
public class TickerController {

    @Autowired
    private TickerManager tickerManager;
    @RequestMapping(value = "v1/allticker", method = RequestMethod.GET)
    @ResponseBody
    public String listAccountsI(HttpServletRequest request,
                                HttpServletResponse response) {
        if (SYMBOL_TICKER_MAP == null || SYMBOL_TICKER_MAP.size() <= 0) {
           return JsonUtil.toJson(tickerManager.symbolTicker());
        }
        return JsonUtil.toJson(SYMBOL_TICKER_MAP);
    }
}
