package com.zytx.websocket.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 初始化Netty配置
 */
@Component
@Data
@PropertySource("classpath:application-websocket.yml")
@ConfigurationProperties(prefix = "initnetty")
public class InitNetty implements Serializable {
    private static final long serialVersionUID = -5018935843541633243L;

    private static InitNetty initNetty;

    @PostConstruct
    public void init() {
        //初始化当前类，以便能够使用注解注入
        initNetty = this;
    }

    public static InitNetty getInitNetty() {
        return initNetty;
    }

    //WebSocket启动地址
    private String webHost;
    //WebSocket启动监听端口
    private int webPort;
    //返回Netty核心线程个数
    private int bossThread;
    //Netty工作线程个数
    private int workerThread;

    //是否保持链接
    private boolean keepAlive;

    private String webSocketPath;

    //TCP数据接收缓冲区大小。
    private int revbuf;
    //心跳
    private int heart;
    private int maxContext;
    //服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝
    private int backlog;

    /**
     * 后面的参数暂时不用
     */

    //消息 重发周期
//    @Value("${initnetty.period}")
    private int period;

    /**
     * TCP参数，立即发送数据，默认值为Ture（Netty默认为True而操作系统默认为False）。
     * 该值设置Nagle算法的启用，改算法将小的碎片数据连接成更大的报文来最小化所发送的报文的数量，
     * 如果需要发送一些较小的报文，则需要禁用该算法。Netty默认禁用该算法，从而最小化报文传输延时。
     */
    private boolean nodelay = true;
    /**
     * 地址复用，默认值False。有四种情况可以使用：
     * (1).当有一个有相同本地地址和端口的socket1处于TIME_WAIT状态时，而你希望启动的程序的socket2要占用该地址和端口，比如重启服务且保持先前端口。
     * (2).有多块网卡或用IP Alias技术的机器在同一端口启动多个进程，但每个进程绑定的本地IP地址不能相同。
     * (3).单个进程绑定相同的端口到多个socket上，但每个socket绑定的ip地址不同。
     * (4).完全相同的地址和端口的重复绑定。但这只用于UDP的多播，不用于TCP。
     */
    private boolean reuseaddr = true;


    //服务名称
    private String serverName = "iot-netty-chat";
    //消息 重发延迟
    private int initalDelay = 10;
}
