package com.cls.common.base;

import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/29-10:11
 * Description：
 */
public interface BaseMappers<T> extends Mapper<T>, MySqlMapper<T> {

}
