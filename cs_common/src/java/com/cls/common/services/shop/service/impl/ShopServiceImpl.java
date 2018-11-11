package com.cls.common.services.shop.service.impl;

import com.cls.common.context.WebSession;
import com.cls.common.exception.ClsException;
import com.cls.common.search.ExampleBuilder;
import com.cls.common.search.Search;
import com.cls.common.services._enum.BEHAVIOR;
import com.cls.common.services._enum.STATUS;
import com.cls.common.services._enum.TYPE;
import com.cls.common.services.behavior.entity.UserOperation;
import com.cls.common.services.behavior.mapper.OperationMapper;
import com.cls.common.services.commodity.mapper.CommodityInfoMapper;
import com.cls.common.services.shop.entity.ShopInfo;
import com.cls.common.stat.LogStat;
import com.cls.common.utils.DateUtil;
import com.cls.common.utils.SortUtil;

import com.cls.common.services.shop.mapper.ShopInfoMapper;
import com.cls.common.services.shop.service.IShopService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/22-14:07
 * Description：
 *      shopService
 */
@Service
public class ShopServiceImpl implements IShopService {

    @Autowired
    private ShopInfoMapper shopInfoMapper;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private CommodityInfoMapper commodityInfoMapper;

    /**
     *
     * @param search
     * @param rowBounds
     * @return
     */
    public List<ShopInfo> queryAllByRowBounds(Search search, RowBounds rowBounds){
        return shopInfoMapper.selectByExampleAndRowBounds(new ExampleBuilder(ShopInfo.class).search(search).build(), rowBounds);
    }

    /**
     * 按模糊查询
     * @param k
     * @param like
     * @return
     */
    public List<ShopInfo> queryByRestriction(String k, String like, RowBounds rowBounds){
        Example example = new Example(ShopInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike(k,like);
        criteria.andEqualTo("status",STATUS.YES.code);
        return shopInfoMapper.selectByExampleAndRowBounds(example, rowBounds);
    }

    /**
     * 查询浏览量最高
     * @param size
     * @return List<ShopInfo>
     */
    public List<ShopInfo> queryViewsShops(int page, int size){
        int offset = (page-1) * size;
        Map<String, Object> searchCondition = new HashMap<>();
        searchCondition.put("type", TYPE.shop.code);
        searchCondition.put("behavior", BEHAVIOR.VIEW.code);
        searchCondition.put("status", STATUS.YES.code);
        List<UserOperation> userOperationsFinal = operationMapper.selectByExample(new ExampleBuilder(UserOperation.class).search(searchCondition).build());
        if(CollectionUtils.isEmpty(userOperationsFinal)){
            List<ShopInfo> shopInfos = shopInfoMapper.selectByExampleAndRowBounds(new ExampleBuilder(ShopInfo.class).build(),
                    new RowBounds(offset, size));
            if(CollectionUtils.isEmpty(shopInfos)){
                throw new ClsException(LogStat.NO_DATA);
            }
            return shopInfos;
        }
        Map<Integer, Integer> shopIdCountMap = new HashMap<>();
        for(UserOperation userOperation : userOperationsFinal){
            Integer count = shopIdCountMap.get(userOperation.getObjId());
            if(count == null || count.equals(0)){
                shopIdCountMap.put(userOperation.getObjId(), 1);
            }else{
                shopIdCountMap.put(userOperation.getObjId(), count+1);
            }
        }
        List<Map.Entry> list = SortUtil.sortMapByVal(shopIdCountMap);
        List<Map.Entry> sortedList = Lists.newArrayList();
        if(list.size() >= offset+size){
            sortedList = list.subList(offset, size);
        }else if(list.size() < offset){
            return Lists.newArrayList();
        }else{
            sortedList = list.subList(offset, list.size());
        }
        List<Integer> sortedIds = Lists.newArrayList();
        for(Map.Entry<Integer,Integer> entry : sortedList){
            sortedIds.add(entry.getKey());
        }
        Search s = new Search();
        s.put("id_in",sortedIds);
        List<ShopInfo> shopInfoList = shopInfoMapper.selectByExample(new ExampleBuilder(ShopInfo.class).search(s).build());
        if(CollectionUtils.isNotEmpty(shopInfoList)){
            return shopInfoList;
        }else{
            throw new ClsException(LogStat.NO_OBJ);
        }

    }

    /**
     * 查询收藏量最高
     * @param size
     * @return List<ShopInfo>
     */
    public List<ShopInfo> queryLikeShops(int page, int size){
        int offset = (page-1) * size;
        List<UserOperation> userOperations = operationMapper.selectByExampleAndRowBounds(
                new ExampleBuilder(UserOperation.class).search(new Search().put("type",TYPE.shop.code).put("behavior",BEHAVIOR.LIKE.code)).sort("count_desc").build(),
                new RowBounds(offset, size));
        if(CollectionUtils.isEmpty(userOperations)){
            return Lists.newArrayList();
        }
        List<Integer> shopIds = userOperations.stream().map(UserOperation::getObjId).collect(Collectors.toList());
        List<ShopInfo> shopInfos = shopInfoMapper.selectByExample(
                new ExampleBuilder(ShopInfo.class).search(new Search().put("id_in",shopIds)).build());
        if(CollectionUtils.isEmpty(shopInfos) || shopInfos.size() == 0){
            return Lists.newArrayList();
        }
        return shopInfos;
    }

    /**
     * 查询销量最多Desc
     * @return
     */
    public List<ShopInfo> querySellsShops(int page, int size){
        int offset = (page-1) * size;
        List<ShopInfo> shopInfos = shopInfoMapper.selectByExampleAndRowBounds(
                new ExampleBuilder(ShopInfo.class).sort("sellsCount_desc").build(),
                new RowBounds(offset, size));
        if(CollectionUtils.isEmpty(shopInfos)){
            return Lists.newArrayList();
        }else{
            return shopInfos;
        }
    }

    /**
     * 按销量进行排序倒序：
     *    查出UserOperation表中所有已购买的商品ids，然后用它们获得所有commodityInfo对象，
     * 通过commodityInfo对象获得所有的shopId，然后可以得知每个店铺的销量数据。
     * @param size
     * @return List<ShopInfo>
     */
    public List<ShopInfo> queryHotShops(int size){
        List<ShopInfo> shopInfos = querySellsShops(1,size);
        if(CollectionUtils.isNotEmpty(shopInfos)){
            return shopInfos;
        }else{
            return queryViewsShops(1,size);
        }
    }


    /**
     * 改变状态
     * @param id
     * @param status
     * @return
     */
    @Override
    public int changeStatus(Integer id, Integer status, Integer uid) {
        return 0;
    }

    @Override
    public ShopInfo queryById(Integer id) {
        return shopInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<ShopInfo> queryByProperties(Search search, String sort) {
        return shopInfoMapper.selectByExample(new ExampleBuilder(ShopInfo.class).search(search).sort(sort).build());
    }


    @Override
    public List<ShopInfo> queryAllList(Integer pid, int page, int size) {
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
    public int addNew(ShopInfo shopInfo) {
        shopInfo.setCreateTime(DateUtil.getCurrentMili());
        return shopInfoMapper.insertSelective(shopInfo);
    }

    @Override
    public int addNews(List<ShopInfo> shopInfos) {
        return 0;
    }

    /**
     * 更新店铺操作
     * @param shopInfo
     * @return
     */
    @Override
    public int updateInfo(ShopInfo shopInfo) {
        Integer uid = (Integer) WebSession.getVal("cuid");
        if(uid == null || uid.equals(0)){
            throw new ClsException(LogStat.NO_LOGIN);
        }
        shopInfo.setUpdateTime(DateUtil.getCurrentMili());
        shopInfo.setUpdateUser(uid);
        Search search = new Search();
        search.put("uid", shopInfo.getUid());
        search.put("id", shopInfo.getId());
        search.put("status", STATUS.YES.code);
        return shopInfoMapper.updateByExampleSelective(shopInfo, new ExampleBuilder(ShopInfo.class).search(search).build());
    }

    @Override
    public int updateInfoByExample(ShopInfo shopInfo, Search search) {
        return shopInfoMapper.updateByExampleSelective(shopInfo, new ExampleBuilder(ShopInfo.class).search(search).build());
    }


}
