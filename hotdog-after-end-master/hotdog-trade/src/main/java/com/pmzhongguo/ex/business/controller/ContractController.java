package com.pmzhongguo.ex.business.controller;

import com.contract.dto.MoneyDto;
import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.contract.contractenum.ContractOperateTypeEnum;
import com.pmzhongguo.contract.dto.ContractAccountDto;
import com.pmzhongguo.contract.dto.UsdtTransferDto;
import com.pmzhongguo.ex.business.service.ContractService;
import com.pmzhongguo.ex.business.service.manager.ContractAccountDetailManager;
import com.pmzhongguo.ex.business.service.manager.ContractAccountManager;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.service.manager.CustomerManager;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtilMember;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.validate.transfer.inter.AccountValidate;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiIgnore
@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("contract")
public class ContractController extends TopController {

    @Autowired
    private ContractAccountManager contractAccountManager;

    @Autowired
    private ContractAccountDetailManager contractAccountDetailManager;

    @Autowired
    private CustomerManager customerManager;

    @Autowired
    private AccountValidate accountValidate;

    /**
     * 从币币区转到全仓区
     * @param request
     * @param response
     * @param memberId
     * @param currency
     * @param num
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = "balance/addChange/{memberId}/{num}/{currency}", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp addChange(HttpServletRequest request,
                             HttpServletResponse response, @PathVariable Integer memberId, @PathVariable String currency, @PathVariable BigDecimal num) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
            return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
        }
        if(memberId.intValue() != m.getId().intValue()) {
            return new ObjResp(Resp.FAIL, "LANG_ERROR_OPERATION_OBJECT", null);
        }
        currency = currency.toUpperCase();
        if(!"USDT".equals(currency)) {
            return new ObjResp(Resp.FAIL, "ERROR_CURRENCY", null);
        }
        if(accountValidate.isVirtualAccount(m.getId())){
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.CONTRACT_VA_OPERATION_LIMIT.getErrorENMsg(), null);
        }
        contractAccountManager.accountTransfer(memberId, currency, num, ContractOperateTypeEnum.ADDBALANCE);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }


    /**
     * 从全仓区转到币币区
     * @param request
     * @param response
     * @param memberId
     * @param currency
     * @param num
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = "balance/subtChange/{memberId}/{num}/{currency}", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp subtChange( HttpServletRequest request,
                               HttpServletResponse response, @PathVariable Integer memberId, @PathVariable String currency, @PathVariable BigDecimal num) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
            return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
        }
        if(memberId.intValue() != m.getId().intValue()) {
            return new ObjResp(Resp.FAIL, "LANG_ERROR_OPERATION_OBJECT", null);
        }
        currency = currency.toUpperCase();
        if(!"USDT".equals(currency)) {
            return new ObjResp(Resp.FAIL, "ERROR_CURRENCY", null);
        }
        if (contractAccountManager.checkOrderExist(memberId)){
            return new ObjResp(Resp.FAIL, "LANG_EXIST_ORDER", null);
        }
        if(accountValidate.isVirtualAccount(m.getId())){
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.CONTRACT_VA_OPERATION_LIMIT.getErrorENMsg(), null);
        }
        contractAccountManager.accountTransfer(memberId, currency, num.negate(), ContractOperateTypeEnum.OUTBALNCE);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }



    /**
     * 从币币区转到逐仓区
     * @param request
     * @param response
     * @param memberId
     * @param currency
     * @param num
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = "zcBalance/addChange/{memberId}/{num}/{currency}", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp addZcChange(HttpServletRequest request,
                             HttpServletResponse response, @PathVariable Integer memberId, @PathVariable String currency, @PathVariable BigDecimal num) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
            return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
        }
        if(memberId.intValue() != m.getId().intValue()) {
            return new ObjResp(Resp.FAIL, "LANG_ERROR_OPERATION_OBJECT", null);
        }
        currency = currency.toUpperCase();
        if(!"USDT".equals(currency)) {
            return new ObjResp(Resp.FAIL, "ERROR_CURRENCY", null);
        }
        if(accountValidate.isVirtualAccount(m.getId())){
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.CONTRACT_VA_OPERATION_LIMIT.getErrorENMsg(), null);
        }
        contractAccountManager.accountTransfer(memberId, currency, num, ContractOperateTypeEnum.ADDZCBALANCE);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }


    /**
     * 从逐仓区转到币币区
     * @param request
     * @param response
     * @param memberId
     * @param currency
     * @param num
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = "zcBalance/subtChange/{memberId}/{num}/{currency}", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp subtZcChange( HttpServletRequest request,
                               HttpServletResponse response, @PathVariable Integer memberId, @PathVariable String currency, @PathVariable BigDecimal num) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
            return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
        }
        if(memberId.intValue() != m.getId().intValue()) {
            return new ObjResp(Resp.FAIL, "LANG_ERROR_OPERATION_OBJECT", null);
        }
        currency = currency.toUpperCase();
        if(!"USDT".equals(currency)) {
            return new ObjResp(Resp.FAIL, "ERROR_CURRENCY", null);
        }
        if(accountValidate.isVirtualAccount(m.getId())){
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.CONTRACT_VA_OPERATION_LIMIT.getErrorENMsg(), null);
        }
        contractAccountManager.accountTransfer(memberId, currency, num.negate(), ContractOperateTypeEnum.OUTZCBALANCE);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }




    /**
     * 从法币区转到全仓区
     * @param request
     * @param response
     * @param memberId
     * @param currency
     * @param num
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = "balance/otcAddChange/{memberId}/{num}/{currency}", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp otcAddChange(HttpServletRequest request,
                             HttpServletResponse response, @PathVariable Integer memberId, @PathVariable String currency, @PathVariable BigDecimal num) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        if(memberId.intValue() != m.getId().intValue()) {
            return new ObjResp(Resp.FAIL, "LANG_ERROR_OPERATION_OBJECT", null);
        }
        currency = currency.toUpperCase();
        if(!"USDT".equals(currency)) {
            return new ObjResp(Resp.FAIL, "ERROR_CURRENCY", null);
        }
        if(accountValidate.isVirtualAccount(m.getId())){
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.CONTRACT_VA_OPERATION_LIMIT.getErrorENMsg(), null);
        }
        contractAccountManager.otcAccountTransfer(memberId, currency, num, ContractOperateTypeEnum.OTCADDBALANCE);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }

    /**
     * 从全仓区转到法币区
     * @param request
     * @param response
     * @param memberId
     * @param currency
     * @param num
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = "balance/otcSubtChange/{memberId}/{num}/{currency}", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp otcsubtChange( HttpServletRequest request,
                               HttpServletResponse response, @PathVariable Integer memberId, @PathVariable String currency, @PathVariable BigDecimal num) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        if(memberId.intValue() != m.getId().intValue()) {
            return new ObjResp(Resp.FAIL, "LANG_ERROR_OPERATION_OBJECT", null);
        }
        currency = currency.toUpperCase();
        if(!"USDT".equals(currency)) {
            return new ObjResp(Resp.FAIL, "ERROR_CURRENCY", null);
        }
        if (contractAccountManager.checkOrderExist(memberId)){
            return new ObjResp(Resp.FAIL, "LANG_EXIST_ORDER", null);
        }

        /**
         *  检查剩余资产是否大于等于5 USDT
         *  added by Daily on 2002/7/2 13:41
         */
        if(!accountValidate.isTransfer(m.getId(), currency, num)){
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.INSUFFICIENT_RESIDUAL_ASSETS.getErrorENMsg(), null);
        }
        if(accountValidate.isVirtualAccount(m.getId())){
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.CONTRACT_VA_OPERATION_LIMIT.getErrorENMsg(), null);
        }
        contractAccountManager.otcAccountTransfer(memberId, currency, num.negate(), ContractOperateTypeEnum.OTCOUTBALANCE);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }




    /**
     * 从法币区转到逐仓区
     * @param request
     * @param response
     * @param memberId
     * @param currency
     * @param num
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = "zcBalance/otcAddChange/{memberId}/{num}/{currency}", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp otcAddZcChange(HttpServletRequest request,
                                HttpServletResponse response, @PathVariable Integer memberId, @PathVariable String currency, @PathVariable BigDecimal num) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        if(memberId.intValue() != m.getId().intValue()) {
            return new ObjResp(Resp.FAIL, "LANG_ERROR_OPERATION_OBJECT", null);
        }
        currency = currency.toUpperCase();
        if(!"USDT".equals(currency)) {
            return new ObjResp(Resp.FAIL, "ERROR_CURRENCY", null);
        }
        if(accountValidate.isVirtualAccount(m.getId())){
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.CONTRACT_VA_OPERATION_LIMIT.getErrorENMsg(), null);
        }
        contractAccountManager.otcAccountTransfer(memberId, currency, num, ContractOperateTypeEnum.OTCADDZCBALANCE);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }

    /**
     * 从逐仓区转到法币区
     * @param request
     * @param response
     * @param memberId
     * @param currency
     * @param num
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = "zcBalance/otcSubtChange/{memberId}/{num}/{currency}", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp otcsubtZcChange( HttpServletRequest request,
                                  HttpServletResponse response, @PathVariable Integer memberId, @PathVariable String currency, @PathVariable BigDecimal num) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        if(memberId.intValue() != m.getId().intValue()) {
            return new ObjResp(Resp.FAIL, "LANG_ERROR_OPERATION_OBJECT", null);
        }
        currency = currency.toUpperCase();
        if(!"USDT".equals(currency)) {
            return new ObjResp(Resp.FAIL, "ERROR_CURRENCY", null);
        }
        /**
         *  检查剩余资产是否大于等于5 USDT
         *  added by Daily on 2002/7/2 13:41
         */
        if(!accountValidate.isTransfer(m.getId(), currency, num)){
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.INSUFFICIENT_RESIDUAL_ASSETS.getErrorENMsg(), null);
        }
        if(accountValidate.isVirtualAccount(m.getId())){
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.CONTRACT_VA_OPERATION_LIMIT.getErrorENMsg(), null);
        }
        contractAccountManager.otcAccountTransfer(memberId, currency, num.negate(), ContractOperateTypeEnum.OTCOUTZCBALANCE);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }


    /**
     * 从全仓区转到逐仓区
     * @param request
     * @param response
     * @param memberId
     * @param currency
     * @param num
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = "balance/handleTrans/{memberId}/{num}/{currency}", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp handleTransBalance(HttpServletRequest request,
                               HttpServletResponse response, @PathVariable Integer memberId, @PathVariable String currency, @PathVariable BigDecimal num) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        if(memberId.intValue() != m.getId().intValue()) {
            return new ObjResp(Resp.FAIL, "LANG_ERROR_OPERATION_OBJECT", null);
        }
        currency = currency.toUpperCase();
        if(!"USDT".equals(currency)) {
            return new ObjResp(Resp.FAIL, "ERROR_CURRENCY", null);
        }
        if (contractAccountManager.checkOrderExist(memberId)){
            return new ObjResp(Resp.FAIL, "LANG_EXIST_ORDER", null);
        }
        if(accountValidate.isVirtualAccount(m.getId())){
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.CONTRACT_VA_OPERATION_LIMIT.getErrorENMsg(), null);
        }
        contractAccountManager.assetChange(memberId, currency, num, ContractOperateTypeEnum.HANDLETRANSBALANCE);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }

    /**
     * 从逐仓区转到全仓区
     * @param request
     * @param response
     * @param memberId
     * @param currency
     * @param num
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = "zcBalance/handleTrans/{memberId}/{num}/{currency}", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp handleTransZcBalance(HttpServletRequest request,
                               HttpServletResponse response, @PathVariable Integer memberId, @PathVariable String currency, @PathVariable BigDecimal num) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        if(memberId.intValue() != m.getId().intValue()) {
            return new ObjResp(Resp.FAIL, "LANG_ERROR_OPERATION_OBJECT", null);
        }
        currency = currency.toUpperCase();
        if(!"USDT".equals(currency)) {
            return new ObjResp(Resp.FAIL, "ERROR_CURRENCY", null);
        }
        if(accountValidate.isVirtualAccount(m.getId())){
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.CONTRACT_VA_OPERATION_LIMIT.getErrorENMsg(), null);
        }
        contractAccountManager.assetChange(memberId, currency, num, ContractOperateTypeEnum.HANDLETRANSZCBALANCE);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }

    @ApiOperation(value = "查询会员合约账户操作明细", notes = "输入字段： 会员ID:memberId  币种:currency ", httpMethod = "GET")
    @RequestMapping(value = "getMemberDetailsPage", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getMemberDetailsPage( HttpServletRequest request,
                                         HttpServletResponse response) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
          return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        Map<String, Object> params = $params(request);
        params.put("memberId", m.getId());
        if(HelpUtils.isMapNullValue(params, "sortname")) {
            params.put("sortname", "createtime");
            params.put("sortorder", "desc");
        }
        List<MoneyDto> list = contractAccountDetailManager.selectUsdtPage(params);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, HelpUtils.newHashMap("rows", list, "total", params.get("total")));
    }

    @ApiOperation(value = "查询会员合约账户操作明细", notes = "输入字段： 会员ID:memberId  币种:currency ", httpMethod = "GET")
    @RequestMapping(value = "getMemberZcDetailsPage", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getMemberZcDetailsPage( HttpServletRequest request,
                                         HttpServletResponse response) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        Map<String, Object> params = $params(request);
        params.put("memberId", m.getId());
        if(HelpUtils.isMapNullValue(params, "sortname")) {
            params.put("sortname", "createtime");
            params.put("sortorder", "desc");
        }
        List<MoneyDto> list = contractAccountDetailManager.selectZcPage(params);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, HelpUtils.newHashMap("rows", list, "total", params.get("total")));
    }

    @ApiOperation(value = "查询会员合约账户操作明细", notes = "输入字段： 会员ID:memberId  币种:currency ", httpMethod = "GET")
    @RequestMapping(value = "getMemberDetailsPage/{isout}", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getMemberDetailsPageByType( HttpServletRequest request,
                                         HttpServletResponse response,@PathVariable Integer isout) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        Map<String, Object> params = $params(request);
        params.put("memberId", m.getId());
        if(HelpUtils.isMapNullValue(params, "sortname")) {
            params.put("sortname", "createtime");
            params.put("sortorder", "desc");
            params.put("isout",isout);
        }
        List<MoneyDto> list = contractAccountDetailManager.selectUsdtPage(params);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, HelpUtils.newHashMap("rows", list, "total", params.get("total")));
    }

    @ApiOperation(value = "查询会员合约账户操作明细", notes = "输入字段： 会员ID:memberId  币种:currency ", httpMethod = "GET")
    @RequestMapping(value = "getMemberZcDetailsPage/{isout}", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getMemberZcDetailsPageByType( HttpServletRequest request,
                                           HttpServletResponse response,@PathVariable Integer isout) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        Map<String, Object> params = $params(request);
        params.put("memberId", m.getId());
        if(HelpUtils.isMapNullValue(params, "sortname")) {
            params.put("sortname", "createtime");
            params.put("sortorder", "desc");
            params.put("isout",isout);
        }
        List<MoneyDto> list = contractAccountDetailManager.selectZcPage(params);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, HelpUtils.newHashMap("rows", list, "total", params.get("total")));
    }



    @ApiOperation(value = "获得用户账户信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "accounts", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp listAccountsI(HttpServletRequest request,
                                      HttpServletResponse response) {

        // 校验用户是否登录
        Member member = JedisUtilMember.getInstance().getMember(request, null);
        if (null == member) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        ContractAccountDto contractAccountDto = contractAccountDetailManager.getAccounts(member.getId());
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, contractAccountDto);
    }

    @ApiOperation(value = "编辑合约用户信息",notes = "输入字段：isValid:合约是否被激活，默认未激活。-1：未激活；0激活",httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "editCustomer", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp editCustomer(HttpServletRequest request, HttpServletResponse response) {
        // 校验用户是否登录
        Member member = JedisUtilMember.getInstance().getMember(request, null);
        if (null == member) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        Map<String, Object> map = $params(request);
        ObjResp objResp = customerManager.editCustomer(map, member);
        if (objResp.getState().equals(Resp.FAIL)) {
            return objResp;
        }
        JedisUtilMember.getInstance().setMember(request, member);
        return objResp;
    }

    @ApiOperation(value = "绑定邀请码",notes = "输入字段：invite_code:邀请码",httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "bindingInviteCode", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp bindingInviteCode(HttpServletRequest request, HttpServletResponse response) {
        // 校验用户是否登录11
        Member member = JedisUtilMember.getInstance().getMember(request, null);
        if (null == member) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        if (member.getIntroduce_m_id() != null) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.CONTRACT_INVITATION_CODE_ALREADY_EXISTS.getErrorENMsg(), null);
        }
        Map<String, Object> map = $params(request);
        return customerManager.bindingInviteCode(request, map, member);
    }

    /**
     * 从币币区转到跟单区
     * @param request
     * @param response
     * @param memberId
     * @param currency
     * @param num
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = "balance/addGdChange/{memberId}/{num}/{currency}", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp addGdChange(HttpServletRequest request,
                             HttpServletResponse response, @PathVariable Integer memberId, @PathVariable String currency, @PathVariable BigDecimal num) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        if(memberId.intValue() != m.getId().intValue()) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_OPERATION_OBJECT.getErrorENMsg(), null);
        }
        currency = currency.toUpperCase();
        if(!"USDT".equals(currency)) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.ERROR_CURRENCY.getErrorENMsg(), null);
        }
        if(accountValidate.isVirtualAccount(m.getId())){
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.CONTRACT_VA_OPERATION_LIMIT.getErrorENMsg(), null);
        }
        contractAccountManager.accountTransfer(memberId, currency, num, ContractOperateTypeEnum.ADDGDBALANCE);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }


    /**
     * 从跟单区转到币币区
     * @param request
     * @param response
     * @param memberId
     * @param currency
     * @param num
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = "balance/subtGdChange/{memberId}/{num}/{currency}", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp subtGdChange( HttpServletRequest request,
                               HttpServletResponse response, @PathVariable Integer memberId, @PathVariable String currency, @PathVariable BigDecimal num) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        if(memberId.intValue() != m.getId().intValue()) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_OPERATION_OBJECT.getErrorENMsg(), null);
        }
        currency = currency.toUpperCase();
        if(!"USDT".equals(currency)) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.ERROR_CURRENCY.getErrorENMsg(), null);
        }
        if (contractAccountManager.checkOrderExist(memberId)){
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_EXIST_ORDER.getErrorENMsg(), null);
        }
        if(accountValidate.isVirtualAccount(m.getId())){
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.CONTRACT_VA_OPERATION_LIMIT.getErrorENMsg(), null);
        }
        contractAccountManager.accountTransfer(memberId, currency, num.negate(), ContractOperateTypeEnum.OUTGDBALNCE);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }
}
