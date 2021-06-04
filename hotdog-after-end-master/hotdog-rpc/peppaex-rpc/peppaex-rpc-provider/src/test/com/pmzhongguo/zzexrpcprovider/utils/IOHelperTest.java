package com.pmzhongguo.zzexrpcprovider.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

/**
 * @description: 一定要写注释啊
 * @date: 2019-12-23 09:59
 * @author: 十一
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class IOHelperTest {

    @Test
    public void pathTest() {
        String basePath = new ApplicationHome(this.getClass()).getSource().getParentFile().getPath() + File.separator;
        log.info("basePath:{}", basePath);
    }
}
