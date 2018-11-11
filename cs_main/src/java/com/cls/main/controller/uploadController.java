package com.cls.main.controller;

import com.cls.common.backdata.JsonBackData;
import com.cls.common.base.BaseController;
import com.cls.common.exception.ClsException;
import com.cls.common.services.resource.entity.PicInfo;
import com.cls.common.services.resource.service.impl.ResourceServiceImpl;
import com.cls.common.stat.LogStat;
import com.cls.common.utils.DateUtil;
import com.cls.common.utils.UploadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/5/18-16:49
 * Description：
 */
@RequestMapping(value = "/web/load")
@Controller
public class uploadController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(uploadController.class);

    @Autowired
    private ResourceServiceImpl resourceService;

    /**
     *
     * @return
     */
    @RequestMapping(value = "/up",method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData upload(
            MultipartFile file,
            HttpServletRequest request
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            Integer uid = getUID();
            Integer type = new Integer(request.getHeader("type"));
            Integer oid =  new Integer(request.getHeader("oid"));
            Integer attribute = new Integer(request.getHeader("attribute"));
            if(!checkInteger(type) || !checkInteger(oid) || !checkInteger(attribute)){
                throw new ClsException(LogStat.PARAM_LEAK);
            }
            String url = UploadUtil.uploadImgToQiuniu(file);
            PicInfo picInfo = new PicInfo();
            picInfo.setAttribute(attribute);
            picInfo.setUrl(url);
            picInfo.setObjId(oid);
            picInfo.setType(type);
            picInfo.setUpdateTime(DateUtil.getCurrentMili());
            picInfo.setCreateTime(DateUtil.getCurrentMili());
            picInfo.setCreateUser(uid);
            picInfo.setUpdateUser(uid);
            resourceService.addNew(picInfo);
            jsonBackData.setBackData("插入图片数据成功");
        }catch(Throwable throwable){
            logger.warn(throwable.toString());
            wrapException(jsonBackData, throwable);
        }
        return jsonBackData;
    }

}
