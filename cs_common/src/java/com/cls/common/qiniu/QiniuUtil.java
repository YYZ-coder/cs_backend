package com.cls.common.qiniu;


import com.alibaba.fastjson.JSONException;
import net.sf.ehcache.util.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Project: cs_backend
 *
 * @author Yue
 * @create 2018/5/18-12:28
 * Description：
 */
public class QiniuUtil {

    /*private static Logger logger = LoggerFactory.getLogger(QiniuUtil.class);

    PropertyUtil propertyUtil = new PropertyUtil();
    private String bucketName = propertyUtil.getProperty("bucketName");
    private String domain = propertyUtil.getProperty("domain");
    private String ACCESS_KEY = propertyUtil.getProperty("ACCESS_KEY");
    private String SECRET_KEY = propertyUtil.getProperty("SECRET_KEY");

    //通过文件路径上传文件
    public ExecuteResult<String> uploadFile(String localFile) throws AuthException, JSONException {
        File file = new File(localFile);
        *//**
         * 文件后缀名 文件扩展名
         *//*
        String filenameExtension = localFile.substring(localFile.lastIndexOf("."), localFile.length());
        return uploadFile(file, filenameExtension);
    }

    //通过File上传
    public ExecuteResult<String> uploadFile(File file, String filenameExtension) throws AuthException, JSONException {
        ExecuteResult<String> executeResult = new ExecuteResult<String>();
        String uptoken = getUpToken();

        // 可选的上传选项，具体说明请参见使用手册。
        PutExtra extra = new PutExtra();
        SimpleDateFormat time = new SimpleDateFormat("yyyy/MM/dd");
        // 上传文件
        PutRet ret = IoApi.putFile(uptoken, time.format(new Date()) + "/" + UUID.randomUUID() + filenameExtension, file.getAbsolutePath(), extra);

        if (ret.ok()) {
            executeResult.setSuccessMessage("上传成功!");
            executeResult.setResult(domain+ret.getKey());
        } else {
            executeResult.addErrorMessage("上传失败");
        }
        return executeResult;
    }

    *//**
     * 从 inputstream 中写入七牛
     *
     * @param key     文件名
     * @param content 要写入的内容
     * @return
     * @throws AuthException
     * @throws JSONException
     *//*
    public boolean uploadFile(String key, String content) throws AuthException, JSONException {
        // 读取的时候按的二进制，所以这里要同一
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());

        String uptoken = getUpToken();

        // 可选的上传选项，具体说明请参见使用手册。
        PutExtra extra = new PutExtra();

        // 上传文件
        PutRet ret = IoApi.Put(uptoken, key, inputStream, extra);

        if (ret.ok()) {
            return true;
        } else {
            return false;
        }
    }

    //获得下载地址
    public String getDownloadFileUrl(String filename) throws Exception {
        Mac mac = getMac();
        String baseUrl = URLUtils.makeBaseUrl(domain, filename);
        GetPolicy getPolicy = new GetPolicy();
        String downloadUrl = getPolicy.makeRequest(baseUrl, mac);
        return downloadUrl;
    }

    //删除文件
    public void deleteFile(String filename) {
        Mac mac = getMac();
        RSClient client = new RSClient(mac);
        client.delete(domain, filename);
    }

    //获取凭证
    private String getUpToken() throws AuthException, JSONException {
        Mac mac = getMac();
        PutPolicy putPolicy = new PutPolicy(bucketName);
        String uptoken = putPolicy.token(mac);
        return uptoken;
    }

    private Mac getMac() {
        Mac mac = new Mac(ACCESS_KEY, SECRET_KEY);
        return mac;
    }*/
}
