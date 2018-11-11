package com.cls.wx.evaluate.controller;

import com.cls.common.backdata.JsonBackData;
import com.cls.common.base.BaseController;
import com.cls.common.exception.ClsException;
import com.cls.common.search.Search;
import com.cls.common.stat.LogStat;
import com.cls.common.utils.DateUtil;
import com.cls.common.utils.DozerUtil;
import com.cls.common.services._enum.ATTRIBUTE;
import com.cls.common.services._enum.STATUS;
import com.cls.common.services._enum.TYPE;
import com.cls.wx.evaluate.dto.EvaluationDTO;
import com.cls.common.services.evaluate.entity.EvaluationInfo;
import com.cls.common.services.evaluate.service.impl.EvaluationServiceImpl;
import com.cls.wx.evaluate.vo.EvaluationVO;
import com.cls.common.services.resource.entity.PicInfo;
import com.cls.common.services.resource.service.impl.ResourceServiceImpl;
import com.cls.common.services.resource.vo.PicInfoVO;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/26-23:52
 * Description：
 *      评论操作
 */
@RequestMapping(value = "/wx/evaluate")
@Controller
public class EvaluateController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(EvaluateController.class);

    @Autowired
    private EvaluationServiceImpl evaluationService;

    @Autowired
    private ResourceServiceImpl resourceService;

    /**
     * 包装资源信息
     * @param evaluationVOS
     */
    private void wrapResourceIn(List<EvaluationVO> evaluationVOS){
        List<Integer> ids = evaluationVOS.stream().map(EvaluationVO::getId).collect(Collectors.toList());
        Map<Integer, List<PicInfo>> idPicInfosMap = new HashMap<>();
        Search search = new Search();
        search.put("objId_in", ids);
        search.put("type", TYPE.evaluation.code);
        search.put("attribute", ATTRIBUTE.THUMB_PIC.code);
        List<PicInfo> picInfos = resourceService.queryByProperties(search, "");
        if(CollectionUtils.isEmpty(picInfos)){
            return;
        }
        for(PicInfo picInfo : picInfos){
            List<PicInfo> picInfoList = idPicInfosMap.get(picInfo.getObjId());
            if(CollectionUtils.isEmpty(picInfoList)){
                idPicInfosMap.put(picInfo.getObjId(), Lists.newArrayList(picInfo));
            }else{
                picInfoList.add(picInfo);
            }
        }
        for(EvaluationVO evaluationVO : evaluationVOS){
            List<PicInfo> picInfos1 = idPicInfosMap.get(evaluationVO.getId());
            List<PicInfoVO> picInfoVOS = DozerUtil.mapToList(picInfos1, PicInfoVO.class);
            evaluationVO.setThumbPics(picInfoVOS);
        }

    }

    /**
     * 当前用户对当前订单中商品的评论
     * @param objId
     * @param orderId
     * @return JsonBackData
     */
    @RequestMapping(value = "/querybyowner", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData queryByOwner(
            @RequestParam("objId") @NotNull @Valid Integer objId,
            @RequestParam("orderId") @NotNull @Valid Integer orderId
    ){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            Search search = new Search();
            search.put("objId",objId);
            search.put("orderId",orderId);
            search.put("type", TYPE.commodity.code);
            search.put("status", STATUS.YES.code);
            Integer cuid = getUID();
            search.put("userId", cuid);
            search.put("createUser", cuid);
            List<EvaluationInfo> evaluationInfos = evaluationService.queryByProperties(search,"updateTime_desc");
            if(CollectionUtils.isEmpty(evaluationInfos)){
                throw new ClsException(LogStat.NO_DATA);
            }
            List<EvaluationVO> evaluationVOS = DozerUtil.mapToList(evaluationInfos, EvaluationVO.class);
            jsonBackData.setBackData(evaluationVOS);
        }catch(Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData, t);
        }
        return jsonBackData;
    }

    /**
     * 查找当前commodity的评论List
     * @param cid
     * @return
     */
    @RequestMapping(value = "/querybycomm", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData queryByCommodity(
            @RequestParam(value = "cid") @Valid @NotNull @Min(1) Integer cid
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            Search search = new Search();
            search.put("objId", cid);
            search.put("status", STATUS.YES.code);
            search.put("type", TYPE.commodity.code);
            List<EvaluationInfo> evaluationInfoList = evaluationService.queryByProperties(search, "createTime_desc");
            if(CollectionUtils.isEmpty(evaluationInfoList)){
                throw new ClsException(LogStat.NO_EVALUATION, "当前商品没有评论["+ cid +"]");
            }
            List<EvaluationVO> evaluationVOS = DozerUtil.mapToList(evaluationInfoList, EvaluationVO.class);
            wrapResourceIn(evaluationVOS);
            jsonBackData.setBackData(evaluationVOS);
        }catch (Exception e){
            logger.warn("[]" + e.getCause().toString());
            wrapException(jsonBackData, e);
        }
        return jsonBackData;
    }


    /**
     * 修改评论
     * @param evaluationDTO
     * @return JsonBackData
     */
    @RequestMapping(value = "/modifyevalu",method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData modifyEvaluation(
            @RequestBody @Valid @NotNull EvaluationDTO evaluationDTO
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            Search search = new Search();
            search.put("id", evaluationDTO.getId());
            Integer cuid = getUID();
            search.put("userId", cuid);
            search.put("createUser", cuid);
            List<EvaluationInfo> evaluationInfos = evaluationService.queryByProperties(search,"");
            if(CollectionUtils.isEmpty(evaluationInfos)){
                throw new ClsException(LogStat.NO_DATA);
            }
            EvaluationInfo evaluatInf = evaluationInfos.get(0);
            evaluatInf.setStarCount(evaluationDTO.getStarCount());
            evaluatInf.setUpdateTime(DateUtil.getCurrentMili());
            evaluatInf.setContent(evaluationDTO.getContent());
            evaluatInf.setTitle(evaluationDTO.getTitle());
            evaluationService.updateInfo(evaluatInf);
            jsonBackData.setBackData(evaluatInf);
        }catch(Throwable e){
            wrapException(jsonBackData,e);
        }
        return jsonBackData;
    }

    /**
     * 添加评论
     * @param evaluationDTO
     * @return JsonBackData
     */
    @RequestMapping(value = "/addevalutocomm", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData addEvaluation(
             @RequestBody @Valid @NotNull EvaluationDTO evaluationDTO
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            EvaluationInfo evaluationInfo = DozerUtil.mapTo(evaluationDTO, EvaluationInfo.class);
            Integer cuid = getUID();
            //随机生成匿名名称
            evaluationInfo.setAnonymityName("xxx");
            evaluationInfo.setCreateTime(DateUtil.getCurrentMili());
            evaluationInfo.setCreateUser(cuid);
            evaluationInfo.setUpdateTime(DateUtil.getCurrentMili());
            evaluationInfo.setUpdateUser(cuid);
            evaluationInfo.setUserId(cuid);
            evaluationService.addNew(evaluationInfo);
            jsonBackData.setBackData("添加成功");
        }catch(Throwable e){
            logger.warn(e.getCause().toString());
            wrapException(jsonBackData,e);
        }
        return jsonBackData;
    }

    //-----------------------------------------------------------------------------

    /**
     * 删除评论---仅系统管理员
     * @param eid
     * @return JsonBackData
     */
    @RequestMapping(value = "/deleEvalu", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData deleteEvaluation(
            @RequestParam("eid") @Valid @NotNull Integer eid
    ){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            Integer cuid = getUID();
            //验证存在性
            EvaluationInfo evaluationInfo = evaluationService.queryById(eid);
            if(evaluationInfo == null){
                throw new ClsException(LogStat.NO_EVALUATION);
            }
            evaluationInfo.setUpdateUser(cuid);
            evaluationInfo.setUpdateTime(DateUtil.getCurrentMili());
            evaluationInfo.setStatus(STATUS.NO.code);
            evaluationService.updateInfoByExample(evaluationInfo, new Search().put("id",eid));
            jsonBackData.setBackData("删除评论成功");
        }catch (Throwable e){
            e.printStackTrace();
            wrapException(jsonBackData,e);
        }
        return jsonBackData;
    }

}
