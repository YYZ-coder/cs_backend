package com.cls.common.services.role.service.impl;

import com.cls.common.search.ExampleBuilder;
import com.cls.common.search.Search;
import com.cls.common.services.role.entity.RoleInfo;
import com.cls.common.services.role.mapper.RoleInfoMapper;
import com.cls.common.services.role.service.IRoleService;
import com.cls.common.services.user.entity.UserInfo;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**
 * Project: cs_backend
 *
 * @author Yue
 * @create 2018/5/14-18:26
 * Description：
 */
@Service
public class RoleServiceImpl implements IRoleService{

    @Autowired
    private RoleInfoMapper roleInfoMapper;

    @Override
    public int changeStatus(Integer id, Integer status, Integer uid) {
        return 0;
    }

    @Override
    public RoleInfo queryById(Integer id) {
        return roleInfoMapper.selectByPrimaryKey(id);
    }

    /**
     * 按模糊查询
     * @param k
     * @param like
     * @return
     */
    public List<RoleInfo> queryByRestriction(String k, String like){
        Example example = new Example(RoleInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike(k,like);
        return roleInfoMapper.selectByExample(example);
    }

    @Override
    public List<RoleInfo> queryByProperties(Search search, String sort) {
        return roleInfoMapper.selectByExample(new ExampleBuilder(RoleInfo.class).search(search).sort(sort).build());
    }

    @Override
    public List<RoleInfo> queryAllList(Integer pid, int page, int size) {
        return null;
    }

    @Override
    public int deleteById(Integer id) {
        return roleInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteByIds(List<Integer> ids) {
        return roleInfoMapper.deleteByExample(new ExampleBuilder(RoleInfo.class).search(new Search().put("id_in",ids)).build());
    }

    @Override
    public int deleteByProps(Map<String, Object> props) {
        return 0;
    }

    @Override
    public int addNew(RoleInfo roleInfo) {
        return roleInfoMapper.insertSelective(roleInfo);
    }

    @Override
    public int addNews(List<RoleInfo> roleInfos) {
        return 0;
    }

    @Override
    public int updateInfo(RoleInfo roleInfo) {
        return 0;
    }

    @Override
    public int updateInfoByExample(RoleInfo roleInfo, Search search) {
        return roleInfoMapper.updateByExampleSelective(roleInfo, new ExampleBuilder(RoleInfo.class).search(search).build());

    }
}
