package com.cls.wx.address.controller;

import com.cls.common.backdata.JsonBackData;
import com.cls.common.base.BaseController;
import com.cls.common.exception.ClsException;
import com.cls.common.search.Search;
import com.cls.common.stat.LogStat;
import com.cls.common.utils.DateUtil;
import com.cls.common.utils.DozerUtil;
import com.cls.common.services._enum.STATUS;
import com.cls.common.services._enum.TYPE;
import com.cls.wx.address.dto.AddressDTO;
import com.cls.common.services.address.entity.AddressInfo;
import com.cls.common.services.address.service.impl.AddressServiceImpl;
import com.cls.wx.address.vo.AddressInfoVO;
import com.cls.common.services.shop.entity.ShopInfo;
import com.cls.common.services.shop.service.impl.ShopServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/29-10:20
 * Description：
 */
@Controller
@RequestMapping("/wx/addrs")
public class AddressController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    private AddressServiceImpl addressService;

    @Autowired
    private ShopServiceImpl shopService;


    /**
     * 加载当前用户的所有地址
     * @return
     */
    @RequestMapping(value = "/queryAll", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData queryAll(){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            Integer uid = getUID();
            Search search = new Search();
            search.put("pid",uid);
            search.put("type",TYPE.ADD_USER.code);
            search.put("status",STATUS.YES.code);
            List<AddressInfo> addressInfoList = addressService.queryByProperties(search,"");
            List<AddressInfoVO> addressInfoVOS = DozerUtil.mapToList( addressInfoList, AddressInfoVO.class);
            jsonBackData.setBackData(addressInfoVOS);
        }catch(Throwable t){
            logger.info(t.toString());
            wrapException(jsonBackData, t);
        }
        return jsonBackData;
    }

    /**
     * 加载默认地址
     * @return
     */
    @RequestMapping(value = "/loadDefault", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData loadDefault(){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            Integer uid = getUID();
            Search search = new Search();
            search.put("type",TYPE.ADD_USER.code);
            search.put("status",STATUS.YES.code);
            search.put("isDefault", 1);//默认地址
            search.put("pid",uid);
            List<AddressInfo> addressInfos = addressService.queryByProperties(search, "");
            if(CollectionUtils.isEmpty(addressInfos)){
                throw new ClsException(LogStat.NO_DATA);
            }
            AddressInfoVO addressInfoVO = DozerUtil.mapTo(addressInfos.get(0), AddressInfoVO.class);
            jsonBackData.setBackData(addressInfoVO);
        }catch (Throwable t){
            logger.info(t.toString());
            wrapException(jsonBackData, t);
        }
        return jsonBackData;
    }

    /**
     * 设置默认地址
     * @param id
     * @return
     */
    @RequestMapping(value = "/setDefault", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData setDefault(
            @RequestParam("id") @Valid @NotNull Integer id
    ){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            Integer uid = getUID();
            AddressInfo addressInfo = addressService.queryById(id);
            if(addressInfo == null){
                throw new ClsException(LogStat.NO_DATA);
            }
            Search search = new Search();
            search.put("pid",uid);
            search.put("status",STATUS.YES.code);
            search.put("type", TYPE.commodity.code);
            List<AddressInfo> addressInfos = addressService.queryByProperties(search,"");
            if(CollectionUtils.isEmpty(addressInfos)){
                throw new ClsException(LogStat.NO_DATA);
            }
            List<Integer> ids = addressInfos.stream().map(AddressInfo::getId).collect(Collectors.toList());
            //全部置为noDefault
            addressService.setNoDefault(ids);
            //一个置为default
            addressService.setDefault(id);
            jsonBackData.setBackData("设置成功");
        }catch(Throwable t){
            logger.info(t.toString());
            wrapException(jsonBackData,t);
        }
        return jsonBackData;
    }

    /**
     * 由id查询
     * @param id
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData queryById(@RequestParam @Valid @NotNull @Min(1) Integer id){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            Integer cuid = getUID();
            AddressInfo addressInfo = addressService.queryById(id);
            if(addressInfo == null){
                throw new ClsException(LogStat.NO_DATA);
            }
            if(addressInfo.getType().equals(TYPE.ADD_USER.code) && !addressInfo.getPid().equals(cuid)){
                throw new ClsException(LogStat.NO_AUTHC);
            }
            AddressInfoVO addressInfoVO = DozerUtil.mapTo(addressInfo, AddressInfoVO.class);
            jsonBackData.setBackData(addressInfoVO);
        }catch(Throwable t){
            logger.info(t.toString());
            wrapException(jsonBackData,t);
        }
        return jsonBackData;
    }

    /**
     * 删除地址
     * @param aid
     * @return JsonBackData
     */
    @RequestMapping(value = "/delteAddress", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData deletAddress(@RequestParam @Valid @NotNull @Min(1) Integer aid){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            AddressInfo addressInfo = addressService.queryById(aid);
            if(addressInfo == null){
                throw new ClsException(LogStat.NO_DATA);
            }
            Integer cuid = getUID();
            addressInfo.setStatus(STATUS.NO.code);
            addressInfo.setUpdateUser(cuid);
            addressInfo.setUpdateTime(DateUtil.getCurrentMili());
            if(addressInfo.getType().equals(TYPE.ADD_SHOP.code)){
                //验证店铺存在性
                Search search = new Search().put("id", addressInfo.getPid()).put("uid", cuid).put("status",STATUS.YES.code);
                List<ShopInfo> shopInfos = shopService.queryByProperties(search, "");
                if(CollectionUtils.isEmpty(shopInfos)){
                    throw new ClsException(LogStat.NOTEXIST_SHOP);
                }
                Search searchadd = new Search().put("id",aid);
                addressService.updateInfoByExample(addressInfo, searchadd);
            }else if(addressInfo.getType().equals(TYPE.ADD_USER.code)){
                addressService.updateInfoByExample(addressInfo, new Search().put("id",aid));
            }else{
                throw new ClsException(LogStat.DATA_FALSE);
            }
            jsonBackData.setBackData("删除地址成功");
        }catch (Throwable t){
            logger.info(t.toString());
            wrapException(jsonBackData, t);
        }
        return jsonBackData;
    }

    /**
     * 修改地址
     * @return JsonBackData
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData modifyAddress(
            @RequestBody @Valid @NotNull AddressDTO addressDTO
    ){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            if( addressDTO.getId() == null || addressDTO.getId().equals(0)){
                throw new ClsException(LogStat.PARAM_LEAK);
            }
            Integer uid = getUID();
            AddressInfo addressInfo = DozerUtil.mapTo(addressDTO, AddressInfo.class);
            Search search = new Search().put("id",addressDTO.getId());
            addressInfo.setUpdateUser(uid);
            addressInfo.setUpdateTime(DateUtil.getCurrentMili());
            //检查当前用户是否有当前要修改的地址信息
            addressService.updateInfoByExample(addressInfo, search);
            jsonBackData.setBackData("修改成功");
        }catch (Throwable t){
            logger.info(t.toString());
            wrapException(jsonBackData,t);
        }
        return jsonBackData;
    }

    /**
     * 新增地址
     * @param addressDTO
     * @return
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData newAddress(
            @RequestBody @Valid @NotNull AddressDTO addressDTO
    ){

        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            AddressInfo addressInfo = DozerUtil.mapTo(addressDTO, AddressInfo.class);
            Integer cuid = getUID();
            if(addressDTO.getType().equals(TYPE.ADD_USER.code)){
                addressInfo.setPid(cuid);
            }else if(addressDTO.getType().equals(TYPE.ADD_SHOP.code) &&
                    addressDTO.getPid() != null &&
                    !addressDTO.getPid().equals(0)){
                ShopInfo shopInfo = shopService.queryById(addressDTO.getPid());
                //shop归属为当前用户
                if(shopInfo == null && !shopInfo.getUid().equals(cuid)){
                    throw new ClsException(LogStat.NOTEXIST_SHOP);
                }
                addressInfo.setPid(shopInfo.getId());
            }
            addressInfo.setUpdateTime(DateUtil.getCurrentMili());
            addressInfo.setUpdateUser(cuid);
            addressInfo.setCreateTime(DateUtil.getCurrentMili());
            addressInfo.setCreateUser(cuid);
            addressService.addNew(addressInfo);
        }catch(Throwable t){
            logger.info(t.toString());
            wrapException(jsonBackData,t);
        }
        return jsonBackData;
    }
}
