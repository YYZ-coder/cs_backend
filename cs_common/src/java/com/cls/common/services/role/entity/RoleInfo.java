package com.cls.common.services.role.entity;

import com.cls.common.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/5/14-18:13
 * Description：
 *
 */
@Data
@Entity
@Table(name= "cs_role_info")
public class RoleInfo extends BaseEntity {

    @Column(name = "role_name")
    private String roleName;

    @Column(name= "status")
    private Integer status;
}
