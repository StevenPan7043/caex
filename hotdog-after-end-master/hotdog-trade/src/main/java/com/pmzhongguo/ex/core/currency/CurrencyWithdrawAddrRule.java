package com.pmzhongguo.ex.core.currency;

import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.zzextool.utils.StringUtil;

/**
 * @description: 币种提现地址规则校验
 * @date: 2019-10-18 11:00
 * @author: 十一
 */
public class CurrencyWithdrawAddrRule {


    private Currency currency;

    public CurrencyWithdrawAddrRule(String currency) {
        this.currency = HelpUtils.getCurrencyMap().get(currency.toUpperCase());

    }

    private CurrencyWithdrawAddrRule() {
    }

    /**
     * 地址校验
     * @param address 地址
     * @return 地址正确返回true
     */
    public boolean validate(String address) {

        // 为null,不做校验
        if (this.currency == null || StringUtil.isNullOrBank(currency.getWithdraw_rule())) {
            return true;
        }
        try {
            String[] rules = currency.getWithdraw_rule().split(",");
            for (String rule : rules) {
                if (!checkRule(rule,address)) {
                    return false;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    /**
     *
     * @param rule 规则key:value
     * @param address 地址
     * @return 符合规则返回true
     */
    private boolean checkRule(String rule,String address) {
        String[] rules = rule.split(":");
        if (rules[0].trim().equals(RuleEnum.MAX_LEN.rule)) {
            return Integer.parseInt(rules[1].trim()) >= address.length();
        }else if (rules[0].trim().equals(RuleEnum.PRE.rule)) {
            return address.startsWith(rules[1].trim());
        }else if(rules[0].trim().equals(RuleEnum.MIN_LEN.rule)){
            return address.length() >= Integer.parseInt(rules[1].trim());
        }
        return true;

    }


    private enum RuleEnum {
        /**
         * 最大长度
         */
        MAX_LEN("maxlen"),
        /**
         * 前缀
         */
        PRE("pre"),
        /**
         * 最小长度
         */
        MIN_LEN("minlen");

        private String rule;

        RuleEnum(String rule) {
            this.rule = rule;
        }

        public String getRule() {
            return rule;
        }

    }
}
