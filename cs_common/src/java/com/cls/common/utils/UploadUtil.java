package com.cls.common.utils;

import com.cls.common.Constant;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Project: cs_backend
 *
 * @author Yue
 * @create 2018/5/18-20:02
 * Description：
 */
public class UploadUtil {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(UploadUtil.class);

    /**
     * 上传图片
     * @param file
     * @return String
     * @throws Exception
     */
    public static String uploadImgToQiuniu(MultipartFile file) throws Exception {
        String imgUrl = "";
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);
        String accessKey = Constant.ACCESS_KEY;
        String secretKey = Constant.SECRET_KEY;
        String bucket = Constant.BUCKET;
        // 如果不指定 key 值，以文件内容的 hash 值作为文件名
        // 获取原文件名的后缀
        String originalFilename = file.getOriginalFilename();
        String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
        String key = UUIDUtil.generateUUID() + extName;
        try {
            byte[] uploadBytes = file.getBytes();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(uploadBytes, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                imgUrl = "/" + putRet.key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    logger.warn(ex2.toString());
                }
            }
        } catch (UnsupportedEncodingException ex) {
            logger.warn(ex.toString());
        }
        return imgUrl;
    }
}
