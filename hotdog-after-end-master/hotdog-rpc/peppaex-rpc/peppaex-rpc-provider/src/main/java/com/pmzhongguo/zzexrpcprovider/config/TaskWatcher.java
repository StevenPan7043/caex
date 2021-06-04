/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/20 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.config;

import com.pmzhongguo.zzexrpcprovider.basic.TaskManager;
import com.pmzhongguo.zzexrpcprovider.bean.GenericServiceImpl;
import com.pmzhongguo.zzexrpcquartz.service.market.MarketApiService;
import com.pmzhongguo.zzextool.consts.StaticConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/20 23:17
 * @description：zk 监听器
 * @version: $
 */
@Slf4j
@Component
public class TaskWatcher extends GenericServiceImpl implements Watcher {

    /**
     * 标记服务器信息
     */
    public static String LOG_PREFIX_OF_THREAD;
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private ZooKeeper zooKeeper;
    /**
     * 当前服务节点路径
     */
    private String selfPath;
    /**
     * 执行节点路径
     */
    private String waitPath;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private DubboService dubboService;


    /**
     * 构造方法
     */
    public TaskWatcher() {
        LOG_PREFIX_OF_THREAD = "[主机IP: " + StaticConst.hostIP + "; 主机名: " + StaticConst.hostName + "; 内网IP" + StaticConst.localIP + "]";
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    /**
     * 初始化zk对象和跟节点
     */
    public void init() {
        try {
            this.createConnection(StaticConst.ZKAddress, StaticConst.SESSION_TIMEOUT);
            //StaticConst.GROUP_PATH不存在的话，由一个服务器创建即可；
            this.createPath(StaticConst.GROUP_PATH, "根节点由" + LOG_PREFIX_OF_THREAD + "创建", true);
            if (this.getLock()) {
                // 开启调度
                taskManager.startAllTask();
            } else {
                // 获取不到锁就创建服务对象
                dubboService.createMarketService(MarketApiService.class, zooKeeper, 0);
            }
            // 创建定时器检查调度状态
            checkQuartzStatus();
        } catch (Exception e) {
            log.error("<================ TaskWatch init失败： " + e.getLocalizedMessage());
        }
    }

    /**
     * 定时器检查调度状态, 并关闭zk
     */
    public void checkQuartzStatus() {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(() -> {
            // TODO 判断调度是否在运行

            if (true) {
                return;
            }
            try {
                // 删除本服务节点
                deletePath();
                // 重新启动调度并创建节点
                restartServer();
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 0, 15, TimeUnit.SECONDS);
    }

    /**
     * 重新启动调度服务并创建zk节点
     *
     * @throws InterruptedException
     * @throws IOException
     * @throws KeeperException
     */
    public void restartServer() {
        taskManager.stopAllTask();
        init();
    }

    /**
     * 创建ZK连接
     *
     * @param connectString  ZK服务器地址列表
     * @param sessionTimeout Session超时时间
     */
    public void createConnection(String connectString, int sessionTimeout) {
        try {
            zooKeeper = new ZooKeeper(connectString, sessionTimeout, this);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connectedSemaphore.countDown();
        }
    }

    /**
     * 关闭ZK连接
     */
    private void releaseConnection() {
        if (this.zooKeeper != null) {
            try {
                this.zooKeeper.close();
            } catch (InterruptedException e) {
            }
        }
        log.info(LOG_PREFIX_OF_THREAD + "主动释放连接");
    }

    /**
     * 创建节点
     *
     * @param path 节点path
     * @param data 初始数据内容
     * @return
     */
    public boolean createPath(String path, String data, boolean needWatch) throws KeeperException, InterruptedException, IOException {
        if (ZooKeeper.States.CLOSED == zooKeeper.getState()) {
            zooKeeper = new ZooKeeper(StaticConst.ZKAddress, StaticConst.SESSION_TIMEOUT, this);
        }
        if (ZooKeeper.States.CONNECTING == zooKeeper.getState()) {
            connectedSemaphore.await();
        }
        if (zooKeeper.exists(path, needWatch) == null) {
            log.info("根节点创建成功, Path: " + this.zooKeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT) + ", content: " + data);
        }
        return true;
    }

    /**
     * 删除当前子节点
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void deletePath() throws KeeperException, InterruptedException {
        zooKeeper.delete(selfPath, -1);
        releaseConnection();
    }

    /**
     * 获取锁
     *
     * @return
     */
    public boolean getLock() throws KeeperException, InterruptedException {
        // 判断子节点不存在则创建
        if (StringUtils.isBlank(selfPath) || zooKeeper.exists(this.selfPath, false) == null) {
            // EPHEMERAL_SEQUENTIAL 自增的临时节点
            selfPath = zooKeeper.create(StaticConst.SUB_PATH, StaticConst.SUB_DATA.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            log.warn(LOG_PREFIX_OF_THREAD + "创建锁路径:" + selfPath + "；dubbo信息: " + StaticConst.SUB_DATA);
        }
        if (checkMinPath()) {
            return getLockSuccess();
        }
        return false;
    }

    /**
     * 检查自己是不是最小的节点
     *
     * @return
     */
    public boolean checkMinPath() throws KeeperException, InterruptedException {
        DubboService.activeIp = StaticConst.SUB_DATA;
        List<String> subNodes = zooKeeper.getChildren(StaticConst.GROUP_PATH, false);
        Collections.sort(subNodes);
        int index = subNodes.indexOf(selfPath.substring(StaticConst.GROUP_PATH.length() + 1));
        switch (index) {
            case -1: {
                log.error(LOG_PREFIX_OF_THREAD + "本节点已不在了..." + selfPath);
                return false;
            }
            case 0: {
                log.info(LOG_PREFIX_OF_THREAD + "子节点中，我是最小的节点： Path: " + selfPath);
                return true;
            }
            default: {
                this.waitPath = StaticConst.GROUP_PATH + "/" + subNodes.get(index - 1);
                log.info(LOG_PREFIX_OF_THREAD + "获取子节点中，排在我前面的" + waitPath);
                try {
                    // 获取前面等待的节点信息, 获取异常则说明节点丢失
                    byte[] result = zooKeeper.getData(waitPath, true, new Stat());
                    if (result == null) {
                        log.error("获取运行中节点数据失败: 请检查节点数据！");
                    }
                    DubboService.activeIp = new String(result);
                    return false;
                } catch (KeeperException e) {
                    if (zooKeeper.exists(waitPath, false) == null) {
                        log.warn(LOG_PREFIX_OF_THREAD + "子节点中，排在我前面的" + waitPath + "已失踪，重新检查");
                        return checkMinPath();
                    } else {
                        throw e;
                    }
                }
            }

        }
    }

    /**
     * 获取锁成功
     */
    public boolean getLockSuccess() throws KeeperException, InterruptedException {
        if (zooKeeper.exists(this.selfPath, false) == null) {
            log.error(LOG_PREFIX_OF_THREAD + "本节点已不在了...");
            return false;
        }
        log.info(LOG_PREFIX_OF_THREAD + "获取锁成功，准备开启调度");
        return true;
    }

    @Override
    public void process(WatchedEvent event) {
        if (event == null) {
            return;
        }
        Event.KeeperState keeperState = event.getState();
        Event.EventType eventType = event.getType();
        if (Event.KeeperState.SyncConnected.equals(keeperState)) {
            if (Event.EventType.None == eventType) // 表示没有任何节点
            {
                log.info(LOG_PREFIX_OF_THREAD + "成功连接上ZK服务器");
            } else if (event.getType() == Event.EventType.NodeDeleted && event.getPath().equals(waitPath)) {
                log.info(LOG_PREFIX_OF_THREAD + "收到消息，前面的调度挂了，准备开启本服务调度。");
                try {
                    if (getLock()) {
                        // 重新初始化配置信息
                        this.initData();
                        // 开启本服务器调度
                        taskManager.startAllTask();
                    }
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        } else if (Event.KeeperState.Disconnected == keeperState) {
            log.error(LOG_PREFIX_OF_THREAD + "与ZK服务器断开连接");
        } else if (Event.KeeperState.AuthFailed == keeperState) {
            log.error(LOG_PREFIX_OF_THREAD + "权限检查失败");
        } else if (Event.KeeperState.Expired == keeperState) {
            log.error(LOG_PREFIX_OF_THREAD + "会话失效");
        }
    }
}
