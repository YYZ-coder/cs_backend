package com.cls.common.services.permission.service.impl;

import com.cls.common.search.Search;
import com.cls.common.services.permission.entity.PermissionInfo;
import com.cls.common.services.permission.mapper.PermissionMapper;
import com.cls.common.services.permission.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/5/14-18:17
 * Description：
 */
@Service
public class PermissionServiceImpl implements IPermissionService{

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public int changeStatus(Integer id, Integer status, Integer uid) {
        return 0;
    }

    @Override
    public PermissionInfo queryById(Integer id) {
        return null;
    }

    @Override
    public List<PermissionInfo> queryByProperties(Search search, String sort) {
        return null;
    }

    @Override
    public List<PermissionInfo> queryAllList(Integer pid, int page, int size) {
        return null;
    }

    @Override
    public int deleteById(Integer id) {
        return 0;
    }

    @Override
    public int deleteByIds(List<Integer> ids) {
        return 0;
    }

    @Override
    public int deleteByProps(Map<String, Object> props) {
        return 0;
    }

    @Override
    public int addNew(PermissionInfo permissionInfo) {
        return 0;
    }

    @Override
    public int addNews(List<PermissionInfo> permissionInfos) {
        return 0;
    }

    @Override
    public int updateInfo(PermissionInfo permissionInfo) {
        return 0;
    }

    @Override
    public int updateInfoByExample(PermissionInfo permissionInfo, Search search) {
        return 0;
    }
}
