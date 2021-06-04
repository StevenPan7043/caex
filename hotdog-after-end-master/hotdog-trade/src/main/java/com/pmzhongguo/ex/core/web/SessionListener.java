package com.pmzhongguo.ex.core.web;

import com.google.common.collect.Maps;
import com.pmzhongguo.ex.business.entity.Member;

import javax.servlet.http.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SessionListener implements HttpSessionListener, HttpSessionAttributeListener {

    public static List<Integer> loginMemberList = new ArrayList<Integer>();

    private static String addLoginLock = "ADDLOGINLOCK";

    private long addTime;

    public static Map<String, HttpSession> sessionMap = Maps.newHashMap();

    @Override
    public void sessionCreated(HttpSessionEvent event) {
//        System.out.println("session 创建, sessionId: " + event.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
//        HttpSession session = event.getSession();
//        Member member = (Member) session.getAttribute(Constants.SYS_SESSION_MEMBER);
//        System.out.println("session 销毁, sessionId: " + event.getSession().getId() + "member name: " + member == null ? "" : member.getM_name());
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
//        System.out.println("添加属性：" + event.getName() + " sessionId: " + event.getSession().getId());
        //当属性保存的时候保存当前时间
//        addTime = System.currentTimeMillis();
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
//        System.out.println("移除属性：" + event.getName() + " sessionId: " + event.getSession().getId());
        //当属性移除的时候计算属性保存时间
//        long removeTime = System.currentTimeMillis();
//        long t = (removeTime - addTime) / 1000;
//        System.out.println("数据保存时间：" + t + "秒");
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
//        System.out.println("更改属性：" + event.getName());
    }
}
