package com.cls.common.services.commodity.mapper;

import com.cls.common.services.commodity.entity.CommodityInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Project: cs_backend
 *
 * @author Yue
 * @create 2018/4/22-15:58
 * Description：
 */
public interface CommodityInfoMapper extends Mapper<CommodityInfo>, MySqlMapper<CommodityInfo> {

    int changeStatus(@Param("id") Integer id, @Param("status") Integer status,
                     @Param("updateTime") Integer updateTime, @Param("updateUser") Integer updateUser);
}
