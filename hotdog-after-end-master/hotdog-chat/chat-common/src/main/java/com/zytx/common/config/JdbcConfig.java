package com.zytx.common.config;

import com.zaxxer.hikari.HikariDataSource;
import com.zytx.common.util.DESEncodeUtil;
import com.zytx.common.util.StringNetherSwap;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@Data
public class JdbcConfig {

    @Value("${datasource.url}")
    String url;
    @Value("${datasource.username}")
    String username;
    @Value("${datasource.password}")
    String password;
    @Value("${datasource.driver-class-name}")
    String driver;

    /**
     * 设置一个数据库的连接池
     */
    @Bean()
    public DataSource mysqlCoreDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setUsername(this.getUserName());
        dataSource.setPassword(this.getRealPassword());//获取的密码为toor,实际为root，需反转
        dataSource.setJdbcUrl(this.url);
        dataSource.setDriverClassName(this.driver);
        //最大连接数
        dataSource.setMaximumPoolSize(50);
        //最小连接数
        dataSource.setMinimumIdle(5);
        return dataSource;
    }

    /**
     * 密码解密
     */
    public String getRealPassword() {
        String pwd = this.password;
        pwd = DESEncodeUtil.decrypt(pwd);
        pwd = StringNetherSwap.swap(pwd);
        return pwd;
    }

    /**
     * 账号解密
     */
    public String getUserName() {
        String name = this.username;
        name = DESEncodeUtil.decrypt(name);
        name = StringNetherSwap.swap(name);
        return name;
    }
}
