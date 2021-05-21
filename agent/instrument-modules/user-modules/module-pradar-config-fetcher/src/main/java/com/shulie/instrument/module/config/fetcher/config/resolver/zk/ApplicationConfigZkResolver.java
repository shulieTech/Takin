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
package com.shulie.instrument.module.config.fetcher.config.resolver.zk;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.pamirs.pradar.*;
import com.pamirs.pradar.common.HttpUtils;
import com.pamirs.pradar.exception.PradarException;
import com.pamirs.pradar.internal.config.ShadowDatabaseConfig;
import com.pamirs.pradar.pressurement.agent.shared.service.ErrorReporter;
import com.pamirs.pradar.pressurement.base.custominterface.AppInterfaceDomain;
import com.pamirs.pradar.pressurement.base.util.PropertyUtil;
import com.pamirs.pradar.pressurement.datasource.util.DbUrlUtils;
import com.shulie.druid.support.json.JSONUtils;
import com.shulie.instrument.module.config.fetcher.config.AbstractConfig;
import com.shulie.instrument.module.config.fetcher.config.event.FIELDS;
import com.shulie.instrument.module.config.fetcher.config.impl.ApplicationConfig;
import com.shulie.instrument.module.register.zk.ZkNodeCache;
import com.shulie.instrument.module.register.zk.ZkPathChildrenCache;
import com.shulie.instrument.simulator.api.executors.ExecutorServiceFactory;
import com.shulie.instrument.simulator.api.util.CollectionUtils;
import io.shulie.tro.web.config.entity.AllowList;
import io.shulie.tro.web.config.entity.ShadowDB;
import io.shulie.tro.web.config.entity.ShadowJob;
import io.shulie.tro.web.config.enums.AllowListType;
import io.shulie.tro.web.config.enums.BlockListType;
import io.shulie.tro.web.config.enums.ShadowDBType;
import io.shulie.tro.web.config.sync.zk.constants.ZkConfigPathConstants;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author shiyajian
 * create: 2020-08-11
 */
public class ApplicationConfigZkResolver extends AbstractZkResolver<ApplicationConfig> {
    private final static Logger logger = LoggerFactory.getLogger(ApplicationConfigZkResolver.class);

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
    private final String VERSION = ApplicationConfigZkResolver.class.getPackage().getImplementationVersion();
    private static final String NODE_UNIQUE_KEY = UUID.randomUUID().toString().replace("_", "");

    private Map<String, List<AllowList>> allowLists;

    private Map<String, Set<String>> ignoreListMap;
    private List<ShadowDB> shadowDBList;
    private List<ShadowJob> shadowJobList;
    private AbstractConfig config;

    private boolean checkAndGenerate = false;
    private ScheduledFuture future;

    public ApplicationConfigZkResolver(ZookeeperOptions options) {
        super(options);
        this.allowLists = new ConcurrentHashMap<String, List<AllowList>>();
        this.ignoreListMap = new ConcurrentHashMap<String, Set<String>>();
        this.shadowDBList = Collections.EMPTY_LIST;
        this.shadowJobList = Collections.EMPTY_LIST;
    }

    private void trigger(FIELDS... fields) {
        if (fields == null || fields.length == 0) {
            return;
        }

        this.config.trigger(fields);
    }

    @Override
    protected void init(AbstractConfig config) {
        this.config = config;

        addAllowListListener(ExecutorServiceFactory.GLOBAL_EXECUTOR_SERVICE);
        addBlockListener(ExecutorServiceFactory.GLOBAL_EXECUTOR_SERVICE);
        addGuardListener(ExecutorServiceFactory.GLOBAL_EXECUTOR_SERVICE);
        addShadowDbListener(ExecutorServiceFactory.GLOBAL_EXECUTOR_SERVICE);
        addShadowJobListener(ExecutorServiceFactory.GLOBAL_EXECUTOR_SERVICE);

        future = ExecutorServiceFactory.GLOBAL_SCHEDULE_EXECUTOR_SERVICE.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    // 获取配置
                    String troControlWebUrl = PropertyUtil.getTroControlWebUrl();
                    /**
                     * 检查并自动生成应用,如果自动生成应用失败可以忽略
                     * 只需要上传一次
                     */
                    checkAndGenerateApp(troControlWebUrl);
                    /**
                     * 上传应用接入状态,如果上报失败可以忽略
                     * 异常列表
                     */
                    uploadAccessStatus(troControlWebUrl);

                    /**
                     * 上传应用信息,如果失败可以忽略
                     */
                    uploadAppInfo(troControlWebUrl);
                } catch (Throwable e) {
                    logger.error("scheduled application config zk upload access err!", e);
                }
            }
        }, 1, 5, TimeUnit.SECONDS);


    }

    /**
     * 上传应用信息,先判断是否需要上传
     *
     * @param troWebUrl
     */
    private void uploadAppInfo(String troWebUrl) {
        final AppInterfaceDomain appInfo = new AppInterfaceDomain();
        try {
            appInfo.setAppName(AppNameUtils.appName());
            final StringBuilder url = new StringBuilder(troWebUrl + UPLOAD);
            final Map param = new HashMap();
            param.put("appName", AppNameUtils.appName());
            param.put("size", appInfo.getAppDetails().size() + "");
            final HttpUtils.HttpResult httpResult = HttpUtils.doPost(url.toString(), JSONUtils.toJSONString(param));
            if (!httpResult.isSuccess()) {
                logger.warn("SIMULATOR: upload app info error. status: {}, result: {}", httpResult.getStatus(), httpResult.getResult());
                return;
            }
            if (httpResult.getResult() != null && httpResult.getResult().contains("data=true")) {
                final StringBuilder url2 = new StringBuilder(troWebUrl).append(UPLOAD_APP_INFO);
                HttpUtils.HttpResult httpResult1 = HttpUtils.doPost(url2.toString(), JSONUtils.toJSONString(appInfo));
                if(!httpResult1.isSuccess()) {
                    logger.warn("上传应用信息失败: {}", httpResult1.getResult());
                }
            }
        } catch (Throwable e) {
            logger.error("upload app info failed:", e);
        }
        try {
            final String projectName = AppNameUtils.appName();
            appInfo.setAppName(projectName);
            final StringBuilder url = new StringBuilder().append(troWebUrl).append(AGENT_VERSION)
                    .append("?appName=").append(projectName).append("&agentVersion=")
                    .append(VERSION).append("&pradarVersion=").append(VERSION);
            HttpUtils.doGet(url.toString());
        } catch (Throwable e) {
            logger.error("upload agent version info failed:", e);
        }
    }

    /**
     * 定时上报接入状态
     *
     * @param troWeb
     */
    private void uploadAccessStatus(String troWeb) {
        if (ErrorReporter.getInstance().getErrors().isEmpty()) {
            return;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("nodeKey", NODE_UNIQUE_KEY);
        result.put("agentId", Pradar.getAgentId());

        result.put("applicationName", AppNameUtils.appName());
        Map<String, Object> errorList = ErrorReporter.getInstance().getErrors();
        result.put("switchErrorMap", errorList);
        final StringBuilder url = new StringBuilder(troWeb).append(UPLOAD_ACCESS_STATUS);
        try {
            HttpUtils.HttpResult httpResult = HttpUtils.doPost(url.toString(), JSONUtils.toJSONString(result));
            if(!httpResult.isSuccess()) {
                logger.warn("上传应用接入状态失败: {}", httpResult.getResult());
            }
            // TODO 存在一个隐患，去除了清空内存中异常信息，改为agent全量发送异常数据
            // 内存消耗会加重，等tro控制台调整异常展示时同步调整
            ErrorReporter.getInstance().clear(errorList);
        } catch (Throwable e) {
            logger.warn("上传应用接入状态信息报错", e);
        }
    }

    /**
     * 上报控制台，自动生成项目
     *
     * @param troWeb
     */
    private void checkAndGenerateApp(String troWeb) {
        //上传成功，只要一次
        if (checkAndGenerate) {
            return;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(APP_COLUMN_APPLICATION_NAME, AppNameUtils.appName());
        map.put(APP_COLUMN_DDL_PATH, AppNameUtils.appName() + "/ddl.sh");
        map.put(APP_COLUMN_CLEAN_PATH, AppNameUtils.appName() + "/clean.sh");
        map.put(APP_COLUMN_READY_PATH, AppNameUtils.appName() + "/ready.sh");
        map.put(APP_COLUMN_BASIC_PATH, AppNameUtils.appName() + "/basic.sh");
        map.put(APP_COLUMN_CACHE_PATH, AppNameUtils.appName() + "/cache.sh");
        final StringBuilder url = new StringBuilder(troWeb).append(APP_INSERT_URL);
        try {
            HttpUtils.HttpResult httpResult = HttpUtils.doPost(url.toString(), JSONUtils.toJSONString(map));
            if (httpResult.isSuccess()) {
                checkAndGenerate = true;
            }
        } catch (Throwable e) {
            logger.warn("自动增加应用失败", e);
        }
    }

    /**
     * 添加影子job监听器
     *
     * @param executor
     */
    private void addShadowJobListener(final ExecutorService executor) {
        String content = addNodeListener(executor, ZkConfigPathConstants.NAME_SPACE + '/' + Pradar.PRADAR_USER_KEY + ZkConfigPathConstants.SHADOW_JOB_PARENT_PATH, new NodeListener() {
            @Override
            public void onListener(String path, String content) {
                if (StringUtils.isNotBlank(content)) {
                    shadowJobList = JSON.parseObject(content, new TypeReference<List<ShadowJob>>() {
                    }.getType());
                }
                trigger(FIELDS.SHADOW_JOB_CONFIGS);
            }
        });
        if (StringUtils.isNotBlank(content)) {
            shadowJobList = JSON.parseObject(content, new TypeReference<List<ShadowJob>>() {
            }.getType());
        }
        trigger(FIELDS.SHADOW_JOB_CONFIGS);
    }

    /**
     * 添加影子库表监听器, 只需要监听当前应用的path即可
     *
     * @param executor
     */
    private void addShadowDbListener(final ExecutorService executor) {
        String shadowDbPath = ZkConfigPathConstants.NAME_SPACE + '/' + Pradar.PRADAR_USER_KEY + ZkConfigPathConstants.SHADOW_DB_PARENT_PATH + '/' + AppNameUtils.appName();
        String content = addNodeListener(executor, shadowDbPath, new NodeListener() {
            @Override
            public void onListener(String path, String content) {
                if (StringUtils.isNotBlank(content)) {
                    shadowDBList = JSON.parseObject(content, new TypeReference<List<ShadowDB>>() {
                    }.getType());
                }
                trigger(FIELDS.SHADOW_DATABASE_CONFIGS);
            }
        });

        if (StringUtils.isNotBlank(content)) {
            shadowDBList = JSON.parseObject(content, new TypeReference<List<ShadowDB>>() {
            }.getType());
        }
        trigger(FIELDS.SHADOW_DATABASE_CONFIGS);
    }

    /**
     * 添加挡板监听器
     *
     * @param executor
     */
    private void addGuardListener(final ExecutorService executor) {
        //这个暂时还是采取拉取的方式

    }

    /**
     * 添加黑名单监听器
     *
     * @param executor
     */
    private void addBlockListener(final ExecutorService executor) {
        for (final BlockListType blockListType : BlockListType.values()) {
            String content = addNodeListener(executor, ZkConfigPathConstants.NAME_SPACE + '/' + Pradar.PRADAR_USER_KEY + ZkConfigPathConstants.BLOCK_LIST_PARENT_PATH + '/' + blockListType.name().toLowerCase(), new NodeListener() {
                @Override
                public void onListener(String path, String content) {
                    if (StringUtils.isNotBlank(content)) {
                        Set<String> blockLists = JSON.parseObject(content, new TypeReference<Set<String>>() {
                        }.getType());
                        ignoreListMap.put(blockListType.name().toLowerCase(), blockLists);
                    }
                    if (blockListType == BlockListType.SEARCH) {
                        trigger(FIELDS.SEARCH_KEY_WHITE_LIST);
                    } else if (blockListType == BlockListType.CACHE) {
                        trigger(FIELDS.CONTEXT_PATH_BLOCK_LIST);
                    } else if (blockListType == BlockListType.MQ) {

                    }
                }
            });

            if (StringUtils.isNotBlank(content)) {
                Set<String> blockLists = JSON.parseObject(content, new TypeReference<Set<String>>() {
                }.getType());
                ignoreListMap.put(blockListType.name().toLowerCase(), blockLists);
            }
            if (blockListType == BlockListType.SEARCH) {
                trigger(FIELDS.SEARCH_KEY_WHITE_LIST);
            } else if (blockListType == BlockListType.CACHE) {
                trigger(FIELDS.CONTEXT_PATH_BLOCK_LIST);
            } else if (blockListType == BlockListType.MQ) {

            }
        }


    }

    /**
     * 添加白名单监听器
     *
     * @param executor
     */
    private void addAllowListListener(final ExecutorService executor) {
        List<String> list = addPathListener(executor, ZkConfigPathConstants.NAME_SPACE + '/' + Pradar.PRADAR_USER_KEY + ZkConfigPathConstants.ALLOW_LIST_PARENT_PATH, new PathListener() {
            @Override
            public void onListen(String path, List<String> allList, List<String> addList, List<String> deleteList) {
                for (String delete : deleteList) {
                    final String key = ZkConfigPathConstants.NAME_SPACE + '/' + Pradar.PRADAR_USER_KEY + ZkConfigPathConstants.ALLOW_LIST_PARENT_PATH + '/' + delete;
                    Object object = nodeCaches.remove(key);
                    if (object != null && object instanceof ZkNodeCache) {
                        try {
                            ((ZkNodeCache) object).stop();
                        } catch (Throwable e) {
                            logger.error("zk-config-fetcher stop zk node err!{}", ((ZkNodeCache) object).getPath(), e);
                        }
                    } else if (object != null && object instanceof ZkPathChildrenCache) {
                        try {
                            ((ZkPathChildrenCache) object).stop();
                        } catch (Throwable e) {
                            logger.error("zk-config-fetcher stop zk node err!{}", ((ZkPathChildrenCache) object).getPath(), e);
                        }
                    }
                    allowLists.remove(key);
                }

                for (String add : addList) {
                    final String key = ZkConfigPathConstants.NAME_SPACE + '/' + Pradar.PRADAR_USER_KEY + ZkConfigPathConstants.ALLOW_LIST_PARENT_PATH + '/' + add;
                    String content = addNodeListener(executor, key, new NodeListener() {
                        @Override
                        public void onListener(String path, String content) {
                            if (StringUtils.isNotBlank(content)) {
                                List<AllowList> list = JSON.parseObject(content, new TypeReference<List<AllowList>>() {
                                }.getType());
                                allowLists.put(path, list);
                            }
                            trigger(FIELDS.DUBBO_ALLOW_LIST, FIELDS.CACHE_KEY_ALLOW_LIST, FIELDS.URL_WHITE_LIST);
                        }
                    });
                    try {
                        if (StringUtils.isNotBlank(content)) {
                            List<AllowList> list = JSON.parseObject(content, new TypeReference<List<AllowList>>() {
                            }.getType());
                            allowLists.put(key, list);
                        }
                        trigger(FIELDS.DUBBO_ALLOW_LIST, FIELDS.CACHE_KEY_ALLOW_LIST, FIELDS.URL_WHITE_LIST);
                    } catch (Throwable e) {
                        ErrorReporter.buildError()
                                .setErrorType(ErrorTypeEnum.AgentError)
                                .setErrorCode("agent-1002")
                                .setMessage("从ZK获取白名单配置失败")
                                .setDetail("从ZK获取白名单配置失败:" + e.getMessage())
                                .closePradar(ConfigNames.CLUSTER_TEST_READY_CONFIG)
                                .report();
                        logger.error("[config-refresh] config refresh err, path:{} ,data:{}", key, content);
                    }
                }
            }
        });

        for (String node : list) {
            String key = ZkConfigPathConstants.NAME_SPACE + '/' + Pradar.PRADAR_USER_KEY + ZkConfigPathConstants.ALLOW_LIST_PARENT_PATH + '/' + node;
            String content = addNodeListener(executor, key, new NodeListener() {
                @Override
                public void onListener(String path, String content) {
                    if (StringUtils.isNotBlank(content)) {
                        List<AllowList> list = JSON.parseObject(content, new TypeReference<List<AllowList>>() {
                        }.getType());
                        allowLists.put(path, list);
                    }
                    trigger(FIELDS.DUBBO_ALLOW_LIST, FIELDS.CACHE_KEY_ALLOW_LIST, FIELDS.URL_WHITE_LIST);
                }
            });
            try {
                if (StringUtils.isNotBlank(content)) {
                    List<AllowList> allows = JSON.parseObject(content, new TypeReference<List<AllowList>>() {
                    }.getType());
                    allowLists.put(key, allows);
                }
            } catch (Throwable e) {
                logger.error("[config-refresh] config refresh err, path:{} ,data:{}", key, content);
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.AgentError)
                        .setErrorCode("agent-1002")
                        .setMessage("从ZK获取白名单配置失败")
                        .setDetail("从ZK获取白名单配置失败")
                        .closePradar(ConfigNames.CLUSTER_TEST_READY_CONFIG)
                        .report();
                //如果初始化出现错误则直接抛出错误
                throw new PradarException(e);
            }
        }
        trigger(FIELDS.DUBBO_ALLOW_LIST, FIELDS.CACHE_KEY_ALLOW_LIST, FIELDS.URL_WHITE_LIST);
    }

    @Override
    public void triggerFetch(ApplicationConfig applicationConfig) {

    }

    @Override
    public ApplicationConfig fetch() {
        ApplicationConfig.getPressureTable4AccessSimple = true;
        ApplicationConfig.getWhiteList = true;
        ApplicationConfig.getShadowJobConfig = true;
        ApplicationConfig applicationConfig = new ApplicationConfig(this);
        Set<String> urlWhiteList = new HashSet<String>();
        for (Map.Entry<String, List<AllowList>> entry : allowLists.entrySet()) {
            for (AllowList allowList : entry.getValue()) {
                if (allowList.getType() == AllowListType.HTTP) {
                    urlWhiteList.add(allowList.getInterfaceName());
                }
            }
        }
        applicationConfig.setUrlWhiteList(urlWhiteList);
        Set<String> dubboWhiteList = new HashSet<String>();
        for (Map.Entry<String, List<AllowList>> entry : allowLists.entrySet()) {
            for (AllowList allowList : entry.getValue()) {
                if (allowList.getType() == AllowListType.DUBBO) {
                    dubboWhiteList.add(allowList.getInterfaceName());
                }
            }
        }
        applicationConfig.setRpcNameWhiteList(dubboWhiteList);
        applicationConfig.setCacheKeyAllowList(ignoreListMap.get(BlockListType.CACHE.name().toLowerCase()));
        applicationConfig.setSearchWhiteList(ignoreListMap.get(BlockListType.SEARCH.name().toLowerCase()));
        Map<String, ShadowDatabaseConfig> shadowDatabaseConfigMap = new HashMap<String, ShadowDatabaseConfig>();
        for (ShadowDB shadowDB : shadowDBList) {
            ShadowDatabaseConfig shadowDatabaseConfig = new ShadowDatabaseConfig();
            shadowDatabaseConfig.setUrl(shadowDB.getBizJdbcUrl());
            shadowDatabaseConfig.setUsername(shadowDB.getBizUserName());
            shadowDatabaseConfig.setDsType(shadowDB.getType() == ShadowDBType.SCHEMA ? 0 : 1);
            if (shadowDB.getType() == ShadowDBType.SCHEMA) {
                shadowDatabaseConfig.setShadowSchema(shadowDB.getShadowSchemaConfig().getSchema());
                shadowDatabaseConfig.setShadowDriverClassName(shadowDB.getShadowSchemaConfig().getDriverClassName());
                shadowDatabaseConfig.setShadowUsername(shadowDB.getShadowSchemaConfig().getUsername());
                shadowDatabaseConfig.setShadowUrl(shadowDB.getShadowSchemaConfig().getUrl());
                shadowDatabaseConfig.setShadowPassword(shadowDB.getShadowSchemaConfig().getPassword());
                shadowDatabaseConfig.setProperties(shadowDB.getShadowSchemaConfig().getProperties());
            } else {
                Map<String, String> mappingTables = new HashMap<String, String>();
                for (String tableName : shadowDB.getShadowTableConfig().getTableNames()) {
                    mappingTables.put(tableName, Pradar.addClusterTestPrefix(tableName));
                }
                shadowDatabaseConfig.setBusinessShadowTables(mappingTables);
            }
            /**
             * 如果是 jndi，则不需要加用户名
             */
            if (StringUtils.startsWith(shadowDatabaseConfig.getUrl(), "jndi:")) {
                String url = StringUtils.substring(shadowDatabaseConfig.getUrl(), 5);
                shadowDatabaseConfig.setUrl(url);
                shadowDatabaseConfigMap.put(DbUrlUtils.getKey(shadowDatabaseConfig.getUrl(), null), shadowDatabaseConfig);
            } else {
                shadowDatabaseConfigMap.put(DbUrlUtils.getKey(shadowDatabaseConfig.getUrl(), shadowDatabaseConfig.getUsername()), shadowDatabaseConfig);
            }
        }
        applicationConfig.setShadowDatabaseConfigs(shadowDatabaseConfigMap);
        Set<com.pamirs.pradar.internal.config.ShadowJob> shadowJobs = new HashSet<com.pamirs.pradar.internal.config.ShadowJob>();
        for (ShadowJob shadowJob : shadowJobList) {
            com.pamirs.pradar.internal.config.ShadowJob jobConfig = new com.pamirs.pradar.internal.config.ShadowJob();
            jobConfig.setClassName(shadowJob.getClassName());
            jobConfig.setCron(shadowJob.getCron());
            jobConfig.setJobDataType(shadowJob.getJobDataType());
            jobConfig.setJobType(shadowJob.getType().name().toLowerCase());
            jobConfig.setListenerName(shadowJob.getListener());
            shadowJobs.add(jobConfig);
        }
        applicationConfig.setShadowJobs(shadowJobs);

        if (PradarSwitcher.configSyncSwitchOn()
                || (ApplicationConfig.getWhiteList && ApplicationConfig.getPressureTable4AccessSimple && ApplicationConfig.getShadowJobConfig)) {
            // 配置拉取过程中，配置无异常
            // 或者历史配置有完成拉取的记录
            PradarSwitcher.clusterTestReady();
        } else {
            // 本次配置没有成功拉取 并且 历史无成功拉取配置
            // 中断压测
            PradarSwitcher.clusterTestPrepare();
        }
        return applicationConfig;
    }

    @Override
    public ApplicationConfig fetch(FIELDS... fields) {
        ApplicationConfig.getPressureTable4AccessSimple = true;
        ApplicationConfig.getWhiteList = true;
        ApplicationConfig.getShadowJobConfig = true;
        ApplicationConfig applicationConfig = new ApplicationConfig(this);
        for (FIELDS f : fields) {
            switch (f) {
                case URL_WHITE_LIST:
                    Set<String> urlWhiteList = new HashSet<String>();
                    for (Map.Entry<String, List<AllowList>> entry : allowLists.entrySet()) {
                        if (entry.getValue() != null) {
                            for (AllowList allowList : entry.getValue()) {
                                if (allowList.getType() == AllowListType.HTTP) {
                                    urlWhiteList.add(allowList.getInterfaceName());
                                }
                            }
                        }
                    }
                    applicationConfig.setUrlWhiteList(urlWhiteList);
                    break;
                case DUBBO_ALLOW_LIST:
                    Set<String> dubboWhiteList = new HashSet<String>();
                    for (Map.Entry<String, List<AllowList>> entry : allowLists.entrySet()) {
                        if (entry.getValue() != null) {
                            for (AllowList allowList : entry.getValue()) {
                                if (allowList.getType() == AllowListType.DUBBO) {
                                    dubboWhiteList.add(allowList.getInterfaceName());
                                }
                            }
                        }
                    }
                    applicationConfig.setRpcNameWhiteList(dubboWhiteList);
                    break;
                case CACHE_KEY_ALLOW_LIST:
                    applicationConfig.setCacheKeyAllowList(ignoreListMap.get(BlockListType.CACHE.name().toLowerCase()));
                    break;
                case SEARCH_KEY_WHITE_LIST:
                    applicationConfig.setSearchWhiteList(ignoreListMap.get(BlockListType.SEARCH.name().toLowerCase()));
                    break;
                case SHADOW_DATABASE_CONFIGS:
                    Map<String, ShadowDatabaseConfig> shadowDatabaseConfigMap = new HashMap<String, ShadowDatabaseConfig>();
                    if (CollectionUtils.isNotEmpty(shadowDBList)) {
                        for (ShadowDB shadowDB : shadowDBList) {
                            ShadowDatabaseConfig shadowDatabaseConfig = new ShadowDatabaseConfig();
                            shadowDatabaseConfig.setUrl(shadowDB.getBizJdbcUrl());
                            shadowDatabaseConfig.setUsername(shadowDB.getBizUserName());
                            shadowDatabaseConfig.setDsType(shadowDB.getType() == ShadowDBType.SCHEMA ? 0 : 1);
                            if (shadowDB.getType() == ShadowDBType.SCHEMA) {
                                shadowDatabaseConfig.setShadowSchema(shadowDB.getShadowSchemaConfig().getSchema());
                                shadowDatabaseConfig.setShadowDriverClassName(shadowDB.getShadowSchemaConfig().getDriverClassName());
                                shadowDatabaseConfig.setShadowUsername(shadowDB.getShadowSchemaConfig().getUsername());
                                shadowDatabaseConfig.setShadowUrl(shadowDB.getShadowSchemaConfig().getUrl());
                                shadowDatabaseConfig.setShadowPassword(shadowDB.getShadowSchemaConfig().getPassword());
                                shadowDatabaseConfig.setProperties(shadowDB.getShadowSchemaConfig().getProperties());
                            } else {
                                Map<String, String> mappingTables = new HashMap<String, String>();
                                for (String tableName : shadowDB.getShadowTableConfig().getTableNames()) {
                                    mappingTables.put(tableName, Pradar.addClusterTestPrefix(tableName));
                                }
                                shadowDatabaseConfig.setBusinessShadowTables(mappingTables);
                            }
                            /**
                             * 如果是 jndi，则不需要加用户名
                             */
                            if (StringUtils.startsWith(shadowDatabaseConfig.getUrl(), "jndi:")) {
                                String url = StringUtils.substring(shadowDatabaseConfig.getUrl(), 5);
                                shadowDatabaseConfig.setUrl(url);
                                shadowDatabaseConfigMap.put(DbUrlUtils.getKey(shadowDatabaseConfig.getUrl(), null), shadowDatabaseConfig);
                            } else {
                                shadowDatabaseConfigMap.put(DbUrlUtils.getKey(shadowDatabaseConfig.getUrl(), shadowDatabaseConfig.getUsername()), shadowDatabaseConfig);
                            }
                        }
                    }
                    applicationConfig.setShadowDatabaseConfigs(shadowDatabaseConfigMap);
                    break;
                case SHADOW_JOB_CONFIGS:
                    Set<com.pamirs.pradar.internal.config.ShadowJob> shadowJobs = new HashSet<com.pamirs.pradar.internal.config.ShadowJob>();
                    if (CollectionUtils.isNotEmpty(shadowJobList)) {
                        for (ShadowJob shadowJob : shadowJobList) {
                            com.pamirs.pradar.internal.config.ShadowJob jobConfig = new com.pamirs.pradar.internal.config.ShadowJob();
                            jobConfig.setClassName(shadowJob.getClassName());
                            jobConfig.setCron(shadowJob.getCron());
                            jobConfig.setJobDataType(shadowJob.getJobDataType());
                            jobConfig.setJobType(shadowJob.getType().name().toLowerCase());
                            jobConfig.setListenerName(shadowJob.getListener());
                            shadowJobs.add(jobConfig);
                        }
                    }
                    applicationConfig.setShadowJobs(shadowJobs);
                    break;
            }
        }

        if (PradarSwitcher.configSyncSwitchOn()
                || (ApplicationConfig.getWhiteList && ApplicationConfig.getPressureTable4AccessSimple && ApplicationConfig.getShadowJobConfig)) {
            // 配置拉取过程中，配置无异常
            // 或者历史配置有完成拉取的记录
            PradarSwitcher.clusterTestReady();
        } else {
            // 本次配置没有成功拉取 并且 历史无成功拉取配置
            // 中断压测
            PradarSwitcher.clusterTestPrepare();
        }
        return applicationConfig;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (future != null && !future.isDone() && !future.isCancelled()) {
            future.cancel(true);
        }
    }
}
