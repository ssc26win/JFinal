/*
Navicat MySQL Data Transfer

Source Server         : local-mysql
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : yc_platform

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2017-09-07 18:48:55
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
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of dict_data
-- ----------------------------
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
INSERT INTO `dict_data` VALUES ('20', '永顺镇', '1', '永顺镇', '1', '1504751893', '9');
INSERT INTO `dict_data` VALUES ('21', '梨园镇', '2', '梨园镇', '2', '1504751911', '9');
INSERT INTO `dict_data` VALUES ('22', '宋庄镇', '3', '宋庄镇', '3', '1504751924', '9');
INSERT INTO `dict_data` VALUES ('23', '漷县镇', '4', '漷县镇', '4', '1504751941', '9');
INSERT INTO `dict_data` VALUES ('24', '张家湾镇', '5', '张家湾镇', '5', '1504751984', '9');
INSERT INTO `dict_data` VALUES ('25', '马驹桥镇', '6', '马驹桥镇', '6', '1504752012', '9');
INSERT INTO `dict_data` VALUES ('26', '西集镇', '7', '西集镇', '7', '1504752032', '9');
INSERT INTO `dict_data` VALUES ('27', '永乐店镇', '8', '永乐店镇', '8', '1504752045', '9');
INSERT INTO `dict_data` VALUES ('28', '潞城镇', '9', '潞城镇', '9', '1504752056', '9');
INSERT INTO `dict_data` VALUES ('29', '台湖镇', '10', '台湖镇', '10', '1504752100', '9');
INSERT INTO `dict_data` VALUES ('30', '于家务乡', '11', '于家务乡', '11', '1504752113', '9');
INSERT INTO `dict_data` VALUES ('31', '中仓街道', '12', '中仓街道', '12', '1504752126', '9');
INSERT INTO `dict_data` VALUES ('32', '北苑街道', '13', '北苑街道', '13', '1504752137', '9');
INSERT INTO `dict_data` VALUES ('33', '玉桥街道', '14', '玉桥街道', '14', '1504752150', '9');
INSERT INTO `dict_data` VALUES ('34', '新华街道', '15', '新华街道', '15', '1504752170', '9');
INSERT INTO `dict_data` VALUES ('35', '水泵型号1', '1', '水泵型号1', '1', '1504752400', '10');
INSERT INTO `dict_data` VALUES ('36', '水泵型号2', '2', '水泵型号2', '2', '1504752459', '10');
INSERT INTO `dict_data` VALUES ('37', '水量计量设施类型1', '1', '水量计量设施类型1', '1', '1504752552', '11');
INSERT INTO `dict_data` VALUES ('38', '水量计量设施类型2', '2', '水量计量设施类型2', '2', '1504752565', '11');
INSERT INTO `dict_data` VALUES ('39', '平原', '1', '平原', '1', '1504752646', '12');
INSERT INTO `dict_data` VALUES ('40', '山地', '2', '山地', '2', '1504752661', '12');
INSERT INTO `dict_data` VALUES ('41', '丘陵', '3', '丘陵', '3', '1504752680', '12');
INSERT INTO `dict_data` VALUES ('42', '纯净水', '1', '纯净水', '1', '1504752805', '13');
INSERT INTO `dict_data` VALUES ('43', '山泉水', '2', '山泉水', '2', '1504752821', '13');

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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of dict_type
-- ----------------------------
INSERT INTO `dict_type` VALUES ('4', 'UserType', '用户类型', '1503325346');
INSERT INTO `dict_type` VALUES ('5', 'WaterUseType', '取水用途', '1503325468');
INSERT INTO `dict_type` VALUES ('6', 'UnitType', '节水型单位类型', '1503325574');
INSERT INTO `dict_type` VALUES ('7', 'WatersType', '水源类型', '1503325636');
INSERT INTO `dict_type` VALUES ('8', 'ChargeType', '收费类型', '1503325697');
INSERT INTO `dict_type` VALUES ('9', 'Street', '所属乡镇或街道', '1504751781');
INSERT INTO `dict_type` VALUES ('10', 'PumpModel', '水泵型号', '1504752358');
INSERT INTO `dict_type` VALUES ('11', 'CalculateType', '水量计量设施类型', '1504752510');
INSERT INTO `dict_type` VALUES ('12', 'GeomorphicType', '所在地貌类型区', '1504752620');
INSERT INTO `dict_type` VALUES ('13', 'GroundType', '所取用地下水的类型', '1504752757');

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
) ENGINE=InnoDB AUTO_INCREMENT=45938 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES ('45889', '1', 'http://localhost/', '127.0.0.1', '/chart/baiduMap', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'baiduMap', '2017-09-07 18:42:28', '1881', null);
INSERT INTO `sys_log` VALUES ('45890', '1', 'http://localhost/home', '127.0.0.1', '/chart', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'index', '2017-09-07 18:42:30', '7', null);
INSERT INTO `sys_log` VALUES ('45891', '1', 'http://localhost/home', '127.0.0.1', '/chart/getMonth', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'getMonth', '2017-09-07 18:42:30', '5', null);
INSERT INTO `sys_log` VALUES ('45892', '1', 'http://localhost/home', '127.0.0.1', '/chart/getDilay', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'getDilay', '2017-09-07 18:42:30', '9', null);
INSERT INTO `sys_log` VALUES ('45893', '1', 'http://localhost/home', '127.0.0.1', '/chart/company', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'company', '2017-09-07 18:42:30', '26', null);
INSERT INTO `sys_log` VALUES ('45894', '1', 'http://localhost/', '127.0.0.1', '/chart/baiduMap', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'baiduMap', '2017-09-07 18:42:50', '2716', null);
INSERT INTO `sys_log` VALUES ('45895', '1', 'http://localhost/home', '127.0.0.1', '/chart', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'index', '2017-09-07 18:42:53', '6', null);
INSERT INTO `sys_log` VALUES ('45896', '1', 'http://localhost/home', '127.0.0.1', '/chart/getDilay', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'getDilay', '2017-09-07 18:42:53', '9', null);
INSERT INTO `sys_log` VALUES ('45897', '1', 'http://localhost/home', '127.0.0.1', '/chart/getMonth', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'getMonth', '2017-09-07 18:42:53', '11', null);
INSERT INTO `sys_log` VALUES ('45898', '1', 'http://localhost/home', '127.0.0.1', '/chart/company', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'company', '2017-09-07 18:42:53', '26', null);
INSERT INTO `sys_log` VALUES ('45899', '1', 'http://localhost/', '127.0.0.1', '/chart/baiduMap', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'baiduMap', '2017-09-07 18:43:16', '2', null);
INSERT INTO `sys_log` VALUES ('45900', '1', 'http://localhost/home', '127.0.0.1', '/chart/getDilay', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'getDilay', '2017-09-07 18:43:16', '3', null);
INSERT INTO `sys_log` VALUES ('45901', '1', 'http://localhost/home', '127.0.0.1', '/chart/getMonth', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'getMonth', '2017-09-07 18:43:16', '2', null);
INSERT INTO `sys_log` VALUES ('45902', '1', 'http://localhost/home', '127.0.0.1', '/chart', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'index', '2017-09-07 18:43:16', '6', null);
INSERT INTO `sys_log` VALUES ('45903', '1', 'http://localhost/home', '127.0.0.1', '/chart/company', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'company', '2017-09-07 18:43:16', '20', null);
INSERT INTO `sys_log` VALUES ('45904', '1', 'http://localhost/', '127.0.0.1', '/chart/baiduMap', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'baiduMap', '2017-09-07 18:43:49', '3', null);
INSERT INTO `sys_log` VALUES ('45905', '1', 'http://localhost/home', '127.0.0.1', '/chart/getDilay', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'getDilay', '2017-09-07 18:43:50', '5', null);
INSERT INTO `sys_log` VALUES ('45906', '1', 'http://localhost/home', '127.0.0.1', '/chart/getMonth', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'getMonth', '2017-09-07 18:43:50', '4', null);
INSERT INTO `sys_log` VALUES ('45907', '1', 'http://localhost/home', '127.0.0.1', '/chart', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'index', '2017-09-07 18:43:50', '8', null);
INSERT INTO `sys_log` VALUES ('45908', '1', 'http://localhost/home', '127.0.0.1', '/chart/company', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'company', '2017-09-07 18:43:50', '22', null);
INSERT INTO `sys_log` VALUES ('45909', '1', 'http://localhost/', '127.0.0.1', '/chart/baiduMap', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'baiduMap', '2017-09-07 18:45:21', '4', null);
INSERT INTO `sys_log` VALUES ('45910', '1', 'http://localhost/home', '127.0.0.1', '/chart', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'index', '2017-09-07 18:45:21', '9', null);
INSERT INTO `sys_log` VALUES ('45911', '1', 'http://localhost/home', '127.0.0.1', '/chart/getDilay', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'getDilay', '2017-09-07 18:45:21', '4', null);
INSERT INTO `sys_log` VALUES ('45912', '1', 'http://localhost/home', '127.0.0.1', '/chart/getMonth', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'getMonth', '2017-09-07 18:45:21', '2', null);
INSERT INTO `sys_log` VALUES ('45913', '1', 'http://localhost/home', '127.0.0.1', '/chart/company', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'company', '2017-09-07 18:45:21', '20', null);
INSERT INTO `sys_log` VALUES ('45914', '1', 'http://localhost/', '127.0.0.1', '/chart/baiduMap', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'baiduMap', '2017-09-07 18:45:44', '2', null);
INSERT INTO `sys_log` VALUES ('45915', '1', 'http://localhost/home', '127.0.0.1', '/chart/getMonth', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'getMonth', '2017-09-07 18:45:45', '3', null);
INSERT INTO `sys_log` VALUES ('45916', '1', 'http://localhost/home', '127.0.0.1', '/chart', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'index', '2017-09-07 18:45:45', '6', null);
INSERT INTO `sys_log` VALUES ('45917', '1', 'http://localhost/home', '127.0.0.1', '/chart/getDilay', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'getDilay', '2017-09-07 18:45:45', '6', null);
INSERT INTO `sys_log` VALUES ('45918', '1', 'http://localhost/home', '127.0.0.1', '/chart/company', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'company', '2017-09-07 18:45:45', '20', null);
INSERT INTO `sys_log` VALUES ('45919', '1', 'http://localhost/home', '127.0.0.1', '/chart', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'index', '2017-09-07 18:46:15', '5', null);
INSERT INTO `sys_log` VALUES ('45920', '1', 'http://localhost/home', '127.0.0.1', '/chart/company', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'company', '2017-09-07 18:46:15', '16', null);
INSERT INTO `sys_log` VALUES ('45921', '1', 'http://localhost/home', '127.0.0.1', '/chart/getDilay', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'getDilay', '2017-09-07 18:46:15', '2', null);
INSERT INTO `sys_log` VALUES ('45922', '1', 'http://localhost/home', '127.0.0.1', '/chart/getMonth', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'getMonth', '2017-09-07 18:46:15', '2', null);
INSERT INTO `sys_log` VALUES ('45923', '1', 'http://localhost/', '127.0.0.1', '/chart/baiduMap', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'baiduMap', '2017-09-07 18:46:20', '2', null);
INSERT INTO `sys_log` VALUES ('45924', '1', 'http://localhost/home', '127.0.0.1', '/chart', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'index', '2017-09-07 18:46:20', '4', null);
INSERT INTO `sys_log` VALUES ('45925', '1', 'http://localhost/home', '127.0.0.1', '/chart/getMonth', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'getMonth', '2017-09-07 18:46:20', '3', null);
INSERT INTO `sys_log` VALUES ('45926', '1', 'http://localhost/home', '127.0.0.1', '/chart/getDilay', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'getDilay', '2017-09-07 18:46:20', '3', null);
INSERT INTO `sys_log` VALUES ('45927', '1', 'http://localhost/home', '127.0.0.1', '/chart/company', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'company', '2017-09-07 18:46:20', '20', null);
INSERT INTO `sys_log` VALUES ('45928', '1', 'http://localhost/login', '127.0.0.1', '/image/getCode', null, '', '0', 'com.shangsc.platform.controller.ImageController', 'getCode', '2017-09-07 18:46:25', '136', null);
INSERT INTO `sys_log` VALUES ('45929', '1', 'http://localhost/home', '127.0.0.1', '/chart/getDilay', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'getDilay', '2017-09-07 18:46:43', '2', null);
INSERT INTO `sys_log` VALUES ('45930', '1', 'http://localhost/home', '127.0.0.1', '/chart', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'index', '2017-09-07 18:46:43', '7', null);
INSERT INTO `sys_log` VALUES ('45931', '1', 'http://localhost/home', '127.0.0.1', '/chart/getMonth', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'getMonth', '2017-09-07 18:46:43', '3', null);
INSERT INTO `sys_log` VALUES ('45932', '1', 'http://localhost/home', '127.0.0.1', '/chart/company', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'company', '2017-09-07 18:46:43', '44', null);
INSERT INTO `sys_log` VALUES ('45933', '1', 'http://localhost/', '127.0.0.1', '/chart/baiduMap', null, '', '0', 'com.shangsc.platform.controller.ChartController', 'baiduMap', '2017-09-07 18:46:45', '6', null);
INSERT INTO `sys_log` VALUES ('45934', '1', 'http://localhost/', '127.0.0.1', '/basic/company', null, '', '0', 'com.shangsc.platform.controller.basic.CompanyController', 'index', '2017-09-07 18:47:29', '0', null);
INSERT INTO `sys_log` VALUES ('45935', '1', 'http://localhost/basic/company', '127.0.0.1', '/basic/company/getListData', null, '', '0', 'com.shangsc.platform.controller.basic.CompanyController', 'getListData', '2017-09-07 18:47:29', '15', null);
INSERT INTO `sys_log` VALUES ('45936', '1', 'http://localhost/', '127.0.0.1', '/basic/well', null, '', '0', 'com.shangsc.platform.controller.basic.WellController', 'index', '2017-09-07 18:47:30', '1', null);
INSERT INTO `sys_log` VALUES ('45937', '1', 'http://localhost/basic/well', '127.0.0.1', '/basic/well/getListData', null, '', '0', 'com.shangsc.platform.controller.basic.WellController', 'getListData', '2017-09-07 18:47:30', '21', null);

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
) ENGINE=InnoDB AUTO_INCREMENT=148 DEFAULT CHARSET=utf8 COMMENT='后台系统登陆记录';

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
INSERT INTO `sys_login_record` VALUES ('48', '1', '2017-08-24 10:30:16', '0', '1');
INSERT INTO `sys_login_record` VALUES ('49', '1', '2017-08-24 11:38:07', '0', '1');
INSERT INTO `sys_login_record` VALUES ('50', '1', '2017-08-24 11:53:38', '0', '1');
INSERT INTO `sys_login_record` VALUES ('51', '1', '2017-08-24 13:26:14', '0', '1');
INSERT INTO `sys_login_record` VALUES ('52', '1', '2017-08-24 15:53:13', '0', '1');
INSERT INTO `sys_login_record` VALUES ('53', '1', '2017-08-25 09:59:37', '0', '1');
INSERT INTO `sys_login_record` VALUES ('54', '1', '2017-08-25 10:25:27', '0', '1');
INSERT INTO `sys_login_record` VALUES ('55', '1', '2017-08-25 11:00:54', '0', '1');
INSERT INTO `sys_login_record` VALUES ('56', '1', '2017-08-25 11:59:51', '0', '1');
INSERT INTO `sys_login_record` VALUES ('57', '1', '2017-08-25 12:31:54', '1', '0');
INSERT INTO `sys_login_record` VALUES ('58', '1', '2017-08-25 12:31:58', '2', '0');
INSERT INTO `sys_login_record` VALUES ('59', '1', '2017-08-25 12:32:06', '3', '0');
INSERT INTO `sys_login_record` VALUES ('60', '1', '2017-08-25 12:32:25', '4', '0');
INSERT INTO `sys_login_record` VALUES ('61', '1', '2017-08-25 12:32:26', '5', '0');
INSERT INTO `sys_login_record` VALUES ('62', '1', '2017-08-25 12:41:29', '6', '0');
INSERT INTO `sys_login_record` VALUES ('63', '1', '2017-08-25 12:41:35', '0', '1');
INSERT INTO `sys_login_record` VALUES ('64', '32', '2017-08-27 00:25:25', '0', '1');
INSERT INTO `sys_login_record` VALUES ('65', '1', '2017-08-27 00:25:53', '0', '1');
INSERT INTO `sys_login_record` VALUES ('66', '34', '2017-08-27 00:26:55', '0', '1');
INSERT INTO `sys_login_record` VALUES ('67', '34', '2017-08-27 00:28:51', '0', '1');
INSERT INTO `sys_login_record` VALUES ('68', '1', '2017-08-27 01:10:01', '0', '1');
INSERT INTO `sys_login_record` VALUES ('69', '1', '2017-08-28 15:46:29', '0', '1');
INSERT INTO `sys_login_record` VALUES ('70', '1', '2017-09-02 11:26:06', '0', '1');
INSERT INTO `sys_login_record` VALUES ('71', '1', '2017-09-02 12:07:25', '0', '1');
INSERT INTO `sys_login_record` VALUES ('72', '1', '2017-09-02 12:50:41', '0', '1');
INSERT INTO `sys_login_record` VALUES ('73', '1', '2017-09-02 13:22:07', '0', '1');
INSERT INTO `sys_login_record` VALUES ('74', '1', '2017-09-02 13:32:50', '1', '0');
INSERT INTO `sys_login_record` VALUES ('75', '1', '2017-09-02 13:32:55', '0', '1');
INSERT INTO `sys_login_record` VALUES ('76', '1', '2017-09-02 14:13:51', '0', '1');
INSERT INTO `sys_login_record` VALUES ('77', '1', '2017-09-02 16:47:06', '0', '1');
INSERT INTO `sys_login_record` VALUES ('78', '1', '2017-09-02 17:02:32', '0', '1');
INSERT INTO `sys_login_record` VALUES ('79', '1', '2017-09-02 17:32:53', '0', '1');
INSERT INTO `sys_login_record` VALUES ('80', '1', '2017-09-02 18:35:46', '0', '1');
INSERT INTO `sys_login_record` VALUES ('81', '1', '2017-09-02 18:37:19', '1', '0');
INSERT INTO `sys_login_record` VALUES ('82', '1', '2017-09-02 18:37:24', '0', '1');
INSERT INTO `sys_login_record` VALUES ('83', '1', '2017-09-03 11:06:54', '0', '1');
INSERT INTO `sys_login_record` VALUES ('84', '1', '2017-09-03 11:23:50', '1', '0');
INSERT INTO `sys_login_record` VALUES ('85', '1', '2017-09-03 11:23:54', '0', '1');
INSERT INTO `sys_login_record` VALUES ('86', '1', '2017-09-03 12:02:18', '0', '1');
INSERT INTO `sys_login_record` VALUES ('87', '1', '2017-09-03 12:37:02', '0', '1');
INSERT INTO `sys_login_record` VALUES ('88', '1', '2017-09-03 13:27:20', '0', '1');
INSERT INTO `sys_login_record` VALUES ('89', '1', '2017-09-03 14:43:49', '0', '1');
INSERT INTO `sys_login_record` VALUES ('90', '1', '2017-09-03 17:07:23', '0', '1');
INSERT INTO `sys_login_record` VALUES ('91', '1', '2017-09-03 17:08:33', '0', '1');
INSERT INTO `sys_login_record` VALUES ('92', '1', '2017-09-03 17:09:49', '0', '1');
INSERT INTO `sys_login_record` VALUES ('93', '1', '2017-09-03 18:37:08', '0', '1');
INSERT INTO `sys_login_record` VALUES ('94', '1', '2017-09-03 18:49:35', '1', '0');
INSERT INTO `sys_login_record` VALUES ('95', '1', '2017-09-03 18:49:40', '0', '1');
INSERT INTO `sys_login_record` VALUES ('96', '1', '2017-09-03 19:12:29', '0', '1');
INSERT INTO `sys_login_record` VALUES ('97', '1', '2017-09-04 13:59:41', '0', '1');
INSERT INTO `sys_login_record` VALUES ('98', '1', '2017-09-04 14:17:57', '0', '1');
INSERT INTO `sys_login_record` VALUES ('99', '1', '2017-09-04 17:39:28', '0', '1');
INSERT INTO `sys_login_record` VALUES ('100', '36', '2017-09-04 18:57:49', '0', '1');
INSERT INTO `sys_login_record` VALUES ('101', '1', '2017-09-04 20:45:11', '0', '1');
INSERT INTO `sys_login_record` VALUES ('102', '36', '2017-09-04 22:37:21', '1', '0');
INSERT INTO `sys_login_record` VALUES ('103', '36', '2017-09-04 22:37:25', '0', '1');
INSERT INTO `sys_login_record` VALUES ('104', '36', '2017-09-04 22:42:23', '0', '1');
INSERT INTO `sys_login_record` VALUES ('105', '1', '2017-09-04 22:44:58', '0', '1');
INSERT INTO `sys_login_record` VALUES ('106', '1', '2017-09-04 22:50:32', '0', '1');
INSERT INTO `sys_login_record` VALUES ('107', '1', '2017-09-04 22:58:50', '0', '1');
INSERT INTO `sys_login_record` VALUES ('108', '1', '2017-09-04 22:59:51', '0', '1');
INSERT INTO `sys_login_record` VALUES ('109', '1', '2017-09-04 23:08:18', '0', '1');
INSERT INTO `sys_login_record` VALUES ('110', '1', '2017-09-04 23:54:52', '0', '1');
INSERT INTO `sys_login_record` VALUES ('111', '1', '2017-09-05 00:58:57', '0', '1');
INSERT INTO `sys_login_record` VALUES ('112', '1', '2017-09-05 02:11:17', '0', '1');
INSERT INTO `sys_login_record` VALUES ('113', '1', '2017-09-05 02:18:48', '0', '1');
INSERT INTO `sys_login_record` VALUES ('114', '1', '2017-09-05 11:57:37', '1', '0');
INSERT INTO `sys_login_record` VALUES ('115', '1', '2017-09-05 11:57:44', '2', '0');
INSERT INTO `sys_login_record` VALUES ('116', '1', '2017-09-05 11:57:51', '3', '0');
INSERT INTO `sys_login_record` VALUES ('117', '1', '2017-09-05 11:57:57', '0', '1');
INSERT INTO `sys_login_record` VALUES ('118', '1', '2017-09-05 12:18:04', '0', '1');
INSERT INTO `sys_login_record` VALUES ('119', '1', '2017-09-05 15:06:58', '1', '0');
INSERT INTO `sys_login_record` VALUES ('120', '1', '2017-09-05 15:07:06', '0', '1');
INSERT INTO `sys_login_record` VALUES ('121', '1', '2017-09-05 16:15:45', '1', '0');
INSERT INTO `sys_login_record` VALUES ('122', '1', '2017-09-05 16:15:51', '0', '1');
INSERT INTO `sys_login_record` VALUES ('123', '1', '2017-09-05 17:41:50', '1', '0');
INSERT INTO `sys_login_record` VALUES ('124', '1', '2017-09-05 17:41:57', '0', '1');
INSERT INTO `sys_login_record` VALUES ('125', '1', '2017-09-05 18:44:09', '1', '0');
INSERT INTO `sys_login_record` VALUES ('126', '1', '2017-09-05 18:44:15', '0', '1');
INSERT INTO `sys_login_record` VALUES ('127', '1', '2017-09-06 10:49:54', '0', '1');
INSERT INTO `sys_login_record` VALUES ('128', '1', '2017-09-06 11:43:47', '0', '1');
INSERT INTO `sys_login_record` VALUES ('129', '1', '2017-09-06 14:20:30', '0', '1');
INSERT INTO `sys_login_record` VALUES ('130', '1', '2017-09-06 15:20:27', '0', '1');
INSERT INTO `sys_login_record` VALUES ('131', '1', '2017-09-06 16:14:46', '0', '1');
INSERT INTO `sys_login_record` VALUES ('132', '1', '2017-09-06 16:37:56', '1', '0');
INSERT INTO `sys_login_record` VALUES ('133', '1', '2017-09-06 16:38:02', '0', '1');
INSERT INTO `sys_login_record` VALUES ('134', '1', '2017-09-06 17:17:30', '0', '1');
INSERT INTO `sys_login_record` VALUES ('135', '1', '2017-09-06 17:38:24', '0', '1');
INSERT INTO `sys_login_record` VALUES ('136', '1', '2017-09-07 10:09:59', '1', '0');
INSERT INTO `sys_login_record` VALUES ('137', '1', '2017-09-07 10:10:16', '0', '1');
INSERT INTO `sys_login_record` VALUES ('138', '1', '2017-09-07 12:15:50', '1', '0');
INSERT INTO `sys_login_record` VALUES ('139', '1', '2017-09-07 12:16:05', '0', '1');
INSERT INTO `sys_login_record` VALUES ('140', '1', '2017-09-07 12:19:41', '1', '0');
INSERT INTO `sys_login_record` VALUES ('141', '1', '2017-09-07 12:19:52', '0', '1');
INSERT INTO `sys_login_record` VALUES ('142', '1', '2017-09-07 12:41:09', '0', '1');
INSERT INTO `sys_login_record` VALUES ('143', '1', '2017-09-07 16:44:25', '1', '0');
INSERT INTO `sys_login_record` VALUES ('144', '1', '2017-09-07 16:44:39', '0', '1');
INSERT INTO `sys_login_record` VALUES ('145', '37', '2017-09-07 16:47:55', '0', '1');
INSERT INTO `sys_login_record` VALUES ('146', '37', '2017-09-07 16:56:52', '0', '1');
INSERT INTO `sys_login_record` VALUES ('147', '1', '2017-09-07 18:46:43', '0', '1');

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
-- Records of sys_res
-- ----------------------------
INSERT INTO `sys_res` VALUES ('1', null, '系统管理', '系统管理', '', 'fa-cogs', '10', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('2', '1', '资源管理', null, '/sys/res', 'fa-list', '5', '1', null, '1', '1');
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
INSERT INTO `sys_res` VALUES ('194', null, '基础信息', null, '', 'fa-pencil-square-o', '4', '1', null, '1', '1');
INSERT INTO `sys_res` VALUES ('195', '194', '单位信息', null, '/basic/company', 'fa-list', '1', '1', null, '1', '2');
INSERT INTO `sys_res` VALUES ('196', '194', '水表信息', null, '/basic/meter', 'fa-list', '2', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('197', '194', '水井信息', null, '/basic/well', 'fa-list', '3', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('198', '194', '用水指标', null, '/basic/waterindex', 'fa-list', '4', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('199', null, '实时数据', null, '/statis/actual', 'fa-desktop', '3', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('200', null, '统计查询', null, '', 'fa-list-alt', '5', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('201', '200', '读数查询', null, '/statis/readnum', 'fa-list', '2', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('202', '200', '日用水量', null, '/statis/daily', 'fa-list', '3', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('203', '200', '月用水量', null, '/statis/month', 'fa-list', '4', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('204', '200', '年用水量', null, '/statis/year', 'fa-list', '5', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('205', '1', '发布广告', null, '/basic/ad', 'fa-list', '6', '1', null, '1', '0');
INSERT INTO `sys_res` VALUES ('206', null, '导航地图', null, '/chart/baiduMap', 'fa-map-marker', '2', '1', null, '1', '0');

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
INSERT INTO `sys_role` VALUES ('1', '超级管理员', '超级管理员', '1', '2015-05-05 14:24:26', '1');
INSERT INTO `sys_role` VALUES ('56', '管理员', '可增、改、删、查单位信息、水表信息、用水指信息等', '1', null, '1');
INSERT INTO `sys_role` VALUES ('57', '操作员', '可读水表数据，查询用水量', '1', null, '1');
INSERT INTO `sys_role` VALUES ('60', '普通用户', '普通用户', '1', null, '1');

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
INSERT INTO `sys_role_res` VALUES ('4211', '152', '56');
INSERT INTO `sys_role_res` VALUES ('4212', '194', '56');
INSERT INTO `sys_role_res` VALUES ('4213', '195', '56');
INSERT INTO `sys_role_res` VALUES ('4214', '196', '56');
INSERT INTO `sys_role_res` VALUES ('4215', '197', '56');
INSERT INTO `sys_role_res` VALUES ('4216', '198', '56');
INSERT INTO `sys_role_res` VALUES ('4217', '199', '56');
INSERT INTO `sys_role_res` VALUES ('4218', '200', '56');
INSERT INTO `sys_role_res` VALUES ('4219', '201', '56');
INSERT INTO `sys_role_res` VALUES ('4220', '202', '56');
INSERT INTO `sys_role_res` VALUES ('4221', '203', '56');
INSERT INTO `sys_role_res` VALUES ('4222', '204', '56');
INSERT INTO `sys_role_res` VALUES ('4223', '152', '60');
INSERT INTO `sys_role_res` VALUES ('4224', '199', '60');
INSERT INTO `sys_role_res` VALUES ('4225', '200', '60');
INSERT INTO `sys_role_res` VALUES ('4226', '201', '60');
INSERT INTO `sys_role_res` VALUES ('4227', '202', '60');
INSERT INTO `sys_role_res` VALUES ('4228', '203', '60');
INSERT INTO `sys_role_res` VALUES ('4229', '204', '60');
INSERT INTO `sys_role_res` VALUES ('4230', '206', '1');
INSERT INTO `sys_role_res` VALUES ('4231', '152', '57');
INSERT INTO `sys_role_res` VALUES ('4232', '199', '57');
INSERT INTO `sys_role_res` VALUES ('4233', '200', '57');
INSERT INTO `sys_role_res` VALUES ('4234', '201', '57');
INSERT INTO `sys_role_res` VALUES ('4235', '202', '57');
INSERT INTO `sys_role_res` VALUES ('4236', '203', '57');
INSERT INTO `sys_role_res` VALUES ('4237', '204', '57');
INSERT INTO `sys_role_res` VALUES ('4238', '206', '57');

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
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '6fc3b9ada8c1e50b400cd6998ff7be76ea3ae312', '1289', '1', '/sys/static/i/9.jpg', 'admin@admin.com', '2017-08-01 18:00:27', '1234567', 'UzNk7MKbVABao0I275SAs9ShzyYtLQooG42fkoDcL9QtkuFP1Xm7QOtIMrbvxbLc');
INSERT INTO `sys_user` VALUES ('32', 'eason', '6fc3b9ada8c1e50b400cd6998ff7be76ea3ae312', '1288', '1', '/images/guest.jpg', '569165857@qq.com', '2017-08-10 23:54:24', '13332892938', null);
INSERT INTO `sys_user` VALUES ('34', 'test', '6fc3b9ada8c1e50b400cd6998ff7be76ea3ae312', '1287', '1', '/images/guest.jpg', '569165857@qq.com', '2017-08-18 22:24:50', '13332892938', 'hDEflmnlVituFC6hbfTzeze8RVSRseIfdgBAGkAU2RRh4c4kIrIYXTTuwEG2Caem');
INSERT INTO `sys_user` VALUES ('35', '123123', 'fe58cc7ab4ba9f207be0bf82efc8147e136f6b52', null, '1', '/images/guest.jpg', '23123', '2017-08-27 00:47:06', null, null);
INSERT INTO `sys_user` VALUES ('36', '111111111', 'fe58cc7ab4ba9f207be0bf82efc8147e136f6b52', null, '1', '/images/guest.jpg', '1215665487@qq.com', '2017-08-27 01:04:25', '1111111', null);
INSERT INTO `sys_user` VALUES ('37', 'sscs', 'fe58cc7ab4ba9f207be0bf82efc8147e136f6b52', null, '1', '/images/guest.jpg', '1@1.com', '2017-09-07 16:43:34', '134564565', null);

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
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('337', '1', '1');
INSERT INTO `sys_user_role` VALUES ('339', '32', '56');
INSERT INTO `sys_user_role` VALUES ('341', '34', '60');
INSERT INTO `sys_user_role` VALUES ('342', '35', '56');
INSERT INTO `sys_user_role` VALUES ('343', '36', '56');
INSERT INTO `sys_user_role` VALUES ('344', '37', '57');

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='实时数据';

-- ----------------------------
-- Records of t_actual_data
-- ----------------------------
INSERT INTO `t_actual_data` VALUES ('1', null, '001', '1001', '00101', '2', '0', '21.00', '1', '2017-08-02 12:00:00', '22');
INSERT INTO `t_actual_data` VALUES ('2', null, '002', '111122', '00201', '2', '1', '12.00', '1', '2017-09-03 12:02:57', '22');
INSERT INTO `t_actual_data` VALUES ('3', null, '003', '1234123', '00301', '1', '1', '22.00', '2', '2017-10-05 12:02:51', '2');
INSERT INTO `t_actual_data` VALUES ('4', null, '001', '1001', '00101', '1', '0', '3.00', '1', '2017-09-01 12:11:50', '220');
INSERT INTO `t_actual_data` VALUES ('5', null, '001', '1001', '00101', '1', '0', '2.00', '1', '2017-09-05 01:54:28', '222');
INSERT INTO `t_actual_data` VALUES ('6', null, '001', '1001', '00101', '1', '0', '8.00', '1', '2017-09-07 03:33:22', '22');

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
  `street` tinyint(4) DEFAULT NULL COMMENT '所属乡镇或街道',
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
  `longitude` decimal(20,6) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(20,6) DEFAULT NULL COMMENT '纬度',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_inner_code` (`inner_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='单位表';

-- ----------------------------
-- Records of t_company
-- ----------------------------
INSERT INTO `t_company` VALUES ('1', '001', '北京某某科技股份有限公司', '2', '海淀区西二旗辉煌国际55号', '1', null, '张三', '186222229', '234234234', '海淀区水利局', '10', '12', '10', '1', '116.615335', '39.896172', '2017-09-07 12:42:45');
INSERT INTO `t_company` VALUES ('2', '002', '北京某某科技公司', '3', '海淀区中关村丹棱街1号', '3', '2', '李四', '1862342343', '8239042', '海淀区水利局', '1', '2', '2', '2', '116.677893', '39.905817', '2017-09-06 11:57:39');
INSERT INTO `t_company` VALUES ('3', '003', '北京途牛科技股份有限公司', '4', '通州区后南仓', '1', '1', '王五', '1823420340', '100340', '海淀区水利局', '2', '1', '2', '1', '116.627623', '39.901085', '2017-09-02 15:06:17');
INSERT INTO `t_company` VALUES ('5', '004', '北京体彩科技有限公司', '6', '北京市昌平区霍营', '2', '1', '赵六', '154345345', '534345', '昌平水利局', '2', '2', '1', '1', '116.633624', '39.910604', '2017-09-05 01:53:24');

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='年用水指标';

-- ----------------------------
-- Records of t_water_index
-- ----------------------------
INSERT INTO `t_water_index` VALUES ('1', null, '001', '3', '200.00', '10.00', '20.00', '10.00', '20.00', '10.00', '20.00', '10.00', '20.00', '20.00', '20.00', '20.00', '20.00');
INSERT INTO `t_water_index` VALUES ('2', null, '002', '1', '200.00', '20.00', '10.00', '20.00', '10.00', '20.00', '10.00', '20.00', '10.00', '20.00', '20.00', '20.00', '20.00');
INSERT INTO `t_water_index` VALUES ('3', null, '003', '1', '200.00', '10.00', '10.00', '20.00', '20.00', '20.00', '10.00', '20.00', '10.00', '20.00', '20.00', '10.00', '30.00');

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
-- Records of t_water_meter
-- ----------------------------
INSERT INTO `t_water_meter` VALUES ('1', null, '001', '昌平回龙观霞飞路', '00101', '2', '3', '公用表', '2', '1年', '2017-08-25 21:28:31');
INSERT INTO `t_water_meter` VALUES ('2', null, '002', '育知路', '00201', '2', '1', '2', '1', '12个月', '2017-08-15 21:28:36');
INSERT INTO `t_water_meter` VALUES ('4', null, '003', '黄平路', '00301', '1', '2', '123', '1', '12个月', '2017-09-03 11:28:15');
INSERT INTO `t_water_meter` VALUES ('5', null, '001', '11', '00102', '2', '1', '11', '1', '11', '2017-09-03 12:39:27');

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
  `pump_model` tinyint(4) DEFAULT NULL COMMENT '水泵型号',
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

-- ----------------------------
-- Records of t_well
-- ----------------------------
INSERT INTO `t_well` VALUES ('1', '00101#', '1号#', null, '001', '回龙观', '朱辛庄', '1', '2017-01-01 00:00:00', '1.00', '1.00', '1', '1.00', '1', '1', '1', '1', '2', '1', '1', '1', '1', '100111', '1', '1', '1', '1', '1', '1.00');
