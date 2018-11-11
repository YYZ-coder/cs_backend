package com.cls.web.user.controller;


import com.cls.common.backdata.JsonBackData;
import com.cls.common.base.BaseController;
import com.cls.common.exception.ClsException;
import com.cls.common.search.Search;
import com.cls.common.services._enum.STATUS;
import com.cls.common.services.permission.entity.PermissionInfo;
import com.cls.common.services.permission.service.impl.PermissionServiceImpl;
import com.cls.common.services.role.entity.RoleInfo;
import com.cls.common.services.role.service.impl.RoleServiceImpl;
import com.cls.common.services.user.entity.UserInfo;
import com.cls.common.stat.LogStat;
import com.cls.common.utils.DateUtil;
import com.cls.common.utils.DozerUtil;
import com.cls.web.user.AuthcTool;
import com.cls.web.user.ROLE;
import com.cls.web.user.entity.vo.PermissionInfoVO;
import com.cls.web.user.entity.vo.RoleInfoVO;
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

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/5/14-16:43
 * Description：
 */
@RequestMapping("/web/role")
@Controller
public class RoleController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private AuthcTool authcTool;

    @Autowired
    private PermissionServiceImpl permissionService;

    /**
     * 包装角色详细信息
     * @param roleInfoVO
     */
    private void wrapRoleInfoDetail(RoleInfoVO roleInfoVO){
        Integer rid = roleInfoVO.getId();
        Search search = new Search();
        search.put("roleId",rid);
        List<PermissionInfo> permissionInfos = permissionService.queryByProperties(search,"updateTime_desc");
        if(CollectionUtils.isEmpty(permissionInfos)){
            throw new ClsException(LogStat.NO_DATA);
        }
        List<PermissionInfoVO> permissionInfoVOS = DozerUtil.mapToList(permissionInfos,PermissionInfoVO.class);
        roleInfoVO.setPermissionInfoList(permissionInfoVOS);
    }

    /**
     * 角色详情
     * @param id
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData queryDetail(@RequestParam @Valid @NotNull @Min(1) Integer id){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            authcTool.verifyAdminAuthc();
            RoleInfo roleInfo = roleService.queryById(id);
            if(roleInfo == null){
                throw new ClsException(LogStat.NO_DATA);
            }
            RoleInfoVO roleInfoVO = DozerUtil.mapTo(roleInfo,RoleInfoVO.class);
            wrapRoleInfoDetail(roleInfoVO);
            jsonBackData.setBackData(roleInfoVO);
        }catch(Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData, t);
        }
        return jsonBackData;
    }

    /**
     * 删除角色信息
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData deleteRoles(
            @RequestBody @Valid @NotNull List<Integer> ids
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            authcTool.verifyAdminAuthc();
            RoleInfo roleInfo = new RoleInfo();
            roleInfo.setUpdateTime(DateUtil.getCurrentMili());
            roleInfo.setUpdateUser(getUID());
            roleInfo.setStatus(STATUS.NO.code);
            Search search = new Search();
            search.put("id_in", ids);
            roleService.updateInfoByExample(roleInfo,search);
            jsonBackData.setBackData("删除角色成功");
        }catch(Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData,t);
        }
        return jsonBackData;
    }

    /**
     * 查询全部角色
     * @return
     */
    @RequestMapping(value = "/queryAll", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData queryRole(){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            authcTool.verifyAdminAuthc();
            Search search = new Search();
            search.put("status", STATUS.YES.code);
            List<RoleInfo> roleInfoList = roleService.queryByProperties(search,"createTime_desc");
            if(CollectionUtils.isEmpty(roleInfoList)){
                throw new ClsException(LogStat.NO_DATA);
            }
            List<RoleInfoVO> roleInfoVOS = DozerUtil.mapToList(roleInfoList,RoleInfoVO.class);
            jsonBackData.setBackData(roleInfoVOS);
        }catch(Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData, t);
        }
        return jsonBackData;
    }

    /**
     * 角色信息搜索
     * @param key
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData search(
            @RequestParam("key") @NotNull @Valid String key
    ){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            authcTool.verifyAdminAuthc();
            List<RoleInfo> roleInfos = roleService.queryByRestriction("roleName","%" + key + "%");
            if(CollectionUtils.isEmpty(roleInfos)){
                throw new ClsException(LogStat.NO_DATA);
            }
            List<RoleInfoVO> roleInfoVOS = DozerUtil.mapToList(roleInfos,RoleInfoVO.class);
            jsonBackData.setBackData(roleInfoVOS);
        }catch(Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData, t);
        }
        return jsonBackData;
    }


}
