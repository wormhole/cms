/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : cms

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 08/09/2020 00:38:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE cms CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE cms;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单标题',
  `key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单键',
  `parent` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父级菜单',
  `ts` datetime(0) NULL DEFAULT NULL COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('47e0126e-e03c-4e8c-9a1e-b821636aec8b', '图片管理', 'image', '7b6abff6-5b55-447e-99b7-f26845aad3d9', '2020-09-04 22:57:41');
INSERT INTO `menu` VALUES ('7b6abff6-5b55-447e-99b7-f26845aad3d9', '系统管理', 'manage', NULL, '2020-09-04 22:57:17');
INSERT INTO `menu` VALUES ('869d38f6-1336-472f-b6d6-b97163d1bce9', '用户管理', 'user', 'f853785f-5e8d-4622-b680-eb9310eaf9da', '2020-09-04 22:55:28');
INSERT INTO `menu` VALUES ('9162c1ce-2e26-495c-b431-ea025c0e6e7d', '角色管理', 'role', 'f853785f-5e8d-4622-b680-eb9310eaf9da', '2020-09-04 22:56:13');
INSERT INTO `menu` VALUES ('9aea0082-f45c-4f3b-9f97-6e17c9bcc490', '基本信息', 'base', '7b6abff6-5b55-447e-99b7-f26845aad3d9', '2020-09-04 22:59:16');
INSERT INTO `menu` VALUES ('aad0f9d5-6400-4cf6-891b-86d0bc3b383c', '仪表盘', 'dashboard', NULL, '2020-09-04 22:54:34');
INSERT INTO `menu` VALUES ('f853785f-5e8d-4622-b680-eb9310eaf9da', '认证与授权', 'auth', NULL, '2020-09-04 22:55:00');

-- ----------------------------
-- Table structure for property
-- ----------------------------
DROP TABLE IF EXISTS `property`;
CREATE TABLE `property`  (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'key',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'value',
  `ts` datetime(0) NULL DEFAULT NULL COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of property
-- ----------------------------
INSERT INTO `property` VALUES ('5e9cbc50-bb1a-4374-b497-65cdc70b92a1', 'copyright', 'copyright © 2020 by 凉衫薄', '2020-09-06 19:04:22');
INSERT INTO `property` VALUES ('9d5fd101-ecb7-484a-a931-a29a5a3bda91', 'head', '/head.jpg', '2020-09-06 19:04:22');
INSERT INTO `property` VALUES ('ce389eb0-ce61-4df7-8d38-67995ba8368f', 'title', '内容管理系统', '2020-09-06 19:04:22');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `builtin` int(11) NULL DEFAULT NULL COMMENT '是否内置角色：1-是，0-否',
  `ts` datetime(0) NULL DEFAULT NULL COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('2ba05bf7-ed57-47f6-9e49-cf3c4f1ce02a', 'guest', '宾客', 1, '2020-09-06 20:34:02');
INSERT INTO `role` VALUES ('ad66668e-bbc4-4209-91fe-0c581c9e4e93', 'admin', '超级管理员', 1, '2020-09-06 21:05:26');

-- ----------------------------
-- Table structure for role_menu_ref
-- ----------------------------
DROP TABLE IF EXISTS `role_menu_ref`;
CREATE TABLE `role_menu_ref`  (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `role_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色主键',
  `menu_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单主键',
  `ts` datetime(0) NULL DEFAULT NULL COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_menu_ref
-- ----------------------------
INSERT INTO `role_menu_ref` VALUES ('0f029f41-dad0-4476-97f4-9b4b1f718a98', 'ad66668e-bbc4-4209-91fe-0c581c9e4e93', '869d38f6-1336-472f-b6d6-b97163d1bce9', '2020-09-06 21:05:26');
INSERT INTO `role_menu_ref` VALUES ('18d6a5bc-b6d2-4b4a-9f78-5905d4d6cf36', 'ad66668e-bbc4-4209-91fe-0c581c9e4e93', '7b6abff6-5b55-447e-99b7-f26845aad3d9', '2020-09-06 21:05:26');
INSERT INTO `role_menu_ref` VALUES ('4812a60d-8fd6-454c-9c20-2e12052f67fd', 'ad66668e-bbc4-4209-91fe-0c581c9e4e93', '47e0126e-e03c-4e8c-9a1e-b821636aec8b', '2020-09-06 21:05:26');
INSERT INTO `role_menu_ref` VALUES ('802fdd3f-5095-467d-8594-cb54ba673d9a', 'ad66668e-bbc4-4209-91fe-0c581c9e4e93', '9162c1ce-2e26-495c-b431-ea025c0e6e7d', '2020-09-06 21:05:26');
INSERT INTO `role_menu_ref` VALUES ('aa743741-e13b-433e-835c-30126433ab54', 'ad66668e-bbc4-4209-91fe-0c581c9e4e93', 'f853785f-5e8d-4622-b680-eb9310eaf9da', '2020-09-06 21:05:26');
INSERT INTO `role_menu_ref` VALUES ('c056b984-04b8-4ef1-ba9e-f1f441d221e8', '2ba05bf7-ed57-47f6-9e49-cf3c4f1ce02a', 'aad0f9d5-6400-4cf6-891b-86d0bc3b383c', '2020-09-06 20:34:02');
INSERT INTO `role_menu_ref` VALUES ('db0e7fd9-0731-4ab2-8560-d93a44acf387', 'ad66668e-bbc4-4209-91fe-0c581c9e4e93', '9aea0082-f45c-4f3b-9f97-6e17c9bcc490', '2020-09-06 21:05:26');
INSERT INTO `role_menu_ref` VALUES ('f89b5efb-a1ea-4af0-87a5-432892ee26a2', 'ad66668e-bbc4-4209-91fe-0c581c9e4e93', 'aad0f9d5-6400-4cf6-891b-86d0bc3b383c', '2020-09-06 21:05:26');

-- ----------------------------
-- Table structure for upload
-- ----------------------------
DROP TABLE IF EXISTS `upload`;
CREATE TABLE `upload`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路径',
  `ts` datetime(0) NULL DEFAULT NULL COMMENT '时间戳',
  `user_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `type` int(11) NULL DEFAULT NULL COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `telephone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机',
  `enable` int(11) NULL DEFAULT NULL COMMENT '是否启用：1-启用，0-禁用',
  `builtin` int(11) NULL DEFAULT NULL COMMENT '是否内置用户：1-是，0-否',
  `ttl` int(11) NULL DEFAULT NULL COMMENT '会话时间（分）',
  `limit` int(11) NULL DEFAULT NULL COMMENT '允许最大登录数量',
  `lock` int(11) NULL DEFAULT NULL COMMENT '锁定时长（分）',
  `failure` int(11) NULL DEFAULT NULL COMMENT '允许登录失败次数',
  `ts` datetime(0) NULL DEFAULT NULL COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('3a138baa-2afa-40ec-8ee3-7612586ce3fb', 'admin', '$2a$10$SZBZZLPe0pXULZ9IeEBfTeOKle/peyFIkEMZFhKypokbH9JCMawl6', '363408268@qq.com', '18584848465', 1, 1, 30, 1, 30, 5, '2020-09-06 21:17:49');

-- ----------------------------
-- Table structure for user_role_ref
-- ----------------------------
DROP TABLE IF EXISTS `user_role_ref`;
CREATE TABLE `user_role_ref`  (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `user_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户主键',
  `role_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色主键',
  `ts` datetime(0) NULL DEFAULT NULL COMMENT '时间戳',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role_ref
-- ----------------------------
INSERT INTO `user_role_ref` VALUES ('e4992676-784d-4e63-b232-15190ecba681', '3a138baa-2afa-40ec-8ee3-7612586ce3fb', 'ad66668e-bbc4-4209-91fe-0c581c9e4e93', '2020-09-06 20:02:49');

SET FOREIGN_KEY_CHECKS = 1;
