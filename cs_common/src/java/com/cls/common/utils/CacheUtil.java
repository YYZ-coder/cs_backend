package com.cls.common.utils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/3/30-0:46
 * Description：
 *  Guava本地缓存
 */
public class CacheUtil {

    /**
     * Session缓存
     */
    private static LoadingCache<Integer,Object> loadingCache =
            CacheBuilder.newBuilder()
                    .expireAfterAccess(8, TimeUnit.MINUTES)
                    .recordStats()
                    .build(new CacheLoader<Integer, Object>() {

                        @Override
                        public Object load(Integer key) throws Exception {
                            return null;
                        }

                        @Override
                        public Map<Integer, Object> loadAll(Iterable<? extends Integer> keys) throws Exception {
                            return super.loadAll(keys);
                        }
                    });

}
