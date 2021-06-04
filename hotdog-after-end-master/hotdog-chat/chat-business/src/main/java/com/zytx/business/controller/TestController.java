package com.zytx.business.controller;

import com.zytx.business.service.ChatService;
import com.zytx.common.entity.CChatInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
public class TestController {

    @Autowired
    private ChatService chatService;

    @RequestMapping("/test1")
    public String test(HttpServletRequest request, String token) {
        CChatInfo info = new CChatInfo();
        info.setUpdateTime(new Date());
        int a = 1 / 0;
        return null;
    }
}
