package com.pmzhongguo.ex.business.service;

import com.google.common.collect.ImmutableMap;
import com.pmzhongguo.ex.business.entity.Rule;
import com.pmzhongguo.ex.core.utils.DaoUtil;
import com.pmzhongguo.ex.core.web.LockRuleTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 币种规则
 * @date: 2019-07-31 10:00
 * @author: 十一
 */
@Transactional
@Service
public class RuleServiceManage implements IDataProcess {

    @Autowired
    private MemberService memberService;
    @Autowired
    private RuleService ruleService;
    @Autowired
    DaoUtil daoUtil;

    private static final String RULE_SERVICE_MANAGE_CACHE_CURRENCY_RULE_MAP = "rule_service_manage_cache_currency_rule_map";

    /**
     * 缓存锁仓币种规则,只缓存已启用
     * @param servletContext
     */
    public void cacheRuleWithCurrency(ServletContext servletContext) {
        Map<String,Rule> currencyRuleMap = new HashMap<>();
        List<Rule> ruleList = ruleService.getRuleList(ImmutableMap.of("enable",0));
        for (Rule rule : ruleList) {
            if ("交易锁仓".equals(rule.getRuleType())){
                String sql = "UPDATE m_rule SET rule_type = ? where rule_type = '交易锁仓'";
                daoUtil.update(sql, LockRuleTypeEnum.TRADE.getType() + "");
                rule.setRuleType(LockRuleTypeEnum.TRADE.getType() + "");
            }
            currencyRuleMap.put(rule.getCurrency(),rule);
        }
        servletContext.setAttribute(RULE_SERVICE_MANAGE_CACHE_CURRENCY_RULE_MAP,currencyRuleMap);
    }

    /**
     * 从缓存获取币种规则
     * @param currency 币种
     */
    public static Rule getCurrencyRuleByCache(String currency) {
        WebApplicationContext wac = ContextLoader
                .getCurrentWebApplicationContext();
        Map<String,Rule> currencyRuleMap  = ((Map < String, Rule>)
        wac.getServletContext().getAttribute(RULE_SERVICE_MANAGE_CACHE_CURRENCY_RULE_MAP));
        if (CollectionUtils.isEmpty(currencyRuleMap)){
            return null;
        }
        return currencyRuleMap.get(currency);
//        return null;
    }

    @Override
    public void dataSync(ServletContext servletContext) {
        cacheRuleWithCurrency(servletContext);
    }
}
