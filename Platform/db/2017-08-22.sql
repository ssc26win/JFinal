/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : yc_platform

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2017-08-22 01:57:45
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
-- Records of access_token
-- ----------------------------

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
-- Records of app_version
-- ----------------------------

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
-- Records of dict_data
-- ----------------------------
INSERT INTO `dict_data` VALUES ('4', '诽谤辱骂', '1', '', '1', '1467514812', '3');
INSERT INTO `dict_data` VALUES ('5', '淫秽色情', '2', '', '2', '1467514834', '3');
INSERT INTO `dict_data` VALUES ('6', '垃圾广告', '3', '', '3', '1467514907', '3');
INSERT INTO `dict_data` VALUES ('7', '民营企业', '1', '民营企业', '1', '1503325387', '4');
INSERT INTO `dict_data` VALUES ('8', '事业单位', '2', '事业单位', '2', '1503325407', '4');
INSERT INTO `dict_data` VALUES ('9', '政府', '3', '政府', '3', '1503325424', '4');
INSERT INTO `dict_data` VALUES ('10', '社区', '4', '社区', '4', '1503325441', '4');
INSERT INTO `dict_data` VALUES ('11', '生活用水', '1', '生活用水', '1', '1503325485', '5');
INSERT INTO `dict_data` VALUES ('12', '生产用水', '2', '生产用水', '2', '1503325498', '5');
INSERT INTO `dict_data` VALUES ('13', '绿化用水', '3', '绿化用水', '3', '1503325510', '5');
INSERT INTO `dict_data` VALUES ('14', '节水型单位', '1', '节水型单位', '1', '1503325596', '6');
INSERT INTO `dict_data` VALUES ('15', '非节水型单位', '2', '非节水型单位', '2', '1503325609', '6');
INSERT INTO `dict_data` VALUES ('16', '市政自来水', '1', '市政自来水', '1', '1503325654', '7');
INSERT INTO `dict_data` VALUES ('17', '自备井', '2', '自备井', '2', '1503325670', '7');
INSERT INTO `dict_data` VALUES ('18', '预付费', '1', '预付费', '1', '1503325714', '8');
INSERT INTO `dict_data` VALUES ('19', '后付费', '2', '后付费', '2', '1503325726', '8');

-- ----------------------------
-- Table structure for dict_type
-- ----------------------------
DROP TABLE IF EXISTS `dict_type`;
CREATE TABLE `dict_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `remark` varchar(32) DEFAULT NULL,
  `update_time` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of dict_type
-- ----------------------------
INSERT INTO `dict_type` VALUES ('1', '兴趣爱好', '用户选择兴趣爱好', '0');
INSERT INTO `dict_type` VALUES ('3', '举报内容', '举报内容2', '1473167795');
INSERT INTO `dict_type` VALUES ('4', 'UserType', '用户类型', '1503325346');
INSERT INTO `dict_type` VALUES ('5', 'WaterUseType', '取水用途', '1503325468');
INSERT INTO `dict_type` VALUES ('6', 'UnitType', '节水型单位类型', '1503325574');
INSERT INTO `dict_type` VALUES ('7', 'WatersType', '水源类型', '1503325636');
INSERT INTO `dict_type` VALUES ('8', 'ChargeType', '收费类型', '1503325697');

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
) ENGINE=InnoDB AUTO_INCREMENT=38024 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
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
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8 COMMENT='后台系统登陆记录';

-- ----------------------------
-- Records of sys_login_record
-- ----------------------------
INSERT INTO `sys_login_record` VALUES ('16', '1', '2017-08-18 22:43:17', '0', '1');
INSERT INTO `sys_login_record` VALUES ('17', '1', '2017-08-18 23:01:18', '0', '1');
INSERT INTO `sys_login_record` VALUES ('18', '1', '2017-08-18 23:55:55', '0', '1');
INSERT INTO `sys_login_record` VALUES ('19', '1', '2017-08-19 00:03:38', '0', '1');
INSERT INTO `sys_login_record` VALUES ('20', '1', '2017-08-19 01:46:29', '0', '1');
INSERT INTO `sys_login_record` VALUES ('21', '1', '2017-08-20 17:26:48', '0', '1');
INSERT INTO `sys_login_record` VALUES ('22', '1', '2017-08-20 17:37:55', '0', '1');
INSERT INTO `sys_login_record` VALUES ('23', '1', '2017-08-20 17:57:17', '0', '1');
INSERT INTO `sys_login_record` VALUES ('24', '1', '2017-08-20 18:13:48', '0', '1');
INSERT INTO `sys_login_record` VALUES ('25', '1', '2017-08-20 18:22:35', '0', '1');
INSERT INTO `sys_login_record` VALUES ('26', '1', '2017-08-20 20:10:24', '0', '1');
INSERT INTO `sys_login_record` VALUES ('27', '1', '2017-08-20 20:35:08', '0', '1');
INSERT INTO `sys_login_record` VALUES ('28', '1', '2017-08-20 20:39:47', '0', '1');
INSERT INTO `sys_login_record` VALUES ('29', '1', '2017-08-20 21:02:53', '0', '1');
INSERT INTO `sys_login_record` VALUES ('30', '1', '2017-08-20 21:05:30', '0', '1');
INSERT INTO `sys_login_record` VALUES ('31', '1', '2017-08-20 21:17:46', '0', '1');
INSERT INTO `sys_login_record` VALUES ('32', '1', '2017-08-20 21:43:14', '0', '1');
INSERT INTO `sys_login_record` VALUES ('33', '1', '2017-08-20 21:50:54', '0', '1');
INSERT INTO `sys_login_record` VALUES ('34', '1', '2017-08-20 22:05:50', '0', '1');
INSERT INTO `sys_login_record` VALUES ('35', '1', '2017-08-20 23:41:32', '0', '1');
INSERT INTO `sys_login_record` VALUES ('36', '1', '2017-08-21 01:10:09', '0', '1');
INSERT INTO `sys_login_record` VALUES ('37', '1', '2017-08-21 01:21:36', '0', '1');
INSERT INTO `sys_login_record` VALUES ('38', '1', '2017-08-21 22:06:08', '0', '1');
INSERT INTO `sys_login_record` VALUES ('39', '1', '2017-08-21 22:10:39', '0', '1');
INSERT INTO `sys_login_record` VALUES ('40', '1', '2017-08-21 22:16:55', '0', '1');
INSERT INTO `sys_login_record` VALUES ('41', '1', '2017-08-21 22:33:35', '0', '1');
INSERT INTO `sys_login_record` VALUES ('42', '1', '2017-08-21 22:40:55', '0', '1');
INSERT INTO `sys_login_record` VALUES ('43', '1', '2017-08-21 23:05:42', '0', '1');
INSERT INTO `sys_login_record` VALUES ('44', '1', '2017-08-21 23:26:19', '0', '1');
INSERT INTO `sys_login_record` VALUES ('45', '1', '2017-08-21 23:43:39', '0', '1');
INSERT INTO `sys_login_record` VALUES ('46', '1', '2017-08-22 00:24:27', '0', '1');
INSERT INTO `sys_login_record` VALUES ('47', '1', '2017-08-22 01:55:43', '0', '1');

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
) ENGINE=InnoDB AUTO_INCREMENT=206 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of sys_res
-- ----------------------------
INSERT INTO `sys_res` VALUES ('1', null, '系统管理', '系统管理', '', 'fa-cogs', '10', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('2', '1', '资源管理', null, '/sys/res', 'fa-list', '1', '1', null, '1', '1');
INSERT INTO `sys_res` VALUES ('3', '1', '角色管理', null, '/sys/role', 'fa-list', '10', '1', null, '1', '1');
INSERT INTO `sys_res` VALUES ('4', '1', '用户管理', null, '/sys/user', 'fa-list', '11', '1', null, '1', '1');
INSERT INTO `sys_res` VALUES ('9', '4', '用户删除', null, '/sys/user/delete', 'fa-list', '1', '2', null, '1', '2');
INSERT INTO `sys_res` VALUES ('12', '4', '搜索用户', null, '/sys/user/serach', 'fa-list', '1', '2', null, '1', '2');
INSERT INTO `sys_res` VALUES ('18', '2', '资源删除', null, '/sys/res/delete', 'fa-list', '11', '2', null, '1', '0');
INSERT INTO `sys_res` VALUES ('19', '2', '资源保存', null, '/sys/res/save', 'fa-list', '11', '2', null, '1', '0');
INSERT INTO `sys_res` VALUES ('28', '3', '角色删除', null, '/sys/role/delete', 'fa-list', '11', '2', null, '1', '0');
INSERT INTO `sys_res` VALUES ('29', '3', '角色保存', null, '/sys/role/save', 'fa-list', '11', '2', null, '1', '0');
INSERT INTO `sys_res` VALUES ('63', '4', '冻结用户', null, '/sys/user/freeze', 'fa-list', '11', '2', null, '1', '0');
INSERT INTO `sys_res` VALUES ('146', '4', '用户列表', null, '/sys/user/list', 'fa-list', '8', '2', null, '1', '0');
INSERT INTO `sys_res` VALUES ('147', '4', '用户保存', null, '/sys/user/save', 'fa-list', '10', '2', null, '1', '0');
INSERT INTO `sys_res` VALUES ('150', '1', '操作日志', null, '/sys/log', 'fa-list', '11', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('152', null, '控制台', '1234', '/', 'fa-tachometer', '1', '1', '2015-02-10 16:09:40', '1', '0');
INSERT INTO `sys_res` VALUES ('181', '1', '数据字典', null, '/dict', 'fa-list', '12', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('182', '181', '数据字典列表', null, '/dict/list', 'fa-list', '1', '2', null, '1', '0');
INSERT INTO `sys_res` VALUES ('192', null, 'APP管理', null, '', 'fa-android', '9', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('193', '192', 'App版本管理', null, '/app', '', '1', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('194', null, '基础信息', null, '', 'fa-pencil-square-o', '2', '1', null, '1', '1');
INSERT INTO `sys_res` VALUES ('195', '194', '单位信息', null, '/basic/company', 'fa-list', '1', '1', null, '1', '2');
INSERT INTO `sys_res` VALUES ('196', '194', '水表信息', null, '/basic/meter', 'fa-list', '2', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('197', '194', '水井信息', null, '/basic/well', 'fa-list', '3', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('198', '194', '用水指标', null, '/basic/waterindex', 'fa-list', '4', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('199', null, '实时数据', null, '/statis/actual', 'fa-desktop', '1', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('200', null, '统计查询', null, '', 'fa-list-alt', '4', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('201', '200', '读数查询', null, '/statis/readnum', 'fa-list', '2', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('202', '200', '日用水量', null, '/statis/daily', 'fa-list', '3', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('203', '200', '月用水量', null, '/statis/month', 'fa-list', '4', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('204', '200', '年用水量', null, '/statis/year', 'fa-list', '5', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('205', '1', '发布广告', null, '/basic/ad', 'fa-list', '6', '1', null, '1', '0');

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
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', 'admin', '超级管理员', '1', '2015-05-05 14:24:26', '1');
INSERT INTO `sys_role` VALUES ('56', '内容编辑', '内容编辑4', '1', null, '1');
INSERT INTO `sys_role` VALUES ('57', '客服人员', '客服人员', '1', null, '1');
INSERT INTO `sys_role` VALUES ('60', 'dd', 'ss ', '1', null, '1');

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
) ENGINE=InnoDB AUTO_INCREMENT=4187 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of sys_role_res
-- ----------------------------
INSERT INTO `sys_role_res` VALUES ('4141', '1', '1');
INSERT INTO `sys_role_res` VALUES ('4142', '2', '1');
INSERT INTO `sys_role_res` VALUES ('4143', '18', '1');
INSERT INTO `sys_role_res` VALUES ('4144', '19', '1');
INSERT INTO `sys_role_res` VALUES ('4145', '3', '1');
INSERT INTO `sys_role_res` VALUES ('4146', '28', '1');
INSERT INTO `sys_role_res` VALUES ('4147', '29', '1');
INSERT INTO `sys_role_res` VALUES ('4148', '4', '1');
INSERT INTO `sys_role_res` VALUES ('4149', '9', '1');
INSERT INTO `sys_role_res` VALUES ('4150', '12', '1');
INSERT INTO `sys_role_res` VALUES ('4151', '63', '1');
INSERT INTO `sys_role_res` VALUES ('4152', '146', '1');
INSERT INTO `sys_role_res` VALUES ('4153', '147', '1');
INSERT INTO `sys_role_res` VALUES ('4154', '150', '1');
INSERT INTO `sys_role_res` VALUES ('4155', '181', '1');
INSERT INTO `sys_role_res` VALUES ('4156', '182', '1');
INSERT INTO `sys_role_res` VALUES ('4157', '152', '1');
INSERT INTO `sys_role_res` VALUES ('4162', '192', '1');
INSERT INTO `sys_role_res` VALUES ('4163', '193', '1');
INSERT INTO `sys_role_res` VALUES ('4168', '1', '56');
INSERT INTO `sys_role_res` VALUES ('4169', '2', '56');
INSERT INTO `sys_role_res` VALUES ('4170', '18', '56');
INSERT INTO `sys_role_res` VALUES ('4171', '19', '56');
INSERT INTO `sys_role_res` VALUES ('4172', '3', '56');
INSERT INTO `sys_role_res` VALUES ('4173', '28', '56');
INSERT INTO `sys_role_res` VALUES ('4174', '29', '56');
INSERT INTO `sys_role_res` VALUES ('4175', '194', '1');
INSERT INTO `sys_role_res` VALUES ('4176', '195', '1');
INSERT INTO `sys_role_res` VALUES ('4177', '196', '1');
INSERT INTO `sys_role_res` VALUES ('4178', '197', '1');
INSERT INTO `sys_role_res` VALUES ('4179', '198', '1');
INSERT INTO `sys_role_res` VALUES ('4180', '199', '1');
INSERT INTO `sys_role_res` VALUES ('4181', '200', '1');
INSERT INTO `sys_role_res` VALUES ('4182', '201', '1');
INSERT INTO `sys_role_res` VALUES ('4183', '202', '1');
INSERT INTO `sys_role_res` VALUES ('4184', '203', '1');
INSERT INTO `sys_role_res` VALUES ('4185', '204', '1');
INSERT INTO `sys_role_res` VALUES ('4186', '205', '1');

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
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '6fc3b9ada8c1e50b400cd6998ff7be76ea3ae312', '1289', '1', '/sys/static/i/9.jpg', 'admin@admin.com', '2015-07-15 18:00:27', '1234567', 'UzNk7MKbVABao0I275SAs9ShzyYtLQooG42fkoDcL9QtkuFP1Xm7QOtIMrbvxbLc');
INSERT INTO `sys_user` VALUES ('32', 'eason', '5d46f91596dc2f24c1535148f778af0a25612289', 'sfsf', '1', '/images/guest.jpg', '569165857@qq.com', '2016-06-12 23:54:24', '13332892938', null);
INSERT INTO `sys_user` VALUES ('34', 'test', '6fc3b9ada8c1e50b400cd6998ff7be76ea3ae312', 'dfsdfsdfddf ', '1', '/images/guest.jpg', '569165857@qq.com', '2016-07-02 22:24:50', '13332892938', 'hDEflmnlVituFC6hbfTzeze8RVSRseIfdgBAGkAU2RRh4c4kIrIYXTTuwEG2Caem');

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
) ENGINE=InnoDB AUTO_INCREMENT=337 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('315', '1', '1');
INSERT INTO `sys_user_role` VALUES ('333', '32', '1');
INSERT INTO `sys_user_role` VALUES ('334', '32', '56');
INSERT INTO `sys_user_role` VALUES ('335', '32', '57');
INSERT INTO `sys_user_role` VALUES ('336', '32', '60');

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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='实时数据';

-- ----------------------------
-- Records of t_actual_data
-- ----------------------------

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
-- Records of t_ad
-- ----------------------------

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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='单位表';

-- ----------------------------
-- Records of t_company
-- ----------------------------
INSERT INTO `t_company` VALUES ('1', '001', '北京某某科技股份有限公司', '海淀区西二旗辉煌国际65号', '11', '111', '11', '11', '18622222222', '1', '1', '1', '1', '1', '1', '2017-08-20 21:07:16');
INSERT INTO `t_company` VALUES ('2', '111111', '11111', '1', '1', '11', '1', '1', '1', '1', '11', '1', '111', '1', '1', '2017-08-22 00:06:18');

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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='年用水指标';

-- ----------------------------
-- Records of t_water_index
-- ----------------------------
INSERT INTO `t_water_index` VALUES ('1', null, '111', '1', '1.00', '1.00', '1.08', '1.00', '1.00', '1.00', '1.00', '1.00', '1.00', '1.00', '1.00', '1.00', '1.00');

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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='水表信息';

-- ----------------------------
-- Records of t_water_meter
-- ----------------------------
INSERT INTO `t_water_meter` VALUES ('1', null, '11', '1111111', '1', '1', null, '1', '1', '1', null);

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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='水井信息';

-- ----------------------------
-- Records of t_well
-- ----------------------------
