package com.cls.web.user;

import com.cls.common.base.BaseController;
import com.cls.common.exception.ClsException;
import com.cls.common.search.Search;
import com.cls.common.services._enum.STATUS;
import com.cls.common.services.user.entity.UserInfo;
import com.cls.common.services.user.service.impl.UserServiceImpl;
import com.cls.common.stat.LogStat;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Project: cs_backend
 *
 * @author Yue
 * @create 2018/5/16-21:03
 * Description：
 */
@Component
public class AuthcTool extends BaseController{

    @Autowired
    private UserServiceImpl userService;

    /**
     * 鉴权---店主
     */
    public boolean verifyOwnerAuthc(){
        Integer uid = getUID();
        Integer rid = getRID();
        if(!rid.equals(ROLE.SHOP_OWNER.code)){
            throw new ClsException(LogStat.NO_AUTHC);
        }
        Search search = new Search();
        search.put("id", uid);
        search.put("status",STATUS.YES.code);
        search.put("roleId",rid);
        List<UserInfo> userInfos = userService.queryUserInfo(search);
        if(CollectionUtils.isEmpty(userInfos)){
            throw new ClsException(LogStat.NO_LOGIN);
        }
        return true;
    }

    /**
     * 鉴权----管理员
     */
    public boolean verifyAdminAuthc(){
        Integer uid = getUID();
        Integer rid = getRID();
        if(!rid.equals(ROLE.ADMIN.code)){
            throw new ClsException(LogStat.NO_AUTHC);
        }
        Search sea = new Search();
        sea.put("id", uid);
        sea.put("status", STATUS.YES.code);
        sea.put("roleId", rid);
        List<UserInfo> userInfolst = userService.queryUserInfo(sea);
        if(CollectionUtils.isEmpty(userInfolst)){
            throw new ClsException(LogStat.NO_LOGIN);
        }
        return true;
    }

}
