/*
Navicat MySQL Data Transfer

Source Server         : 192.168.56.110_3306
Source Server Version : 80018
Source Host           : 192.168.56.110:3306
Source Database       : it-community

Target Server Type    : MYSQL
Target Server Version : 80018
File Encoding         : 65001

Date: 2020-05-28 14:35:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ic_access_behavior
-- ----------------------------
DROP TABLE IF EXISTS `ic_access_behavior`;
CREATE TABLE `ic_access_behavior` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `u_id` int(11) DEFAULT NULL,
  `art_id` int(11) DEFAULT NULL,
  `rating` int(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ic_article
-- ----------------------------
DROP TABLE IF EXISTS `ic_article`;
CREATE TABLE `ic_article` (
  `art_id` int(11) NOT NULL AUTO_INCREMENT,
  `u_id` int(11) DEFAULT NULL COMMENT '用户表外键',
  `title` varchar(255) DEFAULT NULL,
  `content` longtext,
  `create_time` datetime DEFAULT NULL,
  `c_id` int(11) DEFAULT NULL COMMENT '文章具体类型',
  `tag` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '文章标签',
  `att_id` int(11) DEFAULT NULL COMMENT '文章附件ID',
  `learn_coin` int(11) DEFAULT NULL COMMENT '悬赏学币',
  `is_over` int(1) DEFAULT NULL COMMENT '是否完结',
  `status` int(1) DEFAULT '0' COMMENT '状态 1精华',
  `top` int(1) DEFAULT '0' COMMENT '置顶',
  `t_expiry_time` datetime DEFAULT NULL,
  `hits_zan` int(11) DEFAULT '0' COMMENT '点赞',
  `hits_comment` int(11) DEFAULT '0' COMMENT '评论',
  `watch` int(11) DEFAULT '0' COMMENT '查看',
  PRIMARY KEY (`art_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ic_article_comment
-- ----------------------------
DROP TABLE IF EXISTS `ic_article_comment`;
CREATE TABLE `ic_article_comment` (
  `art_c_id` int(11) NOT NULL AUTO_INCREMENT,
  `p_id` int(11) DEFAULT NULL COMMENT '父评论ID，为空时，表示评论的是文章，为art_c_id对应的值是为评论别人的回复',
  `art_id` int(11) DEFAULT NULL COMMENT '评论文章ID',
  `content` text,
  `create_time` datetime DEFAULT NULL,
  `u_id` int(11) DEFAULT NULL COMMENT '评论人ID',
  `is_take` int(1) DEFAULT '0' COMMENT '是否采纳',
  `hits_zan` int(11) DEFAULT '0' COMMENT '点赞数',
  PRIMARY KEY (`art_c_id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COMMENT='文章评论表';

-- ----------------------------
-- Table structure for ic_article_comment_zan
-- ----------------------------
DROP TABLE IF EXISTS `ic_article_comment_zan`;
CREATE TABLE `ic_article_comment_zan` (
  `art_c_hz_id` int(11) NOT NULL AUTO_INCREMENT,
  `u_id` int(11) DEFAULT NULL COMMENT '点赞回复用户ID',
  `art_c_id` int(11) DEFAULT NULL COMMENT '对应评论ID',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`art_c_hz_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ic_article_favorite
-- ----------------------------
DROP TABLE IF EXISTS `ic_article_favorite`;
CREATE TABLE `ic_article_favorite` (
  `f_id` int(11) NOT NULL AUTO_INCREMENT,
  `u_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `dir_id` int(11) DEFAULT NULL COMMENT '收藏于对用文件夹的ID',
  `art_id` int(11) DEFAULT NULL COMMENT '文章ID',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for ic_article_watch
-- ----------------------------
DROP TABLE IF EXISTS `ic_article_watch`;
CREATE TABLE `ic_article_watch` (
  `art_w_id` int(11) NOT NULL AUTO_INCREMENT,
  `art_id` int(11) DEFAULT NULL,
  `u_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`art_w_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ic_article_zan
-- ----------------------------
DROP TABLE IF EXISTS `ic_article_zan`;
CREATE TABLE `ic_article_zan` (
  `art_hz_id` int(11) NOT NULL AUTO_INCREMENT,
  `art_id` int(11) DEFAULT NULL,
  `u_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`art_hz_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ic_attachement
-- ----------------------------
DROP TABLE IF EXISTS `ic_attachement`;
CREATE TABLE `ic_attachement` (
  `att_id` int(11) NOT NULL AUTO_INCREMENT,
  `art_id` int(11) DEFAULT NULL COMMENT '所属文章ID',
  `url` varchar(100) DEFAULT NULL COMMENT '附件地址',
  PRIMARY KEY (`att_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ic_case
-- ----------------------------
DROP TABLE IF EXISTS `ic_case`;
CREATE TABLE `ic_case` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `u_id` int(11) DEFAULT NULL,
  `title` varchar(30) DEFAULT NULL,
  `introduce` varchar(100) DEFAULT NULL,
  `case_img` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `zan` int(4) DEFAULT '0',
  `state` int(1) DEFAULT '0' COMMENT '审核状态，0：审核中，1：通过，-1：拒绝',
  `sysu_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for ic_case_zan
-- ----------------------------
DROP TABLE IF EXISTS `ic_case_zan`;
CREATE TABLE `ic_case_zan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `c_id` int(11) DEFAULT NULL COMMENT '案例ID',
  `u_id` int(11) DEFAULT NULL COMMENT '点赞人ID',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for ic_category
-- ----------------------------
DROP TABLE IF EXISTS `ic_category`;
CREATE TABLE `ic_category` (
  `c_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `pinyin` varchar(20) DEFAULT NULL,
  `available` int(1) DEFAULT '1' COMMENT '是否可用 ',
  `state` int(1) DEFAULT '1' COMMENT '普通用户是否可用',
  PRIMARY KEY (`c_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ic_exchange
-- ----------------------------
DROP TABLE IF EXISTS `ic_exchange`;
CREATE TABLE `ic_exchange` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `u_id` int(11) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `claim` varchar(255) DEFAULT NULL,
  `qq` varchar(12) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `state` int(1) DEFAULT '0' COMMENT '0:等待交易，1：完成',
  `expiry_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for ic_favorite_folder
-- ----------------------------
DROP TABLE IF EXISTS `ic_favorite_folder`;
CREATE TABLE `ic_favorite_folder` (
  `d_id` int(11) NOT NULL AUTO_INCREMENT,
  `u_id` int(11) DEFAULT NULL COMMENT '所属用户ID',
  `dirname` varchar(50) DEFAULT NULL COMMENT '文件夹名称',
  `create_time` datetime DEFAULT NULL,
  `state` int(1) DEFAULT '1' COMMENT '文件夹是否可修改及删除',
  PRIMARY KEY (`d_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for ic_message
-- ----------------------------
DROP TABLE IF EXISTS `ic_message`;
CREATE TABLE `ic_message` (
  `msg_id` int(11) NOT NULL AUTO_INCREMENT,
  `send_id` int(11) DEFAULT NULL,
  `recv_id` int(11) DEFAULT NULL COMMENT '为0，则是管理员发送',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`msg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ic_message_text
-- ----------------------------
DROP TABLE IF EXISTS `ic_message_text`;
CREATE TABLE `ic_message_text` (
  `msg_text_id` int(11) NOT NULL AUTO_INCREMENT,
  `message` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`msg_text_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ic_news
-- ----------------------------
DROP TABLE IF EXISTS `ic_news`;
CREATE TABLE `ic_news` (
  `news_id` int(11) NOT NULL AUTO_INCREMENT,
  `sysu_id` int(11) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `expiry_time` datetime DEFAULT NULL COMMENT '失效日期',
  `level` int(1) DEFAULT '1' COMMENT '重要程度',
  PRIMARY KEY (`news_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ic_report
-- ----------------------------
DROP TABLE IF EXISTS `ic_report`;
CREATE TABLE `ic_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(1) DEFAULT NULL COMMENT '举报类型：1：文章，2：评论，3：交易',
  `tid` int(11) DEFAULT NULL COMMENT '该type对应表的主键',
  `reason` varchar(255) DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for ic_rights
-- ----------------------------
DROP TABLE IF EXISTS `ic_rights`;
CREATE TABLE `ic_rights` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `auth_name` varchar(20) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL,
  `path` varchar(50) DEFAULT NULL,
  `available` int(1) DEFAULT '1',
  `level` int(1) DEFAULT NULL,
  `icon` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for ic_rights_role
-- ----------------------------
DROP TABLE IF EXISTS `ic_rights_role`;
CREATE TABLE `ic_rights_role` (
  `role_id` int(11) NOT NULL,
  `right_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for ic_role
-- ----------------------------
DROP TABLE IF EXISTS `ic_role`;
CREATE TABLE `ic_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `role_desc` varchar(255) DEFAULT NULL,
  `available` int(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for ic_signIn
-- ----------------------------
DROP TABLE IF EXISTS `ic_signIn`;
CREATE TABLE `ic_signIn` (
  `s_id` int(11) NOT NULL AUTO_INCREMENT,
  `u_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `days` int(4) DEFAULT NULL,
  PRIMARY KEY (`s_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for ic_user
-- ----------------------------
DROP TABLE IF EXISTS `ic_user`;
CREATE TABLE `ic_user` (
  `u_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) DEFAULT NULL COMMENT '姓名',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `account` varchar(11) DEFAULT NULL COMMENT '学号或工号',
  `password` varchar(36) DEFAULT NULL COMMENT '密码',
  `phone` varchar(11) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `class_grade` varchar(20) DEFAULT NULL COMMENT '班级',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `gender` int(1) DEFAULT NULL COMMENT '性别(1：男，0：女)',
  `signature` varchar(255) DEFAULT NULL COMMENT '签名',
  `learn_coin` int(11) DEFAULT '10',
  `city` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `state` int(1) DEFAULT '0' COMMENT '邮箱是否激活',
  `vip` int(1) DEFAULT '0',
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`u_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
