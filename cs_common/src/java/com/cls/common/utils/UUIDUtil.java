package com.cls.common.utils;

import java.util.UUID;

/**
 * Project: wx_cshop_end
 * @author Yue
 * @create 2018/1/23-12:20
 * Description：
 *      生成随机UUID
 */
public class UUIDUtil {

    /**
     * 随机生成UUID
     * @return String
     */
    public static String generateUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
