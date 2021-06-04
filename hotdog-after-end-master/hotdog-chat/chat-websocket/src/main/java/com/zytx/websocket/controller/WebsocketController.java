package com.zytx.websocket.controller;

import com.zytx.websocket.config.InitNetty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class WebsocketController {

    private InitNetty initNetty = InitNetty.getInitNetty();

    @RequestMapping("/test3")
    public void test(HttpServletRequest request, String token) {

        System.out.println(initNetty.getPeriod());
    }
}
