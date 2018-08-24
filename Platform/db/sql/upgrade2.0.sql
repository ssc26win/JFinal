
alter table sys_user add column `inner_code` varchar(50) DEFAULT '' COMMENT '所属公司编码';
alter table sys_user add column `wx_account` varchar(100) DEFAULT '' COMMENT '微信账号';
alter table sys_user add column `wx_memo` varchar(100) DEFAULT '' COMMENT '微信账号备用';


alter table t_company add column `real_code` varchar(100) DEFAULT '' COMMENT '单位编号';
alter table t_company modify column `inner_code` varchar(50) DEFAULT NULL COMMENT '内部编号';
alter table t_company add column `memo` varchar(255) DEFAULT '' COMMENT '备注';

alter table t_water_meter add column `memo` varchar(255) DEFAULT '' COMMENT '备注';

alter table app_version add column `wx_app_id` varchar(50) DEFAULT '' COMMENT '微信授权登录公钥';
alter table app_version add column `wx_app_secret` varchar(100) DEFAULT '' COMMENT '微信授权登录私钥';

CREATE TABLE `t_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `content` varchar(500) DEFAULT NULL COMMENT '内容',
  `img_url` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `memo` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='消息表';

CREATE TABLE `t_msg_receiver` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `msg_id` bigint(20) NOT NULL COMMENT '消息id',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态: 0 未读 1 已读',
  `memo` varchar(500) DEFAULT NULL COMMENT '备注',
  `receiver_id` bigint(20) DEFAULT NULL COMMENT '接收人Id',
  `receiver_name` varchar(50) DEFAULT NULL COMMENT '接收人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='消息接收者表';

CREATE TABLE `t_law_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `content` varchar(500) DEFAULT NULL COMMENT '内容',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `memo` varchar(500) DEFAULT NULL COMMENT '备注',
  `longitude` decimal(20,6) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(20,6) DEFAULT NULL COMMENT '纬度',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='执法记录表';

CREATE TABLE `t_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `rela_id` bigint(20) DEFAULT NULL COMMENT '引用表id',
  `rela_type` varchar(50) DEFAULT NULL COMMENT '引用表类型',
  `img_url` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `memo` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='图片表';

insert into `sys_res`(`name`, `seq`, `pid`, `type`, `iconCls`, `url`) values('App管理', 9, 0, 1, 'fa-android', '');
insert into `sys_role_res`(`role_id`, `res_id`) values(1, 209);

insert into `sys_res`(`name`, `seq`, `pid`, `type`, `iconCls`, `url`) values('App版本', 1, 209, 1, 'fa-list', '/app');
insert into `sys_role_res`(`role_id`, `res_id`) values(1, 210);

insert into `sys_res`(`name`, `seq`, `pid`, `type`, `iconCls`, `url`) values('执法记录', 2, 209, 1, 'fa-list', '/app/law');
insert into `sys_role_res`(`role_id`, `res_id`) values(1, 211);

insert into `sys_res`(`name`, `seq`, `pid`, `type`, `iconCls`, `url`) values('消息管理', 10, 0, 1, 'fa-comments', '');
insert into `sys_role_res`(`role_id`, `res_id`) values(1, 212);

insert into `sys_res`(`name`, `seq`, `pid`, `type`, `iconCls`, `url`) values('发布消息', 1, 212, 1, 'fa-list', '/basic/msg');
insert into `sys_role_res`(`role_id`, `res_id`) values(1, 213);

update sys_res set pid=212 where id=205;
update sys_res set seq=20 where id=1;

