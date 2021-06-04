package com.pmzhongguo.ex.core.websocket;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

public class MySessionContext {
	private static MySessionContext instance;  
    private HashMap<String,HttpSession> sessionMap;  
  
    private MySessionContext() {  
        sessionMap = new HashMap<String,HttpSession>();  
    }  
  
    public static MySessionContext getInstance() {  
        if (instance == null) {  
            instance = new MySessionContext();  
        }  
        return instance;  
    }  
  
    public synchronized void addSession(HttpSession session) {  
        if (session != null) {  
            sessionMap.put(session.getId(), session);  
        }  
    }  
  
    public synchronized void delSession(String sessionID) {  
        if (sessionMap.containsKey(sessionID)) {  
            sessionMap.remove(sessionID);  
        }  
    }  
  
    public synchronized HttpSession getSession(String sessionID) {  
        if (sessionID == null) {  
            return null;  
        }  
        return sessionMap.get(sessionID);  
    }  
  
}
