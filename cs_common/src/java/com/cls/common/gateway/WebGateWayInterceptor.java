package com.cls.common.gateway;

import com.alibaba.fastjson.JSONObject;
import com.cls.common.backdata.JsonBackData;
import com.cls.common.context.WebSession;
import com.cls.common.exception.ClsException;
import com.cls.common.session.TokenSession;
import com.cls.common.stat.LogStat;
import com.cls.common.utils.DateUtil;
import com.qiniu.util.Json;
import com.qiniu.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Project: cs_backend
 *
 * @author Yue
 * @create 2018/5/15-20:17
 * Description：
 */
@Component
public class WebGateWayInterceptor extends WebSession implements AsyncHandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(WebGateWayInterceptor.class);

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    }

    /**
     * 请求预处理
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //设置header，解决跨域
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Headers", "sid,oid,type,attribute,groupId,token,Origin, X-Requested-With, Content-Type, Accept");
        response.setContentType("application/json; charset=utf-8");
        JsonBackData jsonBackData = new JsonBackData(false);
        PrintWriter printWriter = null;
        try{
            String spath = request.getServletPath();
            String token = request.getHeader("token");
            String groupId = request.getHeader("groupId");
            Date date = new Date(DateUtil.getCurrentMili());
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss SSS a");
            logger.info("web request ---> current token:{} | time now:{}",token, format.format(date));
            if(spath.equals("/web/log/in")){
                String gid = request.getHeader("groupId");
                if(gid == null || gid.equals("")){
                    throw new ClsException(LogStat.NO_ROLES);
                }
                WebSession.setAttribute("rid",new Integer(gid));
                return true;
            }
            if(spath.equals("/web/log/verify")){
                WebSession.setAttribute("token",token);
                return true;
            }
            if(spath.equals("/web/log/regin")){
                return true;
            }
            if(StringUtils.isNullOrEmpty(token) || StringUtils.isNullOrEmpty(groupId)){
                throw new ClsException(LogStat.NO_TOKEN);
            }
            String val = TokenSession.getIfPresent(token);
            if(val == null || val.equals("")){
                throw new ClsException(LogStat.EXPIRED_TOKEN);
            }
            String strs[] = val.split("_");
            if(strs.length < 2){
                throw new ClsException(LogStat.DATA_FALSE);
            }
            WebSession.setAttribute("cuid",new Integer(strs[0]));
            WebSession.setAttribute("rid",new Integer(strs[1]));
            return true;
        }catch(Throwable t){
            try {
                printWriter = response.getWriter();
                if(t instanceof ClsException){
                    jsonBackData.setResult(((ClsException) t).getCode());
                }else{
                    jsonBackData.setResult(4000);
                }
                jsonBackData.setSuccess(false);
                jsonBackData.setBackData(t.getMessage());
                printWriter.write(JSONObject.toJSONString(jsonBackData));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        finally {
            if(printWriter != null){
                printWriter.close();
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
