package com.cls.common.base;

import com.cls.common.search.Search;

import java.util.List;
import java.util.Map;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/22-14:18
 * Description：
 *      BaseService
 */
public interface BaseService<T> {

    /**
     * 变更状态
     * @param id
     * @param status
     * @return
     */
    int changeStatus(Integer id, Integer status, Integer uid);

    /**
     * 依照id查询实体对象
     * @param id
     * @return T
     */
    T queryById(Integer id);

    /**
     * 依照属性查询
     * @param props
     * @return List<T>
     */
    List<T> queryByProperties(Search search, String sort);

    /**
     * 依照父id，页数和大小查询
     * @param pid
     * @param page
     * @param size
     * @return
     */
    List<T> queryAllList(Integer pid, int page, int size);

    /**
     * 删除
     * @param id
     * @return
     */
    int deleteById(Integer id);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteByIds(List<Integer> ids);

    /**
     * 按属性值批量删除
     * @param props
     * @return
     */
    int deleteByProps(Map<String, Object> props);

    /**
     * 新增
     * @param t
     * @return
     */
    int addNew(T t);

    /**
     * 批量新增
     * @param ts
     * @return
     */
    int addNews(List<T> ts);

    /**
     * 更新
     * @param t
     * @return
     */
    int updateInfo(T t);

    /**
     *
     * @param t
     * @param search
     * @return
     */
    int updateInfoByExample(T t, Search search);

}
