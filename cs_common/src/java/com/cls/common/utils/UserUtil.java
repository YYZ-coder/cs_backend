package com.cls.common.utils;

/**
 * Project: wx_cshop_end
 * @author Yue
 * @create 2018/1/22-12:47
 * Description：
 *      用户登录工具类
 */
public class UserUtil {

    /**
     * 获取code的请求地址
     */
    private static String Get_Code =
            "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                    "appid=%s&" +
                    "redirect_uri=%s&" +
                    "response_type=code&" +
                    "scope=%s&" +
                    "state=STAT#wechat_redirect";

    /**
     * 拉取用户信息的请求地址
     */
    private static String User_Message =
            "https://api.weixin.qq.com/sns/userinfo?" +
                    "access_token=%s&" +
                    "openid=%s&" +
                    "lang=zh_CN";

    /**
     * 获取Web_access_tokenhttps的请求地址
     */
    private static String Web_access_tokenhttps =
            "https://api.weixin.qq.com/sns/jscode2session?" +
                    "appid=%s&" +
                    "secret=%s&" +
                    "js_code=%s&" +
                    "grant_type=authorization_code";

    /**
     * 密钥
     */
    private static final String secret = "5f2f9de5173d1a0f5e6dd0eb6f74187f";

    /**
     * appId
     */
    private static final String appId = "wx43f021d628a67efb";

    /**
     * 替换字符串
     * @param REDIRECT_URI
     * @param SCOPE
     * @return String
     */
    public static String getCode(String REDIRECT_URI,String SCOPE) {
        return String.format(Get_Code,REDIRECT_URI,SCOPE);
    }

    /**
     * 替换字符串
     * @param CODE
     * @return String
     */
    public static String getWebAccess(String CODE) {
        return String.format(
                Web_access_tokenhttps,
                appId,
                secret,
                CODE);
    }

    /**
     * 替换字符串
     * @param access_token
     * @param openid
     * @return String
     */
    public static String getUserMessage(String access_token, String openid) {
        return String.format(User_Message,access_token,openid);
    }
}
