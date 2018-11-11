package com.cls.common.utils;

import java.io.*;

/**
 * Project: wx_cshop_end
 * @author Yue
 * @create 2018/1/23-12:43
 * Description：
 *      序列化与反序列化
 */
public class SerializeUtil {

    /**
     * 序列化Object至byte[]
     * @param value
     * @return byte[]
     */
    public static byte[] serialize(Object value) {
        if (value == null) {
            throw new NullPointerException("SerializeUtils: Can't serialize null");
        }
        byte[] rv = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream os = null;
        try {
            bos = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bos);
            os.writeObject(value);
            os.close();
            bos.close();
            rv = bos.toByteArray();
        } catch (Exception e) {
        } finally {
            close(os);
            close(bos);
        }
        return rv;
    }

    /**
     * 反序列化byte[]至Object
     * @param in
     * @return Object
     */
    public static Object deserialize(byte[] in) {
        return deserialize(in, Object.class);
    }

    /**
     * 反序列化流数据
     * @param in
     * @param requiredType
     * @param <T>
     * @return T
     */
    public static <T> T deserialize(byte[] in, Class<T>...requiredType) {
        Object rv = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream is = null;
        try {
            if (in != null) {
                bis = new ByteArrayInputStream(in);
                is = new ObjectInputStream(bis);
                rv = is.readObject();
            }else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(is);
            close(bis);
        }
        return (T) rv;
    }

    /**
     * 关闭流
     * @param closeable
     */
    private static void close(Closeable closeable) {
        if (closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }
}
