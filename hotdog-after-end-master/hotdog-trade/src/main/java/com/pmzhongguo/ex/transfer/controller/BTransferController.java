/**
 * zzex.com Inc.
 * Copyright (c) 2019/5/6 All Rights Reserved.
 */
package com.pmzhongguo.ex.transfer.controller;

import com.google.common.collect.Maps;
import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.transfer.entity.FundOperationLog;
import com.pmzhongguo.ex.transfer.entity.ThirdPartyInfo;
import com.pmzhongguo.ex.transfer.service.FundTransferLogManager;
import com.pmzhongguo.ex.transfer.service.ThirdPartyService;
import com.qiniu.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：yukai
 * @date ：Created in 2019/5/6 21:24
 * @description：后台划转controller
 * @version: $
 */
@ApiIgnore
@Controller
@RequestMapping("backstage/api")
public class BTransferController extends TopController
{

    @Autowired
    private ThirdPartyService thirdPartyService;

    @Autowired
    private ThirdPartyService thirdPartyInfo;

    /**
     * 第三方管理导航
     *
     * @return
     */
    @RequestMapping("toThirdParty")
    public String toThirdParty()
    {
        return "business/thirdparty/third_party_list";
    }

    /**
     * 跳转至项目方日志
     * @return
     */
    @RequestMapping("toThirdLogList")
    public String toThirdLogList()
    {   ThirdPartyInfo partyInfo = new ThirdPartyInfo();
        partyInfo.setC_flag(0);
        List<ThirdPartyInfo> thirdPartyInfoList = thirdPartyInfo.findList(partyInfo);

        $attr("info", thirdPartyInfoList);
        return "business/thirdparty/third_log_List";
    }

    @Autowired
    private FundTransferLogManager fundTransferLogManager;
    /**
     * 获取项目方划转日志
     *
     * @return
     */
    @RequestMapping(value = "getThirdLogList", method = RequestMethod.GET, consumes = "application/json")
    @ResponseBody
    public Map getThirdLogList(HttpServletRequest request)
    {
        Map param = $params(request);
        Map map = Maps.newHashMap();
        if (BeanUtil.isEmpty(param.get("tableName"))){
            return map;
        }
        param.put("tableName",(param.get("tableName")+"").toLowerCase());
        List<FundOperationLog> list = fundTransferLogManager.getFundOperationLogListByPage(param);

        map.put("Rows", list);
        map.put("Total", param.get("total"));
        return map;
    }
    /**
     * 添加项目方信息导航
     *
     * @param id
     * @return
     */
    @RequestMapping("toAddThirdParty")
    public ModelAndView thirdPartyEdit(Integer id)
    {
        ModelAndView mv = new ModelAndView("business/thirdparty/third_party_edit");
        if (id != null)
        {
            // 查询第三方信息
            ThirdPartyInfo thirdPartyInfo = thirdPartyService.selectByPrimaryKey(id);
            mv.addObject("info", thirdPartyInfo);
        }
        return mv;
    }

    /**
     * 根据ID删除第三方信息
     *
     * @return
     */
    @RequestMapping(value = "delThirdParty", method = RequestMethod.POST)
    @ResponseBody
    public Resp delThirdParty() {
        try {
            Integer id = $int("id");
            thirdPartyService.delThirdParty(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Resp(Resp.FAIL, "系统异常");
        }
        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    /**
     * 获取项目方信息
     *
     * @return
     */
    @RequestMapping(value = "listThirdParty", method = RequestMethod.GET, consumes = "application/json")
    @ResponseBody
    public Map thirdPartyList(HttpServletRequest request) {
        Map param = $params(request);
        Map map = Maps.newHashMap();
        try {
            List<ThirdPartyInfo> list = thirdPartyService.getAllThirdPartty(param);
            map.put("Rows", list);
            map.put("Total", param.get("total"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return map;
    }

    /**
     * 选择性更新或者插入数据
     *
     * @param thirdPartyInfo
     * @return
     */
    @RequestMapping(value = "addOrEditThirdParty", method = RequestMethod.POST)
    @ResponseBody
    public Resp addOrEditThirdParty(ThirdPartyInfo thirdPartyInfo) {
        try {
            int result = thirdPartyService.addOrEditThirdParty(thirdPartyInfo);
            if (result <= 0) {
                return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_DATA_ERROR.getErrorENMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Resp(Resp.FAIL, e.getMessage());
        }
        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    /**
     * 订单查询
     *
     * @return
     */
    @RequestMapping("/order/to_list_page")
    public String toThirdPartyListPage()
    {
        return "business/thirdparty/third_party_order_list";
    }

    /**
     * 获取项目方信息
     *
     * @return
     */
    @RequestMapping(value = "/order/list_data", method = RequestMethod.GET, consumes = "application/json")
    @ResponseBody
    public Map getOrderByPage(HttpServletRequest request)  {

        Map<String,Object> param = $params(request);
        if(HelpUtils.nullOrBlank(param.get("c_name"))) {
            return null;
        }
        List<Map<String,Object>> list = thirdPartyService.findMgrByPage(param);
        Map map = Maps.newHashMap();
        map.put("Rows", list);
        map.put("Total", param.get("total"));
        return map;
    }
}
