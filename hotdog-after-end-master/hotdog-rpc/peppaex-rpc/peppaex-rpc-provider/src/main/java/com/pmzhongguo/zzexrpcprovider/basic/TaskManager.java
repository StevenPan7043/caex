/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/22 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.basic;

import com.pmzhongguo.zzextool.annotation.TaskService;
import com.pmzhongguo.zzextool.basic.AbstractBaseTask;
import com.pmzhongguo.zzextool.consts.StaticConst;
import com.pmzhongguo.zzextool.utils.IOHelper;
import com.pmzhongguo.zzextool.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/22 9:59
 * @description：调度管理器, 统一启动和关闭
 * @version: $
 */
@Slf4j
@Component
public class TaskManager {

    /**
     * Bean对象容器
     */
    private static Map<String, Object> beanContainer = new HashMap<>();
    @Autowired
    private AutowireCapableBeanFactory beanFactory;
    @Autowired
    private DefaultListableBeanFactory defaultListableBeanFactory;

    /**
     * 异步写入配置信息到文件中
     *
     * @param flag
     */
    public static void writeIOAsync(boolean flag) {
        new Thread(() -> {
            //开启调度的时候在外部创建文件并写入键值对
            IOHelper ioHelper = new IOHelper();
            String content = ioHelper.readFile(null);
            if (StringUtils.isEmpty(content)) {
                content = "flag=0\r\n";
            }
            if (flag) {
                ioHelper.saveFile(null, content);
                return;
            }
            if (content.contains("flag=0\r\n")) {
                content = content.replace("flag=0\r\n", "flag=1\r\n");
            } else if (!content.contains("flag=1\r\n")) {
                content = "flag=1\r\n" + content;
            }
            ioHelper.saveFile(null, content);
        }).start();
    }

    /**
     * 扫描包下的所有task任务，并初始化
     */
    public void startAllTask() throws IllegalAccessException, InstantiationException {
        Reflections f = new Reflections("com.pmzhongguo");
        Set<Class<?>> set = f.getTypesAnnotatedWith(TaskService.class);
        for (Class<?> c : set) {
            Object bean = c.newInstance();
            TaskService annotation = c.getAnnotation(TaskService.class);
            // 判断task是否继承了调度基类
            if (!(bean instanceof AbstractBaseTask)) {
                log.error("调度实现了@TaskService注解, 没有继承AbstractBaseTask。name=" + annotation.value());
                continue;
            }
            beanContainer.put(annotation.value(), bean);
            log.warn("<================>TaskManager正在开启调度: name=" + annotation.value() + "<================>");

            //将new出的对象放入Spring容器中
            defaultListableBeanFactory.registerSingleton(StringUtil.toLowerCaseFirstOne(annotation.value()), bean);
            //自动注入依赖
            beanFactory.autowireBean(bean);
            // 调用start方法
            startTask(bean);
        }
        writeIOAsync(false);
        StaticConst.TASK_STATUS = 1;
    }

    /**
     * 停止所有的调度
     */
    public void stopAllTask() {
        for (Map.Entry<String, Object> map : beanContainer.entrySet()) {
            log.warn("正在停止调度： name=" + map.getKey());
            stopTask(map.getValue());
        }
        //开启调度的时候在外部创建文件并写入键值对
        IOHelper ioHelper = new IOHelper();
        String content = ioHelper.inputStream(null);
        log.warn("读取到配置文件信息: status.config : " + content);
        ioHelper.outputStream(null, content.replace("flag=1", "flag=0"));
    }

    /**
     * 重启调度
     *
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public void restartAllTask() throws InstantiationException, IllegalAccessException {
        stopAllTask();
        startAllTask();
    }

    /**
     * 反射调用bean的startTask方法，要求task都要继承AbstractBaseTask
     *
     * @param bean
     */
    private void startTask(Object bean) {
        Method method = ReflectionUtils.findMethod(bean.getClass(), "startTask", AbstractBaseTask.class);
        ReflectionUtils.invokeMethod(method, bean, bean);
    }

    private void stopTask(Object bean) {
        Method method = ReflectionUtils.findMethod(bean.getClass(), "stopTask", AbstractBaseTask.class);
        ReflectionUtils.invokeMethod(method, bean, null);
    }
}
