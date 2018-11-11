package com.cls.common.base;

import lombok.Data;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/22-0:45
 * Description：
 */
@Data
public class BaseVO {

    private Integer id;

    private Long createTime;

    private Long updateTime;

    private Integer createUser;

    private Integer updateUser;
}
