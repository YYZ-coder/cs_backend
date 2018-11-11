package com.cls.common.utils;

import java.io.File;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/22-18:50
 * Description：
 */
public class CommonUtil {

    /**
     * 获得文件bytes
     * @param file
     * @return byte[]
     */
    public static byte[] getFileBytes(File file){
        byte[] bytes = SerializeUtil.serialize(file);
        return bytes;
    }


}
