package com.zytx.websocket.listener;

import com.zytx.common.constant.RedisKeyConstant;
import com.zytx.common.util.RedisUtil;
import com.zytx.websocket.config.StartConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * springboot开启和关闭监听
 */
@Log4j2
@Component
public class SpringListener {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * springboot开启监听
     *
     * @return
     */
    @Component
    class RunListener implements ApplicationRunner {
        @Override
        public void run(ApplicationArguments args) throws Exception {
            log.info("springboot开启");
            StartConfig.open();
        }
    }

    /**
     * springboot关闭监听
     *
     * @return
     */
    @Component
    class CloseListener implements DisposableBean {

        @Override
        public void destroy() throws Exception {
            log.info("springboot关闭");
            StartConfig.close();
            //清空在线用户
            redisUtil.deleteKey(RedisKeyConstant.GROUP + "*");
        }
    }
}