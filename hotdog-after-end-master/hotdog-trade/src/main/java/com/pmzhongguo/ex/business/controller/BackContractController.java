package com.pmzhongguo.ex.business.controller;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.contract.dto.UsdtTransferDto;
import com.pmzhongguo.contract.entity.MtCliqueUser;
import com.pmzhongguo.ex.business.service.ContractService;
import com.pmzhongguo.ex.core.web.TopController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2020/4/16 10:19 AM
 */
@ApiIgnore
@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("backstage/contract")
public class BackContractController extends TopController {
    @Autowired
    private ContractService contractService;
    /**
     * usdt划转列表界面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "toUsdtTransferList", method = RequestMethod.GET)
    public String toUsdtTransferList(HttpServletRequest request, HttpServletResponse response) {
        List<MtCliqueUser> mtCliqueUsers = contractService.querySysUsers(null);
        $attr("contractSysUsers", mtCliqueUsers);
        return "business/contract/showUsdtTransferList";
    }
    /**
     * usdt划转列表
     * @param request
     * @return
     */
    @RequestMapping(value = "showUsdtTransferList", method = RequestMethod.GET)
    @ResponseBody
    public Map showUsdtTransferList(HttpServletRequest request) {
        Map param = $params(request);
        List<UsdtTransferDto> usdtTransferDtos = contractService.queryUsdtTransferMapByPage(param);
        BigDecimal sumCost = contractService.queryUsdtTransferSum(param);
        Map map = new HashMap();
        param.put("sumCost", sumCost == null ? BigDecimal.ZERO : sumCost);
        map.put("Rows", usdtTransferDtos);
        map.put("Total", param.get("total"));
        return map;
    }

    /**
     * usdt划转列表界面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "toUsdtTransferTeam", method = RequestMethod.GET)
    public String toUsdtTransferTeam(HttpServletRequest request, HttpServletResponse response) {
        List<MtCliqueUser> mtCliqueUsers = contractService.querySysUsers(null);
        $attr("contractSysUsers", mtCliqueUsers);
        return "business/contract/showUsdtTeamList";
    }
    /**
     * 子公司划转统计列表
     * @param request
     * @return
     */
    @RequestMapping(value = "showUsdtTransferTeam", method = RequestMethod.GET)
    @ResponseBody
    public Map showUsdtTransferTeam(HttpServletRequest request) {
        Map param = $params(request);
        List<UsdtTransferDto> usdtTransferDtos = contractService.queryUsdtTransferTeam(param);
        Map map = new HashMap();
        map.put("Rows", usdtTransferDtos);
        map.put("Total", param.get("total"));
        return map;
    }
}
