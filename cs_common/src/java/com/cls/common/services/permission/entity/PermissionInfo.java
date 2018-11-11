package com.cls.common.services.permission.entity;

import com.cls.common.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/5/14-18:19
 * Description：
 */
@Data
@Entity
@Table(name= "cs_permission_info")
public class PermissionInfo extends BaseEntity {

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "permission_name")
    private String permissionName;

    @Column(name = "status")
    private Integer status;
}
