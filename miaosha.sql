/*
 Navicat Premium Data Transfer

 Source Server         : local-root
 Source Server Type    : MySQL
 Source Server Version : 80013
 Source Host           : localhost:3306
 Source Schema         : miaosha

 Target Server Type    : MySQL
 Target Server Version : 80013
 File Encoding         : 65001

 Date: 14/04/2019 11:57:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `id` bigint(20) NOT NULL,
  `goods_name` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `goods_title` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `goods_img` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `goods_detail` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `goods_price` decimal(10, 2) NULL DEFAULT 0.00,
  `goods_stock` int(11) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES (1, 'iphoneX', 'Apple iphone X A(1865) 全面屏', '/img/iphonex.png', 'Apple iphone X A(1865) 全面屏', 8765.00, 100);
INSERT INTO `goods` VALUES (2, '华为mate9', '华为Mate 9 4GB +32GB版本', '/img/meta10.png', '华为Mate 9 4GB +32GB版本', 3212.00, 100);

-- ----------------------------
-- Table structure for miaosha_goods
-- ----------------------------
DROP TABLE IF EXISTS `miaosha_goods`;
CREATE TABLE `miaosha_goods`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '秒杀的商品表',
  `goods_id` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `miaosha_price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '秒杀价格',
  `stock_count` int(11) NULL DEFAULT 0 COMMENT '库存数量',
  `start_date` datetime(0) NULL DEFAULT NULL COMMENT '秒杀开始的时间',
  `end_date` datetime(0) NULL DEFAULT NULL COMMENT '秒杀结束时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of miaosha_goods
-- ----------------------------
INSERT INTO `miaosha_goods` VALUES (1, 1, 0.01, 7, '2019-04-14 10:21:10', '2019-04-14 15:13:17');
INSERT INTO `miaosha_goods` VALUES (2, 2, 0.01, 7, '2018-05-06 16:40:43', '2018-07-04 17:13:58');

-- ----------------------------
-- Table structure for miaosha_order
-- ----------------------------
DROP TABLE IF EXISTS `miaosha_order`;
CREATE TABLE `miaosha_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `order_id` bigint(20) NULL DEFAULT NULL,
  `goods_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `u_uid_god`(`user_id`, `order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of miaosha_order
-- ----------------------------
INSERT INTO `miaosha_order` VALUES (1, 18786705103, 1, 1);
INSERT INTO `miaosha_order` VALUES (2, 18786705103, 2, 2);
INSERT INTO `miaosha_order` VALUES (3, 18786705103, 3, 1);
INSERT INTO `miaosha_order` VALUES (4, 18786705103, 4, 1);
INSERT INTO `miaosha_order` VALUES (5, 18786705103, 5, 2);

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `goods_id` bigint(20) NULL DEFAULT NULL,
  `delivery_addr_id` bigint(20) NULL DEFAULT NULL,
  `goods_name` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `goods_count` int(11) NULL DEFAULT NULL,
  `goods_price` decimal(10, 2) NULL DEFAULT NULL,
  `order_channel` tinyint(4) NULL DEFAULT NULL,
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '0代表未支付、1已经支付、2已经发货、3已经收货、4已经退款、5已经完成',
  `create_date` datetime(0) NULL DEFAULT NULL,
  `pay_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of order_info
-- ----------------------------
INSERT INTO `order_info` VALUES (1, 18786705103, 1, NULL, 'iphoneX', 1, 0.01, 1, 0, '2018-05-01 19:30:33', NULL);
INSERT INTO `order_info` VALUES (2, 18786705103, 2, NULL, '华为mate9', 1, 0.01, 1, 0, '2018-05-06 17:30:31', NULL);
INSERT INTO `order_info` VALUES (3, 18786705103, 1, NULL, 'iphoneX', 1, 0.01, 1, 0, '2018-05-06 20:53:36', NULL);
INSERT INTO `order_info` VALUES (4, 18786705103, 1, NULL, 'iphoneX', 1, 0.01, 1, 0, '2018-05-08 08:52:34', NULL);
INSERT INTO `order_info` VALUES (5, 18786705103, 2, NULL, '华为mate9', 1, 0.01, 1, 0, '2018-05-08 09:59:44', NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID，手机号码',
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'MD5( MD5 ( pass明文 + 固定salt) + salt)',
  `salt` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `head` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像、云贮存的ID',
  `register_date` datetime(0) NULL DEFAULT NULL COMMENT '注册时间',
  `last_login_date` datetime(0) NULL DEFAULT NULL COMMENT '上次登录时间',
  `login_count` int(11) NULL DEFAULT 0 COMMENT '登录次数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18786705103 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (18786705103, 'allen', 'xcallen', '55d2c90932ec170a830910b15d5f907f', '1a2b3d4c', NULL, '2018-02-26 20:39:35', '2018-04-11 20:38:57', 1);

SET FOREIGN_KEY_CHECKS = 1;
