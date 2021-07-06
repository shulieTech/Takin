SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_amdb_app
-- ----------------------------
DROP TABLE IF EXISTS `t_amdb_app`;
CREATE TABLE `t_amdb_app`
(
    `id`                   bigint(20) NOT NULL AUTO_INCREMENT COMMENT '应用ID',
    `app_name`             varchar(512) NOT NULL COMMENT '应用名称',
    `app_manager`          varchar(64)           DEFAULT NULL COMMENT '应用负责人',
    `project_name`         varchar(256)          DEFAULT NULL COMMENT '工程名称',
    `project_version`      varchar(64)           DEFAULT NULL COMMENT '工程版本',
    `git_url`              varchar(256)          DEFAULT NULL COMMENT 'git地址',
    `publish_package_name` varchar(256)          DEFAULT NULL COMMENT '发布包名称',
    `project_submoudle`    varchar(256)          DEFAULT NULL COMMENT '工程子模块',
    `exception_info`       varchar(2000)         DEFAULT NULL COMMENT '异常信息',
    `remark`               varchar(1000)         DEFAULT NULL COMMENT '应用说明',
    `ext`                  longtext COMMENT '扩展字段',
    `flag`                 int(32) DEFAULT NULL COMMENT '标记位',
    `creator`              varchar(64)           DEFAULT NULL COMMENT '创建人编码',
    `creator_name`         varchar(64)           DEFAULT NULL COMMENT '创建人名称',
    `modifier`             varchar(64)           DEFAULT NULL COMMENT '更新人编码',
    `modifier_name`        varchar(64)           DEFAULT NULL COMMENT '更新人名称',
    `gmt_create`           timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modify`           timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `app_manager_name`     varchar(64)           DEFAULT NULL COMMENT '应用负责人名称',
    `tenant`               varchar(64)           DEFAULT NULL COMMENT '租户标示',
    `app_type`             varchar(32)  NOT NULL DEFAULT 'APP' COMMENT '应用类型',
    `app_type_name`        varchar(32)  NOT NULL DEFAULT '应用' COMMENT '应用类型名称',
    PRIMARY KEY (`id`),
    UNIQUE KEY `app_name_unique` (`app_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_amdb_app_instance
-- ----------------------------
DROP TABLE IF EXISTS `t_amdb_app_instance`;
CREATE TABLE `t_amdb_app_instance`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT COMMENT '实例id',
    `app_name`       varchar(512) COLLATE utf8mb4_bin NOT NULL COMMENT '应用名',
    `app_id`         bigint(20) DEFAULT NULL COMMENT '应用ID',
    `agent_id`       varchar(64) COLLATE utf8mb4_bin           DEFAULT NULL COMMENT 'agentId',
    `ip`             varchar(64) COLLATE utf8mb4_bin  NOT NULL COMMENT 'ip',
    `pid`            varchar(32) COLLATE utf8mb4_bin           DEFAULT NULL COMMENT '进程号',
    `agent_version`  varchar(32) COLLATE utf8mb4_bin           DEFAULT NULL COMMENT 'Agent 版本号',
    `md5`            varchar(128) COLLATE utf8mb4_bin          DEFAULT NULL COMMENT 'MD5',
    `ext`            longtext COLLATE utf8mb4_bin COMMENT '扩展字段',
    `flag`           int(32) DEFAULT NULL COMMENT '标记位',
    `creator`        varchar(64) COLLATE utf8mb4_bin           DEFAULT NULL COMMENT '创建人编码',
    `creator_name`   varchar(64) COLLATE utf8mb4_bin           DEFAULT NULL COMMENT '创建人名称',
    `modifier`       varchar(64) COLLATE utf8mb4_bin           DEFAULT NULL COMMENT '更新人编码',
    `modifier_name`  varchar(64) COLLATE utf8mb4_bin           DEFAULT NULL COMMENT '更新人名称',
    `gmt_create`     timestamp                        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modify`     timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `agent_language` varchar(255) COLLATE utf8mb4_bin          DEFAULT NULL,
    `hostname`       varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '主机名称',
    `tenant`         varchar(64) COLLATE utf8mb4_bin  NOT NULL DEFAULT '' COMMENT '租户标示',
    PRIMARY KEY (`id`),
    KEY              `appName_index` (`app_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_amdb_app_instance_snapshot
-- ----------------------------
DROP TABLE IF EXISTS `t_amdb_app_instance_snapshot`;
CREATE TABLE `t_amdb_app_instance_snapshot`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT COMMENT '实例id',
    `app_name`       varchar(512) COLLATE utf8mb4_bin NOT NULL COMMENT '应用名',
    `app_id`         bigint(20) DEFAULT NULL COMMENT '应用ID',
    `agent_id`       varchar(64) COLLATE utf8mb4_bin           DEFAULT NULL COMMENT 'agentId',
    `ip`             varchar(64) COLLATE utf8mb4_bin  NOT NULL COMMENT 'ip',
    `pid`            varchar(32) COLLATE utf8mb4_bin           DEFAULT NULL COMMENT '进程号',
    `agent_version`  varchar(32) COLLATE utf8mb4_bin           DEFAULT NULL COMMENT 'Agent 版本号',
    `md5`            varchar(128) COLLATE utf8mb4_bin          DEFAULT NULL COMMENT 'MD5',
    `ext`            longtext COLLATE utf8mb4_bin COMMENT '扩展字段',
    `flag`           int(32) DEFAULT NULL COMMENT '标记位',
    `creator`        varchar(64) COLLATE utf8mb4_bin           DEFAULT NULL COMMENT '创建人编码',
    `creator_name`   varchar(64) COLLATE utf8mb4_bin           DEFAULT NULL COMMENT '创建人名称',
    `modifier`       varchar(64) COLLATE utf8mb4_bin           DEFAULT NULL COMMENT '更新人编码',
    `modifier_name`  varchar(64) COLLATE utf8mb4_bin           DEFAULT NULL COMMENT '更新人名称',
    `gmt_create`     timestamp                        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modify`     timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `agent_language` varchar(255) COLLATE utf8mb4_bin          DEFAULT NULL,
    `snapshot_date`  datetime                         NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_amdb_app_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_amdb_app_relation`;
CREATE TABLE `t_amdb_app_relation`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT '实例id',
    `from_app_name` varchar(256) COLLATE utf8mb4_bin NOT NULL COMMENT 'From应用',
    `to_app_name`   varchar(256) COLLATE utf8mb4_bin NOT NULL COMMENT 'To应用',
    `creator`       varchar(64) COLLATE utf8mb4_bin           DEFAULT NULL COMMENT '创建人编码',
    `creator_name`  varchar(64) COLLATE utf8mb4_bin           DEFAULT NULL COMMENT '创建人名称',
    `tenant`        varchar(64) COLLATE utf8mb4_bin  NOT NULL DEFAULT '' COMMENT '租户标示',
    `gmt_create`    timestamp                        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modify`    timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `unq_from_to_app_name` (`from_app_name`,`to_app_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_amdb_app_server
-- ----------------------------
DROP TABLE IF EXISTS `t_amdb_app_server`;
CREATE TABLE `t_amdb_app_server`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT '服务ID',
    `app_name`      varchar(512) NOT NULL COMMENT '应用名称',
    `server_name`   varchar(255) NOT NULL COMMENT '服务名',
    `server_type`   varchar(32)  NOT NULL COMMENT '服务类型',
    `ext`           longtext COMMENT '扩展字段',
    `flag`          int(32) DEFAULT NULL COMMENT '标记位',
    `creator`       varchar(64)           DEFAULT NULL COMMENT '创建人编码',
    `creator_name`  varchar(64)           DEFAULT NULL COMMENT '创建人名称',
    `modifier`      varchar(64)           DEFAULT NULL COMMENT '更新人编码',
    `modifier_name` varchar(64)           DEFAULT NULL COMMENT '更新人名称',
    `gmt_create`    timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modify`    timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `appName_serverName_index` (`app_name`,`server_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_amdb_app_server_metrics
-- ----------------------------
DROP TABLE IF EXISTS `t_amdb_app_server_metrics`;
CREATE TABLE `t_amdb_app_server_metrics`
(
    `id`          bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `app_name`    varchar(512) COLLATE utf8mb4_bin NOT NULL COMMENT '应用名称',
    `server_name` varchar(512) COLLATE utf8mb4_bin NOT NULL COMMENT '服务名',
    `server_type` varchar(16) COLLATE utf8mb4_bin  NOT NULL COMMENT '服务类型',
    `rt`          double(10, 2
) DEFAULT NULL COMMENT 'rt',
  `qps` double(10,2) DEFAULT NULL COMMENT 'qps',
  `success_rate` double(10,2) DEFAULT NULL COMMENT '成功率',
  `p_type` varchar(8) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'P90/P95/P99',
  `sampling_interval` int(8) DEFAULT NULL COMMENT '采样时间间隔',
  `statistics_start` datetime(3) DEFAULT NULL COMMENT '统计开始时间',
  `statistics_end` datetime(3) DEFAULT NULL COMMENT '统计结束时间',
  `flag` int(20) DEFAULT NULL COMMENT '标记位',
  `creator` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人编码',
  `creator_name` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人名称',
  `modifier` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '更新人编码',
  `modifier_name` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '更新人名称',
  `gmt_create` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `gmt_modify` datetime(3) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_amdb_app_server_metrics_reports
-- ----------------------------
DROP TABLE IF EXISTS `t_amdb_app_server_metrics_reports`;
CREATE TABLE `t_amdb_app_server_metrics_reports`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `app_name`     varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '应用名称',
    `src_app_name` varchar(256) COLLATE utf8mb4_bin          DEFAULT '' COMMENT '调用来源',
    `call_type`    varchar(8) COLLATE utf8mb4_bin   NOT NULL DEFAULT '' COMMENT '调用类型',
    `call_event`   varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '调用目标',
    `average_rt`   float(10, 4
) DEFAULT '0.0000' COMMENT '平均RT',
  `min_rt` float(10,4) DEFAULT '0.0000' COMMENT '最小RT',
  `max_rt` float(10,4) DEFAULT '0.0000' COMMENT '最大RT',
  `p90_rt` float(10,4) DEFAULT '0.0000' COMMENT 'P90RT',
  `p95_rt` float(10,4) DEFAULT '0.0000' COMMENT 'P95RT',
  `p99_rt` float(10,4) DEFAULT '0.0000' COMMENT 'P99RT',
  `qps` float(10,4) DEFAULT '0.0000' COMMENT 'QPS',
  `success_rate` float(10,4) DEFAULT '0.0000' COMMENT '成功率',
  `sampling_interval` int(4) DEFAULT '0' COMMENT '记录采样间隔',
  `ext` longtext COLLATE utf8mb4_bin COMMENT '扩展字段',
  `statistical_start` datetime(3) NOT NULL COMMENT '统计开始时间',
  `statistical_end` datetime(3) NOT NULL COMMENT '统计结束时间',
  `flag` int(20) DEFAULT NULL COMMENT '标记位',
  `timestamp` datetime(3) DEFAULT NULL COMMENT '记录时间',
  PRIMARY KEY (`id`),
  KEY `appName_index` (`app_name`),
  KEY `appName_callType_callEvent_index` (`app_name`,`call_type`,`call_event`(191)),
  KEY `appName_callType_callEvent_time_index` (`app_name`,`call_type`,`call_event`(191),`statistical_start`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_amdb_config
-- ----------------------------
DROP TABLE IF EXISTS `t_amdb_config`;
CREATE TABLE `t_amdb_config`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `atr_name`      varchar(64)  NOT NULL COMMENT '属性名',
    `atr_value`     varchar(512) NOT NULL COMMENT '属性值',
    `tenant`        varchar(64)  NOT NULL COMMENT '租户标识',
    `creator`       varchar(64) DEFAULT NULL COMMENT '创建人编码',
    `creator_name`  varchar(64) DEFAULT NULL COMMENT '创建人名称',
    `modifier`      varchar(64) DEFAULT NULL COMMENT '更新人编码',
    `modifier_name` varchar(64) DEFAULT NULL COMMENT '更新人名称',
    `gmt_create`    datetime(3) NOT NULL COMMENT '创建时间',
    `gmt_modify`    datetime(3) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_atrName` (`tenant`,`atr_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_amdb_link
-- ----------------------------
DROP TABLE IF EXISTS `t_amdb_link`;
CREATE TABLE `t_amdb_link`
(
    `id`            bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `link_name`     varchar(128) NOT NULL COMMENT '链路名称',
    `entrance`      varchar(128)  DEFAULT NULL COMMENT '应用入口',
    `entrance_type` varchar(64)   DEFAULT NULL COMMENT 'http/rocketmq/rabbitmq/kafka',
    `type`          varchar(32)  NOT NULL COMMENT '自定义链路/其他',
    `ext_info`      varchar(1024) DEFAULT NULL COMMENT '扩展字段，json存储',
    `remark`        varchar(1024) DEFAULT NULL COMMENT '描述信息',
    `creator`       varchar(64)   DEFAULT NULL COMMENT '创建人工号',
    `creator_name`  varchar(128)  DEFAULT NULL COMMENT '创建人姓名',
    `modifier`      varchar(64)   DEFAULT NULL COMMENT '修改人工号',
    `modifier_name` varchar(128)  DEFAULT NULL COMMENT '修改人姓名',
    `gmt_create`    timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `gmt_modify`    timestamp NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `unq_link_name` (`link_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_amdb_link_node
-- ----------------------------
DROP TABLE IF EXISTS `t_amdb_link_node`;
CREATE TABLE `t_amdb_link_node`
(
    `id`            bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `node_id`       varchar(128) NOT NULL COMMENT '节点ID(链路ID+应用+类型+入口)',
    `link_id`       bigint(20) NOT NULL COMMENT '链路主键',
    `app_name`      varchar(512) NOT NULL COMMENT '应用名称',
    `node_name`     varchar(128) NOT NULL COMMENT '节点名称',
    `parent`        tinyint(1) NOT NULL COMMENT '是否为根节点(0是/1否)',
    `entrance`      varchar(128)  DEFAULT NULL COMMENT '应用入口',
    `entrance_type` varchar(64)   DEFAULT NULL COMMENT '入口类型(http/rocketmq/rabbitmq/kafka)',
    `node_level`    int(8) NOT NULL DEFAULT '1' COMMENT '节点所属层级',
    `ext_info`      varchar(1024) DEFAULT NULL COMMENT '扩展字段，json存储',
    `creator`       varchar(64)   DEFAULT NULL COMMENT '创建人工号',
    `creator_name`  varchar(128)  DEFAULT NULL COMMENT '创建人姓名',
    `modifier`      varchar(64)   DEFAULT NULL COMMENT '修改人工号',
    `modifier_name` varchar(128)  DEFAULT NULL COMMENT '修改人姓名',
    `gmt_create`    timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `gmt_modify`    timestamp NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_node_id` (`node_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_amdb_link_node_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_amdb_link_node_relation`;
CREATE TABLE `t_amdb_link_node_relation`
(
    `id`              bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `source_id`       varchar(128) CHARACTER SET utf8 NOT NULL COMMENT '来源节点ID',
    `source_app_name` varchar(128) CHARACTER SET utf8  DEFAULT NULL COMMENT '来源应用名',
    `target_id`       varchar(128) CHARACTER SET utf8 NOT NULL COMMENT '目标节点ID',
    `target_app_name` varchar(128) CHARACTER SET utf8  DEFAULT NULL COMMENT '目标应用名',
    `link_id`         bigint(20) DEFAULT NULL COMMENT '关联链路ID',
    `order_num`       varchar(32) CHARACTER SET utf8   DEFAULT '0' COMMENT '顺序',
    `ext_info`        varchar(1024) CHARACTER SET utf8 DEFAULT NULL COMMENT '扩展字段，json存储',
    `creator`         varchar(64) CHARACTER SET utf8   DEFAULT NULL COMMENT '创建人工号',
    `creator_name`    varchar(128) CHARACTER SET utf8  DEFAULT NULL COMMENT '创建人姓名',
    `modifier`        varchar(64) CHARACTER SET utf8   DEFAULT NULL COMMENT '修改人工号',
    `modifier_name`   varchar(128) CHARACTER SET utf8  DEFAULT NULL COMMENT '修改人姓名',
    `gmt_create`      timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `gmt_modify`      timestamp NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_ source_target_id` (`source_id`,`target_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_amdb_machine_metrics_reports
-- ----------------------------
DROP TABLE IF EXISTS `t_amdb_machine_metrics_reports`;
CREATE TABLE `t_amdb_machine_metrics_reports`
(
    `id`         bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `ip_address` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT 'IP地址',
    `cpu_us`     float(10, 4
) DEFAULT NULL COMMENT 'cpu使用率',
  `io_us` float(10,4) DEFAULT '0.0000' COMMENT 'IO使用率',
  `mem_us` float(10,4) DEFAULT NULL COMMENT '内存占用率',
  `io_rt` float(10,4) DEFAULT NULL COMMENT 'IO响应时间',
  `timestamp` datetime(3) NOT NULL COMMENT '记录时间',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modify` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ip_time` (`ip_address`,`timestamp`),
  KEY `ip_index` (`ip_address`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_amdb_mapper_sql_info
-- ----------------------------
DROP TABLE IF EXISTS `t_amdb_mapper_sql_info`;
CREATE TABLE `t_amdb_mapper_sql_info`
(
    `id`                   bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `sql_id`               varchar(512) NOT NULL COMMENT 'sqlID',
    `sql`                  longtext     NOT NULL COMMENT '完整sql',
    `mapper_path`          varchar(512) NOT NULL COMMENT 'mapper路径',
    `belongs_app`          varchar(64)  NOT NULL COMMENT '所属应用',
    `env`                  varchar(64)  NOT NULL COMMENT '环境',
    `publish_package_name` varchar(64)  NOT NULL COMMENT '应用版本',
    `report_time`          datetime(3) NOT NULL COMMENT '上报时间',
    `scan_time`            datetime(3) NOT NULL COMMENT '扫描时间',
    `ext`                  longtext COMMENT '扩展字段',
    `flag`                 int(32) DEFAULT NULL COMMENT '标记位',
    `creator`              varchar(64)  DEFAULT NULL COMMENT '创建人编码',
    `creator_name`         varchar(64)  DEFAULT NULL COMMENT '创建人名称',
    `modifier`             varchar(64)  DEFAULT NULL COMMENT '更新人编码',
    `modifier_name`        varchar(64)  DEFAULT NULL COMMENT '更新人名称',
    `gmt_create`           datetime(3) NOT NULL COMMENT '创建时间',
    `gmt_modify`           datetime(3) NOT NULL COMMENT '更新时间',
    `branch`               varchar(128) DEFAULT NULL,
    `line`                 int(4) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_amdb_middle_ware_instance
-- ----------------------------
DROP TABLE IF EXISTS `t_amdb_middle_ware_instance`;
CREATE TABLE `t_amdb_middle_ware_instance`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `server_type` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '服务/集群类型',
    `server_name` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '服务名称',
    `ip_address`  varchar(64) COLLATE utf8mb4_bin  NOT NULL COMMENT 'IP地址',
    `gmt_create`  timestamp                        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modify`  timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_amdb_mq_subscribe
-- ----------------------------
DROP TABLE IF EXISTS `t_amdb_mq_subscribe`;
CREATE TABLE `t_amdb_mq_subscribe`
(
    `id`               bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `subscribe_key`    varchar(64)   NOT NULL COMMENT '订阅KEY',
    `subscribe_target` varchar(64)   NOT NULL COMMENT '订阅目标',
    `topic`            varchar(128)  NOT NULL COMMENT 'Topic',
    `fields`           varchar(2048) NOT NULL COMMENT '订阅字段',
    `params`           varchar(1024) NOT NULL COMMENT '订阅参数',
    `tenant`           varchar(64)   NOT NULL COMMENT '租户标识',
    `creator`          varchar(64) DEFAULT NULL COMMENT '创建人编码',
    `creator_name`     varchar(64) DEFAULT NULL COMMENT '创建人名称',
    `modifier`         varchar(64) DEFAULT NULL COMMENT '更新人编码',
    `modifier_name`    varchar(64) DEFAULT NULL COMMENT '更新人名称',
    `gmt_create`       datetime(3) NOT NULL COMMENT '创建时间',
    `gmt_modify`       datetime(3) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_amdb_pradar_e2e_assert_config
-- ----------------------------
DROP TABLE IF EXISTS `t_amdb_pradar_e2e_assert_config`;
CREATE TABLE `t_amdb_pradar_e2e_assert_config`
(
    `id`               bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `node_id`          varchar(64)  NOT NULL COMMENT '节点ID',
    `assert_code`      varchar(256) NOT NULL COMMENT '断言编号',
    `assert_condition` varchar(256) NOT NULL COMMENT '断言脚本',
    `gmt_create`       timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modify`       timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `node_unique` (`node_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_amdb_pradar_e2e_config
-- ----------------------------
DROP TABLE IF EXISTS `t_amdb_pradar_e2e_config`;
CREATE TABLE `t_amdb_pradar_e2e_config`
(
    `id`         bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `node_id`    varchar(64)        DEFAULT '' COMMENT '节点ID',
    `service`    varchar(512)       DEFAULT '' COMMENT '服务',
    `method`     varchar(512)       DEFAULT '' COMMENT '方法',
    `rpc_type`   varchar(4)         DEFAULT '' COMMENT 'rpcType',
    `app_name`   varchar(512)       DEFAULT '' COMMENT '应用名称',
    `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modify` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `node_unique` (`node_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_amdb_pradar_link_config
-- ----------------------------
DROP TABLE IF EXISTS `t_amdb_pradar_link_config`;
CREATE TABLE `t_amdb_pradar_link_config`
(
    `id`         bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `link_id`    varchar(64)        DEFAULT '' COMMENT '链路ID',
    `service`    varchar(512)       DEFAULT '' COMMENT '服务',
    `method`     varchar(512)       DEFAULT '' COMMENT '方法',
    `rpc_type`   varchar(4)         DEFAULT '' COMMENT 'rpcType',
    `app_name`   varchar(512)       DEFAULT '' COMMENT '应用名称',
    `extend`     varchar(1024)      DEFAULT '' COMMENT '扩展信息',
    `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modify` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `link_node_unique` (`link_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_amdb_pradar_link_edge
-- ----------------------------
DROP TABLE IF EXISTS `t_amdb_pradar_link_edge`;
CREATE TABLE `t_amdb_pradar_link_edge`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `link_id`         varchar(64)        DEFAULT '' COMMENT '链路ID',
    `service`         varchar(512)       DEFAULT '' COMMENT '服务名称',
    `method`          varchar(512)       DEFAULT '' COMMENT '方法名称',
    `extend`          varchar(256)       DEFAULT '' COMMENT '扩展信息',
    `app_name`        varchar(512)       DEFAULT '' COMMENT '应用名称',
    `trace_app_name`  varchar(512)       DEFAULT '' COMMENT '入口应用名称',
    `server_app_name` varchar(512)       DEFAULT '' COMMENT '服务端应用名称',
    `rpc_type`        varchar(4)         DEFAULT '' COMMENT 'rpcType',
    `log_type`        varchar(4)         DEFAULT '' COMMENT 'logType',
    `middleware_name` varchar(256)       DEFAULT '' COMMENT '中间件名称',
    `entrance_id`     varchar(64)        DEFAULT '' COMMENT '入口ID',
    `from_app_id`     varchar(64)        DEFAULT '' COMMENT '起始节点应用ID',
    `to_app_id`       varchar(64)        DEFAULT '' COMMENT '目标节点应用ID',
    `edge_id`         varchar(64)        DEFAULT '' COMMENT '边ID',
    `gmt_create`      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modify`      timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `link_edge_unique` (`link_id`,`edge_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_amdb_pradar_link_entrance
-- ----------------------------
DROP TABLE IF EXISTS `t_amdb_pradar_link_entrance`;
CREATE TABLE `t_amdb_pradar_link_entrance`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `entrance_id`     varchar(128) NOT NULL COMMENT '入口ID',
    `app_name`        varchar(512)  DEFAULT '' COMMENT '应用名称',
    `service_name`    varchar(256)  DEFAULT '' COMMENT '服务名',
    `method_name`     varchar(256)  DEFAULT '' COMMENT '方法名',
    `middleware_name` varchar(256)  DEFAULT '' COMMENT '中间件名称',
    `rpc_type`        int(8) DEFAULT '0' COMMENT 'rpc_type',
    `extend`          varchar(1024) DEFAULT '' COMMENT 'extend',
    `gmt_create`      timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modify`      timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_entrance_id` (`entrance_id`) USING BTREE,
    KEY               `idx_appName` (`app_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_amdb_pradar_link_node
-- ----------------------------
DROP TABLE IF EXISTS `t_amdb_pradar_link_node`;
CREATE TABLE `t_amdb_pradar_link_node`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `link_id`         varchar(64)        DEFAULT '' COMMENT '链路ID',
    `app_name`        varchar(512)       DEFAULT '' COMMENT '应用名称',
    `trace_app_name`  varchar(512)       DEFAULT '' COMMENT '入口应用名称',
    `middleware_name` varchar(512)       DEFAULT '' COMMENT '中间件名称',
    `extend`          varchar(1024)      DEFAULT '' COMMENT '扩展信息',
    `app_id`          varchar(64)        DEFAULT '' COMMENT '应用ID',
    `gmt_create`      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modify`      timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `link_node_unique` (`link_id`,`app_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_amdb_pradar_rule
-- ----------------------------
DROP TABLE IF EXISTS `t_amdb_pradar_rule`;
CREATE TABLE `t_amdb_pradar_rule`
(
    `id`         bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id',
    `code`       varchar(64) NOT NULL COMMENT '规则编码',
    `name`       varchar(64) NOT NULL COMMENT '规则名称',
    `rule_type`  tinyint(8) DEFAULT '1' COMMENT '规则类型(1-基础规则 2-调用链规则)',
    `rule`       varchar(1024) DEFAULT NULL COMMENT '规则内容',
    `tips`       varchar(2048) DEFAULT NULL COMMENT '提示',
    `priority`   int(8) NOT NULL DEFAULT '1' COMMENT '优先级(越小越先执行)',
    `gmt_create` timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `gmt_modify` timestamp NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_amdb_publish_info
-- ----------------------------
DROP TABLE IF EXISTS `t_amdb_publish_info`;
CREATE TABLE `t_amdb_publish_info`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT '发布ID',
    `publisher`       varchar(64) NOT NULL COMMENT '发布人',
    `publish_time`    datetime(3) DEFAULT NULL COMMENT '发布时间',
    `publish_server`  varchar(256) DEFAULT NULL COMMENT '发布服务器',
    `publish_app`     varchar(64) NOT NULL COMMENT '发布应用',
    `publish_env`     varchar(64) NOT NULL COMMENT '发布环境',
    `publish_version` varchar(64) NOT NULL COMMENT '发布版本',
    `ext`             longtext COMMENT '扩展字段',
    `flag`            int(32) DEFAULT NULL COMMENT '标记位',
    `creator`         varchar(64)  DEFAULT NULL COMMENT '创建人编码',
    `creator_name`    varchar(64)  DEFAULT NULL COMMENT '创建人名称',
    `modifier`        varchar(64)  DEFAULT NULL COMMENT '更新人编码',
    `modifier_name`   varchar(64)  DEFAULT NULL COMMENT '更新人名称',
    `gmt_create`      datetime(3) NOT NULL COMMENT '创建时间',
    `gmt_modify`      datetime(3) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

SET
FOREIGN_KEY_CHECKS = 1;


ALTER TABLE `t_amdb_pradar_link_entrance`
    ADD COLUMN `link_type` char(2) NOT NULL DEFAULT '' COMMENT '链路日志类型(0-入口,1-出口)';

ALTER TABLE `t_amdb_pradar_link_entrance`
    ADD COLUMN `up_app_name` varchar(512) NOT NULL DEFAULT '' COMMENT '上游应用名称';

create table amdb.t_amdb_app_instance_status
(
    id             bigint auto_increment comment '实例id' primary key,
    app_name       varchar(512)                           not null comment '应用名',
    agent_id       varchar(64) null comment 'agentId',
    ip             varchar(64)                            not null comment 'ip',
    pid            varchar(32) null comment '进程号',
    hostname       varchar(512) default ''                not null comment '主机名称',
    agent_language varchar(255) null comment 'Agent 语言',
    agent_version  varchar(32) null comment 'Agent 版本号',
    probe_version  varchar(32)  default ''                not null comment '探针版本',
    probe_status   char(2)      default ''                not null comment '探针状态(0-已安装,1-未安装,2-安装中,3-卸载中,4-安装失败,5-卸载失败,99-未知状态)',
    error_code     varchar(32) null comment '错误码',
    error_msg      varchar(4096) null comment '错误信息',
    gmt_create     timestamp    default CURRENT_TIMESTAMP not null comment '创建时间',
    gmt_modify     timestamp    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
) comment '应用实例探针状态表';

create
UNIQUE INDEX idx_uniq1 on t_amdb_app_instance_status(app_name,agent_id,ip,pid);

