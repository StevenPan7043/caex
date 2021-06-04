package com.pmzhongguo.ex.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;

/**
 * @author jary
 * @creatTime 2019/11/20 11:10 AM
 */
@Service
@Transactional
public class CurrencyPairService implements IDataProcess {

    @Autowired
    private CurrencyService currencyService;

    @Override
    public void dataSync(ServletContext servletContext) {
        currencyService.cacheCurrencyPair(servletContext);
    }
}
