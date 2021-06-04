package com.pmzhongguo.ex.business.controller;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.service.ApiAccessLimitService;
import com.pmzhongguo.ex.business.service.CurrencyService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.framework.entity.FrmConfig;
import com.pmzhongguo.ex.framework.service.FrmUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 服务器数据同步接口
 * @date: 2018-12-07 13:32
 * @author: 十一
 */
@ApiIgnore
@RestController
@RequestMapping("/m/sync")
public class SyncController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FrmUserService frmUserService;

    @Autowired
    private ApiAccessLimitService apiAccessLimitService;

    @Autowired
    CurrencyService currencyService;

    /**
     * 更新系统配置信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/sys-config",method = RequestMethod.GET)
    public Resp updateSysConfig(HttpServletRequest request) {
        FrmConfig oldFrmConfig = (FrmConfig)request.getServletContext().getAttribute(HelpUtils.MGRCONFIG);
        logger.info("<======= 更改系统配置信息前数据：" + oldFrmConfig.toString());
        // 从数据库中重新读取配置信息更新本地缓存
        FrmConfig frmConfig = frmUserService.findConfig();
        request.getServletContext()
                .setAttribute(HelpUtils.MGRCONFIG, frmConfig);

        apiAccessLimitService.loadApiAccessLimitCache();
        logger.info("<======= 更改系统配置信息后数据：" + frmConfig.toString());
        return new Resp(Resp.SUCCESS,Resp.SUCCESS_MSG);
    }

    /**
     * 更新交易对信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/currency-pair",method = RequestMethod.GET)
    public Resp updateCurrencyPair(HttpServletRequest request) {
        logger.info("<======= 更改交易对信息：");
        currencyService.cacheCurrencyPair(request.getServletContext());
        return new Resp(Resp.SUCCESS,Resp.SUCCESS_MSG);
    }
}
