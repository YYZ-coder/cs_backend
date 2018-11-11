package com.cls.common.services.order.mapper;

import com.cls.common.base.BaseMappers;
import com.cls.common.services.order.entity.OrderInfo;
import org.apache.ibatis.annotations.Param;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/25-0:49
 * Description：
 */
public interface OrderInfoMapper extends BaseMappers<OrderInfo> {

    int changeStatus(@Param("id") Integer id, @Param("status") Integer status,
                     @Param("updateTime") Integer updateTime, @Param("updateUser") Integer updateUser);
}
