package com.cls.web.commodity.controller;

import com.cls.common.backdata.JsonBackData;
import com.cls.common.base.BaseController;
import com.cls.common.exception.ClsException;
import com.cls.common.search.Search;
import com.cls.common.services.PublicService;
import com.cls.common.services._enum.STATUS;
import com.cls.common.services.behavior.service.impl.OperationServiceImpl;
import com.cls.common.services.commodity.CommodityDTO;
import com.cls.common.services.commodity.entity.CommodityInfo;
import com.cls.common.services.commodity.service.impl.CommodityServiceImpl;
import com.cls.common.services.commodity.vo.CommodityShortVO;
import com.cls.common.services.resource.vo.PicInfoVO;
import com.cls.common.services.shop.entity.ShopInfo;
import com.cls.common.services.shop.service.impl.ShopServiceImpl;
import com.cls.common.stat.LogStat;
import com.cls.common.utils.DateUtil;
import com.cls.common.utils.DozerUtil;
import com.cls.web.user.AuthcTool;
import com.cls.web.user.ROLE;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/5/17-8:47
 * Description：
 */
@RequestMapping("/web/comm")
@Controller
public class CommWebController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(CommWebController.class);

    @Autowired
    private CommodityServiceImpl commodityService;

    @Autowired
    private OperationServiceImpl operationService;

    @Autowired
    private ShopServiceImpl shopService;

    @Autowired
    private AuthcTool authcTool;

    /**
     * 通过店铺查询商品数据
     * @return JsonBackData
     */
    @RequestMapping(value = "/queryBys", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData queryByShopId(
            @RequestParam @NotNull Integer page,
            @RequestParam @NotNull Integer size
    ){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            Integer uid = getUID();
            Integer rid = getRID();
            if(!rid.equals(ROLE.SHOP_OWNER.code)){
                throw new ClsException(LogStat.NO_AUTHC);
            }
            Search se = new Search();
            se.put("uid",uid);
            List<ShopInfo> shopInfos = shopService.queryByProperties(se,"");
            if(CollectionUtils.isEmpty(shopInfos)){
                throw new ClsException(LogStat.NO_DATA);
            }
            Search search = new Search();
            search.put("shopId",shopInfos.get(0).getId());
            List<CommodityInfo> commodityInfos = commodityService.queryByProperties(search,"updateTime_desc");
            if(CollectionUtils.isEmpty(commodityInfos)){
                throw new ClsException(LogStat.NO_DATA);
            }
            List<CommodityShortVO> shortVOS = DozerUtil.mapToList(commodityInfos,CommodityShortVO.class);
            jsonBackData.setBackData(shortVOS);
        }catch(Throwable throwable){
            logger.warn(throwable.toString());
            wrapException(jsonBackData, throwable);
        }
        return jsonBackData;
    }

    /**
     * 查询全部
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/queryAll", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData queryAll(
            @RequestParam("page") @NotNull @Valid @Min(1) Integer page,
            @RequestParam("size") @NotNull @Valid @Min(1) Integer size
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            Search search = new Search();
            search.put("status",STATUS.YES.code);
            int offset = (page-1)*size;
            List<CommodityInfo> commodityInfoList = commodityService.queryAllByRowBounds(search, new RowBounds(offset, size));
            if(CollectionUtils.isEmpty(commodityInfoList)){
                throw new ClsException(LogStat.NO_DATA,"没有商品数据");
            }
            List<CommodityShortVO> commodityVOS = DozerUtil.mapToList(commodityInfoList, CommodityShortVO.class);
            PublicService.wrapSellsCount(operationService, commodityVOS);
            PublicService.wrapShopInfo(shopService,commodityVOS);
            jsonBackData.setBackData(commodityVOS);
        }catch(Throwable throwable){
            logger.warn(throwable.toString());
            wrapException(jsonBackData, throwable);
        }
        return jsonBackData;
    }


    /**
     * 修改商品信息
     * @param commodityDTO
     * @return
     */
    @RequestMapping(value = "/modifycomm", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData modifyCommodity(
            @RequestBody @NotNull @Valid CommodityDTO commodityDTO
    ){
        //TODO:需判断当前商家shopId和uid的关系是否对应
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            authcTool.verifyOwnerAuthc();
            List<PicInfoVO> thumbPics = commodityDTO.getThumbPic();
            List<PicInfoVO> detailPics = commodityDTO.getDetailPicUrls();
            List<PicInfoVO> swiperPics = commodityDTO.getSwiperPicUrls();
            Integer uid = getUID();
            if(commodityDTO.getId() == null || commodityDTO.getId().equals(0)){
                throw new ClsException(LogStat.DATA_FALSE);
            }
            CommodityInfo commodityInfo =  commodityService.queryById(commodityDTO.getId());
            if(commodityInfo == null){
                throw new ClsException(LogStat.NO_DATA);
            }
            commodityInfo.setCategoryId(commodityDTO.getCategoryId());
            commodityInfo.setClasses(commodityDTO.getClasses());
            commodityInfo.setCommodityName(commodityDTO.getCommodityName());
            commodityInfo.setIntroduction(commodityDTO.getIntroduction());
            commodityInfo.setInventory(commodityDTO.getInventory());
            commodityInfo.setPrice(commodityDTO.getPrice());
            commodityInfo.setShortIntroduction(commodityDTO.getShortIntroduction());
            commodityInfo.setSpecifications(commodityDTO.getSpecifications());
            Search search = new Search();
            search.put("shopId",commodityDTO.getShopId());
            search.put("id",commodityDTO.getId());
            commodityInfo.setUpdateTime(DateUtil.getCurrentMili());
            commodityInfo.setUpdateUser(uid);
            if(0 == commodityService.updateInfoByExample(commodityInfo,search)){
                jsonBackData.setBackData("修改失败");
            }else{
                jsonBackData.setBackData("修改成功");
            }
        }catch (Throwable t){
            wrapException(jsonBackData, t);
        }
        return jsonBackData;
    }

    /**
     * 删除商品---仅店主
     * @param cids
     * @return JsonBackData
     */
    @RequestMapping(value = "/deleteComm", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData deleteCommodity(
            @RequestBody @NotNull @Valid List<Integer> cids
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            if(true == authcTool.verifyOwnerAuthc()){
                Integer uid = getUID();
                List<CommodityInfo> commodityInfos = commodityService.queryByObjIds(cids);
                if(CollectionUtils.isEmpty(commodityInfos)){
                    throw new ClsException(LogStat.NO_DATA);
                }
                CommodityInfo commodityInfo = new CommodityInfo();
                commodityInfo.setStatus(STATUS.NO.code);
                commodityInfo.setUpdateUser(uid);
                commodityInfo.setUpdateTime(DateUtil.getCurrentMili());
                if(0 == commodityService.updateInfoByExample(commodityInfo,new Search().put("id_in",cids))){
                    jsonBackData.setBackData("删除失败");
                }else{
                    jsonBackData.setBackData("删除成功");
                }
            }
        }catch(Throwable e){
            logger.warn(e.toString());
            wrapException(jsonBackData,e);
        }
        return jsonBackData;
    }

    /**
     * 新增商品---仅店主
     * @param commodityDTO
     * @return JsonBackData
     */
    @RequestMapping(value = "/addComm",method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData addCommodity(
            @RequestBody @Valid @NotNull CommodityDTO commodityDTO
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            authcTool.verifyOwnerAuthc();
            Integer uid = getUID();
            CommodityInfo commodityInfo = DozerUtil.mapTo(commodityDTO, CommodityInfo.class);
            commodityInfo.setCreateUser(uid);
            commodityInfo.setCreateTime(DateUtil.getCurrentMili());
            commodityInfo.setUpdateUser(uid);
            commodityInfo.setUpdateTime(DateUtil.getCurrentMili());
            if(0 == commodityService.addNew(commodityInfo)){
                throw new ClsException(LogStat.INSERT_FAILED, "商品添加失败");
            }else{
                jsonBackData.setBackData("商品添加成功");
            }
        }catch(Throwable e){
            logger.warn(e.toString());
            wrapException(jsonBackData,e);
        }
        return jsonBackData;
    }

}
