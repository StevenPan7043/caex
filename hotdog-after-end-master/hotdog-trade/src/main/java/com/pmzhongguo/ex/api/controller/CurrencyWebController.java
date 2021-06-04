package com.pmzhongguo.ex.api.controller;

import com.pmzhongguo.ex.business.dto.CurrencyVerticalDto;
import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.service.CurrencyVerticalService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtilMember;
import com.pmzhongguo.ex.core.web.ChainTypeEnum;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.qiniu.util.BeanUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/11/25 1:59 PM
 */
@Api(value = "币种设置功能", description = "币种设置功能", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("m")
public class CurrencyWebController extends TopController {


    @Resource
    private CurrencyVerticalService currencyVerticalService;
    /**
     * 获取币种纵表数据
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = " 获取币种纵表信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "getCurrencyVerticalList", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getCurrencyVerticalList(HttpServletRequest request, HttpServletResponse response) {

        String currencyReq = $("currency");
        if (BeanUtil.isEmpty(currencyReq)) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.PLEASE_SELECT_CURRENCY.getErrorENMsg(), null);
        }

        String keyType = $("type");
        if (BeanUtil.isEmpty(keyType)) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.TYPE_FAIL.getErrorENMsg(), null);
        }
        if (!keyType.equals("recharge") && !keyType.equals("withdraw")) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_REQ_PARAM_ERROR.getErrorENMsg(), null);
        }
//        Member m = JedisUtilMember.getInstance().getMember(request, null);
//        if (null == m || m.getId() <= 0) {
//            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
//        }

        Map<String, Currency> currencyMap = HelpUtils.getCurrencyMap();
        if (!currencyMap.containsKey(currencyReq.toUpperCase())) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg(), null);
        }
        Currency currency = currencyMap.get(currencyReq);
        CurrencyVerticalDto currencyVerticalDto = new CurrencyVerticalDto();
        currencyVerticalDto.setCurrencyId(currency.getId());
        currencyVerticalDto.setColumn(keyType);
        List<CurrencyVerticalDto> currencyVerticalList = currencyVerticalService.getCurrencyVerticalList(currencyVerticalDto);
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(currencyVerticalList)) {
            for (CurrencyVerticalDto c : currencyVerticalList) {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("type", c.getColumn());
                resultMap.put("chainType", c.getCategoryKey());
                resultMap.put("value", c.getValue().equals("1"));
                resultMap.put("default", c.getCategoryKey().equals(ChainTypeEnum.ERC20.getType()));
                resultList.add(resultMap);
            }
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, resultList);
    }
}
