/*
 Navicat Premium Data Transfer

 Source Server         : 自动化部署web-dev
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : 127.0.0.1:30311
 Source Schema         : trodb_cloud_base

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 01/05/2021 22:23:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
create database IF NOT EXISTS trodb_cloud;
use trodb_cloud;

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
  PRIMARY KEY (`id`) USING BTREE
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
  PRIMARY KEY (`id`) USING BTREE
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
  PRIMARY KEY (`id`) USING BTREE
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
  PRIMARY KEY (`id`) USING BTREE
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
  PRIMARY KEY (`id`) USING BTREE
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
  PRIMARY KEY (`id`) USING BTREE
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
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_biz_key` (`biz_key`) USING BTREE
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
  PRIMARY KEY (`id`) USING BTREE
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
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8 COMMENT='账户表';

-- ----------------------------
-- Records of t_ac_account
-- ----------------------------
BEGIN;
INSERT INTO `t_ac_account` VALUES (100, 1, 0, 0, 0, NULL, '2020-07-16 16:21:37.574', '2020-07-16 16:21:37.574');
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
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8 COMMENT='账户账本表';

-- ----------------------------
-- Records of t_ac_account_book
-- ----------------------------
BEGIN;
INSERT INTO `t_ac_account_book` VALUES (100, 1, 100, 0, 9803488.39673, 46614.26669, -149897.33658, NULL, NULL, NULL, 0.00000, NULL, NULL, NULL, NULL, 0, 0, NULL, '2020-07-16 16:21:39.454', '2021-04-28 02:00:42.119');
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
-- Table structure for t_base_config
-- ----------------------------
DROP TABLE IF EXISTS `t_base_config`;
CREATE TABLE `t_base_config` (
  `CONFIG_CODE` varchar(64) NOT NULL COMMENT '配置编码',
  `CONFIG_VALUE` varchar(128) NOT NULL COMMENT '配置值',
  `CONFIG_DESC` varchar(128) NOT NULL COMMENT '配置说明',
  `USE_YN` int(1) DEFAULT '0' COMMENT '是否可用(0表示未启用,1表示启用)',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '插入时间',
  `UPDATE_TIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`CONFIG_CODE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='tro配置表';

-- ----------------------------
-- Records of t_base_config
-- ----------------------------
BEGIN;
INSERT INTO `t_base_config` VALUES ('SQL_CHECK', '0', '全应用SQL检查开关 1开启 0关闭', 1, '2019-03-28 22:11:18', '2020-08-17 11:53:33');
INSERT INTO `t_base_config` VALUES ('STRATIGY_TEMPLATE', '{\n \"threadNum\":\"最大并发数\",\n \"cpuNum\":\"cpu核数\",\n \"memorySize\":\"内存(MB)\"\n}', '策略配置模板', 0, NULL, '2020-08-17 11:52:53');
COMMIT;

-- ----------------------------
-- Table structure for t_black_list
-- ----------------------------
DROP TABLE IF EXISTS `t_black_list`;
CREATE TABLE `t_black_list` (
  `BLIST_ID` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `REDIS_KEY` varchar(50) DEFAULT NULL COMMENT 'redis的键',
  `PRINCIPAL_NO` varchar(10) DEFAULT NULL COMMENT '负责人工号',
  `USE_YN` int(1) DEFAULT NULL COMMENT '是否可用(0表示未启动,1表示启动,2表示启用未校验)',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '插入时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '变更时间',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据库配置表';

-- ----------------------------
-- Records of t_database_conf
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
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309485c84ae0d645f26de5a', 'f48c856d2aec493a8902da7485720404', 3, '《 15分钟', 'less15', 'ZH_CN', 'Y', '2019-06-17', '2019-06-17', '000000', '000000', NULL, NULL);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309485c84aeo9s45f26de5a', 'f644eb266aba4a2186341b708f33ios0', 1, '开通任务', '1', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309llop8sdso9s45f26de10', 'f644eb266aba4a2186341b708f33mnk1', 1, '开通中', '1', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309llop8sdso9s45f26de12', 'f644eb266aba4a2186341b708f33mnk1', 2, '开通成功', '2', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309llop8sdso9s45f26de13', 'f644eb266aba4a2186341b708f33mnk1', 3, '开通失败', '3', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309llop8sdso9s45f26de14', 'f644eb266aba4a2186341b708f33mnk1', 4, '启动中', '4', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309llop8sdso9s45f26de15', 'f644eb266aba4a2186341b708f33mnk1', 5, '启动成功', '5', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309llop8sdso9s45f26de16', 'f644eb266aba4a2186341b708f33mnk1', 6, '启动失败', '6', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309llop8sdso9s45f26de17', 'f644eb266aba4a2186341b708f33mnk1', 7, '初始化中', '7', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309llop8sdso9s45f26de18', 'f644eb266aba4a2186341b708f33mnk1', 8, '初始化失败', '8', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309llop8sdso9s45f26de19', 'f644eb266aba4a2186341b708f33mnk1', 9, '运行中', '9', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309llop8sdso9s45f26de20', 'f644eb266aba4a2186341b708f33mnk1', 10, '销毁中', '10', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309llop8sdso9s45f26de21', 'f644eb266aba4a2186341b708f33mnk1', 11, '已过期', '11', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309llop8sdso9s45f26de22', 'f644eb266aba4a2186341b708f33mnk1', 12, '已锁定', '12', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309llop8sdso9s45f26de23', 'f644eb266aba4a2186341b708f33mnk1', 13, '销毁失败', '13', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309llop8sdso9s45f26de24', 'f644eb266aba4a2186341b708f33mnk1', 14, '已销毁', '14', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309llop8sdso9s45f26rty1', 'f644eb266aba4a2186341b708f33aai1', 1, '2核8G500G磁盘', '1', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309llop8sdso9s45f26rty2', 'f644eb266aba4a2186341b708f33aai1', 2, '8核32G500G磁盘', '2', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309llop8sdso9s45f26rty3', 'f644eb266aba4a2186341b708f33aai1', 3, '16核64G500G磁盘', '3', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309ppp984aeo9s45f26de5a', 'f644eb266aba4a2186341b708f33ios0', 2, '销毁任务', '2', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309ppp98sdso9s45f26de12', 'f644eb266aba4a2186341b708f33ios7', 2, '开通失败', '2', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309ppp98sdso9s45f26de13', 'f644eb266aba4a2186341b708f33ios7', 3, '开通成功', '3', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309ppp98sdso9s45f26de14', 'f644eb266aba4a2186341b708f33ios7', 4, '销毁中', '4', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309ppp98sdso9s45f26de15', 'f644eb266aba4a2186341b708f33ios7', 5, '销毁失败', '5', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309ppp98sdso9s45f26de16', 'f644eb266aba4a2186341b708f33ios7', 6, '销毁成功', '6', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309ppp98sdso9s45f26de5a', 'f644eb266aba4a2186341b708f33ios7', 1, '开通中', '1', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309tyu98sdso9s45f26de12', 'f644eb266aba4a2186341b708f33wer1', 1, '长期开通', '1', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('0723f0bef309tyu98sdso9s45f26de13', 'f644eb266aba4a2186341b708f33wer1', 2, '短期抢占', '2', 'ZH_CN', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 0);
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
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdb93', 'f644eb266aba4a2186341b708f33eege', 2, 'Gatling', '1', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, NULL);
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
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdc09', 'f644eb266aba4a2186341b708f33eego', 1, '固定压力值', '1', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', NULL, NULL, NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdc10', 'f644eb266aba4a2186341b708f33eego', 2, '线性递增', '2', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', NULL, NULL, NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdc11', 'f644eb266aba4a2186341b708f33eego', 3, '阶梯递增', '3', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', NULL, NULL, NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdc12', 'f644eb266aba4a2186341b708f33eegp', 1, '失败', '0', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', NULL, NULL, NULL, 0);
INSERT INTO `t_dictionary_data` VALUES ('fc77d5f788dc45528b039d5b1b4fdc13', 'f644eb266aba4a2186341b708f33eegp', 2, '成功', '1', 'ZH_CN', 'Y', '2020-03-17', '2020-03-17', NULL, NULL, NULL, 0);
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
INSERT INTO `t_dictionary_type` VALUES ('2e8162023c1b74421cbe0712124822e5725', '链路探活取数来源', 'Y', '2019-07-10', '2019-07-10', '000000', '000000', NULL, 'LIVE_SOURCE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('2e81620c1b74421cbe0712124822e5725', '链路探活取数来源', 'Y', '2019-07-10', '2019-07-10', '000000', '000000', NULL, 'LIVE_SOURCE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('2e81620c1b74421cbe071224822e5725', '链路探活取数来源', 'Y', '2019-07-10', '2019-07-10', '000000', '000000', NULL, 'LIVE_SOURCE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('3ea3284fbca841bcbcda50afa0d8a24b', '单量计算方式', 'Y', '2019-04-04', '2019-04-04', '000000', '000000', NULL, 'VOLUME_CALC_STATUS', NULL);
INSERT INTO `t_dictionary_type` VALUES ('41a309a6d1e04105acfd5fb08200f0c5', 'http类型', 'Y', '2019-06-12', '2019-06-12', '000000', '000000', NULL, 'HTTP_TYPE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('470557d08abe4eb6a794764209c7763d', '页面分类', 'Y', '2019-06-12', '2019-06-12', '000000', '000000', NULL, 'PAGE_LEVEL', NULL);
INSERT INTO `t_dictionary_type` VALUES ('6ba75716d726493783bfd64cce058780', '链路探活时间', 'Y', '2019-07-10', '2019-07-10', '000000', '000000', NULL, 'LINK_LIVE_TIME', NULL);
INSERT INTO `t_dictionary_type` VALUES ('8c3c52c6889c435a8e055d988016e02a', '接口分类', 'Y', '2019-06-12', '2019-06-12', '000000', '000000', NULL, 'INTERFACE_LEVEL', NULL);
INSERT INTO `t_dictionary_type` VALUES ('944da50e5a334e128b34df906971b113', '应用上传信息类型', 'Y', '2019-03-29', '2019-03-29', '000000', '000000', NULL, 'UPLOAD_INFO_TYPE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('ada9370f26ac4c79acbac0ad7acb0992', '数据库类型', 'Y', '2018-08-30', '2018-08-30', NULL, NULL, NULL, 'DBTYPE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('b8f4ce1a989b4f19a842e08975ee3eb1', '链路类型', 'Y', '2018-12-24', '2018-12-24', '578992', NULL, NULL, 'LINK_TYPE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('ca888ed801664c81815d8c4f5b8dff0c', '白名单', 'Y', '2018-07-05', '2018-07-05', '', '', '', 'WLIST', '');
INSERT INTO `t_dictionary_type` VALUES ('d69be19362c4461fb7e84dfbc21f1747', '链路等级', 'Y', '2018-07-05', '2018-07-05', '', '', '', 'LINKRANK', '');
INSERT INTO `t_dictionary_type` VALUES ('ec18bced105c41018cfbcaf6b3327b9a', 'MQ消息类型', 'Y', '2018-07-30', '2018-07-30', '557092', NULL, NULL, 'MQMSG', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f48c856d2aec493a8902da7485720404', 'JOB调度间隔', 'Y', '2019-06-17', '2019-06-17', '000000', '000000', NULL, 'JOB_INTERVAL', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33aai1', '规格类型', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 'SPEC_TYPE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33eece', '链路所属模块', 'Y', '2019-04-04', '2019-04-04', '000000', '000000', NULL, 'LINK_MODULE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33eegb', '云服务器', 'Y', '2020-03-12', '2020-03-12', '000000', '000000', NULL, 'CLOUD_SERVER', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33eegc', '场景状态', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, 'SCENE_MANAGE_STATUS', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33eegd', '是否删除', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, 'IS_DELETED', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33eege', '脚本类型', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, 'SCRIPT_TYPE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33eegf', '文件类型', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, 'FILE_TYPE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33eegg', 'SLA指标类型', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, 'SLA_TARGER_TYPE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33eegh', '数值比较', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, 'COMPARE_TYPE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33eegk', '存活状态', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, 'LIVE_STATUS', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33eego', '施压模式', 'Y', '2020-03-17', '2020-03-17', '000000', '000000', NULL, 'PT_MODEL', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33eegp', '成功状态', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 'SUCCESS_STATUS', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33ios0', '机器任务类型', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 'MACHINE_TASK_TYPE', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33ios7', '机器任务状态', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 'MACHINE_TASK_STATUS', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33mnk1', '机器状态', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 'MACHINE_STATUS', NULL);
INSERT INTO `t_dictionary_type` VALUES ('f644eb266aba4a2186341b708f33wer1', '开通模式', 'Y', '2020-05-14', '2020-05-14', '000000', '000000', NULL, 'OPEN_TYPE', NULL);
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
COMMIT;

-- ----------------------------
-- Table structure for t_engine_plugin_files_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_engine_plugin_files_ref`;
CREATE TABLE `t_engine_plugin_files_ref` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `plugin_id` bigint(20) NOT NULL COMMENT '插件ID',
  `file_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '文件名称',
  `file_path` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '文件路径',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='引擎插件文件信息';

-- ----------------------------
-- Records of t_engine_plugin_files_ref
-- ----------------------------
BEGIN;
INSERT INTO `t_engine_plugin_files_ref` VALUES (1, 1, 'jmeter-plugins-dubbo-all.jar', '/engine/plugins/dubbo/dubbo-all/jmeter-plugins-dubbo-all.jar', '2021-01-29 13:43:59');
INSERT INTO `t_engine_plugin_files_ref` VALUES (2, 2, 'jmeter-plugins-kafka-all.jar', '/engine/plugins/kafka/kafka-all/jmeter-plugins-kafka-all.jar', '2021-01-29 13:44:48');
COMMIT;

-- ----------------------------
-- Table structure for t_engine_plugin_info
-- ----------------------------
DROP TABLE IF EXISTS `t_engine_plugin_info`;
CREATE TABLE `t_engine_plugin_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `plugin_type` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '插件类型',
  `plugin_name` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '插件名称',
  `gmt_create` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `gmt_update` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态  1 启用， 0 禁用',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `index_pressure_engine_plugin_config_name` (`plugin_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='压力引擎插件配置信息';

-- ----------------------------
-- Records of t_engine_plugin_info
-- ----------------------------
BEGIN;
INSERT INTO `t_engine_plugin_info` VALUES (1, 'dubbo', 'dubbo-all', '2021-01-06 14:43:58', '2021-01-06 14:43:58', 1);
INSERT INTO `t_engine_plugin_info` VALUES (2, 'kafka', 'kafka-all', '2021-01-06 14:44:33', '2021-01-06 14:44:33', 1);
COMMIT;

-- ----------------------------
-- Table structure for t_engine_plugin_supported_versions
-- ----------------------------
DROP TABLE IF EXISTS `t_engine_plugin_supported_versions`;
CREATE TABLE `t_engine_plugin_supported_versions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `plugin_id` bigint(20) DEFAULT NULL COMMENT '插件id',
  `supported_version` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '支持的版本',
  PRIMARY KEY (`id`),
  KEY `index_pressure_engine_support_version_config_id` (`plugin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='压力引擎插件支持的版本信息';

-- ----------------------------
-- Records of t_engine_plugin_supported_versions
-- ----------------------------
BEGIN;
INSERT INTO `t_engine_plugin_supported_versions` VALUES (1, 1, 'all');
INSERT INTO `t_engine_plugin_supported_versions` VALUES (2, 2, 'all');
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='指标采集配置';

-- ----------------------------
-- Records of t_indicators_collect_config
-- ----------------------------
BEGIN;
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
  `IS_DELETED` tinyint(4) DEFAULT '0' COMMENT '是否有效 0:有效;1:无效',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `APPLICATION_NAME` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '应用名',
  `CHANGE_TYPE` tinyint(4) DEFAULT NULL COMMENT '变更类型: 1:无流量调用通知;2:添加调用关系通知',
  `CAN_DELETE` tinyint(4) DEFAULT '0' COMMENT '是否可以删除 0:可以删除;1:不可以删除',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='中间件信息表';

-- ----------------------------
-- Records of t_middleware_info
-- ----------------------------
BEGIN;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网络隔离配置表';

-- ----------------------------
-- Records of t_network_isolate_config
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
  `task_id` bigint(20) NOT NULL COMMENT '任务id',
  `public_ip` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '压力机公网IP',
  `private_ip` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '压力机内网IP',
  `instance_id` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '实例ID',
  `instance_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '实例名称',
  `region_id` varchar(25) COLLATE utf8_bin DEFAULT NULL COMMENT '区域ID',
  `region_name` varchar(25) COLLATE utf8_bin DEFAULT NULL COMMENT '区域ID',
  `platform_id` bigint(20) NOT NULL COMMENT '云平台id',
  `platform_name` varchar(125) COLLATE utf8_bin DEFAULT NULL COMMENT '云平台名称',
  `account_id` bigint(20) NOT NULL COMMENT '账号id',
  `account_name` varchar(125) COLLATE utf8_bin DEFAULT NULL COMMENT '账号名称',
  `spec_id` bigint(20) DEFAULT NULL COMMENT '规格id',
  `spec` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '规格描述',
  `ref_spec` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '第三方规格描述',
  `open_type` int(2) NOT NULL COMMENT '开通类型：1、长期开通 2、短期抢占',
  `open_time` int(2) NOT NULL COMMENT '开通时长：长期开通单位为月，短期抢占单位为小时',
  `expire_date` datetime DEFAULT NULL COMMENT '过期时间',
  `status` int(2) NOT NULL COMMENT '状态 1、开通中 2、开通成功 3、开通失败 4：启动中 5、启动成功 6、启动失败 7、初始化中 8、初始化失败 9、运行中 10、销毁中 11、已过期 12、已锁定 13、销毁失败 14、已销毁',
  `feature` text COLLATE utf8_bin COMMENT '拓展属性',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0: 正常 1： 删除',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_update` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_pressure_machine
-- ----------------------------
BEGIN;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='压测引擎配置';

-- ----------------------------
-- Records of t_pressure_test_engine_config
-- ----------------------------
BEGIN;
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
-- Table structure for t_report
-- ----------------------------
DROP TABLE IF EXISTS `t_report`;
CREATE TABLE `t_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '负责人id',
  `custom_id` bigint(20) DEFAULT NULL COMMENT '客户id',
  `amount` decimal(15,2) DEFAULT '0.00' COMMENT '流量消耗',
  `scene_id` bigint(20) NOT NULL COMMENT '场景ID',
  `scene_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '场景名称',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `status` int(2) DEFAULT '0' COMMENT '报表生成状态:0/就绪状态，1/生成中,2/完成生成',
  `type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '报告类型；0普通场景，1流量调试',
  `conclusion` int(1) DEFAULT '0' COMMENT '压测结论: 0/不通过，1/通过',
  `total_request` bigint(10) DEFAULT NULL COMMENT '请求总数',
  `pressure_type` int(2) DEFAULT '0' COMMENT '施压类型,0:并发,1:tps,2:自定义;不填默认为0',
  `avg_concurrent` decimal(10,2) DEFAULT NULL COMMENT '平均线程数',
  `tps` int(11) DEFAULT NULL COMMENT '目标tps',
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
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门id',
  `create_uid` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `lock` int(11) DEFAULT '1' COMMENT '1-解锁 9-锁定',
  `script_id` bigint(20) DEFAULT NULL COMMENT '脚本id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_script_id` (`script_id`)
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
-- Table structure for t_report_business_activity_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_report_business_activity_detail`;
CREATE TABLE `t_report_business_activity_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_id` bigint(20) NOT NULL COMMENT '报告ID',
  `scene_id` bigint(20) NOT NULL COMMENT '场景ID',
  `business_activity_id` bigint(20) NOT NULL COMMENT '业务活动ID',
  `business_activity_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '业务活动名称',
  `bind_ref` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '绑定关系',
  `application_ids` varchar(1024) COLLATE utf8_bin DEFAULT NULL COMMENT '应用ID',
  `request` bigint(10) DEFAULT NULL COMMENT '请求数',
  `sa` decimal(10,2) DEFAULT NULL COMMENT 'sa',
  `target_sa` decimal(10,2) DEFAULT NULL COMMENT '目标sa',
  `tps` decimal(10,2) DEFAULT NULL COMMENT 'tps',
  `target_tps` decimal(10,2) DEFAULT NULL COMMENT '目标tps',
  `avg_concurrence_num` decimal(10,2) DEFAULT NULL COMMENT '平均并发数',
  `rt` decimal(10,2) DEFAULT NULL COMMENT '响应时间',
  `target_rt` decimal(10,2) DEFAULT NULL COMMENT '目标rt',
  `rt_distribute` varchar(512) COLLATE utf8_bin DEFAULT NULL COMMENT '分布范围，格式json',
  `success_rate` decimal(10,2) DEFAULT NULL COMMENT '成功率',
  `target_success_rate` decimal(10,2) DEFAULT NULL COMMENT '目标成功率',
  `max_tps` decimal(10,2) DEFAULT NULL COMMENT '最大tps',
  `max_rt` decimal(10,2) DEFAULT NULL COMMENT '最大响应时间',
  `min_rt` decimal(10,2) DEFAULT NULL COMMENT '最小响应时间',
  `pass_flag` tinyint(4) DEFAULT NULL COMMENT '是否通过',
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
  `user_id` bigint(20) DEFAULT NULL COMMENT '负责人id',
  `custom_id` bigint(20) DEFAULT NULL COMMENT '客户id',
  `scene_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '场景名称',
  `status` tinyint(4) NOT NULL COMMENT '参考数据字典 场景状态',
  `last_pt_time` timestamp NULL DEFAULT NULL COMMENT '最新压测时间',
  `pt_config` text COLLATE utf8_bin COMMENT '施压配置',
  `type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '场景类型:0普通场景，1流量调试',
  `script_type` tinyint(4) DEFAULT NULL COMMENT '脚本类型：0-Jmeter 1-Gatling',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除：0-否 1-是',
  `create_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `create_name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `update_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '最后修改时间',
  `update_name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '最后修改人',
  `features` text COLLATE utf8_bin COMMENT '扩展字段',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门id',
  `create_uid` bigint(20) DEFAULT NULL COMMENT '创建人id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `ide_last_pt_time` (`last_pt_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_scene_manage
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
  `file_size` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '文件大小：2MB',
  `file_type` tinyint(4) NOT NULL COMMENT '文件类型：0-脚本文件 1-数据文件',
  `file_extend` text COLLATE utf8_bin COMMENT '{\n  “dataCount”:”数据量”,\n  “isSplit”:”是否拆分”,\n  “topic”:”MQ主题”,\n  “nameServer”:”mq nameServer”,\n}',
  `upload_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '上传时间',
  `upload_path` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '上传路径：相对路径',
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
-- Table structure for t_schedule_record_engine_plugins_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_schedule_record_engine_plugins_ref`;
CREATE TABLE `t_schedule_record_engine_plugins_ref` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `schedule_record_id` bigint(20) NOT NULL COMMENT '调度记录ID',
  `engine_plugin_file_path` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '引擎插件存放文件夹',
  `gmt_create` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='调度记录引擎插件信息';

-- ----------------------------
-- Records of t_schedule_record_engine_plugins_ref
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_strategy_config
-- ----------------------------
BEGIN;
INSERT INTO `t_strategy_config` VALUES (1, 'test', '{\n \"threadNum\":\"80\",\n \"cpuNum\":\"2\",\n \"memorySize\":\"3072\",\n \"tpsNum\":\"5000\"\n}', 0, 0, '2020-07-20 10:37:35.299', '2021-02-04 22:59:44.846');
INSERT INTO `t_strategy_config` VALUES (2, '私有化版本策略', '{\n \"threadNum\":\"4000\",\n \"cpuNum\":\"2\",\n \"memorySize\":\"3072\",\n \"tpsNum\":\"8000\",\n\"deploymentMethod\":0\n}', 0, 0, '2021-04-25 08:00:52.623', '2021-04-25 08:19:42.075');
COMMIT;

-- ----------------------------
-- Table structure for t_tro_user
-- ----------------------------
DROP TABLE IF EXISTS `t_tro_user`;
CREATE TABLE `t_tro_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '登录账号',
  `nick` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '用户名称',
  `key` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '用户key',
  `salt` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '盐值',
  `password` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '登录密码',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态 0:启用  1： 冻结',
  `model` tinyint(1) DEFAULT '0' COMMENT '模式 0:体验模式，1:正式模式',
  `role` tinyint(1) DEFAULT '0' COMMENT '角色 0:管理员，1:体验用户 2:正式用户',
  `version` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '客户端版本号 4.2.1',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0: 正常 1： 删除',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_update` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_unique_key` (`key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9765 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_tro_user
-- ----------------------------
BEGIN;
INSERT INTO `t_tro_user` VALUES (1, 'shulie-show', 'shulie-shou', '5b06060a-17cb-4588-bb71-edd7f65035af', '$2a$10$QhtZYezGhYT9kDF72SSWNO', '$2a$10$QhtZYezGhYT9kDF72SSWNO2S/7yCGay4VQT6O.lSGyvAPFAUGDadC', 0, 0, 2, '4.2.3', 0, '2020-03-25 10:49:35', '2020-10-10 15:29:13');
INSERT INTO `t_tro_user` VALUES (2, 'admin', 'admin', '5b06060a-17cb-4588-bb71-edd7f65035aa111', '$2a$10$QhtZYezGhYT9kDF72SSWNO', '$2a$10$QhtZYezGhYT9kDF72SSWNO2S/7yCGay4VQT6O.lSGyvAPFAUGDadC', 0, 0, 0, NULL, 0, '2020-03-25 10:49:35', '2020-09-05 12:11:01');
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
) ENGINE=InnoDB AUTO_INCREMENT=27187 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '插入时间',
  `UPDATE_TIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '变更时间',
  `QUEUE_NAME` varchar(100) DEFAULT NULL COMMENT '队列名称，TYPE=5时该字段才会有值',
  `MQ_TYPE` char(1) DEFAULT NULL COMMENT 'MQ类型, 1ESB 2IBM 3ROCKETMQ 4DPBOOT_ROCKETMQ',
  `IP_PORT` varchar(512) DEFAULT NULL COMMENT 'IP端口,如1.1.1.1:8080,集群时用逗号分隔;当且仅当TYPE=5,MQ_TYPE=(3,4)时才会有值',
  `HTTP_TYPE` int(1) DEFAULT NULL COMMENT 'HTTP类型：1页面 2接口',
  `PAGE_LEVEL` int(1) DEFAULT NULL COMMENT '页面分类：1普通页面加载 2简单查询页面/复杂界面 3复杂查询页面',
  `INTERFACE_LEVEL` int(1) DEFAULT NULL COMMENT '接口类型：1简单操作/查询 2一般操作/查询 3复杂操作 4涉及级联嵌套调用多服务操作 5调用外网操作 ',
  `JOB_INTERVAL` int(1) DEFAULT NULL COMMENT 'JOB调度间隔：1调度间隔≤1分钟 2调度间隔≤5分钟 3调度间隔≤15分钟 4调度间隔≤60分钟',
  PRIMARY KEY (`WLIST_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='白名单管理';

-- ----------------------------
-- Records of t_white_list
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

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) NOT NULL,
  `PASSWORD` varchar(64) NOT NULL,
  `user_group` varchar(30) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `WW` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
