/*
 Navicat Premium Data Transfer

 Source Server         : 自动化部署web-dev
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : 127.0.0.1:30311
 Source Schema         : trodb_web_base

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 01/05/2021 22:16:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

create database IF NOT EXISTS trodb;
use trodb;

-- ----------------------------
-- Table structure for JOB_EXECUTION_LOG
-- ----------------------------
DROP TABLE IF EXISTS `JOB_EXECUTION_LOG`;
CREATE TABLE `JOB_EXECUTION_LOG` (
  `id` varchar(40) COLLATE utf8_bin NOT NULL,
  `job_name` varchar(100) COLLATE utf8_bin NOT NULL,
  `task_id` varchar(255) COLLATE utf8_bin NOT NULL,
  `hostname` varchar(255) COLLATE utf8_bin NOT NULL,
  `ip` varchar(50) COLLATE utf8_bin NOT NULL,
  `sharding_item` int(11) NOT NULL,
  `execution_source` varchar(20) COLLATE utf8_bin NOT NULL,
  `failure_cause` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `is_success` int(11) NOT NULL,
  `start_time` timestamp NULL DEFAULT NULL,
  `complete_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of JOB_EXECUTION_LOG
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for JOB_STATUS_TRACE_LOG
-- ----------------------------
DROP TABLE IF EXISTS `JOB_STATUS_TRACE_LOG`;
CREATE TABLE `JOB_STATUS_TRACE_LOG` (
  `id` varchar(40) COLLATE utf8_bin NOT NULL,
  `job_name` varchar(100) COLLATE utf8_bin NOT NULL,
  `original_task_id` varchar(255) COLLATE utf8_bin NOT NULL,
  `task_id` varchar(255) COLLATE utf8_bin NOT NULL,
  `slave_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `source` varchar(50) COLLATE utf8_bin NOT NULL,
  `execution_type` varchar(20) COLLATE utf8_bin NOT NULL,
  `sharding_item` varchar(100) COLLATE utf8_bin NOT NULL,
  `state` varchar(20) COLLATE utf8_bin NOT NULL,
  `message` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `creation_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `TASK_ID_STATE_INDEX` (`task_id`,`state`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of JOB_STATUS_TRACE_LOG
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for pradar_app_agent
-- ----------------------------
DROP TABLE IF EXISTS `pradar_app_agent`;
CREATE TABLE `pradar_app_agent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_group_id` bigint(20) DEFAULT NULL COMMENT '主机IP',
  `ip` varchar(64) DEFAULT NULL COMMENT '主机IP PORT',
  `machine_room` varchar(128) DEFAULT NULL COMMENT '机器名',
  `host_port` varchar(16) DEFAULT NULL COMMENT '主机IP',
  `hostname` varchar(256) DEFAULT NULL COMMENT '主机名称',
  `agent_status` tinyint(4) DEFAULT NULL COMMENT 'agent状态，1：已上线，2：暂停中，3：已下线',
  `agent_version` varchar(32) DEFAULT NULL COMMENT 'agent版本',
  `create_time` datetime DEFAULT NULL COMMENT '插入时间',
  `modify_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `deleted` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用状态表';

-- ----------------------------
-- Records of pradar_app_agent
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for pradar_app_bamp
-- ----------------------------
DROP TABLE IF EXISTS `pradar_app_bamp`;
CREATE TABLE `pradar_app_bamp` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_point_id` bigint(20) DEFAULT NULL COMMENT '埋点ip',
  `bamp_interval` int(11) DEFAULT NULL COMMENT '间隔，单位min',
  `index_code` varchar(128) DEFAULT NULL COMMENT '指标编码',
  `rt_avg` int(11) DEFAULT NULL COMMENT '响应耗时',
  `deleted` tinyint(1) DEFAULT NULL,
  `gmt_created` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='出数系统';

-- ----------------------------
-- Records of pradar_app_bamp
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for pradar_app_group
-- ----------------------------
DROP TABLE IF EXISTS `pradar_app_group`;
CREATE TABLE `pradar_app_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '组ID',
  `app_info_id` bigint(20) DEFAULT NULL COMMENT '关联应用ID',
  `group_name` varchar(256) DEFAULT NULL COMMENT '组名称',
  `domain_name` varchar(256) DEFAULT NULL COMMENT '域名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(4) DEFAULT NULL COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pradar_app_group
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for pradar_app_info
-- ----------------------------
DROP TABLE IF EXISTS `pradar_app_info`;
CREATE TABLE `pradar_app_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(128) DEFAULT '' COMMENT '应用名',
  `manager_name` varchar(32) DEFAULT NULL COMMENT '负责人姓名',
  `product_line` varchar(256) NOT NULL COMMENT '产品线',
  `app_group` varchar(256) DEFAULT NULL COMMENT '分组',
  `host_ip` varchar(64) DEFAULT NULL COMMENT '主机IP',
  `modify_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '插入时间',
  `deleted` tinyint(4) DEFAULT NULL COMMENT '0：未删除 1：删除',
  `PE` varchar(64) DEFAULT NULL COMMENT 'PE',
  `app_manager` varchar(64) DEFAULT NULL COMMENT '应用管理员',
  `SCM` varchar(64) DEFAULT NULL COMMENT 'SCM管理员',
  `DBA` varchar(64) DEFAULT NULL COMMENT 'DBA',
  `job_number` varchar(20) DEFAULT NULL COMMENT '工号',
  `sms_number` varchar(20) DEFAULT NULL COMMENT '电话号码',
  `reverser_registration` varchar(2) DEFAULT NULL COMMENT '是否反向注册:null:否;1:否;2:是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用基本信息表';

-- ----------------------------
-- Records of pradar_app_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for pradar_app_point
-- ----------------------------
DROP TABLE IF EXISTS `pradar_app_point`;
CREATE TABLE `pradar_app_point` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '埋点ID',
  `app_info_id` bigint(20) DEFAULT NULL COMMENT '应用ID',
  `method` varchar(1024) DEFAULT NULL COMMENT '埋点方法',
  `method_comment` varchar(4096) DEFAULT NULL COMMENT '删除标识',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `deleted` tinyint(4) DEFAULT NULL COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pradar_app_point
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for pradar_app_warn
-- ----------------------------
DROP TABLE IF EXISTS `pradar_app_warn`;
CREATE TABLE `pradar_app_warn` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_point_id` bigint(20) DEFAULT NULL COMMENT '埋点ip',
  `warn_interval` int(11) DEFAULT NULL COMMENT '告警间隔，单位min',
  `span_threshold` int(11) DEFAULT NULL COMMENT '阈值：触发告警,单位ms',
  `span_frequency` int(11) DEFAULT NULL COMMENT '耗时触发频率，单位次',
  `error_frequency` int(11) DEFAULT NULL COMMENT '错误触发频率,单位次',
  `warn_level` tinyint(1) DEFAULT NULL COMMENT '告警级别，1：error， 2：warning',
  `deleted` tinyint(1) DEFAULT NULL,
  `gmt_created` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `app_name` varchar(128) DEFAULT '' COMMENT '应用名',
  `method` varchar(1024) DEFAULT NULL COMMENT '埋点方法',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='告警';

-- ----------------------------
-- Records of pradar_app_warn
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for pradar_biz_key_config
-- ----------------------------
DROP TABLE IF EXISTS `pradar_biz_key_config`;
CREATE TABLE `pradar_biz_key_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `biz_key` varchar(200) DEFAULT NULL COMMENT '业务key,不区分大小写',
  `create_time` datetime DEFAULT NULL COMMENT '插入时间',
  `modify_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `deleted` tinyint(1) DEFAULT NULL COMMENT '1，有效，0，删除',
  PRIMARY KEY (`id`),
  KEY `idx_biz_key` (`biz_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务字段采集配置表';

-- ----------------------------
-- Records of pradar_biz_key_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for pradar_user_login
-- ----------------------------
DROP TABLE IF EXISTS `pradar_user_login`;
CREATE TABLE `pradar_user_login` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(64) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `dept` varchar(256) DEFAULT NULL,
  `user_type` tinyint(1) DEFAULT NULL COMMENT '1：测试， 2：开发， 3：运维， 4：管理',
  `is_deleted` tinyint(1) DEFAULT NULL,
  `gmt_created` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户登录表';

-- ----------------------------
-- Records of pradar_user_login
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_abstract_data
-- ----------------------------
DROP TABLE IF EXISTS `t_abstract_data`;
CREATE TABLE `t_abstract_data` (
  `TAD_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '抽数表id',
  `TDC_ID` bigint(20) NOT NULL COMMENT '数据库表配置id',
  `TABLE_NAME` varchar(500) NOT NULL COMMENT '数据表名',
  `SQL_DDl` text NOT NULL COMMENT '建表语句',
  `ABSTRACT_SQL` tinytext NOT NULL COMMENT '取数逻辑sql(存储文件路径或者sql语句)',
  `DEAL_SQL` tinytext NOT NULL COMMENT '处理数据逻辑sql(存储文件路径或者sql语句)',
  `PRINCIPAL_NO` varchar(10) DEFAULT NULL COMMENT '负责人工号',
  `SQL_TYPE` int(1) NOT NULL DEFAULT '0' COMMENT '0表示sql类型为纯sql输入,1表示为文本类型',
  `DB_STATUS` int(1) DEFAULT '1' COMMENT '数据状态(0代表删除,1代表使用,默认为1)',
  `USE_YN` int(1) DEFAULT '1' COMMENT '该表抽数启用状态(0表示未启用,1表示启用,默认启用)',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '插入时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '变更时间',
  `LOAD_STATUS` int(1) DEFAULT '0' COMMENT '抽数状态(0表示未开始,1表示正在运行,2表示运行成功,3停止运行,4表示运行失败)',
  PRIMARY KEY (`TAD_ID`) USING BTREE,
  KEY `TAD_INDEX1` (`TABLE_NAME`(191)) USING BTREE,
  KEY `TAD_INDEX2` (`PRINCIPAL_NO`) USING BTREE,
  KEY `TAD_INDEX3` (`CREATE_TIME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抽数表';

-- ----------------------------
-- Records of t_abstract_data
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_ac_account
-- ----------------------------
DROP TABLE IF EXISTS `t_ac_account`;
CREATE TABLE `t_ac_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uid` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除',
  `tags` bigint(20) DEFAULT '0' COMMENT '标签',
  `features` longtext COMMENT '扩展字段',
  `gmt_create` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `gmt_update` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `IDX_ACCOUNT_UID` (`uid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8 COMMENT='账户表';

-- ----------------------------
-- Records of t_ac_account
-- ----------------------------
BEGIN;
INSERT INTO `t_ac_account` VALUES (100, 1, 0, 0, 0, NULL, '2020-09-09 10:49:47.730', '2020-09-09 10:49:47.730');
COMMIT;

-- ----------------------------
-- Table structure for t_ac_account_balance
-- ----------------------------
DROP TABLE IF EXISTS `t_ac_account_balance`;
CREATE TABLE `t_ac_account_balance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `acc_id` bigint(20) DEFAULT NULL COMMENT '账户ID（外键）',
  `book_id` bigint(20) DEFAULT NULL COMMENT '账本ID（外键）',
  `amount` decimal(25,5) DEFAULT '0.00000' COMMENT '当前发生金额',
  `balance` decimal(25,5) DEFAULT '0.00000' COMMENT '可用余额',
  `lock_balance` decimal(25,5) DEFAULT '0.00000' COMMENT '冻结余额',
  `subject` int(11) DEFAULT NULL COMMENT '账本科目',
  `direct` tinyint(4) DEFAULT NULL COMMENT '记账方向',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `parent_book_id` bigint(20) DEFAULT '0' COMMENT '父类ID',
  `scene_code` varchar(30) DEFAULT NULL COMMENT '业务代码',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `acc_time` datetime DEFAULT NULL COMMENT '记账时间',
  `outer_id` varchar(100) DEFAULT NULL COMMENT '外部交易资金流水NO',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除',
  `tags` bigint(20) DEFAULT '0' COMMENT '标签',
  `features` longtext COMMENT '扩展字段',
  `gmt_create` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `gmt_update` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `UK_ACCOUNT_BALANCE_BOOK_ID_OUTER_ID_SCENE_CODE` (`book_id`,`outer_id`,`scene_code`) USING BTREE,
  KEY `IDX_ACCOUNT_BALANCE_ACC_ID_BOOK_ID` (`acc_id`,`book_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账户账本明细表';

-- ----------------------------
-- Records of t_ac_account_balance
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_ac_account_book
-- ----------------------------
DROP TABLE IF EXISTS `t_ac_account_book`;
CREATE TABLE `t_ac_account_book` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uid` bigint(20) DEFAULT NULL COMMENT '用户ID（外键）',
  `acc_id` bigint(20) DEFAULT NULL COMMENT '账户ID（外键）',
  `parent_book_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '父类ID',
  `balance` decimal(25,5) DEFAULT '0.00000' COMMENT '余额',
  `lock_balance` decimal(25,5) DEFAULT '0.00000' COMMENT '冻结金额',
  `total_balance` decimal(25,5) DEFAULT '0.00000' COMMENT '总金额',
  `subject` int(11) DEFAULT NULL COMMENT '科目',
  `direct` tinyint(4) DEFAULT NULL COMMENT '记账方向，借或贷',
  `rule` varchar(500) DEFAULT NULL COMMENT '规则',
  `rule_balance` decimal(25,5) DEFAULT '0.00000' COMMENT '规则余额',
  `start_time` datetime(3) DEFAULT NULL COMMENT '生效时间',
  `end_time` datetime(3) DEFAULT NULL COMMENT '失效时间',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `version` int(10) DEFAULT NULL COMMENT '版本',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除',
  `tags` bigint(20) DEFAULT '0' COMMENT '标签',
  `features` longtext COMMENT '扩展字段',
  `gmt_create` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `gmt_update` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `IDX_ACCOUNT_BOOK_ACC_ID` (`acc_id`) USING BTREE,
  KEY `IDX_ACCOUNT_BOOK_UID` (`uid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8 COMMENT='账户账本表';

-- ----------------------------
-- Records of t_ac_account_book
-- ----------------------------
BEGIN;
INSERT INTO `t_ac_account_book` VALUES (100, 1, 100, 0, 0.00000, 0.00000, 0.00000, NULL, NULL, NULL, 0.00000, NULL, NULL, NULL, NULL, 0, 0, NULL, '2020-09-09 10:49:47.733', '2020-09-09 10:49:47.733');
COMMIT;

-- ----------------------------
-- Table structure for t_agent_plugin
-- ----------------------------
DROP TABLE IF EXISTS `t_agent_plugin`;
CREATE TABLE `t_agent_plugin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `plugin_type` varchar(50) DEFAULT NULL COMMENT '中间件类型：HTTP_CLIENT、JDBC、ORM、DB、JOB、MESSAGE、CACHE、POOL、JNDI、NO_SQL、RPC、SEARCH、MQ、SERIALIZE、OTHER',
  `plugin_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '中间件名称：Redis、Mysql、Es',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0: 正常 1： 删除',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_update` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_agent_plugin
-- ----------------------------
BEGIN;
INSERT INTO `t_agent_plugin` VALUES (1, 'HTTP_CLIENT', 'http-client', 0, '2020-10-13 10:20:04', '2020-10-20 10:10:57');
INSERT INTO `t_agent_plugin` VALUES (2, 'JDBC', 'jdbc', 0, '2020-10-13 10:20:04', '2020-10-20 10:10:57');
INSERT INTO `t_agent_plugin` VALUES (3, 'ORM', 'mybatis', 0, '2020-10-13 10:20:04', '2020-10-23 09:49:11');
INSERT INTO `t_agent_plugin` VALUES (6, 'MESSAGE', 'pulsar', 0, '2020-10-13 10:20:04', '2020-10-23 10:36:43');
INSERT INTO `t_agent_plugin` VALUES (7, 'CACHE', 'cache', 0, '2020-10-13 10:20:04', '2020-10-23 10:59:43');
INSERT INTO `t_agent_plugin` VALUES (9, 'JNDI', 'jndi', 0, '2020-10-13 10:20:04', '2020-10-23 11:06:33');
INSERT INTO `t_agent_plugin` VALUES (10, 'NO_SQL', 'aerospike', 0, '2020-10-13 10:20:04', '2020-10-23 11:07:23');
INSERT INTO `t_agent_plugin` VALUES (11, 'RPC', 'dubbo', 0, '2020-10-13 10:20:04', '2020-10-23 11:07:59');
INSERT INTO `t_agent_plugin` VALUES (12, 'SEARCH', 'es', 0, '2020-10-13 10:20:04', '2020-10-23 11:12:00');
INSERT INTO `t_agent_plugin` VALUES (13, 'MQ', 'rocketmq', 0, '2020-10-13 10:20:04', '2020-10-23 11:13:37');
INSERT INTO `t_agent_plugin` VALUES (14, 'SERIALIZE', 'hessian', 0, '2020-10-13 10:20:04', '2020-10-23 11:15:47');
INSERT INTO `t_agent_plugin` VALUES (15, 'MQ', 'ons', 0, '2020-10-13 10:20:04', '2020-10-21 10:09:41');
INSERT INTO `t_agent_plugin` VALUES (16, 'MQ', 'kafka', 0, '2020-10-13 10:20:04', '2020-10-23 11:20:13');
INSERT INTO `t_agent_plugin` VALUES (17, 'MQ', 'rabbit', 0, '2020-10-13 10:20:04', '2020-10-23 11:22:05');
INSERT INTO `t_agent_plugin` VALUES (18, 'MQ', 'jms', 0, '2020-10-13 10:20:04', '2020-10-21 10:06:34');
INSERT INTO `t_agent_plugin` VALUES (19, 'HTTP_CLIENT', 'google-http', 0, '2020-10-13 10:20:04', '2020-10-20 10:10:57');
INSERT INTO `t_agent_plugin` VALUES (20, 'HTTP_CLIENT', 'okhttp', 0, '2020-10-13 10:20:04', '2020-10-20 10:10:57');
INSERT INTO `t_agent_plugin` VALUES (21, 'HTTP_CLIENT', 'grizzly', 0, '2020-10-13 10:20:04', '2020-10-20 10:10:57');
INSERT INTO `t_agent_plugin` VALUES (22, 'JDBC', 'ojdbc', 0, '2020-10-13 10:20:04', '2020-10-20 10:10:57');
INSERT INTO `t_agent_plugin` VALUES (23, 'DB', 'alibase', 0, '2020-10-13 10:20:04', '2020-10-20 10:10:57');
INSERT INTO `t_agent_plugin` VALUES (24, 'DB', 'oss', 0, '2020-10-13 10:20:04', '2020-10-20 10:10:57');
INSERT INTO `t_agent_plugin` VALUES (25, 'DB', 'mongodb', 0, '2020-10-13 10:20:04', '2020-10-20 10:10:57');
INSERT INTO `t_agent_plugin` VALUES (26, 'DB', 'neo4j', 0, '2020-10-13 10:20:04', '2020-10-20 10:10:57');
INSERT INTO `t_agent_plugin` VALUES (27, 'JOB', 'saturn', 0, '2020-10-13 10:20:04', '2020-10-20 10:10:57');
INSERT INTO `t_agent_plugin` VALUES (28, 'JOB', 'xxl-job', 0, '2020-10-13 10:20:04', '2020-10-20 10:10:57');
INSERT INTO `t_agent_plugin` VALUES (29, 'CACHE', 'jetcache', 0, '2020-10-13 10:20:04', '2020-10-20 10:10:57');
INSERT INTO `t_agent_plugin` VALUES (30, 'SERVLET_CONTAINER', 'jetty', 0, '2020-10-23 10:53:18', '2020-10-23 10:53:18');
INSERT INTO `t_agent_plugin` VALUES (31, 'CACHE', 'redis', 0, '2020-10-13 10:20:04', '2020-10-20 10:10:57');
INSERT INTO `t_agent_plugin` VALUES (32, 'CACHE', 'jedis', 0, '2020-10-23 10:56:26', '2020-10-23 10:56:30');
INSERT INTO `t_agent_plugin` VALUES (33, 'CACHE', 'lettuce', 0, '2020-10-23 10:56:54', '2020-10-23 10:57:04');
INSERT INTO `t_agent_plugin` VALUES (34, 'CACHE', 'redisson', 0, '2020-10-23 10:58:35', '2020-10-23 10:58:38');
INSERT INTO `t_agent_plugin` VALUES (35, 'POOL', 'druid连接池', 0, '2020-10-23 11:00:55', '2020-10-23 11:00:55');
INSERT INTO `t_agent_plugin` VALUES (36, 'POOL', 'tomcat连接池', 0, '2020-10-23 11:02:27', '2020-10-23 11:02:29');
INSERT INTO `t_agent_plugin` VALUES (37, 'POOL', 'transactions连接池', 0, '2020-10-23 11:03:08', '2020-10-23 11:03:11');
INSERT INTO `t_agent_plugin` VALUES (38, 'POOL', 'c3p0连接池', 0, '2020-10-23 11:03:59', '2020-10-23 11:04:02');
INSERT INTO `t_agent_plugin` VALUES (39, 'POOL', 'dbcp连接池', 0, '2020-10-23 11:04:29', '2020-10-23 11:04:32');
INSERT INTO `t_agent_plugin` VALUES (40, 'POOL', 'dbcp2连接池', 0, '2020-10-23 11:04:47', '2020-10-23 11:04:53');
INSERT INTO `t_agent_plugin` VALUES (41, 'POOL', 'HikariCP连接池', 0, '2020-10-23 11:05:42', '2020-10-23 11:05:42');
INSERT INTO `t_agent_plugin` VALUES (42, 'RPC', 'grpc', 0, '2020-10-23 11:08:50', '2020-10-23 11:08:50');
INSERT INTO `t_agent_plugin` VALUES (43, 'RPC', 'motan', 0, '2020-10-23 11:09:39', '2020-10-23 11:09:39');
INSERT INTO `t_agent_plugin` VALUES (44, 'MQ', 'spring-kafka', 0, '2020-10-23 11:20:07', '2020-10-23 11:20:07');
INSERT INTO `t_agent_plugin` VALUES (45, 'OTHER', '-', 0, '2020-10-23 17:31:54', '2020-10-23 17:31:54');
COMMIT;

-- ----------------------------
-- Table structure for t_agent_plugin_lib_support
-- ----------------------------
DROP TABLE IF EXISTS `t_agent_plugin_lib_support`;
CREATE TABLE `t_agent_plugin_lib_support` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `plugin_id` bigint(20) NOT NULL COMMENT '插件id',
  `lib_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'jar包名称',
  `lib_version_regexp` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'agent支持的中间件版本的正则表达式',
  `is_ignore` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0: 不忽略 1： 忽略',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0: 正常 1： 删除',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_update` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uni_libname_index` (`lib_name`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_agent_plugin_lib_support
-- ----------------------------
BEGIN;
INSERT INTO `t_agent_plugin_lib_support` VALUES (1, 19, 'google-http-client', '[\"1.20.0\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 09:42:16');
INSERT INTO `t_agent_plugin_lib_support` VALUES (2, 1, 'httpclient', '[\"4.4.1\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-21 10:59:01');
INSERT INTO `t_agent_plugin_lib_support` VALUES (4, 20, 'okhttp', '[\"2.0.0\",\"3.8.1\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 09:42:24');
INSERT INTO `t_agent_plugin_lib_support` VALUES (5, 22, 'ojdbc14', '[\"10.2.0.3.0\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 09:47:55');
INSERT INTO `t_agent_plugin_lib_support` VALUES (6, 3, 'mybatis', '[\"3.4.2\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-21 11:01:39');
INSERT INTO `t_agent_plugin_lib_support` VALUES (7, 23, 'alihbase-client', '[\"2.0.3\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 09:52:18');
INSERT INTO `t_agent_plugin_lib_support` VALUES (10, 24, 'aliyun-sdk-oss', '[\"3.5.0\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 10:17:56');
INSERT INTO `t_agent_plugin_lib_support` VALUES (11, 27, 'saturn-core', '[\"3.5.0\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 10:31:10');
INSERT INTO `t_agent_plugin_lib_support` VALUES (12, 28, 'xxl-job-core', '[\"2.1.2\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 10:31:12');
INSERT INTO `t_agent_plugin_lib_support` VALUES (13, 6, 'pulsar-client', '[\"2.4.0\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-21 11:03:21');
INSERT INTO `t_agent_plugin_lib_support` VALUES (14, 29, 'jetcache-anno', '[\"2.6.0.M2\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 10:40:15');
INSERT INTO `t_agent_plugin_lib_support` VALUES (15, 30, 'jetty-server', '[\"9.2.11.v20150529\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 10:53:29');
INSERT INTO `t_agent_plugin_lib_support` VALUES (16, 30, 'jetty-servlet', '[\"9.2.11.v20150529\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 10:53:31');
INSERT INTO `t_agent_plugin_lib_support` VALUES (17, 32, 'jedis', '[\"3.1.0\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 10:56:35');
INSERT INTO `t_agent_plugin_lib_support` VALUES (18, 33, 'lettuce-core', '[\"5.1.4.RELEASE\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 10:57:20');
INSERT INTO `t_agent_plugin_lib_support` VALUES (19, 31, 'spring-boot-starter-data-redis', '[\"2.1.7.RELEASE\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 10:58:20');
INSERT INTO `t_agent_plugin_lib_support` VALUES (20, 34, 'redisson', '[\"3.13.2\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 10:59:24');
INSERT INTO `t_agent_plugin_lib_support` VALUES (21, 7, 'spring-boot-starter-cache', '[\"2.1.1.RELEASE\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-21 11:05:41');
INSERT INTO `t_agent_plugin_lib_support` VALUES (22, 35, 'druid', '[\"1.1.14\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 17:50:19');
INSERT INTO `t_agent_plugin_lib_support` VALUES (23, 36, 'tomcat-jdbc', '[\"8.5.20\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 11:02:36');
INSERT INTO `t_agent_plugin_lib_support` VALUES (24, 37, 'transactions-jdbc', '[\"4.0.6\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 11:03:15');
INSERT INTO `t_agent_plugin_lib_support` VALUES (25, 38, 'c3p0', '[\"c3p0-0.9.5.2.jar\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 11:04:07');
INSERT INTO `t_agent_plugin_lib_support` VALUES (26, 39, 'dbcp', '[\"dbcp\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 11:04:40');
INSERT INTO `t_agent_plugin_lib_support` VALUES (27, 40, 'commons-dbcp2', '[\"2.7.0\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 11:05:01');
INSERT INTO `t_agent_plugin_lib_support` VALUES (28, 41, 'HikariCP', '[\"3.1.0\",\"2.7.9\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 11:05:47');
INSERT INTO `t_agent_plugin_lib_support` VALUES (29, 9, 'simple-jndi', '[\"0.11.4.1\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-21 11:04:07');
INSERT INTO `t_agent_plugin_lib_support` VALUES (30, 10, 'aerospike-client', '[\"3.2.2\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-21 11:07:26');
INSERT INTO `t_agent_plugin_lib_support` VALUES (31, 11, 'dubbo', '[\"2.8.4\",\"2.7.*\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-21 11:07:47');
INSERT INTO `t_agent_plugin_lib_support` VALUES (32, 42, 'grpc-core', '[\"1.14.0\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 11:08:59');
INSERT INTO `t_agent_plugin_lib_support` VALUES (33, 43, 'motan-core', '[\"0.3.1\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 11:09:43');
INSERT INTO `t_agent_plugin_lib_support` VALUES (34, 12, 'elasticsearch', '[\"6.8.6\",\"2.4.1\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-21 11:10:05');
INSERT INTO `t_agent_plugin_lib_support` VALUES (35, 13, 'rocketmq-client', '[\"3.2.6\",\"4.*.*\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-21 10:49:58');
INSERT INTO `t_agent_plugin_lib_support` VALUES (36, 15, 'ons-client', '[\"1.8.0.Final\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 11:14:33');
INSERT INTO `t_agent_plugin_lib_support` VALUES (37, 16, 'kafka-clients', '[\"0.8.0.0\",\"2.2.0\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-21 11:08:49');
INSERT INTO `t_agent_plugin_lib_support` VALUES (38, 44, 'spring-kafka', '[\"2.3.1.RELEASE\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 11:21:04');
INSERT INTO `t_agent_plugin_lib_support` VALUES (39, 17, 'spring-rabbit', '[\"1.7.0.RELEASE\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-21 11:09:08');
INSERT INTO `t_agent_plugin_lib_support` VALUES (40, 18, 'spring-jms', '[\"4.1.9.RELEASE\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-21 11:09:22');
INSERT INTO `t_agent_plugin_lib_support` VALUES (41, 14, 'hessian', '[\"4.0.63\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-21 11:09:31');
INSERT INTO `t_agent_plugin_lib_support` VALUES (42, 1, 'commons-httpclient', '[\"3.1\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-21 10:58:07');
INSERT INTO `t_agent_plugin_lib_support` VALUES (43, 29, 'jetcache-redis', '[\"2.6.0.M2\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 10:40:33');
INSERT INTO `t_agent_plugin_lib_support` VALUES (44, 43, 'motan-springsupport', '[\"0.3.1\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 11:09:44');
INSERT INTO `t_agent_plugin_lib_support` VALUES (45, 21, 'grizzly-framework', '[\"2.3.21\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 09:44:27');
INSERT INTO `t_agent_plugin_lib_support` VALUES (46, 21, 'grizzly-http-server', '[\"2.3.21\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 09:44:29');
INSERT INTO `t_agent_plugin_lib_support` VALUES (47, 21, 'grizzly-http', '[\"2.3.21\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 09:44:29');
INSERT INTO `t_agent_plugin_lib_support` VALUES (48, 21, 'grizzly-websockets', '[\"2.3.21\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 09:44:30');
INSERT INTO `t_agent_plugin_lib_support` VALUES (49, 25, 'mongodb-driver', '[\"3.11.2\",\"3.8.2\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 10:21:25');
INSERT INTO `t_agent_plugin_lib_support` VALUES (50, 25, 'spring-boot-starter-data-mongodb', '[\"1.5.10.RELEASE\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 10:20:39');
INSERT INTO `t_agent_plugin_lib_support` VALUES (51, 26, 'neo4j-ogm-compiler', '[\"2.0.5\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 10:26:24');
INSERT INTO `t_agent_plugin_lib_support` VALUES (52, 26, 'nneo4j-ogm-embedded-driver', '[\"2.0.5\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 10:26:25');
INSERT INTO `t_agent_plugin_lib_support` VALUES (53, 26, 'neo4j-ogm-bolt-driver', '[\"2.0.5\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 10:26:26');
INSERT INTO `t_agent_plugin_lib_support` VALUES (54, 26, 'neo4j-ogm-http-driver', '[\"2.0.5\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 10:26:27');
INSERT INTO `t_agent_plugin_lib_support` VALUES (55, 26, 'neo4j-ogm-core', '[\"2.0.5\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 10:26:28');
INSERT INTO `t_agent_plugin_lib_support` VALUES (56, 25, 'mongo-java-driver', '[\"3.2.2\"]', 0, 0, '2020-10-13 10:21:25', '2020-10-23 10:21:25');
INSERT INTO `t_agent_plugin_lib_support` VALUES (57, 45, 'lombok', '[\"1.18.6\"]', 1, 0, '2020-10-23 17:32:51', '2020-10-23 17:32:51');
COMMIT;

-- ----------------------------
-- Table structure for t_alarm_list
-- ----------------------------
DROP TABLE IF EXISTS `t_alarm_list`;
CREATE TABLE `t_alarm_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `war_packages` varchar(2000) COLLATE utf8_bin DEFAULT NULL COMMENT 'WAR包名',
  `ip` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT 'IP',
  `alarm_collects` varchar(2000) COLLATE utf8_bin DEFAULT NULL COMMENT '告警汇总',
  `alarm_date` datetime DEFAULT NULL COMMENT '告警时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` int(2) NOT NULL DEFAULT '0' COMMENT '是否已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='告警列表';

-- ----------------------------
-- Records of t_alarm_list
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_app_business_table_info
-- ----------------------------
DROP TABLE IF EXISTS `t_app_business_table_info`;
CREATE TABLE `t_app_business_table_info` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `APPLICATION_ID` bigint(19) NOT NULL COMMENT '应用ID',
  `table_name` text COMMENT 'jar名称',
  `url` varchar(255) DEFAULT NULL COMMENT 'Pradar插件名称',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `IS_DELETED` tinyint(1) DEFAULT '0' COMMENT '是否有效 0:有效;1:无效',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用业务表';

-- ----------------------------
-- Records of t_app_business_table_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_app_middleware_info
-- ----------------------------
DROP TABLE IF EXISTS `t_app_middleware_info`;
CREATE TABLE `t_app_middleware_info` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `APPLICATION_ID` bigint(19) NOT NULL COMMENT '应用ID',
  `JAR_NAME` varchar(20) DEFAULT NULL COMMENT 'jar名称',
  `PLUGIN_NAME` varchar(20) DEFAULT NULL COMMENT 'Pradar插件名称',
  `JAR_TYPE` varchar(20) DEFAULT NULL COMMENT 'Jar类型',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `ACTIVE` tinyint(1) DEFAULT '0' COMMENT '是否增强成功 0:有效;1:未生效',
  `HIDDEN` tinyint(1) DEFAULT '0' COMMENT '是否隐藏 0:隐藏;1:不隐藏',
  `IS_DELETED` tinyint(1) DEFAULT '0' COMMENT '是否有效 0:有效;1:无效',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用中间件列表信息';

-- ----------------------------
-- Records of t_app_middleware_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_application_api_manage
-- ----------------------------
DROP TABLE IF EXISTS `t_application_api_manage`;
CREATE TABLE `t_application_api_manage` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `APPLICATION_ID` bigint(20) DEFAULT NULL COMMENT '应用主键',
  `APPLICATION_NAME` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '应用名称',
  `CUSTOMER_ID` bigint(20) DEFAULT NULL COMMENT '租户id',
  `USER_ID` bigint(20) DEFAULT NULL COMMENT '用户id',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `IS_DELETED` tinyint(4) DEFAULT '0' COMMENT '是否有效 0:有效;1:无效',
  `api` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT 'api',
  `method` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '请求类型',
  `IS_AGENT_REGISTE` tinyint(4) DEFAULT '0' COMMENT '是否有效 0:否;1:是',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_application_api_manage
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_application_ds_manage
-- ----------------------------
DROP TABLE IF EXISTS `t_application_ds_manage`;
CREATE TABLE `t_application_ds_manage` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `APPLICATION_ID` bigint(20) DEFAULT NULL COMMENT '应用主键',
  `APPLICATION_NAME` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '应用名称',
  `DB_TYPE` tinyint(2) DEFAULT '0' COMMENT '存储类型 0:数据库 1:缓存',
  `DS_TYPE` tinyint(2) DEFAULT '0' COMMENT '方案类型 0:影子库 1:影子表 2:影子server',
  `URL` varchar(250) COLLATE utf8_bin DEFAULT NULL COMMENT '数据库url,影子表需填',
  `CONFIG` longtext COLLATE utf8_bin COMMENT 'xml配置',
  `PARSE_CONFIG` longtext COLLATE utf8_bin COMMENT '解析后配置',
  `STATUS` tinyint(2) DEFAULT '0' COMMENT '状态 0:启用；1:禁用',
  `CUSTOMER_ID` bigint(20) DEFAULT NULL COMMENT '租户id',
  `USER_ID` bigint(20) DEFAULT NULL COMMENT '用户id',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `IS_DELETED` tinyint(4) DEFAULT '0' COMMENT '是否有效 0:有效;1:无效',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_app_id` (`APPLICATION_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_application_ds_manage
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_application_info_upload
-- ----------------------------
DROP TABLE IF EXISTS `t_application_info_upload`;
CREATE TABLE `t_application_info_upload` (
  `TAIU_ID` bigint(19) NOT NULL COMMENT '上传应用信息id',
  `APPLICATION_NAME` varchar(64) NOT NULL COMMENT '应用名称',
  `INFO_TYPE` int(2) NOT NULL COMMENT '1 堆栈 2 SQL 异常',
  `UPLOAD_INFO` text COMMENT '保存的信息',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '插入时间',
  PRIMARY KEY (`TAIU_ID`) USING BTREE,
  KEY `app_info_upload_index1` (`CREATE_TIME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用信息上传';

-- ----------------------------
-- Records of t_application_info_upload
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_application_ip
-- ----------------------------
DROP TABLE IF EXISTS `t_application_ip`;
CREATE TABLE `t_application_ip` (
  `ID` varchar(40) COLLATE utf8_bin NOT NULL,
  `APPLICATION_NAME` varchar(300) COLLATE utf8_bin DEFAULT NULL,
  `TYPE` varchar(300) COLLATE utf8_bin DEFAULT NULL,
  `IP` varchar(300) COLLATE utf8_bin DEFAULT NULL,
  `SYSTEM_NAME` varchar(300) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `IDX_T_APP_ID` (`APPLICATION_NAME`(255)) USING BTREE,
  KEY `IDX_T_APP_ID2` (`APPLICATION_NAME`(255)) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='应用ip配置表';

-- ----------------------------
-- Records of t_application_ip
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_application_mnt
-- ----------------------------
DROP TABLE IF EXISTS `t_application_mnt`;
CREATE TABLE `t_application_mnt` (
  `APPLICATION_ID` bigint(19) NOT NULL COMMENT '应用id',
  `APPLICATION_NAME` varchar(50) NOT NULL COMMENT '应用名称',
  `APPLICATION_DESC` varchar(200) DEFAULT NULL COMMENT '应用说明',
  `DDL_SCRIPT_PATH` varchar(200) NOT NULL COMMENT '影子库表结构脚本路径',
  `CLEAN_SCRIPT_PATH` varchar(200) NOT NULL COMMENT '数据清理脚本路径',
  `READY_SCRIPT_PATH` varchar(200) NOT NULL COMMENT '基础数据准备脚本路径',
  `BASIC_SCRIPT_PATH` varchar(200) NOT NULL COMMENT '铺底数据脚本路径',
  `CACHE_SCRIPT_PATH` varchar(200) NOT NULL COMMENT '缓存预热脚本地址',
  `CACHE_EXP_TIME` bigint(19) NOT NULL DEFAULT '0' COMMENT '缓存失效时间(单位秒)',
  `USE_YN` int(1) DEFAULT NULL COMMENT '是否可用(0表示启用,1表示未启用)',
  `AGENT_VERSION` varchar(16) DEFAULT NULL COMMENT 'java agent版本',
  `NODE_NUM` int(4) NOT NULL DEFAULT '1' COMMENT '节点数量',
  `ACCESS_STATUS` int(2) NOT NULL DEFAULT '1' COMMENT '接入状态； 0：正常 ； 1；待配置 ；2：待检测 ; 3：异常 \n',
  `SWITCH_STATUS` varchar(255) NOT NULL DEFAULT 'OPENED' COMMENT 'OPENED："已开启",OPENING："开启中",OPEN_FAILING："开启异常",CLOSED："已关闭",CLOSING："关闭中",CLOSE_FAILING："关闭异常"',
  `EXCEPTION_INFO` text COMMENT '接入异常信息',
  `CREATE_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `ALARM_PERSON` varchar(64) DEFAULT NULL COMMENT '告警人',
  `PRADAR_VERSION` varchar(30) DEFAULT NULL COMMENT 'pradarAgent版本',
  `CUSTOMER_ID` bigint(20) DEFAULT NULL COMMENT '租户id',
  `USER_ID` bigint(11) DEFAULT NULL COMMENT '所属用户',
  PRIMARY KEY (`APPLICATION_ID`) USING BTREE,
  UNIQUE KEY `index_identifier_application_name` (`APPLICATION_NAME`,`CUSTOMER_ID`),
  KEY `T_APLICATION_MNT_INDEX1` (`APPLICATION_NAME`) USING BTREE,
  KEY `T_APLICATION_MNT_INDEX2` (`USE_YN`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用管理表';

-- ----------------------------
-- Records of t_application_mnt
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_application_mnt_config
-- ----------------------------
DROP TABLE IF EXISTS `t_application_mnt_config`;
CREATE TABLE `t_application_mnt_config` (
  `TAMC_ID` bigint(19) NOT NULL COMMENT '应用配置id',
  `APPLICATION_ID` bigint(19) NOT NULL COMMENT '应用id',
  `CHEAT_CHECK` int(1) NOT NULL DEFAULT '0' COMMENT '是否防作弊检查 0不检查 1 检查',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '插入时间',
  `UPDATE_TIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`TAMC_ID`) USING BTREE,
  UNIQUE KEY `t_application_mnt_config_INDEX1` (`APPLICATION_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用管理配置表';

-- ----------------------------
-- Records of t_application_mnt_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_base_config
-- ----------------------------
DROP TABLE IF EXISTS `t_base_config`;
CREATE TABLE `t_base_config` (
  `CONFIG_CODE` varchar(64) NOT NULL COMMENT '配置编码',
  `CONFIG_VALUE` longtext NOT NULL COMMENT '配置值',
  `CONFIG_DESC` varchar(128) NOT NULL COMMENT '配置说明',
  `USE_YN` int(1) DEFAULT '0' COMMENT '是否可用(0表示未启用,1表示启用)',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '插入时间',
  `UPDATE_TIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`CONFIG_CODE`) USING BTREE,
  UNIQUE KEY `unique_idx_config_code` (`CONFIG_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='tro配置表';

-- ----------------------------
-- Records of t_base_config
-- ----------------------------
BEGIN;
INSERT INTO `t_base_config` VALUES ('ES_SERVER', '{\"businessNodes\": \"192.168.1.210:9200,192.168.1.193:9200\",\"performanceTestNodes\": \"192.168.1.210:9200,192.168.1.193:9200\"}', '影子ES配置模板', 0, '2021-04-13 21:21:08', '2021-04-13 21:21:11');
INSERT INTO `t_base_config` VALUES ('Hbase_SERVER', '{\"dataSourceBusiness\":{ \"nodes\":\"192.168.2.241:6379,192.168.2.241:6380\", \"database\":\"aaaa\"},\"dataSourceBusinessPerformanceTest\":{ \"nodes\":\"192.168.1.241:6379,192.168.1.241:6380\", \"password\":\"123456\",  \"database\":\"aaaa\"}}', '影子Hbase配置模板', 0, '2021-04-13 21:23:02', '2021-04-13 21:23:05');
INSERT INTO `t_base_config` VALUES ('KAFKA_CLUSTER', '{\"key\": \"PT_业务主题\",\"topic\": \"PT_业务主题\",\"topicTokens\": \"PT_业务主题:影子主题token\",\"group\": \"\",\"systemIdToken\": \"\"}', '影子kafka集群配置模板', 0, '2021-04-27 09:12:40', '2021-04-27 09:12:40');
INSERT INTO `t_base_config` VALUES ('PRADAR_GUARD_TEMPLATE', 'import  com.example.demo.entity.User ;\nUser user = new User();\nuser.setName(\"挡板\");\nreturn user ;', '挡板模版', 1, NULL, '2020-06-09 10:58:15');
INSERT INTO `t_base_config` VALUES ('SHADOW_DB', '<configurations>\n              <!--数据源调停者-->\n              <datasourceMediator id=\"dbMediatorDataSource\">\n                  <property name=\"dataSourceBusiness\" ref=\"dataSourceBusiness\"/><!--业务数据源-->\n                  <property name=\"dataSourcePerformanceTest\" ref=\"dataSourcePerformanceTest\"/><!--压测数据源-->\n              </datasourceMediator>\n          \n              <!--数据源集合-->\n              <datasources>\n                  <datasource id=\"dataSourceBusiness\"><!--业务数据源--> <!--业务数据源只需要URL及用户名即可进行唯一性确认等验证-->\n                      <property name=\"url\" value=\"jdbc:mysql://127.0.0.1:3306/taco_app\"/><!--数据库连接URL-->\n                      <property name=\"username\" value=\"admin2017\"/><!--数据库连接用户名-->\n                  </datasource>\n                  <datasource id=\"dataSourcePerformanceTest\"><!--压测数据源-->\n                      <property name=\"driverClassName\" value=\"com.mysql.cj.jdbc.Driver\"/><!--数据库驱动-->\n                      <property name=\"url\" value=\"jdbc:mysql://127.0.0.1:3306/pt_taco_app\"/><!--数据库连接URL-->\n                      <property name=\"username\" value=\"admin2017\"/><!--数据库连接用户名-->\n                      <property name=\"password\" value=\"admin2017\"/><!--数据库连接密码-->\n                      <property name=\"initialSize\" value=\"5\"/>\n                      <property name=\"minIdle\" value=\"5\"/>\n                      <property name=\"maxActive\" value=\"20\"/>\n                      <property name=\"maxWait\" value=\"60000\"/>\n                      <property name=\"timeBetweenEvictionRunsMillis\" value=\"60000\"/>\n                      <property name=\"minEvictableIdleTimeMillis\" value=\"300000\"/>\n                      <property name=\"validationQuery\" value=\"SELECT 1 FROM DUAL\"/>\n                      <property name=\"testWhileIdle\" value=\"true\"/>\n                      <property name=\"testOnBorrow\" value=\"false\"/>\n                      <property name=\"testOnReturn\" value=\"false\"/>\n                      <property name=\"poolPreparedStatements\" value=\"true\"/>\n                      <property name=\"maxPoolPreparedStatementPerConnectionSize\" value=\"20\"/>\n                      <property name=\"connectionProperties\" value=\"druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500\"/>\n                  </datasource>\n              </datasources>\n          </configurations>', '影子库配置模板', 0, '2020-11-30 16:47:30', '2020-11-30 16:48:11');
INSERT INTO `t_base_config` VALUES ('SHADOW_SERVER', '{\n    \"dataSourceBusiness\":{\n        \"master\":\"192.168.2.240\",\n        \"nodes\":\"192.168.2.241:6379,192.168.2.241:6380\"\n\n    },\n    \"dataSourceBusinessPerformanceTest\":{\n      \"master\":\"192.168.2.240\",\n        \"nodes\":\"192.168.1.241:6379,192.168.1.241:6380\",\n        \"password\":\"123456\",\n\"database\":0\n    }\n}', '影子server配置模板', 0, '2020-11-30 16:48:35', '2020-12-02 14:03:21');
INSERT INTO `t_base_config` VALUES ('SHELL_SCRIPT_DOWNLOAD_SAMPLE', '[\n{\n    \"name\":\"影子库结构脚本\",\n        \"url\":\"/opt/tro/script/shell/sample/ddl.sh\"\n    },\n    {\n    \"name\":\"数据库清理脚本\",\n        \"url\":\"/opt/tro/script/shell/sample/clean.sh\"\n    },\n    {\n    \"name\":\"基础数据准备脚本\",\n        \"url\":\"/opt/tro/script/shell/sample/ready.sh\"\n    },\n    {\n    \"name\":\"铺底数据脚本\",\n        \"url\":\"/opt/tro/script/shell/sample/basic.sh\"\n    },\n    {\n    \"name\":\"缓存预热脚本\",\n        \"url\":\"/opt/tro/script/shell/sample/cache.sh\"\n    }\n\n]', 'shell脚本样例下载配置', 1, '2020-12-23 20:36:05', '2020-12-23 20:36:05');
INSERT INTO `t_base_config` VALUES ('SQL_CHECK', '0', '全应用SQL检查开关 1开启 0关闭', 1, '2019-03-28 22:11:18', '2019-11-04 10:49:00');
INSERT INTO `t_base_config` VALUES ('WHITE_LIST_SWITCH', '1', '白名单开关：0-关闭 1-开启', 0, NULL, '2021-01-28 00:23:57');
COMMIT;

-- ----------------------------
-- Table structure for t_black_list
-- ----------------------------
DROP TABLE IF EXISTS `t_black_list`;
CREATE TABLE `t_black_list` (
  `BLIST_ID` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `REDIS_KEY` varchar(50) DEFAULT NULL COMMENT 'redis的键',
  `PRINCIPAL_NO` varchar(10) DEFAULT NULL COMMENT '负责人工号(废弃不用)',
  `USE_YN` int(1) DEFAULT NULL COMMENT '是否可用(0表示未启动,1表示启动,2表示启用未校验)',
  `CUSTOMER_ID` bigint(20) DEFAULT NULL COMMENT '租户id',
  `USER_ID` bigint(20) DEFAULT NULL COMMENT '用户id',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '插入时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '变更时间',
  `type` tinyint(4) DEFAULT NULL COMMENT '黑名单类型',
  `value` varchar(1024) DEFAULT NULL COMMENT '黑名单数据',
  `APPLICATION_ID` bigint(19) NOT NULL COMMENT '应用id',
  `gmt_create` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `gmt_modified` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '软删',
  PRIMARY KEY (`BLIST_ID`) USING BTREE,
  KEY `T_BLACK_LIST_INDEX2` (`PRINCIPAL_NO`) USING BTREE,
  KEY `T_BLACK_LIST_INDEX3` (`USE_YN`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='黑名单管理';

-- ----------------------------
-- Records of t_black_list
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_bs_tch_link
-- ----------------------------
DROP TABLE IF EXISTS `t_bs_tch_link`;
CREATE TABLE `t_bs_tch_link` (
  `BLINK_ID` bigint(19) NOT NULL COMMENT '业务链路id',
  `TLINK_ID` bigint(19) NOT NULL COMMENT '技术链路id',
  `TLINK_ORDER` int(10) DEFAULT NULL COMMENT '技术链路横向排序编号',
  `TLINK_BANK` int(10) DEFAULT NULL COMMENT '技术链路竖向排序编号',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  UNIQUE KEY `T_INDEX_BT` (`BLINK_ID`,`TLINK_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='业务链路和技术链路关系表';

-- ----------------------------
-- Records of t_bs_tch_link
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_business_activities
-- ----------------------------
DROP TABLE IF EXISTS `t_business_activities`;
CREATE TABLE `t_business_activities` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `whole_report_id` bigint(20) NOT NULL COMMENT '压测报告整体概况ID',
  `pessure_process_id` bigint(20) NOT NULL COMMENT '业务流程ID',
  `name` varchar(20) DEFAULT NULL COMMENT '业务活动名称',
  `tps` varchar(10) DEFAULT NULL COMMENT 'tps',
  `avg_rt` varchar(10) DEFAULT NULL COMMENT '平均rt',
  `success_rate` varchar(10) DEFAULT NULL COMMENT '请求成功率',
  `peak_time` varchar(10) DEFAULT NULL COMMENT '压测峰值时刻',
  `peak_tps` varchar(10) DEFAULT NULL COMMENT '压测峰值TPS',
  `peak_avg_rt` varchar(10) DEFAULT NULL COMMENT '压测峰值rt',
  `peak_request` varchar(10) DEFAULT NULL COMMENT '压测峰值请求量',
  `peak_request_rate` varchar(10) DEFAULT NULL COMMENT '压测峰值请求成功率',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0-未删除、1-已删除',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='压测报告业务活动';

-- ----------------------------
-- Records of t_business_activities
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_business_link_manage_table
-- ----------------------------
DROP TABLE IF EXISTS `t_business_link_manage_table`;
CREATE TABLE `t_business_link_manage_table` (
  `LINK_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `LINK_NAME` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '链路名称',
  `ENTRACE` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '链路入口',
  `RELATED_TECH_LINK` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '业务链路绑定的技术链路',
  `LINK_LEVEL` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '业务链路级别: p0/p1/p2/p3 ',
  `PARENT_BUSINESS_ID` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '业务链路的上级业务链路id',
  `IS_CHANGE` tinyint(4) DEFAULT NULL COMMENT '是否有变更 0:正常；1:已变更',
  `IS_CORE` tinyint(4) DEFAULT NULL COMMENT '业务链路是否否核心链路 0:不是;1:是',
  `CUSTOMER_ID` bigint(20) DEFAULT NULL COMMENT '租户id',
  `USER_ID` bigint(20) DEFAULT NULL COMMENT '用户id',
  `IS_DELETED` tinyint(4) DEFAULT '0' COMMENT '是否有效 0:有效;1:无效',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `BUSINESS_DOMAIN` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '业务域: 0:订单域", "1:运单域", "2:结算域 ',
  `CAN_DELETE` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否可以删除 0:可以删除;1:不可以删除',
  PRIMARY KEY (`LINK_ID`) USING BTREE,
  KEY `T_LINK_MNT_INDEX1` (`LINK_NAME`) USING BTREE,
  KEY `T_LINK_MNT_INDEX2` (`ENTRACE`(255)) USING BTREE,
  KEY `T_LINK_MNT_INDEX3` (`CREATE_TIME`) USING BTREE,
  KEY `RELATED_TECH_LINK` (`RELATED_TECH_LINK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务链路管理表';

-- ----------------------------
-- Records of t_business_link_manage_table
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_capacity_water_level_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_capacity_water_level_detail`;
CREATE TABLE `t_capacity_water_level_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `cwli_id` bigint(20) NOT NULL COMMENT '压测报告容量水位ID',
  `app_id` varchar(20) DEFAULT NULL COMMENT '应用ID',
  `app_name` varchar(20) DEFAULT NULL COMMENT '应用名称',
  `server_ip` varchar(15) DEFAULT NULL COMMENT '服务器IP',
  `standard` tinyint(1) DEFAULT '0' COMMENT '是否达标 0-达标、1-不达标',
  `cpu_use_ratio` varchar(5) DEFAULT NULL COMMENT 'CPU利用率',
  `cpu_load` varchar(5) DEFAULT NULL COMMENT 'CPU LOAD',
  `memory_use_ratio` varchar(5) DEFAULT NULL COMMENT '内存利用率',
  `disk_use_ratio` varchar(5) DEFAULT NULL COMMENT '磁盘利用率',
  `ygc` varchar(5) DEFAULT NULL COMMENT 'ygc',
  `fgc` varchar(5) DEFAULT NULL COMMENT 'fgc',
  `cache_read_rt` varchar(5) DEFAULT NULL COMMENT '缓存读RT',
  `cache_write_rt` varchar(5) DEFAULT NULL COMMENT '缓存写RT',
  `db_read_rt` varchar(5) DEFAULT NULL COMMENT 'db读RT',
  `db_write_rt` varchar(5) DEFAULT NULL COMMENT 'db写RT',
  `remote_call_rt` varchar(5) DEFAULT NULL COMMENT '远程调用rt',
  `cache_read_tps` varchar(5) DEFAULT NULL COMMENT '缓存读TPS',
  `cache_write_tpst` varchar(5) DEFAULT NULL COMMENT '缓存写TPS',
  `db_tps` varchar(5) DEFAULT NULL COMMENT '数据库TPS',
  `remote_call_tps` varchar(5) DEFAULT NULL COMMENT '远程调用TPS',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0-未删除、1-已删除',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='压测报告容量水位详情';

-- ----------------------------
-- Records of t_capacity_water_level_detail
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_capacity_water_level_info
-- ----------------------------
DROP TABLE IF EXISTS `t_capacity_water_level_info`;
CREATE TABLE `t_capacity_water_level_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `whole_report_id` bigint(20) NOT NULL COMMENT '压测报告整体概况ID',
  `app_id` bigint(20) DEFAULT NULL COMMENT '应用ID',
  `app_name` varchar(20) DEFAULT NULL COMMENT '应用名称',
  `total_count` varchar(5) DEFAULT NULL COMMENT '主机总量',
  `not_standard_count` varchar(5) DEFAULT NULL COMMENT '达标主机',
  `cause` text COMMENT '未达标主要原因',
  `last_cwli_id` bigint(20) DEFAULT NULL COMMENT '对比上次报错信息ID',
  `last_not_standard_count` varchar(5) DEFAULT NULL COMMENT '上次未达标主机数量',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0-未删除、1-已删除',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='压测报告容量水位';

-- ----------------------------
-- Records of t_capacity_water_level_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_cloud_account
-- ----------------------------
DROP TABLE IF EXISTS `t_cloud_account`;
CREATE TABLE `t_cloud_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `platform_id` bigint(20) NOT NULL COMMENT '云平台id',
  `platform_name` varchar(125) COLLATE utf8_bin NOT NULL COMMENT '云平台名称',
  `account` varchar(125) COLLATE utf8_bin NOT NULL COMMENT '账号',
  `authorize_param` text COLLATE utf8_bin COMMENT '平台授权数据',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态 1:启用  0： 冻结',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0: 正常 1： 删除',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_update` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_cloud_account
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_cloud_platform
-- ----------------------------
DROP TABLE IF EXISTS `t_cloud_platform`;
CREATE TABLE `t_cloud_platform` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(125) COLLATE utf8_bin NOT NULL COMMENT '云平台名称',
  `class_path` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '插件类全路径',
  `authorize_param` text COLLATE utf8_bin COMMENT '平台授权参数',
  `jar_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Jar包名称',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态 1:启用  0： 冻结',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0: 正常 1： 删除',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_update` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `index_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_cloud_platform
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_data_build
-- ----------------------------
DROP TABLE IF EXISTS `t_data_build`;
CREATE TABLE `t_data_build` (
  `DATA_BUILD_ID` bigint(19) NOT NULL COMMENT '数据构建id',
  `APPLICATION_ID` bigint(19) DEFAULT NULL COMMENT '应用id',
  `DDL_BUILD_STATUS` int(1) DEFAULT '0' COMMENT '影子库表结构脚本构建状态(0未启动 1正在执行 2执行成功 3执行失败)',
  `DDL_LAST_SUCCESS_TIME` datetime DEFAULT NULL COMMENT '影子库表结构脚本上一次执行成功时间',
  `CACHE_BUILD_STATUS` int(1) DEFAULT '0' COMMENT '缓存预热脚本执行状态',
  `CACHE_LAST_SUCCESS_TIME` datetime DEFAULT NULL COMMENT '缓存预热脚本上一次执行成功时间',
  `READY_BUILD_STATUS` int(1) DEFAULT '0' COMMENT '基础数据准备脚本执行状态',
  `READY_LAST_SUCCESS_TIME` datetime DEFAULT NULL COMMENT '基础数据准备脚本上一次执行成功时间',
  `BASIC_BUILD_STATUS` int(1) DEFAULT '0' COMMENT '铺底数据脚本执行状态',
  `BASIC_LAST_SUCCESS_TIME` datetime DEFAULT NULL COMMENT '铺底数据脚本上一次执行成功时间',
  `CLEAN_BUILD_STATUS` int(1) DEFAULT '0' COMMENT '数据清理脚本执行状态',
  `CLEAN_LAST_SUCCESS_TIME` datetime DEFAULT NULL COMMENT '数据清理脚本上一次执行成功时间',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '插入时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '变更时间',
  PRIMARY KEY (`DATA_BUILD_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='压测数据构建表';

-- ----------------------------
-- Records of t_data_build
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_database_conf
-- ----------------------------
DROP TABLE IF EXISTS `t_database_conf`;
CREATE TABLE `t_database_conf` (
  `TDC_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '数据库配置主键',
  `BASIC_LINK_ID` bigint(19) DEFAULT NULL COMMENT '基础链路id',
  `DB_TYPE` int(1) NOT NULL COMMENT '数据库类型',
  `DATA_SOURCE` varchar(50) DEFAULT NULL COMMENT '影子库所属系统',
  `DATA_TURN` varchar(50) DEFAULT NULL COMMENT '数据流转(默认SPT,为菜鸟阿斯旺)',
  `URL` varchar(1000) NOT NULL COMMENT '数据库连接地址',
  `USERNAME` varchar(20) NOT NULL COMMENT '数据库登录用户名',
  `PASSWD` varchar(1024) NOT NULL COMMENT '数据库登录密码',
  `DRIVER_CLASS_NAME` varchar(50) NOT NULL COMMENT '数据库驱动',
  `DATABASE_IP` varchar(15) NOT NULL COMMENT '数据库ip',
  `DATABASE_NAME` varchar(20) NOT NULL COMMENT '数据库名称',
  `DB_STATUS` int(1) DEFAULT '1' COMMENT '数据状态(0代表删除,1代表使用,默认为1)',
  `LOAD_STATUS` int(1) DEFAULT '0' COMMENT '抽数状态(0表示未开始,1表示正在运行,2表示运行结束)',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '插入时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '变更时间',
  `DICT_TYPE` varchar(32) NOT NULL COMMENT '数据字典类型（ID值）',
  `PUBLIC_KEY` varchar(1024) DEFAULT NULL COMMENT '数据库公钥',
  PRIMARY KEY (`TDC_ID`) USING BTREE,
  KEY `TDC_INDEX1` (`DATABASE_NAME`) USING BTREE,
  KEY `TDC_INDEX3` (`CREATE_TIME`) USING BTREE,
  KEY `TDC_INDEX4` (`DATABASE_NAME`) USING BTREE,
  KEY `TDC_INDEX5` (`DATABASE_IP`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6504276019602526209 DEFAULT CHARSET=utf8mb4 COMMENT='数据库配置表';

-- ----------------------------
-- Records of t_database_conf
-- ----------------------------
BEGIN;
INSERT INTO `t_database_conf` VALUES (6477785221335879680, 6483187071434362880, 1, 'TRO', 'SPT', 'jdbc:mysql://localhost:3306/tro?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true', 'gisuser', 'MbomYKEDsslLMhmJ9jVdYsS3oQBLvfbAwcMu5U4wCIz1bvYy4C/7QL59EuXSwxqnyZ9HHzQy1ysyTl56Rn/8fw==', 'com.mysql.jdbc.Driver', '10.224.66.41', 'tro', 0, 0, '2018-12-10 14:45:48', '2018-12-10 14:45:48', 'ada9370f26ac4c79acbac0ad7acb0992', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzURufk4p2wU85s0/mixGuyppzeqwH/mFBpGefSswqbsw9uCyxnbVNu1oxYmpTFe1GPfUWluO8riUCZB5GwUNsCAwEAAQ==');
INSERT INTO `t_database_conf` VALUES (6489110632980287488, 6486148600630874112, 1, 'irad_cainiao', NULL, 'jdbc:mysql://10.230.44.197:3306/dbtest?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true', 'gisuser', 'gisuser', 'com.mysql.jdbc.Driver', '', 'irad_cainiao', 1, 0, '2019-01-10 20:48:56', '2019-01-10 20:48:56', 'ada9370f26ac4c79acbac0ad7acb0992', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzURufk4p2wU85s0/mixGuyppzeqwH/mFBpGefSswqbsw9uCyxnbVNu1oxYmpTFe1GPfUWluO8riUCZB5GwUNsCAwEAAQ==');
INSERT INTO `t_database_conf` VALUES (6491927141138042880, 6489308559245840384, 1, 'test', NULL, 'jdbc:mysql://10.224.68.42:3306/tro?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true', 'test', 'test', 'com.mysql.jdbc.Driver', '10.224.68.42', 'test', 0, 0, '2019-01-18 15:20:44', '2019-01-18 15:20:44', 'ada9370f26ac4c79acbac0ad7acb0992', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzURufk4p2wU85s0/mixGuyppzeqwH/mFBpGefSswqbsw9uCyxnbVNu1oxYmpTFe1GPfUWluO8riUCZB5GwUNsCAwEAAQ==');
INSERT INTO `t_database_conf` VALUES (6491940695392129024, 6489308559245840384, 1, 'test', NULL, '10.10.10.10', 'search', 'search', 'com.mysql.jdbc.Driver', '10.10.10.10', 'test', 0, 0, '2019-01-18 16:14:36', '2019-01-18 16:14:36', 'ada9370f26ac4c79acbac0ad7acb0992', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzURufk4p2wU85s0/mixGuyppzeqwH/mFBpGefSswqbsw9uCyxnbVNu1oxYmpTFe1GPfUWluO8riUCZB5GwUNsCAwEAAQ==');
INSERT INTO `t_database_conf` VALUES (6491993663210524672, 6483187071434362880, 1, 'TRO', NULL, 'jdbc:mysql://10.224.66.41:3306/tro?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true', 'gisuser', 'gisuser', 'com.mysql.jdbc.Driver', '10.224.66.41', 'tro', 0, 0, '2019-01-18 19:45:04', '2019-01-18 19:45:04', 'ada9370f26ac4c79acbac0ad7acb0992', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzURufk4p2wU85s0/mixGuyppzeqwH/mFBpGefSswqbsw9uCyxnbVNu1oxYmpTFe1GPfUWluO8riUCZB5GwUNsCAwEAAQ==');
INSERT INTO `t_database_conf` VALUES (6492207793427189760, 6487881648934227968, 1, 'TRO', NULL, 'jdbc:mysql://10.224.66.41:3306/fang?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true', 'gisuser', 'gisuser', 'com.mysql.jdbc.Driver', '10.224.66.41', 'fang', 1, 0, '2019-01-19 09:55:57', '2019-01-19 09:55:57', 'ada9370f26ac4c79acbac0ad7acb0992', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzURufk4p2wU85s0/mixGuyppzeqwH/mFBpGefSswqbsw9uCyxnbVNu1oxYmpTFe1GPfUWluO8riUCZB5GwUNsCAwEAAQ==');
INSERT INTO `t_database_conf` VALUES (6492935171216510976, 6486147486594371584, 1, 'OMS', NULL, 'jdbc:mysql://10.230.22.205:3306/tro?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true', 'gisuser', 'gisuser', 'com.mysql.jdbc.Driver', '10.230.22.205', 'tro', 0, 0, '2019-01-21 10:06:17', '2019-01-21 10:06:17', 'ada9370f26ac4c79acbac0ad7acb0992', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzURufk4p2wU85s0/mixGuyppzeqwH/mFBpGefSswqbsw9uCyxnbVNu1oxYmpTFe1GPfUWluO8riUCZB5GwUNsCAwEAAQ==');
INSERT INTO `t_database_conf` VALUES (6493037996898521088, 6489308559245840384, 2, '11', NULL, '11', '111', '11', 'oracle.jdbc.driver.OracleDriver', '', '11', 0, 0, '2019-01-21 16:55:00', '2019-01-21 16:55:00', 'ada9370f26ac4c79acbac0ad7acb0992', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzURufk4p2wU85s0/mixGuyppzeqwH/mFBpGefSswqbsw9uCyxnbVNu1oxYmpTFe1GPfUWluO8riUCZB5GwUNsCAwEAAQ==');
INSERT INTO `t_database_conf` VALUES (6493038787738734592, 6486148600630874112, 1, 'oms', NULL, 'jdbc:mysql://10.230.22.205:3306/oms?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true', 'gisuser', 'gisuser', 'com.mysql.jdbc.Driver', '10.230.22.205', 'tro', 1, 0, '2019-01-21 16:58:01', '2019-01-21 16:58:01', 'ada9370f26ac4c79acbac0ad7acb0992', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzURufk4p2wU85s0/mixGuyppzeqwH/mFBpGefSswqbsw9uCyxnbVNu1oxYmpTFe1GPfUWluO8riUCZB5GwUNsCAwEAAQ==');
INSERT INTO `t_database_conf` VALUES (6493040109603000320, 6486148600630874112, 2, '22', NULL, '22', '22', '22', 'oracle.jdbc.driver.OracleDriver', '', '22', 0, 0, '2019-01-21 17:03:16', '2019-01-21 17:03:16', 'ada9370f26ac4c79acbac0ad7acb0992', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzURufk4p2wU85s0/mixGuyppzeqwH/mFBpGefSswqbsw9uCyxnbVNu1oxYmpTFe1GPfUWluO8riUCZB5GwUNsCAwEAAQ==');
INSERT INTO `t_database_conf` VALUES (6493043007963533312, 6489308559245840384, 2, '1111', NULL, '1111', '123', '123', 'oracle.jdbc.driver.OracleDriver', '', '11111', 1, 0, '2019-01-21 17:14:48', '2019-01-21 17:14:48', 'ada9370f26ac4c79acbac0ad7acb0992', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzURufk4p2wU85s0/mixGuyppzeqwH/mFBpGefSswqbsw9uCyxnbVNu1oxYmpTFe1GPfUWluO8riUCZB5GwUNsCAwEAAQ==');
INSERT INTO `t_database_conf` VALUES (6493293734510202880, 6483187071434362880, 2, 'TRO', NULL, 'jdbc:oracle:thin:@10.230.25.245:1521:gisdb', 'trotest', 'Dkv6FOigEil4j++P+1abYxZ5dAsviyeo9da4V0YI6wb8bMldvoln6UHtrMxUIOuGuvSStDCa/Au1YgbHA6WniA==', 'oracle.jdbc.driver.OracleDriver', '10.10.10.10', 'gisdb', 1, 0, '2019-01-22 09:51:05', '2019-01-22 09:51:05', 'ada9370f26ac4c79acbac0ad7acb0992', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzURufk4p2wU85s0/mixGuyppzeqwH/mFBpGefSswqbsw9uCyxnbVNu1oxYmpTFe1GPfUWluO8riUCZB5GwUNsCAwEAAQ==');
INSERT INTO `t_database_conf` VALUES (6493744043493691392, 6486148600630874112, 1, 'oms', NULL, 'jdbc:mysql://10.230.22.205:3306/oms?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true', 'gisuser', 'MbomYKEDsslLMhmJ9jVdYsS3oQBLvfbAwcMu5U4wCIz1bvYy4C/7QL59EuXSwxqnyZ9HHzQy1ysyTl56Rn/8fw==', 'com.mysql.jdbc.Driver', '10.230.22.205', 'tro', 1, 0, '2019-01-23 15:40:27', '2019-01-23 15:40:27', 'ada9370f26ac4c79acbac0ad7acb0992', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzURufk4p2wU85s0/mixGuyppzeqwH/mFBpGefSswqbsw9uCyxnbVNu1oxYmpTFe1GPfUWluO8riUCZB5GwUNsCAwEAAQ==');
INSERT INTO `t_database_conf` VALUES (6494383888171798528, 6489308559245840384, 2, 'test', NULL, 'jdbc:mysql://10.230.22.205:3306/oms?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true', 'test', 'test', 'oracle.jdbc.driver.OracleDriver', '10.230.22.205', 'test', 1, 0, '2019-01-25 10:02:58', '2019-01-25 10:02:58', 'ada9370f26ac4c79acbac0ad7acb0992', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzURufk4p2wU85s0/mixGuyppzeqwH/mFBpGefSswqbsw9uCyxnbVNu1oxYmpTFe1GPfUWluO8riUCZB5GwUNsCAwEAAQ==');
INSERT INTO `t_database_conf` VALUES (6494384325029531648, 6489308559245840384, 2, 'test1', NULL, 'test1', 'test1', 'test1', 'oracle.jdbc.driver.OracleDriver', '', 'test1', 1, 0, '2019-01-25 10:04:42', '2019-01-25 10:04:42', 'ada9370f26ac4c79acbac0ad7acb0992', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzURufk4p2wU85s0/mixGuyppzeqwH/mFBpGefSswqbsw9uCyxnbVNu1oxYmpTFe1GPfUWluO8riUCZB5GwUNsCAwEAAQ==');
INSERT INTO `t_database_conf` VALUES (6502440055712256000, 6486148600630874112, 1, 'oms', NULL, 'jdbc:mysql://10.230.22.205:3306/oms?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true', 'gisuser', 'gisuser', 'com.mysql.jdbc.Driver', '10.230.22.205', 'tro', 1, 0, '2019-02-16 15:35:18', '2019-02-16 15:35:18', 'ada9370f26ac4c79acbac0ad7acb0992', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzURufk4p2wU85s0/mixGuyppzeqwH/mFBpGefSswqbsw9uCyxnbVNu1oxYmpTFe1GPfUWluO8riUCZB5GwUNsCAwEAAQ==');
INSERT INTO `t_database_conf` VALUES (6502727307910713344, 6483187071434362880, 2, 'TRO', NULL, 'jdbc:oracle:thin:@10.230.25.245:1521:gisdb', 'trotest', 'trotest', 'oracle.jdbc.driver.OracleDriver', '10.230.25.245', 'gisdb', 1, 0, '2019-02-17 10:36:45', '2019-02-17 10:36:45', 'ada9370f26ac4c79acbac0ad7acb0992', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzURufk4p2wU85s0/mixGuyppzeqwH/mFBpGefSswqbsw9uCyxnbVNu1oxYmpTFe1GPfUWluO8riUCZB5GwUNsCAwEAAQ==');
INSERT INTO `t_database_conf` VALUES (6502847996902772736, 6486148600630874112, 1, 'oms', NULL, 'jdbc:mysql://10.230.22.205:3306/oms?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true', 'gisuser', 'MbomYKEDsslLMhmJ9jVdYsS3oQBLvfbAwcMu5U4wCIz1bvYy4C/7QL59EuXSwxqnyZ9HHzQy1ysyTl56Rn/8fw==', 'com.mysql.jdbc.Driver', '10.230.22.205', 'tro', 1, 0, '2019-02-17 18:36:19', '2019-02-17 18:36:19', 'ada9370f26ac4c79acbac0ad7acb0992', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzURufk4p2wU85s0/mixGuyppzeqwH/mFBpGefSswqbsw9uCyxnbVNu1oxYmpTFe1GPfUWluO8riUCZB5GwUNsCAwEAAQ==');
INSERT INTO `t_database_conf` VALUES (6502854737757474816, 6486148600630874112, 1, 'oms', NULL, 'jdbc:mysql://10.230.22.205:3306/oms?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true', 'gisuser', 'MbomYKEDsslLMhmJ9jVdYsS3oQBLvfbAwcMu5U4wCIz1bvYy4C/7QL59EuXSwxqnyZ9HHzQy1ysyTl56Rn/8fw==', 'com.mysql.jdbc.Driver', '10.230.22.205', 'tro', 1, 0, '2019-02-17 19:03:06', '2019-02-17 19:03:06', 'ada9370f26ac4c79acbac0ad7acb0992', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzURufk4p2wU85s0/mixGuyppzeqwH/mFBpGefSswqbsw9uCyxnbVNu1oxYmpTFe1GPfUWluO8riUCZB5GwUNsCAwEAAQ==');
INSERT INTO `t_database_conf` VALUES (6503143502132547584, 6486147486594371584, 2, '213', NULL, 'jdbc:oracle:thin:@10.230.44.245:gisdb', 'sqtest', 'sqtest', 'oracle.jdbc.driver.OracleDriver', '10.230.44.245', 'tr', 0, 0, '2019-02-18 14:10:33', '2019-02-18 14:10:33', 'ada9370f26ac4c79acbac0ad7acb0992', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzURufk4p2wU85s0/mixGuyppzeqwH/mFBpGefSswqbsw9uCyxnbVNu1oxYmpTFe1GPfUWluO8riUCZB5GwUNsCAwEAAQ==');
INSERT INTO `t_database_conf` VALUES (6503487653810278400, 6489308559245840384, 1, '1111', NULL, '1', '11', '11', 'com.mysql.jdbc.Driver', '', '11', 0, 0, '2019-02-19 12:58:05', '2019-02-19 12:58:05', 'ada9370f26ac4c79acbac0ad7acb0992', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzURufk4p2wU85s0/mixGuyppzeqwH/mFBpGefSswqbsw9uCyxnbVNu1oxYmpTFe1GPfUWluO8riUCZB5GwUNsCAwEAAQ==');
INSERT INTO `t_database_conf` VALUES (6503513589276479488, 6489308559245840384, 2, '1', NULL, '1', '1', '1', 'oracle.jdbc.driver.OracleDriver', '', '1111111', 0, 0, '2019-02-19 14:41:09', '2019-02-19 14:41:09', 'ada9370f26ac4c79acbac0ad7acb0992', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzURufk4p2wU85s0/mixGuyppzeqwH/mFBpGefSswqbsw9uCyxnbVNu1oxYmpTFe1GPfUWluO8riUCZB5GwUNsCAwEAAQ==');
INSERT INTO `t_database_conf` VALUES (6504276019602526208, 6489308559245840384, 1, 'oms', NULL, 'jdbc:mysql://10.230.22.206:3306/oms?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true', 'gisuser', 'MbomYKEDsslLMhmJ9jVdYsS3oQBLvfbAwcMu5U4wCIz1bvYy4C/7QL59EuXSwxqnyZ9HHzQy1ysyTl56Rn/8fw==', 'com.mysql.jdbc.Driver', '10.230.22.205', 'tro', 0, 0, '2019-02-21 17:10:46', '2019-02-21 17:10:46', 'ada9370f26ac4c79acbac0ad7acb0992', 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKzURufk4p2wU85s0/mixGuyppzeqwH/mFBpGefSswqbsw9uCyxnbVNu1oxYmpTFe1GPfUWluO8riUCZB5GwUNsCAwEAAQ==');
COMMIT;

-- ----------------------------
-- Table structure for t_datasource_tag_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_datasource_tag_ref`;
CREATE TABLE `t_datasource_tag_ref` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `datasource_id` bigint(20) NOT NULL COMMENT '数据源id',
  `tag_id` bigint(20) NOT NULL COMMENT '标签id',
  `gmt_create` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `gmt_update` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_datasourceId_tagId` (`datasource_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_datasource_tag_ref
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_dictionary_data
-- ----------------------------
DROP TABLE IF EXISTS `t_dictionary_data`;
CREATE TABLE `t_dictionary_data` (
  `ID` varchar(50) NOT NULL COMMENT 'ID',
  `DICT_TYPE` varchar(50) DEFAULT NULL COMMENT '数据字典分类',
  `VALUE_ORDER` int(11) DEFAULT NULL COMMENT '序号',
  `VALUE_NAME` varchar(50) DEFAULT NULL COMMENT '值名称',
  `VALUE_CODE` varchar(50) DEFAULT NULL COMMENT '值代码',
  `LANGUAGE` varchar(10) DEFAULT NULL COMMENT '语言',
  `ACTIVE` char(1) DEFAULT NULL COMMENT '是否启用',
  `CREATE_TIME` date DEFAULT NULL COMMENT '创建时间',
  `MODIFY_TIME` date DEFAULT NULL COMMENT '更新时间',
  `CREATE_USER_CODE` varchar(50) DEFAULT NULL COMMENT '创建人',
  `MODIFY_USER_CODE` varchar(50) DEFAULT NULL COMMENT '更新人',
  `NOTE_INFO` varchar(250) DEFAULT NULL COMMENT '备注信息',
  `VERSION_NO` bigint(20) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `IDX_MUL_QUERY` (`ACTIVE`,`DICT_TYPE`,`VALUE_CODE`) USING BTREE,
  KEY `idx_VERSION_NO` (`VERSION_NO`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据字典基础数据表';

-- ----------------------------
-- Records of t_dictionary_data
-- ----------------------------
BEGIN;
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309485c84ae0d645f26das32', '6ba75716d726493783bfd64cce058110', -1, '离线', '-1', 'ZH_CN', 'Y', '2019-06-17', '2019-06-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309485c84ae0d645f26das33', '6ba75716d726493783bfd64cce058110', 0, '空闲', '0', 'ZH_CN', 'Y', '2019-06-17', '2019-06-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309485c84ae0d645f26das34', '6ba75716d726493783bfd64cce058110', 1, '压测中', '1', 'ZH_CN', 'Y', '2019-06-17', '2019-06-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309485c84ae0d645f26de5a', 'f48c856d2aec493a8902da7485720404', 3, '《 15分钟', 'less15', 'ZH_CN', 'Y', '2019-06-17', '2019-06-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('07461d8c848e4644b563f9eef56bf1a7', '41a309a6d1e04105acfd5fb08200f0c5', 1, '页面', 'PAGE', 'ZH_CN', 'Y', '2019-06-12', '2019-06-12', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('092e39d5b9614092b333f849588ee288', 'b8f4ce1a989b4f19a842e08975ee3eb1', 1, '业务链路', 'BUSINESS_LINK', 'zh_CN', 'Y', '2018-12-24', '2018-12-24', NULL, NULL, NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('12e7f7fc6f5240ea906a8d13344f86ba', 'f48c856d2aec493a8902da7485720404', 2, '《 5分钟', 'less5', 'ZH_CN', 'Y', '2019-06-17', '2019-06-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('12f606f88ebc4b04806f9c094e94d3f3', '470557d08abe4eb6a794764209c7763d', 2, '简单查询页面/复杂界面', '5s', 'ZH_CN', 'Y', '2019-06-12', '2019-06-12', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('16f6a9e9f20947cfa4e4cd4f5f5430fa', 'f644eb266aba4a2186341b708f33eece', 6, '签收', 'SIGNING', 'ZH_CN', 'N', '2019-04-04', '2019-04-04', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('17101eefd9ff4624a581d2e6c29cd7b3', '6ba75716d726493783bfd64cce058780', 3, '近1周', '168', 'ZH_CN', 'Y', '2019-07-10', '2019-07-11', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('1bfe98ddb7a84401b04b108f402e4a16', '8c3c52c6889c435a8e055d988016e02a', 2, '一般操作/查询', '100ms', 'ZH_CN', 'Y', '2019-06-12', '2019-06-12', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('226588d83e814de09e703f10d4582a85', '944da50e5a334e128b34df906971b113', 1, '堆栈异常', '1', 'ZH_CN', 'Y', '2019-03-29', '2019-03-29', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('287b5429a5634e72838f53023673fdf9', '6ba75716d726493783bfd64cce058780', 2, '近1天', '24', 'ZH_CN', 'Y', '2019-07-10', '2019-07-11', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('29e1ac2d7b614b0686cb5e00ee476862', 'f48c856d2aec493a8902da7485720404', 4, '《 60分钟', 'less60', 'ZH_CN', 'Y', '2019-06-17', '2019-06-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('352f90a0e56c41908e543aa421359a31', 'ca888ed801664c81815d8c4f5b8dff0c', 5, 'MQ', 'mq', 'ZH_CN', 'Y', '2019-04-03', '2019-04-03', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('425fffeddc0b42bca9908a167abd98af', 'f644eb266aba4a2186341b708f33eece', 7, 'CUBC结算', 'SETTLEMENT', 'ZH_CN', 'N', '2019-04-04', '2019-04-04', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('445521c501cc4ea3bf19732ea81d29b2', 'ca888ed801664c81815d8c4f5b8dff0c', 1, 'HTTP', 'http', 'zh_CN', 'Y', '2018-07-05', '2019-04-03', '', '', '', 0);
INSERT INTO `t_dictionary_data` VALUES ('45ddc5768689413386ac14bc3442d539', '944da50e5a334e128b34df906971b113', 2, 'SQL解析异常', '2', 'ZH_CN', 'Y', '2019-03-29', '2019-03-29', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('4642aca4bcac47288b279a9e044cea54', '2e81620c1b74421cbe071224822e5725', 2, 'PRADAR', 'pradar', 'ZH_CN', 'Y', '2019-07-10', '2019-07-10', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('49a8c79896ea41a784b6b61aa083f542', 'f644eb266aba4a2186341b708f33eece', 8, '轨迹', 'TRAIL', 'ZH_CN', 'N', '2019-04-17', '2019-04-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('4e27eecaeb3a4d1a865c5bd874ad127b', 'ca888ed801664c81815d8c4f5b8dff0c', 4, 'JOB', 'job', 'ZH_CN', 'Y', '2019-03-04', '2019-04-03', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('5434f289c4d5494e8002b842893153b3', '6ba75716d726493783bfd64cce058780', 5, '自定义时间段', '0', 'ZH_CN', 'Y', '2019-07-10', '2019-07-11', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('576ab6777e7f410289b153635324120d', 'ada9370f26ac4c79acbac0ad7acb0992', 1, 'MYSQL', 'MYSQL', 'zh_CN', 'Y', '2018-08-30', '2018-08-30', NULL, NULL, NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('5cea119bd6f0473db30cd227109285bc', 'f644eb266aba4a2186341b708f33eece', 4, '移动端-教师', 'TRANSFER', 'ZH_CN', 'Y', '2019-04-04', '2019-04-04', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('5d59803f47cc41e0a8b4cf1867248954', '2e81620c1b74421cbe071224822e5725', 1, 'SPT', 'spt', 'ZH_CN', 'Y', '2019-07-10', '2019-07-10', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('6438e36d424c4f54ab4f0773d338f9a1', '51a309asdsdfads8779fd5fb08200f03u', 0, '订单域', '0', 'ZH_CN', 'Y', '2021-01-15', '2021-01-15', NULL, NULL, NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('6438e36d424c4f54ab4f0773d338f9a2', '51a309asdsdfads8779fd5fb08200f03u', 1, '运单域', '1', 'ZH_CN', 'Y', '2021-01-15', '2021-01-15', NULL, NULL, NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('6438e36d424c4f54ab4f0773d338f9a3', '51a309asdsdfads8779fd5fb08200f03u', 2, '结算域', '2', 'ZH_CN', 'Y', '2021-01-15', '2021-01-15', NULL, NULL, NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('702e091427f44807ae4baed6e1871078', '3ea3284fbca841bcbcda50afa0d8a24b', 1, '计算订单量', 'orderVolume', 'ZH_CN', 'Y', '2019-04-04', '2019-04-09', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('782ccd949d9f453a857577a20b06392a', 'ec18bced105c41018cfbcaf6b3327b9a', 4, 'DPBOOT_ROCKETMQ', 'DPBOOT_ROCKETMQ', 'zh_CN', 'Y', '2018-10-10', '2018-10-10', '', '', '', 0);
INSERT INTO `t_dictionary_data` VALUES ('7ac9c0823e3645a1afd3d2f521d468c0', 'f48c856d2aec493a8902da7485720404', 1, '《 1分钟', 'less1', 'ZH_CN', 'Y', '2019-06-17', '2019-06-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('7d16b5e08c66499da224adb240da6426', 'f644eb266aba4a2186341b708f33eece', 3, '移动端-学生', 'BILLING', 'ZH_CN', 'Y', '2019-04-04', '2019-04-04', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('81a43f092f9a4df6a1dfd23b45213eef', 'ada9370f26ac4c79acbac0ad7acb0992', 2, 'ORACLE', 'ORACLE', 'zh_CN', 'Y', '2018-08-30', '2018-08-30', NULL, NULL, NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('853152241ac7404c8910f2c723a50863', '8c3c52c6889c435a8e055d988016e02a', 3, '复杂操作', '300ms', 'ZH_CN', 'Y', '2019-06-12', '2019-06-12', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('880554742fa343d8adc9faff1a632999', 'd69be19362c4461fb7e84dfbc21f1747', 2, 'p1', 'p1', 'zh_CN', 'Y', '2018-07-05', '2018-07-05', '', '', '', 0);
INSERT INTO `t_dictionary_data` VALUES ('8a57a87e1ee14effaa5a99d65b7f9780', '8c3c52c6889c435a8e055d988016e02a', 5, '调用外网操作', 'other', 'ZH_CN', 'Y', '2019-06-12', '2019-06-12', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('8a5a846bed734a76914aa133ad7c8d45', '470557d08abe4eb6a794764209c7763d', 1, '普通页面加载', '3s', 'ZH_CN', 'Y', '2019-06-12', '2019-06-12', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('8c0b5b11fe324b22a8ef010fa8799b23', '3ea3284fbca841bcbcda50afa0d8a24b', 2, '计算运单量', 'billingVolume', 'ZH_CN', 'Y', '2019-04-04', '2019-04-09', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('9009f0bef309485c84aeo9s45f26de01', '2e81620c1disodsobe071224822e5725', 1, 'PUT', '1', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('9009f0bef309485c84aeo9s45f26de02', '2e81620c1disodsobe071224822e5725', 2, 'GET', '2', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('9009f0bef309485c84aeo9s45f26de03', '2e81620c1disodsobe071224822e5725', 3, 'POST', '3', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('9009f0bef309485c84aeo9s45f26de04', '2e81620c1disodsobe071224822e5725', 4, 'OPTIONS', '4', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('9009f0bef309485c84aeo9s45f26de05', '2e81620c1disodsobe071224822e5725', 5, 'DELETE', '5', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('9009f0bef309485c84aeo9s45f26de06', '2e81620c1disodsobe071224822e5725', 6, 'HEAD', '6', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('9009f0bef309485c84aeo9s45f26de07', '2e81620c1disodsobe071224822e5725', 7, 'TRACE', '7', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('9009f0bef309485c84aeo9s45f26de08', '2e81620c1disodsobe071224822e5725', 8, 'CONNECT', '8', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('9009f0bef309485c84aeo9s45f26de09', '2e81620c1disodsobe071224822e5725', 9, 'DEFAULT', '0', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('901fb9678a254b80b30e527b7a980947', 'f644eb266aba4a2186341b708f33eece', 10, '非主流程链路', 'NON_CORE_LINKE', 'ZH_CN', 'N', '2019-04-04', '2019-04-04', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('91e2170859034632bfa6ab4d4c49fed8', 'd69be19362c4461fb7e84dfbc21f1747', 4, 'p3', 'p3', 'zh_CN', 'Y', '2018-07-05', '2018-07-05', '', '', '', 0);
INSERT INTO `t_dictionary_data` VALUES ('a54e6e51ee9c49cf8968bc3976f1ef19', '3ea3284fbca841bcbcda50afa0d8a24b', 3, '不计算单量', 'noVolume', 'ZH_CN', 'Y', '2019-04-09', '2019-04-09', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('af5d138bd14d4d4bbfe6eafb411bc029', 'ca888ed801664c81815d8c4f5b8dff0c', 3, '禁止压测', 'block', 'zh_CN', 'Y', '2018-05-14', '2018-05-14', '', '', '', 0);
INSERT INTO `t_dictionary_data` VALUES ('b4924562ebf745a1a631762c5b89b12f', 'ca888ed801664c81815d8c4f5b8dff0c', 2, 'DUBBO', 'dubbo', 'zh_CN', 'Y', '2018-07-05', '2019-04-03', '', '', '', 0);
INSERT INTO `t_dictionary_data` VALUES ('bcb4bd52b1984336930c255056403b31', 'd69be19362c4461fb7e84dfbc21f1747', 1, 'p0', 'p0', 'zh_CN', 'Y', '2018-07-05', '2018-07-05', '', '', '', 0);
INSERT INTO `t_dictionary_data` VALUES ('c2a41e51ced0457b9cfeb09e31f137a9', 'f644eb266aba4a2186341b708f33eece', 1, 'PC学生', 'CREATE_ORDER', 'ZH_CN', 'Y', '2019-04-04', '2019-04-04', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('cfccbe57756f4716accb165e1a568ef8', '6ba75716d726493783bfd64cce058780', 4, '近1月', '720', 'ZH_CN', 'Y', '2019-07-10', '2019-07-11', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('d5acad1497414134a3547a13ce661054', '41a309a6d1e04105acfd5fb08200f0c5', 2, '接口', 'INTERFACE', 'ZH_CN', 'Y', '2019-06-12', '2019-06-12', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('d6cb7549a94c4e4687318bff334a8601', 'f644eb266aba4a2186341b708f33eece', 5, '派送', 'DELIVERY', 'ZH_CN', 'N', '2019-04-04', '2019-04-04', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('dde28d43648b4428b80e06469ec679b0', 'ec18bced105c41018cfbcaf6b3327b9a', 1, 'ESB', 'ESB', 'zh_CN', 'Y', '2018-07-30', '2018-07-30', NULL, NULL, NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('dee1a075947641d4846304e568f2052e', 'ec18bced105c41018cfbcaf6b3327b9a', 3, 'ROCKETMQ', 'ROCKETMQ', 'zh_CN', 'Y', '2018-08-16', '2018-08-16', NULL, NULL, NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('e1f9137866a4436188513255ca2567ac', 'b8f4ce1a989b4f19a842e08975ee3eb1', 2, '技术链路', 'TECHNOLOGY_LINK', 'zh_CN', 'Y', '2018-12-24', '2018-12-24', '', '', '', 0);
INSERT INTO `t_dictionary_data` VALUES ('e4217237551a430abd346ee09bc38740', '6ba75716d726493783bfd64cce058780', 1, '近1小时', '1', 'ZH_CN', 'Y', '2019-07-10', '2019-07-10', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('eb81a4b38b404ce5b6a966cb96640897', 'f644eb266aba4a2186341b708f33eece', 2, 'PC教师', 'ORDER', 'ZH_CN', 'Y', '2019-04-04', '2019-04-04', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('ec18bced105c41018cfbcaf6b3327b9a', 'ec18bced105c41018cfbcaf6b3327b9a', 2, 'IBM', 'IBM', 'zh_CN', 'Y', '2018-07-30', '2018-07-30', NULL, NULL, NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('f22c57900cfc4069a965bf29c17e8d86', '8c3c52c6889c435a8e055d988016e02a', 1, '简单操作/查询', '50ms', 'ZH_CN', 'Y', '2019-06-12', '2019-06-12', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('f26c27c432644ca985b95ca46979c422', '8c3c52c6889c435a8e055d988016e02a', 4, '涉及级联嵌套调用多服务操作', '500ms', 'ZH_CN', 'Y', '2019-06-12', '2019-06-12', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('faf2c31d2f4c47c0be7426a36dc02bfc', 'd69be19362c4461fb7e84dfbc21f1747', 3, 'p2', 'p2', 'zh_CN', 'Y', '2018-05-21', '2018-05-21', '', '', '', 0);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fda90', 'f644eb266aba4a2186341b708f33eegg', 5, 'CPU利用率', '4', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fda91', 'f644eb266aba4a2186341b708f33eegg', 6, '内存占用', '5', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdb12f', 'f644eb266aba4a2186341b708f33eegb', 3, '云服务器', '华为云', 'ZH_CN', 'Y', '2020-03-12', '2020-03-12', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdb4a', 'f644eb266aba4a2186341b708f33eegb', 1, '云服务器', '阿里云ECS', 'ZH_CN', 'Y', '2020-03-12', '2020-03-12', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdb60', 'f644eb266aba4a2186341b708f33eegc', 0, '待启动', '0', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdb61', 'f644eb266aba4a2186341b708f33eegc', 1, '启动中', '1', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdb62', 'f644eb266aba4a2186341b708f33eegc', 2, '压测中', '2', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdb6f', '470557d08abe4eb6a794764209c7763d', 3, '复杂查询页面', '8s', 'ZH_CN', 'Y', '2019-06-12', '2019-06-12', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdb87', 'f644eb266aba4a2186341b708f33eegb', 2, '云服务器', '腾讯云', 'ZH_CN', 'Y', '2020-03-12', '2020-03-12', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdb90', 'f644eb266aba4a2186341b708f33eegd', 1, '否', '0', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdb91', 'f644eb266aba4a2186341b708f33eegd', 2, '是', '1', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdb92', 'f644eb266aba4a2186341b708f33eege', 1, 'JMeter', '0', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdb93', 'f644eb266aba4a2186341b708f33eege', 2, 'Gatling', '1', 'ZH_CN', 'N', '2020-03-17', '2020-03-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdb94', 'f644eb266aba4a2186341b708f33eege', 3, 'LoadRunner', '2', 'ZH_CN', 'N', '2020-03-17', '2020-03-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdb95', 'f644eb266aba4a2186341b708f33eegf', 1, '脚本文件', '0', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdb96', 'f644eb266aba4a2186341b708f33eegf', 2, '数据文件', '1', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdb97', 'f644eb266aba4a2186341b708f33eegg', 1, 'RT', '0', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdb98', 'f644eb266aba4a2186341b708f33eegg', 2, 'TPS', '1', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdb99', 'f644eb266aba4a2186341b708f33eegg', 3, '成功率', '2', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdc01', 'f644eb266aba4a2186341b708f33eegg', 4, 'SA', '3', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdc02', 'f644eb266aba4a2186341b708f33eegh', 1, '>=', '0', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', NULL, NULL, NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdc03', 'f644eb266aba4a2186341b708f33eegh', 2, '>', '1', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', NULL, NULL, NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdc04', 'f644eb266aba4a2186341b708f33eegh', 3, '=', '2', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', NULL, NULL, NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdc05', 'f644eb266aba4a2186341b708f33eegh', 4, '<=', '3', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', NULL, NULL, NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdc06', 'f644eb266aba4a2186341b708f33eegh', 5, '<', '4', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', NULL, NULL, NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdc07', 'f644eb266aba4a2186341b708f33eegk', 1, '启用', '0', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', NULL, NULL, NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdc08', 'f644eb266aba4a2186341b708f33eegk', 2, '禁用', '1', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', NULL, NULL, NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('tc77d5f788dc45dsfds039d5b1b4fdc01', 'f644eb266aba4a2186341b708f33kklll', 0, 'MYSQL', '0', 'ZH_CN', 'Y', '2021-01-06', '2021-01-06', NULL, NULL, NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('tc77d5f788dc45dsfds039d5b1b4fdc10', 'f644eb266aba4a2186341b708f33wer2', 0, 'ROCKETMQ', 'ROCKETMQ', 'ZH_CN', 'Y', '2021-02-18', '2021-02-18', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('tc77d5f788dc45dsfds039d5b1b4fdc11', 'f644eb266aba4a2186341b708f33wer2', 1, 'KAFKA', 'KAFKA', 'ZH_CN', 'Y', '2021-02-18', '2021-02-18', '000000', '000000', NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for t_dictionary_type
-- ----------------------------
DROP TABLE IF EXISTS `t_dictionary_type`;
CREATE TABLE `t_dictionary_type` (
  `ID` varchar(50) NOT NULL COMMENT 'ID',
  `TYPE_NAME` varchar(50) DEFAULT NULL COMMENT '分类名称',
  `ACTIVE` char(1) DEFAULT NULL COMMENT '是否启用',
  `CREATE_TIME` date DEFAULT NULL COMMENT '创建时间',
  `MODIFY_TIME` date DEFAULT NULL COMMENT '更新时间',
  `CREATE_USER_CODE` varchar(50) DEFAULT NULL COMMENT '创建人',
  `MODIFY_USER_CODE` varchar(50) DEFAULT NULL COMMENT '更新人',
  `PARENT_CODE` varchar(50) DEFAULT NULL COMMENT '上级分类编码',
  `TYPE_ALIAS` varchar(50) DEFAULT NULL COMMENT '分类别名',
  `IS_LEAF` char(1) DEFAULT NULL COMMENT '是否为子分类',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据字典分类表';

-- ----------------------------
-- Records of t_dictionary_type
-- ----------------------------
BEGIN;
INSERT INTO `t_dictionary_type` VALUES ('2e81620c1b74421cbe071224822e5725', '链路探活取数来源', 'Y', '2019-07-10', '2019-07-10', '000000', '000000', NULL, 'LIVE_SOURCE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('2e81620c1disodsobe071224822e5725', 'HTTP请求类型', 'Y', '2020-07-07', '2020-07-07', '000000', '000000', NULL, 'HTTP_METHOD_TYPE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('3ea3284fbca841bcbcda50afa0d8a24b', '单量计算方式', 'Y', '2019-04-04', '2019-04-04', '000000', '000000', NULL, 'VOLUME_CALC_STATUS', NULL);
INSERT INTO `t_dictionary_type` VALUES ('41a309a6d1e04105acfd5fb08200f0c5', 'http类型', 'Y', '2019-06-12', '2019-06-12', '000000', '000000', NULL, 'HTTP_TYPE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('470557d08abe4eb6a794764209c7763d', '页面分类', 'Y', '2019-06-12', '2019-06-12', '000000', '000000', NULL, 'PAGE_LEVEL', NULL);
INSERT INTO `t_dictionary_type` VALUES ('51a309asdsdfads8779fd5fb08200f03u', '业务域类型', 'Y', '2021-01-15', '2021-01-15', '000000', '000000', NULL, 'domain', NULL);
INSERT INTO `t_dictionary_type` VALUES ('6ba75716d726493783bfd64cce058110', 'PRESSURE_MACHINE_STATUS', 'Y', '2020-11-17', '2020-11-17', '000000', '000000', NULL, 'PRESSURE_MACHINE_STATUS', NULL);
INSERT INTO `t_dictionary_type` VALUES ('6ba75716d726493783bfd64cce058780', '链路探活时间', 'Y', '2019-07-10', '2019-07-10', '000000', '000000', NULL, 'LINK_LIVE_TIME', NULL);
INSERT INTO `t_dictionary_type` VALUES ('8c3c52c6889c435a8e055d988016e02a', '接口分类', 'Y', '2019-06-12', '2019-06-12', '000000', '000000', NULL, 'INTERFACE_LEVEL', NULL);
INSERT INTO `t_dictionary_type` VALUES ('944da50e5a334e128b34df906971b113', '应用上传信息类型', 'Y', '2019-03-29', '2019-03-29', '000000', '000000', NULL, 'UPLOAD_INFO_TYPE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('ada9370f26ac4c79acbac0ad7acb0992', '数据库类型', 'Y', '2018-08-30', '2018-08-30', NULL, NULL, NULL, 'DBTYPE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('b8f4ce1a989b4f19a842e08975ee3eb1', '链路类型', 'Y', '2018-12-24', '2018-12-24', '578992', NULL, NULL, 'LINK_TYPE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('ca888ed801664c81815d8c4f5b8dff0c', '白名单', 'Y', '2018-07-05', '2018-07-05', '', '', '', 'WLIST', '');
INSERT INTO `t_dictionary_type` VALUES ('d69be19362c4461fb7e84dfbc21f1747', '链路等级', 'Y', '2018-07-05', '2018-07-05', '', '', '', 'LINKRANK', '');
INSERT INTO `t_dictionary_type` VALUES ('ec18bced105c41018cfbcaf6b3327b9a', 'MQ消息类型', 'Y', '2018-07-30', '2018-07-30', '557092', NULL, NULL, 'MQMSG', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f48c856d2aec493a8902da7485720404', 'JOB调度间隔', 'Y', '2019-06-17', '2019-06-17', '000000', '000000', NULL, 'JOB_INTERVAL', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33eece', '链路所属模块', 'Y', '2019-04-04', '2019-04-04', '000000', '000000', NULL, 'LINK_MODULE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33eegb', '云服务器', 'Y', '2020-03-12', '2020-03-12', '000000', '000000', NULL, 'CLOUD_SERVER', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33eegc', '场景状态', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, 'SCENE_MANAGE_STATUS', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33eegd', '是否删除', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, 'IS_DELETED', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33eege', '脚本类型', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, 'SCRIPT_TYPE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33eegf', '文件类型', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, 'FILE_TYPE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33eegg', 'SLA指标类型', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, 'SLA_TARGER_TYPE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33eegh', '数值比较', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, 'COMPARE_TYPE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33eegk', '存活状态', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, 'LIVE_STATUS', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33kklll', '可验证的数据源类型', 'Y', '2021-01-06', '2021-01-06', '000000', '000000', NULL, 'VERIFY_DATASOURCE_TYPE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33wer2', '影子消费者', 'Y', '2021-02-18', '2021-02-18', '000000', '000000', NULL, 'SHADOW_CONSUMER', NULL);
COMMIT;

-- ----------------------------
-- Table structure for t_ebm_producer
-- ----------------------------
DROP TABLE IF EXISTS `t_ebm_producer`;
CREATE TABLE `t_ebm_producer` (
  `TEP_ID` bigint(20) NOT NULL COMMENT 'ESB/IBM生产者id',
  `MSG_TYPE` varchar(5) NOT NULL COMMENT '消息类型(1表示ESB,2表示IBM)',
  `DICT_TYPE` varchar(32) NOT NULL COMMENT '数据字典类型（ID值）',
  `MSG_HOST` varchar(30) DEFAULT NULL COMMENT '消息地址',
  `MSG_PORT` varchar(10) DEFAULT NULL COMMENT '消息端口',
  `QUEUE_MANAGER` varchar(50) DEFAULT NULL COMMENT '队列管理器',
  `QUEUE_CHANNEL` varchar(50) DEFAULT NULL COMMENT '系统队列通道',
  `CCSID` varchar(20) DEFAULT NULL COMMENT '编码字符集标识符',
  `ESBCODE` varchar(1024) DEFAULT NULL COMMENT 'ESBCODE',
  `REQUEST_COMOUT` varchar(1024) DEFAULT NULL COMMENT 'REQUESTCOMOUT',
  `SLEEP_TIME` bigint(20) DEFAULT NULL COMMENT '消息休眠时间,默认为毫秒',
  `MSG_COUNT` bigint(20) DEFAULT NULL COMMENT '消息发送数量,默认为条',
  `MSG_SUCCESS_COUNT` bigint(20) DEFAULT NULL COMMENT '消息发送成功条数',
  `THREAD_COUNT` bigint(10) DEFAULT NULL COMMENT '线程数量',
  `MESSAGE_SIZE` bigint(20) DEFAULT NULL COMMENT '消息体大小',
  `PRODUCE_STATUS` varchar(5) DEFAULT '0' COMMENT '生产消息状态, 0未生产消息 1正在生产消息 2已停止生产消息 3生产消息失败 4正在生产但是有发送失败的数据',
  `PRODUCE_START_TIME` datetime DEFAULT NULL COMMENT '开始生产消息时间',
  `PRODUCE_END_TIME` datetime DEFAULT NULL COMMENT '停止生产消息时间',
  `LAST_PRODUCE_TIME` datetime DEFAULT NULL COMMENT '上次停止生产时间',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`TEP_ID`) USING BTREE,
  KEY `TEP_INDEX1` (`PRODUCE_START_TIME`,`PRODUCE_END_TIME`) USING BTREE,
  KEY `TEP_INDEX2` (`PRODUCE_STATUS`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='ESB/IBM生产消息数据表';

-- ----------------------------
-- Records of t_ebm_producer
-- ----------------------------
BEGIN;
INSERT INTO `t_ebm_producer` VALUES (6446074431516184576, '2', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.25.15', '9002', 'SUBQM01', 'ECS.CLIENT', '1208', 'PT_TRO_TESTMQ2', 'PT_TRO_TESTMQ2', 2, 30, 31, NULL, NULL, '2', '2018-09-14 06:03:12', '2018-09-14 06:03:27', '2018-09-14 06:02:51', '2018-09-14 02:38:26', '2018-09-14 06:03:27');
INSERT INTO `t_ebm_producer` VALUES (6446085230829768704, '1', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.44.51', '1418', 'MQ2_02', 'ADMIN.CHANNEL', '1208', 'PT_ESB_OA2ESB_PRO_TEST', 'QU_LMS_REQUEST_COM_IN', 1, 1000, 21, 100, 200, '2', '2019-05-29 12:32:38', '2019-05-29 12:32:49', '2019-05-29 10:54:49', '2018-09-14 03:21:21', '2019-05-29 12:32:48');
INSERT INTO `t_ebm_producer` VALUES (6446126769203318784, '1', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.44.50', '1418', 'MQ2_01', 'ADMIN.CHANNEL', '1208', 'PT_ESB_OA2ESB_PRO_TEST', 'QU_LMS_REQUEST_COM_IN', 1, 100, 11, 100, 1024, '2', '2018-11-07 17:05:32', '2018-11-07 17:06:37', '2018-10-17 16:52:29', '2018-09-14 06:06:25', '2018-11-07 17:12:38');
INSERT INTO `t_ebm_producer` VALUES (6446353920372510720, '1', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.44.50', '1418', 'MQ2_01', 'ADMIN.CHANNEL', '1208', 'PT_ESB_DOP2ESB_PRO_TEST', 'QU_LMS_REQUEST_COM_IN', 1, 1000, 80200, 100, 1024, '2', '2018-11-16 16:14:57', '2018-11-16 16:16:05', '2018-11-16 12:10:13', '2018-09-14 21:09:02', '2018-11-16 16:16:04');
INSERT INTO `t_ebm_producer` VALUES (6446358505782775808, '2', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.25.15', '9002', 'SUBQM01', 'ECS.CLIENT', '1208', 'PT_TRO_TESTMQ', 'PT_TRO_TESTMQ', 1, 1, 1000, 1, 2500, '2', '2019-05-29 10:54:09', '2019-05-29 10:54:14', '2019-05-29 10:52:38', '2018-09-14 21:27:15', '2019-05-29 10:54:13');
INSERT INTO `t_ebm_producer` VALUES (6458969562846072832, '2', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.25.15', '9002', 'SUBQM01', 'ECS.CLIENT', '1208', 'PT_TRO_TESTMQ_1019', 'PT_TRO_TESTMQ_1019', 1, 1000, NULL, 100, 2048, '2', '2018-11-07 15:13:35', '2018-11-07 15:14:20', '2018-11-07 14:54:44', '2018-10-19 16:39:05', '2018-11-07 15:14:20');
INSERT INTO `t_ebm_producer` VALUES (6469009585368731648, '1', 'ec18bced105c41018cfbcaf6b3327b9a', '0.0.0.0', '1111', '测试ECSMQ', '测试ECSMQ', '1028', 'PT_ECSMQ', 'T_OPT_TFR_UNLOAD', 1, 100, NULL, 100, 1024, '2', '2019-05-29 10:55:12', '2019-05-29 10:55:17', '2018-11-16 15:29:27', '2018-11-16 09:34:33', '2019-05-29 10:55:17');
INSERT INTO `t_ebm_producer` VALUES (6469101031488753664, '1', 'ec18bced105c41018cfbcaf6b3327b9a', '10.10.10.10', '1000', 'TEST', 'TEST', '1208', 'PT_ECSMQ', 'T_OPT_TFR_UNLOAD', 1, 2000, NULL, 10, 1024, '2', '2019-05-29 10:48:09', '2019-05-29 10:48:16', '2018-11-16 16:10:21', '2018-11-16 15:37:56', '2019-05-29 10:48:16');
COMMIT;

-- ----------------------------
-- Table structure for t_exception_info
-- ----------------------------
DROP TABLE IF EXISTS `t_exception_info`;
CREATE TABLE `t_exception_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(512) DEFAULT NULL COMMENT '异常类型',
  `code` varchar(512) DEFAULT NULL COMMENT '异常编码',
  `agent_code` varchar(512) DEFAULT NULL COMMENT 'agent异常编码',
  `description` varchar(512) DEFAULT NULL COMMENT '异常描述',
  `suggestion` varchar(512) DEFAULT NULL COMMENT '处理建议',
  `count` bigint(20) DEFAULT '0' COMMENT '发生次数',
  `gmt_create` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `gmt_modified` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '软删',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_exception_info
-- ----------------------------
BEGIN;
INSERT INTO `t_exception_info` VALUES (1, '调用栈异常', 'EA0001', 'agent-0001', '插件注册拦截器初始化失败', '目前仅neo4j中间件插件会抛出此异常，如果失败，可能的原因是jar包冲突导致的类冲突问题。', 0, '2021-01-21 19:45:14.941', '2021-01-21 19:45:14.941', 0, NULL);
INSERT INTO `t_exception_info` VALUES (2, '调用栈异常', 'EA0002', 'agent-0002', '开启监听器执行失败', '监听器注册或返回结果异常(代码层面)。', 0, '2021-01-21 19:45:14.941', '2021-01-21 19:45:14.941', 0, NULL);
INSERT INTO `t_exception_info` VALUES (3, '调用栈异常', 'EA0003', 'agent-0003', '监听器执行返回结果为失败', '需要根据详情针对对应的中间件插件进行排查。', 0, '2021-01-21 19:45:14.941', '2021-01-21 19:45:14.941', 0, NULL);
INSERT INTO `t_exception_info` VALUES (4, '调用栈异常', 'EA0004', 'agent-0004', 'agent启动时，获取配置失败：白名单、影子数据源配置、', '1、接口返回值为null；2、接口返回数据内容异常，无所需数据；3、配置处理异常，需根据异常信息结合日志判断；', 0, '2021-01-21 19:45:14.941', '2021-01-21 19:45:14.941', 0, NULL);
INSERT INTO `t_exception_info` VALUES (5, '调用栈异常', 'EA0005', 'agent-0005', 'agent实时更新配置异常', '需根据异常上报内容具体查看。', 0, '2021-01-21 19:45:14.941', '2021-01-21 19:45:14.941', 0, NULL);
INSERT INTO `t_exception_info` VALUES (6, '调用栈异常', 'EA0006', 'agent-0006', '客户端有效性验证失败', '1、检查license；2、查看异常上报信息。', 0, '2021-01-21 19:45:14.941', '2021-01-21 19:45:14.941', 0, NULL);
INSERT INTO `t_exception_info` VALUES (7, '调用栈异常', 'EA0007', 'agent-0007', '采样率配置获取连接zk失败/采样率配置注册zk监听器失败/采样率配置启动zk监听器失败', '1、确认配置文件zk地址；2、确认网络是否可访问。', 0, '2021-01-21 19:45:14.941', '2021-01-21 19:45:14.941', 0, NULL);
INSERT INTO `t_exception_info` VALUES (8, '调用栈异常', 'EA0008', 'agent-0008', '压测开关关闭', '检测压测配置', 0, '2021-01-21 19:45:14.941', '2021-01-21 19:45:14.941', 0, NULL);
INSERT INTO `t_exception_info` VALUES (9, '调用栈异常', 'EA0009', 'web-filter-0001', 'filter注册到容器tomcat/jetty失败', '暂无。', 0, '2021-01-21 19:45:14.941', '2021-01-21 19:45:14.941', 0, NULL);
INSERT INTO `t_exception_info` VALUES (10, '调用栈异常', 'EA00010', 'EA00010', '有以下rpcid 日志，但没有对应的调用栈rpcid节点信息：xxxxxxx、yyyyyyy', '查看调用栈节日志：显示未展示的 rpcid', 0, '2021-01-21 19:45:14.941', '2021-01-21 19:45:14.941', 0, NULL);
INSERT INTO `t_exception_info` VALUES (11, 'Job异常', 'ESJ0001', 'shadow-job-0001', '影子job注册失败', '1、影子job类不存在or冲突\n2、未找到trigger相关数据', 0, '2021-01-21 19:45:14.941', '2021-01-21 19:45:14.941', 0, NULL);
INSERT INTO `t_exception_info` VALUES (12, '压测检查异常', 'EPC0001', 'EPC0001', '应用在线节点数与节点总数不一致', '1、应用管理-应用详情-编辑，重新编辑节点总数量，保持与在线节点数一致。', 0, '2021-01-21 19:45:14.941', '2021-01-21 19:45:14.941', 0, NULL);
INSERT INTO `t_exception_info` VALUES (13, '压测检查异常', 'EPC0002', 'EPC0002', '应用各实例 Agent 版本不一致', '1、应用管理-应用详情-节点管理，查看每个节点的 Agent 版本，并升级到同一最新版本。', 0, '2021-01-21 19:45:14.941', '2021-01-21 19:45:14.941', 0, NULL);
INSERT INTO `t_exception_info` VALUES (14, '挡板异常', 'EM0001', 'link-guard-enhance-0001', '挡板增强失败', '挡板代码问题。', 0, '2021-01-21 19:45:14.942', '2021-01-21 19:45:14.942', 0, NULL);
INSERT INTO `t_exception_info` VALUES (15, '挡板异常', 'EM0002', 'link-guard-enhance-0002', '获取挡板列表失败', '暂无。', 0, '2021-01-21 19:45:14.942', '2021-01-21 19:45:14.942', 0, NULL);
INSERT INTO `t_exception_info` VALUES (16, '挡板异常', 'EM0003', 'link-guard-enhance-0003', '挡板编译失败异常', '暂无。', 0, '2021-01-21 19:45:14.942', '2021-01-21 19:45:14.942', 0, NULL);
INSERT INTO `t_exception_info` VALUES (17, '影子库表异常', 'ESDB0001', 'datasource-0001', '数据源获取链接失败', '1、应用代码业务库配置为空\n2、应用代码影子库配置为空\n3、应用代码无法连接业务库(网络无法访问/帐号密码不正确)\n4、应用代码无法连接影子库(网络无法访问/帐号密码不正确)', 0, '2021-01-21 19:45:14.942', '2021-01-21 19:45:14.942', 0, NULL);
INSERT INTO `t_exception_info` VALUES (18, '影子库表异常', 'ESDB0002', 'datasource-0002', '没有配置对应的影子表或影子库，', '应用影子库表配置中，该业务库无影子库配置或未配置该业务库的映射', 0, '2021-01-21 19:45:14.942', '2021-01-21 19:45:14.942', 0, NULL);
INSERT INTO `t_exception_info` VALUES (19, '影子库表异常', 'ESDB0003', 'datasource-0003', '影子库配置不正确', '1、业务库影子库映射未配置业务库/未配置影子库/业务库与影子库映射<dataSources>\n2、业务库影子库映射未配置数据源调停者<datasourceMediator>\n3、业务库影子库映射中业务库 未配置url或未配置username或未配置id\n4、业务库影子库映射中数据源调停者(datasourceMediator)的id与业务库配置id不同\n5、业务库影子库映射中影子库配置以下参数缺失或配置不正确(url/username/password/driverClassName),为空、未配置、为\"null\"\n6、应用运行中，实际业务库的url与映射配置中的业务库url不一致/实际业务库使用的username与映射配置中的业务库username不一致', 0, '2021-01-21 19:45:14.942', '2021-01-21 19:45:14.942', 0, NULL);
INSERT INTO `t_exception_info` VALUES (20, '影子库表异常', 'ESDB0004', 'datasource-0004', '没有配置对应的影子表', '应用影子表配置中，该业务无影子表的映射', 0, '2021-01-21 19:45:14.942', '2021-01-21 19:45:14.942', 0, NULL);
INSERT INTO `t_exception_info` VALUES (21, '影子库表异常', 'ESDB0005', 'ESDB0005', 'agent启动时，获取配置失败', '1、接口返回值为null\n2、接口返回数据内容异常，无所需数据\n3、配置处理异常，需根据异常信息结合日志判断', 0, '2021-01-21 19:45:14.942', '2021-01-21 19:45:14.942', 0, NULL);
INSERT INTO `t_exception_info` VALUES (22, 'MQ异常', 'EMQ0001', 'MQ-0001', '消息队列消费端启动失败/订阅队列失败/等初始化异常', '1、网络连接\n2、topic不存在', 0, '2021-01-21 19:45:14.942', '2021-01-21 19:45:14.942', 0, NULL);
INSERT INTO `t_exception_info` VALUES (23, 'MQ异常', 'EMQ0002', 'MQ-0002', '消费者配置异常，无法消费', '1、SF-KAFKA：消费者配置systemIdToken或者templateTopic参数为空。', 0, '2021-01-21 19:45:14.942', '2021-01-21 19:45:14.942', 0, NULL);
INSERT INTO `t_exception_info` VALUES (24, '白名单异常', 'EWL0001', 'EWL0001', 'agent启动时，获取白名单配置失败', '1、接口返回值为null\n2、接口返回数据内容异常，无所需数据\n3、配置处理异常，需根据异常信息结合日志判断', 0, '2021-01-21 19:45:14.942', '2021-01-21 19:45:14.942', 0, NULL);
INSERT INTO `t_exception_info` VALUES (25, '白名单异常', 'EWL0002', 'whiteList-0001', '未配置白名单', '添加白名单方法', 0, '2021-01-21 19:45:14.942', '2021-01-21 19:45:14.942', 0, NULL);
COMMIT;

-- ----------------------------
-- Table structure for t_fast_debug_config_info
-- ----------------------------
DROP TABLE IF EXISTS `t_fast_debug_config_info`;
CREATE TABLE `t_fast_debug_config_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL COMMENT '调试名称',
  `request_url` varchar(256) NOT NULL COMMENT '完整url',
  `headers` varchar(2048) DEFAULT NULL,
  `cookies` varchar(2048) DEFAULT NULL COMMENT 'cookies',
  `body` longtext COMMENT '请求体',
  `http_method` varchar(128) DEFAULT NULL COMMENT '请求方法',
  `timeout` int(1) DEFAULT NULL COMMENT '响应超时时间',
  `is_redirect` tinyint(1) DEFAULT NULL COMMENT '是否重定向',
  `business_link_id` bigint(20) NOT NULL COMMENT '业务活动id',
  `content_type` varchar(512) DEFAULT NULL COMMENT 'contentType数据',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未调试，1，调试中',
  `gmt_create` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `gmt_modified` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '软删',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人',
  `modifier_id` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_fast_debug_config_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_fast_debug_exception
-- ----------------------------
DROP TABLE IF EXISTS `t_fast_debug_exception`;
CREATE TABLE `t_fast_debug_exception` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `result_id` bigint(20) DEFAULT NULL COMMENT '结果id',
  `trace_id` varchar(512) DEFAULT NULL COMMENT 'traceId',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `agent_Id` longtext COMMENT 'agentid',
  `rpc_id` varchar(512) DEFAULT NULL COMMENT 'rpc_id',
  `application_Name` varchar(512) NOT NULL COMMENT '应用名',
  `type` varchar(512) DEFAULT NULL COMMENT '异常类型',
  `code` varchar(512) DEFAULT NULL COMMENT '异常编码',
  `description` longtext COMMENT '异常描述',
  `suggestion` varchar(512) DEFAULT NULL COMMENT '处理建议',
  `time` varchar(512) DEFAULT NULL COMMENT '异常时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '软删',
  `gmt_create` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `gmt_modified` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index_traceId` (`trace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_fast_debug_exception
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_fast_debug_machine_performance
-- ----------------------------
DROP TABLE IF EXISTS `t_fast_debug_machine_performance`;
CREATE TABLE `t_fast_debug_machine_performance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trace_id` varchar(512) DEFAULT NULL COMMENT 'traceId',
  `rpc_id` varchar(512) DEFAULT NULL COMMENT 'rpcid',
  `log_type` tinyint(4) DEFAULT NULL COMMENT '服务端、客户端',
  `index` varchar(128) DEFAULT NULL COMMENT '性能类型，beforeFirst/beforeLast/afterFirst/afterLast/exceptionFirst/exceptionLast',
  `cpu_usage` decimal(10,4) DEFAULT '0.0000' COMMENT 'cpu利用率',
  `cpu_load` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT 'cpu load',
  `memory_usage` decimal(10,4) DEFAULT '0.0000' COMMENT '没存利用率',
  `memory_total` decimal(20,2) DEFAULT '0.00' COMMENT '堆内存总和',
  `io_wait` decimal(10,4) DEFAULT '0.0000' COMMENT 'io 等待率',
  `young_gc_count` bigint(20) DEFAULT NULL,
  `young_gc_time` bigint(20) DEFAULT NULL,
  `old_gc_count` bigint(20) DEFAULT NULL,
  `old_gc_time` bigint(20) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0: 正常 1： 删除',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_update` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_fast_debug_machine_performance
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_fast_debug_result
-- ----------------------------
DROP TABLE IF EXISTS `t_fast_debug_result`;
CREATE TABLE `t_fast_debug_result` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `config_id` bigint(20) NOT NULL COMMENT '调试配置id',
  `name` varchar(256) NOT NULL COMMENT '调试名称',
  `business_link_name` varchar(256) NOT NULL COMMENT '业务活动名',
  `request_url` varchar(1024) NOT NULL COMMENT '完整url',
  `http_method` varchar(256) NOT NULL COMMENT '调试名称',
  `request` longtext COMMENT '请求体，包含url,body,header',
  `leakage_check_data` longtext COMMENT '漏数检查数据,每次自己报存一份，并保持结果',
  `response` longtext COMMENT '请求返回体',
  `error_message` longtext,
  `trace_id` varchar(512) DEFAULT NULL COMMENT '通过response解析出来traceId',
  `call_time` bigint(20) DEFAULT NULL COMMENT '调用时长',
  `gmt_create` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `gmt_modified` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '软删',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '操作人',
  `debug_result` tinyint(1) DEFAULT NULL COMMENT '0:失败；1：成功；调试中根据config中status判断',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_fast_debug_result
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_fast_debug_stack_info
-- ----------------------------
DROP TABLE IF EXISTS `t_fast_debug_stack_info`;
CREATE TABLE `t_fast_debug_stack_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(255) DEFAULT NULL,
  `agent_id` varchar(255) DEFAULT NULL,
  `trace_id` varchar(512) DEFAULT NULL COMMENT 'traceId',
  `rpc_id` varchar(512) NOT NULL COMMENT 'rpcid',
  `level` varchar(64) DEFAULT NULL COMMENT '日志级别',
  `type` tinyint(4) DEFAULT NULL COMMENT '服务端，客户端',
  `content` longtext COMMENT 'stack信息',
  `is_stack` tinyint(1) DEFAULT NULL COMMENT '是否调用栈日志',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0: 正常 1： 删除',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_update` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `index_traceId` (`trace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_fast_debug_stack_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_file_manage
-- ----------------------------
DROP TABLE IF EXISTS `t_file_manage`;
CREATE TABLE `t_file_manage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '文件名称',
  `file_size` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '文件大小：2MB',
  `file_ext` varchar(16) COLLATE utf8_bin DEFAULT NULL COMMENT '文件后缀',
  `file_type` tinyint(4) NOT NULL COMMENT '文件类型：0-脚本文件 1-数据文件 2-脚本jar文件 3-jmeter ext jar',
  `file_extend` text COLLATE utf8_bin COMMENT '{\n  “dataCount”:”数据量”,\n  “isSplit”:”是否拆分”,\n  “topic”:”MQ主题”,\n  “nameServer”:”mq nameServer”,\n}',
  `customer_id` bigint(20) NOT NULL COMMENT '客户id（当前登录用户对应的admin的id，数据隔离使用）',
  `upload_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '上传时间',
  `upload_path` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '上传路径：相对路径',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `gmt_create` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `gmt_update` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_file_manage
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_first_link_mnt
-- ----------------------------
DROP TABLE IF EXISTS `t_first_link_mnt`;
CREATE TABLE `t_first_link_mnt` (
  `LINK_ID` varchar(36) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '主键id',
  `LINK_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '链路名称',
  `SECOND_LINKS` text NOT NULL COMMENT '二级业务链路id串',
  `LINK_TPS` bigint(19) DEFAULT NULL COMMENT '链路TPS',
  `LINK_TPS_RULE` text COMMENT '一级链路TPS计算规则',
  `USE_YN` int(1) DEFAULT NULL COMMENT '是否可用(0表示未启用,1表示启用)',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '插入时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '变更时间',
  `remark` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注(预留)',
  PRIMARY KEY (`LINK_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='一级链路管理表';

-- ----------------------------
-- Records of t_first_link_mnt
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_indicators_collect_config
-- ----------------------------
DROP TABLE IF EXISTS `t_indicators_collect_config`;
CREATE TABLE `t_indicators_collect_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(20) NOT NULL COMMENT '压测引擎名称',
  `type` varchar(50) NOT NULL COMMENT '关联字典表dictionary_id',
  `collect_time` int(3) DEFAULT NULL COMMENT '采集周期',
  `plugin_path` varchar(1000) DEFAULT NULL COMMENT '指标抓去插件',
  `class_name` varchar(100) DEFAULT NULL COMMENT '启动类名称',
  `jar_name` varchar(100) DEFAULT NULL COMMENT 'jar名称',
  `status` tinyint(1) DEFAULT '0' COMMENT '0-可用 1-不可用',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0-未删除、1-已删除',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='指标采集配置';

-- ----------------------------
-- Records of t_indicators_collect_config
-- ----------------------------
BEGIN;
INSERT INTO `t_indicators_collect_config` VALUES (1, '阿里云ECS', 'fc77d5f788dc45528b039d5b1b4fdb4a', 20, NULL, NULL, NULL, 0, 0, '2020-03-12 23:31:58', '2020-03-12 23:55:28');
COMMIT;

-- ----------------------------
-- Table structure for t_isolation_app_item_config
-- ----------------------------
DROP TABLE IF EXISTS `t_isolation_app_item_config`;
CREATE TABLE `t_isolation_app_item_config` (
  `item_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `application_id` bigint(20) NOT NULL COMMENT '应用ID',
  `type` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '明细类型',
  `key_id` bigint(20) NOT NULL COMMENT '注册中心ID、Rocket集群名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`item_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_isolation_app_item_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_isolation_app_main_config
-- ----------------------------
DROP TABLE IF EXISTS `t_isolation_app_main_config`;
CREATE TABLE `t_isolation_app_main_config` (
  `application_id` bigint(20) NOT NULL COMMENT '应用ID',
  `check_network` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否校验网络',
  `mock_app_nodes` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '[]' COMMENT '应用隔离MOCK节点',
  `status` tinyint(255) NOT NULL COMMENT '当前状态  0.未隔离  1. 隔离中  2.已隔离 ',
  `iso_app_rets` text COLLATE utf8_bin COMMENT '应用隔离上传隔离信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `error_msg` varchar(5000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`application_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_isolation_app_main_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_isolation_app_reg_config
-- ----------------------------
DROP TABLE IF EXISTS `t_isolation_app_reg_config`;
CREATE TABLE `t_isolation_app_reg_config` (
  `reg_type` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '注册中心类型',
  `reg_addr` varchar(1024) COLLATE utf8_bin NOT NULL COMMENT '注册中心地址',
  `reg_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `enable` tinyint(255) NOT NULL COMMENT '是否启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`reg_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_isolation_app_reg_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_leakcheck_config
-- ----------------------------
DROP TABLE IF EXISTS `t_leakcheck_config`;
CREATE TABLE `t_leakcheck_config` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `business_activity_id` bigint(11) NOT NULL COMMENT '业务活动id',
  `datasource_id` bigint(11) NOT NULL COMMENT '数据源id',
  `leakcheck_sql_ids` text NOT NULL COMMENT '漏数sql主键集合，逗号分隔',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否有效 0:有效;1:无效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_leakcheck_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_leakcheck_config_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_leakcheck_config_detail`;
CREATE TABLE `t_leakcheck_config_detail` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `datasource_id` bigint(11) NOT NULL COMMENT '数据源id',
  `sql_content` varchar(255) NOT NULL COMMENT '漏数sql',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否有效 0:有效;1:无效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_leakcheck_config_detail
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_leakverify_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_leakverify_detail`;
CREATE TABLE `t_leakverify_detail` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `result_id` bigint(11) NOT NULL COMMENT '验证结果id',
  `leak_sql` varchar(500) NOT NULL COMMENT '漏数sql',
  `status` tinyint(1) DEFAULT '0' COMMENT '是否漏数 0:正常;1:漏数;2:未检测;3:检测失败',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否有效 0:有效;1:无效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_leakverify_detail
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_leakverify_result
-- ----------------------------
DROP TABLE IF EXISTS `t_leakverify_result`;
CREATE TABLE `t_leakverify_result` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `ref_type` tinyint(1) DEFAULT '0' COMMENT '引用类型 0:压测场景;1:业务流程;2:业务活动',
  `ref_id` bigint(11) NOT NULL COMMENT '引用id',
  `report_id` bigint(11) DEFAULT NULL COMMENT '报告id',
  `dbresource_id` bigint(11) NOT NULL COMMENT '数据源id',
  `dbresource_name` varchar(255) DEFAULT NULL COMMENT '数据源名称',
  `dbresource_url` varchar(500) DEFAULT NULL COMMENT '数据源地址',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否有效 0:有效;1:无效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_leakverify_result
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_link_bottleneck
-- ----------------------------
DROP TABLE IF EXISTS `t_link_bottleneck`;
CREATE TABLE `t_link_bottleneck` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `APP_NAME` varchar(50) NOT NULL DEFAULT '' COMMENT '应用名',
  `KEY_WORDS` varchar(200) NOT NULL DEFAULT '' COMMENT '关键字(TOPIC,JOB信息)',
  `BOTTLENECK_TYPE` tinyint(1) NOT NULL DEFAULT '4' COMMENT '瓶颈类型(1、基础资源负载及异常，2、异步处理，3、TPS/RT稳定性，4，RT响应时间)',
  `BOTTLENECK_LEVEL` tinyint(1) NOT NULL DEFAULT '3' COMMENT '瓶颈等级(1、严重，2、普通，3、正常)',
  `TEXT` mediumtext COMMENT '瓶颈文本',
  `CONTENT` mediumtext COMMENT 'JSON串',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `ACTIVE` char(1) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'Y' COMMENT '是否有效(Y/N)',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_appname` (`APP_NAME`) USING BTREE,
  KEY `idx_keywords` (`KEY_WORDS`) USING BTREE,
  KEY `idx_createTime` (`CREATE_TIME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='链路瓶颈';

-- ----------------------------
-- Records of t_link_bottleneck
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_link_detection
-- ----------------------------
DROP TABLE IF EXISTS `t_link_detection`;
CREATE TABLE `t_link_detection` (
  `LINK_DETECTION_ID` bigint(19) NOT NULL COMMENT '主键id',
  `APPLICATION_ID` bigint(19) DEFAULT NULL COMMENT '应用id',
  `SHADOWLIB_CHECK` int(1) DEFAULT '0' COMMENT '影子库整体同步检测状态(0未启用,1正在检测,2检测成功,3检测失败)',
  `SHADOWLIB_ERROR` text COMMENT '影子库检测失败内容',
  `CACHE_CHECK` int(1) DEFAULT '0' COMMENT '缓存预热校验状态(0未启用,1正在检测,2检测成功,3检测失败)',
  `CACHE_ERROR` text COMMENT '缓存预热实时检测失败内容',
  `WLIST_CHECK` int(1) DEFAULT '0' COMMENT '白名单校验状态(0未启用,1正在检测,2检测成功,3检测失败)',
  `WLIST_ERROR` text COMMENT '白名单异常错误信息',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '插入时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '变更时间',
  PRIMARY KEY (`LINK_DETECTION_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='链路检测表';

-- ----------------------------
-- Records of t_link_detection
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_link_guard
-- ----------------------------
DROP TABLE IF EXISTS `t_link_guard`;
CREATE TABLE `t_link_guard` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '数据库唯一主键',
  `application_name` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '项目名称',
  `application_id` bigint(19) DEFAULT NULL COMMENT '应用id',
  `method_info` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '出口信息',
  `groovy` longtext COLLATE utf8_bin COMMENT 'GROOVY脚本',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(4) unsigned DEFAULT '0' COMMENT '是否有效 0:有效;1:无效',
  `is_enable` tinyint(4) DEFAULT '1' COMMENT '0:未启用；1:启用',
  `remark` varchar(1024) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_app_id` (`application_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_link_guard
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_link_live_config
-- ----------------------------
DROP TABLE IF EXISTS `t_link_live_config`;
CREATE TABLE `t_link_live_config` (
  `live_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `application_name` varchar(30) NOT NULL COMMENT '应用名称',
  `interface_name` varchar(100) NOT NULL COMMENT '接口名称',
  `interface_type` varchar(20) NOT NULL COMMENT '接口类型',
  `threshold` varchar(10) NOT NULL COMMENT '告警阈值，事务成功率',
  `alarm_policy` varchar(30) NOT NULL COMMENT '告警策略，默认是WX_OFFICIAL',
  `live_source` char(1) NOT NULL COMMENT '探活取数来源,1SPT 2PRADAR',
  `entry_flag` char(1) DEFAULT NULL COMMENT '是否入口压测链路，是Y 否N',
  `scene_name` varchar(50) DEFAULT NULL COMMENT '场景名称，仅入口或SPT来源时需要填写',
  `aswan_id` varchar(20) DEFAULT NULL COMMENT '阿斯旺ID，仅入口或SPT来源时需要填写',
  `live_status` char(1) NOT NULL DEFAULT '0' COMMENT '探活状态，0未开启, 1开启',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`live_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='链路探活配置表';

-- ----------------------------
-- Records of t_link_live_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_link_live_history
-- ----------------------------
DROP TABLE IF EXISTS `t_link_live_history`;
CREATE TABLE `t_link_live_history` (
  `live_time` bigint(20) NOT NULL COMMENT '探活时间戳',
  `live_id` bigint(20) NOT NULL COMMENT '探活ID',
  `success_rate` decimal(6,4) NOT NULL COMMENT '事务成功率',
  KEY `idx_live_id_time` (`live_time`,`live_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='链路探活历史数据';

-- ----------------------------
-- Records of t_link_live_history
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_link_manage_table
-- ----------------------------
DROP TABLE IF EXISTS `t_link_manage_table`;
CREATE TABLE `t_link_manage_table` (
  `LINK_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `LINK_NAME` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '链路名称',
  `ENTRACE` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '链路入口',
  `PT_ENTRACE` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '影子入口',
  `CHANGE_BEFORE` longtext CHARACTER SET utf8 COMMENT '技术链路变更前',
  `CHANGE_AFTER` longtext CHARACTER SET utf8 COMMENT '技术链路变更后',
  `CHANGE_REMARK` longtext CHARACTER SET utf8 COMMENT '变化差异',
  `IS_CHANGE` tinyint(4) DEFAULT NULL COMMENT '是否有变更 0:正常；1:已变更',
  `IS_JOB` tinyint(2) DEFAULT '0' COMMENT '任务类型 0:普通入口；1:定时任务',
  `CUSTOMER_ID` bigint(20) DEFAULT NULL COMMENT '租户id',
  `USER_ID` bigint(20) DEFAULT NULL COMMENT '用户id',
  `IS_DELETED` tinyint(4) DEFAULT '0' COMMENT '是否有效 0:有效;1:无效',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `APPLICATION_NAME` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '应用名',
  `CHANGE_TYPE` tinyint(4) DEFAULT NULL COMMENT '变更类型: 1:无流量调用通知;2:添加调用关系通知',
  `CAN_DELETE` tinyint(4) DEFAULT '0' COMMENT '是否可以删除 0:可以删除;1:不可以删除',
  `features` text,
  PRIMARY KEY (`LINK_ID`) USING BTREE,
  KEY `T_LINK_MNT_INDEX1` (`LINK_NAME`) USING BTREE,
  KEY `T_LINK_MNT_INDEX2` (`ENTRACE`(255)) USING BTREE,
  KEY `T_LINK_MNT_INDEX3` (`CREATE_TIME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='链路管理表';

-- ----------------------------
-- Records of t_link_manage_table
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_link_mnt
-- ----------------------------
DROP TABLE IF EXISTS `t_link_mnt`;
CREATE TABLE `t_link_mnt` (
  `LINK_ID` bigint(19) NOT NULL COMMENT '主键id',
  `LINK_NAME` varchar(200) DEFAULT NULL COMMENT '链路名称',
  `TECH_LINKS` text COMMENT '业务链路下属技术链路ids',
  `LINK_DESC` varchar(200) DEFAULT NULL COMMENT '链路说明',
  `LINK_TYPE` int(1) DEFAULT NULL COMMENT '1: 表示业务链路; 2: 表示技术链路; 3: 表示既是业务也是技术链路; ',
  `ASWAN_ID` varchar(200) DEFAULT NULL COMMENT '阿斯旺链路id',
  `LINK_ENTRENCE` varchar(200) DEFAULT NULL COMMENT '链路入口(http接口)',
  `RT_SA` varchar(10) DEFAULT NULL COMMENT '请求达标率(%)',
  `RT` varchar(10) DEFAULT NULL COMMENT '请求标准毫秒值',
  `TPS` varchar(20) DEFAULT NULL COMMENT '吞吐量(每秒完成事务数量)',
  `TARGET_SUCCESS_RATE` varchar(10) DEFAULT NULL COMMENT '目标成功率(%)',
  `DICT_TYPE` varchar(50) DEFAULT NULL COMMENT '字典分类',
  `LINK_RANK` varchar(20) DEFAULT NULL COMMENT '链路等级',
  `PRINCIPAL_NO` varchar(10) DEFAULT NULL COMMENT '链路负责人工号',
  `USE_YN` int(1) DEFAULT NULL COMMENT '是否可用(0表示未启用,1表示启用)',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '插入时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '变更时间',
  `LINK_MODULE` varchar(5) DEFAULT NULL COMMENT '链路模块 1下单 2订单 3开单 4中转 5派送 6签收 7CUBC结算 10非主流程链路',
  `VOLUME_CALC_STATUS` varchar(5) DEFAULT NULL COMMENT '是否计算单量',
  `TARGET_TPS` varchar(20) DEFAULT NULL COMMENT '目标TPS',
  PRIMARY KEY (`LINK_ID`) USING BTREE,
  KEY `T_LINK_MNT_INDEX1` (`LINK_NAME`) USING BTREE,
  KEY `T_LINK_MNT_INDEX2` (`USE_YN`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='链路管理表';

-- ----------------------------
-- Records of t_link_mnt
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_link_research_live
-- ----------------------------
DROP TABLE IF EXISTS `t_link_research_live`;
CREATE TABLE `t_link_research_live` (
  `LIVE_ID` bigint(20) NOT NULL COMMENT '链路探活ID，主键',
  `SCENE_NAME` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '场景名称',
  `ASWAN_ID` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '场景ID',
  `IS_DELETED` int(5) NOT NULL DEFAULT '0' COMMENT '是否删除标志，1删除 0未删除',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime NOT NULL COMMENT '更新时间',
  `STATUS` varchar(5) COLLATE utf8_bin NOT NULL COMMENT '探活启动状态，0未启动 1启动',
  PRIMARY KEY (`LIVE_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_link_research_live
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_link_research_live_alarm
-- ----------------------------
DROP TABLE IF EXISTS `t_link_research_live_alarm`;
CREATE TABLE `t_link_research_live_alarm` (
  `ALARM_ID` bigint(20) NOT NULL COMMENT '主键，告警ID',
  `APPLICATION_NAME` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '应用名称',
  `INTERFACE_NAME` varchar(256) COLLATE utf8_bin NOT NULL COMMENT '接口名称',
  `ALARM_CONTENT` mediumtext COLLATE utf8_bin COMMENT '告警内容',
  `ALARM_PERSON` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '告警人',
  `ALARM_THRESHOLD` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '告警阈值',
  `SEND_RESULT` int(5) NOT NULL COMMENT '发送结果 1成功 2失败',
  `ALARM_TIME` datetime DEFAULT NULL COMMENT '告警时间',
  `CREATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ALARM_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_link_research_live_alarm
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_link_research_live_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_link_research_live_ref`;
CREATE TABLE `t_link_research_live_ref` (
  `ID` bigint(20) NOT NULL COMMENT '主键ID',
  `LIVE_ID` bigint(20) NOT NULL COMMENT '链路探活ID',
  `APPLICATION_ID` bigint(20) NOT NULL COMMENT '应用ID',
  `WLIST_ID` bigint(20) NOT NULL COMMENT '白名单ID',
  `ALARM_THRESHOLD` int(12) NOT NULL COMMENT '告警阈值',
  `ALARM_POLICY` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '告警策略',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `IDX_1` (`LIVE_ID`,`APPLICATION_ID`,`WLIST_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_link_research_live_ref
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_link_service_mnt
-- ----------------------------
DROP TABLE IF EXISTS `t_link_service_mnt`;
CREATE TABLE `t_link_service_mnt` (
  `LINK_SERVICE_ID` bigint(19) NOT NULL COMMENT '主键id',
  `LINK_ID` bigint(19) DEFAULT NULL COMMENT '链路id',
  `INTERFACE_NAME` varchar(1000) DEFAULT NULL COMMENT '接口名称',
  `INTERFACE_DESC` varchar(1000) DEFAULT NULL COMMENT '接口说明',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '插入时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`LINK_SERVICE_ID`) USING BTREE,
  KEY `T_LS_IDX1` (`LINK_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='基础链路服务关联表';

-- ----------------------------
-- Records of t_link_service_mnt
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_link_topology_info
-- ----------------------------
DROP TABLE IF EXISTS `t_link_topology_info`;
CREATE TABLE `t_link_topology_info` (
  `TLTI_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '链路拓扑图主键ID',
  `LINK_ID` bigint(20) DEFAULT NULL COMMENT '基础链路id',
  `LINK_NAME` varchar(32) NOT NULL COMMENT '链路名称',
  `ENTRANCE_TYPE` varchar(32) DEFAULT NULL COMMENT '入口类型 job dubbo mq http',
  `LINK_ENTRANCE` varchar(256) DEFAULT NULL COMMENT '链路入口',
  `NAME_SERVER` varchar(1024) DEFAULT NULL COMMENT '队列的 nameserver',
  `LINK_TYPE` varchar(50) DEFAULT NULL COMMENT '链路类型',
  `LINK_GROUP` varchar(32) DEFAULT NULL COMMENT '链路分组',
  `FROM_LINK_IDS` varchar(256) DEFAULT NULL COMMENT '上级链路id',
  `TO_LINK_IDS` varchar(256) DEFAULT NULL COMMENT '下级链路id',
  `SHOW_FROM_LINK_ID` varchar(256) DEFAULT NULL COMMENT '只显示上级ID',
  `SHOW_TO_LINK_ID` varchar(256) DEFAULT NULL COMMENT '只显示下级ID',
  `SECOND_LINK_ID` varchar(20) DEFAULT NULL COMMENT '二级链路Id',
  `SECOND_LINK_NAME` varchar(32) DEFAULT NULL COMMENT '二级链路名称',
  `APPLICATION_NAME` varchar(64) DEFAULT NULL COMMENT '应用名称',
  `VOLUME_CALC_STATUS` varchar(2) DEFAULT NULL COMMENT '1 计算订单量  2 计算运单量 3 不计算单量',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '插入时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '变更时间',
  PRIMARY KEY (`TLTI_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='透明流量链路拓扑';

-- ----------------------------
-- Records of t_link_topology_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_login_record
-- ----------------------------
DROP TABLE IF EXISTS `t_login_record`;
CREATE TABLE `t_login_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(512) DEFAULT NULL COMMENT '登录用户',
  `ip` varchar(128) DEFAULT NULL COMMENT '登录ip',
  `gmt_create` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `gmt_modified` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0: 正常 1： 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_login_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_machine_spec
-- ----------------------------
DROP TABLE IF EXISTS `t_machine_spec`;
CREATE TABLE `t_machine_spec` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `spec` varchar(125) COLLATE utf8_bin NOT NULL COMMENT '机器规格描述',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0: 正常 1： 删除',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_update` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_machine_spec
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_machine_task
-- ----------------------------
DROP TABLE IF EXISTS `t_machine_task`;
CREATE TABLE `t_machine_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `task_type` int(2) NOT NULL COMMENT '任务类型：1、开通任务 2、注销任务',
  `serial_no` varchar(125) COLLATE utf8_bin NOT NULL COMMENT '序列号',
  `platform_id` bigint(20) NOT NULL COMMENT '云平台id',
  `platform_name` varchar(125) COLLATE utf8_bin NOT NULL COMMENT '云平台名称',
  `account_id` bigint(20) NOT NULL COMMENT '账号id',
  `account_name` varchar(125) COLLATE utf8_bin NOT NULL COMMENT '账号名称',
  `spec_id` bigint(20) DEFAULT NULL COMMENT '规格id',
  `spec` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '规格描述',
  `open_type` int(2) DEFAULT NULL COMMENT '开通类型：1、长期开通 2、短期抢占',
  `open_time` int(2) DEFAULT NULL COMMENT '开通时长：长期开通单位为月，短期抢占单位为小时',
  `machine_num` int(5) NOT NULL COMMENT '机器数量',
  `extend_config` text COLLATE utf8_bin COMMENT '更多配置',
  `status` int(2) NOT NULL COMMENT '任务状态：1、开通中 2、开通失败 3、开通成功 4、销毁中 5、销毁失败 6、销毁成功',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0: 正常 1： 删除',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_update` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uni_serial_no` (`serial_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_machine_task
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_machine_task_log
-- ----------------------------
DROP TABLE IF EXISTS `t_machine_task_log`;
CREATE TABLE `t_machine_task_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `task_id` bigint(20) NOT NULL COMMENT '任务id',
  `serial_no` varchar(125) COLLATE utf8_bin NOT NULL COMMENT '序列号',
  `ip` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '机器IP',
  `hostname` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '机器名称',
  `status` tinyint(2) DEFAULT NULL COMMENT '状态 1、开通中 2、开通成功 3、开通失败 4：启动中 5、启动成功 6、启动失败 7、初始化中 8、初始化失败 9、运行中 10、销毁中 11、已过期 12、已锁定 13、销毁失败 14、已销毁',
  `log` text COLLATE utf8_bin COMMENT '操作日志',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0: 正常 1： 删除',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_update` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uni_serial_no_ip` (`serial_no`,`ip`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_machine_task_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_middleware_info
-- ----------------------------
DROP TABLE IF EXISTS `t_middleware_info`;
CREATE TABLE `t_middleware_info` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `MIDDLEWARE_TYPE` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '中间件类型',
  `MIDDLEWARE_NAME` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '中间件名称',
  `MIDDLEWARE_VERSION` varchar(100) DEFAULT NULL COMMENT '中间件版本号',
  `IS_DELETED` tinyint(4) DEFAULT '0' COMMENT '是否有效 0:有效;1:无效',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `t_middleware_info` (`MIDDLEWARE_TYPE`) USING BTREE,
  KEY `MIDDLEWARE_NAME` (`MIDDLEWARE_NAME`(255)) USING BTREE,
  KEY `MIDDLEWARE_VERSION` (`MIDDLEWARE_VERSION`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='中间件信息表';

-- ----------------------------
-- Records of t_middleware_info
-- ----------------------------
BEGIN;
INSERT INTO `t_middleware_info` VALUES (1, '数据库', 'mysql', '8.0.16', 0, '2020-05-18 14:31:03', '2020-05-18 14:31:03');
INSERT INTO `t_middleware_info` VALUES (2, '缓存', 'redis', '1.0.0', 0, '2020-05-18 14:31:49', '2020-05-18 14:31:49');
INSERT INTO `t_middleware_info` VALUES (3, '消息队列', 'rocketmq', '2.0.4', 0, '2020-05-18 21:51:39', '2020-05-18 21:51:39');
INSERT INTO `t_middleware_info` VALUES (4, '缓存', 'redis', '1.4.5', 0, '2020-08-26 17:59:58', '2020-08-26 17:59:58');
INSERT INTO `t_middleware_info` VALUES (5, '数据库', 'mysql', '1.0.7', 0, '2020-08-26 17:59:58', '2020-08-26 17:59:58');
INSERT INTO `t_middleware_info` VALUES (6, '远程调用', 'dubbo', '2.6.4', 0, '2020-08-26 17:59:58', '2020-08-26 17:59:58');
INSERT INTO `t_middleware_info` VALUES (7, '消息队列', 'rocketmq', '3.2.5', 0, '2020-08-26 17:59:58', '2020-08-26 17:59:58');
INSERT INTO `t_middleware_info` VALUES (8, '远程调用', 'dubbo', '1.0.7', 0, '2020-08-26 17:59:58', '2020-08-26 17:59:58');
INSERT INTO `t_middleware_info` VALUES (9, '缓存', 'redis', '1.3.46', 0, '2020-08-26 18:00:44', '2020-08-26 18:00:44');
COMMIT;

-- ----------------------------
-- Table structure for t_middleware_link_relate
-- ----------------------------
DROP TABLE IF EXISTS `t_middleware_link_relate`;
CREATE TABLE `t_middleware_link_relate` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `MIDDLEWARE_ID` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '中间件id',
  `TECH_LINK_ID` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '技术链路id',
  `BUSINESS_LINK_ID` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '业务链路id',
  `SCENE_ID` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '场景id',
  `IS_DELETED` tinyint(4) DEFAULT '0' COMMENT '是否有效 0:有效;1:无效',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `t_middleware_link_relate1` (`MIDDLEWARE_ID`,`TECH_LINK_ID`) USING BTREE,
  KEY `t_middleware_link_relate2` (`TECH_LINK_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='链路中间件关联表';

-- ----------------------------
-- Records of t_middleware_link_relate
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_mq_isolate_config
-- ----------------------------
DROP TABLE IF EXISTS `t_mq_isolate_config`;
CREATE TABLE `t_mq_isolate_config` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CLUSTER_NAME` varchar(100) DEFAULT NULL COMMENT '集群名称',
  `CLUSTER_ALIAS` varchar(100) DEFAULT NULL COMMENT '集群别名',
  `NAMESRV_ADDR` varchar(500) DEFAULT NULL COMMENT 'NAMESERVER地址',
  `PT_NAMESRV_ADDR` varchar(500) DEFAULT NULL COMMENT '压测NAMESERVER地址',
  `BROKER_NAME` varchar(512) DEFAULT NULL COMMENT 'broker名称，多个用逗号分隔',
  `TOPIC` varchar(1024) DEFAULT NULL COMMENT 'TOPIC名称，多个用逗号分隔',
  `CONSUMER_GROUP` varchar(1024) DEFAULT NULL COMMENT '消费组，多个用逗号分隔',
  `ISOLATED_STATUS` int(1) DEFAULT NULL COMMENT '隔离状态(0:停写，1:读完成，2:隔离)',
  `BROKER_PROPERTY` varchar(512) DEFAULT NULL COMMENT 'broker属性',
  `ACTIVE` char(2) DEFAULT NULL COMMENT '是否有效: Y:有效 N:无效',
  `FAILED_DETAIL` text COMMENT '操作出错信息',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '变更时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='MQ隔离配置表';

-- ----------------------------
-- Records of t_mq_isolate_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_mq_msg
-- ----------------------------
DROP TABLE IF EXISTS `t_mq_msg`;
CREATE TABLE `t_mq_msg` (
  `MSG_ID` bigint(20) NOT NULL COMMENT '主键ID',
  `MSG_TYPE` varchar(5) NOT NULL COMMENT '消息类型',
  `DICT_TYPE` varchar(32) NOT NULL COMMENT '数据字典类型（ID值）',
  `MSG_HOST` varchar(30) DEFAULT NULL COMMENT '消息地址(ESB和IBM使用)',
  `MSG_PORT` varchar(10) DEFAULT NULL COMMENT '消息端口(ESB和IBM使用)',
  `MSG_IP` varchar(500) DEFAULT NULL COMMENT '集群ip(形式为host:port;host:port,ROCKETMQ使用)',
  `TOPIC` varchar(50) DEFAULT NULL COMMENT '订阅主题',
  `GROUPNAME` varchar(30) DEFAULT NULL COMMENT '组名',
  `QUEUE_CHANNEL` varchar(50) DEFAULT NULL COMMENT '系统队列通道',
  `QUEUE_MANAGER` varchar(50) DEFAULT NULL COMMENT '队列管理器',
  `CCSID` varchar(20) DEFAULT NULL COMMENT '编码字符集标识符',
  `BASE_QUEUE_NAME` varchar(30) DEFAULT NULL COMMENT '基础队列名称',
  `TRANSPORT_TYPE` varchar(20) DEFAULT NULL COMMENT '传输类型',
  `ESBCODE` varchar(1024) DEFAULT NULL COMMENT 'ESBCODE',
  `CONSUME_STATUS` char(5) DEFAULT '0' COMMENT '消费状态, 0未消费 1正在消费 2已消费 3消费失败',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime NOT NULL COMMENT '更新时间',
  `CONSUME_START_TIME` datetime DEFAULT NULL COMMENT '消费开始时间',
  `CONSUME_END_TIME` datetime DEFAULT NULL COMMENT '消费完成时间',
  `LAST_CONSUME_TIME` datetime DEFAULT NULL COMMENT '上次消费时间',
  PRIMARY KEY (`MSG_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='MQ虚拟消费消息表';

-- ----------------------------
-- Records of t_mq_msg
-- ----------------------------
BEGIN;
INSERT INTO `t_mq_msg` VALUES (6432496213928448000, '1', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.29.24', '1418', '', NULL, NULL, 'ADMIN.CHANNEL', 'MQM2', '1208', 'QU_LMS_REQUEST_COM_IN', '1', 'PT_ESB_OA2ESB_PRO_TEST', '2', '2018-08-07 15:23:27', '2019-04-08 16:21:10', '2019-04-08 16:21:05', '2019-04-08 16:21:11', '2019-04-08 16:15:02');
INSERT INTO `t_mq_msg` VALUES (6432496473169989632, '2', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.25.15', '9002', '', NULL, NULL, 'ECS.CLIENT', 'SUBQM01', NULL, NULL, '1', 'PT_TRO_TESTMQ', '3', '2018-08-07 15:24:29', '2019-11-05 16:26:44', '2019-11-05 16:26:44', '2019-05-29 10:54:19', '2019-05-29 10:54:19');
INSERT INTO `t_mq_msg` VALUES (6432862071414591488, '2', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.25.15', '9002', '', NULL, NULL, 'ECS.CLIENT', 'SUBQM01', NULL, NULL, '1', 'PT_TRO_TESTMQ2', '2', '2018-08-08 15:37:14', '2018-09-21 17:18:50', '2018-09-21 17:18:36', '2018-09-21 17:18:51', '2018-09-14 21:17:27');
INSERT INTO `t_mq_msg` VALUES (6432873722507431936, '1', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.29.24', '1418', '', NULL, NULL, 'ADMIN.CHANNEL', 'MQM2', '1208', 'QU_LMS_REQUEST_COM_IN', '1', 'PT_ESB_DOP2ESB_PRO_NOMAL|PT_ESB_OA2ESB_PRO_TEST', '3', '2018-08-08 16:23:32', '2018-10-16 15:23:40', '2018-10-16 15:23:29', '2018-10-16 15:23:41', '2018-09-21 10:47:43');
INSERT INTO `t_mq_msg` VALUES (6432881246853926912, '1', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.29.24', '1418', '', NULL, NULL, 'ADMIN.CHANNEL', 'MQM2', '1208', 'QU_LMS_REQUEST_COM_IN', '1', 'PT_ESB_DOP2ESB_PRO_NOMAL', '2', '2018-08-08 16:53:26', '2018-09-13 23:32:21', '2018-09-13 23:32:12', '2018-09-13 23:32:22', '2018-08-24 09:50:40');
INSERT INTO `t_mq_msg` VALUES (6433155695578714112, '1', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.44.50', '1418', '', NULL, NULL, 'ADMIN.CHANNEL', 'MQ2_01', '1208', 'QU_LMS_REQUEST_COM_IN', '1', 'PT_ESB_OA2ESB_PRO_TEST', '3', '2018-08-09 11:04:00', '2018-11-07 17:03:17', '2018-10-16 15:23:26', '2018-09-14 00:11:21', '2018-09-14 00:11:21');
INSERT INTO `t_mq_msg` VALUES (6433156039796854784, '1', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.44.51', '1418', '', NULL, NULL, 'ADMIN.CHANNEL', 'MQ2_02', '1208', 'QU_LMS_REQUEST_COM_IN', '1', 'PT_ESB_DOP2ESB_PRO_TEST', '2', '2018-08-09 11:05:22', '2018-11-07 17:04:05', '2018-09-21 17:55:50', '2018-09-21 17:55:54', '2018-09-21 10:41:56');
INSERT INTO `t_mq_msg` VALUES (6436036123318816768, '3', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.20.224', '9876', '10.230.20.224:9876', 'PT_TRO_RMQ_TEST', 'CID_002', NULL, NULL, NULL, NULL, '1', '', '2', '2018-08-17 09:49:47', '2018-09-13 14:21:18', '2018-08-24 09:47:51', '2018-08-24 09:48:40', '2018-08-23 10:11:34');
INSERT INTO `t_mq_msg` VALUES (6436101837685788672, '1', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.30.30', '121', '', NULL, NULL, '123123', '123123', NULL, '123', '1', 'PT_2341', '3', '2018-08-17 14:10:54', '2018-08-17 14:11:23', '2018-08-17 14:11:24', NULL, NULL);
INSERT INTO `t_mq_msg` VALUES (6436101905495101440, '1', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.30.30', '1111', '', NULL, NULL, '1111', '1111', NULL, '1111', '1', 'PT_2341', '3', '2018-08-17 14:11:10', '2018-08-17 14:11:21', '2018-08-17 14:11:22', NULL, NULL);
INSERT INTO `t_mq_msg` VALUES (6436105264801255424, '1', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.30.12', '121', '', NULL, NULL, '123123', '123123', NULL, '123', '1', 'PT_2341', '0', '2018-08-17 14:24:31', '2018-08-17 14:27:42', NULL, NULL, NULL);
INSERT INTO `t_mq_msg` VALUES (6437504594422534144, '1', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.25.29', '1430', '', NULL, NULL, 'ADMIN.CHANNEL', 'ESBESB', '1111', 'QU_LMS_REQUEST_COM_IN', '1', 'PT_ESB_111|PT_ESB_111', '3', '2018-08-21 11:05:01', '2018-10-16 15:23:38', '2018-10-16 15:23:24', '2018-10-16 15:23:36', NULL);
INSERT INTO `t_mq_msg` VALUES (6437577600293408768, 'null', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.44.5', '99', '', NULL, NULL, '99', '99', '99', '99', '1', 'PT_99NNNNNNN', '0', '2018-08-21 15:55:04', '2018-08-21 16:32:23', NULL, NULL, NULL);
INSERT INTO `t_mq_msg` VALUES (6437582930012278784, 'null', 'ec18bced105c41018cfbcaf6b3327b9a', '123.123.123.123', '3456', '', NULL, NULL, '2345346', '23456436', '334566', '13435', '1', 'PT_againMMMMMMM', '0', '2018-08-21 16:16:15', '2018-08-21 16:31:43', NULL, NULL, NULL);
INSERT INTO `t_mq_msg` VALUES (6437586587797688320, 'null', 'ec18bced105c41018cfbcaf6b3327b9a', '12.12.12.12', '1212', '', NULL, NULL, '0821', '0821', '0821222', '0821', '1', 'PT_0821', '0', '2018-08-21 16:30:47', '2018-08-21 16:31:13', NULL, NULL, NULL);
INSERT INTO `t_mq_msg` VALUES (6437586753401393152, 'null', 'ec18bced105c41018cfbcaf6b3327b9a', '1.1.1.1', '1', '', NULL, NULL, '1', '1', NULL, NULL, '1', 'PT_100', '0', '2018-08-21 16:31:26', '2018-08-21 16:34:15', NULL, NULL, NULL);
INSERT INTO `t_mq_msg` VALUES (6437586901980418048, 'null', 'ec18bced105c41018cfbcaf6b3327b9a', '2.2.2.2', '2', '', NULL, NULL, '2', '2', '2', '2', '1', 'PT_2ttttt', '0', '2018-08-21 16:32:02', '2018-08-21 16:33:13', NULL, NULL, NULL);
INSERT INTO `t_mq_msg` VALUES (6437587605361004544, 'null', 'ec18bced105c41018cfbcaf6b3327b9a', '192.168.67.12', '1111', '', NULL, NULL, 'TEST', 'TEST', 'TEST', '测试队列', '1', 'PT_TESTAAAAAAAA', '0', '2018-08-21 16:34:49', '2018-08-21 16:35:15', NULL, NULL, NULL);
INSERT INTO `t_mq_msg` VALUES (6437588336096841728, '2', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.12.10', '8888', '', NULL, NULL, 'ADMIN.CHANNEL', 'SUBQM01', NULL, NULL, '1', 'PT_TEST_TRO', '2', '2018-08-21 16:37:43', '2018-08-21 17:24:43', '2018-08-21 16:37:55', '2018-08-21 16:38:05', NULL);
INSERT INTO `t_mq_msg` VALUES (6437639802996133888, '2', 'ec18bced105c41018cfbcaf6b3327b9a', '36.36.36.36', '20000', '', NULL, NULL, 'ECS.CLIENT', 'SUBQM03', NULL, NULL, '1', 'PT_TRO_IBM_TEST', '0', '2018-08-21 20:02:14', '2018-08-21 20:02:14', NULL, NULL, NULL);
INSERT INTO `t_mq_msg` VALUES (6438216255429152768, '3', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.20.224', '9876', '10.230.20.224:9876', 'PT_TRO_RMQ_TEST03', 'AAAAAA', NULL, NULL, NULL, NULL, '1', '', '2', '2018-08-23 10:12:51', '2018-09-13 14:21:37', '2018-08-23 10:12:59', '2018-08-23 18:09:54', NULL);
INSERT INTO `t_mq_msg` VALUES (6438218155654385664, '2', 'ec18bced105c41018cfbcaf6b3327b9a', '22.22.22.22', '2222', '', NULL, NULL, 'IBM.CLIENT', 'QU_IBMQUEUE_TEST', NULL, NULL, '1', 'PT_0823', '0', '2018-08-23 10:20:25', '2018-08-23 10:20:25', NULL, NULL, NULL);
INSERT INTO `t_mq_msg` VALUES (6438223615803527168, '2', 'ec18bced105c41018cfbcaf6b3327b9a', '23.23.23.23', '2323', '', NULL, NULL, 'IBM.CLIENT', 'TEST0823', NULL, NULL, '1', 'PT_TRO_082301', '2', '2018-08-23 10:42:06', '2018-08-23 10:48:39', '2018-08-23 10:47:31', '2018-08-23 10:48:39', NULL);
INSERT INTO `t_mq_msg` VALUES (6445834370396721152, '3', 'ec18bced105c41018cfbcaf6b3327b9a', NULL, NULL, '10.230.20.224:9876', 'PT_TRO_TEST', 'CGROUP_0914', NULL, NULL, NULL, NULL, NULL, '', '3', '2018-09-13 10:44:31', '2018-09-14 00:19:05', '2018-09-14 00:18:52', NULL, NULL);
INSERT INTO `t_mq_msg` VALUES (6445973977998299136, '3', 'ec18bced105c41018cfbcaf6b3327b9a', NULL, NULL, '10.230.44.41:9876;10.230.44.44:9876', 'PT_RocketMqTest20180413', 'rocketmq-producer-demo', NULL, NULL, NULL, NULL, NULL, '', '2', '2018-09-13 19:59:16', '2018-09-14 21:23:36', '2018-09-14 21:22:35', '2018-09-14 21:23:36', '2018-09-14 05:45:18');
INSERT INTO `t_mq_msg` VALUES (6446041293553405952, '3', 'ec18bced105c41018cfbcaf6b3327b9a', NULL, NULL, '10.230.44.41:9876;10.230.44.44:9876', 'PT_BASIC_JCH', 'PT_JCH_TEST_CGROUP_003', NULL, NULL, NULL, NULL, NULL, '', '3', '2018-09-14 00:26:46', '2018-09-14 00:32:34', '2018-09-14 00:32:07', NULL, NULL);
INSERT INTO `t_mq_msg` VALUES (6446118513101770752, '3', 'ec18bced105c41018cfbcaf6b3327b9a', NULL, NULL, '10.230.44.41:9876;10.230.44.44:9876', 'PT_TRO_TEST_001', 'PT_TEST_001', NULL, NULL, NULL, NULL, NULL, '', '2', '2018-09-14 05:33:36', '2018-09-21 18:44:18', '2018-09-21 18:43:18', '2018-09-21 18:44:19', '2018-09-21 18:33:12');
INSERT INTO `t_mq_msg` VALUES (6446128540390789120, '1', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.44.50', '1418', NULL, NULL, NULL, 'ADMIN.CHANNEL', 'MQ2_01', '1208', 'QU_LMS_REQUEST_COM_IN', '1', 'PT_ESB_DOP2ESB_PRO_TEST', '2', '2018-09-14 06:13:27', '2018-11-07 17:44:59', '2018-11-07 17:43:21', '2018-11-07 17:45:00', '2018-10-16 15:21:53');
INSERT INTO `t_mq_msg` VALUES (6446562188155949056, '3', 'ec18bced105c41018cfbcaf6b3327b9a', NULL, NULL, '10.230.20.224:9876', 'PT_BASIC_JCH', 'TEST_0915', NULL, NULL, NULL, NULL, NULL, '', '2', '2018-09-15 10:56:36', '2018-09-27 14:09:17', '2018-09-27 14:08:04', '2018-09-27 14:09:18', '2018-09-27 14:05:17');
INSERT INTO `t_mq_msg` VALUES (6446565790958227456, '3', 'ec18bced105c41018cfbcaf6b3327b9a', NULL, NULL, '10.230.44.41:9876;10.230.44.44:9876', 'PT_GL_0915', 'PT_YP_TEST', NULL, NULL, NULL, NULL, NULL, '', '2', '2018-09-15 11:10:55', '2018-09-15 11:11:40', '2018-09-15 11:11:10', '2018-09-15 11:11:40', NULL);
INSERT INTO `t_mq_msg` VALUES (6449466814031204352, '3', 'ec18bced105c41018cfbcaf6b3327b9a', NULL, NULL, '10.230.44.41:9876;10.230.44.44:9876', 'PT_TRO_TEST_1013', 'PT_TRO_CGROUP_0913', NULL, NULL, NULL, NULL, NULL, '', '3', '2018-09-23 11:18:33', '2018-10-16 22:19:56', '2018-10-16 22:17:57', '2018-10-16 22:19:56', '2018-10-16 14:48:51');
INSERT INTO `t_mq_msg` VALUES (6456692219947520000, '4', 'ec18bced105c41018cfbcaf6b3327b9a', NULL, NULL, '10.230.20.224:9876', 'PT_BASIC_JCH', 'PT_TEST_0915', NULL, NULL, NULL, NULL, NULL, '', '3', '2018-10-13 09:49:44', '2018-10-13 17:24:02', '2018-10-13 17:23:55', '2018-10-13 17:24:02', '2018-10-13 17:20:59');
INSERT INTO `t_mq_msg` VALUES (6456701029315514368, '4', 'ec18bced105c41018cfbcaf6b3327b9a', NULL, NULL, '10.230.44.41:9876;10.230.44.44:9876', 'TRO_TEST_DO_NOT_DELETE', 'TRO_TEST_GROUP', NULL, NULL, NULL, NULL, NULL, '', '0', '2018-10-13 10:24:45', '2018-10-13 10:24:45', NULL, NULL, NULL);
INSERT INTO `t_mq_msg` VALUES (6457420400727953408, '4', 'ec18bced105c41018cfbcaf6b3327b9a', NULL, NULL, '10.230.44.41:9876;10.230.44.44:9876', 'PT_TRO_TEST_1016', 'PT_TRO_CGROUP_1016', NULL, NULL, NULL, NULL, NULL, '', '3', '2018-10-15 10:03:16', '2018-10-16 15:36:38', '2018-10-16 15:36:07', '2018-10-16 15:36:34', '2018-10-16 11:00:35');
INSERT INTO `t_mq_msg` VALUES (6457797855879892992, '3', 'ec18bced105c41018cfbcaf6b3327b9a', NULL, NULL, '10.230.44.41:9876;10.230.44.44:9876', 'PT_TRO_TEST_1015', 'PT_TRO_CGROUP_1015', NULL, NULL, NULL, NULL, NULL, '', '2', '2018-10-16 11:03:08', '2018-10-22 15:47:43', '2018-10-22 15:47:28', '2018-10-22 15:47:43', '2018-10-19 16:32:45');
INSERT INTO `t_mq_msg` VALUES (6457859223496691712, '3', 'ec18bced105c41018cfbcaf6b3327b9a', NULL, NULL, '10.230.44.41:9876;10.230.44.44:9876', 'PT_TRO_TEST_1016', 'PT_TRO_CGROUP_1016', NULL, NULL, NULL, NULL, NULL, '', '2', '2018-10-16 15:07:00', '2019-05-29 08:44:07', '2019-05-29 08:43:42', '2019-05-29 08:44:08', '2018-10-22 16:39:10');
INSERT INTO `t_mq_msg` VALUES (6457876898398605312, '4', 'ec18bced105c41018cfbcaf6b3327b9a', NULL, NULL, '10.230.44.41:9876;10.230.44.44:9876', 'PT_DPBOOT_TEST_1016', 'PT_DPBOOT_CGROUP_1016', NULL, NULL, NULL, NULL, NULL, '', '2', '2018-10-16 16:17:14', '2019-05-29 09:14:46', '2019-05-29 09:12:58', '2019-05-29 09:14:46', '2019-05-29 08:44:47');
INSERT INTO `t_mq_msg` VALUES (6458672275234230272, '1', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.27.237', '1111', NULL, NULL, NULL, 'TEST', 'TEST', '1208', 'TEST', '1', 'PT_TEST_1111', '0', '2018-10-18 20:57:46', '2018-11-07 16:25:28', NULL, NULL, NULL);
INSERT INTO `t_mq_msg` VALUES (6458961338516377600, '3', 'ec18bced105c41018cfbcaf6b3327b9a', NULL, NULL, '10.230.44.41:9876;10.230.44.44:9876', 'PT_DPBoot_RMQ_1019', 'PT_DPBOOT_CGROUP_1019', NULL, NULL, NULL, NULL, NULL, '', '2', '2018-10-19 16:06:24', '2018-10-19 16:19:24', '2018-10-19 16:15:39', '2018-10-19 16:19:24', '2018-10-19 16:08:02');
INSERT INTO `t_mq_msg` VALUES (6458969269861355520, '2', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.25.15', '9002', NULL, NULL, NULL, 'ECS.CLIENT', 'SUBQM01', NULL, NULL, '1', 'PT_TRO_TESTMQ_1019', '2', '2018-10-19 16:37:55', '2018-10-19 22:39:11', '2018-10-19 22:38:44', '2018-10-19 22:39:11', NULL);
INSERT INTO `t_mq_msg` VALUES (6467860418797768704, '2', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.20.130', '5002', NULL, NULL, NULL, 'ECS.CLIENT', 'QMSUB01', NULL, NULL, '1', 'PT_Q_OPT_TFR_UNLOAD_HAN', '2', '2018-11-13 05:28:10', '2018-11-16 16:23:11', '2018-11-16 16:13:58', '2018-11-16 16:23:12', '2018-11-16 16:07:59');
INSERT INTO `t_mq_msg` VALUES (6467860667574521856, '2', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.20.131', '5003', NULL, NULL, NULL, 'ECS.CLIENT', 'QMSUB02', NULL, NULL, '1', 'PT_Q_OPT_TFR_UNLOAD_PKG', '2', '2018-11-13 05:29:10', '2018-11-16 16:23:15', '2018-11-16 16:11:43', '2018-11-16 16:23:16', '2018-11-16 16:08:04');
INSERT INTO `t_mq_msg` VALUES (6468024806024417280, '2', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.20.131', '5003', NULL, NULL, NULL, 'ECS.CLIENT', 'QMSUB02', NULL, NULL, '1', 'PT_Q_OPT_TFR_UNLOAD_QMC', '2', '2018-11-13 16:21:23', '2018-11-16 16:23:13', '2018-11-16 16:13:57', '2018-11-16 16:23:13', '2018-11-16 16:08:02');
INSERT INTO `t_mq_msg` VALUES (6513299538915102720, '3', 'ec18bced105c41018cfbcaf6b3327b9a', NULL, NULL, '10.230.28.19:9876', 'PT_CUBC_TRADE_BIZ_OPERATE', 'PT_ASSET_CONSUMER_OVER_GROUP', NULL, NULL, NULL, NULL, NULL, '', '3', '2019-03-18 14:47:01', '2020-02-15 01:24:16', '2020-02-15 01:24:16', '2019-04-08 16:05:54', '2019-04-08 16:05:54');
INSERT INTO `t_mq_msg` VALUES (6515839071909056512, '3', 'ec18bced105c41018cfbcaf6b3327b9a', NULL, NULL, '10.230.28.19:9876', 'PT_CUBC_TRADE_CORE_UPDATE', 'PT_ASSET_CONSUMER_UPDATE_GROUP', NULL, NULL, NULL, NULL, NULL, '', '3', '2019-03-25 14:58:13', '2019-04-08 16:01:19', '2019-03-25 14:58:18', NULL, NULL);
INSERT INTO `t_mq_msg` VALUES (6539331973511385088, '1', 'ec18bced105c41018cfbcaf6b3327b9a', '10.10.10.10', '1000', NULL, NULL, NULL, 'TEST', 'TEST', '1208', 'T_OPT_TFR_UNLOAD', '1', 'PT_ECSMQ', '3', '2019-05-29 10:50:37', '2020-01-09 20:43:28', '2020-01-09 20:43:28', '2019-05-29 10:52:10', '2019-05-29 10:52:10');
INSERT INTO `t_mq_msg` VALUES (6540028220719239168, '3', 'ec18bced105c41018cfbcaf6b3327b9a', NULL, NULL, '192.168.1.2;230.44.5.6', '23', '333', NULL, NULL, NULL, NULL, NULL, '', '0', '2019-05-31 08:57:15', '2019-05-31 08:57:15', NULL, NULL, NULL);
INSERT INTO `t_mq_msg` VALUES (6540045835361587200, '3', 'ec18bced105c41018cfbcaf6b3327b9a', NULL, NULL, '2.2.', '45', '2', NULL, NULL, NULL, NULL, NULL, '', '0', '2019-05-31 10:07:15', '2019-11-05 16:27:31', NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for t_network_isolate_config
-- ----------------------------
DROP TABLE IF EXISTS `t_network_isolate_config`;
CREATE TABLE `t_network_isolate_config` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `APP_NAME` varchar(100) DEFAULT NULL COMMENT '应用名称',
  `VIP_ADDR` varchar(50) DEFAULT NULL COMMENT 'VIP地址',
  `BALANCE_TYPE` varchar(10) DEFAULT NULL COMMENT '负载均衡类型',
  `POOL_NAME` varchar(1000) DEFAULT NULL COMMENT '地址池名称，多个用逗号分隔',
  `ISOLATED_STATUS` int(1) DEFAULT NULL COMMENT '隔离状态(0:未隔离，1:预隔离，2:隔离)',
  `ACTIVE` char(2) DEFAULT NULL COMMENT '是否有效: Y:有效 N:无效',
  `FAILED_DETAIL` text COMMENT '隔离出错信息',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '变更时间',
  `ISOLATED_IP` varchar(100) DEFAULT NULL COMMENT '隔离成功IP，多个用逗号分隔',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COMMENT='网络隔离配置表';

-- ----------------------------
-- Records of t_network_isolate_config
-- ----------------------------
BEGIN;
INSERT INTO `t_network_isolate_config` VALUES (3, 'com-function-web', '11.22.33.44', 'F5', '33.33.33.33', 0, 'N', 'read app_config file did not find the app_name information', '2019-05-14 18:45:31', '2019-06-13 17:36:35', NULL);
INSERT INTO `t_network_isolate_config` VALUES (6, 'cubc-asset-web', '192.168.10.11', 'A10', '192.168.10.1;192.168.10.2;192.168.10.3', 0, 'N', 'read app_config file did not find the app_name information', '2019-05-16 17:44:11', '2019-05-21 08:35:48', NULL);
INSERT INTO `t_network_isolate_config` VALUES (11, 'crm-sync', '130.130.130.130', 'A10', '12.12.12.12', 0, 'N', 'read app_config file did not find the app_name information', '2019-05-17 09:25:39', '2019-06-13 16:59:16', NULL);
INSERT INTO `t_network_isolate_config` VALUES (20, 'gis-srv-web', '6.6.6.6', 'A10', '5.5.5.5', 0, 'Y', '参数错误', '2019-05-07 14:32:28', '2019-06-19 15:11:44', '[\r\n      \"1.1.1.2\",\r\n      \"1.1.1.3\"\r\n  ]');
INSERT INTO `t_network_isolate_config` VALUES (21, 'gis', '1.1.1.1', 'F5', '1.1.1.2;1.1.1.8', 0, 'N', NULL, '2019-06-05 11:16:14', '2019-06-13 16:59:31', NULL);
INSERT INTO `t_network_isolate_config` VALUES (22, 'agent-test-client', '10.231.13.80', 'F5', '10.231.13.90', 0, 'Y', '', '2019-06-12 08:59:59', '2019-07-23 09:35:34', '');
INSERT INTO `t_network_isolate_config` VALUES (23, 'agent-test-server', '10.231.13.80', 'F5', '10.231.13.90', 0, 'Y', '', '2019-06-12 11:00:58', '2019-07-08 15:45:06', '');
INSERT INTO `t_network_isolate_config` VALUES (24, 'agent-test', '10.230.44.5', 'F5', '10.230.22.30', 0, 'N', NULL, '2019-06-13 16:53:59', '2019-06-13 16:53:59', NULL);
INSERT INTO `t_network_isolate_config` VALUES (25, 'dls-inventory-mq', '10.230.44.5', 'F5', '10.10.10.10', 0, 'N', NULL, '2019-06-13 17:12:33', '2019-06-13 17:12:33', NULL);
INSERT INTO `t_network_isolate_config` VALUES (26, 'com-function-web', '1.2.3.3', 'F5', '10.10.10.1', 0, 'N', NULL, '2019-06-13 17:36:58', '2019-06-13 17:36:58', NULL);
INSERT INTO `t_network_isolate_config` VALUES (27, 'dls-load-service', '10.230.22.30', 'A10', '110.203.30.10', 0, 'N', NULL, '2019-06-13 18:25:30', '2019-06-13 18:25:30', NULL);
INSERT INTO `t_network_isolate_config` VALUES (28, 'rps-exp-app', '1.1.1.1', 'F5', '1.1.1.1', 0, 'Y', '参数错误', '2019-07-04 09:02:02', '2019-07-04 09:09:55', '');
COMMIT;

-- ----------------------------
-- Table structure for t_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `t_operation_log`;
CREATE TABLE `t_operation_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `module` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '操作模块-主模块',
  `sub_module` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '操作模块-字模块',
  `type` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '操作类型',
  `status` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '操作状态',
  `content` text COLLATE utf8_bin NOT NULL COMMENT '操作内容描述',
  `user_id` bigint(20) NOT NULL COMMENT '操作人id',
  `user_name` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '操作人名称',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0: 正常 1： 删除',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_update` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_operation_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_performance_criteria_config
-- ----------------------------
DROP TABLE IF EXISTS `t_performance_criteria_config`;
CREATE TABLE `t_performance_criteria_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `env` varchar(50) NOT NULL COMMENT '0-针对应用配置 1-全局配置',
  `app_id` bigint(19) NOT NULL COMMENT '应用ID',
  `app_name` varchar(50) NOT NULL COMMENT '应用名称',
  `type` tinyint(2) DEFAULT NULL COMMENT '标准类型1-小于、2-小于等于、3-等于、4-大于、5-大于等于',
  `value` varchar(5) DEFAULT NULL COMMENT '标准值',
  `standard` tinyint(1) DEFAULT NULL COMMENT '是否达标0-达标、1-不达标',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0-未删除、1-已删除',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='性能标准配置';

-- ----------------------------
-- Records of t_performance_criteria_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_performance_thread_data
-- ----------------------------
DROP TABLE IF EXISTS `t_performance_thread_data`;
CREATE TABLE `t_performance_thread_data` (
  `agent_id` varchar(256) DEFAULT NULL COMMENT 'agent_id',
  `app_name` varchar(256) DEFAULT NULL COMMENT 'app_name',
  `timestamp` varchar(256) DEFAULT NULL COMMENT 'timestamp',
  `app_ip` varchar(256) DEFAULT NULL COMMENT 'app_ip',
  `thread_data` longtext COMMENT '线程栈数据',
  `base_id` bigint(20) NOT NULL COMMENT 'base_thread_关联关系',
  `gmt_create` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  PRIMARY KEY (`base_id`) USING BTREE,
  UNIQUE KEY `base_id` (`base_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_performance_thread_data
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_performance_thread_stack_data
-- ----------------------------
DROP TABLE IF EXISTS `t_performance_thread_stack_data`;
CREATE TABLE `t_performance_thread_stack_data` (
  `thread_stack` longtext COMMENT '线程堆栈',
  `thread_stack_link` bigint(20) NOT NULL COMMENT 'influxDB关联',
  `gmt_create` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  PRIMARY KEY (`thread_stack_link`) USING BTREE,
  UNIQUE KEY `thread_stack_link` (`thread_stack_link`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_performance_thread_stack_data
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_pessure_test_task_activity_config
-- ----------------------------
DROP TABLE IF EXISTS `t_pessure_test_task_activity_config`;
CREATE TABLE `t_pessure_test_task_activity_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `pessure_process_id` bigint(20) NOT NULL COMMENT '压测任务流程ID',
  `name` varchar(20) NOT NULL COMMENT '业务活动名称',
  `type_name` varchar(19) NOT NULL COMMENT '业务活动类型',
  `scenario_id` varchar(50) DEFAULT NULL COMMENT '压测平台场景ID',
  `domain` varchar(50) NOT NULL COMMENT '业务域',
  `target_tps` tinyint(2) DEFAULT NULL COMMENT '目标TPS',
  `target_rt` varchar(5) DEFAULT NULL COMMENT '目标RT',
  `request_success_rate` int(3) DEFAULT NULL COMMENT '目标请求成功率',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0-未删除、1-已删除',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='压测任务业务活动配置';

-- ----------------------------
-- Records of t_pessure_test_task_activity_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_pessure_test_task_process_config
-- ----------------------------
DROP TABLE IF EXISTS `t_pessure_test_task_process_config`;
CREATE TABLE `t_pessure_test_task_process_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(50) NOT NULL COMMENT '压测任务名称',
  `type` tinyint(1) NOT NULL COMMENT '压测任务类型',
  `engine_id` bigint(20) NOT NULL COMMENT '压测引擎ID',
  `engine_name` varchar(20) NOT NULL COMMENT '压测引擎名称',
  `process_id` bigint(20) NOT NULL COMMENT '业务流程ID',
  `process_name` varchar(20) NOT NULL COMMENT '业务流程名称',
  `scenario_id` varchar(20) DEFAULT NULL COMMENT 'PTS场景ID',
  `status` tinyint(1) DEFAULT '0' COMMENT '0-为启动、1-执行中、2-压测结束',
  `pessure_time` datetime DEFAULT NULL COMMENT '压测最新时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0-未删除、1-已删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='压测任务业务流程配置';

-- ----------------------------
-- Records of t_pessure_test_task_process_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_prada_http_data
-- ----------------------------
DROP TABLE IF EXISTS `t_prada_http_data`;
CREATE TABLE `t_prada_http_data` (
  `TPHD_ID` bigint(19) NOT NULL COMMENT 'prada获取http接口表id',
  `APP_NAME` varchar(128) COLLATE utf8_bin NOT NULL COMMENT '应用名称',
  `INTERFACE_NAME` varchar(1024) COLLATE utf8_bin NOT NULL COMMENT '接口名称',
  `INTERFACE_TYPE` int(1) DEFAULT '1' COMMENT '接口类型(1.http 2.dubbo 3.禁止压测 4.job)',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '同步数据时间',
  PRIMARY KEY (`TPHD_ID`) USING BTREE,
  KEY `TPHD_index1` (`APP_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='prada获取http接口表';

-- ----------------------------
-- Records of t_prada_http_data
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_pressure_machine
-- ----------------------------
DROP TABLE IF EXISTS `t_pressure_machine`;
CREATE TABLE `t_pressure_machine` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '压力机名称',
  `ip` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '压力机IP',
  `flag` varchar(125) COLLATE utf8_bin DEFAULT NULL COMMENT '标签',
  `cpu` int(2) NOT NULL COMMENT 'cpu核数',
  `memory` bigint(20) NOT NULL COMMENT '内存，单位字节',
  `machine_usage` decimal(10,4) NOT NULL DEFAULT '0.0000' COMMENT '机器水位 ',
  `disk` bigint(20) NOT NULL COMMENT '磁盘，单位字节',
  `cpu_usage` decimal(10,4) NOT NULL COMMENT 'cpu利用率',
  `cpu_load` decimal(10,4) NOT NULL COMMENT 'cpu load',
  `memory_used` decimal(10,4) NOT NULL COMMENT '内存利用率',
  `disk_io_wait` decimal(10,4) DEFAULT NULL COMMENT '磁盘 IO 等待率',
  `transmitted_total` bigint(20) NOT NULL DEFAULT '0' COMMENT '机器网络带宽总大小',
  `transmitted_in` bigint(20) NOT NULL DEFAULT '0' COMMENT '网络带宽入大小',
  `transmitted_in_usage` decimal(10,4) NOT NULL DEFAULT '0.0000' COMMENT '网络带宽入利用率',
  `transmitted_out` bigint(20) NOT NULL DEFAULT '0' COMMENT '网络带宽出大小',
  `transmitted_out_usage` decimal(10,4) NOT NULL DEFAULT '0.0000' COMMENT '网络带宽出利用率',
  `transmitted_usage` decimal(10,4) NOT NULL COMMENT '网络带宽利用率',
  `scene_names` varchar(125) COLLATE utf8_bin DEFAULT NULL COMMENT '压测场景id',
  `status` int(2) NOT NULL DEFAULT '0' COMMENT '状态 0：空闲 ；1：压测中  -1:离线',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0: 正常 1： 删除',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_update` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `index_ip` (`ip`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_pressure_machine
-- ----------------------------
BEGIN;
INSERT INTO `t_pressure_machine` VALUES (47, 'shulie-prod-k8s-node001', '192.168.100.236', NULL, 4, 7918608384, 0.4630, 147691114496, 0.0125, 0.1000, 0.4630, 0.0030, 0, 9442, 0.0000, 10014, 0.0000, 0.0000, '', 0, 0, '2020-12-23 22:49:06', '2021-04-20 16:13:05');
INSERT INTO `t_pressure_machine` VALUES (48, 'shulie-prod-k8s-node002', '192.168.100.237', NULL, 4, 7918596096, 0.2332, 147691114496, 0.0075, 0.0000, 0.2332, 0.0000, 100, 973, 0.0001, 894, 0.0001, 0.0001, '', 0, 0, '2020-12-23 22:55:06', '2021-04-20 16:13:05');
INSERT INTO `t_pressure_machine` VALUES (49, 'shulie-prod-k8s-node003', '192.168.100.238', NULL, 4, 7918608384, 0.2293, 147691114496, 0.0025, 0.0000, 0.2293, 0.0000, 0, 2441, 0.0000, 120, 0.0000, 0.0000, '', 0, 0, '2020-12-23 22:55:06', '2021-04-20 16:13:05');
INSERT INTO `t_pressure_machine` VALUES (50, 'shulie-prod-k8s-node004', '192.168.100.3', NULL, 4, 7918604288, 0.1652, 42140479488, 0.0201, 0.0400, 0.1652, 0.0000, 0, 985, 0.0000, 1039, 0.0000, 0.0000, '', -1, 0, '2020-12-23 22:56:06', '2020-12-28 00:05:00');
INSERT INTO `t_pressure_machine` VALUES (51, 'shulie-prod-k8s-node005', '192.168.100.251', NULL, 4, 7918604288, 0.1632, 42140479488, 0.0101, 0.0000, 0.1632, 0.0000, 0, 2786, 0.0000, 1115, 0.0000, 0.0000, '', -1, 0, '2020-12-23 22:56:07', '2020-12-28 00:05:00');
INSERT INTO `t_pressure_machine` VALUES (52, 'shulie-prod-k8s-node007', '192.168.100.250', NULL, 4, 7918604288, 0.1615, 42140479488, 0.0100, 0.1300, 0.1615, 0.0000, 0, 1002, 0.0000, 787, 0.0000, 0.0000, '', -1, 0, '2020-12-23 22:57:06', '2020-12-28 00:05:00');
INSERT INTO `t_pressure_machine` VALUES (53, 'shulie-prod-k8s-node006', '192.168.100.249', NULL, 4, 7918604288, 0.1641, 42140479488, 0.0125, 0.1000, 0.1641, 0.0000, 0, 1078, 0.0000, 918, 0.0000, 0.0000, '', -1, 0, '2020-12-23 22:57:07', '2020-12-28 00:05:00');
INSERT INTO `t_pressure_machine` VALUES (54, 'shulie-prod-k8s-node008', '192.168.100.5', NULL, 4, 7918592000, 0.1546, 42140479488, 0.0101, 0.1000, 0.1546, 0.0000, 10, 1000, 0.0008, 786, 0.0006, 0.0008, '', -1, 0, '2020-12-23 22:58:06', '2020-12-28 00:05:00');
COMMIT;

-- ----------------------------
-- Table structure for t_pressure_test_engine_config
-- ----------------------------
DROP TABLE IF EXISTS `t_pressure_test_engine_config`;
CREATE TABLE `t_pressure_test_engine_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(20) NOT NULL COMMENT '压测引擎名称',
  `type` varchar(5) NOT NULL COMMENT '压测引擎类型 PTS，SPT，JMETER',
  `access_key` varchar(100) DEFAULT NULL COMMENT 'access_key',
  `secret_key` varchar(100) DEFAULT NULL COMMENT 'secret_key',
  `region_id` varchar(50) DEFAULT NULL COMMENT '地域ID',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) DEFAULT '0' COMMENT '0-可用 1-不可用',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0-未删除、1-已删除',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='压测引擎配置';

-- ----------------------------
-- Records of t_pressure_test_engine_config
-- ----------------------------
BEGIN;
INSERT INTO `t_pressure_test_engine_config` VALUES (1, 'SPT', 'SPT', 'sto-full-link', '123456', NULL, NULL, 0, 0, '2020-03-07 00:14:23', '2020-03-07 01:08:32');
INSERT INTO `t_pressure_test_engine_config` VALUES (2, 'pts', '', NULL, NULL, NULL, NULL, 0, 1, '2020-03-11 23:10:37', '2020-03-11 23:11:16');
INSERT INTO `t_pressure_test_engine_config` VALUES (3, 'sptTest', '', '123', '321', '1', '备注', 1, 0, '2020-03-11 23:19:41', '2020-03-11 23:52:47');
INSERT INTO `t_pressure_test_engine_config` VALUES (4, 'sptTest31231212', '', '123', '321', '1', '备注', 1, 0, '2020-03-11 23:57:40', '2020-03-11 23:57:40');
INSERT INTO `t_pressure_test_engine_config` VALUES (5, 'PTS12312分1', '', '123', '321', '1', '备注', 0, 0, '2020-03-11 23:58:46', '2020-03-11 23:58:46');
COMMIT;

-- ----------------------------
-- Table structure for t_pressure_time_record
-- ----------------------------
DROP TABLE IF EXISTS `t_pressure_time_record`;
CREATE TABLE `t_pressure_time_record` (
  `RECORD_ID` varchar(36) COLLATE utf8_bin NOT NULL COMMENT '压测记录id',
  `START_TIME` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '开始压测时间',
  `END_TIME` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '结束压测时间',
  PRIMARY KEY (`RECORD_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='压测时间记录表';

-- ----------------------------
-- Records of t_pressure_time_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_quick_access
-- ----------------------------
DROP TABLE IF EXISTS `t_quick_access`;
CREATE TABLE `t_quick_access` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '数据库唯一主键',
  `custom_id` bigint(20) DEFAULT NULL COMMENT '客户id',
  `quick_name` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '快捷键名称',
  `quick_logo` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '快捷键logo',
  `url_address` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '实际地址',
  `order` int(10) DEFAULT NULL COMMENT '顺序',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除 0:未删除;1:已删除',
  `is_enable` tinyint(4) DEFAULT '1' COMMENT '0:未启用；1:启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_quick_access
-- ----------------------------
BEGIN;
INSERT INTO `t_quick_access` VALUES (1, NULL, '发起压测', NULL, '/pressureTestManage/pressureTestScene/pressureTestSceneConfig?action=add', 1, '2020-06-29 19:05:44', '2020-06-29 23:48:30', 0, 1);
INSERT INTO `t_quick_access` VALUES (2, NULL, '压测场景', NULL, '/pressureTestManage/pressureTestScene', 2, '2020-06-29 19:06:07', '2020-06-29 23:49:42', 0, 1);
INSERT INTO `t_quick_access` VALUES (3, NULL, '压测报告', NULL, '/pressureTestManage/pressureTestReport', 3, '2020-06-29 19:06:15', '2020-06-29 23:49:58', 0, 1);
INSERT INTO `t_quick_access` VALUES (4, NULL, '应用列表', NULL, '/appManage', 4, '2020-06-29 19:06:21', '2020-06-29 23:50:12', 0, 1);
COMMIT;

-- ----------------------------
-- Table structure for t_report
-- ----------------------------
DROP TABLE IF EXISTS `t_report`;
CREATE TABLE `t_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scene_id` bigint(20) NOT NULL COMMENT '场景ID',
  `scene_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '场景名称',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `status` int(2) DEFAULT '0' COMMENT '报表生成状态:0/就绪状态，1/生成中,2/完成生成',
  `conclusion` int(1) DEFAULT '0' COMMENT '压测结论: 0/不通过，1/通过',
  `total_request` bigint(10) DEFAULT NULL COMMENT '请求总数',
  `avg_tps` decimal(10,2) DEFAULT NULL COMMENT '平均tps',
  `avg_rt` decimal(10,2) DEFAULT NULL COMMENT '平均响应时间',
  `concurrent` int(7) DEFAULT NULL COMMENT '最大并发',
  `success_rate` decimal(10,2) DEFAULT NULL COMMENT '成功率',
  `sa` decimal(10,2) DEFAULT NULL COMMENT 'sa',
  `operate_id` bigint(20) DEFAULT NULL COMMENT '操作用户ID',
  `features` text COLLATE utf8_bin COMMENT '扩展字段，JSON数据格式',
  `is_deleted` int(1) DEFAULT '0' COMMENT '是否删除:0/正常，1、已删除',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP,
  `gmt_update` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_report
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_report_app_ip_list
-- ----------------------------
DROP TABLE IF EXISTS `t_report_app_ip_list`;
CREATE TABLE `t_report_app_ip_list` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `report_id` varchar(100) COLLATE utf8_bin NOT NULL,
  `link_id` varchar(100) COLLATE utf8_bin NOT NULL,
  `application_name` varchar(200) COLLATE utf8_bin NOT NULL,
  `type` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `system_name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `ip` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `cpu` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `memory` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `io_read` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `io_write` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `io_all` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `pk_t_report_app_ip_idx` (`report_id`,`link_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_report_app_ip_list
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_report_application_summary
-- ----------------------------
DROP TABLE IF EXISTS `t_report_application_summary`;
CREATE TABLE `t_report_application_summary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_id` bigint(20) NOT NULL,
  `application_name` varchar(64) COLLATE utf8_bin NOT NULL,
  `machine_total_count` int(11) DEFAULT NULL COMMENT '总机器数',
  `machine_risk_count` int(11) DEFAULT NULL COMMENT '风险机器数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_idx_report_appliacation` (`report_id`,`application_name`),
  KEY `idx_report_id` (`report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_report_application_summary
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_report_bottleneck_interface
-- ----------------------------
DROP TABLE IF EXISTS `t_report_bottleneck_interface`;
CREATE TABLE `t_report_bottleneck_interface` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_id` bigint(20) NOT NULL,
  `application_name` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `sort_no` int(11) DEFAULT NULL,
  `interface_type` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '接口类型',
  `interface_name` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `tps` decimal(10,2) DEFAULT NULL,
  `rt` decimal(10,2) DEFAULT NULL,
  `node_count` int(11) DEFAULT NULL,
  `error_reqs` int(11) DEFAULT NULL,
  `bottleneck_weight` decimal(16,10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_report_id` (`report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_report_bottleneck_interface
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_report_business_activity_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_report_business_activity_detail`;
CREATE TABLE `t_report_business_activity_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_id` bigint(20) NOT NULL COMMENT '报告ID',
  `scene_id` bigint(20) NOT NULL COMMENT '场景ID',
  `business_activity_id` bigint(20) NOT NULL COMMENT '业务活动ID',
  `business_activity_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '业务活动名称',
  `request` bigint(10) DEFAULT NULL COMMENT '请求数',
  `sa` decimal(10,2) DEFAULT NULL COMMENT 'sa',
  `tps` decimal(10,2) DEFAULT NULL COMMENT 'tps',
  `rt` decimal(10,2) DEFAULT NULL COMMENT '响应时间',
  `success_rate` decimal(10,2) DEFAULT NULL COMMENT '成功率',
  `max_tps` decimal(10,2) DEFAULT NULL COMMENT '最大tps',
  `max_rt` decimal(10,2) DEFAULT NULL COMMENT '最大响应时间',
  `min_rt` decimal(10,2) DEFAULT NULL COMMENT '最小响应时间',
  `features` text COLLATE utf8_bin,
  `is_deleted` int(1) DEFAULT '0',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP,
  `gmt_update` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_report_business_activity_detail
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_report_list
-- ----------------------------
DROP TABLE IF EXISTS `t_report_list`;
CREATE TABLE `t_report_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `second_link_id` bigint(20) DEFAULT NULL,
  `second_link_tps` int(10) DEFAULT NULL COMMENT '二级链路的TPS',
  `link_service_name` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '业务链路名称',
  `link_basic_name` text COLLATE utf8_bin COMMENT '基础链路名称',
  `link_basic` mediumtext COLLATE utf8_bin COMMENT '基础链路实体',
  `start_time` datetime DEFAULT NULL COMMENT '压测开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '压测结束时间',
  `rt_standard` int(6) NOT NULL DEFAULT '0' COMMENT 'rt达标标准',
  `status` int(2) NOT NULL DEFAULT '0' COMMENT '0:压测完成;1:压测中',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` int(2) NOT NULL DEFAULT '0' COMMENT '是否已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='压测报告列表';

-- ----------------------------
-- Records of t_report_list
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_report_machine
-- ----------------------------
DROP TABLE IF EXISTS `t_report_machine`;
CREATE TABLE `t_report_machine` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_id` bigint(20) NOT NULL,
  `application_name` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '应用名称',
  `machine_ip` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '机器ip',
  `agent_id` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'agentid',
  `machine_base_config` varchar(512) COLLATE utf8_bin DEFAULT NULL COMMENT '机器基本信息',
  `machine_tps_target_config` text COLLATE utf8_bin COMMENT '机器tps对应指标信息',
  `risk_value` decimal(16,6) DEFAULT NULL COMMENT '风险计算值',
  `risk_flag` tinyint(4) DEFAULT NULL COMMENT '是否风险机器(0-否，1-是)',
  `risk_content` varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT '风险提示内容',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_report_application_machine` (`report_id`,`application_name`,`machine_ip`) USING BTREE,
  KEY `idx_report_id_application_name` (`report_id`,`application_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_report_machine
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_report_summary
-- ----------------------------
DROP TABLE IF EXISTS `t_report_summary`;
CREATE TABLE `t_report_summary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_id` bigint(20) NOT NULL,
  `bottleneck_interface_count` int(11) DEFAULT NULL COMMENT '瓶颈接口',
  `risk_machine_count` int(11) DEFAULT NULL COMMENT '风险机器数',
  `business_activity_count` int(11) DEFAULT NULL COMMENT '业务活动数',
  `unachieve_business_activity_count` int(11) DEFAULT NULL COMMENT '未达标业务活动数',
  `application_count` int(11) DEFAULT NULL COMMENT '应用数',
  `machine_count` int(11) DEFAULT NULL COMMENT '机器数',
  `warn_count` int(11) DEFAULT NULL COMMENT '告警次数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_report_id` (`report_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_report_summary
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_return_data
-- ----------------------------
DROP TABLE IF EXISTS `t_return_data`;
CREATE TABLE `t_return_data` (
  `TRD_ID` bigint(20) NOT NULL COMMENT '回传id',
  `INC_DATA_JAR_NAME` varchar(64) NOT NULL COMMENT 'JAR包名称',
  `INC_DATA_JAR_PATH` varchar(128) NOT NULL COMMENT 'JAR包保存路径',
  `PRINCIPAL_NO` varchar(16) DEFAULT NULL COMMENT '负责人工号',
  `LINK_ID` bigint(20) NOT NULL COMMENT '链路ID',
  `START_CLASS` varchar(128) NOT NULL COMMENT '启动类',
  `LOAD_STATUS` int(1) DEFAULT '2' COMMENT '抽数状态(1表示正在运行,2表示运行结束)',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`TRD_ID`) USING BTREE,
  UNIQUE KEY `rt_unique` (`INC_DATA_JAR_NAME`,`START_CLASS`) USING BTREE COMMENT '相同jar包不能有相同启动类'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据回传JAR配置';

-- ----------------------------
-- Records of t_return_data
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_rocketmq_producer
-- ----------------------------
DROP TABLE IF EXISTS `t_rocketmq_producer`;
CREATE TABLE `t_rocketmq_producer` (
  `TRP_ID` bigint(20) NOT NULL COMMENT 'ROCKETMQ生产者id',
  `MSG_TYPE` varchar(5) DEFAULT NULL,
  `DICT_TYPE` varchar(32) DEFAULT NULL COMMENT '数据字典类型',
  `MSG_IP` varchar(500) DEFAULT NULL COMMENT '集群ip(形式为host:port;host:port,ROCKETMQ使用)',
  `TOPIC` varchar(50) DEFAULT NULL COMMENT '订阅主题',
  `GROUPNAME` varchar(30) DEFAULT NULL COMMENT '组名',
  `SLEEP_TIME` bigint(20) DEFAULT NULL COMMENT '消息休眠时间,默认为毫秒',
  `MSG_COUNT` bigint(20) DEFAULT NULL COMMENT '消息发送数量,默认为条',
  `THREAD_COUNT` int(5) DEFAULT NULL COMMENT '线程数量',
  `MESSAGE_SIZE` bigint(20) DEFAULT NULL COMMENT '消息大小',
  `MSG_SUCCESS_COUNT` bigint(20) DEFAULT NULL COMMENT '发送消息成功数量',
  `PRODUCE_STATUS` varchar(5) DEFAULT '0' COMMENT '生产消息状态, 0未生产消息 1正在生产消息 2已停止生产消息 3生产消息异常  4正在生产但是有发送失败的数据',
  `PRODUCE_START_TIME` datetime DEFAULT NULL COMMENT '开始生产消息时间',
  `PRODUCE_END_TIME` datetime DEFAULT NULL COMMENT '停止生产消息时间',
  `LAST_PRODUCE_TIME` datetime DEFAULT NULL COMMENT '上次停止生产时间',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime NOT NULL COMMENT '更新时间',
  `THREAD_COUNT1` bigint(10) DEFAULT NULL COMMENT '线程数量',
  PRIMARY KEY (`TRP_ID`) USING BTREE,
  KEY `TRP_INDEX1` (`PRODUCE_STATUS`) USING BTREE,
  KEY `TRP_INDEX2` (`PRODUCE_START_TIME`,`PRODUCE_END_TIME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='ROCKETMQ生产消息数据表';

-- ----------------------------
-- Records of t_rocketmq_producer
-- ----------------------------
BEGIN;
INSERT INTO `t_rocketmq_producer` VALUES (6446027738628231160, '3', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.44.41:9876;10.230.44.44:9876', 'PT_RocketMqTest20180413', 'rocketmq-producer-demo', 20, 1000, NULL, NULL, 1000, '0', '2018-09-14 21:21:47', '2018-09-14 21:21:59', '2018-09-14 05:37:29', '2018-09-14 01:23:06', '2018-09-14 21:21:59', NULL);
INSERT INTO `t_rocketmq_producer` VALUES (6446072232371294208, '3', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.20.224:9876', 'PT_BASIC_JCH', 'PT_TEST_0915', 1000, 10000, 10, 1024, 5, '3', '2018-10-17 10:46:59', '2018-10-17 10:47:26', '2018-10-17 10:46:38', '2018-09-14 02:29:42', '2018-10-17 10:47:47', NULL);
INSERT INTO `t_rocketmq_producer` VALUES (6446117219251261440, '3', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.44.41:9876;10.230.44.44:9876', 'PT_TRO_TEST_001', 'PT_TEST_001', 1000, 3000, 10, NULL, 100, '0', '2018-09-26 18:28:44', '2018-09-26 18:30:26', '2018-09-21 18:42:01', '2018-09-14 05:28:28', '2018-09-26 18:30:26', NULL);
INSERT INTO `t_rocketmq_producer` VALUES (6446117219251261444, '3', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.44.41:9876;10.230.44.44:9876', 'JasonYan_20180920', 'JasonYan_group', 200, 20, NULL, NULL, 1, '0', '2018-10-09 14:27:28', '2018-10-09 14:28:00', '2018-09-21 16:53:44', '2018-09-14 05:28:28', '2018-10-09 14:28:00', NULL);
INSERT INTO `t_rocketmq_producer` VALUES (6446565135631781888, '3', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.44.41:9876;10.230.44.44:9876', 'PT_GL_0915', 'PT_YP_TEST', 10, 200, NULL, NULL, 200, '0', '2018-09-15 11:09:37', '2018-09-15 11:10:06', NULL, '2018-09-15 11:08:19', '2018-09-15 11:10:06', NULL);
INSERT INTO `t_rocketmq_producer` VALUES (6450959832269721600, '3', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.44.41:9876;10.230.44.44:9876', 'PT_TRO_TEST_0923', 'PT_TRO_CGROUP_0923', 1, 1000, NULL, NULL, NULL, '0', '2018-09-27 15:02:08', '2018-09-27 14:39:18', '2018-09-27 14:39:18', '2018-09-27 14:11:17', '2018-10-09 14:28:58', NULL);
INSERT INTO `t_rocketmq_producer` VALUES (6456447957070778368, '3', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.44.5:8888', 'TEST20181012', 'JasonYan', 100, 100, 1, 100, NULL, '0', NULL, NULL, NULL, '2018-10-12 17:39:08', '2018-10-12 17:39:08', NULL);
INSERT INTO `t_rocketmq_producer` VALUES (6456690457249320960, '4', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.20.224:9876', 'PT_BASIC_JCH', 'PT_TEST_0915', 1000, 10000, 1, 1000, NULL, '0', '2018-10-13 13:38:24', '2018-10-13 13:38:29', '2018-10-13 11:18:25', '2018-10-13 09:42:44', '2018-10-13 13:38:28', NULL);
INSERT INTO `t_rocketmq_producer` VALUES (6456693292825317376, '3', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.44.41:9876;10.230.44.44:9876', 'PT_TRO_TEST_1013', 'PT_TRO_CGROUP_0913', 2, 100, 5, 24, NULL, '3', '2018-10-16 14:43:50', '2018-10-16 14:49:52', '2018-10-16 14:38:12', '2018-10-13 09:54:00', '2018-10-16 14:50:10', NULL);
INSERT INTO `t_rocketmq_producer` VALUES (6456696336585592832, '4', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.44.41:9876;10.230.44.44:9876', 'TRO_TEST_DO_NOT_DELETE', 'TRO_TEST_GROUP', 1000, 10000, 10, 1000, NULL, '0', '2018-10-13 13:34:31', '2018-10-13 13:34:57', '2018-10-13 13:30:25', '2018-10-13 10:06:06', '2018-10-13 13:34:56', NULL);
INSERT INTO `t_rocketmq_producer` VALUES (6457419982841057280, '4', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.44.41:9876;10.230.44.44:9876', 'PT_TRO_TEST_1016', 'PT_TRO_CGROUP_1016', 1, 1000, 100, 1024, NULL, '3', '2018-10-16 15:23:43', '2018-10-16 15:35:37', '2018-10-16 11:06:48', '2018-10-15 10:01:36', '2018-10-16 15:35:49', NULL);
INSERT INTO `t_rocketmq_producer` VALUES (6457798218569748480, '3', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.44.41:9876;10.230.44.44:9876', 'PT_TRO_TEST_0915', 'PT_TRO_CGROUP_0915', 1, 1000, 200, 10, NULL, '2', '2018-10-19 16:36:10', '2018-10-19 16:36:36', '2018-10-17 14:54:06', '2018-10-16 11:04:35', '2018-10-19 16:36:35', NULL);
INSERT INTO `t_rocketmq_producer` VALUES (6457858919648727040, '3', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.44.41:9876;10.230.44.44:9876', 'PT_TRO_TEST_1016', 'PT_TRO_CGROUP_1016', 1, 1000, 100, 2048, NULL, '2', '2019-05-29 08:40:33', '2019-05-29 08:40:54', '2018-10-22 16:38:56', '2018-10-16 15:05:47', '2019-05-29 08:40:54', NULL);
INSERT INTO `t_rocketmq_producer` VALUES (6457872934894571520, '4', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.44.41:9876;10.230.44.44:9876', 'PT_DPBOOT_TEST_1016', 'PT_DPBOOT_CGROUP_1016', 1, 1, 1, 2048, NULL, '2', '2019-05-29 10:17:23', '2019-05-29 10:18:43', '2019-05-29 09:12:46', '2018-10-16 16:01:29', '2019-05-29 10:18:43', NULL);
INSERT INTO `t_rocketmq_producer` VALUES (6458960799535730688, '3', 'ec18bced105c41018cfbcaf6b3327b9a', '10.230.44.41:9876;10.230.44.44:9876', 'PT_DPBoot_RMQ_1019', 'PT_DPBoot_CGROUP_1019', 1, 1000, 100, 2048, NULL, '2', '2018-10-19 16:08:06', '2018-10-19 16:10:57', '2018-10-19 16:07:16', '2018-10-19 16:04:16', '2018-10-19 16:10:56', NULL);
COMMIT;

-- ----------------------------
-- Table structure for t_scene
-- ----------------------------
DROP TABLE IF EXISTS `t_scene`;
CREATE TABLE `t_scene` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `SCENE_NAME` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '场景名',
  `BUSINESS_LINK` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '场景所绑定的业务链路名集合',
  `SCENE_LEVEL` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '场景等级 :p0/p1/02/03',
  `IS_CORE` tinyint(4) DEFAULT '0' COMMENT '是否核心场景 0:不是;1:是',
  `IS_CHANGED` tinyint(4) DEFAULT '0' COMMENT '是否有变更 0:没有变更，1有变更',
  `CUSTOMER_ID` bigint(20) DEFAULT NULL COMMENT '租户id',
  `USER_ID` bigint(20) DEFAULT NULL COMMENT '用户id',
  `IS_DELETED` tinyint(4) DEFAULT '0' COMMENT '是否有效 0:有效;1:无效',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `T_LINK_MNT_INDEX1` (`SCENE_NAME`) USING BTREE,
  KEY `T_LINK_MNT_INDEX2` (`SCENE_NAME`) USING BTREE,
  KEY `T_LINK_MNT_INDEX3` (`CREATE_TIME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场景表';

-- ----------------------------
-- Records of t_scene
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_scene_add_temp_table
-- ----------------------------
DROP TABLE IF EXISTS `t_scene_add_temp_table`;
CREATE TABLE `t_scene_add_temp_table` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `BUSENESS_ID` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '业务链路id',
  `TECH_ID` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '技术链路id',
  `PARENT_BUSINESS_ID` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '业务链路的上级业务链路',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `T_LINK_MNT_INDEX1` (`BUSENESS_ID`) USING BTREE,
  KEY `T_LINK_MNT_INDEX2` (`TECH_ID`(255)) USING BTREE,
  KEY `T_LINK_MNT_INDEX3` (`PARENT_BUSINESS_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场景添加暂存表';

-- ----------------------------
-- Records of t_scene_add_temp_table
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_scene_business_activity_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_scene_business_activity_ref`;
CREATE TABLE `t_scene_business_activity_ref` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scene_id` bigint(20) NOT NULL COMMENT '场景ID',
  `business_activity_id` bigint(20) NOT NULL COMMENT '业务活动id',
  `business_activity_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '业务活动名称',
  `application_ids` varchar(1024) COLLATE utf8_bin DEFAULT NULL COMMENT '关联应用id，多个用,隔开',
  `goal_value` text COLLATE utf8_bin NOT NULL COMMENT '目标值，json',
  `bind_ref` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '绑定关系：jmx文件采样器名称',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0',
  `create_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `create_name` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `update_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `update_name` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_scene_id` (`scene_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_scene_business_activity_ref
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_scene_link_relate
-- ----------------------------
DROP TABLE IF EXISTS `t_scene_link_relate`;
CREATE TABLE `t_scene_link_relate` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ENTRANCE` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '链路入口',
  `IS_DELETED` tinyint(4) DEFAULT '0' COMMENT '是否有效 0:有效;1:无效',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `BUSINESS_LINK_ID` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '业务链路ID',
  `TECH_LINK_ID` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '技术链路ID',
  `SCENE_ID` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '场景ID',
  `PARENT_BUSINESS_LINK_ID` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '当前业务链路ID的上级业务链路ID',
  `FRONT_UUID_KEY` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '前端数结构对象key',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `T_LINK_MNT_INDEX2` (`ENTRANCE`(255)) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='链路场景关联表';

-- ----------------------------
-- Records of t_scene_link_relate
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_scene_manage
-- ----------------------------
DROP TABLE IF EXISTS `t_scene_manage`;
CREATE TABLE `t_scene_manage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scene_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '场景名称',
  `status` tinyint(4) NOT NULL COMMENT '参考数据字典 场景状态',
  `last_pt_time` timestamp NULL DEFAULT NULL COMMENT '最新压测时间',
  `pt_config` text COLLATE utf8_bin COMMENT '施压配置',
  `script_type` tinyint(4) DEFAULT NULL COMMENT '脚本类型：0-Jmeter 1-Gatling',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除：0-否 1-是',
  `create_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `create_name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `update_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '最后修改时间',
  `update_name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '最后修改人',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `ide_last_pt_time` (`last_pt_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_scene_manage
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_scene_scheduler_task
-- ----------------------------
DROP TABLE IF EXISTS `t_scene_scheduler_task`;
CREATE TABLE `t_scene_scheduler_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scene_id` bigint(20) NOT NULL COMMENT '场景id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `content` text COMMENT '启动场景参数',
  `is_executed` tinyint(2) DEFAULT '0' COMMENT '0：待执行，1:执行中；2:已执行',
  `execute_time` datetime NOT NULL COMMENT '压测场景定时执行时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '状态 0: 正常 1： 删除',
  `gmt_create` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `gmt_update` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_scene_scheduler_task
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_scene_script_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_scene_script_ref`;
CREATE TABLE `t_scene_script_ref` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scene_id` bigint(20) NOT NULL COMMENT '场景ID',
  `script_type` tinyint(4) NOT NULL COMMENT '脚本类型',
  `file_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '文件名称',
  `file_size` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '文件大小：2MB',
  `file_type` tinyint(4) NOT NULL COMMENT '文件类型：0-脚本文件 1-数据文件',
  `upload_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '上传时间',
  `upload_path` varchar(128) COLLATE utf8_bin NOT NULL COMMENT '上传路径：相对路径',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0',
  `create_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `create_name` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `update_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `update_name` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_scene_id` (`scene_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_scene_script_ref
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_scene_sla_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_scene_sla_ref`;
CREATE TABLE `t_scene_sla_ref` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scene_id` bigint(20) NOT NULL COMMENT '场景ID',
  `sla_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '规则名称',
  `business_activity_ids` varchar(512) COLLATE utf8_bin NOT NULL COMMENT '业务活动ID，多个用,隔开，-1表示所有',
  `target_type` tinyint(4) NOT NULL COMMENT '指标类型：0-RT 1-TPS 2-成功率 3-SA',
  `condition` text COLLATE utf8_bin NOT NULL COMMENT '警告/终止逻辑：json格式',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态：0-启用 1-禁用',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0',
  `create_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `create_name` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `update_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `update_name` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_scene_id` (`scene_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_scene_sla_ref
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_scene_tag_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_scene_tag_ref`;
CREATE TABLE `t_scene_tag_ref` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scene_id` bigint(20) NOT NULL COMMENT '场景发布id',
  `tag_id` bigint(20) NOT NULL COMMENT '标签id',
  `gmt_create` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `gmt_update` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_sceneId_tagId` (`scene_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_scene_tag_ref
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_schedule_event
-- ----------------------------
DROP TABLE IF EXISTS `t_schedule_event`;
CREATE TABLE `t_schedule_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `event_type` int(2) NOT NULL COMMENT '事件类型:1、文件拆分，2、开通机器',
  `status` int(1) NOT NULL COMMENT '状态：-1，失败，1、处理中，2、完成',
  `features` text COLLATE utf8_bin COMMENT '扩展字段',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `gmt_create` datetime DEFAULT NULL,
  `gmt_update` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_schedule_event
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_schedule_record
-- ----------------------------
DROP TABLE IF EXISTS `t_schedule_record`;
CREATE TABLE `t_schedule_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scene_id` bigint(20) NOT NULL COMMENT '场景ID',
  `task_id` bigint(20) DEFAULT NULL COMMENT '任务ID',
  `pod_num` int(11) DEFAULT NULL COMMENT 'pod数量',
  `pod_class` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'pod种类',
  `status` tinyint(4) DEFAULT NULL COMMENT '调度结果 0-失败 1-成功',
  `cpu_core_num` decimal(6,2) DEFAULT NULL COMMENT 'CPU核数',
  `memory_size` decimal(8,2) DEFAULT NULL COMMENT '内存G',
  `create_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '调度时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_scene_id` (`scene_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_schedule_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_script_execute_result
-- ----------------------------
DROP TABLE IF EXISTS `t_script_execute_result`;
CREATE TABLE `t_script_execute_result` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `script_deploy_id` bigint(20) NOT NULL COMMENT '实例id',
  `script_id` bigint(20) NOT NULL COMMENT '脚本id',
  `script_version` int(11) NOT NULL DEFAULT '0',
  `gmt_create` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `executor` varchar(256) DEFAULT NULL COMMENT '执行人',
  `success` tinyint(1) DEFAULT NULL COMMENT '执行结果',
  `result` longtext COMMENT '执行结果',
  PRIMARY KEY (`id`),
  KEY `idx_script_deploy_id` (`script_deploy_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_script_execute_result
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_script_file_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_script_file_ref`;
CREATE TABLE `t_script_file_ref` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_id` bigint(20) NOT NULL COMMENT '文件id',
  `script_deploy_id` bigint(20) NOT NULL COMMENT '脚本发布id',
  `gmt_create` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `gmt_update` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_script_file_ref
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_script_manage
-- ----------------------------
DROP TABLE IF EXISTS `t_script_manage`;
CREATE TABLE `t_script_manage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL COMMENT '名称',
  `gmt_create` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `gmt_update` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `script_version` int(11) NOT NULL DEFAULT '0',
  `is_deleted` tinyint(1) DEFAULT '0',
  `feature` longtext COMMENT '拓展字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`) USING BTREE COMMENT '名称需要唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_script_manage
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_script_manage_deploy
-- ----------------------------
DROP TABLE IF EXISTS `t_script_manage_deploy`;
CREATE TABLE `t_script_manage_deploy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `script_id` bigint(20) DEFAULT NULL,
  `name` varchar(64) NOT NULL COMMENT '名称',
  `ref_type` varchar(16) DEFAULT NULL COMMENT '关联类型(业务活动)',
  `ref_value` varchar(64) DEFAULT NULL COMMENT '关联值(活动id)',
  `type` tinyint(4) NOT NULL COMMENT '脚本类型;0为jmeter脚本',
  `status` tinyint(4) NOT NULL COMMENT '0代表新建，1代表调试通过',
  `description` varchar(512) DEFAULT NULL COMMENT '描述',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '操作人id',
  `create_user_name` varchar(64) DEFAULT NULL COMMENT '操作人名称',
  `gmt_create` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `gmt_update` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `script_version` int(11) NOT NULL DEFAULT '0',
  `is_deleted` tinyint(1) DEFAULT '0',
  `feature` longtext COMMENT '拓展字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_version` (`name`,`script_version`) COMMENT '名称加版本需要唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_script_manage_deploy
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_script_tag_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_script_tag_ref`;
CREATE TABLE `t_script_tag_ref` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `script_id` bigint(20) NOT NULL COMMENT '场景发布id',
  `tag_id` bigint(20) NOT NULL COMMENT '标签id',
  `gmt_create` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `gmt_update` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_script_tag_ref
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_second_basic
-- ----------------------------
DROP TABLE IF EXISTS `t_second_basic`;
CREATE TABLE `t_second_basic` (
  `SECOND_LINK_ID` bigint(19) NOT NULL COMMENT '二级链路id',
  `BASIC_LINK_ID` bigint(19) NOT NULL COMMENT '业务链路id',
  `BLINK_ORDER` int(10) DEFAULT NULL COMMENT '业务链路编号(每一条链路的排序)横向',
  `BLINK_BANK` int(10) DEFAULT NULL COMMENT '业务链路等级(二级链路下有基础链路1，基础链路2等)竖向',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '插入时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '变更时间',
  UNIQUE KEY `T_INDEX_SB` (`SECOND_LINK_ID`,`BASIC_LINK_ID`,`BLINK_BANK`) USING BTREE COMMENT '二级链路id,基础链路id，链路等级联合主键'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='二级链路基础链路关联关系表';

-- ----------------------------
-- Records of t_second_basic
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_second_basic_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_second_basic_ref`;
CREATE TABLE `t_second_basic_ref` (
  `SECOND_LINK_ID` bigint(20) NOT NULL COMMENT '二级链路id',
  `BASIC_LINK_ID` bigint(20) NOT NULL COMMENT '业务链路id',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '插入时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '变更时间',
  UNIQUE KEY `T_INDEX_SB` (`SECOND_LINK_ID`,`BASIC_LINK_ID`) USING BTREE COMMENT '二级链路id,基础链路id联合主键'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='二级/基础链路关联关系表';

-- ----------------------------
-- Records of t_second_basic_ref
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_second_link_mnt
-- ----------------------------
DROP TABLE IF EXISTS `t_second_link_mnt`;
CREATE TABLE `t_second_link_mnt` (
  `LINK_ID` varchar(36) NOT NULL COMMENT '主键id',
  `LINK_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '链路名称',
  `BASE_LINKS` text NOT NULL COMMENT '基础链路列表',
  `ASWAN_ID` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '阿斯旺id',
  `LINK_TPS` bigint(19) DEFAULT NULL COMMENT '链路TPS',
  `TARGET_TPS` bigint(19) DEFAULT NULL COMMENT '目标TPS',
  `LINK_TPS_RULE` text COMMENT '二级链路TPS计算规则',
  `USE_YN` int(1) DEFAULT NULL COMMENT '是否可用(0表示未启用,1表示启用)',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '插入时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '变更时间',
  `remark` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注(预留)',
  `TEST_STATUS` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '0' COMMENT '测试状态 0没在测试 1正在测试',
  PRIMARY KEY (`LINK_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='二级链路管理';

-- ----------------------------
-- Records of t_second_link_mnt
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_shadow_job_config
-- ----------------------------
DROP TABLE IF EXISTS `t_shadow_job_config`;
CREATE TABLE `t_shadow_job_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `application_id` bigint(19) NOT NULL COMMENT '应用ID',
  `name` varchar(100) NOT NULL COMMENT '任务名称',
  `type` tinyint(2) NOT NULL COMMENT 'JOB类型 0-quartz、1-elastic-job、2-xxl-job',
  `config_code` text COMMENT '配置代码',
  `status` tinyint(1) DEFAULT '1' COMMENT '0-可用 1-不可用',
  `active` tinyint(1) DEFAULT '1' COMMENT '0-可用 1-不可用',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `is_deleted` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除 0-未删除、1-已删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_app_id` (`application_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='影子JOB任务配置';

-- ----------------------------
-- Records of t_shadow_job_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_shadow_mq_consumer
-- ----------------------------
DROP TABLE IF EXISTS `t_shadow_mq_consumer`;
CREATE TABLE `t_shadow_mq_consumer` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `topic_group` varchar(1000) DEFAULT NULL COMMENT 'topic+group，以#号拼接',
  `type` varchar(20) DEFAULT NULL COMMENT '白名单类型',
  `application_id` bigint(19) DEFAULT NULL COMMENT '应用id',
  `application_name` varchar(200) DEFAULT NULL COMMENT '应用名称，冗余',
  `status` int(1) DEFAULT '1' COMMENT '是否可用(0表示未启用,1表示已启用)',
  `deleted` int(1) DEFAULT '0' COMMENT '是否可用(0表示未删除,1表示已删除)',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `feature` text COMMENT '拓展字段',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_app_id` (`application_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='影子消费者';

-- ----------------------------
-- Records of t_shadow_mq_consumer
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_shadow_table_config
-- ----------------------------
DROP TABLE IF EXISTS `t_shadow_table_config`;
CREATE TABLE `t_shadow_table_config` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '影子表配置id',
  `SHADOW_DATASOURCE_ID` bigint(20) DEFAULT NULL COMMENT '影子表所属数据源',
  `SHADOW_TABLE_NAME` varchar(128) NOT NULL COMMENT '需要使用影子表的表名',
  `SHADOW_TABLE_OPERATION` varchar(128) DEFAULT NULL COMMENT '该表 有哪些操作',
  `ENABLE_STATUS` int(4) DEFAULT NULL COMMENT '是否使用 影子表 1 使用 0 不使用',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '插入时间',
  `UPDATE_TIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `SHADOW_TABLE_INDEX1` (`SHADOW_TABLE_NAME`) USING BTREE,
  KEY `SHADOW_TABLE_INDEX2` (`SHADOW_DATASOURCE_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='影子表配置表';

-- ----------------------------
-- Records of t_shadow_table_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_shadow_table_datasource
-- ----------------------------
DROP TABLE IF EXISTS `t_shadow_table_datasource`;
CREATE TABLE `t_shadow_table_datasource` (
  `SHADOW_DATASOURCE_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '影子表数据源id',
  `APPLICATION_ID` bigint(20) NOT NULL COMMENT '关联app_mn主键id',
  `DATABASE_IPPORT` varchar(128) NOT NULL COMMENT '数据库ip端口  xx.xx.xx.xx:xx',
  `DATABASE_NAME` varchar(128) NOT NULL COMMENT '数据库表明',
  `USE_SHADOW_TABLE` int(4) DEFAULT NULL COMMENT '是否使用 影子表 1 使用 0 不使用',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '插入时间',
  `UPDATE_TIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`SHADOW_DATASOURCE_ID`) USING BTREE,
  UNIQUE KEY `SHADOW_DATASOURCE_INDEX2` (`APPLICATION_ID`,`DATABASE_IPPORT`,`DATABASE_NAME`) USING BTREE,
  KEY `SHADOW_DATASOURCE_INDEX1` (`APPLICATION_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='影子表数据源配置';

-- ----------------------------
-- Records of t_shadow_table_datasource
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_snapshot_app_info
-- ----------------------------
DROP TABLE IF EXISTS `t_snapshot_app_info`;
CREATE TABLE `t_snapshot_app_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `pessure_process_id` bigint(20) NOT NULL COMMENT '压测任务流量ID',
  `app_id` varchar(19) NOT NULL COMMENT '应用名称',
  `app_name` varchar(50) NOT NULL COMMENT '应用名称',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0-未删除、1-已删除',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报告应用列表';

-- ----------------------------
-- Records of t_snapshot_app_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_snapshot_server_info
-- ----------------------------
DROP TABLE IF EXISTS `t_snapshot_server_info`;
CREATE TABLE `t_snapshot_server_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `app_server_id` bigint(20) NOT NULL COMMENT '压测报告ID',
  `server_ip` varchar(15) NOT NULL COMMENT '服务器IP',
  `instance_id` varchar(50) NOT NULL COMMENT '实例ID',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0-未删除、1-已删除',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报告快照应用服务器列表';

-- ----------------------------
-- Records of t_snapshot_server_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_strategy_config
-- ----------------------------
DROP TABLE IF EXISTS `t_strategy_config`;
CREATE TABLE `t_strategy_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `strategy_name` varchar(128) COLLATE utf8_bin NOT NULL COMMENT '策略名称',
  `strategy_config` text COLLATE utf8_bin NOT NULL COMMENT '策略配置',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_strategy_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_tag_manage
-- ----------------------------
DROP TABLE IF EXISTS `t_tag_manage`;
CREATE TABLE `t_tag_manage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(64) NOT NULL COMMENT '标签名称',
  `tag_type` tinyint(4) DEFAULT NULL COMMENT '标签类型;0为脚本标签;1为数据源标签',
  `tag_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '标签状态;0为可用',
  `gmt_create` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  `gmt_update` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_tag_manage
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_tc_sequence
-- ----------------------------
DROP TABLE IF EXISTS `t_tc_sequence`;
CREATE TABLE `t_tc_sequence` (
  `id` bigint(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) COLLATE utf8_bin NOT NULL,
  `value` bigint(20) DEFAULT NULL,
  `gmt_modified` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_NAME` (`name`) USING BTREE,
  KEY `IDX_SEQUENCE_NAME` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_tc_sequence
-- ----------------------------
BEGIN;
INSERT INTO `t_tc_sequence` VALUES (13, 'BASE_ORDER_LINE', 86000, '2021-04-19 15:40:41.164');
INSERT INTO `t_tc_sequence` VALUES (14, 'THREAD_ORDER_LINE', 30358500, '2021-04-19 17:25:02.413');
COMMIT;

-- ----------------------------
-- Table structure for t_trace_manage
-- ----------------------------
DROP TABLE IF EXISTS `t_trace_manage`;
CREATE TABLE `t_trace_manage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trace_object` varchar(128) NOT NULL COMMENT '追踪对象',
  `report_id` bigint(20) DEFAULT NULL COMMENT '报告id',
  `agent_id` varchar(128) DEFAULT NULL,
  `server_ip` varchar(16) DEFAULT NULL COMMENT '服务器ip',
  `process_id` int(11) DEFAULT NULL COMMENT '进程id',
  `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `feature` text COMMENT '拓展字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_trace_manage
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_trace_manage_deploy
-- ----------------------------
DROP TABLE IF EXISTS `t_trace_manage_deploy`;
CREATE TABLE `t_trace_manage_deploy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trace_manage_id` bigint(20) NOT NULL COMMENT '追踪对象id',
  `trace_deploy_object` varchar(128) NOT NULL COMMENT '追踪对象实例名称',
  `sample_id` varchar(128) DEFAULT NULL COMMENT '追踪凭证',
  `level` int(11) DEFAULT NULL COMMENT '级别',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父id',
  `has_children` int(11) DEFAULT '2' COMMENT '0:没有;1:有;2未知',
  `line_num` int(11) DEFAULT NULL COMMENT '行号',
  `avg_cost` decimal(20,6) DEFAULT NULL COMMENT '平均耗时',
  `p50` decimal(20,6) DEFAULT NULL COMMENT 'p50',
  `p90` decimal(20,6) DEFAULT NULL COMMENT 'p90',
  `p95` decimal(20,6) DEFAULT NULL COMMENT 'p95',
  `p99` decimal(20,6) DEFAULT NULL COMMENT 'p99',
  `min` decimal(20,6) DEFAULT NULL COMMENT 'min',
  `max` decimal(20,6) DEFAULT NULL COMMENT 'max',
  `status` int(11) NOT NULL COMMENT '状态0:待采集;1:采集中;2:采集结束',
  `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `feature` text COMMENT '拓展字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_trace_manage_deploy
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_trace_node_info
-- ----------------------------
DROP TABLE IF EXISTS `t_trace_node_info`;
CREATE TABLE `t_trace_node_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(512) DEFAULT NULL COMMENT '应用名',
  `trace_id` varchar(512) DEFAULT NULL COMMENT 'traceId',
  `rpc_id` varchar(512) DEFAULT NULL COMMENT 'rpcid',
  `log_type` tinyint(4) DEFAULT NULL COMMENT 'log_type',
  `trace_app_name` varchar(512) DEFAULT NULL COMMENT '入口的app名称',
  `up_app_name` varchar(512) DEFAULT NULL COMMENT '上游的app名称',
  `start_time` bigint(20) DEFAULT NULL COMMENT '开始时间的时间戳',
  `cost` bigint(20) DEFAULT NULL COMMENT '耗时',
  `middleware_name` varchar(512) DEFAULT NULL COMMENT '中间件名称',
  `service_name` varchar(512) DEFAULT NULL COMMENT '服务名',
  `method_name` varchar(512) DEFAULT NULL COMMENT '方法名',
  `remote_ip` varchar(128) DEFAULT NULL COMMENT '远程IP',
  `result_code` varchar(128) DEFAULT NULL COMMENT '结果编码',
  `request` longtext COMMENT '请求内容',
  `response` longtext COMMENT '响应内容',
  `callback_msg` longtext COMMENT '附加信息',
  `cluster_test` tinyint(1) DEFAULT NULL COMMENT '1是压测流量，0是业务流量',
  `host_ip` varchar(512) DEFAULT NULL COMMENT '日志所属机器ip',
  `agent_id` varchar(512) DEFAULT NULL COMMENT 'agent id',
  `gmt_create` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `gmt_modified` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '软删',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `rpc_type` tinyint(4) DEFAULT NULL COMMENT 'rpc类型',
  `port` tinyint(4) DEFAULT NULL COMMENT '端口',
  `is_upper_unknown_node` tinyint(1) DEFAULT NULL COMMENT '是否下游有未知节点',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_trace_node_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_tro_authority
-- ----------------------------
DROP TABLE IF EXISTS `t_tro_authority`;
CREATE TABLE `t_tro_authority` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id主键',
  `role_id` varchar(50) DEFAULT NULL COMMENT '角色id',
  `resource_id` varchar(255) NOT NULL COMMENT '资源id',
  `action` varchar(255) DEFAULT NULL COMMENT '操作类型(0:all,1:query,2:create,3:update,4:delete,5:start,6:stop,7:export,8:enable,9:disable,10:auth)',
  `status` tinyint(1) DEFAULT '0' COMMENT '是否启用 0:启用;1:禁用',
  `object_type` tinyint(1) DEFAULT NULL COMMENT '对象类型:0:角色 1:用户(4.5.1版本后废弃不用)',
  `object_id` varchar(255) DEFAULT NULL COMMENT '对象id:角色,用户(4.5.1版本后废弃不用)',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否有效 0:有效;1:无效',
  `scope` varchar(255) DEFAULT NULL COMMENT '权限范围：0:全部 1:本部门 2:本部门及以下 3:自己及以下 3:仅自己',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=834 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_tro_authority
-- ----------------------------
BEGIN;
INSERT INTO `t_tro_authority` VALUES (824, '772', '4', '[2, 3, 4]', 0, NULL, NULL, NULL, '2020-11-19 09:44:29', '2020-11-19 09:44:59', 0, '[2]', NULL);
INSERT INTO `t_tro_authority` VALUES (825, '772', '5', '[2, 3, 4]', 0, NULL, NULL, NULL, '2020-11-19 09:44:29', '2020-11-19 09:44:59', 0, '[2]', NULL);
INSERT INTO `t_tro_authority` VALUES (826, '772', '6', '[2, 3, 4, 6]', 0, NULL, NULL, NULL, '2020-11-19 09:44:29', '2020-11-19 09:44:59', 0, '[2]', NULL);
INSERT INTO `t_tro_authority` VALUES (827, '772', '368', '[2, 3, 4, 7]', 0, NULL, NULL, NULL, '2020-11-19 09:44:29', '2020-11-19 09:44:59', 0, '[2]', NULL);
INSERT INTO `t_tro_authority` VALUES (828, '772', '9', '[2, 3, 4, 5]', 0, NULL, NULL, NULL, '2020-11-19 09:44:29', '2020-11-19 09:44:59', 0, '[2]', NULL);
INSERT INTO `t_tro_authority` VALUES (829, '772', '10', '[]', 0, NULL, NULL, NULL, '2020-11-19 09:44:29', '2020-11-19 09:44:59', 0, '[2]', NULL);
INSERT INTO `t_tro_authority` VALUES (830, '772', '14', '[2, 3, 4, 6]', 0, NULL, NULL, NULL, '2020-11-19 09:44:29', '2020-11-19 09:44:59', 0, '[2]', NULL);
INSERT INTO `t_tro_authority` VALUES (831, '772', '15', '[2, 3, 4]', 0, NULL, NULL, NULL, '2020-11-19 09:44:29', '2020-11-19 09:44:59', 0, '[2]', NULL);
INSERT INTO `t_tro_authority` VALUES (832, '772', '307', '[]', 0, NULL, NULL, NULL, '2020-11-19 09:44:29', '2020-11-19 09:44:29', 0, NULL, NULL);
INSERT INTO `t_tro_authority` VALUES (833, '772', '16', '[]', 0, NULL, NULL, NULL, '2020-11-19 09:44:29', '2020-11-19 09:44:29', 0, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for t_tro_base_role
-- ----------------------------
DROP TABLE IF EXISTS `t_tro_base_role`;
CREATE TABLE `t_tro_base_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色id主键',
  `name` varchar(20) NOT NULL COMMENT '角色名称',
  `alias` varchar(20) NOT NULL COMMENT '角色别名',
  `code` varchar(20) NOT NULL COMMENT '角色编码',
  `description` varchar(255) NOT NULL COMMENT '角色描述',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态(0:启用 1:禁用)',
  `action` varchar(255) NOT NULL COMMENT '操作类型(0:all,1:query,2:create,3:update,4:delete,5:start,6:stop,7:export,8:enable,9:disable,10:auth)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='基础角色表';

-- ----------------------------
-- Records of t_tro_base_role
-- ----------------------------
BEGIN;
INSERT INTO `t_tro_base_role` VALUES (1, '应用管理员', '', 'APP_ADMIN', '应用管理员对应用具有基础的「编辑」、「删除」、「查看」权限，并可以对「应用组长」「应用组员」进行账号管理', 0, '[\"3\",\"4\",\"1\",\"10\"]');
INSERT INTO `t_tro_base_role` VALUES (2, '应用组长', '', 'APP_MANAGER', '应用组长对应用具有基础的「编辑」、「删除」、「查看」权限，并可以对「应用组员」进行账号管理', 0, '[\"3\",\"4\",\"1\",\"10\"]');
INSERT INTO `t_tro_base_role` VALUES (3, '应用组员', '', 'APP_USER', '应用组员对应用具有基础的「查看」权限', 0, '[\"1\"]');
COMMIT;

-- ----------------------------
-- Table structure for t_tro_dbresource
-- ----------------------------
DROP TABLE IF EXISTS `t_tro_dbresource`;
CREATE TABLE `t_tro_dbresource` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `type` tinyint(2) DEFAULT '0' COMMENT '0:MYSQL',
  `name` varchar(64) NOT NULL COMMENT '数据源名称',
  `jdbc_url` varchar(500) NOT NULL COMMENT '数据源地址',
  `username` varchar(64) NOT NULL COMMENT '数据源用户',
  `password` varchar(200) NOT NULL COMMENT '数据源密码',
  `features` longtext COMMENT '扩展字段，k-v形式存在',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否有效 0:有效;1:无效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_tro_dbresource
-- ----------------------------
BEGIN;
INSERT INTO `t_tro_dbresource` VALUES (8, 0, 'test-datasource', 'jdbc:mysql://127.0.0.1:3306/taco_app', 'canal', '1M+ttskHvzVPEmNvdut19Q==', NULL, 1, 1, NULL, '2021-02-03 23:28:59', '2021-02-03 23:28:59', 0);
INSERT INTO `t_tro_dbresource` VALUES (11, 0, 'canace-demo-0.0.1-02', 'jdbc:mysql://127.0.0.1:3306/tankTestDB', 'canal', '1M+ttskHvzVPEmNvdut19Q==', NULL, 1, 1, NULL, '2021-03-18 22:59:29', '2021-03-18 22:59:29', 0);
COMMIT;

-- ----------------------------
-- Table structure for t_tro_dept
-- ----------------------------
DROP TABLE IF EXISTS `t_tro_dept`;
CREATE TABLE `t_tro_dept` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门id主键',
  `name` varchar(20) NOT NULL COMMENT '部门名称',
  `code` varchar(50) DEFAULT NULL COMMENT '部门编码',
  `parent_id` bigint(11) DEFAULT NULL COMMENT '上级部门id',
  `level` varchar(50) DEFAULT NULL COMMENT '部门层级',
  `path` varchar(50) DEFAULT NULL COMMENT '部门路径',
  `sequence` int(5) DEFAULT '0' COMMENT '排序',
  `ref_id` varchar(255) DEFAULT NULL COMMENT '第三方平台唯一标识',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否有效 0:有效;1:无效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_tro_dept
-- ----------------------------
BEGIN;
INSERT INTO `t_tro_dept` VALUES (1, '一级部门A', 'A', NULL, '', '1', 1000, NULL, NULL, '2020-09-03 14:24:28', '2020-09-03 15:18:13', 0);
INSERT INTO `t_tro_dept` VALUES (2, '二级部门A1', 'A1', 1, NULL, '1.1', 1100, NULL, NULL, '2020-09-03 14:24:40', '2020-09-03 15:18:14', 0);
INSERT INTO `t_tro_dept` VALUES (3, '二级部门A2', 'A2', 1, NULL, '1.2', 1200, NULL, NULL, '2020-09-03 14:24:48', '2020-09-03 15:18:15', 0);
INSERT INTO `t_tro_dept` VALUES (4, '一级部门B', 'B', NULL, NULL, '2', 2000, NULL, NULL, '2020-09-03 14:24:56', '2020-09-03 15:18:16', 0);
INSERT INTO `t_tro_dept` VALUES (5, '二级部门B1', 'B1', 4, NULL, '2.1', 2100, NULL, NULL, '2020-09-03 14:25:02', '2020-09-03 15:18:17', 0);
INSERT INTO `t_tro_dept` VALUES (6, '二级部门B2', 'B2', 4, NULL, '2.2', 2200, NULL, NULL, '2020-09-03 14:25:11', '2020-09-03 15:18:19', 0);
INSERT INTO `t_tro_dept` VALUES (7, '三级部门B21', 'B21', 6, NULL, '2.2.1', 2210, NULL, NULL, '2020-09-03 14:25:19', '2020-09-03 15:17:39', 0);
INSERT INTO `t_tro_dept` VALUES (8, '三级部门B22', 'B22', 6, NULL, '2.2.2', 2220, NULL, NULL, '2020-09-03 14:25:26', '2020-09-03 15:17:42', 0);
COMMIT;

-- ----------------------------
-- Table structure for t_tro_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_tro_resource`;
CREATE TABLE `t_tro_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资源id主键',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父资源id',
  `type` tinyint(1) NOT NULL COMMENT '资源类型(0:菜单1:按钮 2:数据)',
  `code` varchar(100) DEFAULT NULL COMMENT '资源编码',
  `name` varchar(255) DEFAULT NULL COMMENT '资源名称',
  `alias` varchar(255) DEFAULT NULL COMMENT '资源别名，存放后端资源编码',
  `value` varchar(1024) NOT NULL COMMENT '资源值（菜单是url，应用是应用id）',
  `sequence` int(5) DEFAULT '0' COMMENT '排序',
  `action` varchar(255) DEFAULT NULL COMMENT '操作权限',
  `features` longtext COMMENT '扩展字段，k-v形式存在',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否有效 0:有效;1:无效',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_value` (`value`(191))
) ENGINE=InnoDB AUTO_INCREMENT=410 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_tro_resource
-- ----------------------------
BEGIN;
INSERT INTO `t_tro_resource` VALUES (1, NULL, 0, 'dashboard', '系统概览', '', '[\"/api/user/work/bench\"]', 1000, '[]', NULL, NULL, NULL, '2020-09-01 17:10:02', '2021-01-22 11:28:33', 0);
INSERT INTO `t_tro_resource` VALUES (2, NULL, 0, 'linkTease', '链路梳理', '', '', 2400, '[]', NULL, NULL, NULL, '2020-09-01 17:16:56', '2020-11-12 20:12:24', 0);
INSERT INTO `t_tro_resource` VALUES (4, 2, 0, 'businessActivity', '业务活动', '', '[\"/api/activities\"]', 2200, '[2,3,4]', NULL, NULL, NULL, '2020-09-01 17:26:09', '2021-01-18 11:28:48', 0);
INSERT INTO `t_tro_resource` VALUES (5, 2, 0, 'businessFlow', '业务流程', '', '[\"/api/link/scene/manage\"]', 2300, '[2,3,4]', NULL, NULL, NULL, '2020-09-01 17:26:54', '2021-01-22 11:28:33', 0);
INSERT INTO `t_tro_resource` VALUES (6, NULL, 0, 'appManage', '应用管理', NULL, '[\"/api/application/center/list\",\"/api/application/center/app/switch\",\"/api/console/switch/whitelist\"]', 3000, '[2,3,4,6]', NULL, NULL, NULL, '2020-09-01 17:31:32', '2021-01-22 11:28:33', 0);
INSERT INTO `t_tro_resource` VALUES (8, NULL, 0, 'pressureTestManage', '压测管理', NULL, '', 5300, '[]', NULL, NULL, NULL, '2020-09-01 17:36:41', '2020-11-12 20:12:33', 0);
INSERT INTO `t_tro_resource` VALUES (9, 8, 0, 'pressureTestManage_pressureTestScene', '压测场景', NULL, '[\"/api/scenemanage/list\",\"/api/application/center/app/switch\",\"/api/console/switch/whitelist\"]', 5100, '[2,3,4,5]', NULL, NULL, NULL, '2020-09-01 17:38:28', '2021-01-22 11:28:33', 0);
INSERT INTO `t_tro_resource` VALUES (10, 8, 0, 'pressureTestManage_pressureTestReport', '压测报告', NULL, '[\"/api/report/listReport\"]', 5200, '[]', NULL, NULL, NULL, '2020-09-01 17:43:10', '2020-11-06 14:18:51', 0);
INSERT INTO `t_tro_resource` VALUES (11, NULL, 0, 'configCenter', '配置中心', NULL, '', 6800, '[]', NULL, NULL, NULL, '2020-09-01 17:44:26', '2021-02-25 22:05:48', 0);
INSERT INTO `t_tro_resource` VALUES (12, 11, 0, 'configCenter_pressureMeasureSwitch', '压测开关设置', NULL, '[\"/api/application/center/app/switch\"]', 6100, '[6]', NULL, NULL, NULL, '2020-09-01 17:46:04', '2020-11-06 11:59:44', 0);
INSERT INTO `t_tro_resource` VALUES (13, 11, 0, 'configCenter_whitelistSwitch', '白名单开关设置', NULL, '[\"/api/console/switch/whitelist\"]', 6200, '[6]', NULL, NULL, NULL, '2020-09-01 17:47:15', '2021-01-22 11:28:33', 0);
INSERT INTO `t_tro_resource` VALUES (14, 11, 0, 'configCenter_blacklist', '黑名单', NULL, '[\"/api/confcenter/query/blist\"]', 6300, '[2,3,4,6]', NULL, NULL, NULL, '2020-09-01 17:48:02', '2021-01-22 11:28:33', 0);
INSERT INTO `t_tro_resource` VALUES (15, 11, 0, 'configCenter_entryRule', '入口规则', NULL, '[\"/api/api/get\"]', 6400, '[2,3,4]', NULL, NULL, NULL, '2020-09-01 17:49:15', '2020-11-02 20:31:16', 0);
INSERT INTO `t_tro_resource` VALUES (16, NULL, 0, 'flowAccount', '流量账户', NULL, '[\"/api/settle/balance/list\"]', 7000, '[]', NULL, NULL, NULL, '2020-09-01 17:51:25', '2021-01-22 11:28:33', 0);
INSERT INTO `t_tro_resource` VALUES (307, 11, 0, 'configCenter_operationLog', '操作日志', NULL, '[\"/api/operation/log/list\"]', 6500, '[]', NULL, NULL, NULL, '2020-09-28 15:27:38', '2021-01-22 11:28:33', 0);
INSERT INTO `t_tro_resource` VALUES (367, 11, 0, 'configCenter_authorityConfig', '权限管理', NULL, '[\"/api/role/list\"]', 6000, '[2,3,4]', NULL, NULL, NULL, '2020-11-10 11:53:09', '2021-01-22 11:28:33', 0);
INSERT INTO `t_tro_resource` VALUES (368, NULL, 0, 'scriptManage', '脚本管理', NULL, '[\"/api/scriptManage\"]', 4000, '[2,3,4,7]', NULL, NULL, NULL, '2020-11-10 18:55:05', '2020-11-16 18:10:12', 0);
INSERT INTO `t_tro_resource` VALUES (403, NULL, 0, 'debugTool', '调试工具', NULL, '', 9001, '[]', NULL, NULL, NULL, '2021-01-14 11:19:50', '2021-02-25 22:09:48', 0);
INSERT INTO `t_tro_resource` VALUES (404, 403, 0, 'debugTool_linkDebug', '链路调试', NULL, '[\"/debugTool/linkDebug\",\"/api/fastdebug/config/list\"]', 9000, '[2,3,4,5]', NULL, NULL, NULL, '2021-01-14 11:22:03', '2021-02-25 22:09:49', 0);
INSERT INTO `t_tro_resource` VALUES (406, 11, 0, 'configCenter_dataSourceConfig', '数据源配置', NULL, '[\"/api/datasource/list\"]', 6600, ' [2,3,4]', NULL, NULL, NULL, '2021-01-06 15:17:40', '2021-02-03 22:59:56', 0);
INSERT INTO `t_tro_resource` VALUES (407, 11, 0, 'configCenter_bigDataConfig', '开关配置', NULL, '[\"/api/pradar/switch/list\"]', 6700, '[3]', NULL, NULL, NULL, '2021-02-22 14:22:49', '2021-02-25 22:05:33', 0);
INSERT INTO `t_tro_resource` VALUES (408, 12, 0, 'appWhiteList', '白名单列表', NULL, '[\"/api/whitelist/list\"]', 7900, '[]', NULL, NULL, NULL, '2021-04-14 11:39:00', '2021-04-14 11:39:51', 0);
INSERT INTO `t_tro_resource` VALUES (409, 12, 0, 'appWhiteList', '白名单列表', NULL, '[\"/api/whitelist/list\"]', 7900, '[]', NULL, NULL, NULL, '2021-04-14 11:39:00', '2021-04-14 11:39:51', 0);
COMMIT;

-- ----------------------------
-- Table structure for t_tro_role
-- ----------------------------
DROP TABLE IF EXISTS `t_tro_role`;
CREATE TABLE `t_tro_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色id主键',
  `application_id` bigint(20) DEFAULT NULL COMMENT '应用id(4.5.1版本后废弃不用)',
  `name` varchar(20) NOT NULL COMMENT '角色名称',
  `alias` varchar(255) DEFAULT NULL COMMENT '角色别名',
  `code` varchar(20) DEFAULT NULL COMMENT '角色编码',
  `description` varchar(255) DEFAULT NULL COMMENT '角色描述',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态(0:启用 1:禁用)',
  `features` longtext COMMENT '扩展字段，k-v形式存在',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否有效 0:有效;1:无效',
  PRIMARY KEY (`id`),
  KEY `idx_application_id` (`application_id`)
) ENGINE=InnoDB AUTO_INCREMENT=775 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- Records of t_tro_role
-- ----------------------------
BEGIN;
INSERT INTO `t_tro_role` VALUES (772, NULL, '本部门角色', NULL, NULL, '勿动', 0, NULL, NULL, NULL, '2020-11-19 09:42:53', '2020-11-19 09:42:53', 0);
INSERT INTO `t_tro_role` VALUES (773, NULL, '全部', NULL, NULL, '全部', 0, NULL, NULL, NULL, '2020-11-25 22:23:42', '2020-11-25 22:23:42', 0);
INSERT INTO `t_tro_role` VALUES (774, NULL, '测试负责人', NULL, NULL, '负责全链路压测', 0, NULL, NULL, NULL, '2020-11-26 10:08:33', '2020-11-26 10:08:33', 0);
COMMIT;

-- ----------------------------
-- Table structure for t_tro_role_user_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_tro_role_user_relation`;
CREATE TABLE `t_tro_role_user_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id主键',
  `role_id` varchar(255) NOT NULL COMMENT '角色id',
  `user_id` varchar(255) NOT NULL COMMENT '用户id',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否有效 0:有效;1:无效',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`(191)),
  KEY `idx_role_id` (`role_id`(191))
) ENGINE=InnoDB AUTO_INCREMENT=482 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_tro_role_user_relation
-- ----------------------------
BEGIN;
INSERT INTO `t_tro_role_user_relation` VALUES (457, '560', '1', NULL, '2020-10-31 01:50:37', '2020-10-31 01:50:37', 0);
INSERT INTO `t_tro_role_user_relation` VALUES (464, '581', '1', NULL, '2020-10-31 02:00:04', '2020-10-31 02:00:04', 0);
INSERT INTO `t_tro_role_user_relation` VALUES (465, '584', '1', NULL, '2020-10-31 02:00:04', '2020-10-31 02:00:04', 0);
INSERT INTO `t_tro_role_user_relation` VALUES (466, '587', '1', NULL, '2020-10-31 02:00:04', '2020-10-31 02:00:04', 0);
INSERT INTO `t_tro_role_user_relation` VALUES (467, '590', '1', NULL, '2020-10-31 02:00:04', '2020-10-31 02:00:04', 0);
INSERT INTO `t_tro_role_user_relation` VALUES (468, '593', '1', NULL, '2020-10-31 02:00:04', '2020-10-31 02:00:04', 0);
INSERT INTO `t_tro_role_user_relation` VALUES (470, '602', '1', NULL, '2020-10-31 02:00:04', '2020-10-31 02:00:04', 0);
INSERT INTO `t_tro_role_user_relation` VALUES (471, '600', '1', NULL, '2020-10-31 02:00:04', '2020-10-31 02:00:04', 0);
INSERT INTO `t_tro_role_user_relation` VALUES (472, '605', '1', NULL, '2020-10-31 02:00:05', '2020-10-31 02:00:05', 0);
INSERT INTO `t_tro_role_user_relation` VALUES (473, '608', '1', NULL, '2020-10-31 02:00:05', '2020-10-31 02:00:05', 0);
INSERT INTO `t_tro_role_user_relation` VALUES (474, '611', '1', NULL, '2020-10-31 02:00:31', '2020-10-31 02:00:31', 0);
INSERT INTO `t_tro_role_user_relation` VALUES (475, '614', '1', NULL, '2020-10-31 02:03:04', '2020-10-31 02:03:04', 0);
INSERT INTO `t_tro_role_user_relation` VALUES (478, '725', '1', NULL, '2020-11-04 13:54:40', '2020-11-04 13:54:40', 0);
INSERT INTO `t_tro_role_user_relation` VALUES (480, '761', '1', NULL, '2020-11-11 14:34:21', '2020-11-11 14:34:21', 0);
INSERT INTO `t_tro_role_user_relation` VALUES (481, '764', '1', NULL, '2020-11-12 20:14:15', '2020-11-12 20:14:15', 0);
COMMIT;

-- ----------------------------
-- Table structure for t_tro_trace_entry
-- ----------------------------
DROP TABLE IF EXISTS `t_tro_trace_entry`;
CREATE TABLE `t_tro_trace_entry` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `app_name` varchar(128) NOT NULL COMMENT '应用',
  `entry` varchar(250) NOT NULL COMMENT '入口',
  `method` varchar(50) DEFAULT NULL COMMENT '方法',
  `status` varchar(20) NOT NULL COMMENT '状态',
  `start_time` bigint(20) NOT NULL COMMENT '开始时间',
  `end_time` bigint(20) NOT NULL COMMENT '结束时间',
  `process_time` bigint(20) NOT NULL COMMENT '耗时',
  `trace_id` varchar(30) NOT NULL COMMENT 'traceId',
  PRIMARY KEY (`id`),
  KEY `start_time` (`start_time`,`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='trace入口列表';

-- ----------------------------
-- Records of t_tro_trace_entry
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_tro_user
-- ----------------------------
DROP TABLE IF EXISTS `t_tro_user`;
CREATE TABLE `t_tro_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `name` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '登录账号',
  `nick` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '用户名称',
  `key` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '用户key',
  `salt` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '盐值',
  `password` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '登录密码',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态 0:启用  1： 冻结',
  `model` tinyint(1) DEFAULT '0' COMMENT '模式 0:体验模式，1:正式模式',
  `role` tinyint(1) DEFAULT '0' COMMENT '角色 0:管理员，1:体验用户 2:正式用户',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0: 正常 1： 删除',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_update` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `user_type` int(1) DEFAULT '0' COMMENT '用户类型，0:系统管理员，1:其他',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_tro_user
-- ----------------------------
BEGIN;
INSERT INTO `t_tro_user` VALUES (1, 1, 'admin', 'admin', '5b06060a-17cb-4588-bb71-edd7f65035af', '$2a$10$wZitQofmRoiDxO1wBcbpq.', '$2a$10$wZitQofmRoiDxO1wBcbpq.9zbO1P24T/80bP/E2dPsZGCIjfsE9ua', 0, 0, 0, 0, '2020-03-25 10:49:35', '2020-11-17 23:14:23', 0);
COMMIT;

-- ----------------------------
-- Table structure for t_tro_user_dept_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_tro_user_dept_relation`;
CREATE TABLE `t_tro_user_dept_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id主键',
  `user_id` varchar(255) NOT NULL COMMENT '用户id',
  `dept_id` varchar(255) NOT NULL COMMENT '部门id',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否有效 0:有效;1:无效',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`(191)),
  KEY `idx_dept_id` (`dept_id`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_tro_user_dept_relation
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_upload_interface_data
-- ----------------------------
DROP TABLE IF EXISTS `t_upload_interface_data`;
CREATE TABLE `t_upload_interface_data` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '抽数表id',
  `APP_NAME` varchar(64) NOT NULL COMMENT 'APP名',
  `INTERFACE_VALUE` varchar(256) NOT NULL COMMENT '接口值',
  `INTERFACE_TYPE` int(1) DEFAULT '1' COMMENT '上传数据类型 查看字典  暂时 1 dubbo 2 job',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '插入时间',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `TAD_INDEX1` (`APP_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='dubbo和job接口上传收集表';

-- ----------------------------
-- Records of t_upload_interface_data
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_warn_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_warn_detail`;
CREATE TABLE `t_warn_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pt_id` bigint(20) NOT NULL COMMENT '压测编号：同压测报告ID',
  `sla_id` bigint(20) NOT NULL COMMENT 'SLA配置ID',
  `sla_name` varchar(30) COLLATE utf8_bin NOT NULL COMMENT 'SLA配置名称',
  `business_activity_id` bigint(20) NOT NULL COMMENT '业务活动ID',
  `business_activity_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '业务活动名称',
  `warn_content` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '告警内容',
  `warn_rule_detail` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `real_value` double(11,3) DEFAULT NULL COMMENT '压测实际值',
  `warn_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '告警时间',
  `create_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_pt_id` (`pt_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_warn_detail
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_white_list
-- ----------------------------
DROP TABLE IF EXISTS `t_white_list`;
CREATE TABLE `t_white_list` (
  `WLIST_ID` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `INTERFACE_NAME` varchar(1000) DEFAULT NULL COMMENT '接口名称',
  `TYPE` varchar(20) DEFAULT NULL COMMENT '白名单类型',
  `DICT_TYPE` varchar(50) DEFAULT NULL COMMENT '字典分类',
  `APPLICATION_ID` bigint(19) DEFAULT NULL,
  `PRINCIPAL_NO` varchar(10) DEFAULT NULL COMMENT '负责人工号',
  `USE_YN` int(1) DEFAULT '1' COMMENT '是否可用(0表示未启动,1表示启动,2表示启用未校验)',
  `CUSTOMER_ID` bigint(20) DEFAULT NULL COMMENT '租户id',
  `USER_ID` bigint(20) DEFAULT NULL COMMENT '用户id',
  `CREATE_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `QUEUE_NAME` varchar(100) DEFAULT NULL COMMENT '队列名称，TYPE=5时该字段才会有值',
  `MQ_TYPE` char(1) DEFAULT NULL COMMENT 'MQ类型, 1ESB 2IBM 3ROCKETMQ 4DPBOOT_ROCKETMQ',
  `IP_PORT` varchar(512) DEFAULT NULL COMMENT 'IP端口,如1.1.1.1:8080,集群时用逗号分隔;当且仅当TYPE=5,MQ_TYPE=(3,4)时才会有值',
  `HTTP_TYPE` int(1) DEFAULT NULL COMMENT 'HTTP类型：1页面 2接口',
  `PAGE_LEVEL` int(1) DEFAULT NULL COMMENT '页面分类：1普通页面加载 2简单查询页面/复杂界面 3复杂查询页面',
  `INTERFACE_LEVEL` int(1) DEFAULT NULL COMMENT '接口类型：1简单操作/查询 2一般操作/查询 3复杂操作 4涉及级联嵌套调用多服务操作 5调用外网操作 ',
  `JOB_INTERVAL` int(1) DEFAULT NULL COMMENT 'JOB调度间隔：1调度间隔≤1分钟 2调度间隔≤5分钟 3调度间隔≤15分钟 4调度间隔≤60分钟',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否已经删除 0:未删除;1:删除',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_global` tinyint(1) DEFAULT '1' COMMENT '是否全局生效',
  `is_handwork` tinyint(1) DEFAULT '0' COMMENT '是否手工添加',
  PRIMARY KEY (`WLIST_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='白名单管理';

-- ----------------------------
-- Records of t_white_list
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_whitelist_effective_app
-- ----------------------------
DROP TABLE IF EXISTS `t_whitelist_effective_app`;
CREATE TABLE `t_whitelist_effective_app` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `wlist_id` bigint(20) NOT NULL COMMENT '白名单id',
  `interface_name` varchar(1024) NOT NULL COMMENT '接口名',
  `type` varchar(20) DEFAULT NULL COMMENT '白名单类型',
  `EFFECTIVE_APP_NAME` varchar(1024) NOT NULL COMMENT '生效应用',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `gmt_create` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `gmt_modified` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '软删',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_whitelist_effective_app
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_whole_report
-- ----------------------------
DROP TABLE IF EXISTS `t_whole_report`;
CREATE TABLE `t_whole_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `pessure_process_id` bigint(20) NOT NULL COMMENT '压测任务业务流程ID',
  `name` varchar(20) NOT NULL COMMENT '压测名称',
  `pessure_result` tinyint(1) DEFAULT '0' COMMENT '0-通过、1-不通过',
  `start_time` datetime DEFAULT NULL COMMENT '压测开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '压测结束时间',
  `request_total` varchar(10) DEFAULT NULL COMMENT '请求总量',
  `target_tps` varchar(10) DEFAULT NULL COMMENT '目标TPS',
  `actual_avg_tps` varchar(10) DEFAULT NULL COMMENT '实际平均TPS',
  `actual_max_tps` varchar(10) DEFAULT NULL COMMENT '实际最大TPS',
  `target_rt` varchar(10) DEFAULT NULL COMMENT '目标RT',
  `actual_avg_rt` varchar(10) DEFAULT NULL COMMENT '实际平均RT',
  `target_request_rate` varchar(10) DEFAULT NULL COMMENT '目标请求成功率',
  `actual_request_rate` varchar(10) DEFAULT NULL COMMENT '实际请求成功率',
  `peak_time` datetime DEFAULT NULL COMMENT '峰值时刻',
  `peak_tps` varchar(10) DEFAULT NULL COMMENT '压测峰值TPS',
  `peak_request_total` varchar(10) DEFAULT NULL COMMENT '压测峰值请求量',
  `peak_success_rate` varchar(10) DEFAULT NULL COMMENT '压测峰值成功率',
  `peak_rt` varchar(10) DEFAULT NULL COMMENT '压测峰值rt',
  `last_report_id` bigint(20) DEFAULT NULL COMMENT '上次压测报告ID',
  `last_avg_tps` varchar(10) DEFAULT NULL COMMENT '上次实际平均TPS',
  `last_max_tps` varchar(10) DEFAULT NULL COMMENT '上次实际最大TPS',
  `last_avg_rt` varchar(10) DEFAULT NULL COMMENT '上次实际平均R',
  `last_request_total` varchar(10) DEFAULT NULL COMMENT '上次实际请求成功率',
  `optimization_strategy` text COMMENT '优化策略',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0-未删除、1-已删除',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='压测报告整体概况';

-- ----------------------------
-- Records of t_whole_report
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
