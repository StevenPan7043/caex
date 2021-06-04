package com.zytx.business.config;

import com.zytx.business.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class InitBusinessConfig {

    @Autowired
    private ChatService chatService;

    @PostConstruct
    public void init() {
        chatService.initChatInfo();
    }
}
