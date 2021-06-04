package com.zytx.websocket.constant;

/**
 *
 */
public class LogConstant {

    public static final String HANDLERADDED = "[DefaultWebSocketHandler.handlerAdded]";

    public static final String JOIN_SUCCESS = "加入成功";

    public static final String CHANNELACTIVE = "[DefaultWebSocketHandler.channelActive]";

    public static final String CHANNEL_SUCCESS = "链接成功";

    public static final String DISCONNECT = "异常断开";

    public static final String EXCEPTIONCAUGHT = "[DefaultWebSocketHandler.exceptionCaught]";

    public static final String CHANNELINACTIVE = "[AbstractHandler：channelInactive]";

    public static final String CLOSE_SUCCESS = "关闭成功";

    public static final String NOTFINDLOGINCHANNLEXCEPTION = "[捕获异常：LoginChannelNotFoundException]-[AbstractHandler：channelInactive] 关闭未正常注册链接！";

    public static final String USEREVENTTRIGGERED = "[捕获异常：LoginChannelNotFoundException]-[AbstractHandler：userEventTriggered] 超时！";
}
