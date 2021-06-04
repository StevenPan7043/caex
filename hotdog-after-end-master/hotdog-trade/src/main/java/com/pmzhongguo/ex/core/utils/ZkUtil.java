package com.pmzhongguo.ex.core.utils;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @description: 一定要写注释啊
 * @date: 2019-04-02 15:26
 * @author: 十一
 */
public class ZkUtil {

    /**
     * zk更新数据防止更新失败，重试机制；重试次数
     */
    private static final int RETRY_NUM = 5;
    /**
     * 重试间隔时间,单位：秒
     */
    private static final int INTERVAL_TIME = 1000 * 5;

    private static Logger logger = LoggerFactory.getLogger("zookeeper");

    private static ZooKeeper zk;

    private ZkUtil() {
    }

    /**
     * host: ip
     * session_timeout: 超时心跳
     * @return
     */
    public  ZooKeeper getZk() {
        if (zk == null) {
            String host = null;
            Integer timeOut = 0;
            try {
                host = PropertiesUtil.getPropValByKey("zookeeper.host");
                timeOut = Integer.parseInt(PropertiesUtil.getPropValByKey("zookeeper.session_timeout"));
                zk = new ZooKeeper(host,timeOut,null);
            } catch (IOException e) {
                logger.error("<================== 读取zookeeper配置失败,host: " + host +  " session_timeout" + timeOut + " Cause by: "+ e.getCause(),e);
            }

        }
        return zk;
    }



    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到时才会装载，从而实现了延迟加载。
     */
    private static class ZkUtilHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static ZkUtil instance = new ZkUtil();
    }

    /**
     * 当getInstance方法第一次被调用的时候，它第一次读取
     * RedisUtilHolder.instance，导致RedisUtilHolder类得到初始化；而这个类在装载并被初始化的时候，会初始化它的静
     * 态域，从而创建RedisUtil的实例，由于是静态的域，因此只会在虚拟机装载类的时候初始化一次，并由虚拟机来保证它的线程安全性。
     * 这个模式的优势在于，getInstance方法并没有被同步，并且只是执行一个域的访问，因此延迟初始化并没有增加任何访问成本。
     */
    public static ZkUtil getInstance() {
        return ZkUtilHolder.instance;
    }

    /**
     *
     * @param path 创建路径
     * @param data 存储的数据
     * @return
     */
    public  boolean createPath( String path, String data ) {
        try {
            ZooKeeper zk = getZk();
            if(zk == null) {
                logger.error("<================== 获取zk失败");
                return false;
            }
            String zkPath =  zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            return true;

        } catch ( KeeperException e ) {
            logger.error( "<================== 节点创建失败, 发生KeeperException! path: " + path + ", data: " + data
                    + ", errMsg:" + e.getCause(), e );
        } catch ( InterruptedException e ) {
            logger.error( "<================== 节点创建失败, 发生 InterruptedException! path: " + path + ", data: " + data
                    + ", errMsg:" + e.getCause(), e );
        }
        return false;
    }

    public boolean writeData( String path, String data){
        int retryNum = RETRY_NUM;
        new Thread(() -> {
            writeData(path,data,retryNum);
        }).start();
        return true;
    }

    /**
     * zk 写入数据
     * @param path 路径
     * @param data 数据
     * @param retryNum 重复写多少次
     * @return
     */
    public boolean writeData( String path, String data,int retryNum){
        while (retryNum > 0) {
            retryNum --;
            try {
                ZooKeeper zk = getZk();

                if (zk == null) {
                    logger.error("<================== 获取zk失败");
                    // 重试休眠
                    retrySleep();
                    continue;
                }
                // 不存在就创建路径
                if (!isExists(path)) {
                    createPath(path,data);
                    logger.error("<================== zk写入数据成功");
                }else  {
                    zk.setData(path, data.getBytes(), -1);
                    logger.error("<================== zk写入数据成功");
                }

            } catch (KeeperException e) {
                logger.error( "<================== 更新数据失败, 发生KeeperException! path: " + path + ", data:" + data
                        + ", errMsg:" + e.getMessage(), e );
            } catch (InterruptedException e) {
                logger.error( "<================== 更新数据失败, 发生InterruptedException! path: " + path + ", data:" + data
                        + ", errMsg:" + e.getMessage(), e );
            }
            retrySleep();
        }
        return true;
    }

    /**
     * 重试休眠
     */
    private void retrySleep() {
        try {
            // 休眠时间
            Thread.sleep(INTERVAL_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    /**
     * <p>判断某个zNode节点是否存在, Stat exists(path<节点路径>, watch<并设置是否监控这个目录节点，这里的 watcher 是在创建 ZooKeeper 实例时指定的 watcher>)</p>
     * @param path zNode节点路径
     * @return 存在返回true,反之返回false
     */
    public boolean isExists( String path ){
        try {
            ZooKeeper zk = getZk();
            if (zk == null) {
                logger.error("<================== 获取zk失败");
                return false;
            }
            Stat stat = zk.exists( path, false );
            return null != stat;
        } catch (KeeperException e) {
            logger.error( "<================== 读取数据失败,发生KeeperException! path: " + path
                    + ", errMsg:" + e.getMessage(), e );
        } catch (InterruptedException e) {
            logger.error( "<================== 读取数据失败,发生InterruptedException! path: " + path
                    + ", errMsg:" + e.getMessage(), e );
        }
        return false;
    }

    /**
     * 关闭ZK连接
     */
    public void releaseConnection() {

        if (zk != null) {
            try {
                zk.close();
                zk = null;
            } catch (InterruptedException e) {
                logger.error( "<================== 关闭zk失败, 发生InterruptedException! errMsg: " + e.getMessage(), e );
            }
        }
    }


}
