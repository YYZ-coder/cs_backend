package com.cls.common.services.commodity.service.impl;

import com.cls.common.context.WebSession;
import com.cls.common.exception.ClsException;
import com.cls.common.search.ExampleBuilder;
import com.cls.common.search.Search;
import com.cls.common.services.behavior.entity.UserOperation;
import com.cls.common.services.behavior.mapper.OperationMapper;
import com.cls.common.services.commodity.entity.CommodityInfo;
import com.cls.common.stat.LogStat;
import com.cls.common.utils.SortUtil;
import com.cls.common.services._enum.BEHAVIOR;
import com.cls.common.services._enum.STATUS;
import com.cls.common.services.commodity.service.ICommodityService;
import com.cls.common.services.commodity.mapper.CommodityInfoMapper;
import com.cls.common.services._enum.TYPE;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/22-15:57
 * Description：
 *
 */
@Service
public class CommodityServiceImpl implements ICommodityService {

    @Autowired
    private CommodityInfoMapper commodityInfoMapper;

    @Autowired
    private OperationMapper operationMapper;

    /**
     * 根据id查询
     * @param ids
     * @return List<CommodityInfo>
     */
    @Transactional
    public List<CommodityInfo> queryByObjIds(List<Integer> ids){
        Search search = new Search();
        search.put("id_in", ids);
        List<CommodityInfo> commodityInfos = commodityInfoMapper.selectByExample(new ExampleBuilder(CommodityInfo.class).search(search).build());
        if(CollectionUtils.isEmpty(commodityInfos)){
            throw new ClsException(LogStat.NO_DATA);
        }
        return commodityInfos;
    }

    @Override
    public int changeStatus(Integer id, Integer status, Integer uid) {
        return 0;
    }

    /**
     * 分页查询
     * @param search
     * @param rowBounds
     * @return
     */
    public List<CommodityInfo> queryAllByRowBounds(Search search, RowBounds rowBounds){
        return commodityInfoMapper.selectByExampleAndRowBounds(new ExampleBuilder(CommodityInfo.class).search(search).build(), rowBounds);
    }

    /**
     * query by id
     * @param id
     * @return CommodityInfo
     */
    @Transactional
    @Override
    public CommodityInfo queryById(Integer id) {
        return commodityInfoMapper.selectByPrimaryKey(id);
    }

    /**
     * 按时间倒序
     * @param size
     * @return List<CommodityInfo>
     */
    @Transactional
    public List<CommodityInfo> queryByNews(int page, int size){
        int offset = (page-1) * size;
        Search search = new Search();
        search.put("status",STATUS.YES.code);
        Example example = new ExampleBuilder(CommodityInfo.class).search(search).sort("createTime_desc").build();
        List<CommodityInfo> commodityInfos = commodityInfoMapper.selectByExampleAndRowBounds(example, new RowBounds(offset, size));
        if(CollectionUtils.isNotEmpty(commodityInfos)){
            if(commodityInfos.size() >= offset + size){
                return commodityInfos.subList(offset, size);
            }else if(commodityInfos.size() < offset){
                return Lists.newArrayList();
            }else{
                return commodityInfos.subList(offset, commodityInfos.size());
            }
        }else{
            return Lists.newArrayList();
        }
    }

    /**
     * 按viewCount排序
     * @param page 从1开始
     * @param size
     * @return
     */
    @Transactional
    public List<CommodityInfo> queryByViews(int page, int size){
        int offset = (page-1) * size;
        Search search = new Search();
        search.put("behavior", BEHAVIOR.VIEW.code);
        search.put("status",STATUS.YES.code);
        search.put("type",TYPE.commodity.code);
        Example example = new ExampleBuilder(UserOperation.class).search(search).sort("count_desc").build();
        List<UserOperation> commodityInfos = operationMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(commodityInfos)){
            //任意搜索数据
            List<CommodityInfo> commodityInfos1 = commodityInfoMapper.selectByExampleAndRowBounds(new ExampleBuilder(CommodityInfo.class).build(),
                    new RowBounds(offset, size));
            if(CollectionUtils.isEmpty(commodityInfos1)){
                throw new ClsException(LogStat.NO_DATA);
            }
            return commodityInfos1;
        }
        Map<Integer, Integer> commIdCountMap = new HashMap<>();
        for(UserOperation userOperation : commodityInfos){
            Integer count = commIdCountMap.get(userOperation.getObjId());
            if(count == null || count.equals(0)){
                commIdCountMap.put(userOperation.getObjId(), 1);
            }else{
                commIdCountMap.put(userOperation.getObjId(), count+1);
            }
        }
        List<Map.Entry> list = SortUtil.sortMapByVal(commIdCountMap);
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
        List<CommodityInfo> commodityInfoList = commodityInfoMapper.selectByExample(new ExampleBuilder(CommodityInfo.class).search(s).build());
        if(CollectionUtils.isNotEmpty(commodityInfoList)){
            return commodityInfoList;
        }else{
            throw new ClsException(LogStat.NO_OBJ);
        }
    }

    /**
     * 按销量排序
     * @param page
     * @param size
     * @return
     */
    @Transactional
    public List<CommodityInfo> queryByBought(int page, int size){
        int offset = (page-1) * size;
        List<UserOperation> userOperations = operationMapper.selectByExampleAndRowBounds(
                new ExampleBuilder(UserOperation.class).search(new Search().put("behavior", BEHAVIOR.BOUGHT.code).put("type",TYPE.commodity.code).put("status", STATUS.YES.code)).sort("count_desc").build(),
                new RowBounds(offset, size));
        if(CollectionUtils.isEmpty(userOperations) || userOperations.size() == 0){
            return Lists.newArrayList();
        }
        List<Integer> ids = userOperations.stream().map(UserOperation::getObjId).collect(Collectors.toList());
        List<CommodityInfo> commodityInfos = commodityInfoMapper.selectByExample(new ExampleBuilder(CommodityInfo.class).search(new Search().put("id_in",ids)).build());
        if(CollectionUtils.isEmpty(commodityInfos) || commodityInfos.size() == 0){
            return Lists.newArrayList();
        }
        return commodityInfos;
    }

    //------------------------------------------------------------------------------------------

    @Override
    public List<CommodityInfo> queryByProperties(Search search, String sort) {
        return commodityInfoMapper.selectByExample(new ExampleBuilder(CommodityInfo.class).search(search).sort(sort).build());
    }

    /**
     *
     * @param shopId
     * @param page
     * @param size
     * @return
     */
    @Override
    public List<CommodityInfo> queryAllList(Integer shopId, int page, int size) {
        Example example = new ExampleBuilder(CommodityInfo.class).search(
                new Search().put("shopId",shopId).put("status",STATUS.YES.code)).build();
        int offset = (page-1) * size;
        return commodityInfoMapper.selectByExampleAndRowBounds(example, new RowBounds(offset, size));
    }

    /**
     * 更新删除状态
     * @param id
     * @return
     */
    @Override
    public int deleteById(Integer id) {
        return commodityInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteByIds(List<Integer> ids) {
        return 0;
    }

    @Override
    public int deleteByProps(Map<String, Object> props) {
        return 0;
    }

    /**
     *
     * @param commodityInfo
     * @return
     */
    @Override
    public int addNew(CommodityInfo commodityInfo) {
        return commodityInfoMapper.insertSelective(commodityInfo);
    }

    @Override
    public int addNews(List<CommodityInfo> commodityInfos) {
        return 0;
    }

    @Override
    public int updateInfo(CommodityInfo commodityInfo) {
        return 0;
    }

    /**
     * 依靠example更新
     * @param commodityInfo
     * @param search
     * @return
     */
    public int updateInfoByExample(CommodityInfo commodityInfo, Search search){
        return commodityInfoMapper.updateByExampleSelective(commodityInfo, new ExampleBuilder(CommodityInfo.class).search(search).build());
    }
}
