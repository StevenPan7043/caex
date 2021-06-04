package com.zytx.common.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CommonController {

    @RequestMapping("/test2")
    public String test(HttpServletRequest request, String token) {
        System.out.println("2222222");
        return "是否是乱码";
    }
}
