package com.cls.common.filter;

import com.cls.common.context.WebSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/5-14:47
 * Description：
 *  请求/回复过滤器
 */
public class WxRequestReplyFilter extends WebSession implements Filter {

    /** RequestReplyFilter的日志记录器 */
    private static Logger logger = LoggerFactory.getLogger(WxRequestReplyFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * 进行过滤/包装操作
     * @param req
     * @param res
     * @param filterChain
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) {
        System.out.println(Thread.currentThread().getId() + " " + Thread.currentThread().getName());
        //判断头部token信息
        HttpServletRequest request = (HttpServletRequest)req;
        //TODO: 获得请求url,进行权限分级/wx开头的放任,其他开头的
        //TODO: 拿到token进行解密和验证处理
        Integer cuid = new Integer(((HttpServletRequest) req).getHeader("uid"));
        WebSession.setAttribute("cuid",cuid);
        String token = request.getHeader("JSESSIONID");
        //校验token,并且放到WebSession中，进行分发
        WebSession.setAttribute("cuid",2);
        try {
            filterChain.doFilter(req,res);
        } catch (Exception e) {
            logger.warn( e.toString());
        }
    }

    /**
     * 销毁Filter
     */
    @Override
    public void destroy() {
        logger.info(this.getClass().getSimpleName()+": has destroyed at "+ LocalDate.now().toString() + "-" + LocalTime.now().toString());
    }
}
