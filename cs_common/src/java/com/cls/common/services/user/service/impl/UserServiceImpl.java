package com.cls.common.services.user.service.impl;

import com.cls.common.search.ExampleBuilder;
import com.cls.common.search.Search;
import com.cls.common.services.behavior.entity.UserOperation;
import com.cls.common.services.user.entity.UserInfo;
import com.cls.common.services.user.mapper.UserInfoMapper;
import com.cls.common.services.user.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.xml.registry.infomodel.User;
import java.util.List;
import java.util.Map;

/**
 * Project: wx_cshop_end
 * @author Yue
 * @create 2018/1/22-23:41
 * Description：
 *  用户ServiceImpl
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    protected UserInfoMapper userMapper;

    /**
     * 更新
     * @param userInfo
     * @param search
     * @return
     */
    @Override
    public int updateInfoByExample(UserInfo userInfo, Search search) {
        return userMapper.updateByExampleSelective(userInfo, new ExampleBuilder(UserInfo.class).search(search).build());
    }

    /**
     * 按模糊查询
     * @param k
     * @param like
     * @return
     */
    public List<UserInfo> queryByRestriction(String k, String like){
        Example example = new Example(UserInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike(k,like);
        return userMapper.selectByExample(example);
    }

    /**
     * 查找用户信息
     * @param search
     * @return
     */
    public List<UserInfo> queryUserInfo(Search search){
        List<UserInfo> userInfos = userMapper.selectByExample(new ExampleBuilder(UserInfo.class).search(search).build());
        return userInfos;
    }

    /**
     * 存储用户信息
     * @param userInfo
     * @return 用户id
     */
    public int addUser(UserInfo userInfo){
        return insert(userInfo);
    }

    /**
     * 更新用户登录次数
     * @param userInfo
     * @return
     */
    public int updateWebLogRecord(UserInfo userInfo){
        Search search = new Search();
        search.put("id",userInfo.getId());
        search.put("roleId",userInfo.getRoleId());
        search.put("accountName", userInfo.getAccountName());
        return userMapper.updateByExampleSelective(userInfo, new ExampleBuilder(UserInfo.class).search(search).build());
    }

    /**
     * 更新用户操作记录
     * @param userInfo
     * @return
     */
    public int updateWxLogRecord(UserInfo userInfo){
        Search search = new Search();
        search.put("openId",userInfo.getOpenId());
        search.put("id",userInfo.getId());
        return userMapper.updateByExampleSelective(userInfo,new ExampleBuilder(UserInfo.class).search(search).build());
    }


    /**
     * 查询open_id=openId的用户
     * @param openId
     * @return UserInfo
     */
    public List<UserInfo> findByOpenId(String openId){
        Example example = new Example(UserInfo.class);
        example.createCriteria().andEqualTo("openId",openId);
        return userMapper.selectByExample(example);
    }


    //------------------------------------------------------------------------------

    public Integer insert(UserInfo userInfo) {
        return userMapper.insertSelective(userInfo);
    }

    @Override
    public int changeStatus(Integer id, Integer status, Integer uid) {
        return 0;
    }

    @Override
    public UserInfo queryById(Integer id) {
        return null;
    }

    @Override
    public List<UserInfo> queryByProperties(Search search, String sort) {
        return userMapper.selectByExample(new ExampleBuilder(UserInfo.class).search(search).sort(sort).build());
    }

    @Override
    public List<UserInfo> queryAllList(Integer pid, int page, int size) {
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
    public int addNew(UserInfo userInfo) {
        return 0;
    }

    @Override
    public int addNews(List<UserInfo> userInfos) {
        return 0;
    }

    @Override
    public int updateInfo(UserInfo userInfo) {
        return 0;
    }

    public int updateUser(UserInfo userInfo) {
        return userMapper.updateByPrimaryKeySelective(userInfo);
    }
}
