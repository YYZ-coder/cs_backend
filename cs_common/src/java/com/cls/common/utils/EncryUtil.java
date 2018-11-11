package com.cls.common.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

/**
 * Project: wx_cshop_end
 * @author Yue
 * @create 2018/3/25-21:53
 * Description：
 *      加密
 */
public class EncryUtil {

    /**
     * 解密wx加密数据
     * @param data
     * @param key
     * @param iv
     * @param encodingFormat
     * @return String
     * @throws Exception
     */
    public static String decryptWxUserInfo(
            String data, String key, String iv, String encodingFormat)
            throws Exception {
        //被加密的数据
        byte[] dataByte = Base64.decodeBase64(data);
        //加密秘钥
        byte[] keyByte = Base64.decodeBase64(key);
        //偏移量
        byte[] ivByte = Base64.decodeBase64(iv);
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
        AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
        parameters.init(new IvParameterSpec(ivByte));
        // 初始化
        cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
        byte[] resultByte = cipher.doFinal(dataByte);
        if (null != resultByte &&
            resultByte.length > 0) {
            String result = new String(resultByte, encodingFormat);
            return result;
        }
        return null;
    }

    /**
     * MD-5加密
     * @param ms
     * @return String
     */
    public static String encryptByMD5(String ms){
        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(ms.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算，加盐
                int number = b & 0xff;
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }
            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * RSA加密
     * @param str
     * @return String
     */
    public static String encryptByRSA(String str){
        return null;
    }

}
