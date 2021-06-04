/**
 * zzex.com Inc.
 * Copyright (c) 2019/5/14 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.config;

import com.alibaba.dubbo.rpc.*;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.pmzhongguo.zzextool.enums.CommonEnum;
import com.pmzhongguo.zzextool.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ：yukai
 * @date ：Created in 2019/5/14 17:01
 * @description：dubbo 服务调用拦截, 日志打印
 * @version: $
 */
@Slf4j
public class DubboLogFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        //开始时间
        long startTime = System.currentTimeMillis();
        //执行接口调用逻辑
        Result result = invoker.invoke(invocation);
        //调用耗时
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed > 3000) {
            log.warn("接口请求消耗时间: " + elapsed + "秒！");
        }
        //如果发生异常 则打印异常日志
        if (result.hasException() && invoker.getInterface() != GenericService.class) {
            if (result.getException() instanceof StackOverflowError) {
                log.error("<============== 堆栈溢出: " + result.getException().getLocalizedMessage());
                result = new RpcResult(new BusinessException(1000, "<============== 堆栈溢出!请查看调度日志!"));
            } else {
                log.error("<============== dubbo执行异常: ", result.getException());
                result = new RpcResult(new BusinessException(1000, CommonEnum.LANG_SYSTEM_BUSY.getErrorENMsg()));
            }
        } else {
            //打印正确响应日志
            /*DubboServiceResponse serviceResponse = new DubboServiceResponse();
            serviceResponse.setMethodName(invocation.getMethodName());
            serviceResponse.setInterfaceName(invocation.getInvoker().getInterface().getName());
            serviceResponse.setArgs(invocation.getArguments());
            serviceResponse.setResult(new Object[]{result.getValue()});
            serviceResponse.setSpendTime(elapsed);
            log.info("dubbo服务响应成功,返回数据: " + JSON.toJSONString(result));*/
        }
        //返回结果响应结果
        return result;
    }
}
