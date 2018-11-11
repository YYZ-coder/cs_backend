package com.cls.common.services.behavior.mapper;

import com.cls.common.services.behavior.entity.UserOperation;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/22-16:39
 * Description：
 */
public interface OperationMapper extends Mapper<UserOperation>, MySqlMapper<UserOperation>{
}
