package com.pmzhongguo.ex.business.mapper;

import com.pmzhongguo.ex.business.entity.Rule;
import com.pmzhongguo.ex.core.mapper.SuperMapper;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/7/24 5:25 PM
 */
public interface RuleMapper extends SuperMapper {
    Integer addRule(Rule rule);

    Integer updateRule(Rule rule);

    @MapKey("currency")
    Map<String,Rule> getRuleMap(Rule rule);

    Integer addTradeLockToRule(Rule rule);

    Integer updateRuleByparams(Map params);

    List<Rule> getRuleList(Map<String, Object> param);

}
