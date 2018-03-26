/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : smokedb

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2018-03-22 17:47:10
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menuid` int(11) NOT NULL AUTO_INCREMENT,
  `menuicon` varchar(100) DEFAULT NULL,
  `menuname` varchar(12) NOT NULL,
  `menuurl` varchar(20) DEFAULT NULL,
  `number` int(11) DEFAULT NULL,
  `parentid` int(11) NOT NULL,
  PRIMARY KEY (`menuid`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', 'menu-icon fa fa-pencil-square-o', '项目评审统计表', '/expert/list', '1', '0');
INSERT INTO `sys_menu` VALUES ('2', 'menu-icon fa fa-cog', '系统设置', null, '6', '0');
INSERT INTO `sys_menu` VALUES ('3', null, '角色管理', 'role/index', '4', '2');
INSERT INTO `sys_menu` VALUES ('4', null, '用户管理', 'user/index', '5', '2');
INSERT INTO `sys_menu` VALUES ('5', 'menu-icon fa fa-bar-chart-o', '专家统计报表', '/expert/count', '2', '0');
INSERT INTO `sys_menu` VALUES ('6', 'menu-icon fa fa-beer', '项目统计报表', '/project/list', '3', '0');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rights` decimal(19,2) DEFAULT NULL,
  `role_name` varchar(20) NOT NULL,
  `role_pid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '126.00', '超级管理员', '0');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(20) DEFAULT NULL,
  `password` varchar(50) NOT NULL,
  `sex` int(11) DEFAULT NULL,
  `user_id` varchar(18) NOT NULL,
  `user_name` varchar(10) NOT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4dm5kxn3potpfgdigehw7pdyu` (`role_id`),
  CONSTRAINT `FK4dm5kxn3potpfgdigehw7pdyu` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', '13984842424', 'AZICOnu9cyUFFvBp3xi1AA==', '1', 'admin', '管理员', '1');

-- ----------------------------
-- Table structure for s_expert
-- ----------------------------
DROP TABLE IF EXISTS `s_expert`;
CREATE TABLE `s_expert` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `expert_name_skill` varchar(255) DEFAULT NULL,
  `expert_type` int(11) DEFAULT NULL,
  `expert_unit_skill` varchar(255) DEFAULT NULL,
  `project_name` varchar(50) DEFAULT NULL,
  `review_cost` decimal(19,2) DEFAULT NULL,
  `review_time` datetime DEFAULT NULL,
  `review_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of s_expert
-- ----------------------------
