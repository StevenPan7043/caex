/**
 * zzex.com Inc.
 * Copyright (c) 2019/5/25 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.controller;

import com.pmzhongguo.zzexrpcprovider.scheduler.KLinesTask;
import com.pmzhongguo.zzexrpcprovider.scheduler.MarketTask;
import com.pmzhongguo.zzexrpcprovider.scheduler.StaticTask;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ：yukai
 * @date ：Created in 2019/5/25 16:27
 * @description：调度管理控制器
 * @version: $
 */
@Controller
@RequestMapping(value = "/task")
public class TaskManagerController {

    @Autowired
    private StaticTask staticTask;

    @Autowired
    private MarketTask marketTask;

    @Autowired
    private KLinesTask kLinesTask;

    /**
     * 开启调度
     *
     * @param type
     * @return
     */
    @RequestMapping(value = "/{type}/start")
    @ResponseBody
    public String startTask(@PathVariable String type) {
        if (StringUtils.isEmpty(type)) {
            return "validate url param";
        }
        String message = "success";
        switch (type) {
            case "static":
                staticTask.startTask(staticTask);
                break;
            case "kline":
                kLinesTask.startTask(kLinesTask);
                break;
            case "market":
                marketTask.startTask(marketTask);
                break;
            default:
                return "failed";
        }
        return message;
    }

    /**
     * 关闭调度
     *
     * @param type
     * @return
     */
    @RequestMapping(value = "/{type}/stop")
    @ResponseBody
    public String stopTask(@PathVariable String type) {
        if (StringUtils.isEmpty(type)) {
            return "validate url param";
        }
        String message = "success";
        switch (type) {
            case "static":
                staticTask.stopTask();
                break;
            case "kline":
                kLinesTask.stopTask();
                break;
            case "market":
                marketTask.stopTask();
                break;
            default:
                message = "failed";
        }
        return message;
    }

    /**
     * 关闭调度
     *
     * @param type
     * @return
     */
    @RequestMapping(value = "/{type}/reset")
    @ResponseBody
    public String resetTask(@PathVariable String type, String cron) {
        if (StringUtils.isEmpty(type)) {
            return "validate url param";
        }
        String message = "success";
        switch (type) {
            case "static":
                staticTask.reset("executeTask", cron, staticTask);
                break;
            case "kline":
                kLinesTask.reset("executeTask", cron, staticTask);
                break;
            case "market":
                marketTask.reset("executeTask", cron, staticTask);
                break;
            default:
                message = "failed";
        }
        return message;
    }

}
