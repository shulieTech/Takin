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

/**
 * @ClassName AgentUrls
 * @Description agent请求url
 * @Author qianshui
 * @Date 2020/11/16 下午1:37
 */
public class AgentUrls {

    /**
     * 统一前缀
     */
    public static final String PREFIX_URL = "/api";

    /**
     * 白名单查询接口
     */
    public static final String WHITELIST_URL = "/confcenter/query/bwlist";

    /**
     * 白名单查询接口 / 读取file
     */
    public static final String WHITELIST_FILE_URL = "/confcenter/wbmnt/query";

    /**
     * 拉取影子库表配置的接口
     */
    public static final String SHADOW_DB_TABLE_URL = "/link/ds/configs/pull";

    /**
     * 拉取影子库表配置的接口
     */
    public static final String SHADOW_SHADOW_CONSUMER_URL = "/agent/configs/shadow/consumer";

    /**
     * 拉取影子server配置的接口
     */
    public static final String SHADOW_SERVER_URL = "/link/ds/server/configs/pull";

    /**
     * 查询影子job的接口
     */
    public static final String TRO_SHADOW_JOB_URL = "/shadow/job/queryByAppName";

    /**
     * 上报错误的影子job接口
     */
    public static final String TRO_REPORT_ERROR_SHADOW_JOB_URL = "/shadow/job/update";

    /**
     * 上报应用的接口
     */
    public static final String APP_INSERT_URL = "/application/center/app/info";

    /**
     * 上报应用状态的接口
     */
    public static final String UPLOAD_ACCESS_STATUS = "/application/agent/access/status";

    /**
     * 上传应用信息接口
     */
    public static final String UPLOAD_APP_INFO = "/confcenter/interface/add/interfaceData";

    /**
     * 是否需要上传应用信息接口
     */
    public static final String UPLOAD = "/confcenter/interface/query/needUpload";

    /**
     * 更新应用agent版本的接口
     */
    public static final String AGENT_VERSION = "/confcenter/applicationmnt/update/applicationAgent";

    /**
     * 全局压测开关
     */
    public static final String APP_PRESSURE_SWITCH_STATUS = "/application/center/app/switch/agent";

    /**
     * 白名单开关
     */
    public static final String APP_WHITE_LIST_SWITCH_STATUS = "/global/switch/whitelist";

    /**
     * 挡板配置
     */
    public static final String GUARD_URL = "/link/guard/guardmanage";

    public static final String BWLISTMETRIC_URL = "/confcenter/wbmnt/query/bwlistmetric";

    public static final String REGISTER_URL = "/agent/api/register";

    /**
     * 中间件状态
     */
    public static final String MIDDLE_STAUTS_URL = "/application/agent/middle/status";

    public static final String ROCKETMQ_ISO_URL = "/isolation/query/rockemtMqIsoQuery";

    public static final String FAST_DEBUG_STACK_UPLOAD_URL = "/fast/debug/stack/upload";

    public static final String FAST_DEBUG_AGENT_LOG_UPLOAD_URL = "/fast/debug/log/upload";


    /**
     * 拉取ES Server配置的接口
     */
    public static final String SHADOW_ES_SERVER_URL = "/link/es/server/configs/pull";

    /**
     * 拉取kafka cluster配置的接口
     */
    public static final String SHADOW_KAFKA_CLUSTER_URL = "/link/kafka/cluster/configs/pull";

    /**
     * 拉取Hbase Server配置的接口
     */
    public static final String SHADOW_HBASE_SERVER_URL = "/link/hbase/server/configs/pull";

    /**
     * 性能分析
     */
    public static final String PERFORMANCE_BASE_URL = "/agent/performance/basedata";
    public static final String PERFORMANCE_TRACE_URL = "/traceManage/uploadTraceInfo";


}
