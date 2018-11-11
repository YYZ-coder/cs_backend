package com.cls.common.utils;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/2-20:56
 * Description：
 *      protoStuff的序列化操作
 */
public class ProStuffSerializeUtil {

    /**
     * 序列化
     * @param t
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> byte[] serialize(T t,Class<T> clazz) {
        return ProtobufIOUtil.toByteArray(t, RuntimeSchema.createFrom(clazz),
                LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
    }

    /**
     * 反序列化
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T deSerialize(byte[] data,Class<T> clazz) {
        RuntimeSchema<T> runtimeSchema = RuntimeSchema.createFrom(clazz);
        T t = runtimeSchema.newMessage();
        ProtobufIOUtil.mergeFrom(data, t, runtimeSchema);
        return t;
    }
}
