/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : localhost:3306
 Source Schema         : restapi

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : 65001

 Date: 24/11/2020 11:16:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for external_system
-- ----------------------------
DROP TABLE IF EXISTS `external_system`;
CREATE TABLE `external_system`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `external_system_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `bussiness_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `kafka_topic` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of external_system
-- ----------------------------
INSERT INTO `external_system` VALUES (1, 'ZHLD', 'DevInfo', 'TOPIC_INTERFACE_ZHLD_DEVINFO');
INSERT INTO `external_system` VALUES (2, 'ZHLD', 'DevDetailData', 'TOPIC_INTERFACE_ZHLD_DEVDETAILDATA');
INSERT INTO `external_system` VALUES (3, 'ZHLD', 'DevEventInfo', 'TOPIC_INTERFACE_ZHLD_DEVEVENTINFO');
INSERT INTO `external_system` VALUES (4, 'ZHLD', 'DevAlarmInfo', 'TOPIC_INTERFACE_ZHLD_DEVALARMINFO');
INSERT INTO `external_system` VALUES (5, 'ZHLD', 'DevfaultInfo', 'TOPIC_INTERFACE_ZHLD_DEVFAULTINFO');
INSERT INTO `external_system` VALUES (6, 'ZHGL', 'DevInfo', 'TOPIC_INTERFACE_ZHGL_DEVINFO');
INSERT INTO `external_system` VALUES (7, 'ZHGL', 'DevDetailData', 'TOPIC_INTERFACE_ZHGL_DEVDETAILDATA');
INSERT INTO `external_system` VALUES (8, 'ZHGL', 'DevEventInfo', 'TOPIC_INTERFACE_ZHGL_DEVEVENTINFO');
INSERT INTO `external_system` VALUES (9, 'ZHGL', 'DevAlarmInfo', 'TOPIC_INTERFACE_ZHGL_DEVALARMINFO');
INSERT INTO `external_system` VALUES (10, 'ZHGL', 'DevfaultInfo', 'TOPIC_INTERFACE_ZHGL_DEVFAULTINFO');
INSERT INTO `external_system` VALUES (11, 'ZHSW', 'DevInfo', 'TOPIC_INTERFACE_ZHSW_DEVINFO');
INSERT INTO `external_system` VALUES (12, 'ZHSW', 'DevDetailData', 'TOPIC_INTERFACE_ZHSW_DEVDETAILDATA');
INSERT INTO `external_system` VALUES (13, 'ZHSW', 'DevEventInfo', 'TOPIC_INTERFACE_ZHSW_DEVEVENTINFO');
INSERT INTO `external_system` VALUES (14, 'ZHSW', 'DevAlarmInfo', 'TOPIC_INTERFACE_ZHSW_DEVALARMINFO');
INSERT INTO `external_system` VALUES (15, 'ZHSW', 'DevfaultInfo', 'TOPIC_INTERFACE_ZHSW_DEVFAULTINFO');
INSERT INTO `external_system` VALUES (16, 'ZHID', 'DevInfo', 'TOPIC_INTERFACE_ZHID_DEVINFO');
INSERT INTO `external_system` VALUES (17, 'ZHID', 'DevDetailData', 'TOPIC_INTERFACE_ZHID_DEVDETAILDATA');
INSERT INTO `external_system` VALUES (18, 'ZHID', 'DevEventInfo', 'TOPIC_INTERFACE_ZHID_DEVEVENTINFO');
INSERT INTO `external_system` VALUES (19, 'ZHID', 'DevAlarmInfo', 'TOPIC_INTERFACE_ZHID_DEVALARMINFO');
INSERT INTO `external_system` VALUES (20, 'ZHID', 'DevfaultInfo', 'TOPIC_INTERFACE_ZHID_DEVFAULTINFO');

-- ----------------------------
-- Table structure for external_system_scheduler_log
-- ----------------------------
DROP TABLE IF EXISTS `external_system_scheduler_log`;
CREATE TABLE `external_system_scheduler_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `external_system_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `bussiness_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `start_time` timestamp(0) NULL DEFAULT NULL,
  `end_time` timestamp(0) NULL DEFAULT NULL,
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `process_milliseconds` int(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of external_system_scheduler_log
-- ----------------------------
INSERT INTO `external_system_scheduler_log` VALUES (1, 'ZHLD', 'DevInfo', '2020-03-24 17:10:29', '2020-03-24 17:10:30', 'success', '数据接收成功', 727);
INSERT INTO `external_system_scheduler_log` VALUES (2, 'ZHLD', 'DevInfo', '2020-03-24 17:11:21', '2020-03-24 17:11:21', 'success', '数据接收成功', 4);
INSERT INTO `external_system_scheduler_log` VALUES (3, 'ZHLD', 'DevInfo', '2020-03-24 17:11:32', '2020-03-24 17:11:32', 'success', '数据接收成功', 2);
INSERT INTO `external_system_scheduler_log` VALUES (4, 'ZHLD', 'DevInfo', '2020-04-09 16:22:15', '2020-04-09 16:23:15', 'success', '数据接收成功', 60365);
INSERT INTO `external_system_scheduler_log` VALUES (5, 'ZHLD', 'DevInfo', '2020-04-09 16:23:25', '2020-04-09 16:23:37', 'success', '数据接收成功', 12469);
INSERT INTO `external_system_scheduler_log` VALUES (6, 'ZHLD', 'DevInfo', '2020-04-09 16:23:58', '2020-04-09 16:23:58', 'success', '数据接收成功', 4);
INSERT INTO `external_system_scheduler_log` VALUES (7, 'ZHLD', 'DevInfo', '2020-04-09 16:25:51', '2020-04-09 16:25:51', 'success', '数据接收成功', 15);
INSERT INTO `external_system_scheduler_log` VALUES (8, 'ZHLD', 'DevInfo', '2020-04-09 16:26:08', '2020-04-09 16:26:08', 'success', '数据接收成功', 3);
INSERT INTO `external_system_scheduler_log` VALUES (9, 'ZHLD', 'DevInfo', '2020-04-09 16:26:22', '2020-04-09 16:26:22', 'success', '数据接收成功', 4);
INSERT INTO `external_system_scheduler_log` VALUES (10, 'ZHLD', 'DevInfo', '2020-04-09 16:41:20', '2020-04-09 16:41:20', 'success', '数据接收成功', 68);
INSERT INTO `external_system_scheduler_log` VALUES (11, 'ZHLD', 'DevInfo', '2020-04-09 16:47:38', '2020-04-09 16:47:38', 'success', '数据接收成功', 704);
INSERT INTO `external_system_scheduler_log` VALUES (12, 'ZHLD', 'DevInfo', '2020-04-09 16:49:16', '2020-04-09 16:49:16', 'success', '数据接收成功', 22);
INSERT INTO `external_system_scheduler_log` VALUES (13, 'ZHLD', 'DevInfo', '2020-04-09 16:49:17', '2020-04-09 16:49:17', 'success', '数据接收成功', 4);
INSERT INTO `external_system_scheduler_log` VALUES (14, 'ZHLD', 'DevInfo', '2020-04-09 16:49:18', '2020-04-09 16:49:18', 'success', '数据接收成功', 10);
INSERT INTO `external_system_scheduler_log` VALUES (15, 'ZHLD', 'DevInfo', '2020-04-09 16:49:18', '2020-04-09 16:49:18', 'success', '数据接收成功', 4);

-- ----------------------------
-- Table structure for headers
-- ----------------------------
DROP TABLE IF EXISTS `headers`;
CREATE TABLE `headers`  (
  `interface_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `key_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `value_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of headers
-- ----------------------------
INSERT INTO `headers` VALUES ('interface0002', 'Content-Type', 'application/x-www-form-urlencoded');
INSERT INTO `headers` VALUES ('interface0001', 'Content-Type', 'application/x-www-form-urlencoded');
INSERT INTO `headers` VALUES ('7bcff9e8452f412da28ae14030290896', 'headerKey_1', 'headerVal_1');
INSERT INTO `headers` VALUES ('7bcff9e8452f412da28ae14030290896', 'headerKey_2', 'headerVal_2');

-- ----------------------------
-- Table structure for http_base_info
-- ----------------------------
DROP TABLE IF EXISTS `http_base_info`;
CREATE TABLE `http_base_info`  (
  `interface_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `interface_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`interface_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of http_base_info
-- ----------------------------
INSERT INTO `http_base_info` VALUES ('423e338214d2494ea828ea0853df0dfb', 'http://localhost:8088/demo/page_request', 'POST', '胡西祥测试接口');
INSERT INTO `http_base_info` VALUES ('7bcff9e8452f412da28ae14030290896', 'https://www.baidu.com', 'GET', '胡西祥测试接口');
INSERT INTO `http_base_info` VALUES ('interface0001', 'http://www.dcits.com', 'get', '神州信息官网测试接口');
INSERT INTO `http_base_info` VALUES ('interface0002', 'http://localhost:8088/demo/getrequest001', 'get', 'GET请求测试接口001');
INSERT INTO `http_base_info` VALUES ('interface0003', 'http://localhost:8088/demo/getrequest002?paramKey=paramValue', 'get', 'GET请求测试接口002');

-- ----------------------------
-- Table structure for pages
-- ----------------------------
DROP TABLE IF EXISTS `pages`;
CREATE TABLE `pages`  (
  `interface_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接口请求ID',
  `page_num_name` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '页码字段名称',
  `page_num` int(11) NULL DEFAULT NULL COMMENT '起始页码',
  `page_size_name` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '每页大小字段名称',
  `page_size` int(11) NULL DEFAULT NULL COMMENT '每页大小',
  `total_name` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '全部记录数字段',
  PRIMARY KEY (`interface_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for parameters
-- ----------------------------
DROP TABLE IF EXISTS `parameters`;
CREATE TABLE `parameters`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `interface_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `key_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `value_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `value_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of parameters
-- ----------------------------
INSERT INTO `parameters` VALUES (1, 'interface0002', 'paramKey', 'paramValue', 'String');
INSERT INTO `parameters` VALUES (2, '7bcff9e8452f412da28ae14030290896', 'paramKey_1', 'paramVal_1', 'string');
INSERT INTO `parameters` VALUES (3, '7bcff9e8452f412da28ae14030290896', 'paramKey_2', 'paramVal_2', 'string');
INSERT INTO `parameters` VALUES (9, '423e338214d2494ea828ea0853df0dfb', 'pageNumberName', 'pageNumber', 'string');
INSERT INTO `parameters` VALUES (10, '423e338214d2494ea828ea0853df0dfb', 'pageNumber', '2', 'int');
INSERT INTO `parameters` VALUES (11, '423e338214d2494ea828ea0853df0dfb', 'pageSizeName', 'pageSize', 'string');
INSERT INTO `parameters` VALUES (12, '423e338214d2494ea828ea0853df0dfb', 'pageSize', '5', 'int');
INSERT INTO `parameters` VALUES (13, '423e338214d2494ea828ea0853df0dfb', 'totalName', 'total', 'string');

-- ----------------------------
-- Table structure for people
-- ----------------------------
DROP TABLE IF EXISTS `people`;
CREATE TABLE `people`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sex` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `age` int(11) NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of people
-- ----------------------------
INSERT INTO `people` VALUES (1, 'hu', 'man', 20, '北京市');
INSERT INTO `people` VALUES (2, 'xi', '男', 21, '北京市');
INSERT INTO `people` VALUES (3, 'huxixiang', '男', 30, '山东省');
INSERT INTO `people` VALUES (4, '胡西祥', '男', 30, '山东省');
INSERT INTO `people` VALUES (5, '胡西祥', '男', 30, '山东省');
INSERT INTO `people` VALUES (6, '胡西祥', '男', 30, '山东省');
INSERT INTO `people` VALUES (7, '胡西祥', '男', 30, '山东省');
INSERT INTO `people` VALUES (8, '胡西祥', '男', 30, '山东省');
INSERT INTO `people` VALUES (9, '胡西祥', '男', 30, '山东省');
INSERT INTO `people` VALUES (10, '胡西祥', '男', 30, '山东省');
INSERT INTO `people` VALUES (11, '胡西祥', '男', 30, '山东省');
INSERT INTO `people` VALUES (12, '胡西祥', '男', 30, '山东省');
INSERT INTO `people` VALUES (13, '胡西祥', '男', 30, '山东省');
INSERT INTO `people` VALUES (14, '胡西祥', '男', 30, '山东省');
INSERT INTO `people` VALUES (16, '胡西祥', '男', 30, '山东省');

-- ----------------------------
-- Table structure for scheduler_crons
-- ----------------------------
DROP TABLE IF EXISTS `scheduler_crons`;
CREATE TABLE `scheduler_crons`  (
  `interface_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `cron` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '调度状态信息',
  `class_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `kafka_topic` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `system_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对接的外系统ID',
  PRIMARY KEY (`interface_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scheduler_crons
-- ----------------------------
INSERT INTO `scheduler_crons` VALUES ('423e338214d2494ea828ea0853df0dfb', '0/5 * * * * ?', 'stopped', 'PageNumberRequestProcess', 'API_REQUEST_DEMO_TOPIC', NULL);
INSERT INTO `scheduler_crons` VALUES ('7bcff9e8452f412da28ae14030290896', '0/5 * * * * ?', 'stopped', 'InterfaceProcessCommon', 'API_REQUEST_DEMO_TOPIC', NULL);
INSERT INTO `scheduler_crons` VALUES ('interface0001', '0/2 * * * * ?', 'stopped', 'InterfaceProcessCommon', 'API_REQUEST_DEMO_TOPIC', NULL);
INSERT INTO `scheduler_crons` VALUES ('interface0002', '0/5 * * * * ?', 'stopped', 'InterfaceProcessCommon', 'API_REQUEST_DEMO_TOPIC', NULL);
INSERT INTO `scheduler_crons` VALUES ('interface0003', '0/5 * * * * ?', 'stopped', 'InterfaceProcessCommon', 'API_REQUEST_DEMO_TOPIC', NULL);

-- ----------------------------
-- Table structure for scheduler_job_log
-- ----------------------------
DROP TABLE IF EXISTS `scheduler_job_log`;
CREATE TABLE `scheduler_job_log`  (
  `scheduler_job_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `interface_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `interface_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口中文名称',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `success_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `fail_desc` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `process_class` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '处理类的名称',
  `process_start_time` timestamp(0) NULL DEFAULT NULL COMMENT '任务执行开始时间',
  `process_end_time` timestamp(0) NULL DEFAULT NULL COMMENT '任务执行结束时间',
  `process_milliseconds` int(255) NULL DEFAULT NULL COMMENT '任务执行多长时间，单位毫秒',
  PRIMARY KEY (`scheduler_job_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scheduler_job_log
-- ----------------------------
INSERT INTO `scheduler_job_log` VALUES ('01058498bdff47e48be8909487dd930d', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:17:30', '2020-05-06 17:17:30', 31);
INSERT INTO `scheduler_job_log` VALUES ('05db47169e4d4733b087cc83e48f435a', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:17:15', '2020-05-06 17:17:15', 51);
INSERT INTO `scheduler_job_log` VALUES ('09426cb3b9984ad28fb9d254e8469274', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:17:00', '2020-05-06 17:17:00', 36);
INSERT INTO `scheduler_job_log` VALUES ('0b1ce0576be84c039e10b8a924fd264e', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:17:25', '2020-05-06 17:17:25', 59);
INSERT INTO `scheduler_job_log` VALUES ('1a8432e2d5d74f248364297a12e4a558', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:17:20', '2020-05-06 17:17:20', 33);
INSERT INTO `scheduler_job_log` VALUES ('3145b4efa00f4a279f6e9f4869e61a2c', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:17:30', '2020-05-06 17:17:30', 19);
INSERT INTO `scheduler_job_log` VALUES ('38e760713ae9440bb51b665c2f98da21', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:16:40', '2020-05-06 17:16:41', 707);
INSERT INTO `scheduler_job_log` VALUES ('3a01545a3da942e49932f4bcdfe924ab', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:17:05', '2020-05-06 17:17:05', 39);
INSERT INTO `scheduler_job_log` VALUES ('4b4c51a2be14476a8c2c2bfa80acca2c', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:17:05', '2020-05-06 17:17:05', 55);
INSERT INTO `scheduler_job_log` VALUES ('4b53802a628a466c8ca1c5c280dbde45', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:17:20', '2020-05-06 17:17:20', 33);
INSERT INTO `scheduler_job_log` VALUES ('50fe697ba7134daaa0696e265413529c', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:17:10', '2020-05-06 17:17:10', 79);
INSERT INTO `scheduler_job_log` VALUES ('55f7134d5b88436884cbf568a59ae16c', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:17:10', '2020-05-06 17:17:10', 72);
INSERT INTO `scheduler_job_log` VALUES ('5e74f91ae1e84f91a933cfa96c24e4f9', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:17:00', '2020-05-06 17:17:00', 24);
INSERT INTO `scheduler_job_log` VALUES ('62090738d1c64654beda55b2b720bad7', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:16:50', '2020-05-06 17:16:50', 55);
INSERT INTO `scheduler_job_log` VALUES ('66386c3307d041939070e3c3f7c738cf', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:16:55', '2020-05-06 17:16:55', 21);
INSERT INTO `scheduler_job_log` VALUES ('6c07d0f98e714c0f8e1bebea9e2767a4', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:16:50', '2020-05-06 17:16:50', 41);
INSERT INTO `scheduler_job_log` VALUES ('6dfa0a21cb6d41b28255cfef4f604ca6', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:16:45', '2020-05-06 17:16:45', 52);
INSERT INTO `scheduler_job_log` VALUES ('73a355236caa428eb3e5ef19a1f60fb9', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:17:00', '2020-05-06 17:17:00', 36);
INSERT INTO `scheduler_job_log` VALUES ('7741374d0681459ea815883e08a88f27', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:16:55', '2020-05-06 17:16:55', 33);
INSERT INTO `scheduler_job_log` VALUES ('8113c5d2e569410591cd33841f595ab2', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:17:25', '2020-05-06 17:17:25', 46);
INSERT INTO `scheduler_job_log` VALUES ('a53f07fc83ff479685e5c500f84081e8', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:17:30', '2020-05-06 17:17:30', 31);
INSERT INTO `scheduler_job_log` VALUES ('b27efeac9c3c4486a078559b3e1254e9', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:17:25', '2020-05-06 17:17:25', 59);
INSERT INTO `scheduler_job_log` VALUES ('b3986b2cb546463cbe55d96a32533a93', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:17:15', '2020-05-06 17:17:15', 64);
INSERT INTO `scheduler_job_log` VALUES ('b471e62a83e2472b8f61c53e503d4ee5', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:16:45', '2020-05-06 17:16:45', 52);
INSERT INTO `scheduler_job_log` VALUES ('c101304091ef4bb9b9a428344f7adcae', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:16:45', '2020-05-06 17:16:45', 39);
INSERT INTO `scheduler_job_log` VALUES ('c12bed4c88f6407dae784c12bd9e3f3d', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:17:20', '2020-05-06 17:17:20', 27);
INSERT INTO `scheduler_job_log` VALUES ('d9160f5eebe14f6696fe10f7148a3c5b', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:16:40', '2020-05-06 17:16:41', 707);
INSERT INTO `scheduler_job_log` VALUES ('e1fc75feab0648388eda754cdcd07982', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:17:10', '2020-05-06 17:17:10', 79);
INSERT INTO `scheduler_job_log` VALUES ('e27b496e17ed415daed2ccbb9aea9395', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:16:40', '2020-05-06 17:16:41', 687);
INSERT INTO `scheduler_job_log` VALUES ('e37ad9bb39b74bdf8fa7d2d57e9b25b0', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:17:15', '2020-05-06 17:17:15', 64);
INSERT INTO `scheduler_job_log` VALUES ('e6c285cfa8b64364b2caeced4d4c72e5', '423e338214d2494ea828ea0853df0dfb', NULL, 'success', '接口423e338214d2494ea828ea0853df0dfb执行成功', NULL, 'PageNumberRequestProcess', '2020-05-06 17:16:50', '2020-05-06 17:16:50', 55);

-- ----------------------------
-- Table structure for tokens
-- ----------------------------
DROP TABLE IF EXISTS `tokens`;
CREATE TABLE `tokens`  (
  `interface_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `effective_end_time` timestamp(0) NULL DEFAULT NULL,
  `system_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`system_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
