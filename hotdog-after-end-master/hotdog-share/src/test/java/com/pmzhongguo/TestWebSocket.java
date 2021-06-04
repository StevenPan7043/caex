package com.pmzhongguo;

import com.google.common.collect.Maps;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TestWebSocket
{
    public WebSocketClient client;

    public TestWebSocket(String wsUrl)
    {
        try
        {
            client = new WebSocketClient(new URI(wsUrl), new Draft_6455())
            {
                @Override
                public void onOpen(ServerHandshake arg0)
                {
                    System.out.println("打开链接");
//                    client.send("{\"channel\":\"depth.zzexzc\"}");
//                    client.send("{\"channel\":\"depth.ethzc\"}");
                    client.send("{\"channel\":\"ticker.zzexzc\"}");
//                    client.send("{\"channel\":\"trade.zzexzc\"}");
//                    client.send("{\"channel\":\"kline.15min.zzexzc\"}");
                }

                @Override
                public void onMessage(String str)
                {
                    System.out.println(str);
                }

                @Override
                public void onError(Exception arg0)
                {
                    arg0.printStackTrace();
                    System.out.println("发生错误已关闭");
                }

                @Override
                public void onClose(int arg0, String arg1, boolean arg2)
                {
                    System.out.println("链接已关闭");
                }

                @Override
                public void onMessage(ByteBuffer bytes)
                {
                    try
                    {
                        String str = new String(bytes.array(), "utf-8");
                        System.out.println("收到消息拉============================>" + str);
                    } catch (UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                }
            };
            client.connect();
            while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN))
            {
                System.out.println("还没有打开, state: " + client.getReadyState());
            }
            System.out.println("打开了");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws NotYetConnectedException
    {
        TestWebSocket c = new TestWebSocket("ws://127.0.0.1:8085/ws/v1");
//        TestWebSocket c = new TestWebSocket("ws://47.75.218.160:8060/ws/v1");
//        TestWebSocket c = new TestWebSocket("ws://47.52.232.214/ws/v1");
//        TestWebSocket c = new TestWebSocket("wss://echo.websocket.org/");
    }
}
