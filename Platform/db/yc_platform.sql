/*
Navicat MySQL Data Transfer

Source Server         : shuibiao
Source Server Version : 50531
Source Host           : 47.94.196.59:8904
Source Database       : yc_platform

Target Server Type    : MYSQL
Target Server Version : 50531
File Encoding         : 65001

Date: 2017-09-05 19:13:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for access_token
-- ----------------------------
DROP TABLE IF EXISTS `access_token`;
CREATE TABLE `access_token` (
  `uid` int(10) NOT NULL,
  `token` varchar(64) NOT NULL COMMENT 'app用户登陆凭证',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `im_token` varchar(64) DEFAULT NULL COMMENT '第三方IM登陆凭证',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `token_index` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for app_version
-- ----------------------------
DROP TABLE IF EXISTS `app_version`;
CREATE TABLE `app_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL COMMENT '更新说明',
  `create_time` datetime DEFAULT NULL,
  `link_url` varchar(255) DEFAULT NULL COMMENT '外部下载地址',
  `nature_no` int(11) DEFAULT NULL COMMENT '自然版本号',
  `os` varchar(20) DEFAULT NULL COMMENT '系统类型android or ios',
  `url` varchar(200) DEFAULT NULL COMMENT '内部下载地址',
  `version_no` varchar(20) DEFAULT NULL COMMENT '版本号',
  `status` tinyint(4) DEFAULT NULL COMMENT '1-有效   0-无效',
  `is_force` tinyint(4) DEFAULT '0' COMMENT '是否强制升级 0:否 1：是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for dict_data
-- ----------------------------
DROP TABLE IF EXISTS `dict_data`;
CREATE TABLE `dict_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL COMMENT '名称',
  `value` varchar(255) NOT NULL COMMENT '对应的值',
  `remark` varchar(32) DEFAULT NULL,
  `seq` int(11) NOT NULL COMMENT '排序，由小到大',
  `update_time` int(10) NOT NULL,
  `dict_type_id` int(11) NOT NULL COMMENT '字典类型ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for dict_type
-- ----------------------------
DROP TABLE IF EXISTS `dict_type`;
CREATE TABLE `dict_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `remark` varchar(32) DEFAULT NULL,
  `update_time` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL COMMENT '系统用户ID',
  `from` varchar(255) DEFAULT NULL COMMENT '来源 url',
  `ip` varchar(22) DEFAULT NULL COMMENT '客户端IP',
  `url` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL COMMENT '记录时间',
  `err_msg` text COMMENT '异常信息',
  `err_code` int(10) DEFAULT '0' COMMENT '状态码，0：正常',
  `class_name` varchar(200) DEFAULT NULL COMMENT 'controller类名',
  `method_name` varchar(64) DEFAULT NULL COMMENT '方法名',
  `start_time` datetime DEFAULT NULL COMMENT '操作时间',
  `spend_time` bigint(20) DEFAULT NULL COMMENT '耗时，毫秒',
  `params` text COMMENT '提供的参数',
  PRIMARY KEY (`id`),
  KEY `FK_sys_EVENT` (`uid`),
  CONSTRAINT `sys_log_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=43893 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for sys_login_record
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_record`;
CREATE TABLE `sys_login_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sys_uid` int(11) NOT NULL,
  `login_date` datetime NOT NULL COMMENT '登陆时间',
  `login_err_times` int(11) NOT NULL COMMENT '1天内连续出错次数',
  `login_status` tinyint(4) NOT NULL COMMENT '1-成功  0-失败',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=135 DEFAULT CHARSET=utf8 COMMENT='后台系统登陆记录';

-- ----------------------------
-- Table structure for sys_res
-- ----------------------------
DROP TABLE IF EXISTS `sys_res`;
CREATE TABLE `sys_res` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) DEFAULT NULL,
  `name` varchar(111) DEFAULT NULL,
  `des` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `iconCls` varchar(255) DEFAULT 'am-icon-file',
  `seq` int(11) DEFAULT '1',
  `type` int(1) DEFAULT '2' COMMENT '1 功能 2 权限',
  `modifydate` timestamp NULL DEFAULT NULL,
  `enabled` int(1) DEFAULT '1' COMMENT '是否启用 1：启用  0：禁用',
  `level` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=207 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(55) NOT NULL DEFAULT '',
  `des` varchar(55) DEFAULT NULL,
  `seq` int(11) DEFAULT '1',
  `createdate` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '1' COMMENT '0-禁用  1-启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for sys_role_res
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_res`;
CREATE TABLE `sys_role_res` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `res_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_sys_ROLE_RES_RES_ID` (`res_id`),
  KEY `FK_sys_ROLE_RES_ROLE_ID` (`role_id`),
  CONSTRAINT `sys_role_res_ibfk_1` FOREIGN KEY (`res_id`) REFERENCES `sys_res` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sys_role_res_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4239 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(55) DEFAULT NULL,
  `pwd` varchar(255) DEFAULT NULL,
  `des` varchar(55) DEFAULT NULL,
  `status` int(1) DEFAULT '1' COMMENT '#1 不在线 2.封号状态 ',
  `icon` varchar(255) DEFAULT '/images/guest.jpg',
  `email` varchar(50) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `token` varchar(64) DEFAULT NULL COMMENT 'cookieid',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_index` (`name`),
  UNIQUE KEY `token_index` (`token`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_SYSTME_USER_ROLE_USER_ID` (`user_id`),
  KEY `FK_SYSTME_USER_ROLE_ROLE_ID` (`role_id`),
  CONSTRAINT `sys_user_role_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sys_user_role_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=345 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for t_actual_data
-- ----------------------------
DROP TABLE IF EXISTS `t_actual_data`;
CREATE TABLE `t_actual_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `company_id` bigint(20) DEFAULT NULL COMMENT '单位id',
  `inner_code` varchar(50) DEFAULT NULL COMMENT '单位编号',
  `line_num` varchar(255) DEFAULT NULL COMMENT '路别',
  `meter_num` varchar(50) DEFAULT NULL COMMENT '水表表号',
  `waters_type` tinyint(4) DEFAULT NULL COMMENT '水源类型',
  `alarm` varchar(255) DEFAULT NULL COMMENT '告警',
  `net_water` decimal(10,2) DEFAULT NULL COMMENT '净用水量',
  `state` tinyint(4) DEFAULT NULL COMMENT '状态',
  `write_time` datetime DEFAULT NULL COMMENT '抄表时间',
  `voltage` varchar(50) DEFAULT NULL COMMENT '电池电压',
  PRIMARY KEY (`id`),
  KEY `idx_inner_code` (`inner_code`) USING BTREE,
  KEY `idx_meter_num` (`meter_num`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='实时数据';

-- ----------------------------
-- Table structure for t_ad
-- ----------------------------
DROP TABLE IF EXISTS `t_ad`;
CREATE TABLE `t_ad` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `content` varchar(500) DEFAULT NULL COMMENT '内容',
  `img_url` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `status` tinyint(4) DEFAULT NULL COMMENT '发布状态',
  `memo` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='水井信息';

-- ----------------------------
-- Table structure for t_company
-- ----------------------------
DROP TABLE IF EXISTS `t_company`;
CREATE TABLE `t_company` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `inner_code` varchar(50) DEFAULT NULL COMMENT '单位编号',
  `name` varchar(255) DEFAULT NULL COMMENT '单位名称',
  `street` varchar(255) DEFAULT NULL COMMENT '所属乡镇或街道',
  `address` varchar(255) DEFAULT NULL COMMENT '单位地址',
  `customer_type` tinyint(4) DEFAULT NULL COMMENT '用户类型',
  `water_use_type` tinyint(4) DEFAULT NULL COMMENT '取水用途',
  `contact` varchar(100) DEFAULT NULL COMMENT '联系人',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `postal_code` varchar(50) DEFAULT NULL COMMENT '邮政编码',
  `department` varchar(255) DEFAULT NULL COMMENT '管水部门',
  `well_count` int(11) DEFAULT '0' COMMENT '水井数量',
  `first_watermeter_count` int(11) DEFAULT '0' COMMENT '一级表数量',
  `remotemeter_count` int(11) DEFAULT '0' COMMENT '远传表数量',
  `unit_type` tinyint(4) DEFAULT NULL COMMENT '节约用水型单位类型',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_inner_code` (`inner_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='单位表';

-- ----------------------------
-- Table structure for t_water_index
-- ----------------------------
DROP TABLE IF EXISTS `t_water_index`;
CREATE TABLE `t_water_index` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `company_id` bigint(20) DEFAULT NULL COMMENT '单位id',
  `inner_code` varchar(50) DEFAULT NULL COMMENT '单位编号',
  `water_use_type` varchar(255) DEFAULT NULL COMMENT '取水用途',
  `water_index` decimal(10,2) DEFAULT NULL COMMENT '年用水指标',
  `january` decimal(10,2) DEFAULT NULL COMMENT '一月',
  `february` decimal(10,2) DEFAULT NULL COMMENT '二月',
  `march` decimal(10,2) DEFAULT NULL COMMENT '三月',
  `april` decimal(10,2) DEFAULT NULL COMMENT '四月',
  `may` decimal(10,2) DEFAULT NULL COMMENT '五月',
  `june` decimal(10,2) DEFAULT NULL COMMENT '六月',
  `july` decimal(10,2) DEFAULT NULL COMMENT '七月',
  `august` decimal(10,2) DEFAULT NULL COMMENT '八月',
  `september` decimal(10,2) DEFAULT NULL COMMENT '九月',
  `october` decimal(10,2) DEFAULT NULL COMMENT '十月',
  `november` decimal(10,2) DEFAULT NULL COMMENT '十一月',
  `december` decimal(10,2) DEFAULT NULL COMMENT '十二月',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_inner_code` (`company_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='年用水指标';

-- ----------------------------
-- Table structure for t_water_meter
-- ----------------------------
DROP TABLE IF EXISTS `t_water_meter`;
CREATE TABLE `t_water_meter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `company_id` bigint(20) DEFAULT NULL COMMENT '单位id',
  `inner_code` varchar(50) DEFAULT NULL COMMENT '单位编号',
  `line_num` varchar(50) DEFAULT NULL COMMENT '路别',
  `meter_num` varchar(50) DEFAULT NULL COMMENT '水表表号',
  `waters_type` tinyint(4) DEFAULT NULL COMMENT '水源类型',
  `water_use_type` tinyint(4) DEFAULT NULL COMMENT '取水用途',
  `meter_attr` varchar(50) DEFAULT NULL COMMENT '水表属性',
  `charge_type` tinyint(4) DEFAULT NULL COMMENT '收费类型',
  `billing_cycle` varchar(50) DEFAULT NULL COMMENT '计费周期',
  `regist_date` datetime DEFAULT NULL COMMENT '注册日期',
  PRIMARY KEY (`id`),
  KEY `idx_inner_code` (`inner_code`) USING BTREE,
  KEY `idx_meter_num` (`meter_num`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='水表信息';

-- ----------------------------
-- Table structure for t_well
-- ----------------------------
DROP TABLE IF EXISTS `t_well`;
CREATE TABLE `t_well` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `well_num` varchar(50) DEFAULT NULL COMMENT '水井编号',
  `name` varchar(255) DEFAULT NULL COMMENT '水井名称',
  `company_id` bigint(20) DEFAULT NULL COMMENT '单位id',
  `inner_code` varchar(50) DEFAULT NULL COMMENT '单位编号',
  `township` varchar(100) DEFAULT NULL COMMENT '乡',
  `village` varchar(100) DEFAULT NULL COMMENT '村',
  `address` varchar(100) DEFAULT NULL COMMENT '水井地址',
  `start_date` datetime DEFAULT NULL COMMENT '成井时间（年）',
  `well_depth` decimal(10,2) DEFAULT NULL COMMENT '井深（米）',
  `ground_depth` decimal(10,2) DEFAULT NULL COMMENT '地下水埋深（米）',
  `oneself_well` tinyint(2) DEFAULT NULL COMMENT '是否为单位自备井',
  `inner_diameter` decimal(10,2) DEFAULT NULL COMMENT '井口井管内径（毫米）',
  `material` varchar(255) DEFAULT NULL COMMENT '井壁管材料',
  `application` varchar(255) DEFAULT NULL COMMENT '应用状况',
  `electromechanics` tinyint(2) DEFAULT NULL COMMENT '是否已配套机电设备',
  `calculate_water` tinyint(2) DEFAULT NULL COMMENT '是否已安装水量计量设施',
  `pump_model` varchar(50) DEFAULT NULL COMMENT '水泵型号',
  `calculate_type` tinyint(4) DEFAULT NULL COMMENT '水量计量设施类型',
  `above_scale` tinyint(2) DEFAULT NULL COMMENT '是否为规模以上地下水水源地的水井',
  `geomorphic_type` tinyint(4) DEFAULT NULL COMMENT '所在地貌类型区',
  `ground_type` tinyint(4) DEFAULT NULL COMMENT '所取用地下水的类型',
  `name_code` varchar(255) DEFAULT NULL COMMENT '所在水资源三级区名称及编码',
  `waters_type` tinyint(4) DEFAULT NULL COMMENT '水源类型',
  `use_efficiency` varchar(100) DEFAULT NULL COMMENT '主要取水用途及效益',
  `method` varchar(50) DEFAULT NULL COMMENT '取水量确定方法',
  `licence` tinyint(2) DEFAULT NULL COMMENT '是否已办理取水许可证',
  `licence_code` varchar(50) DEFAULT NULL COMMENT '取水许可证编号',
  `water_withdrawals` decimal(10,2) DEFAULT NULL COMMENT '年许可取水量（万立方米）',
  PRIMARY KEY (`id`),
  KEY `idx_inner_code` (`inner_code`) USING BTREE,
  KEY `idx_well_num` (`well_num`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='水井信息';
