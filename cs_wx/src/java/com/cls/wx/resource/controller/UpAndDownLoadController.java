package com.cls.wx.resource.controller;

import com.cls.common.backdata.JsonBackData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/5/1-15:48
 * Description：
 *      上传下载资源
 */
@RequestMapping("/toremote")
@Controller
public class UpAndDownLoadController {

    public JsonBackData upLoad(){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        return jsonBackData;
    }

    public JsonBackData downLoad(){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        return jsonBackData;
    }

}
