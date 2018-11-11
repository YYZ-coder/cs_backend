package com.cls.common.services.evaluate.mapper;

import com.cls.common.services.evaluate.entity.EvaluationInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Project: cs_backend
 *
 * @author Yue
 * @create 2018/4/25-0:58
 * Description：
 */
public interface EvaluationMapper extends Mapper<EvaluationInfo>, MySqlMapper<EvaluationInfo> {

    int changeStatus(@Param("id") Integer id, @Param("status") Integer status,
                     @Param("updateTime") Integer updateTime, @Param("updateUser") Integer updateUser);
}
