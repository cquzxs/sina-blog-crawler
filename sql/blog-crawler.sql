/*
Navicat MySQL Data Transfer

Source Server         : 10.0.101.23
Source Server Version : 50717
Source Host           : 10.0.101.23:3306
Source Database       : blog-crawler

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-12-04 17:42:37
*/
use blog-crawler;
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for blog_user_info
-- ----------------------------
DROP TABLE IF EXISTS `blog_user_info`;
CREATE TABLE `blog_user_info` (
  `id` varchar(255) NOT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `introduce` varchar(255) DEFAULT NULL,
  `blog_level` int(255) DEFAULT NULL,
  `follow_count` int(255) DEFAULT NULL,
  `fans_count` int(255) DEFAULT NULL,
  `blog_count` int(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
