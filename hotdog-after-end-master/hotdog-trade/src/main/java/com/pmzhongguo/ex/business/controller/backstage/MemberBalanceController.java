package com.pmzhongguo.ex.business.controller.backstage;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.dto.AccountHistoryDto;
import com.pmzhongguo.ex.business.dto.AccountHistoryExportDto;
import com.pmzhongguo.ex.business.service.MemberBalanceService;
import com.pmzhongguo.ex.business.vo.CoinRechargeExcelVo;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.pmzhongguo.zzextool.utils.ExportExcel;
import com.pmzhongguo.zzextool.utils.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ApiIgnore
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("backstage/member")
public class MemberBalanceController extends TopController {

    @Autowired
    private MemberBalanceService memberBalanceService;

    /**
     * 用户历史资产资产列表界面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/member_balance_history", method = RequestMethod.GET)
    public String toListAsserts(HttpServletRequest request, HttpServletResponse response) {

        return "business/member/member_balance_history";
    }

    @RequestMapping(value = "/everyTotalBalance", method = RequestMethod.GET)
    @ResponseBody
    public Map queryBalanceHistory(HttpServletRequest request, HttpServletResponse response) {
        Map param = $params(request);
        if (!StringUtil.isNullOrBank(param.get("currency"))) {
            param.put("currency", param.get("currency").toString().toUpperCase());
        }
        List<AccountHistoryDto> accountHistoryDtos = memberBalanceService.everyTotalBalance(param);
        Map resultMap = new HashMap();
        resultMap.put("Rows", accountHistoryDtos);
        resultMap.put("Total", param.get("total"));
        return resultMap;
    }

    @RequestMapping(value = "balanceHistoryDetails", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    @ResponseBody
    public Map queryBalanceHistoryDetails(HttpServletRequest request, HttpServletResponse response) {
        String record_date = $("record_date");
        String m_name = $("m_name");
        Map map = new HashMap();
        if (record_date == null) {
            return map;
        }
        if (m_name == null) {
            return map;
        }
        map.put("startTime", record_date);
        map.put("endTime", record_date);
        map.put("m_name", m_name);
        Map<String, List<AccountHistoryDto>> resultMap = memberBalanceService.balanceHistoryDetails(map);
        return resultMap;
    }

    /**
     * 用户历史资产导出界面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "member_balance_history_export", method = RequestMethod.GET)
    public String toEditMember(HttpServletRequest request, HttpServletResponse response) {
        String id = $("id");
        String m_name = $("m_name");
        Map map = new HashMap();
        map.put("id", id);
        map.put("m_name", m_name);
        $attr("info", map);
        return "business/member/member_balance_history_export";
    }

    /**
     * 导出 充值记录
     *
     * @param request
     * @param response
     * @throws BusinessException
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "balanceHistoryExport", method = RequestMethod.GET)
    public Resp balanceHistoryExport(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Map<String, Object> param = $params(request);
        if (!StringUtil.isNullOrBank(param.get("currency"))) {
            param.put("currency", param.get("currency").toString().toUpperCase());
        }
        param.put("limit", 5000);
        return memberBalanceService.balanceHistoryExport(param, response);
    }

    /**
     * 用户合约资产界面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/member_contract_balance", method = RequestMethod.GET)
    public String toContractBalanceFace(HttpServletRequest request, HttpServletResponse response) {

        return "business/member/member_contract_balance";
    }

    /**
     * 用户合约资产
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "contractBalance", method = RequestMethod.GET)
    @ResponseBody
    public Map contractBalance(HttpServletRequest request, HttpServletResponse response) {
        Map param = $params(request);
        if (!StringUtil.isNullOrBank(param.get("currency"))) {
            param.put("currency", param.get("currency").toString().toUpperCase());
        }
        Map resultMap = new HashMap();
        resultMap.put("Rows", memberBalanceService.contractBalance(param));
        resultMap.put("Total", param.get("total"));
        return resultMap;
    }

    @RequestMapping(value = "contractBalanceExport", method = RequestMethod.GET)
    public void contractBalanceExport(HttpServletRequest request, HttpServletResponse response) {
        Map param = $params(request);
        param.put("pagesize",5000);
        memberBalanceService.contractBalanceExport(param, response);
    }
}
