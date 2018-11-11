package com.cls.common.services.shop.mapper;

import com.cls.common.base.BaseMappers;
import com.cls.common.services.shop.entity.ShopInfo;
import org.apache.ibatis.annotations.Param;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/22-13:43
 * Description：
 *      店铺Mapper
 */
public interface ShopInfoMapper extends BaseMappers<ShopInfo> {
    int changeStatus(@Param("id") Integer id, @Param("status") Integer status,
                     @Param("updateTime") Integer updateTime, @Param("updateUser") Integer updateUser);
}
