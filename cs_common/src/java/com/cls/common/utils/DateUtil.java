package com.cls.common.utils;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/27-1:05
 * Description：
 */
public class DateUtil {

    /**
     * 获得当前秒数
     * @return
     */
    public static Long getCurrentSecond(){
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获得当前毫秒数
     * @return
     */
    public static Long getCurrentMili(){
        return System.currentTimeMillis();
    }
}
