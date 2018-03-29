
alter table sys_user add column `inner_code` varchar(50) DEFAULT '' COMMENT '所属公司编码';
alter table sys_user add column `wx_account` varchar(100) DEFAULT '' COMMENT '微信账号';

alter table app_version add column `wx_app_id` varchar(50) DEFAULT '' COMMENT '微信授权登录公钥';
alter table app_version add column `wx_app_secret` varchar(100) DEFAULT '' COMMENT '微信授权登录私钥';


insert into `sys_role_res`(`role_id`, `res_id`) values(1, 209);
insert into `sys_res`(`name`, `seq`, `pid`, `type`, `iconCls`, `url`) values('App管理', 9, 0, 1, 'fa-android', '/app');


insert into `sys_role_res`(`role_id`, `res_id`) values(1, 210);
insert into `sys_res`(`name`, `seq`, `pid`, `type`, `iconCls`, `url`) values('预警水表', 6, 0, 1, 'fa-alarm', '/alarm');
