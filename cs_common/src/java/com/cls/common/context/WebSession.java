package com.cls.common.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/21-18:18
 * Description：
 *      跟随线程的数据
 */
public class WebSession {

    private static ThreadLocal<Map<String, Object>> local = new ThreadLocal<Map<String, Object>>(){
        @Override
        protected Map initialValue(){
            return Collections.synchronizedMap(new HashMap<>(10));
        }
    };

    /**
     * 设置值
     * @param key
     * @param val
     */
    protected static void setAttribute(String key, Object val){
        local.get().put(key,val);
    }

    /**
     * get
     * @param key
     * @return
     */
    public static Object getVal(String key){
        return local.get().get(key);
    }

    /**
     * getMap
     * @return
     */
    public static Map getMap(){
        return local.get();
    }


}
