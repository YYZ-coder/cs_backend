package com.cls.wx.user.controller;

import com.cls.common.backdata.JsonBackData;
import com.cls.common.exception.NotNullException;
import com.cls.common.session.TokenSession;
import com.google.common.base.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/2-23:34
 * Description：
 *      维护Session不过期
 */
@Controller
@RequestMapping("/wx/mt_session")
public class MaintainSessionController {

    /**
     * Maintain微信小程序的session
     * @param token
     * @return JsonBackData
     */
    @RequestMapping(value = "/hold",method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData holdSession(@RequestParam("token")String token){
        JsonBackData jsonBackData = new JsonBackData();
        try{
            if(Objects.isNull(token) || "".equals(token)){
                throw new NotNullException("token不为空",this.getClass());
            }
            if(Strings.isNullOrEmpty(TokenSession.getSession(token,String.class))){
                throw new NotNullException("token本地缓存为空",this.getClass());
            }
            jsonBackData.setSuccess(true);
            jsonBackData.setResult(200);
        }catch(Exception e){
            jsonBackData.setSuccess(false);
            jsonBackData.setResult(500);
        }finally {
            return jsonBackData;
        }
    }

}
