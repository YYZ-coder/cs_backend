package com.cls.main.controller;

import com.cls.common.backdata.JsonBackData;
import com.cls.common.context.WebSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/3/28-23:02
 * Description：
 *      Test
 */
@Controller
public class A {

    protected static final Logger log = LoggerFactory.getLogger(A.class);

    @RequestMapping(value = "/",method = RequestMethod.GET)
    @ResponseBody
    public JsonBackData index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println(Thread.currentThread().getId() + " " + Thread.currentThread().getName());
        String a = "1";
        //日志记录
        log.info("/commonController --> index()函数");
        String b = (String)WebSession.getVal("uid");
       /* if(a == "1"){
            throw new ClsException(LOG_FAILED,"A.class 抛异常");
        }*/
        /*ModelAndView m = new ModelAndView();
        m.addObject(new JsonBackData(true,new UserInfo(),200));*/

        return new JsonBackData(true,null,200);
    }
}
