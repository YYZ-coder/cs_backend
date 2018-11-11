package com.cls.common.qiniu;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/5/18-12:27
 * Description：
 */
public class CommonUtil {

    /**
     * MultipartFile 转换成File
     * @param multfile 原文件类型
     * @return File
     */
    public File multipartToFile(MultipartFile multfile) {

        CommonsMultipartFile cf = (CommonsMultipartFile)multfile;
        //这个myfile是MultipartFile的
        DiskFileItem fi = (DiskFileItem) cf.getFileItem();
        File file = fi.getStoreLocation();
        return file;
    }
}
