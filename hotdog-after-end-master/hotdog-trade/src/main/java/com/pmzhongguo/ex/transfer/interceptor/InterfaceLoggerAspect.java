package com.pmzhongguo.ex.transfer.interceptor;

import com.google.gson.Gson;
import com.pmzhongguo.ex.business.dto.AccountDetailDto;
import com.pmzhongguo.ex.business.service.AccountDetailService;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.OptSourceEnum;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.transfer.controller.NRSTransferController;
import com.pmzhongguo.ex.transfer.entity.ThirdPartyInfo;
import com.pmzhongguo.ex.transfer.service.ThirdPartyService;
import com.pmzhongguo.otc.otcenum.AccountOperateTypeEnum;
import com.pmzhongguo.zzextool.utils.JsonUtil;
import com.qiniu.util.BeanUtil;
import com.qiniu.util.DateStyleEnum;
import com.qiniu.util.DateUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 第三方接口慢运行监控
 * @author jary
 * @creatTime 2019/9/28 1:46 PM
 */
@Component
@Aspect
public class InterfaceLoggerAspect {


    private Logger logger = LoggerFactory.getLogger(InterfaceLoggerAspect.class);


    @Pointcut(value = "execution(public * com.pmzhongguo.ex.transfer.controller.*Controller.*(..))")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        Object[] args = this.gerFilterArgs(joinPoint.getArgs());
        String params = BeanUtil.isEmpty(args) ? "" : JsonUtil.beanToJson(args[0]);

        String declaringTypeName = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        Object result = null;
        try {
            long startTimeMillis = System.currentTimeMillis();
            //调用 proceed() 方法才会真正的执行实际被代理的方法
            result = joinPoint.proceed();
            long execTimeMillis = System.currentTimeMillis() - startTimeMillis;
            if (execTimeMillis > 5000) {
                logger.info("***********************");
                logger.info("\n Class Name :[{}],\n Method Name : [{}], \n params:{} ,\n result :{},\n execute time : {} millis. ", declaringTypeName, methodName, params, new Gson().toJson(result), execTimeMillis);
            }
            if (methodName.contains("fundTransferDeposit")) {
                ObjResp objResp = JsonUtil.jsonToBean(JsonUtil.beanToJson(result), ObjResp.class);
                if (objResp.getState().equals(Resp.FAIL)) {
                    return result;
                }
            }
            return result;
        } catch (Throwable te) {
            logger.error(te.getMessage(), te);
            return result;
        }
    }

    private Object[] gerFilterArgs(Object[] args) {
        Object[] arguments = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ServletRequest || args[i] instanceof ServletResponse || args[i] instanceof MultipartFile) {
                continue;
            }
            arguments[i] = args[i];
        }
        return arguments;
    }
}
