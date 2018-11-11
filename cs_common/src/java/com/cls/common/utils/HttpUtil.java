package com.cls.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Project: wx_cshop_end
 * @author Yue
 * @create 2018/1/23-10:16
 * Description：
 *      http请求操作
 */
public class HttpUtil {

    /**
     * 通过HttpGet类发送GET请求并获取返回信息
     * @param path 发送至的网址
     * @return String
     */
    public static String httpGet(String path){
        if(path==null){
            return null;
        }
        return request(new HttpGet(path));
    }

    public static String httpPost(String path){
        if(path == null){
            return null;
        }
        return request(new HttpPost(path));
    }

    private static String request(HttpUriRequest req){
        String res = null;
        try{
            HttpResponse response = HttpClients.createDefault().execute(req);
            HttpEntity entity = response.getEntity();
            res = EntityUtils.toString(entity);
        }catch(IOException e){
            e.printStackTrace();
        }
        return res;
    }
}
