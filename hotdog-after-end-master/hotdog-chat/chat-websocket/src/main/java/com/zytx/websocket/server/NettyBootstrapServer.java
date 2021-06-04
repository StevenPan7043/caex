package com.zytx.websocket.server;

import com.zytx.websocket.config.InitNetty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 **/
public class NettyBootstrapServer extends AbstractBootstrapServer {

    private final Logger log = LoggerFactory.getLogger(NettyBootstrapServer.class);

    private InitNetty serverBean = InitNetty.getInitNetty();

    private EventLoopGroup bossGroup;

    private EventLoopGroup workGroup;

    ServerBootstrap bootstrap = null;// 启动辅助类

    Object waitLock = new Object(); //加锁，防止重复启动

    /**
     * 服务开启
     */
    public void start() {
        synchronized (waitLock) {
            initEventPool();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_REUSEADDR, serverBean.isReuseaddr())
                    .option(ChannelOption.SO_BACKLOG, serverBean.getBacklog())
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .option(ChannelOption.SO_RCVBUF, serverBean.getRevbuf())
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) {
                            initHandler(ch.pipeline(), serverBean);
                        }
                    })
                    .childOption(ChannelOption.TCP_NODELAY, serverBean.isNodelay())
                    .childOption(ChannelOption.SO_KEEPALIVE, serverBean.isKeepAlive())
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .bind(serverBean.getWebHost(), serverBean.getWebPort())
                    .addListener((ChannelFutureListener) channelFuture -> {
                        if (channelFuture.isSuccess()) {
                            log.info("服务端启动成功【" + serverBean.getWebHost() + ":" + serverBean.getWebPort() + "】");
                        } else {
                            log.warn("服务端启动失败【" + serverBean.getWebHost() + ":" + serverBean.getWebPort() + "】:"+channelFuture.cause().toString());
                        }
                    });
        }
    }

    /**
     * 初始化EventPool 参数
     */
    private void initEventPool() {
        bootstrap = new ServerBootstrap();
//        if (Epoll.isAvailable()) {
        bossGroup = new NioEventLoopGroup(serverBean.getBossThread(), new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            public Thread newThread(Runnable r) {
                return new Thread(r, "BOSS_" + index.incrementAndGet());
            }
        });
        workGroup = new NioEventLoopGroup(serverBean.getWorkerThread(), new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            public Thread newThread(Runnable r) {
                return new Thread(r, "WORK_" + index.incrementAndGet());
            }
        });
//        }
    }

    /**
     * 关闭资源
     */
    public void shutdown() {
        synchronized (waitLock) {
            if (workGroup != null && bossGroup != null) {
                try {
                    bossGroup.shutdownGracefully().sync();// 优雅关闭
                    workGroup.shutdownGracefully().sync();
                    log.info("服务器关闭");
                } catch (InterruptedException e) {
                    log.error("服务端关闭资源失败【" + serverBean.getWebHost() + ":" + serverBean.getWebPort() + "】");
                }
            }
        }
    }
}
