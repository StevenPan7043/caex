package com.pmzhongguo.market.base;

import com.pmzhongguo.market.constants.InitConstant;
import com.pmzhongguo.market.ws.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author jary
 * @creatTime 2020/5/19 3:52 PM
 */
@Component
public class InitDataLoad implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(InitDataLoad.class);

    @Override
    public void run(String... args) throws Exception {

        //连接火币ws api
        WebSocketClient.HBSocket(InitConstant.MARKET_SYMBOL_LIST);
    }
}
