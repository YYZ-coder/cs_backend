package com.cls.common.gateway;

import com.cls.common.context.WebSession;
import com.cls.common.session.TokenSession;
import com.cls.common.utils.DateUtil;
import com.qiniu.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/26-23:12
 * Description：
 *  微信小程序网关拦截器:  佛系网关
 */
@Component
public class WxGateWayInterceptor extends WebSession implements AsyncHandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(WxGateWayInterceptor.class);

    /**
     * 请求拦截预处理
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try{
            String ticket = "";
            String token = request.getHeader("token");
            Date date = new Date(DateUtil.getCurrentMili());
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss SSS a");
            logger.info("wx request ---> current token:{} | time now:{}",token, format.format(date));
            if(! StringUtils.isNullOrEmpty(token)){
                ticket = TokenSession.getSession(token, String.class);
            }
            if(ticket != "" && ticket != null){
                WebSession.setAttribute("cuid",new Integer(ticket));
            }
            return true;
        }catch(Throwable t){
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    }
}
