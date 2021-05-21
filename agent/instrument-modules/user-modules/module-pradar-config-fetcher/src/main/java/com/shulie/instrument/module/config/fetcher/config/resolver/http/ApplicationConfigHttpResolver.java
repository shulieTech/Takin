/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shulie.instrument.module.config.fetcher.config.resolver.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.pamirs.pradar.AppNameUtils;
import com.pamirs.pradar.ErrorTypeEnum;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.PradarSwitcher;
import com.pamirs.pradar.common.HttpUtils;
import com.pamirs.pradar.internal.config.*;
import com.pamirs.pradar.pressurement.agent.event.IEvent;
import com.pamirs.pradar.pressurement.agent.event.impl.ClusterTestSwitchOffEvent;
import com.pamirs.pradar.pressurement.agent.event.impl.ClusterTestSwitchOnEvent;
import com.pamirs.pradar.pressurement.agent.event.impl.WhiteListSwitchOffEvent;
import com.pamirs.pradar.pressurement.agent.event.impl.WhiteListSwitchOnEvent;
import com.pamirs.pradar.pressurement.agent.listener.EventResult;
import com.pamirs.pradar.pressurement.agent.listener.PradarEventListener;
import com.pamirs.pradar.pressurement.agent.shared.service.ErrorReporter;
import com.pamirs.pradar.pressurement.agent.shared.service.EventRouter;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.pamirs.pradar.pressurement.agent.shared.service.ShadowDatabaseConfigParser;
import com.pamirs.pradar.pressurement.base.custominterface.AppInterfaceDomain;
import com.pamirs.pradar.pressurement.base.util.PropertyUtil;
import com.pamirs.pradar.pressurement.datasource.util.DbUrlUtils;
import com.shulie.instrument.module.config.fetcher.config.event.FIELDS;
import com.shulie.instrument.module.config.fetcher.config.impl.ApplicationConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author shiyajian
 * create: 2020-08-11
 */
public class ApplicationConfigHttpResolver extends AbstractHttpResolver<ApplicationConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfigHttpResolver.class.getName());

    public static final String PRADAR_AGENT_VERSION_CONFIG_ENV = "agent.version";

    /**
     * 压测白名单查询接口
     */
    private static final String WHITELIST_FILE_URL = "/api/confcenter/wbmnt/query/";
    /**
     * 拉取影子库表配置的接口
     */
    private static final String SHADOW_DB_TABLE_URL = "/api/link/ds/configs/pull";

    /**
     * 拉取 trace 入口的规则配置
     */
    private static final String TRACE_REGULAR_RULE_URL = "/api/api/pull";

    private static final String REDIS_SHADOW_SERVER_URL = "/api/link/ds/server/configs/pull";

    private static final String ES_SHADOW_SERVER_URL = "/api/link/es/server/configs/pull";
    /**
     * 查询影子job的接口
     */
    public static final String TRO_SHADOW_JOB_URL = "/api/shadow/job/queryByAppName";
    /**
     * 上报错误的影子job接口
     */
    public static final String TRO_REPORT_ERROR_SHADOW_JOB_URL = "/api/shadow/job/update";
    /**
     * 上报应用的接口
     */
    private static final String APP_INSERT_URL = "/api/application/center/app/info";
    /**
     * 上报应用状态的接口
     */
    private static final String UPLOAD_ACCESS_STATUS = "/api/application/agent/access/status";
    /**
     * 上传应用信息接口
     */
    private static final String UPLOAD_APP_INFO = "/api/confcenter/interface/add/interfaceData";
    /**
     * 是否需要上传应用信息接口
     */
    private static final String UPLOAD = "/api/confcenter/interface/query/needUpload";
    /**
     * 更新应用agent版本的接口
     */
    private static final String AGENT_VERSION = "/api/confcenter/applicationmnt/update/applicationAgent";
    /**
     * 挡板 url
     */
    private static String MOCK_URL = "/api/link/guard/guardmanage?current=0&pageSize=1024&applicationName=%s";

    /**
     * hbase影子集群
     */
    private static final String SHADOW_HBASE_SERVER_URL = "/api/link/hbase/server/configs/pull";


    /**
     * 影子MQ消费者接口
     */
    private static final String TRO_SHADOW_MQ_CONSUMER_URL = "/api/agent/configs/shadow/consumer";

    private static final String DATA = "data";
    private static final String B_LISTS = "bLists";
    private static final String W_LISTS = "wLists";
    private static final String REDIS_KEY = "REDIS_KEY";
    private static final String DUBBO = "dubbo";
    private static final String RPC = "rpc";
    private static final String MQ = "mq";
    private static final String SEARCH = "search";
    private static final String HTTP = "http";
    private static final String TYPE2 = "TYPE";
    private static final String GLOBAL = "isGlobal";
    private static final String APP_NAMES = "appNames";
    private static final String INTERFACE_NAME = "INTERFACE_NAME";
    //新增应用参数
    private static final String NODE_UNIQUE_KEY = UUID.randomUUID().toString().replace("_", "");
    private String VERSION;

    private AtomicBoolean whiteListPullSwitch = new AtomicBoolean(Boolean.TRUE);
    private AtomicBoolean shadowConfigPullSwitch = new AtomicBoolean(Boolean.TRUE);

    public ApplicationConfigHttpResolver(int interval, TimeUnit timeUnit) {
        super("application-config-fetch-scheduled", interval, timeUnit);
        EventRouter.router().addListener(new PradarEventListener() {

            @Override
            public EventResult onEvent(IEvent event) {
                if (event instanceof ClusterTestSwitchOnEvent) {
                    shadowConfigPullSwitch.compareAndSet(Boolean.FALSE, Boolean.TRUE);
                } else if (event instanceof WhiteListSwitchOnEvent) {
                    whiteListPullSwitch.compareAndSet(Boolean.FALSE, Boolean.TRUE);
                } else {
                    return EventResult.IGNORE;
                }
                return EventResult.success("ApplicationConfigHttpResolver update config pull switch");
            }

            @Override
            public int order() {
                return 11;
            }
        }).addListener(new PradarEventListener() {
            @Override
            public EventResult onEvent(IEvent event) {
                if (event instanceof ClusterTestSwitchOffEvent) {
                    shadowConfigPullSwitch.compareAndSet(Boolean.TRUE, Boolean.FALSE);
                } else if (event instanceof WhiteListSwitchOffEvent) {
                    whiteListPullSwitch.compareAndSet(Boolean.TRUE, Boolean.FALSE);
                } else {
                    return EventResult.IGNORE;
                }
                return EventResult.success("ApplicationConfigHttpResolver update config pull switch");
            }

            @Override
            public int order() {
                return 12;
            }
        });
    }

    @Override
    public ApplicationConfig fetch() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("SIMULATOR: prepare to fetch config....");
        }
        PradarSwitcher.turnConfigSyncSwitchOn();
        // 获取配置
        String troControlWebUrl = PropertyUtil.getTroControlWebUrl();
        /**
         * 检查并自动生成应用,如果自动生成应用失败可以忽略
         */
        checkAndGenerateApp(troControlWebUrl);
        /**
         * 上传应用接入状态,如果上报失败可以忽略
         */
        uploadAccessStatus(troControlWebUrl);

        /**
         * 上传应用信息,如果失败可以忽略
         */
        uploadAppInfo(troControlWebUrl);

        /**
         * 拉取es名单信息
         */
        fetchEsBlockList();

        ApplicationConfig applicationConfig = new ApplicationConfig(this);
        boolean isSuccess;
        if (whiteListPullSwitch.get()) {
            /**
             * 从服务端获取白名单列表,如果失败则启动失败
             */
            isSuccess = getWhiteList(troControlWebUrl, applicationConfig);
            if (!isSuccess) {
                PradarSwitcher.turnConfigSyncSwitchOff();
                if (ApplicationConfig.getWhiteList) {
                    // 存在成功获取配置
                    applicationConfig.setUrlWhiteList(GlobalConfig.getInstance().getUrlWhiteList());
                    applicationConfig.setRpcNameWhiteList(GlobalConfig.getInstance().getRpcNameWhiteList());
                    applicationConfig.setCacheKeyAllowList(GlobalConfig.getInstance().getCacheKeyWhiteList());
                    applicationConfig.setContextPathBlockList(GlobalConfig.getInstance().getContextPathBlockList());
                    applicationConfig.setMqList(GlobalConfig.getInstance().getMqWhiteList());
                    applicationConfig.setSearchWhiteList(GlobalConfig.getInstance().getSearchWhiteList());
                }
                LOGGER.error("SIMULATOR: get white list from server failed");
            } else {
                ApplicationConfig.getWhiteList = Boolean.TRUE;
            }
        }
        if (shadowConfigPullSwitch.get()) {
            /**
             * 读取压测的影子数据源配置
             */
            isSuccess = getPressureTable4AccessSimple(troControlWebUrl, applicationConfig);
            if (!isSuccess) {
                PradarSwitcher.turnConfigSyncSwitchOff();
                if (ApplicationConfig.getPressureTable4AccessSimple) {
                    applicationConfig.setShadowDatabaseConfigs(GlobalConfig.getInstance().getShadowDatasourceConfigs());
                }
                LOGGER.error("SIMULATOR: get shadow db config from server failed");
            } else {
                ApplicationConfig.getPressureTable4AccessSimple = Boolean.TRUE;
            }

            /**
             * 获取影子job配置
             */
            isSuccess = getShadowJobConfig(troControlWebUrl, applicationConfig);
            if (!isSuccess) {
                PradarSwitcher.turnConfigSyncSwitchOff();
                LOGGER.error("SIMULATOR: get shadow job config from server failed");
            } else {
                ApplicationConfig.getShadowJobConfig = Boolean.TRUE;
            }

            /**
             * 上报错误的影子job配置,如果失败则可以忽略
             */
            reportErrorShadowJobConfig(troControlWebUrl);
        }

        Set<MockConfig> mockConfigs = getMockSet(troControlWebUrl);
        applicationConfig.setMockConfigs(mockConfigs);

        /**
         * 抓取影子server的配置
         */
        getShadowRedisServerConfig(troControlWebUrl, applicationConfig);

        getShadowEsServerConfig(troControlWebUrl, applicationConfig);
        getHbaseShadowConfig(troControlWebUrl, applicationConfig);

        /**
         * 获取 trace 规则入口配置
         */
        getTraceRegularRules(troControlWebUrl);


        /**
         * 拉取mq影子消费者信息
         */
        isSuccess = fetchMqShadowConsumer(troControlWebUrl, applicationConfig);
        if (!isSuccess) {
            PradarSwitcher.turnConfigSyncSwitchOff();
            LOGGER.error("[pradar] get shadow consumer from server failed");
        }

        if (PradarSwitcher.configSyncSwitchOn()
                || (ApplicationConfig.getWhiteList && ApplicationConfig.getPressureTable4AccessSimple && ApplicationConfig.getShadowJobConfig)) {
            // 配置拉取过程中，配置无异常
            // 或者历史配置有完成拉取的记录
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("SIMULATOR: successful to fetched config.");
            }
            PradarSwitcher.clusterTestReady();
        } else {
            // 本次配置没有成功拉取 并且 历史无成功拉取配置
            // 中断压测
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("SIMULATOR: failed to fetched config.");
            }
            PradarSwitcher.clusterTestPrepare();
        }
        return applicationConfig;
    }

    private static Set<MockConfig> getMockSet(String troControlWebUrl) {
        try {
            String projectName = AppNameUtils.appName();
            String linkGuardUrl = String.format(MOCK_URL, projectName);
            final StringBuilder url = new StringBuilder(troControlWebUrl).append(linkGuardUrl);
            final HttpUtils.HttpResult httpResult = HttpUtils.doGet(url.toString());
            if (!httpResult.isSuccess()) {
                LOGGER.warn("SIMULATOR: [FetchConfig] get mock config error. status: {}, result: {}", httpResult.getStatus(), httpResult.getResult());
                return Collections.EMPTY_SET;
            }

            JSONObject dataMap = JSON.parseObject(httpResult.getResult());
            JSONArray mapList = dataMap.getJSONArray(DATA);
            if (mapList == null || mapList.isEmpty()) {
                return Collections.EMPTY_SET;
            }

            Set<MockConfig> mockConfigs = new HashSet<MockConfig>();
            for (Object obj : mapList) {
                Map<String, Object> map = (Map<String, Object>) obj;
                //禁用状态不拉取配置
                if (map.containsKey("isEnable") && "false".equals(String.valueOf(map.get("isEnable")))) {
                    continue;
                }
                String methodInfo = (String) map.get("methodInfo");
                if (StringUtils.isBlank(methodInfo)) {
                    continue;
                }

                int type = 1;
                final String typeStr = (String) map.get("type");
                if (NumberUtils.isDigits(typeStr)) {
                    type = Integer.valueOf(typeStr);
                }

                if (methodInfo.indexOf("#") == -1) {
                    continue;
                }
                String params = null;
                if (StringUtils.indexOf(methodInfo, "(") != -1) {
                    params = StringUtils.substring(methodInfo, StringUtils.indexOf(methodInfo, "(") + 1);
                    if (StringUtils.indexOf(params, ")") != -1) {
                        params = StringUtils.substring(params, 0, StringUtils.indexOf(params, ")"));
                    }
                    methodInfo = StringUtils.substring(methodInfo, 0, StringUtils.indexOf(methodInfo, "("));
                }

                String[] classMethod = StringUtils.split(methodInfo, '#');
                if (classMethod.length != 2) {
                    continue;
                }
                if (StringUtils.isBlank(classMethod[0]) || StringUtils.isBlank(classMethod[1])) {
                    continue;
                }

                String script = (String) map.get("groovy");

                MockConfig mockConfig = new MockConfig();
                mockConfig.setClassName(StringUtils.trim(classMethod[0]));
                mockConfig.setMethodName(StringUtils.trim(classMethod[1]));
                mockConfig.setCodeScript(script);
                mockConfig.setType(type);
                if (StringUtils.isNotBlank(params)) {
                    String[] argTypes = StringUtils.split(params, ',');
                    List<String> parameterTypes = new ArrayList<String>();
                    for (String argType : argTypes) {
                        if (StringUtils.isNotBlank(argType)) {
                            parameterTypes.add(StringUtils.trim(argType));
                        }
                    }
                    mockConfig.setMethodArgClasses(parameterTypes);
                }

                mockConfigs.add(mockConfig);
            }
            return mockConfigs;
        } catch (Throwable e) {
            LOGGER.warn("link guard config parse err!", e);
            ErrorReporter.buildError()
                    .setErrorType(ErrorTypeEnum.LinkGuardEnhance)
                    .setErrorCode("link-guard-enhance-0002")
                    .setMessage("获取挡板列表失败！")
                    .setDetail("获取挡板列表失败:" + e.getMessage())
                    .report();
        }
        return Collections.EMPTY_SET;
    }


    /**
     * 拉取mq影子消费者
     */
    private boolean fetchMqShadowConsumer(String troControlWebUrl, ApplicationConfig applicationConfig) {
        try {

            StringBuilder url = new StringBuilder(troControlWebUrl)
                    .append(TRO_SHADOW_MQ_CONSUMER_URL).append("?appName=").append(AppNameUtils.appName());
            final HttpUtils.HttpResult httpResult = HttpUtils.doGet(url.toString());
            if (!httpResult.isSuccess()) {
                LOGGER.warn("SIMULATOR: [FetchConfig] get shadow consumer config error. status: {}, result: {}", httpResult.getStatus(), httpResult.getResult());
                return Boolean.FALSE;
            }

            if (StringUtils.isBlank(httpResult.getResult())) {
                LOGGER.error("[pradar] get shadow consumer config from server with empty response.");
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("agent-0009")
                        .setMessage("获取影子消费者配置失败")
                        .setDetail("获取影子消费者配置失败,接口返回值为空")
                        .report();
                return Boolean.FALSE;
            }

            JSONObject dataMap = JSON.parseObject(httpResult.getResult());
            Boolean success = (Boolean) dataMap.get("success");
            if (!success) {
                LOGGER.error("[pradar] get shadow consumer config from server with a fault response.");
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("agent-0004")
                        .setMessage("获取影子MQ配置失败")
                        .setDetail("获取影子MQ配置失败,接口返回查询状态success为false")
                        .report();
                return false;
            }


            JSONArray mapList = dataMap.getJSONArray(DATA);
            if (mapList == null || mapList.isEmpty()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("SIMULATOR: [FetchConfig] get shadow consumer config size is null. ");
                }
                return Boolean.TRUE;
            }


            for (int i = 0; i < mapList.size(); i++) {
                JSONObject stringObjectMap = (JSONObject) mapList.get(i);
                Map<String, List<String>> topicGroups = (Map<String, List<String>>) stringObjectMap.get("topicGroups");
                Set<Entry<String, List<String>>> entries = topicGroups.entrySet();
                Set<String> mqList = applicationConfig.getMqList();
                if (mqList == null) {
                    mqList = new HashSet<String>();
                    applicationConfig.setMqList(mqList);
                }

                for (Entry<String, List<String>> entry : entries) {
                    String key = entry.getKey();
                    List<String> values = entry.getValue();
                    for (int j = 0; j < values.size(); j++) {
                        String value = key + "#" + values.get(i);
                        mqList.add(value);
                    }
                }
            }
            return true;

        } catch (Throwable e) {
            LOGGER.error("[pradar] Report Error Shadow mq consumer failed.", e);
            ErrorReporter.buildError()
                    .setErrorType(ErrorTypeEnum.AgentError)
                    .setErrorCode("agent-0009")
                    .setMessage("获取影子消费者配置失败")
                    .setDetail("获取影子消费者配置失败,配置处理异常:" + e.getMessage())
                    .report();
            return false;
        }
    }

    @Override
    public ApplicationConfig fetch(FIELDS... fields) {
        ApplicationConfig applicationConfig = new ApplicationConfig(this);
        if (fields == null || fields.length == 0) {
            return applicationConfig;
        }
        String troControlWebUrl = PropertyUtil.getTroControlWebUrl();
        for (FIELDS field : fields) {
            switch (field) {
                case WHITE_LIST:
                    /**
                     * 从服务端获取白名单列表,如果失败则启动失败
                     */
                    boolean isSuccess = getWhiteList(troControlWebUrl, applicationConfig);
                    if (!isSuccess) {
                        PradarSwitcher.turnConfigSyncSwitchOff();
                        if (ApplicationConfig.getWhiteList) {
                            // 存在成功获取配置
                            applicationConfig.setUrlWhiteList(GlobalConfig.getInstance().getUrlWhiteList());
                            applicationConfig.setRpcNameWhiteList(GlobalConfig.getInstance().getRpcNameWhiteList());
                            applicationConfig.setCacheKeyAllowList(GlobalConfig.getInstance().getCacheKeyWhiteList());
                            applicationConfig.setContextPathBlockList(GlobalConfig.getInstance().getContextPathBlockList());
                            applicationConfig.setMqList(GlobalConfig.getInstance().getMqWhiteList());
                        }
                        LOGGER.error("SIMULATOR: get white list from server failed");
                    } else {
                        ApplicationConfig.getWhiteList = Boolean.TRUE;
                    }
                    break;
                case SHADOW_DATABASE_CONFIGS:
                    /**
                     * 读取压测的影子数据源配置
                     */
                    isSuccess = getPressureTable4AccessSimple(troControlWebUrl, applicationConfig);
                    if (!isSuccess) {
                        PradarSwitcher.turnConfigSyncSwitchOff();
                        if (ApplicationConfig.getPressureTable4AccessSimple) {
                            applicationConfig.setShadowDatabaseConfigs(GlobalConfig.getInstance().getShadowDatasourceConfigs());
                        }
                        LOGGER.error("SIMULATOR: get shadow db config from server failed");
                    } else {
                        ApplicationConfig.getPressureTable4AccessSimple = Boolean.TRUE;
                    }
                    break;

                case SHADOW_JOB_CONFIGS:
                    /**
                     * 获取影子job配置
                     */
                    isSuccess = getShadowJobConfig(troControlWebUrl, applicationConfig);
                    if (!isSuccess) {
                        PradarSwitcher.turnConfigSyncSwitchOff();
                        LOGGER.error("SIMULATOR: get shadow job config from server failed");
                    } else {
                        ApplicationConfig.getShadowJobConfig = Boolean.TRUE;
                    }

                    /**
                     * 上报错误的影子job配置,如果失败则可以忽略
                     */
                    reportErrorShadowJobConfig(troControlWebUrl);
                    break;
                case MOCK_CONFIGS:
                    Set<MockConfig> mockConfigs = getMockSet(troControlWebUrl);
                    applicationConfig.setMockConfigs(mockConfigs);
                    break;
                case SHADOW_REDIS_SERVER_CONFIG:

                    /**
                     * 抓取redis 影子server配置信息
                     */
                    getShadowRedisServerConfig(troControlWebUrl, applicationConfig);
                    break;
                case SHADOW_ES_SERVER_CONFIG:
                    getShadowEsServerConfig(troControlWebUrl, applicationConfig);
                    break;
                case SHADOW_HBASE_SERVER_CONFIG:
                    getHbaseShadowConfig(troControlWebUrl, applicationConfig);
                    break;
                default:

            }
        }
        return applicationConfig;
    }

    private void getShadowEsServerConfig(String troControlWebUrl, ApplicationConfig applicationConfig) {
        String appName = System.getProperty("pradar.project.name");
        String accessUrl = String.format("%s%s?appName=%s", troControlWebUrl, ES_SHADOW_SERVER_URL, appName);
        try {
            String response = System.getProperty("shadow.es.config");
            if (StringUtils.isBlank(response)) {
                HttpUtils.HttpResult httpResult = HttpUtils.doGet(accessUrl);
                if (!httpResult.isSuccess()) {
                    GlobalConfig.getInstance().setShadowEsServer(Boolean.FALSE);
                    return;
                }
                response = httpResult.getResult();
            }
            Map<String, ShadowEsServerConfig> shadowEsConfigMap = new HashMap<String, ShadowEsServerConfig>();
            JSONObject res = JSON.parseObject(response);
            Boolean success = (Boolean) res.get("success");
            if (!success) {
                LOGGER.error("[pradar] get es shadow config from server with a fault response. url={}, result={}", accessUrl, response);
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.RedisServer)
                        .setErrorCode("agent-0001")
                        .setMessage("get es shadow config error.")
                        .setDetail("获取影子es配置失败,接口返回查询状态success为false")
                        .report();
                return;
            }
            JSONArray datas = res.getJSONArray(DATA);
            if (datas == null || datas.size() == 0) {
                GlobalConfig.getInstance().setShadowEsServer(Boolean.FALSE);
                return;
            }
            for (Object object : datas) {
                JSONObject data = (JSONObject) object;
                if (0 != Long.parseLong(data.get("status").toString())) {
                    continue;
                }
                String config = (String) data.get("config");
                JSONObject configMap = JSON.parseObject(config);
                String businessNodes = (String) configMap.get("businessNodes");
                String performanceTestNodes = (String) configMap.get("performanceTestNodes");
                Object biz_cluster_name = configMap.get("businessClusterName");

                Object pt_cluster_name = configMap.get("performanceClusterName");
                ShadowEsServerConfig shadowEsServerConfig = new ShadowEsServerConfig(
                        Arrays.asList(businessNodes.split(","))
                        , Arrays.asList(performanceTestNodes.split(","))
                        , biz_cluster_name == null ? null : (String) biz_cluster_name
                        , pt_cluster_name == null ? null : (String) pt_cluster_name
                );
                shadowEsConfigMap.put(shadowEsServerConfig.identifyKey(), shadowEsServerConfig);
            }
            applicationConfig.setShadowEsServerConfigs(shadowEsConfigMap);
            GlobalConfig.getInstance().setShadowEsServer(Boolean.TRUE);
        } catch (Throwable e) {
            LOGGER.error("fetch es server config error. url={}", accessUrl, e);
        }

    }

    private void getShadowRedisServerConfig(String troControlWebUrl, ApplicationConfig applicationConfig) {
        Properties properties = System.getProperties();
        String appName = properties.getProperty("pradar.project.name");
        StringBuilder builder = new StringBuilder(troControlWebUrl)
                .append(REDIS_SHADOW_SERVER_URL)
                .append("?appName=")
                .append(appName);
        try {
            HttpUtils.HttpResult httpResult = HttpUtils.doGet(builder.toString());
            if (!httpResult.isSuccess()) {
                LOGGER.warn("SIMULATOR: [FetchConfig] get shadow redis server config error. url={}, status={}, result={}", builder.toString(), httpResult.getStatus(), httpResult.getResult());
                return;
            }

            //没有配置默认为影子表
            Map<String, ShadowRedisConfig> shadowRedisConfigMap = new HashMap<String, ShadowRedisConfig>();


            //解析装入内存
            JSONObject result = JSON.parseObject(httpResult.getResult());

            Boolean success = result.getBoolean("success");
            if (!success) {
                LOGGER.error("SIMULATOR: get redis shadow config from server with a fault response. url={}, result={}", builder.toString(), httpResult.getResult());
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.RedisServer)
                        .setErrorCode("agent-0001")
                        .setMessage("get redis shadow config error.")
                        .setDetail("获取影子redis配置失败,接口返回查询状态success为false")
                        .report();
                return;
            }

            Type type = new TypeReference<List<Map>>() {
            }.getType();
            List<Map> datas = result.getObject(DATA, type);

            if (datas == null || datas.size() == 0) {
                GlobalConfig.getInstance().setShadowDbRedisServer(false);
                return;
            }
            for (Map data : datas) {
                Map business = (Map) data.get("dataSourceBusiness");
                StringBuilder keyBuilder = new StringBuilder();
                if (business.get("master") != null) {
                    String businessMaster = (String) business.get("master");
                    keyBuilder.append(businessMaster)
                            .append(",");
                }
                String nodes = (String) business.get("nodes");
                keyBuilder.append(nodes);

                Map dataSourceBusinessPerformanceTest = (Map) data.get("dataSourceBusinessPerformanceTest");
                ShadowRedisConfig config = new ShadowRedisConfig();
                if (dataSourceBusinessPerformanceTest.get("master") != null) {
                    String pressureMaster = String.valueOf(dataSourceBusinessPerformanceTest.get("master"));
                    config.setMaster(pressureMaster);
                }
                String pressureNodes = String.valueOf(dataSourceBusinessPerformanceTest.get("nodes"));
                config.setNodes(pressureNodes);
                String passwd = String.valueOf(dataSourceBusinessPerformanceTest.get("password"));
                if (!"null".equalsIgnoreCase(passwd)) {
                    config.setPassword(passwd);
                }
                String database = String.valueOf(dataSourceBusinessPerformanceTest.get("database"));
                if (!"null".equalsIgnoreCase(database) && NumberUtils.isDigits(database)) {
                    config.setDatabase(Integer.valueOf(database));
                }
                shadowRedisConfigMap.put(keyBuilder.toString(), config);
            }
            applicationConfig.setShadowRedisConfigs(shadowRedisConfigMap);
            GlobalConfig.getInstance().setShadowDbRedisServer(true);

        } catch (Throwable e) {
            LOGGER.error("fetch redis server config error. url={}", builder.toString()
                    , e);
        }

    }

    /**
     * 上传应用信息,先判断是否需要上传
     *
     * @param troWebUrl
     */
    private void uploadAppInfo(String troWebUrl) {
        final AppInterfaceDomain appInfo = new AppInterfaceDomain();
        final StringBuilder uploadAppInfoUrl = new StringBuilder(troWebUrl + UPLOAD);
        try {
            appInfo.setAppName(AppNameUtils.appName());
            final Map param = new HashMap();
            param.put("appName", AppNameUtils.appName());
            param.put("size", appInfo.getAppDetails().size() + "");
            final HttpUtils.HttpResult httpResult = HttpUtils.doPost(uploadAppInfoUrl.toString(), JSON.toJSONString(param));
            if (!httpResult.isSuccess()) {
                LOGGER.warn("SIMULATOR: upload app info error. status: {}, result: {}", httpResult.getStatus(), httpResult.getResult());
                return;
            }
            if (httpResult.getResult() != null && httpResult.getResult().contains("data=true")) {
                final StringBuilder url2 = new StringBuilder(troWebUrl).append(UPLOAD_APP_INFO);
                HttpUtils.HttpResult httpResult1 = HttpUtils.doPost(url2.toString(), JSON.toJSONString(appInfo));
                if (!httpResult1.isSuccess()) {
                    LOGGER.warn("上传应用信息失败: {}", httpResult1.getResult());
                }
            }
        } catch (Throwable e) {
            LOGGER.error("upload app info failed. url={}", uploadAppInfoUrl, e);
        }
        final String projectName = AppNameUtils.appName();
        appInfo.setAppName(projectName);
        final StringBuilder uploadAgentVersion = new StringBuilder().append(troWebUrl).append(AGENT_VERSION)
                .append("?appName=").append(projectName).append("&agentVersion=")
                .append(getAgentVersion()).append("&pradarVersion=").append(getAgentVersion());
        try {
            HttpUtils.doGet(uploadAgentVersion.toString());
        } catch (Throwable e) {
            LOGGER.error("upload agent version info failed. url={}", uploadAgentVersion.toString(), e);
        }
    }

    /**
     * 定时上报接入状态
     *
     * @param troWeb
     */
    private void uploadAccessStatus(String troWeb) {
        Map<String, Object> errorList = ErrorReporter.getInstance().getErrors();
        if (errorList.isEmpty()) {
            return;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("nodeKey", NODE_UNIQUE_KEY);
        result.put("agentId", Pradar.getAgentId());
        result.put("applicationName", AppNameUtils.appName());
        result.put("switchErrorMap", errorList);
        final StringBuilder url = new StringBuilder(troWeb).append(UPLOAD_ACCESS_STATUS);
        try {
            HttpUtils.HttpResult httpResult = HttpUtils.doPost(url.toString(), JSON.toJSONString(result));
            if (!httpResult.isSuccess()) {
                LOGGER.warn("上传应用接入状态失败. url={}, result={}", url.toString(), httpResult.getResult());
            }
            // TODO 存在一个隐患，去除了清空内存中异常信息，改为agent全量发送异常数据
            // 内存消耗会加重，等tro控制台调整异常展示时同步调整
            ErrorReporter.getInstance().clear(errorList);
        } catch (Throwable e) {
            LOGGER.warn("上传应用接入状态信息报错. url={}", url.toString(), e);
        }
    }

    /**
     * 上报控制台，自动生成项目
     *
     * @param troWeb
     */
    private void checkAndGenerateApp(String troWeb) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(APP_COLUMN_APPLICATION_NAME, AppNameUtils.appName());
        map.put(APP_COLUMN_DDL_PATH, AppNameUtils.appName() + "/ddl.sh");
        map.put(APP_COLUMN_CLEAN_PATH, AppNameUtils.appName() + "/clean.sh");
        map.put(APP_COLUMN_READY_PATH, AppNameUtils.appName() + "/ready.sh");
        map.put(APP_COLUMN_BASIC_PATH, AppNameUtils.appName() + "/basic.sh");
        map.put(APP_COLUMN_CACHE_PATH, AppNameUtils.appName() + "/cache.sh");
        final StringBuilder url = new StringBuilder(troWeb).append(APP_INSERT_URL);
        try {
            HttpUtils.HttpResult httpResult = HttpUtils.doPost(url.toString(), JSON.toJSONString(map));
            if (!httpResult.isSuccess()) {
                LOGGER.warn("上报应用失败 url={}, result={}", url.toString(), httpResult.getResult());
            }
        } catch (Throwable e) {
            LOGGER.warn("自动增加应用失败 url={}", url.toString(), e);
        }
    }

    /**
     * 拉取es名单信息,这一期先从文件中读取，后续可以改成接口拉取
     */
    private void fetchEsBlockList() {
        File file = new File(PropertyUtil.getEsBlockListFilePath());
        if (!file.exists()) {
            return;
        }
        Set<String> list = new HashSet<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] arr = StringUtils.split(line, ',');
                for (String str : arr) {
                    str = StringUtils.trim(str);
                    if (StringUtils.isBlank(str)) {
                        continue;
                    }
                    list.add(str);
                }
            }
        } catch (IOException e) {
            LOGGER.error("SIMULATOR: Read ES Block list failed.", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOGGER.error("SIMULATOR: Read ES Block list failed.", e);
                }
            }
        }
    }

    /**
     * 上报错误的影子job
     *
     * @param troControlWebUrl
     */
    private void reportErrorShadowJobConfig(String troControlWebUrl) {
        StringBuilder url = new StringBuilder(troControlWebUrl)
                .append(TRO_REPORT_ERROR_SHADOW_JOB_URL);
        try {
            for (ShadowJob shaDowJob : GlobalConfig.getInstance().getErrorRegisterJobs()) {
                Map<String, Object> params = new HashMap<String, Object>(5, 1);
                params.put("id", shaDowJob.getId());
                params.put("active", shaDowJob.getActive());
                params.put("message", shaDowJob.getErrorMessage());
                HttpUtils.HttpResult httpResult = HttpUtils.doPost(url.toString(), JSON.toJSONString(params));
                if (!httpResult.isSuccess()) {
                    LOGGER.warn("上报错误的影子 job 失败. url={}, result={}", url, httpResult.getResult());
                }
            }
        } catch (Throwable e) {
            LOGGER.error("SIMULATOR: Report Error Shadow job failed. url={}", url, e);
        }
    }

    /**
     * 获取trace 入口的规则配置
     *
     * @param troControlWebUrl
     */
    private static boolean getTraceRegularRules(String troControlWebUrl) {
        StringBuilder url = new StringBuilder(troControlWebUrl)
                .append(TRACE_REGULAR_RULE_URL).append("?appName=").append(AppNameUtils.appName());
        try {
            HttpUtils.HttpResult httpResult = HttpUtils.doGet(url.toString());
            if (!httpResult.isSuccess()) {
                LOGGER.error("SIMULATOR: [FetchConfig] get trace regular rules config from server with error response. url={}, status={}, result={} ",
                        url, httpResult.getStatus(), httpResult.getResult());
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("agent-0005")
                        .setMessage("获取入口规则配置失败")
                        .setDetail("获取入口规则配置失败,接口返回值为空")
                        .report();
                return false;
            }

            JSONObject map = JSON.parseObject(httpResult.getResult());
            Boolean success = map.getBoolean("success");
            if (!success) {
                LOGGER.error("SIMULATOR: [FetchConfig] get trace regular rules config from server with a fault response. url={}, status={}, result={} ",
                        url, httpResult.getStatus(), httpResult.getResult());
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("agent-0005")
                        .setMessage("获取入口规则配置失败")
                        .setDetail("获取入口规则配置失败,接口返回值为空")
                        .report();
                return false;
            }
            Map<String, List<String>> data = map.getObject(DATA, new TypeReference<Map<String, List<String>>>() {
            }.getType());
            Set<String> sets = new HashSet<String>();
            for (Map.Entry<String, List<String>> entry : data.entrySet()) {
                if (StringUtils.equals(entry.getKey(), AppNameUtils.appName())) {
                    sets.addAll(entry.getValue());
                }
            }
            if (!sets.isEmpty()) {
                GlobalConfig.getInstance().setTraceRules(sets);
            }
        } catch (Throwable e) {
            LOGGER.error("SIMULATOR: get shadow job config from server with err response. url={}", url, e);
            ErrorReporter.buildError()
                    .setErrorType(ErrorTypeEnum.AgentError)
                    .setErrorCode("agent-0005")
                    .setMessage("获取入口规则配置失败")
                    .setDetail("获取入口规则配置失败,配置处理异常：" + e.getMessage())
                    .report();
            return false;
        }
        return true;
    }

    /**
     * 获取影子job的配置
     *
     * @param troControlWebUrl
     */
    private boolean getShadowJobConfig(String troControlWebUrl, ApplicationConfig applicationConfig) {
        StringBuilder url = new StringBuilder(troControlWebUrl)
                .append(TRO_SHADOW_JOB_URL).append("?appName=").append(AppNameUtils.appName());
        try {
            Set<ShadowJob> shadowJobs = new HashSet<ShadowJob>();
            HttpUtils.HttpResult httpResult = HttpUtils.doGet(url.toString());
            if (!httpResult.isSuccess()) {
                LOGGER.error("SIMULATOR: [FetchConfig] get shadow job config from server with error response. url={}, status={}, result={}"
                        , url, httpResult.getStatus(), httpResult.getResult());
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("agent-0004")
                        .setMessage("获取影子job配置失败")
                        .setDetail("获取影子job配置失败,接口返回值为空")
                        .report();
                return false;
            }

            Map map = JSON.parseObject(httpResult.getResult());
            Boolean success = (Boolean) map.get("success");
            if (!success) {
                LOGGER.error("SIMULATOR: [FetchConfig] get shadow datasource config from server with a fault response. url={}, status={}, result={}"
                        , url, httpResult.getStatus(), httpResult.getResult());
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("agent-0004")
                        .setMessage("获取影子job配置失败")
                        .setDetail("获取影子job配置失败,接口返回查询状态success为false")
                        .report();
                return false;
            }
            List<Map> data = (List<Map>) map.get(DATA);
            if (null != data && !data.isEmpty()) {
                for (Map datum : data) {
                    ShadowJob shaDowJob = new ShadowJob();
                    shaDowJob.setId(Long.valueOf(NumberFormat.getInstance().format(datum.get("id"))));
                    shaDowJob.setActive(Integer.valueOf(NumberFormat.getInstance().format(datum.get("active"))));
                    shaDowJob.setClassName((String) datum.get("name"));
                    shaDowJob.setStatus(NumberFormat.getInstance().format(datum.get("status")));
                    Map<String, Object> configCode = JSON.parseObject(String.valueOf(datum.get("configCode")));
                    shaDowJob.setCron(configCode.get("cron") == null ? null : configCode.get("cron").toString());
                    if (null != configCode.get("listener")) {
                        shaDowJob.setListenerName(configCode.get("listener") == null ? null : configCode.get("listener").toString());
                    }

                    String jobType = configCode.get("jobType") == null ? null : configCode.get("jobType").toString();
                    if (null != jobType) {
                        shaDowJob.setJobType(jobType);
                    }
                    String jobDataType = configCode.get("jobDataType") == null ? null : configCode.get("jobDataType").toString();
                    if (null != jobDataType) {
                        shaDowJob.setJobDataType(jobDataType);
                    }

                    String beanName = configCode.get("beanName") == null ? null : configCode.get("beanName").toString();
                    if (null != beanName) {
                        if (null == shaDowJob.getExtendParam()) {
                            shaDowJob.setExtendParam(new HashMap<String, Object>());
                        }
                        shaDowJob.getExtendParam().put("beanName", beanName);
                    }

                    shadowJobs.add(shaDowJob);
                }
            }
            GlobalConfig.getInstance().clearErrorRegisteredJobs();
            applicationConfig.setShadowJobs(shadowJobs);
        } catch (Throwable e) {
            LOGGER.error("SIMULATOR: get shadow job config from server with err response. url={}", url, e);
            ErrorReporter.buildError()
                    .setErrorType(ErrorTypeEnum.AgentError)
                    .setErrorCode("agent-0004")
                    .setMessage("获取影子job配置失败")
                    .setDetail("获取影子job配置失败,配置处理异常：" + e.getMessage())
                    .report();
            return false;
        }
        return true;
    }


    /**
     * 压测影子库，影子表数据读取 for 接入简化 -> 覆盖原配置
     *
     * @param troWebUrl
     */
    private boolean getPressureTable4AccessSimple(String troWebUrl, ApplicationConfig applicationConfig) {
        final String getShadowDatasourceUrl = troWebUrl + SHADOW_DB_TABLE_URL + "?appName=" + AppNameUtils.appName();
        try {
            HttpUtils.HttpResult httpResult = HttpUtils.doGet(getShadowDatasourceUrl);
            if (!httpResult.isSuccess()) {
                LOGGER.warn("SIMULATOR: [FetchConfig] get datasource config error. url={}, status={}, result={}"
                        , getShadowDatasourceUrl, httpResult.getStatus(), httpResult.getResult());
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("agent-0004")
                        .setMessage("获取影子数据源配置失败")
                        .setDetail("获取影子数据源配置失败,status: " + httpResult.getStatus() + ", result: " + httpResult.getResult())
                        .report();
                return false;
            }
            Map<String, Object> resultMap = JSON.parseObject(httpResult.getResult());
            Boolean success = (Boolean) resultMap.get("success");
            if (!success) {
                LOGGER.warn("SIMULATOR: [FetchConfig] get shadow job config from server with a fault response. url={}, status={}, result={}"
                        , getShadowDatasourceUrl, httpResult.getStatus(), httpResult.getResult());
                String error = resultMap.get("error") == null ? "" : resultMap.get("error").toString();
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("agent-0004")
                        .setMessage("获取影子数据源配置失败")
                        .setDetail("获取影子数据源配置失败,接口返回值为false,错误信息:" + error)
                        .report();
                return false;
            }


            if (!resultMap.containsKey("data")) {
                LOGGER.error("SIMULATOR: get shadow db config with a err response. can't found attributes data from response. url={}, status={}, result={}"
                        , getShadowDatasourceUrl, httpResult.getStatus(), httpResult.getResult());
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("agent-0004")
                        .setMessage("获取影子数据源配置失败")
                        .setDetail("获取影子数据源配置失败,接口返回值异常，无data属性")
                        .report();
                return false;
            }
            List<Map<String, Object>> dataMapList = null;
            try {
                dataMapList = (List<Map<String, Object>>) resultMap.get("data");
            } catch (Throwable e) {
                LOGGER.error("SIMULATOR: get shadow db config with a err response. can't convert attributes data to map from response. url={}", getShadowDatasourceUrl, e);
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("agent-0004")
                        .setMessage("获取影子数据源配置失败")
                        .setDetail("获取影子数据源配置失败,接口返回值异常，data不是kv对象数组：" + e.getMessage())
                        .report();
                return false;
            }
            /**
             * 如果拉取到的影子库配置为空不做强制校验，因为有一些应用是没有数据库访问的
             */
            if (dataMapList == null) {
                dataMapList = new ArrayList<Map<String, Object>>();
            }

            List<ShadowDatabaseConfig> configs = ShadowDatabaseConfigParser.getInstance().parse(dataMapList);
            /**
             * 转换成map
             * key: url | username
             * value: ShadowDatabaseConfig
             */
            Map<String, ShadowDatabaseConfig> map = new HashMap<String, ShadowDatabaseConfig>();
            for (ShadowDatabaseConfig shadowDatabaseConfig : configs) {
                /**
                 * 如果是 jndi，则不需要加用户名
                 */
                if (StringUtils.startsWith(shadowDatabaseConfig.getUrl(), "jndi:")) {
                    String url = StringUtils.substring(shadowDatabaseConfig.getUrl(), 5);
                    shadowDatabaseConfig.setUrl(url);
                    map.put(DbUrlUtils.getKey(shadowDatabaseConfig.getUrl(), null), shadowDatabaseConfig);
                } else {
                    map.put(DbUrlUtils.getKey(shadowDatabaseConfig.getUrl(), shadowDatabaseConfig.getUsername()), shadowDatabaseConfig);
                }
            }
            applicationConfig.setShadowDatabaseConfigs(map);

        } catch (Throwable e) {
            LOGGER.error("SIMULATOR: get shadow db config with a err response. got a unknow err. url={}", getShadowDatasourceUrl, e);
            ErrorReporter.buildError()
                    .setErrorType(ErrorTypeEnum.AgentError)
                    .setErrorCode("agent-0004")
                    .setMessage("获取影子数据源配置失败")
                    .setDetail("获取影子数据源配置失败,配置处理异常:" + e.getMessage())
                    .report();
            return false;
        }
        return true;
    }


    /**
     * 从服务端获取白名单列表
     */
    private boolean getWhiteList(String troWebUrl, ApplicationConfig applicationConfig) {
        final StringBuilder url = new StringBuilder(troWebUrl)
                .append(WHITELIST_FILE_URL)
                .append(userAppKey());
        try {
//                    .append("?appName=")
//                    .append(AppNameUtils.appName());

            return loadList(url, applicationConfig);
        } catch (Throwable e) {
            LOGGER.error("SIMULATOR: [FetchConfig] get whitelist config error. url={}", troWebUrl, e);
            ErrorReporter.buildError()
                    .setErrorType(ErrorTypeEnum.AgentError)
                    .setErrorCode("agent-0004")
                    .setMessage("获取白名单列表失败")
                    .setDetail("获取白名单列表失败:" + e.getMessage())
                    .report();
            return false;
        }
    }

    /**
     * 加载配置列表
     *
     * @param url
     * @param applicationConfig
     */
    private boolean loadList(final StringBuilder url,
                             ApplicationConfig applicationConfig) {
        final HttpUtils.HttpResult httpResult = HttpUtils.doGet(url.toString());
        if (!httpResult.isSuccess()) {
            LOGGER.warn("SIMULATOR: [FetchConfig] get whitelist config error. status: {}, result: {}", httpResult.getStatus(), httpResult.getResult());
            ErrorReporter.buildError()
                    .setErrorType(ErrorTypeEnum.AgentError)
                    .setErrorCode("agent-0004")
                    .setMessage("获取白名单列表失败")
                    .setDetail("获取白名单列表失败,status: " + httpResult.getStatus() + ", result: " + httpResult.getResult())
                    .report();
            return false;
        }

        final Set<String> urlWarList = new HashSet<String>();
        final Set<String> mqList = new HashSet<String>();
        final Set<String> rpcClassMethodName = new HashSet<String>();
        final Set<String> redisKeyWhiteList = new HashSet<String>();
        final Set<String> blockList = new HashSet<String>();
        final Set<String> searchWhiteList = new HashSet<String>();

        JSONObject dataMap = JSON.parseObject(httpResult.getResult());
        JSONObject dataObj = dataMap.getJSONObject(DATA);
        JSONArray blackList = dataObj.getJSONArray(B_LISTS);
        for (int i = 0; i < blackList.size(); i++) {
            JSONObject blackMap = blackList.getJSONObject(i);
            Object keyObj = blackMap.get(REDIS_KEY);
            redisKeyWhiteList.add(String.valueOf(keyObj));
        }
        final JSONArray whitelist = dataObj.getJSONArray(W_LISTS);
        for (int i = 0; i < whitelist.size(); i++) {
            final JSONObject jsonObject1 = whitelist.getJSONObject(i);
            final String name = StringUtils.trim(jsonObject1.getString(INTERFACE_NAME));
            final String type = jsonObject1.getString(TYPE2);
            boolean isGlobal = true;
            if (jsonObject1.containsKey(GLOBAL)) {
                isGlobal = jsonObject1.getBoolean(GLOBAL);
            }
            JSONArray appNames = null;
            if (jsonObject1.containsKey(APP_NAMES)) {
                appNames = jsonObject1.getJSONArray(APP_NAMES);
            }
            /**
             * 如果是部分生效，但是生效的应用中未包含当前应用，则忽略当前这份配置
             */
            if (!isGlobal && (appNames == null || !appNames.contains(AppNameUtils.appName()))) {
                continue;
            }

            if (HTTP.equals(type)) {
                if (name.startsWith("mq:")) {
                    mqList.add(name.substring(3));
                } else if (name.startsWith("rabbitmq:")) {
                    mqList.add(name.substring(9));
                } else if (name.startsWith("search:")) {
                    searchWhiteList.add(name.substring(7));
                } else {
                    urlWarList.add(name);
                }
            } else if (DUBBO.equals(type)) {
                rpcClassMethodName.add(name);
            } else if (RPC.equals(type)) {
                rpcClassMethodName.add(name);
            } else if (MQ.equals(type)) {
                mqList.add(name);
            } else if (SEARCH.equals(type)) {
                searchWhiteList.add(name);
            } else if ("block".equals(type)) {
                blockList.add(name);
            }
        }
        applicationConfig.setUrlWhiteList(urlWarList);
        applicationConfig.setRpcNameWhiteList(rpcClassMethodName);
        applicationConfig.setCacheKeyAllowList(redisKeyWhiteList);
        applicationConfig.setContextPathBlockList(blockList);
        applicationConfig.setMqList(mqList);
        applicationConfig.setSearchWhiteList(searchWhiteList);
        return true;
    }

    private String getAgentVersion() {
        if (StringUtils.isBlank(VERSION)) {
            String version = System.getProperty(PRADAR_AGENT_VERSION_CONFIG_ENV);
            if (StringUtils.isNotBlank(version)) {
                VERSION = version;
            }
        }
        return VERSION;
    }

    private static String userAppKey() {
        String key = System.getProperty(Pradar.USER_APP_KEY);
        if (key == null || key.isEmpty()) {
            return null;
        } else {
            return key;
        }
    }


    /**
     * Hbase压测影子库，影子表数据读取 for 接入简化 -> 覆盖原配置
     *
     * @param troWebUrl
     */
    private boolean getHbaseShadowConfig(String troWebUrl, ApplicationConfig applicationConfig) {
        final String url = troWebUrl + SHADOW_HBASE_SERVER_URL + "?appName=" + AppNameUtils.appName();
        try {
            HttpUtils.HttpResult result = HttpUtils.doGet(url);

            if (!result.isSuccess()) {
                LOGGER.error("pull shadow hbase config error {}, url={}", result.getResult(), troWebUrl + SHADOW_HBASE_SERVER_URL + "?appName=" + AppNameUtils.appName());
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("agent-0010")
                        .setMessage("获取影子数据源配置失败")
                        .setDetail("获取影子数据源配置失败,接口返回值为null")
                        .report();
                return false;
            }
            Map<String, Object> resultMap = JSONObject.parseObject(result.getResult());
            Boolean success = (Boolean) resultMap.get("success");
            if (!success) {
                String error = resultMap.get("error") == null ? "" : resultMap.get("error").toString();
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("agent-0010")
                        .setMessage("获取影子Hbase数据源配置失败")
                        .setDetail("获取影子Hbase数据源配置失败,接口返回值为false,错误信息:" + error)
                        .report();
                return false;
            }


            if (!resultMap.containsKey("data")) {
                LOGGER.error("[pradar] get shadow hbase config with a err response. can't found attributes data from response. url={}", url);
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("agent-0010")
                        .setMessage("获取影子Hbase数据源配置失败")
                        .setDetail("获取影子Hbase数据源配置失败,接口返回值异常，无data属性")
                        .report();
                return false;
            }
            List<Map<String, Object>> dataMapList = null;
            try {
                dataMapList = (List<Map<String, Object>>) resultMap.get("data");
            } catch (Exception e) {
                LOGGER.error("[pradar] get shadow hbase config with a err response. can't convert attributes data to map from response. url={}", url, e);
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("agent-0010")
                        .setMessage("获取影子Hbase数据源配置失败")
                        .setDetail("获取影子Hbase数据源配置失败,接口返回值异常，data不是kv对象数组：" + e.getMessage())
                        .report();
                return false;
            }

            Map<String, ShadowHbaseConfig> shadowHbaseConfigMap = new HashMap<String, ShadowHbaseConfig>();
            ;
            for (Map<String, Object> map : dataMapList) {
                Map<String, Object> configMap = JSONObject.parseObject((String) map.get("config"));
                if (configMap != null) {
                    Map<String, Object> business = (Map<String, Object>) configMap.get("dataSourceBusiness");
                    Map<String, Object> performance = (Map<String, Object>) configMap.get("dataSourcePerformanceTest");
                    if (performance != null && business != null) {
                        ShadowHbaseConfig bconfig = convertConfig(business);
                        ShadowHbaseConfig pconfig = convertConfig(performance);
                        shadowHbaseConfigMap.put(bconfig.toKey(), pconfig);
                    }
                }
            }

            if (shadowHbaseConfigMap.size() > 0) {
                GlobalConfig.getInstance().setShadowHbaseServer(true);
                ;
            }

            applicationConfig.setShadowHbaseConfigs(shadowHbaseConfigMap);

        } catch (Throwable e) {
            LOGGER.error("[pradar] get shadow hbase config with a err response. got a unknow err. url={}", url, e);
            ErrorReporter.buildError()
                    .setErrorType(ErrorTypeEnum.AgentError)
                    .setErrorCode("agent-0010")
                    .setMessage("获取影子Hbase数据源配置失败")
                    .setDetail("获取影子Hbase数据源配置失败,配置处理异常:" + e.getMessage())
                    .report();
            return false;
        }
        return true;
    }

    public ShadowHbaseConfig convertConfig(Map<String, Object> business) {
        ShadowHbaseConfig config = new ShadowHbaseConfig();
        String bquorum = (String) business.get("quorum");
        String bport = (String) business.get("port");
        String bznode = (String) business.get("znode");
        Map<String, String> bparams = (Map<String, String>) business.get("params");
        config.setQuorum(bquorum);
        config.setPort(bport);
        config.setZnode(bznode);
        config.setParams(bparams);

        if (bparams != null) {
            String token = bparams.get("hbase.sf.token");
            if (token != null) {
                config.setToken(token);
            }
            String username = bparams.get("hbase.sf.username");
            if (username != null) {
                config.setUsername(username);
            }
        }

        return config;
    }
}
