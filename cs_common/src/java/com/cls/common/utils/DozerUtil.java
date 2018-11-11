package com.cls.common.utils;

import com.google.common.collect.Lists;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/23-0:37
 * Description：
 *  DozerUtils
 */
@Component
public class DozerUtil implements ApplicationContextAware{

    @Autowired
    private static Mapper mapper;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        mapper = applicationContext.getBean(Mapper.class);
    }

    /**
     * mapTo
     * @param source
     * @param destinationClazz
     * @param mapId
     * @param <T>
     * @param <V>
     * @return
     */
    public static <T,V> T mapTo(V source ,Class<T> destinationClazz, String mapId){
        if(StringUtils.isEmpty(mapId)){
            return null;
        }
        return mapper.map(source ,destinationClazz, mapId);
    }

    /**
     * map To Destination
     * @param source
     * @param destinationClazz
     * @param <T>
     * @param <V>
     * @return T
     */
    public static <T,V> T mapTo(V source ,Class<T> destinationClazz){
        return mapper.map(source, destinationClazz);
    }

    /**
     * map To List
     * @param list
     * @param destinationClazz
     * @param <T>
     * @param <V>
     * @return List<T>
     */
    public static <T,V> List<T> mapToList(List<V> list, Class<T> destinationClazz){
        if(list == null || list.size() == 0){
            return Lists.newArrayList();
        }
        return list.stream().map(v -> mapper.map(v,destinationClazz)).collect(Collectors.toList());
    }

}
