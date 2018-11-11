package com.cls.common.services.user.mapper;

import com.cls.common.services.user.entity.UserInfo;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/3/28-22:11
 * Description：
 *  用户Mapper
 */
public interface UserInfoMapper extends Mapper<UserInfo>, MySqlMapper<UserInfo>{
}
