import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

/**
 * Project: cs_backend
 *
 * @author Yue
 * @create 2018/4/21-15:49
 * Description：
 */
public class M {

    private static Logger logger = LoggerFactory.getLogger(M.class);

    public void upload(){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        //...生成上传凭证，然后准备上传
        String accessKey = "9V0sdhCHTuxt6QgEE2vb3cPJNuh1Cg10EnvvtL9c";
        String secretKey = "231F_mPNfyY3KJ6VX8DnxJJibW2tDAHEJ5a9HcZy";
        String bucket = "cs-shop-back-data";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
//        String localFilePath = "/home/qiniu/test.mp4";
        String localFilePath = "D:\\10.jpg";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        String localTempDir = Paths.get(System.getenv("java.io.tmpdir"), bucket).toString();
        try {
            //设置断点续传文件进度保存目录
            FileRecorder fileRecorder = new FileRecorder(localTempDir);
            UploadManager uploadManager = new UploadManager(cfg, fileRecorder);
            try {
                Response response = uploadManager.put(localFilePath, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                System.out.println(response.bodyString());
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    logger.warn(ex2.toString());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.warn(ex.toString());
        }
    }

    public static void main(String args[]) {
        new M().upload();
    }

//    public void main(String args[]) throws ExecutionException {
//        ClassPathXmlApplicationContext l =new ClassPathXmlApplicationContext("classpath:spring.xml");
       /* UserServiceImpl userService = (UserServiceImpl)l.getBean("userServiceImpl");
        UserInfo userInfo = userService.selectById(1);
        System.out.println(userInfo);*/
       /*String a = new String("ve");
       String k = new String("k");
       TokenSession.saveSession(k, a, String.class);
       TokenSession.getSession(k,String.class);
*/
//        System.out.println(add(9));
//    }

    /**
     * @param a
     * @return
     */
    public static int add(int a){
        if(a == 0) return 0;
        return a - a/10 * 10 + add(a/10);
    }
}
