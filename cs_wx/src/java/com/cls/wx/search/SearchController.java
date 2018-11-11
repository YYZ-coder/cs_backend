package com.cls.wx.search;

import com.cls.common.backdata.JsonBackData;
import com.cls.common.base.BaseController;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/5/12-21:18
 * Description：
 *  搜索
 */
@RequestMapping("/wx/search")
@Controller
public class SearchController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(SearchController.class);


    private void wrapBackData(){

    }

    /**
     * 搜索关键字接口
     * @param key
     * @return
     */
    @RequestMapping(value = "/bykey", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData searchByKey(
            @RequestParam("key") @NotNull @Valid @Length(min = 1) String key
    ){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            //搜索店铺/商品

        }catch(Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData, t);
        }
        return jsonBackData;
    }
}
