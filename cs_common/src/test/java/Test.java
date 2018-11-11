import com.cls.common.utils.CommonUtil;
import com.cls.common.utils.EncryUtil;
import com.cls.common.utils.SortUtil;
import com.google.common.collect.Lists;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project: cs_backend
 *
 * @author Yue
 * @create 2018/4/2-21:31
 * Description：
 */
public class Test {
    public void main(String args[]){
        /*Map<Integer, Integer> a = new HashMap<>();
        a.put(1,10);
        a.put(10,2);
        a.put(22,6);
        List<Map.Entry> list = SortUtil.sortMapByVal(a);
        for(Map.Entry<Integer, Integer> entry : list){
            System.out.println("key:"+entry.getKey().toString() + "-"+ "value:" +entry.getValue().toString());
        }*/
        List<Integer> list = Lists.newArrayList(1,2,3,4,5,6,7,8,9);
        List<Integer> sub = list.subList(2,list.size());
        System.out.println(sub);
    }

    /*public void testImages() {
        //测试上传图片
        byte[] buff = CommonUtil.getFileBytes(new File("C:/Users/paperpass/Desktop/tu29253_9.jpg"));
        String key = QiniuStorage.uploadImage(buff);
        System.out.println("key = " + key);

        //测试下载图片
        String url = QiniuStorage.getUrl(key);
        System.out.println("url = " + url);

        //测试下载不同大小的图片
        url = QiniuStorage.getUrl(key, ThumbModel.THUMB_256);
        System.out.println("url = " + url);

    }*/
}
