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