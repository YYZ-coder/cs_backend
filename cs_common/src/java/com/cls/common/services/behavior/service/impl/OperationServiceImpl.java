package com.cls.common.services.behavior.service.impl;

import com.cls.common.context.WebSession;
import com.cls.common.search.ExampleBuilder;
import com.cls.common.search.Search;
import com.cls.common.services.behavior.entity.UserOperation;
import com.cls.common.services.behavior.mapper.OperationMapper;
import com.cls.common.services.behavior.service.IOperationService;
import com.cls.common.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/22-16:53
 * Description：
 *
 */
@Service
public class OperationServiceImpl implements IOperationService {

    @Autowired
    private OperationMapper operationMapper;

    /**
     *
     * @param search
     * @param sort
     * @return
     */
    public List<UserOperation> queryByDetail(Search search , String sort){
        return  operationMapper.selectByExample(new ExampleBuilder(UserOperation.class).search(search).sort(sort).build());
    }


    /**
     * 精确删除
     * @param type
     * @param behavior
     * @param userId
     * @param objId
     * @return
     */
    public int updateByDetail(UserOperation userOperation, Integer type, Integer behavior, Integer userId, Integer objId){
        Search search = new Search();
        search.put("type",type);
        search.put("behavior",behavior);
        search.put("userId",userId);
        search.put("objId",objId);
        userOperation.setUpdateUser(userId);
        userOperation.setUpdateTime(DateUtil.getCurrentMili());
        return operationMapper.updateByExampleSelective(userOperation, new ExampleBuilder(UserOperation.class).search(search).build());
    }

    //----------------------------------------------------------------------------------------------------

    @Override
    public int changeStatus(Integer id, Integer status, Integer uid) {
        return 0;
    }

    @Override
    public UserOperation queryById(Integer id) {
        return operationMapper.selectByPrimaryKey(id);
    }

    /**
     *
     * @param search
     * @param sort
     * @return
     */
    @Override
    public List<UserOperation> queryByProperties(Search search, String sort) {
        return operationMapper.selectByExample(new ExampleBuilder(UserOperation.class).search(search).sort(sort).build());
    }

    @Override
    public List<UserOperation> queryAllList(Integer pid, int page, int size) {
        return null;
    }

    @Override
    public int deleteById(Integer id) {
        return operationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteByIds(List<Integer> ids) {
        Example example = new Example(UserOperation.class);
        example.createCriteria().andIn("id",ids);
        return operationMapper.deleteByExample(example);
    }

    @Override
    public int deleteByProps(Map<String, Object> props) {
        return 0;
    }

    /**
     *
     * @param userOperation
     * @return
     */
    @Override
    public int addNew(UserOperation userOperation) {
        Integer cuid = (Integer) WebSession.getVal("cuid");
        userOperation.setCount(1);
        userOperation.setUserId(cuid);
        userOperation.setCreateTime(DateUtil.getCurrentMili());
        userOperation.setCreateUser(cuid);
        userOperation.setUpdateTime(DateUtil.getCurrentMili());
        userOperation.setUpdateUser(cuid);
        return operationMapper.insertSelective(userOperation);
    }

    @Override
    public int addNews(List<UserOperation> userOperations) {
        return operationMapper.insertList(userOperations);
    }

    /**
     *
     * @param userOperation
     * @return
     */
    @Override
    public int updateInfo(UserOperation userOperation) {
        Search search = new Search();
        search.put("objId",userOperation.getObjId());
        search.put("type",userOperation.getType());
        search.put("behavior",userOperation.getBehavior());
        userOperation.setUpdateTime(DateUtil.getCurrentMili());
        userOperation.setUpdateUser(userOperation.getUserId());
        return operationMapper.updateByExampleSelective(userOperation, new ExampleBuilder(UserOperation.class).search(search).build());
    }

    @Override
    public int updateInfoByExample(UserOperation userOperation, Search search) {
        return operationMapper.updateByExampleSelective(userOperation, new ExampleBuilder(UserOperation.class).search(search).build());
    }
}
