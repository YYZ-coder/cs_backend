package com.cls.common.interceptor;

import com.cls.common.backdata.JsonBackData;
import com.cls.common.exception.ClsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.json.Json;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/5-15:45
 * Description：
 *      请求/回复拦截器:主要处理异常，构造JsonBackData
 */
@Component
public class RequestReplyExceptionResolver implements HandlerExceptionResolver, HandlerMethodReturnValueHandler {

    /** 日志 */
    private static Logger logger = LoggerFactory.getLogger(RequestReplyExceptionResolver.class);

    /** 返回给前端的数据对象 */
    private JsonBackData jsonBackData = new JsonBackData();

    /**
     * controller执行失败(拦截异常)
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return ModelAndView
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        logger.info("resolveException:{} -executed at {}",this.getClass().getSimpleName(), LocalTime.now());
        logger.info("resolveException:throw exception:{}",ex.toString());
        System.out.println(ex.getClass());
        ModelAndView modelAndView = new ModelAndView();
        try {
            ex.getMessage();
            PrintWriter printWriter = response.getWriter();
            printWriter.write("error");
            if(ex instanceof  ClsException){
                ClsException exception = (ClsException)ex;
                System.out.println("status:" + ";cause:"+exception.getCause());
                jsonBackData.setResult(response.getStatus());
                jsonBackData.setBackData(exception.getMessage());
                modelAndView.addObject(jsonBackData);
                modelAndView.setView(null);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            modelAndView.setViewName("");
        }
        return modelAndView;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        if(returnType.hasMethodAnnotation(ResponseBody.class)){
            return true;
        }
        return false;
    }

    /**
     * 处理返回值
     * @param returnValue
     * @param returnType
     * @param mavContainer
     * @param webRequest
     * @throws Exception
     */
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
    }

}
