package com.contract.cms.controller;

import com.alibaba.fastjson.JSONObject;
import com.contract.cms.common.ExportExcel;
import com.contract.cms.common.MappingUtils;
import com.contract.common.FunctionUtils;
import com.contract.common.GetRest;
import com.contract.common.RestResponse;
import com.contract.common.mail.HelpUtils;
import com.contract.dto.ReportDto;
import com.contract.entity.*;
import com.contract.exception.ThrowJsonException;
import com.contract.service.api.ApiTradeService;
import com.contract.service.cms.OrderService;
import com.contract.service.cms.PlatSession;
import com.contract.service.cms.RoleService;
import com.contract.service.redis.RedisUtilsService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisUtilsService redisUtilsService;
    @Autowired
    private ApiTradeService apiTradeService;

    /**
     * 全仓列表
     *
     * @param order
     * @param request
     * @return
     */
    @RequestMapping(value = MappingUtils.showOrderList)
    public ModelAndView showOrderList(CContractOrder order, HttpServletRequest request) {
        Integer roleid = PlatSession.getRoleId(request);
        Integer userid = PlatSession.getUserId(request);
        if (!FunctionUtils.isEquals(1, roleid)) {
            order.setUserid(PlatSession.getUserId(request));
        }
        List<MtCliqueUser> users = roleService.queryUserList(request);
        List<CContractOrder> list = orderService.queryOrderList(order);
        for (CContractOrder cContractOrder : list) {
            if (cContractOrder.getStatus() == 2) {
                continue;
            }
            if (cContractOrder.getStatus() == 1) {
                String symbol = cContractOrder.getCoin().replace("_", "/").toUpperCase();
                String allKeyMoeny = "order_" + cContractOrder.getCid() + "_" + symbol + "_" + cContractOrder.getOrdercode();
                String key = redisUtilsService.getKey(allKeyMoeny);
                CContractOrder redisContractOrder = JSONObject.parseObject(key, CContractOrder.class);
                if (redisContractOrder == null) {
                    continue;
                }
                cContractOrder.setRates(redisContractOrder.getRates());
            }
        }
        List<Coins> coins = redisUtilsService.queryCoins();
//		ReportDto map=orderService.getOrdermoney(order);
        PageInfo<CContractOrder> pageInfo = new PageInfo<CContractOrder>(list);
        ModelAndView view = new ModelAndView(MappingUtils.showOrderList);
        view.addObject("order", order);
        view.addObject("pageInfo", pageInfo);
//		view.addObject("map", map);
        view.addObject("users", users);
        view.addObject("roleid", roleid);
        view.addObject("userid", userid);
        view.addObject("coins", coins);
        return view;
    }

    @RequestMapping(value = MappingUtils.showReport)
    public ModelAndView showReport(CContractOrder order) {
        ReportDto map = orderService.getOrderReport(order);
        ModelAndView view = new ModelAndView(MappingUtils.showReport);
        view.addObject("order", order);
        view.addObject("map", map);
        return view;
    }

    @RequestMapping(value = MappingUtils.showEntrustList)
    public ModelAndView showEntrustList(HttpServletRequest request, CEntrustOrder cEntrustOrder) {
        Integer roleid = PlatSession.getRoleId(request);
        if (!FunctionUtils.isEquals(1, roleid)) {
            cEntrustOrder.setUserid(PlatSession.getUserId(request));
        }
        List<MtCliqueUser> users = roleService.queryUserList(request);
        List<Coins> coins = redisUtilsService.queryCoins();
        List<CEntrustOrder> list = orderService.queryEntrustList(cEntrustOrder);
        PageInfo<CEntrustOrder> pageInfo = new PageInfo<CEntrustOrder>(list);
        ModelAndView view = new ModelAndView(MappingUtils.showEntrustList);
        view.addObject("order", cEntrustOrder);
        view.addObject("pageInfo", pageInfo);
        view.addObject("users", users);
        view.addObject("roleid", roleid);
        view.addObject("coins", coins);
        return view;
    }

    /**
     * 逐仓列表
     *
     * @param zcOrder
     * @param request
     * @return
     */
    @RequestMapping(value = MappingUtils.showZcList)
    public ModelAndView showZcList(CZcOrder zcOrder, HttpServletRequest request) {
        Integer roleid = PlatSession.getRoleId(request);
        if (!FunctionUtils.isEquals(1, roleid)) {
            zcOrder.setUserid(PlatSession.getUserId(request));
        }
        List<MtCliqueUser> users = roleService.queryUserList(request);
        List<CZcOrder> list = orderService.queryZcList(zcOrder);
        for (CZcOrder cZcOrder : list) {
            if (cZcOrder.getStatus() == 2) {
                continue;
            }
            if (cZcOrder.getStatus() == 1) {
                String symbol = cZcOrder.getCoin().replace("_", "/").toUpperCase();
                String allKeyMoeny = "zc_" + cZcOrder.getCid() + "_" + symbol + "_" + cZcOrder.getOrdercode();
                String key = redisUtilsService.getKey(allKeyMoeny);
                CZcOrder redisCZcOrder = JSONObject.parseObject(key, CZcOrder.class);
                if (redisCZcOrder == null) {
                    continue;
                }
                cZcOrder.setReward(redisCZcOrder.getReward());
            }
        }
        List<Coins> coins = redisUtilsService.queryCoins();
        ReportDto map = orderService.getZCmoney(zcOrder);
        PageInfo<CZcOrder> pageInfo = new PageInfo<CZcOrder>(list);
        ModelAndView view = new ModelAndView(MappingUtils.showZcList);
        view.addObject("order", zcOrder);
        view.addObject("pageInfo", pageInfo);
        view.addObject("map", map);
        view.addObject("users", users);
        view.addObject("roleid", roleid);
        view.addObject("coins", coins);
        return view;
    }

    /**
     * 获取request所有的参数
     */
    public static Map<String, Object> $params(HttpServletRequest request) {
        return $params(false, request);
    }

    public static Map<String, Object> $params(boolean putUserInfo,
                                              HttpServletRequest request) {
        Map<String, Object> params = WebUtils.getParametersStartingWith(
                request, "");
        return params;
    }

    @RequestMapping(value = "/contract/export", method = RequestMethod.GET)
    @ResponseBody
    public void contractExport(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> map = $params(request);
        String fileName = "后台合约报表-" + HelpUtils.formatDateByFormatStr(new Date(), "yyyyMMddHHmmss");
        ExportExcel excel = new ExportExcel(fileName, ContractExportDto.class);
        List<ContractExportDto> list = orderService.getContractExport(map);
        excel.setDataList(list);
        excel.write(response, fileName + ".xls");
    }

    @RequestMapping(value = MappingUtils.handleCloseOrder)
    @ResponseBody
    public RestResponse handleCloseOrder(String ordercode) {

        if (StringUtils.isEmpty(ordercode)) {
            return GetRest.getFail("未找到订单号");
        }
        Map<String, Object> map = new HashedMap();
        map.put("ordercode", ordercode);
        List<CContractOrder> orderList = orderService.selectContractOrder(map);
        if (orderList.size() <= 0) {
            throw new ThrowJsonException("订单不存在");
        }

        apiTradeService.handleCloseOrder(orderList.get(0), null, "自动平仓", false);
        return GetRest.getSuccess("成功");
    }
}
