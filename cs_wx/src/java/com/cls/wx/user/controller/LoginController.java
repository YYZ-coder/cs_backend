package com.cls.wx.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cls.common.backdata.JsonBackData;
import com.cls.common.base.BaseController;
import com.cls.common.exception.ClsException;
import com.cls.common.services.user.service.impl.UserServiceImpl;
import com.cls.common.session.TokenSession;
import com.cls.common.stat.LogStat;
import com.cls.common.utils.DateUtil;
import com.cls.common.services.user.entity.UserInfo;
import com.cls.common.utils.EncryUtil;
import com.cls.common.utils.HttpUtil;
import com.cls.common.utils.UserUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Project: wx_cshop_end
 * @author Yue
 * @create 2018/1/22-12:28
 * Description：
 *      登录Controller
 */
@RequestMapping("/wlog")
@Controller
public class LoginController extends BaseController{

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    /** 用于用户的数据库存储查询 */
    @Autowired
    private UserServiceImpl userService;

    /**
     * 生成可校验token
     * @param uid
     * @return String
     */
    private String genToken(Integer uid){
        return EncryUtil.encryptByMD5(uid.toString());
    }

    /**
     * 包装成UserInfo
     * @param userInfoString
     * @param open_id
     * @return UserInfo
     */
    private UserInfo wrapToUserInfo(String userInfoString,String open_id){
        JSONObject jsonObject = JSON.parseObject(userInfoString);
        return  new UserInfo(
                open_id,
                0,
                0,
                jsonObject.getString("nickName"),
                "",
                "",
                0,
                jsonObject.getString("avatarUrl"),
                jsonObject.getString("city"),
                jsonObject.getString("province"),
                jsonObject.getString("country"),
                jsonObject.getInteger("gender"),
                jsonObject.getString("language"));
    }

    /**
     * 小程序缓存中没有openid的key，则后台重新生成token，并新建维护登录态
     * @param code 用户调用login获得的code(用来获得session_Key和openId)
     * @param data 用户的数据(已加密)
     * @param iv
     * @return JsonBackData
     */
    @RequestMapping(value = "/in",method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData login(@RequestParam(value = "code") String code,
                        @RequestParam(value = "encryptedData") String data,
                        @RequestParam(value = "iv") String iv)
    {
        JsonBackData jsonBackData = new JsonBackData(true,null,200);
        logger.info("user :获得的前台登录的code为：{} || nowTime: {}",code,System.currentTimeMillis());
        Integer uid = 0;
        UserInfo userIf;
        JSONObject json = JSON.parseObject(HttpUtil.httpGet(UserUtil.getWebAccess(code)));
        String session_key = (String)json.get("session_key");
        String open_id = (String)json.get("openid");
        try {
            //解密userInfo
            String userInfoString = EncryUtil.decryptWxUserInfo(data,session_key,iv,"UTF-8");
            List<UserInfo> userInfoList  = userService.findByOpenId(open_id);
            if(CollectionUtils.isNotEmpty(userInfoList)){
                uid = userInfoList.get(0).getId();
                userIf = userInfoList.get(0);
                //TODO:可进行完整性校验
                userIf.setUpdateTime(DateUtil.getCurrentMili());
                userIf.setLogCount(userIf.getLogCount()+1);
                userIf.setUpdateUser(userIf.getId());
                if(0 == userService.updateWxLogRecord(userIf)){
                    throw new ClsException(LogStat.UPDATE_FAILED);
                }
                logger.info("更新用户登录次数和时间记录-uid:"+userIf.getId());
            }else{
                UserInfo userInfo = wrapToUserInfo(userInfoString, open_id);
                uid = userService.addUser(userInfo);
                logger.info("添加新用户成功-uid:"+uid);
            }
            String token = genToken(uid);
            TokenSession.saveSession(token , uid.toString(), String.class);
            jsonBackData.setBackData(token);
        } catch (Throwable e) {
            logger.warn(e.toString());
            wrapException(jsonBackData,e);
        }
        return jsonBackData;
    }

    /**
     *  验证用户会话是否超时
     * @param sessionId
     * @return JsonBackData
     */
    @RequestMapping(value = "/confirm",method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData confirm(
            @RequestParam(value = "sessionId") String sessionId){
        logger.info("confirm 会话Id：{}",sessionId);
        JsonBackData jsonBackData = new JsonBackData(true,null,200);
        try{
            if(TokenSession.getSession(sessionId,String.class) != null){
                jsonBackData.setBackData(new String("valid Session"));
            }else{
                throw new Exception("当前用户Session失效");
            }
        }catch(Exception e) {
            logger.info("confirm 异常：{}", e.getMessage());
            wrapException(jsonBackData,e);
        }
        return jsonBackData;
    }






}