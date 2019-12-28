/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : cms

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 28/12/2019 17:50:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限名',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `deletable` int(11) NOT NULL COMMENT '是否可被删除：1-是，2-否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES ('63b386ad-d71e-4c4c-8274-bc3896950c4d', 'dashboard', '控制面板', 0);
INSERT INTO `permission` VALUES ('7c7f5593-4c92-4a65-bc59-0e11bdf9f22d', 'user', '用户与权限管理模块', 0);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `deletable` int(11) NOT NULL COMMENT '是否可被删除：1-是，0-否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('44a2b276-a27f-4662-bb5e-70094a624391', 'guest', '访客', 0);
INSERT INTO `role` VALUES ('ad66668e-bbc4-4209-91fe-0c581c9e4e93', 'admin', '超级管理员角色', 0);

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `role_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色主键',
  `permission_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES ('2841ac28-25d8-4920-aa27-9e5e19671a71', 'ad66668e-bbc4-4209-91fe-0c581c9e4e93', '63b386ad-d71e-4c4c-8274-bc3896950c4d');
INSERT INTO `role_permission` VALUES ('34f621f6-e46e-4527-8367-1a2dd20972d8', '82e3ed1e-f7b0-40ba-aa72-b7e7a34aff3b', '63b386ad-d71e-4c4c-8274-bc3896950c4d');
INSERT INTO `role_permission` VALUES ('6484c067-94ec-4744-92c3-093029e7aa67', 'ad66668e-bbc4-4209-91fe-0c581c9e4e93', '7c7f5593-4c92-4a65-bc59-0e11bdf9f22d');
INSERT INTO `role_permission` VALUES ('71313644-7ae8-485d-a8c1-5d870a342380', 'aa05c32a-610d-4fef-bbc0-14656d8c0e9d', '63b386ad-d71e-4c4c-8274-bc3896950c4d');
INSERT INTO `role_permission` VALUES ('9b0f018a-952e-43e2-ae31-5139c7e3f7ae', 'c5ad2569-8289-490c-966f-2bc577f2d318', '63b386ad-d71e-4c4c-8274-bc3896950c4d');
INSERT INTO `role_permission` VALUES ('b129cd7f-5dfa-4e11-8f54-4310251598d5', '44a2b276-a27f-4662-bb5e-70094a624391', '63b386ad-d71e-4c4c-8274-bc3896950c4d');
INSERT INTO `role_permission` VALUES ('c0d6ec50-526a-46b8-a949-adb865517b1d', '9fbcadb0-ce49-435d-80dd-c8e543e52006', '63b386ad-d71e-4c4c-8274-bc3896950c4d');
INSERT INTO `role_permission` VALUES ('d47bdfbf-2c1f-4644-b05a-f6e880331596', '5db4d30e-93d6-47ed-a750-aed2d85eb79a', '63b386ad-d71e-4c4c-8274-bc3896950c4d');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `telephone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机',
  `enabled` int(11) NOT NULL COMMENT '是否启用：1-启用，0-禁用',
  `deletable` int(11) NOT NULL COMMENT '是否可删除：1-是，0-否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('a8ef088d-252b-4548-9afb-c209aa654de8', 'admin', 'b3d63778665d633d455df9d02bb1a05a', '363408268@qq.com', '18584848465', 1, 0);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `user_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户主键',
  `role_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('38fe9d4e-b2d9-4ee3-92d5-88efed626c1a', 'a8ef088d-252b-4548-9afb-c209aa654de8', 'ad66668e-bbc4-4209-91fe-0c581c9e4e93');

SET FOREIGN_KEY_CHECKS = 1;
