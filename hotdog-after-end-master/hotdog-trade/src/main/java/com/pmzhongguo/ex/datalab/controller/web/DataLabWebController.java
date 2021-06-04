package com.pmzhongguo.ex.datalab.controller.web;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtilMember;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.datalab.contants.AccountFeeConstant;
import com.pmzhongguo.ex.datalab.contants.CurrencyAuthConstant;
import com.pmzhongguo.ex.datalab.manager.AccountFeeManager;
import com.pmzhongguo.ex.datalab.manager.CurrencyAuthManager;
import com.pmzhongguo.ex.datalab.manager.PairFreeDetailManager;
import com.qiniu.util.BeanUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/11/29 3:13 PM
 */
@Api(value = "数据实验室", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("dataLab")
public class DataLabWebController extends TopController {

    @Autowired private CurrencyAuthManager currencyAuthManager;
    @Autowired private AccountFeeManager accountFeeManager;
    @Autowired private PairFreeDetailManager pairFreeDetailManager;

    /**
     * 持仓分布
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "持仓分布", notes = "mName:会员账号,mName:会员账号,startTime:开始时间，endTime:结束时间，", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "getPositionAcountList/{ts}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp getPositionAcountList(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 校验用户是否登录
            Member member = JedisUtilMember.getInstance().getMember(request, null);
            if (null == member) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
            }
            Map<String, Object> reqMap = $params(request);
            return currencyAuthManager.getPositionAcountList(reqMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.UNDEFINE_ERROR.getErrorENMsg(), null);
        }
    }

    /**
     * 交易数据
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "交易数据", notes = "currencyPair:交易对,uid:会员id,tType：交易类型，startTime:开始时间，endTime:结束时间，", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "getTradeList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp getTradeList(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 校验用户是否登录
            Member member = JedisUtilMember.getInstance().getMember(request, null);
            if (null == member) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
            }
            Map<String, Object> reqMap = $params(request);
            return currencyAuthManager.getTradeListByPage(reqMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.UNDEFINE_ERROR.getErrorENMsg(), null);
        }
    }

    /**
     * 交易对手续费记录查询
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "交易对手续费记录查询", notes = "symbol:交易对", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "getFeePairDetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp getFeePairDetails(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 校验用户是否登录
            Member member = JedisUtilMember.getInstance().getMember(request, null);
            if (null == member) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
            }
            Map<String, Object> reqMap = $params(request);
            reqMap.put(AccountFeeConstant.memberId, member.getId());
            return pairFreeDetailManager.getPairFreeDetailListByPage(reqMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.UNDEFINE_ERROR.getErrorENMsg(), null);
        }
    }

    /**
     * 账户余额查询
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "账户余额查询", notes = "currencyPair:交易对", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "getFeeAmount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp getFeeAmount(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 校验用户是否登录
            Member member = JedisUtilMember.getInstance().getMember(request, null);
            if (null == member) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
            }
            Map<String, Object> reqMap = $params(request);
            reqMap.put(AccountFeeConstant.member, member);
            reqMap.put(AccountFeeConstant.memberId, member.getId());
            return accountFeeManager.getAccountFee(reqMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.UNDEFINE_ERROR.getErrorENMsg(), null);
        }
    }

    /**
     * 用户绑定交易对列表查询
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "用户绑定交易对列表查询", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "getFeeCurrencyPairList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp getFeeCurrencyPairList(HttpServletRequest request, HttpServletResponse response) {
        // 校验用户是否登录
        Member member = JedisUtilMember.getInstance().getMember(request, null);
//        member = memberService.getMemberById(708411);
        if (null == member) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        Map<String, Object> map = $params(request);
        if (BeanUtil.isEmpty(map.get("isFree"))) {
            map.put("isFree", 0);
        }
        map.put(CurrencyAuthConstant.memberId, member.getId());
        return currencyAuthManager.getCurrencyAuthMap(map);
    }

    /**
     * 提现功能
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = "/feeCurrencyWithdrawal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp feeCurrencyWithdrawal(HttpServletRequest request, HttpServletResponse response,
                                         @RequestBody Map<String, Object> formMap) {
        try {
            // 校验用户是否登录
            Member member = JedisUtilMember.getInstance().getMember(request, null);
            if (null == member) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
            }
            member = memberService.getMemberBy(HelpUtils.newHashMap("m_name", member.getM_name()));
            formMap.put(AccountFeeConstant.member, member);
            return accountFeeManager.doUpdateProcess(formMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.UNDEFINE_ERROR.getErrorENMsg(), null);
        }
    }
}
