package com.cls.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Project: wx_cshop_end
 * @author Yue
 * @create 2018/1/23-12:35
 * Description：
 *      Redis操作类
 */
@Component
public class RedisUtil {

    @Autowired
    private static RedisTemplate redisTemplate;

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisUtil(){
    }

    /**
     * 设置当前数据库
     * @param index
     */
    public static void setDB(final int index){
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(index);
                return null;
            }
        });
    }

    /**
     * 获得当前数据库的大小
     * @return long
     */
    public static Long getDBSize(){
        return (Long)redisTemplate.execute(new RedisCallback() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
    }

    /**
     * 存入key-value值
     * @param key
     * @param value
     */
    public static void set(final byte[] key,final byte[] value){
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.set(key,value);
                return null;
            }
        });
    }

    /**
     * 设置key的Expire
     * @param key
     * @param expire
     */
    public static void setExpire(final byte[] key,final int expire){
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.expire(key,expire);
                return null;
            }
        });
    }

    /**
     * 获取key的value
     * @param key
     * @return Object
     */
    public static Object get(final byte[] key){
        return redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                try{
                    return redisConnection.get(key);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    /**
     * 对key执行get、set操作
     * @param key
     * @param value
     * @return Object
     */
    public static Object getset(final byte[] key,final byte[] value){
        return redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.getSet(key,value);
            }
        });
    }

    /**
     * 删除key
     * @param key
     */
    public static Object del(final byte[] key){
        return redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.del(key);
            }
        });
    }

    /**
     * 删除数据库
     */
    public static void flushDb(){
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return null;
            }
        });
    }

    /**
     * 列举所有key
     * @param pres
     * @return Set<Object>
     */
    public static Set<byte[]> keys(final byte[] pres){
        return (Set<byte[]>) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.keys(pres);
            }
        });
    }

    /**
     * 倘若当前key不存在则set：适用于加锁操作
     * Set if No eXist --- setNX
     * @param key
     * @param value
     * @return Boolean
     */
    public static Boolean setNx(final byte[] key,final byte[] value){
        return (Boolean)redisTemplate.execute(new RedisCallback() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.setNX(key,value);
            }
        });
    }

    /**
     * 获得redisTemplate
     * @return RedisTemplate
     */
    public static RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }
}
