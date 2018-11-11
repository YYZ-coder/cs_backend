package com.cls.common.services.evaluate.service.impl;

import com.cls.common.context.WebSession;
import com.cls.common.search.ExampleBuilder;
import com.cls.common.search.Search;
import com.cls.common.services._enum.STATUS;
import com.cls.common.services.evaluate.entity.EvaluationInfo;
import com.cls.common.services.evaluate.mapper.EvaluationMapper;
import com.cls.common.services.evaluate.service.IEvaluationService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/22-16:01
 * Description：
 */
@Service
public class EvaluationServiceImpl implements IEvaluationService {

    @Autowired
    private EvaluationMapper evaluationMapper;

    @Override
    public EvaluationInfo queryById(Integer id) {
        return evaluationMapper.selectByPrimaryKey(id);
    }

    /**
     * 通过搜索条件查询并排序
     * @param search
     * @param sort
     * @return
     */
    @Override
    public List<EvaluationInfo> queryByProperties(Search search, String sort) {
        return evaluationMapper.selectByExample(new ExampleBuilder(EvaluationInfo.class).search(search).sort(sort).build());
    }

    /**
     * 查找当前对象的所有评论数
     * @param objId
     * @return
     */
    public Integer sumCountById(Integer objId){
        Search search = new Search();
        search.put("objId",objId);
        search.put("status", STATUS.YES.code);
        return evaluationMapper.selectCountByExample(new ExampleBuilder(EvaluationInfo.class).search(search).build());
    }

    /**
     * 分页查询pid的list
     * @param pid
     * @param page
     * @param size
     * @return
     */
    @Override
    public List<EvaluationInfo> queryAllList(Integer pid, int page, int size) {
        int offset = (page-1) *size;
        Search search = new Search();
        search.put("objId",pid);
        return evaluationMapper.selectByExampleAndRowBounds(
                new ExampleBuilder(EvaluationInfo.class).search(search).build(), new RowBounds(offset, size));
    }

    /**
     * 变更状态
     * @param id
     * @param status
     * @return
     */
    public int changeStatus(Integer id, Integer status, Integer uid){
        return 0;
    }

    @Override
    public int deleteById(Integer id) {
        return evaluationMapper.deleteByPrimaryKey(id);
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
     * 增加新评论
     * @param evaluationInfo
     * @return
     */
    @Override
    public int addNew(EvaluationInfo evaluationInfo) {
        return evaluationMapper.insertSelective(evaluationInfo);
    }

    @Override
    public int addNews(List<EvaluationInfo> evaluationInfos) {
        return 0;
    }

    @Override
    public int updateInfo(EvaluationInfo evaluationInfo) {
        Integer cuid = (Integer)WebSession.getVal("cuid");
        evaluationInfo.setUpdateUser(cuid);
        Search search = new Search();
        search.put("id", evaluationInfo.getId());
        return evaluationMapper.updateByExampleSelective(evaluationInfo, new ExampleBuilder(EvaluationInfo.class).search(search).build());
    }

    /**
     *
     * @param evaluationInfo
     * @param search
     * @return
     */
    public int updateInfoByExample(EvaluationInfo evaluationInfo, Search search){
        return evaluationMapper.updateByExampleSelective(evaluationInfo, new ExampleBuilder(EvaluationInfo.class).search(search).build());
    }

}
