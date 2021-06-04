package com.pmzhongguo.otc.utils;

import com.google.common.collect.Maps;
import com.pmzhongguo.ex.core.threadpool.ZzexThreadFactory;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程静态获取类
 * @author jary
 * @creatTime 2019/9/20 10:49 AM
 */
public class ZZEXExecutor {

    private static final ConcurrentMap<String, ExecutorService> executorServiceMap = Maps.newConcurrentMap();

    /**
     *
     * @param nThreads 线程个数
     * @param namePrefix  线程名
     * @return
     */
    public static ExecutorService getInstance(int nThreads, String namePrefix) {
        return Executors.newFixedThreadPool(nThreads, new ZzexThreadFactory(namePrefix));
    }
}
