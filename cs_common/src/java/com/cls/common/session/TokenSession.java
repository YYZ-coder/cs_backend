package com.cls.common.session;

import com.cls.common.exception.ClsException;
import com.cls.common.stat.LogStat;
import com.cls.common.utils.ProStuffSerializeUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.ibatis.jdbc.Null;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/1/23-12:08
 * Description：
 *      Session工具
 */
public class TokenSession {

    /** 日志 */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TokenSession.class);

    /** 存在Guava Cache中，且时间设置为：最后一次访问后5min即失效 */
    private static final LoadingCache<String,String> sessionCache =
            CacheBuilder.newBuilder()
            .expireAfterAccess(8, TimeUnit.MINUTES)
            .concurrencyLevel(10)
            .initialCapacity(10)
            .recordStats()
            .removalListener(notification ->
                    logger.info("TokenSession：{} was removed,cause is {}",
                            notification.getKey(),notification.getCause()))
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    return null;
                }
            });


    /**
     * 获取ifPresent
     * @param key
     * @return
     */
    public static String getIfPresent(String key){
        String val =  sessionCache.getIfPresent(key);
        if(val == null){
            throw new ClsException(LogStat.EXPIRED_TOKEN);
        }
        return ProStuffSerializeUtil.deSerialize(val.getBytes(),String.class);
    }

    /**
     * 存储Session
     * @param key
     * @param value
     */
    public static <T> void saveSession(String key,T value, Class<T> clazz){
        sessionCache.put(key, new String(ProStuffSerializeUtil.serialize(value,clazz)));
    }

    /**
     * 获得Session值
     * @param key
     * @return Object
     */
    public static <T> T getSession(String key, Class<T> clazz)
            throws ExecutionException{
        return  ProStuffSerializeUtil.deSerialize(sessionCache.get(key).getBytes(),clazz);
    }

    /**
     * 移除key
     * @param key
     */
    public static void removeSession(String key) throws NullPointerException{
        sessionCache.invalidate(key);
    }

}
