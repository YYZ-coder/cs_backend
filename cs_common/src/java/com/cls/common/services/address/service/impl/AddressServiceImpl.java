package com.cls.common.services.address.service.impl;

import com.cls.common.exception.ClsException;
import com.cls.common.search.ExampleBuilder;
import com.cls.common.search.Search;
import com.cls.common.services.address.entity.AddressInfo;
import com.cls.common.services.address.mapper.AddressMapper;
import com.cls.common.services.address.service.IAddressService;
import com.cls.common.stat.LogStat;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Project: cs_backend
 *
 * @author Yue
 * @create 2018/4/25-1:07
 * Description：
 */
@Service
public class AddressServiceImpl implements IAddressService {

    @Autowired
    private AddressMapper addressMapper;


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
    public AddressInfo queryById(Integer id) {
        return addressMapper.selectByPrimaryKey(id);
    }

    /**
     *
     * @param search
     * @param sort
     * @return
     */
    @Override
    public List<AddressInfo> queryByProperties(Search search, String sort) {
        List<AddressInfo> addressInfos = addressMapper.selectByExample(new ExampleBuilder(AddressInfo.class).search(search).sort(sort).build());
        if(CollectionUtils.isEmpty(addressInfos)){
            throw new ClsException(LogStat.NO_DATA);
        }
        return addressInfos;
    }

    /**
     * 设置默认地址
     * @param id
     * @return
     */
    public int setDefault(Integer id){
        AddressInfo addressInfo = new AddressInfo();
        addressInfo.setIsDefault(1);
        Search search = new Search();
        search.put("id",id);
        return addressMapper.updateByExampleSelective(addressInfo, new ExampleBuilder(AddressInfo.class).search(search).build());
    }

    /**
     * 设置NoDefault
     * @param ids
     * @return
     */
    public int setNoDefault(List<Integer> ids){
        AddressInfo addressInfo = new AddressInfo();
        addressInfo.setIsDefault(0);
        Search search = new Search();
        search.put("id_in", ids);
        return addressMapper.updateByExampleSelective(addressInfo, new ExampleBuilder(AddressInfo.class).search(search).build());
    }

    @Override
    public List<AddressInfo> queryAllList(Integer pid, int page, int size) {
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

    /**
     *
     * @param addressInfo
     * @return
     */
    @Override
    public int addNew(AddressInfo addressInfo) {
        return addressMapper.insertSelective(addressInfo);
    }

    @Override
    public int addNews(List<AddressInfo> addressInfos) {
        return 0;
    }

    @Override
    public int updateInfo(AddressInfo addressInfo) {
        return 0;
    }

    /**
     * 更新信息
     * @param addressInfo
     * @return
     */
    public int updateInfoByExample(AddressInfo addressInfo, Search search) {
        return addressMapper.updateByExampleSelective(addressInfo, new ExampleBuilder(AddressInfo.class).search(search).build());
    }
}
