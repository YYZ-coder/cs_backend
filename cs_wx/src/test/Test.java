import com.cls.common.utils.DateUtil;

/**
 * Project: cs_backend
 *
 * @author Yue
 * @create 2018/4/2-0:28
 * Description：
 */
public class Test {


    public static void main(String args[]){
        System.out.println(DateUtil.getCurrentSecond());
        System.out.println(DateUtil.getCurrentMili());
        System.out.println(new Long(DateUtil.getCurrentMili()).intValue());
    }
}
