package com.cls.web.user.entity.vo;

import com.cls.common.base.BaseVO;
import com.cls.common.services.permission.entity.PermissionInfo;
import lombok.Data;

import java.util.List;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/5/15-20:51
 * Description：
 */
@Data
public class RoleInfoVO extends BaseVO{

    private String roleName;

    private List<PermissionInfoVO> permissionInfoList;
}
