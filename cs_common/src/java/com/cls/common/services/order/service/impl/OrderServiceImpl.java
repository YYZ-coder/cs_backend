package com.cls.common.services.order.service.impl;

import com.cls.common.search.ExampleBuilder;
import com.cls.common.search.Search;
import com.cls.common.services.order.entity.OrderInfo;
import com.cls.common.services.order.mapper.OrderInfoMapper;
import com.cls.common.services.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/25-1:05
 * Description：
 */
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    /**
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    public int changeStatus(Integer id, Integer status, Integer uid) {
        return 0;
    }

    @Override
    public OrderInfo queryById(Integer id) {
        return orderInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<OrderInfo> queryByProperties(Search search, String sort) {
        return orderInfoMapper.selectByExample(new ExampleBuilder(OrderInfo.class).search(search).sort(sort).build());
    }

    /**
     * @param uid
     * @return
     */
    public List<OrderInfo> queryByUserId(Integer uid){
        Search search = new Search();
        search.put("userId",uid);
        search.put("status", 0);
        search.put("boughtStatus", 1);//已购买
        return orderInfoMapper.selectByExample(new ExampleBuilder(OrderInfo.class).search(search).build());
    }

    @Override
    public List<OrderInfo> queryAllList(Integer pid, int page, int size) {
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
    public int addNew(OrderInfo orderInfo) {
        return orderInfoMapper.insertSelective(orderInfo);
    }

    @Override
    public int addNews(List<OrderInfo> orderInfos) {
        return 0;
    }

    @Override
    public int updateInfo(OrderInfo orderInfo) {
        return 0;
    }

    @Override
    public int updateInfoByExample(OrderInfo orderInfo, Search search) {
        return orderInfoMapper.updateByExampleSelective(orderInfo, new ExampleBuilder(OrderInfo.class).search(search).build());
    }
}
