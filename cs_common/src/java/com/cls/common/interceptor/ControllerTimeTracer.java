package com.cls.common.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/2-22:44
 * Description：
 *      方法执行前后执行其他操作
 */
@Aspect
@Component
public class ControllerTimeTracer {

    private static final Logger logger = LoggerFactory.getLogger(ControllerTimeTracer.class);

    /**
     * around函数
     * @param proceedingJoinPoint
     * @throws Throwable
     */
    @Around(value = "execution(public * com.cls.*.*.controller.*.*(..)) ||" +
            "@annotation(org.springframework.web.bind.annotation.ResponseBody)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) {
        Object obj = null;
        try{
            long startTime = System.currentTimeMillis();
            obj = proceedingJoinPoint.proceed();
            long endTime = System.currentTimeMillis();
            logger.info("接口" + proceedingJoinPoint.getSignature() + " 耗时:" + (endTime-startTime)+"ms");
        }catch(Throwable t){
            logger.warn("接口耗时方法异常："+t.toString());
        }
        return obj;
    }
}
