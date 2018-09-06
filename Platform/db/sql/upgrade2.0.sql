
alter table sys_user add column `inner_code` varchar(50) DEFAULT '' COMMENT '所属公司编码';
alter table sys_user add column `wx_account` varchar(100) DEFAULT '' COMMENT '微信账号';
alter table sys_user add column `wx_memo` varchar(100) DEFAULT '' COMMENT '微信账号备用';


alter table t_company add column `real_code` varchar(100) DEFAULT '' COMMENT '单位编号';
alter table t_company modify column `inner_code` varchar(50) DEFAULT NULL COMMENT '内部编号';
alter table t_company add column `memo` varchar(255) DEFAULT '' COMMENT '备注';

alter table t_water_meter add column `memo` varchar(255) DEFAULT '' COMMENT '备注';
alter table t_water_meter add column `vender` varchar(255) DEFAULT '' COMMENT '生产厂家';

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

insert into dict_type(id, name, remark, update_time) values (15, 'MeterAttr', '水表属性', 1524888888) ;

insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 2, 2, 1524888888,'食品制造业','食品制造业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 3, 3, 1524888888,'金属制品业', '金属制品业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 4, 4, 1524888888,'部队', '部队');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 5, 5, 1524888888,'道路运输业', '道路运输业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 6, 6, 1524888888,'通用设备制造业', '通用设备制造业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 7, 7, 1524888888,'装卸搬运和其他运输服务业', '装卸搬运和其他运输服务业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 8, 8, 1524888888,'纺织服装、鞋、帽制造业',  '纺织服装、鞋、帽制造业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 9, 9, 1524888888,'纺织业',  '纺织业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 10, 10, 1524888888,'研究与试验发展',  '研究与试验发展');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 11, 11, 1524888888,'畜牧业',  '畜牧业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 12, 12, 1524888888,'电气机械及器材制造业', '电气机械及器材制造业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 13, 13, 1524888888,'电力、热力的生产和供应业', '电力、热力的生产和供应业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 14, 14, 1524888888,'环境管理业',  '环境管理业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 15, 15, 1524888888,'水的生产和供应业', '水的生产和供应业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 16, 16, 1524888888,'有色金属冶炼及压延加工业', '有色金属冶炼及压延加工业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 17, 17, 1524888888,'文化艺术业',  '文化艺术业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 18, 18, 1524888888,'教育', '教育');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 19, 19, 1524888888,'房屋和土木工程建筑业', '房屋和土木工程建筑业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 20, 20, 1524888888,'建筑装饰业',  '建筑装饰业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 21, 21, 1524888888,'建筑安装业',  '建筑安装业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 22, 22, 1524888888,'广播、电视、电影和音像业', '广播、电视、电影和音像业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 23, 23, 1524888888,'工艺品及其他制造业',  '工艺品及其他制造业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 24, 24, 1524888888,'居民服务业',  '居民服务业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 25, 25, 1524888888,'娱乐业',  '娱乐业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 26, 26, 1524888888,'国家机构', '国家机构');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 27, 27, 1524888888,'商务服务业',  '商务服务业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 28, 28, 1524888888,'印刷业和记录媒介的复制',  '印刷业和记录媒介的复制');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 29, 29, 1524888888,'卫生', '卫生');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 30, 30, 1524888888,'医药制造业',  '医药制造业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 31, 31, 1524888888,'化学原料及化学制品制造业', '化学原料及化学制品制造业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 32, 32, 1524888888,'化学原料制品', '化学原料制品');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 33, 33, 1524888888,'农副食品加工业',  '农副食品加工业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 34, 34, 1524888888,'农、林、牧、渔服务业', '农、林、牧、渔服务业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 35, 35, 1524888888,'其他生产运营用水', '其他生产运营用水');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 36, 36, 1524888888,'其他服务业',  '其他服务业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 37, 37, 1524888888,'其他工业', '其他工业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 38, 38, 1524888888,'公共设施管理业',  '公共设施管理业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 39, 39, 1524888888,'体育', '体育');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 40, 40, 1524888888,'仓储业',  '仓储业');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 41, 41, 1524888888,'乡镇用水户',  '乡镇用水户');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 42, 42, 1524888888,'乡镇用水', '乡镇用水');
insert into dict_data(dict_type_id, value, seq, update_time, name, remark) values (15, 43, 43, 1524888888,'专用设备制造业' '专用设备制造业');

update t_water_meter twm set twm.meter_attr=(select dd.value from dict_data dd where dd.dict_type_id=15 and twm.meter_attr=dd.name)
where twm.meter_attr<>'' and twm.meter_attr is not NULL;

select id,meter_attr from t_water_meter ORDER BY meter_attr desc;
