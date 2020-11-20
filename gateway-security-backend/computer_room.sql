/*
 Navicat Premium Data Transfer

 Source Server         : 本地Mysql8.0.21
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : localhost:3306
 Source Schema         : computer_room

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 20/11/2020 15:29:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for company
-- ----------------------------
DROP TABLE IF EXISTS `company`;
CREATE TABLE `company`  (
  `company_id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `company_name` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `contacts` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `tel` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `address` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `community_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_deleted` int NOT NULL DEFAULT 0,
  `create_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_person` int NULL DEFAULT NULL,
  `modified_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modified_person` int NULL DEFAULT NULL,
  `deleted_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted_person` int NULL DEFAULT NULL,
  PRIMARY KEY (`company_id`) USING BTREE,
  UNIQUE INDEX `company_name`(`company_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `department_id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `department_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_deleted` int NULL DEFAULT 0,
  `create_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_person` int NULL DEFAULT NULL,
  `modified_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modified_person` int NULL DEFAULT NULL,
  `deleted_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted_person` int NULL DEFAULT NULL,
  `department_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`department_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES (2, 'IT', 0, NULL, NULL, '2020-11-07 10:25:14', 1, NULL, NULL, NULL);
INSERT INTO `department` VALUES (3, 'CT', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `department` VALUES (4, 'ATM', 1, NULL, NULL, '2020-11-07 10:25:18', NULL, NULL, 1, NULL);

-- ----------------------------
-- Table structure for sys_backend_api
-- ----------------------------
DROP TABLE IF EXISTS `sys_backend_api`;
CREATE TABLE `sys_backend_api`  (
  `api_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `api_pid` int NOT NULL COMMENT '父ID',
  `api_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'API名称',
  `api_url` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'API请求地址',
  `api_method` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'API请求方式：GET、POST、PUT、DELETE',
  `api_sort` int NOT NULL COMMENT '排序',
  `api_description` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `is_deleted` int NOT NULL DEFAULT 0,
  `create_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_person` int NULL DEFAULT NULL,
  `modified_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `modified_person` int NULL DEFAULT NULL,
  `deleted_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `deleted_person` int NULL DEFAULT NULL,
  `api_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`api_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_backend_api
-- ----------------------------
INSERT INTO `sys_backend_api` VALUES (1, 0, '/user/selectAll', '/**', 'GET,POST,PUT,DELETE', 1, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_frontend_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_frontend_menu`;
CREATE TABLE `sys_frontend_menu`  (
  `menu_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `menu_pid` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '父ID',
  `menu_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '前端菜单名称',
  `menu_code_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `menu_url` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '前端菜单访问HTML地址',
  `menu_sort` int NOT NULL COMMENT '排序',
  `menu_description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `is_deleted` int NOT NULL DEFAULT 0,
  `create_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_person` int NULL DEFAULT NULL,
  `modified_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `modified_person` int NULL DEFAULT NULL,
  `deleted_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `deleted_person` int NULL DEFAULT NULL,
  `menu_remark` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`menu_id`) USING BTREE,
  UNIQUE INDEX `menu_name`(`menu_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_frontend_menu
-- ----------------------------
INSERT INTO `sys_frontend_menu` VALUES (1, '0', '全部', 'all', '/**', 0, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_frontend_menu` VALUES (2, '1', '用户管理', 'auth', '/sysuser', 1, '', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_frontend_menu` VALUES (4, '2', '用户列表', 'sysuser', '/sysuer/list', 2, '', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_frontend_menu` VALUES (5, '2', '角色列表', 'sysrole', '/sysrole/list', 2, '', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_frontend_menu` VALUES (6, '2', '菜单管理', 'frontendMenu', '/sysMenu/list', 2, '', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_frontend_menu` VALUES (17, '2', '公司列表', 'company', '/company', 2, '', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_frontend_menu` VALUES (18, '2', '部门列表', 'department', '/department', 2, '', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色ID',
  `role_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `is_deleted` int NOT NULL DEFAULT 0,
  `create_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `create_person` int NULL DEFAULT NULL,
  `modified_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `modified_person` int NULL DEFAULT NULL,
  `deleted_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `deleted_person` int NULL DEFAULT NULL,
  `role_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE INDEX `role_id`(`role_id`) USING BTREE,
  UNIQUE INDEX `role_name`(`role_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'super_admin', '超级管理员', 0, NULL, NULL, '2020-11-07 10:26:56', 1, NULL, NULL, NULL);
INSERT INTO `sys_role` VALUES (2, 'admin', '管理员', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role` VALUES (3, 'visitor', '游客', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);


-- ----------------------------
-- Table structure for sys_role_backend_api
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_backend_api`;
CREATE TABLE `sys_role_backend_api`  (
  `role_api_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int NOT NULL COMMENT '角色ID',
  `api_id` int NOT NULL COMMENT 'API管理表ID',
  `is_deleted` int NOT NULL DEFAULT 0,
  `create_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `create_person` int NULL DEFAULT NULL,
  `modified_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `modified_person` int NULL DEFAULT NULL,
  `deleted_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `deleted_person` int NULL DEFAULT NULL,
  `role_api_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`role_api_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_backend_api
-- ----------------------------
INSERT INTO `sys_role_backend_api` VALUES (1, 1, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_backend_api` VALUES (2, 2, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_backend_api` VALUES (3, 3, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_backend_api` VALUES (29, 5, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_backend_api` VALUES (30, 6, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_backend_api` VALUES (31, 7, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_backend_api` VALUES (32, 8, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_backend_api` VALUES (33, 9, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_backend_api` VALUES (34, 10, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_backend_api` VALUES (35, 11, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_role_frontend_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_frontend_menu`;
CREATE TABLE `sys_role_frontend_menu`  (
  `role_menu_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int NOT NULL COMMENT '角色ID',
  `menu_id` int NOT NULL COMMENT '前端菜单管理ID',
  `is_deleted` int NOT NULL DEFAULT 0,
  `create_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `create_person` int NULL DEFAULT NULL,
  `modified_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `modified_person` int NULL DEFAULT NULL,
  `deleted_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `deleted_person` int NULL DEFAULT NULL,
  `role_menu_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`role_menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 524 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_frontend_menu
-- ----------------------------
INSERT INTO `sys_role_frontend_menu` VALUES (345, 2, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (346, 2, 4, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (347, 2, 5, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (348, 2, 6, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (349, 2, 204536, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (350, 2, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (351, 6, 4, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (352, 6, 6, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (353, 6, 204536, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (354, 6, 11, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (355, 6, 12, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (356, 6, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (357, 6, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (358, 6, 3, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (359, 6, 7, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (360, 6, 8, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (361, 8, 4, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (362, 8, 204536, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (363, 8, 7, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (364, 8, 10, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (365, 8, 11, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (366, 8, 9, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (367, 8, 14, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (368, 8, 15, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (369, 8, 204535, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (370, 8, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (371, 8, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (372, 8, 3, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (373, 10, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (374, 10, 4, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (375, 10, 5, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (376, 10, 6, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (377, 10, 204536, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (378, 10, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (379, 9, 8, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (380, 9, 12, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (381, 9, 13, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (382, 9, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (383, 9, 3, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (401, 5, 3, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (402, 5, 7, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (403, 5, 10, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (404, 5, 11, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (405, 5, 8, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (406, 5, 12, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (407, 5, 13, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (408, 5, 9, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (409, 5, 14, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (410, 5, 15, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (411, 5, 16, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (412, 5, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (504, 1, 2, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (505, 1, 4, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (506, 1, 5, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (507, 1, 6, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (508, 1, 17, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (509, 1, 18, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (510, 1, 11, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (511, 1, 8, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (512, 1, 12, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (513, 1, 13, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (514, 1, 21, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (515, 1, 15, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (516, 1, 16, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (517, 1, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (518, 1, 3, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (519, 1, 7, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (520, 1, 9, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (521, 11, 16, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (522, 11, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_frontend_menu` VALUES (523, 11, 3, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名称',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码-BCR算法加密',
  `company_id` int NULL DEFAULT NULL,
  `department_id` int NULL DEFAULT NULL,
  `job_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `real_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `user_tel` varchar(11) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `user_description` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `is_deleted` int NOT NULL DEFAULT 0 COMMENT '状态',
  `create_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `create_person` int NULL DEFAULT NULL,
  `modified_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `modified_person` int NULL DEFAULT NULL,
  `deleted_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `deleted_person` int NULL DEFAULT NULL,
  `user_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 297 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$y3scDalc1b2VXB8JESCc2uBlU3nnuyuO91yT8qrE2WZHRg0mLgr3u', NULL, NULL, NULL, '超管', NULL, '超级管理员', 0, NULL, NULL, '2020-10-17 17:47:35', 1, NULL, NULL, NULL);
INSERT INTO `sys_user` VALUES (42, 'assetAdmin', '$2a$10$WBJOmO1zE.xAlMplGDqCK.Zh0ExcEr8aESSjOtVlCHzT9Ezk5PYjy', NULL, NULL, NULL, '资产管理', NULL, '可以对资产模块进行维护的管理员', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_role_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `role_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色ID',
  `is_deleted` int NOT NULL DEFAULT 0,
  `create_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `create_person` int NULL DEFAULT NULL,
  `modified_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `modified_person` int NULL DEFAULT NULL,
  `deleted_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `deleted_person` int NULL DEFAULT NULL,
  `user_role_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`user_role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 73 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (54, '42', '5', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (59, '1', '1', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (60, '49', '3', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (61, '50', '3', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (62, '50', '5', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (65, '51', '9', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (66, '51', '10', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (67, '52', '9', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (68, '52', '10', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (69, '53', '9', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (70, '53', '10', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (71, '54', '1', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
