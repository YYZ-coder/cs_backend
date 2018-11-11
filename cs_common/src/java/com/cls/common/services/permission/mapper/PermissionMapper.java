package com.cls.common.services.permission.mapper;

import com.cls.common.services.user.entity.UserInfo;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Project: cs_backend
 *
 * @author Yue
 * @create 2018/5/14-18:18
 * Description：
 */
public interface PermissionMapper extends Mapper<UserInfo>, MySqlMapper<UserInfo> {
}
