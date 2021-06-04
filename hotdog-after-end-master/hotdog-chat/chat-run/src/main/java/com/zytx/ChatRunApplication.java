package com.zytx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan("com.zytx.business.filter")
@EnableScheduling
public class ChatRunApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatRunApplication.class, args);
    }
}
