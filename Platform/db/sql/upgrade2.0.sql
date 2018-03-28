
alter table sys_user add column `inner_code` varchar(50) DEFAULT '' COMMENT '所属公司编码';
alter table sys_user add column `wx_account` varchar(100) DEFAULT '' COMMENT '微信账号';

alter table app_version add column `wx_app_id` varchar(50) DEFAULT '' COMMENT '微信授权登录公钥';
alter table app_version add column `wx_app_secret` varchar(100) DEFAULT '' COMMENT '微信授权登录私钥';


