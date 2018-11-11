CREATE TABLE `cs_address_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `status` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '0-未删除|1-删除',
  `type` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '归属者类型：0-用户|1-商铺',
  `pid` int(11) unsigned NOT NULL COMMENT '归属者id',
  `province` varchar(10) NOT NULL DEFAULT '' COMMENT '省份',
  `city` varchar(10) NOT NULL DEFAULT '' COMMENT '市',
  `county` varchar(10) NOT NULL DEFAULT '' COMMENT '区/县',
  `consignee_name` varchar(11) NOT NULL DEFAULT '' COMMENT '收货人姓名',
  `phone_num` varchar(11) NOT NULL DEFAULT '' COMMENT '手机号',
  `detail_location` varchar(20) NOT NULL DEFAULT '' COMMENT '详细地址',
  `is_default` int(2) NOT NULL DEFAULT '0' COMMENT '0-不为默认|1-为默认',
  `create_time` bigint(10) unsigned NOT NULL DEFAULT '0',
  `update_time` bigint(10) unsigned NOT NULL DEFAULT '0',
  `create_user` int(10) unsigned NOT NULL DEFAULT '0',
  `update_user` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='地址信息表';

CREATE TABLE `cs_commodity_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `status` int(2) unsigned NOT NULL DEFAULT '0' COMMENT '0-未删除|1-删除',
  `commodity_name` varchar(50) NOT NULL COMMENT '商品名',
  `price` decimal(10,2) unsigned NOT NULL DEFAULT '999.99' COMMENT '商品价格',
  `shop_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '所属商铺id',
  `classes` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '商品所属类别：0-默认|1-衣|2-食|3-其它',
  `category_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '商品所属商铺中的类别',
  `short_introduction` varchar(255) NOT NULL COMMENT '简介',
  `introduction` varchar(500) NOT NULL COMMENT '商品概要介绍',
  `inventory` int(20) unsigned NOT NULL DEFAULT '0' COMMENT '库存',
  `specifications` varchar(255) NOT NULL DEFAULT '' COMMENT '规格：个/条/袋/斤/千克',
  `create_time` bigint(20) unsigned NOT NULL DEFAULT '0',
  `update_time` bigint(20) unsigned NOT NULL DEFAULT '0',
  `create_user` int(20) unsigned NOT NULL DEFAULT '0',
  `update_user` int(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `isc` (`id`,`shop_id`,`category_id`) USING BTREE,
  KEY `sid` (`shop_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='商品信息表';

CREATE TABLE `cs_evaluate_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `type` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '评价类型：0-商品|1-商铺',
  `status` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '0-未删|1-删除',
  `title` varchar(11) NOT NULL DEFAULT '' COMMENT '评价标题',
  `content` varchar(255) NOT NULL DEFAULT '' COMMENT '评价信息',
  `order_id` int(20) unsigned NOT NULL,
  `obj_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '归属对象id',
  `anonymity_name` varchar(255) NOT NULL DEFAULT '',
  `user_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
  `star_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '星级数',
  `create_time` bigint(20) unsigned NOT NULL DEFAULT '0',
  `update_time` bigint(20) unsigned NOT NULL DEFAULT '0',
  `create_user` int(20) unsigned NOT NULL DEFAULT '0',
  `update_user` int(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='评价信息表';

CREATE TABLE `cs_order_info` (
  `id` int(40) unsigned NOT NULL AUTO_INCREMENT,
  `bought_status` int(2) unsigned NOT NULL DEFAULT '0' COMMENT '购买状态（0-未购买|1-已购买）',
  `address_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '订单配送地址信息id',
  `trans_way` int(2) unsigned NOT NULL DEFAULT '0' COMMENT '配送方式：0-普通快递|1-顺丰快递',
  `trans_price` decimal(10,2) unsigned NOT NULL DEFAULT '999.99' COMMENT '配送费用',
  `comm_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '订单商品总数',
  `bought_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '当前订单购买次数',
  `sum_price` decimal(10,2) unsigned NOT NULL DEFAULT '999.99' COMMENT '订单总价格',
  `user_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
  `status` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '0-未删除|1-删除',
  `create_time` bigint(20) unsigned NOT NULL DEFAULT '0',
  `update_time` bigint(20) unsigned NOT NULL DEFAULT '0',
  `create_user` int(20) unsigned NOT NULL DEFAULT '0',
  `update_user` int(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `mix_idx` (`id`,`bought_status`,`user_id`,`status`,`create_user`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8 COMMENT='订单信息表';

CREATE TABLE `cs_resource_pic` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `url` varchar(255) NOT NULL DEFAULT '' COMMENT 'url地址',
  `obj_id` int(11) unsigned NOT NULL COMMENT '归属对象id',
  `type` int(11) unsigned NOT NULL COMMENT '归属对象类型（0-商品|1-商铺|2-用户|3-评价）',
  `attribute` int(11) unsigned NOT NULL COMMENT '属性（0-缩略图|1-详情图片|2-avatar图片|3-swiper图片）',
  `create_time` bigint(20) unsigned NOT NULL,
  `update_time` bigint(20) unsigned NOT NULL,
  `create_user` int(20) unsigned NOT NULL,
  `update_user` int(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `comb_idx` (`id`,`obj_id`,`type`) USING BTREE,
  UNIQUE KEY `id_idx` (`id`) USING BTREE,
  KEY `cs_idx` (`obj_id`,`type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

CREATE TABLE `cs_shop_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '店主id',
  `status` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '0-未删除|1-删除',
  `sells_count` int(20) unsigned NOT NULL DEFAULT '0' COMMENT '总销量',
  `shop_name` varchar(11) NOT NULL DEFAULT '' COMMENT '商铺名',
  `short_introduction` varchar(20) NOT NULL DEFAULT '' COMMENT '商铺简介',
  `type` int(11) unsigned NOT NULL DEFAULT '3' COMMENT '店铺类型：1-衣|2-食|3-其它',
  `phone_num` varchar(11) NOT NULL DEFAULT '' COMMENT '店铺手机号',
  `commodity_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '商品数量',
  `star_level` int(2) unsigned NOT NULL DEFAULT '0' COMMENT '星级：满五星',
  `address_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '商铺地址信息id',
  `introduction` varchar(255) NOT NULL DEFAULT '' COMMENT '详细介绍',
  `create_time` bigint(20) unsigned NOT NULL DEFAULT '0',
  `update_time` bigint(20) unsigned NOT NULL DEFAULT '0',
  `create_user` int(20) unsigned NOT NULL DEFAULT '0',
  `update_user` int(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='店铺信息表';

CREATE TABLE `cs_user_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `avatar_url` varchar(255) NOT NULL DEFAULT '' COMMENT '头像url',
  `open_id` varchar(50) NOT NULL DEFAULT '0' COMMENT 'open_id',
  `profession_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '职业Id',
  `role_id` int(10) unsigned NOT NULL DEFAULT '1' COMMENT '角色id：1-普通用户',
  `nick_name` varchar(20) NOT NULL DEFAULT '' COMMENT '用户名',
  `pass_word` varchar(255) NOT NULL DEFAULT '' COMMENT '加密后的密码',
  `phone_num` varchar(11) NOT NULL DEFAULT '0' COMMENT '手机号',
  `email` varchar(100) NOT NULL DEFAULT '' COMMENT '邮箱地址',
  `gender` int(1) unsigned NOT NULL DEFAULT '0' COMMENT '性别：1-男|0-女',
  `age` int(5) unsigned NOT NULL DEFAULT '0' COMMENT '年龄',
  `city` varchar(100) NOT NULL DEFAULT '' COMMENT '城市',
  `province` varchar(100) NOT NULL DEFAULT '' COMMENT '省份',
  `country` varchar(100) NOT NULL DEFAULT '' COMMENT '国家',
  `log_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '登录次数',
  `language` varchar(50) NOT NULL DEFAULT '' COMMENT '语言',
  `create_time` bigint(10) unsigned NOT NULL DEFAULT '0',
  `update_time` bigint(10) unsigned NOT NULL DEFAULT '0',
  `create_user` int(10) unsigned NOT NULL DEFAULT '0',
  `update_user` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户汇总信息表';

CREATE TABLE `cs_user_operation` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `status` int(2) unsigned NOT NULL DEFAULT '0' COMMENT '状态：0-未删除|1-删除',
  `behavior` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '行为：0-浏览|1-购物车|2-正在购买|3-已购买|4-收藏',
  `obj_id` int(11) unsigned NOT NULL COMMENT '对象id：商品/商铺',
  `type` int(11) unsigned NOT NULL COMMENT '类型：0-商品|1-商铺',
  `user_id` int(11) unsigned NOT NULL COMMENT '用户id',
  `count` int(11) unsigned NOT NULL DEFAULT '1' COMMENT '对当前对象的行为次数',
  `order_id` bigint(40) unsigned NOT NULL DEFAULT '0' COMMENT '订单id',
  `create_time` bigint(20) unsigned NOT NULL DEFAULT '0',
  `update_time` bigint(20) unsigned NOT NULL DEFAULT '0',
  `create_user` int(20) unsigned NOT NULL DEFAULT '0',
  `update_user` int(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `order_idx` (`order_id`) USING BTREE,
  KEY `bhv` (`behavior`,`type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=443 DEFAULT CHARSET=utf8 COMMENT='用户对商品的操作表';

