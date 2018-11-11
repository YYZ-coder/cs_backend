package com.cls.common.services.resource.mapper;

import com.cls.common.base.BaseMappers;
import com.cls.common.services.resource.entity.PicInfo;
import org.apache.ibatis.annotations.Param;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/22-17:45
 * Description：
 */
public interface ResourceMapper extends BaseMappers<PicInfo>{

    int changeStatus(@Param("id") Integer id, @Param("status") Integer status,
                     @Param("updateTime") Integer updateTime, @Param("updateUser") Integer updateUser);
}
