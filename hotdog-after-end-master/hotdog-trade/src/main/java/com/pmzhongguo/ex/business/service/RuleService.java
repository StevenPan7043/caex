package com.pmzhongguo.ex.business.service;

import com.pmzhongguo.ex.business.entity.Rule;
import com.pmzhongguo.ex.business.mapper.RuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/8/29 11:36 AM
 */
@Service
@Transactional
public class RuleService {
    @Autowired
    private RuleMapper ruleMapper;
    public List<Rule> getRuleList(Map params) {
        return ruleMapper.getRuleList(params);
    }

    public void addRule(Rule rule) {
        int i = ruleMapper.addRule(rule);
    }

    public void updateRule(Rule rule) {
        int i = ruleMapper.updateRule(rule);
    }

    public void addTradeLockToRule(Rule rule){
        int i = ruleMapper.addTradeLockToRule(rule);
    }
}
