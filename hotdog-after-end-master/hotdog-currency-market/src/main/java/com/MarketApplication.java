package com;

import com.pmzhongguo.market.constants.InitConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;


@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class MarketApplication {

    public static void main(String[] args) {
         SpringApplication.run(MarketApplication.class, args);
    }

    @PostConstruct
    public void init() {
        InitConstant.WEB_REQUEST_URLS = webRequestUrls;
        InitConstant.EOS_USDT_CLOSE_URL = eosUSDTCloseUrl;
        InitConstant.EOS_SYMBOL_CLOSE_URL = eosSymbolCloseUrl;
        InitConstant.EOS_MARKET_URL = eosMarketUrl;
        InitConstant.REDIS_HOST = redisHost;
        InitConstant.REDIS_PORT = redisPort;
        InitConstant.REDIS_PASSWORD = redisPassword;
        InitConstant.MARKET_SYMBOL = marketSymbol;
        InitConstant.MARKET_SYMBOL_LIST = Arrays.asList(marketSymbol.split(","));
    }

    @Value("${eos.url.usdt_close}")
    private String eosUSDTCloseUrl;

    @Value("${eos.url.symbol_close}")
    private String eosSymbolCloseUrl;

    @Value("${eos.url.market}")
    private String eosMarketUrl;
    /**
     * redis 地址
     */
    @Value("${spring.redis.host}")
    private String redisHost;
    /**
     * redis端口
     */
    @Value("${spring.redis.port}")
    private String redisPort;
    /**
     * redis 密码
     */
    @Value("${spring.redis.password}")
    private String redisPassword;

    /**
     * 过滤请求url
     */
    @Value("${web.request.urls}")
    private String webRequestUrls;

    /**
     * 市值交易对集合
     */
    @Value("${web.request.marketSymbol}")
    private String marketSymbol;
}
