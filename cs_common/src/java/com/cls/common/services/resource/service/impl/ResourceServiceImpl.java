package com.cls.common.services.resource.service.impl;

import com.cls.common.search.ExampleBuilder;
import com.cls.common.search.Search;
import com.cls.common.services.resource.entity.PicInfo;
import com.cls.common.services.resource.mapper.ResourceMapper;
import com.cls.common.services.resource.service.IResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/22-17:44
 * Description：
 */
@Service
public class ResourceServiceImpl implements IResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    /**
     *
     * @param type
     * @param objId
     * @param attribute
     * @return
     */
    public List<PicInfo> queryByDetail(Integer type, Integer objId, Integer attribute){
        Search search = new Search();
        search.put("type",type);
        search.put("objId",objId);
        search.put("attribute",attribute);
        return resourceMapper.selectByExample(new ExampleBuilder(PicInfo.class).search(search).build());
    }

    /**
     * 查询
     * @param type
     * @param objId
     * @return
     */
    public List<PicInfo> queryByDetail(Integer type, Integer objId){
        Search search = new Search();
        search.put("type", type);
        search.put("objId", objId);
        return resourceMapper.selectByExample(new ExampleBuilder(PicInfo.class).search(search).build());
    }

    /**
     *
     * @param id
     * @param status
     * @param uid
     * @return
     */
    @Override
    public int changeStatus(Integer id, Integer status, Integer uid) {
        return 0;
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public PicInfo queryById(Integer id) {
        return resourceMapper.selectByPrimaryKey(id);
    }

    /**
     *
     * @param search
     * @param sort
     * @return
     */
    @Override
    public List<PicInfo> queryByProperties(Search search, String sort) {
        return resourceMapper.selectByExample(new ExampleBuilder(PicInfo.class).search(search).sort(sort).build());
    }


    @Override
    public List<PicInfo> queryAllList(Integer pid, int page, int size) {
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
    public int addNew(PicInfo picInfo) {
        return resourceMapper.insertSelective(picInfo);
    }

    @Override
    public int addNews(List<PicInfo> picInfos) {
        return 0;
    }

    @Override
    public int updateInfo(PicInfo picInfo) {
        return 0;
    }

    @Override
    public int updateInfoByExample(PicInfo picInfo, Search search) {
        return resourceMapper.updateByExampleSelective(picInfo, new ExampleBuilder(PicInfo.class).search(search).build());
    }
}
