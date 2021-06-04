package com.pmzhongguo.ex.business.controller;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.Rule;
import com.pmzhongguo.ex.business.entity.WarehouseDetail;
import com.pmzhongguo.ex.business.mapper.RuleMapper;
import com.pmzhongguo.ex.business.service.RuleService;
import com.pmzhongguo.ex.business.service.RuleServiceManage;
import com.pmzhongguo.ex.business.service.WarehouseAccountService;
import com.pmzhongguo.ex.business.service.WarehouseDetailService;
import com.pmzhongguo.ex.business.vo.WarehouseAccountVo;
import com.pmzhongguo.ex.core.utils.*;
import com.pmzhongguo.ex.core.web.*;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.zzextool.consts.JedisChannelConst;
import com.qiniu.util.BeanUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/8/3 10:29 AM
 */
@ApiIgnore
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("backstage/a")
public class LockReleaseController extends TopController {

    @Autowired
    private RuleMapper ruleMapper;

    @Autowired
    private RuleService ruleService;
    @Autowired
    private WarehouseAccountService warehouseAccountService;

    @Autowired
    private WarehouseDetailService warehouseDetailService;
    private static Logger zkLog = LoggerFactory.getLogger("zookeeper");

    /**
     * 锁仓释放资产列表界面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/toWarehouseList", method = RequestMethod.GET)
    public String toWarehouseList(HttpServletRequest request, HttpServletResponse response) {
        $attr("ReleaseTypes", LockReleaseEnum.values());
        return "business/warehouse/warehouse_list";
    }
    /**
     * 获取锁仓释放资产
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getWarehouseList")
    @ResponseBody
    public Map<String, Object> getWarehouseList(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = $params(request);
        List<WarehouseAccountVo> warehouseAccountListPage = warehouseAccountService.getWarehouseAccountListPage(params);
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("Rows", warehouseAccountListPage);
        map.put("Total", params.get("total"));
        return map;
    }
    /**
     * 锁仓明细列表界面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "toWarehouseDetailList", method = RequestMethod.GET)
    public String toWarehouseDetailList(HttpServletRequest request, HttpServletResponse response) {
        return "business/warehouse/warehouse_detail_List";
    }


    /**
     * 获取锁仓明细
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getWarehouseDetailList")
    @ResponseBody
    public Map<String, Object> getWarehouseDetailList(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = $params(request);
        List<WarehouseDetail> warehouseDetails = warehouseDetailService.selectWarehouseDetail(params);
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("Rows", warehouseDetails);
        map.put("Total", params.get("total"));
        return map;
    }

    /**
     *跳转到币种锁仓规则页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "toRuleList", method = RequestMethod.GET)
    public String toRuleList(HttpServletRequest request, HttpServletResponse response) {
        return "business/warehouse/rule_list";
    }

    /**
     * 获取币种锁仓规则信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "getRuleList")
    @ResponseBody
    public Map<String, Object> getRuleList(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = $params(request);
        if (BeanUtil.isEmpty(params.get("enable"))) {
            params.put("enable", EnableTypeEnum.ENABLE);
        }
        List<Rule> ruleList = ruleService.getRuleList(params);
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("Rows", ruleList);
        map.put("Total", params.get("total"));
        return map;
    }

    @RequestMapping("/toAddRule")
    public String toAddRule(HttpServletRequest request, HttpServletResponse response) {
        //List<Currency> currencyIsLockLst = HelpUtils.getCurrencyIsLockLst();
        //因为币种的锁仓字段没了 所以要所有币种
        List<Currency> currencyIsLockLst = HelpUtils.getCurrencyLst();
        $attr("lockList", currencyIsLockLst);
        $attr("ruleType", LockRuleTypeEnum.values());
        $attr("lockRuleType", RuleTypeEnum.values());
        return "business/warehouse/add_rule";
    }

    @RequestMapping(value = "/addRule", method = RequestMethod.POST)
    @ResponseBody
    public Resp addRule(Rule rule, HttpServletRequest request) {
        Map<String, Object> params = $params(request);
        if (BeanUtil.isEmpty(rule.getRuleDetail()) || Double.valueOf(rule.getRuleDetail()) <= 0 || Double.valueOf(rule.getRuleDetail()) > 100) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.RULEDETAIL_MAST_BE_MORE_THAN_ZERO.getErrorCNMsg());
        }
        if (BeanUtil.isEmpty(rule.getLockReleaseTime()) || Double.valueOf(rule.getLockReleaseTime()) <= 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LOCKRELEASETIME_MAST_BE_MORE_THAN_ZERO.getErrorCNMsg());
        }
        List<Rule> ruleList = ruleService.getRuleList(params);
        if (!CollectionUtils.isEmpty(ruleList)){
            return new Resp(Resp.FAIL, ErrorInfoEnum.RULE_IS_EXISTS.getErrorENMsg());
        }
        if (rule.getRuleTimeType().equalsIgnoreCase(RuleTypeEnum.HOUR.getCodeEn()) && rule.getLockReleaseTime() > 23) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.HOUR_MAX_23.getErrorCNMsg());
        } else if (rule.getRuleTimeType().equalsIgnoreCase(RuleTypeEnum.DAY.getCodeEn()) && rule.getLockReleaseTime() > 29) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.DAY_MAX_29.getErrorCNMsg());
        } else if (rule.getRuleTimeType().equalsIgnoreCase(RuleTypeEnum.MONTH.getCodeEn()) && rule.getLockReleaseTime() > 11) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.MONTH_MAX_11.getErrorCNMsg());
        }else if (rule.getRuleTimeType().equalsIgnoreCase(RuleTypeEnum.MIN.getCodeEn()) && rule.getLockReleaseTime() > 59 && rule.getRuleType().equals(LockRuleTypeEnum.AUTO_RELEASE.getType())){
            return new Resp(Resp.FAIL, ErrorInfoEnum.MIN_MAX_59.getErrorCNMsg());
        }
        rule.setRuleDetail(Double.valueOf(rule.getRuleDetail())+"");
        rule.setEnable(EnableTypeEnum.UN_ENABLE.getType());
        ruleService.addTradeLockToRule(rule);
        return new Resp(Resp.SUCCESS, "添加规则成功，默认禁用，请手动启用");
    }

    @RequestMapping(value = "/editRule", method = RequestMethod.POST)
    @ResponseBody
    public Resp editRule(Rule rule, HttpServletRequest request) {
        if (BeanUtil.isEmpty(rule.getCurrency())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.PLEASE_SELECT_CURRENCY.getErrorENMsg());
        }
        List<Rule> ruleList = ruleService.getRuleList(HelpUtils.newHashMap("id", rule.getId()));
        if (CollectionUtils.isEmpty(ruleList)) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.RULE_IS_NOT_EXISTS.getErrorENMsg());
        }
        Rule currRule = ruleList.get(0);

        if (!rule.getCurrency().equalsIgnoreCase(currRule.getCurrency())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.CURRENCY_EXCEPTION.getErrorENMsg());
        }
        List<WarehouseAccountVo> warehouseAccountList = warehouseAccountService.getWarehouseAccountListPage(HelpUtils.newHashMap("currency", currRule.getCurrency(), "ruleIds", currRule.getId(), "released", LockReleaseEnum.RELEASING_END.getType()));
        if (!CollectionUtils.isEmpty(warehouseAccountList)) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.RULE_USERS_USING.getErrorENMsg());
        }
        currRule.setRuleReleaseScale(rule.getRuleReleaseScale());
        currRule.setRuleType(rule.getRuleType());
        currRule.setRuleDetail(rule.getRuleDetail());
        currRule.setEnable(EnableTypeEnum.UN_ENABLE.getType());
        currRule.setLockReleaseTime(rule.getLockReleaseTime());
        ruleService.updateRule(currRule);
        JedisUtil.getInstance().publish(JedisChannelConst.JEDIS_CHANNEL_SYNC_CURRENCY_RULE,JedisChannelConst.SYNC_MESSAGE);
        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    @RequestMapping("/toEditRule")
    public String toEditRule(HttpServletRequest request,
                             HttpServletResponse response) {
        Rule rule =null;
        List<Rule> ruleList = ruleService.getRuleList(HelpUtils.newHashMap("id", $int("id")));
        if (CollectionUtils.isEmpty(ruleList)){
            rule =new Rule();
        }
        rule = ruleList.get(0);
        List<Currency> currencyIsLockLst = HelpUtils.getCurrencyLst();
        $attr("lockList", currencyIsLockLst);
        $attr("info", rule);
        $attr("ruleType", RuleTypeEnum.values());
        return "business/warehouse/add_rule";
    }

    /**
     * 规则启用
     * @param rule
     * @param request
     * @return
     */
    @RequestMapping(value = "/ruleEnable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Resp ruleEnable(Rule rule, HttpServletRequest request) {
        List<Rule> ruleList = ruleService.getRuleList(HelpUtils.newHashMap("id", rule.getId()));
        if (CollectionUtils.isEmpty(ruleList)){
            return  new Resp(Resp.FAIL,ErrorInfoEnum.RULE_IS_NOT_EXISTS.getErrorCNMsg());
        }
        //交易锁仓走本地
        if (ruleList.get(0).getRuleType().equals(LockRuleTypeEnum.TRADE.getCodeCn()) || ruleList.get(0).getRuleType().equals(LockRuleTypeEnum.TRADE.getType()+"")) {
            try {
                if (rule.getEnable() == 0) {
                    //找出要更改的锁仓规则，因为传不进currency要重新找
                    Rule oldRule = new Rule();
                    oldRule.setId(rule.getId());
                    Map<String, Rule> ruleMap = ruleMapper.getRuleMap(oldRule);
                    String currency = null;
                    for (String key : ruleMap.keySet()) {
                        currency = key;
                    }
                    //缓存里面如果有就失败
                    if (null != RuleServiceManage.getCurrencyRuleByCache(currency)) {
                        return new ObjResp(Resp.FAIL, "该币种已有充值锁仓规则，请先禁用", null);
                    }
                }
                if (ruleList.get(0).getRuleType().equals(LockRuleTypeEnum.TRADE.getCodeCn())) {
                    rule.setRuleType(LockRuleTypeEnum.TRADE.getType() + "");
                    rule.setRuleTimeType(RuleTypeEnum.MIN.getCodeEn());
                }
                ruleMapper.updateRule(rule);
            } catch (Exception e) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_REQ_TIME_OUT.getErrorENMsg(), null);
            } finally {
                JedisUtil.getInstance().publish(JedisChannelConst.JEDIS_CHANNEL_SYNC_CURRENCY_RULE, "sync");
            }
            return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
        }
        return ruleEnable(rule);
    }




}
