package com.pmzhongguo.ex.core.websocket;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@WebListener
public class RequestListener implements ServletRequestListener
{

    @Override
    public void requestDestroyed(ServletRequestEvent sre)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre)
    {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        String url = request.getRequestURI();
        // websocket 需要提前创建session. 只对websocket开放
        if (url.contains("/websocket/"))
        {
            // TODO Auto-generated method stub
            HttpSession session = ((HttpServletRequest) sre.getServletRequest()).getSession();
            System.out.println("sessionID: " + session.getId() + " url: " + request.getRequestURI());
        }
    }

}
