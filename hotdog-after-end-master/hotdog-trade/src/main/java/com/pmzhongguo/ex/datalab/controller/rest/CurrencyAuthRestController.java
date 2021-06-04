package com.pmzhongguo.ex.datalab.controller.rest;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.datalab.entity.dto.CurrencyAuthDto;
import com.pmzhongguo.ex.datalab.entity.vo.CurrencyAuthVo;
import com.pmzhongguo.ex.datalab.enums.DateAuthEnum;
import com.pmzhongguo.ex.datalab.manager.AccountFeeManager;
import com.pmzhongguo.ex.datalab.manager.CurrencyAuthManager;
import com.pmzhongguo.ex.datalab.manager.PairFreeDetailManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/11/30 2:52 PM
 */
@ApiIgnore
@Controller
@RequestMapping("backstage/ca")
public class CurrencyAuthRestController extends TopController {

    @Autowired private CurrencyAuthManager currencyAuthManager;
    @Autowired private AccountFeeManager accountFeeManager;
    @Autowired private PairFreeDetailManager pairFreeDetailManager;

    /**
     * 跳转至绑定用户币种权限页面
     * @return
     */
    @RequestMapping("toCurrencyAuth")
    public String toCurrencyAuth() {
        return "business/datalab/currency_auth_list";
    }

    /**
     * 用户交易对权限 - 查询
     * @param request
     * @return
     */
    @RequestMapping(value = "getCurrencyAuthList",method = RequestMethod.GET, consumes = "application/json")
    @ResponseBody
    public Map getCurrencyAuthList(HttpServletRequest request) {
        try {
            return currencyAuthManager.getParamMap($params(request));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * add绑定用户币种权限页面
     * @return
     */
    @RequestMapping("toAddCurrencyAuth")
    public String toAddCurrencyAuth() {
        DateAuthEnum[] values = DateAuthEnum.values();
        $attr("dateAuthList", DateAuthEnum.values());
        return "business/datalab/add_currency_auth";
    }

    /**
     * add绑定用户币种权限页面
     * @return
     */
    @RequestMapping(value = "toEditCurrencyAuth",method = RequestMethod.GET)
    public String toEditCurrencyAuth(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> reqMapVo = $params(request);
        CurrencyAuthDto currencyAuthDto = currencyAuthManager.getCurrencyAuthDtoById(reqMapVo);
        CurrencyPair currencyPair = HelpUtils.getCurrencyPairMap().get(currencyAuthDto.getSymbol().toUpperCase());
        DateAuthEnum[] values = DateAuthEnum.values();
        for (DateAuthEnum dateAuthEnum : values) {
            dateAuthEnum.setFlag(false);
            for (Map map : currencyAuthDto.getAuthorityList()) {
                if (map.containsKey(dateAuthEnum.getCodeEn())) {
                    dateAuthEnum.setFlag(true);
                }
            }
        }
        $attr("dateAuthList", values);
        $attr("info", currencyAuthDto);
        $attr("currencyPair", currencyPair);
        return "business/datalab/edit_currency_auth";
    }

    /**
     * 用户交易对权限 - 编辑
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "editCurrencyAuth", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp editCurrencyAuth(HttpServletRequest request,CurrencyAuthVo currencyAuthVo) {
        try {
            Map<String, Object> reqMapVo = $params(request);
            return currencyAuthManager.edit(reqMapVo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.UNDEFINE_ERROR.getErrorENMsg(),null);
        }
    }

    /**
     * 用户交易对权限 - 新增
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "addCurrencyAuth", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp addCurrencyAuth(HttpServletRequest request,CurrencyAuthVo currencyAuthVo) {
        try {
            Map<String, Object> reqMapVo = $params(request);
            return currencyAuthManager.doSaveProcess(reqMapVo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.UNDEFINE_ERROR.getErrorENMsg(),null);
        }
    }

    /**
     * 手续费用户资产列表界面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "toAccountFeeList", method = RequestMethod.GET)
    public String toAccountFeeList(HttpServletRequest request, HttpServletResponse response) {
        return "business/datalab/account_fee_list";
    }
    /**
     * 手续费用户资产列表 - 查询
     * @param request
     * @return
     */
    @RequestMapping(value = "accountFeeList",method = RequestMethod.GET, consumes = "application/json")
    @ResponseBody
    public Map accountFeeList(HttpServletRequest request) {
        try {
            return accountFeeManager.getParamMap($params(request));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 交易对手续费记录界面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "toPairFeeDetailList", method = RequestMethod.GET)
    public String toPairFeeDetailList(HttpServletRequest request, HttpServletResponse response) {
        return "business/datalab/pair_fee_detail_list";
    }
    /**
     * 交易对手续费记录列表 - 查询
     * @param request
     * @return
     */
    @RequestMapping(value = "pairFeeDetailList",method = RequestMethod.GET, consumes = "application/json")
    @ResponseBody
    public Map pairFeeDetailList(HttpServletRequest request) {
        try {
            return pairFreeDetailManager.getParamMap($params(request));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
