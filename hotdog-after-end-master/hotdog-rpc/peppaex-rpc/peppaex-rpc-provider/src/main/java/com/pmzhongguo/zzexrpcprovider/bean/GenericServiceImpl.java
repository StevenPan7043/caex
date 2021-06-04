/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/8 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.bean;

import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.zzexrpcprovider.consts.QuotationKeyConst;
import com.pmzhongguo.zzexrpcprovider.service.currency.CurrencyService;
import com.pmzhongguo.zzexrpcprovider.service.framework.FrmUserService;
import com.pmzhongguo.zzextool.utils.DaoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/8 16:20
 * @description：controller 基类
 * @version: $
 */
@Slf4j
public class GenericServiceImpl implements GenericService {

    @Autowired
    public DaoUtil daoUtil;

    @Autowired
    protected CurrencyService currencyService;

    @Autowired
    protected FrmUserService frmUserService;

    /**
     * 初始化系统配置和币对配置
     */
    @Override
    public void initData() {
        log.warn("<==================初始化数据=======================>");
        QuotationKeyConst.frmConfig = frmUserService.findConfig();
        QuotationKeyConst.currencyPairs = currencyService.getCurrencyPairLst();
        QuotationKeyConst.PAIRS.clear();
        for (CurrencyPair currencyPair : QuotationKeyConst.currencyPairs) {
            QuotationKeyConst.PAIRS.add(currencyPair.getKey_name().toLowerCase());
        }
        QuotationKeyConst.currencysMap = (Map<String, Currency>) currencyService.getCurrencyMap().get("retMap");
    }

    /**
     * 根据frm_gen_code表中的配置生成对应的编号。
     *
     * @param id
     * @param preFix 前缀，可以为空(注意不要传null，传"")，例如结算时，前缀是财务周期名称，就要传此参数
     * @return
     */
    protected synchronized String genCode(int id, String preFix) {
        Map data = daoUtil.queryForMap(
                "SELECT CONCAT(prefix, IF(need_date = 1, DATE_FORMAT(NOW(),'%y%m%d'), ''), '-') prefix, t.suffix, t.for_table, t.for_column FROM frm_gen_code t where t.id = ?",
                id);
        String prefix = preFix + (String) data.get("prefix");
        String suffix = (String) data.get("suffix");
        String forTable = (String) data.get("for_table");
        String forColumn = (String) data.get("for_column");

        String sql = "select max(" + forColumn + ") from " + forTable
                + " where " + forColumn + " like '" + prefix + "%'";
        String strCode = daoUtil.queryForObject(sql, String.class);
        if (strCode == null) {
            return prefix + suffix;
        } else {
            String[] temp = strCode.split("-");
            String num = (Integer.parseInt(temp[1]) + 1) + "";
            String finalNum = StringUtils.leftPad(num, temp[1].length(), '0');
            return prefix + finalNum;
        }
    }

    public Map<String, CurrencyPair> getCurrencyPairMap() {
        Map<String, CurrencyPair> currencyPairMap = currencyService.getCurrencyPairMap();
        return currencyPairMap;
    }

}
