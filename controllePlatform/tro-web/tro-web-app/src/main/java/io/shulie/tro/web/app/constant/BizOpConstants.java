/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.tro.web.app.constant;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @author shiyajian
 * create: 2020-09-16
 */
public final class BizOpConstants {

    public static final Map<String, String> modelNameMap = Maps.newHashMap();

    private BizOpConstants() { /* no instance */ }

    public static class Modules {
        public static final String ACCOUNT_MANAGE = "账户管理";
        public static final String LINK_CARDING = "链路梳理";
        public static final String APPLICATION_MANAGE = "应用管理";
        public static final String PRESSURE_TEST_MANAGE = "压测管理";
        public static final String CONFIG_CENTER = "配置中心";
        public static final String SCRIPT_MANAGE = "脚本管理";
        public static final String FAST_DEBUG_MANAGE = "快速调试";
        public static final String PRADAR = "PRADAR";
        public static final String MIDDLEWARE_SCAN_MANAGE = "中间件扫描";
    }

    public static class SubModules {
        public static final String ACCOUNT_SUB = "子模块";
        public static final String SYSTEM_PROCESS = "系统流程";
        public static final String BUSINESS_ACTIVITY = "业务活动";
        public static final String BUSINESS_PROCESS = "业务流程";
        public static final String BASIC_INFO = "基础信息";
        public static final String SHADOW_DATABASE_TABLE = "影子库表";
        public static final String OUTLET_BAFFLE = "出口挡板";
        public static final String JOB_TASK = "job任务";
        public static final String WHITE_LIST = "白名单";
        public static final String SHADOW_CONSUMER = "白名单";
        public static final String PRESSURE_TEST_SCENE = "压测场景";
        public static final String PRESSURE_TEST_SWITCH = "压测开关";
        public static final String PRESSURE_WHITELIST_SWITCH = "白名单开关";
        public static final String BLACKLIST = "黑名单";
        public static final String ENTRYRULE = "入口规则";
        public static final String SCRIPT_MANAGE = "脚本管理";
        public static final String SHELL_SCRIPT_MANAGE = "shell脚本管理";
        public static final String AUTHORITY_CONFIG = "权限管理";
        /**
         * 快速调试
         */
        public static final String FAST_DEBUG_CONFIG = "调试配置";
        public static final String FAST_DEBUG_RESULT = "调试结果";

        /**
         * 中间件扫描
         */
        public static final String MIDDLEWARE_SCAN = "中间件扫描";

        /**
         * pradar ZK 配置
         */
        public static final String PRADAR_CONFIG = "pradar ZK 配置";
    }

    public static class ModuleCode {
        public static final String DASHBOARD = "dashboard";
        public static final String LINK_CARDING = "linkTease";
        public static final String SYSTEM_PROCESS = "systemFlow";
        public static final String BUSINESS_ACTIVITY = "businessActivity";
        public static final String BUSINESS_PROCESS = "businessFlow";
        public static final String APPLICATION_MANAGE = "appManage";
        public static final String PRESSURE_TEST_MANAGE = "pressureTestManage";
        public static final String PRESSURE_TEST_SCENE = "pressureTestManage_pressureTestScene";
        public static final String PRESSURE_TEST_REPORT = "pressureTestManage_pressureTestReport";
        public static final String PRESSURE_STATISTIC = "pressureTestManage_pressureTestStatistic";
        public static final String CONFIG_CENTER = "configCenter";
        public static final String PRESSURE_TEST_SWITCH = "configCenter_pressureMeasureSwitch";
        public static final String PRESSURE_WHITELIST_SWITCH = "configCenter_whitelistSwitch";
        public static final String BLACKLIST = "configCenter_blacklist";
        public static final String ENTRYRULE = "configCenter_entryRule";
        public static final String FLOW_ACCOUNT = "flowAccount";
        public static final String OPERATIONLOG = "configCenter_operationLog";
        public static final String SCRIPT_MANAGE = "scriptManage";
        public static final String AUTHORITY_CONFIG = "configCenter_authorityConfig";
        public static final String DEBUGTOOL = "debugTool";
        public static final String DEBUGTOOL_LINKDEBUG = "debugTool_linkDebug";
        public static final String DEBUGTOOL_LINKDEBUG_DETAIL = "debugTool_linkDebug_detail";
        public static final String PRADAR_CONFIG = "configCenter_bigDataConfig";
        public static final String LEAK_VERIFY = "leakVerify";
        public static final String DATASOURCE_MANAGE = "configCenter_dataSourceConfig";
        // todo 需要前端给
        public static final String MIDDLEWARE_SCAN_MANAGE = "middleware_scan_manage";
        // 白名单列表
        public static final String APPWHITELIST = "appWhiteList";


    }

    public static class OpTypes {
        public static final String CREATE = "新增";
        public static final String UPDATE = "编辑";
        public static final String DELETE = "删除";
        public static final String OPEN = "开启";
        public static final String CLOSE = "关闭";
        public static final String START = "启动";
        public static final String START_TRIAL_RUN = "启动试跑";
        public static final String STOP = "停止";
        public static final String DISABLE = "禁用";
        public static final String ENABLE = "启用";
        public static final String ADD = "加入";
        public static final String REMOVE = "移除";
        public static final String DEBUG = "调试";
        public static final String ROLLBACK = "回滚";
        public static final String EXECUTE = "执行";
    }

    public static class Message {

        /**
         * 系统流程
         */
        public static final String MESSAGE_SYSTEM_PROCESS_CREATE = "message.system_process.create";
        public static final String MESSAGE_SYSTEM_PROCESS_DELETE = "message.system_process.delete";
        public static final String MESSAGE_SYSTEM_PROCESS_UPDATE = "message.system_process.update";

        /**
         * 业务活动
         */
        public static final String MESSAGE_BUSINESS_ACTIVITY_CREATE = "message.business_activity.create";
        public static final String MESSAGE_BUSINESS_ACTIVITY_DELETE = "message.business_activity.delete";
        public static final String MESSAGE_BUSINESS_ACTIVITY_UPDATE = "message.business_activity.update";

        /**
         * 业务流程
         */
        public static final String MESSAGE_BUSINESS_PROCESS_CREATE = "message.business_process.create";
        public static final String MESSAGE_BUSINESS_PROCESS_DELETE = "message.business_process.delete";
        public static final String MESSAGE_BUSINESS_PROCESS_UPDATE = "message.business_process.update";

        /**
         * 基础信息
         */
        public static final String MESSAGE_BASIC_INFO_CREATE = "message.basic_info.create";
        public static final String MESSAGE_BASIC_INFO_DELETE = "message.basic_info.delete";
        public static final String MESSAGE_BASIC_INFO_UPDATE = "message.basic_info.update";

        /**
         * 影子库表
         */
        public static final String MESSAGE_SHADOW_DATABASE_TABLE_CREATE = "message.shadow_database_table.create";
        public static final String MESSAGE_SHADOW_DATABASE_TABLE_DELETE = "message.shadow_database_table.delete";
        public static final String MESSAGE_SHADOW_DATABASE_TABLE_UPDATE = "message.shadow_database_table.update";
        public static final String MESSAGE_SHADOW_DATABASE_ENABLE_DISABLE
            = "message.shadow_database_table.enable_disable";

        /**
         * job
         */
        public static final String MESSAGE_JOB_TASK_CREATE = "message.job_task.create";
        public static final String MESSAGE_JOB_TASK_DELETE = "message.job_task.delete";
        public static final String MESSAGE_JOB_TASK_UPDATE = "message.job_task.update";
        public static final String MESSAGE_JOB_TASK_ENABLE_DISABLE = "message.job_task.enable_disable";

        /**
         * 出口挡板
         */
        public static final String MESSAGE_OUTLET_BAFFLE_CREATE = "message.outlet_baffle.create";
        public static final String MESSAGE_OUTLET_BAFFLE_DELETE = "message.outlet_baffle.delete";
        public static final String MESSAGE_OUTLET_BAFFLE_UPDATE = "message.outlet_baffle.update";
        public static final String MESSAGE_OUTLET_BAFFLE_ENABLE_DISABLE = "message.outlet_baffle.enable_disable";

        /**
         * 白名单
         */
        public static final String MESSAGE_WHITE_LIST_CREATE = "message.white_list.create";
        public static final String MESSAGE_WHITE_LIST_ADD_REMOVE = "message.white_list.add_remove";
        public static final String MESSAGE_WHITE_LIST_UPDATE = "message.white_list.update";
        public static final String MESSAGE_WHITE_LIST_DELETE = "message.white_list.delete";

        /**
         * 影子消费者
         */
        public static final String MESSAGE_SHADOW_CONSUMER_CREATE = "message.shadow_consumer.create";
        public static final String MESSAGE_SHADOW_CONSUMER_ADD_REMOVE = "message.shadow_consumer.add_remove";
        public static final String MESSAGE_SHADOW_CONSUMER_UPDATE = "message.shadow_consumer.update";
        public static final String MESSAGE_SHADOW_CONSUMER_DELETE = "message.shadow_consumer.delete";

        /**
         * 压测场景
         */
        public static final String MESSAGE_PRESSURE_TEST_SCENE_CREATE = "message.pressure_test_scene.create";
        public static final String MESSAGE_PRESSURE_TEST_SCENE_DELETE = "message.pressure_test_scene.delete";
        public static final String MESSAGE_PRESSURE_TEST_SCENE_UPDATE = "message.pressure_test_scene.update";
        public static final String MESSAGE_PRESSURE_TEST_SCENE_START = "message.pressure_test_scene.start";
        public static final String MESSAGE_PRESSURE_TEST_SCENE_START_TRIAL_RUN
            = "message.pressure_test_scene.startTrialRun";
        public static final String MESSAGE_PRESSURE_TEST_SCENE_STOP = "message.pressure_test_scene.stop";

        /**
         * 压测开关
         */
        public static final String MESSAGE_PRESSURE_TEST_SWITCH_ACTION = "message.pressure_test_switch.action";

        /**
         * 白名单开关
         */
        public static final String MESSAGE_WHITELIST_SWITCH_ACTION = "message.whitelist_switch.action";

        /**
         * 黑名单
         */
        public static final String MESSAGE_BLACKLIST_CREATE = "message.blacklist.create";
        public static final String MESSAGE_BLACKLIST_DELETE = "message.blacklist.delete";
        public static final String MESSAGE_BLACKLIST_BATCH_DELETE = "message.blacklist.batch.delete";
        public static final String MESSAGE_BLACKLIST_UPDATE = "message.blacklist.update";
        public static final String MESSAGE_BLACKLIST_ACTION = "message.blacklist.action";
        public static final String MESSAGE_BLACKLIST_BATCH_ACTION = "message.blacklist.batch.action";

        /**
         * 入口规则
         */
        public static final String MESSAGE_ENTRYRULE_CREATE = "message.entryrule.create";
        public static final String MESSAGE_ENTRYRULE_DELETE = "message.entryrule.delete";
        public static final String MESSAGE_ENTRYRULE_UPDATE = "message.entryrule.update";

        /**
         * 脚本管理
         */
        public static final String SCRIPT_MANAGE_CREATE = "message.script_manage.create";
        public static final String SCRIPT_MANAGE_UPDATE = "message.script_manage.update";
        public static final String SCRIPT_MANAGE_DELETE = "message.script_manage.delete";

        public static final String SCRIPT_MANAGE_SCRIPTID_DELETE = "message.script_manage.scriptId.delete";
        public static final String SCRIPT_MANAGE_EXECUTE = "message.script_manage.execute";
        public static final String SCRIPT_MANAGE_ROLLBACK = "message.script_manage.rollback";
        /**
         * 快速调试
         */
        public static final String FAST_DEBUG_CONFIG_CREATE = "fast.debug.config.create";
        public static final String FAST_DEBUG_CONFIG_UPDATE = "fast.debug.config.update";
        public static final String FAST_DEBUG_CONFIG_DELETE = "fast.debug.config.delete";
        public static final String FAST_DEBUG_CONFIG_DEBUG = "fast.debug.config.debug";

        /**
         * pradarZK开关配置
         */
        public static final String PRADAR_CONFIG_UPDATE = "pradar.config.update";
    }

    public static class Vars {

        /**
         * 系统流程
         */
        public static final String SYSTEM_PROCESS = "systemProcess";

        /**
         * 入口应用
         */
        public static final String ENTRY_APPLICATION = "entryApplication";

        /**
         * 接口
         */
        public static final String INTERFACE = "interface";

        /**
         * 接口类型
         */
        public static final String INTERFACE_TYPE = "interfaceType";

        /**
         * 应用
         */
        public static final String APPLICATION = "application";

        /**
         * 节点数量
         */
        public static final String NODE_NUMBER = "nodeNumber";

        /**
         * 应用名称
         */
        public static final String APPLICATION_NAME = "applicationName";

        /**
         * 入口类型
         */
        public static final String ENTRANCE_TYPE = "entranceType";

        /**
         * 服务名称
         */
        public static final String SERVICE_NAME = "serviceName";

        /**
         * 业务活动
         */
        public static final String BUSINESS_ACTIVITY = "businessActivity";

        /**
         * 业务流程
         */
        public static final String BUSINESS_PROCESS = "businessProcess";

        /**
         * 业务流程可选信息
         */
        public static final String BUSINESS_PROCESS_SELECTIVE_CONTENT = "selectiveContent";

        /**
         * 影子库表url
         */
        public static final String SHADOW_DATABASE_TABLE_URL = "shadowDatabaseTableURL";

        /**
         * 类名#方法名
         */
        public static final String CLASS_METHOD_NAME = "classMethodName";

        /**
         * 任务
         */
        public static final String TASK = "task";

        /**
         * 入口规则
         */
        public static final String ENTRY_API = "entryApi";

        /**
         * 场景ID
         */
        public static final String SCENE_ID = "sceneId";

        /**
         * 场景名称
         */
        public static final String SCENE_NAME = "sceneName";

        /**
         * 场景可选信息
         */
        public static final String SCENE_SELECTIVE_CONTENT = "selectiveContent";

        /**
         * redis key
         */
        @Deprecated
        public static final String REDIS_KEY = "redisKey";

        /**
         * 黑名单类型
         */
        public static final String BLACKLIST_TYPE = "blacklistType";

        /**
         * 黑名单值
         */
        public static final String BLACKLIST_VALUE = "blacklistValue";

        /**
         * 开启关闭操作
         */
        public static final String ACTION = "action";

        /**
         * 脚本id
         */
        public static final String SCRIPT_MANAGE_ID = "scriptId";

        /**
         * 脚本发布实例id
         */
        public static final String SCRIPT_MANAGE_DEPLOY_ID = "scriptManageDeployId";
        /**
         * 脚本发布实例id
         */
        public static final String EXECUTE_RESULT = "executeResult";
        /**
         * 脚本发布实例名称
         */
        public static final String SCRIPT_MANAGE_DEPLOY_NAME = "scriptManageDeployName";
        /**
         * 脚本名称
         */
        public static final String SCRIPT_MANAGE_NAME = "scriptManageName";


        /**
         * 快速调试
         */
        public static final String FAST_DEBUG_CONFIG_ID = "fastDebugConfigId";
        public static final String FAST_DEBUG_CONFIG_NAME = "fastDebugConfigName";

        public static final String CONSUMER_TYPE = "consumerType";
        public static final String CONSUMER_TOPIC_GROUP = "topicGroup";

    }
}
