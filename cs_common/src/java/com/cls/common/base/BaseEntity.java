package com.cls.common.base;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/3/29-22:49
 * Description：
 *     基础实体类
 */
@Data
public class BaseEntity {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "update_time")
    private Long updateTime;

    @Column(name = "create_user")
    private Integer createUser;

    @Column(name = "update_user")
    private Integer updateUser;

}
