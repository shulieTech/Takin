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

package io.shulie.tro.web.app.init.sync;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.alibaba.excel.util.StringUtils;

import com.google.common.collect.Lists;
import com.pamirs.tro.entity.dao.confcenter.TApplicationMntDao;
import com.pamirs.tro.entity.dao.confcenter.TBListMntDao;
import com.pamirs.tro.entity.dao.user.TUserMapper;
import com.pamirs.tro.entity.domain.entity.DsModelWithBLOBs;
import com.pamirs.tro.entity.domain.entity.LinkGuardEntity;
import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import com.pamirs.tro.entity.domain.entity.TBList;
import com.pamirs.tro.entity.domain.entity.TWList;
import com.pamirs.tro.entity.domain.entity.configs.Configurations;
import com.pamirs.tro.entity.domain.entity.configs.DataSource;
import com.pamirs.tro.entity.domain.entity.simplify.TShadowJobConfig;
import com.pamirs.tro.entity.domain.entity.user.User;
import io.shulie.tro.utils.json.JsonHelper;
import io.shulie.tro.web.app.response.application.ShadowConsumerResponse;
import io.shulie.tro.web.app.service.ShadowConsumerService;
import io.shulie.tro.web.app.service.config.ConfigService;
import io.shulie.tro.web.app.service.dsManage.DsService;
import io.shulie.tro.web.app.service.linkManage.LinkGuardService;
import io.shulie.tro.web.app.service.linkManage.WhiteListService;
import io.shulie.tro.web.app.service.simplify.ShadowJobConfigService;
import io.shulie.tro.web.config.entity.AllowList;
import io.shulie.tro.web.config.entity.Guard;
import io.shulie.tro.web.config.entity.ShadowConsumer;
import io.shulie.tro.web.config.entity.ShadowDB;
import io.shulie.tro.web.config.entity.ShadowJob;
import io.shulie.tro.web.config.enums.AllowListType;
import io.shulie.tro.web.config.enums.BlockListType;
import io.shulie.tro.web.config.enums.ShadowConsumerType;
import io.shulie.tro.web.config.enums.ShadowDSType;
import io.shulie.tro.web.config.enums.ShadowJobType;
import io.shulie.tro.web.config.sync.api.AllowListSyncService;
import io.shulie.tro.web.config.sync.api.BlockListSyncService;
import io.shulie.tro.web.config.sync.api.GuardSyncService;
import io.shulie.tro.web.config.sync.api.ShadowConsumerSyncService;
import io.shulie.tro.web.config.sync.api.ShadowDbSyncService;
import io.shulie.tro.web.config.sync.api.ShadowJobSyncService;
import io.shulie.tro.web.config.sync.api.SwitchSyncService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shiyajian
 * create: 2020-09-17
 */
@Service
@Slf4j
public class ConfigSyncServiceImpl implements ConfigSyncService {

    @Autowired
    private GuardSyncService guardSyncService;
    @Autowired
    private SwitchSyncService switchSyncService;
    @Autowired
    private AllowListSyncService allowListSyncService;
    @Autowired
    private ShadowJobSyncService shadowJobSyncService;
    @Autowired
    private ShadowConsumerSyncService shadowConsumerSyncService;
    @Autowired
    private BlockListSyncService blockListSyncService;
    @Autowired
    private ShadowDbSyncService shadowDbSyncService;
    @Autowired
    private LinkGuardService linkGuardService;
    @Resource
    private TApplicationMntDao tApplicationMntDao;
    @Autowired
    private ConfigService configService;
    @Autowired
    private WhiteListService whiteListService;
    @Autowired
    private ShadowJobConfigService shadowJobConfigService;
    @Autowired
    private DsService dsService;
    @Resource
    private TBListMntDao tbListMntDao;
    @Resource
    private TUserMapper userMapper;
    @Autowired
    private ShadowConsumerService shadowConsumerService;

    private List<User> userList = Lists.newArrayList();

    @PostConstruct
    public void init() {
        userList = userMapper.selectDistinctUserAppKey();
    }

    /**
     * TODO 未来应用从AMDB查询，applicationId 可能是根据规则生成的字符串
     */
    @Override
    public void syncGuard(String userAppKey, long applicationId, String applicationName) {
        if (StringUtils.isEmpty(applicationName)) {
            TApplicationMnt tApplicationMnt = tApplicationMntDao.queryApplicationinfoById(applicationId);
            applicationName = tApplicationMnt.getApplicationName();
        }
        guardSyncService.syncGuard(userAppKey, applicationName, queryAndParseGuard(applicationId));
    }

    private List<Guard> queryAndParseGuard(long applicationId) {
        List<LinkGuardEntity> dbGuards = linkGuardService.getAllEnabledGuard(String.valueOf(applicationId));
        if (CollectionUtils.isEmpty(dbGuards)) {
            return Lists.newArrayList();
        }
        List<Guard> guards = dbGuards.stream().map(db -> {
            Guard guard = new Guard();
            guard.setId(db.getId());
            String methodInfo = db.getMethodInfo();
            String[] methodInfoArray = methodInfo.split("#");
            String classname = methodInfoArray.length == 2 ? methodInfoArray[0] : db.getMethodInfo();
            String method = methodInfoArray.length == 2 ? methodInfoArray[1] : db.getMethodInfo();
            guard.setClassName(classname);
            guard.setMethodName(method);
            guard.setCodeScript(db.getGroovy());
            return guard;
        }).sorted(Comparator.comparing(Guard::getId)).collect(Collectors.toList());
        return guards;
    }

    @Override
    public void syncClusterTestSwitch(String userAppKey) {
        boolean clusterTestSwitch = configService.getClusterTestSwitch(userAppKey);
        switchSyncService.turnClusterTestSwitch(userAppKey, clusterTestSwitch);
    }

    @Override
    public void syncAllowListSwitch(String userAppKey) {
        boolean clusterTestSwitch = configService.getAllowListSwitch(userAppKey);
        switchSyncService.turnAllowListSwitch(userAppKey, clusterTestSwitch);

    }

    @Override
    public void syncAllowList(String userAppKey, long applicationId, String applicationName) {
        if (StringUtils.isEmpty(applicationName)) {
            TApplicationMnt tApplicationMnt = tApplicationMntDao.queryApplicationinfoById(applicationId);
            applicationName = tApplicationMnt.getApplicationName();
        }
        allowListSyncService.syncAllowList(userAppKey, applicationName, queryAndParseAllowList(applicationId));
    }

    private List<AllowList> queryAndParseAllowList(long applicationId) {
        List<TWList> allEnableWhitelists = whiteListService.getAllEnableWhitelists(String.valueOf(applicationId));
        if (CollectionUtils.isEmpty(allEnableWhitelists)) {
            return Lists.newArrayList();
        }
        List<AllowList> allowLists = allEnableWhitelists.stream().map(db -> {
            AllowList allowList = new AllowList();
            allowList.setId(db.getWlistId());
            allowList.setInterfaceName(db.getInterfaceName());
            String type = db.getType();
            try {
                allowList.setType(getType(Integer.parseInt(type)));
            } catch (Exception e) {
                log.error("AllowList Type Parse Exception: {}", e.getMessage());
            }
            return allowList;
        }).sorted(Comparator.comparing(AllowList::getId)).collect(Collectors.toList());
        return allowLists;
    }

    private AllowListType getType(int dbType) {
        AllowListType type;
        switch (dbType) {
            case 1:
                type = AllowListType.HTTP;
                break;
            case 2:
                type = AllowListType.DUBBO;
                break;
            case 3:
                type = AllowListType.RABBITMQ;
                break;
            default:
                type = AllowListType.UNKNOW;
                break;
        }
        return type;
    }

    @Override
    public void syncShadowJob(String userAppKey, long applicationId, String applicationName) {
        if (StringUtils.isEmpty(applicationName)) {
            TApplicationMnt tApplicationMnt = tApplicationMntDao.queryApplicationinfoById(applicationId);
            applicationName = tApplicationMnt.getApplicationName();
        }
        shadowJobSyncService.syncShadowJob(userAppKey, applicationName, queryAndParseShadowJob(applicationId));
    }

    private List<ShadowJob> queryAndParseShadowJob(long applicationId) {
        List<TShadowJobConfig> allEnableShadowJobs = shadowJobConfigService.getAllEnableShadowJobs(applicationId);
        if (CollectionUtils.isEmpty(allEnableShadowJobs)) {
            return Lists.newArrayList();
        }
        List<ShadowJob> shadowJobs = allEnableShadowJobs.stream().map(db -> {
            ShadowJob shadowJob = new ShadowJob();
            shadowJob.setId(db.getId());
            Integer type = db.getType();
            shadowJob.setType(
                type == 0 ?
                    ShadowJobType.QUARTZ :
                    type == 1 ?
                        ShadowJobType.ELASTIC_JOB :
                        type == 2 ?
                            ShadowJobType.XXL_JOB :
                            ShadowJobType.UNKNOWN
            );
            String xmlConfigStr = db.getConfigCode();
            SAXReader reader = new SAXReader();
            try {
                Document document = reader.read(new ByteArrayInputStream(xmlConfigStr.getBytes()));
                Element root = document.getRootElement();
                Element classnameE = root.element("className");
                Element jobDataTypeE = root.element("jobDataType");
                Element cronE = root.element("cron");
                shadowJob.setClassName(classnameE.getText());
                shadowJob.setJobDataType(jobDataTypeE.getText());
                shadowJob.setCron(cronE.getText());
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            }
            return shadowJob;
        }).sorted(Comparator.comparing(ShadowJob::getId)).collect(Collectors.toList());
        return shadowJobs;
    }

    @Override
    public void syncShadowDB(String userAppKey, long applicationId, String applicationName) {
        if (StringUtils.isEmpty(applicationName)) {
            TApplicationMnt tApplicationMnt = tApplicationMntDao.queryApplicationinfoById(applicationId);
            applicationName = tApplicationMnt.getApplicationName();
        }
        shadowDbSyncService.syncShadowDB(userAppKey, applicationName, queryAndParseShadowDB(applicationId));
    }

    @Override
    public void syncShadowConsumer(String userAppKey, long applicationId, String applicationName) {
        if (StringUtils.isEmpty(applicationName)) {
            TApplicationMnt tApplicationMnt = tApplicationMntDao.queryApplicationinfoById(applicationId);
            applicationName = tApplicationMnt.getApplicationName();
        }
        shadowConsumerSyncService.syncShadowConsumer(userAppKey, applicationName,
            queryAndParseShadowConsumer(applicationId));
    }

    private List<ShadowConsumer> queryAndParseShadowConsumer(long applicationId) {
        List<ShadowConsumerResponse> consumerResponses = shadowConsumerService
            .getShadowConsumersByApplicationId(applicationId);
        if (CollectionUtils.isEmpty(consumerResponses)) {
            return Lists.newArrayList();
        }
        return consumerResponses.stream()
            .filter(ShadowConsumerResponse::getEnabled)
            .map(consumer -> {
                ShadowConsumer shadowConsumer = new ShadowConsumer();
                String[] split = consumer.getTopicGroup().split("#");
                shadowConsumer.setGroup(split[1]);
                shadowConsumer.setTopic(split[0]);
                shadowConsumer.setId(consumer.getId());
                shadowConsumer.setType(ShadowConsumerType.of(consumer.getType().name()));
                return shadowConsumer;
            }).collect(Collectors.toList());
    }

    private List<ShadowDB> queryAndParseShadowDB(long applicationId) {
        List<DsModelWithBLOBs> allEnableDbConfigs = dsService.getAllEnabledDbConfig(applicationId);
        if (CollectionUtils.isEmpty(allEnableDbConfigs)) {
            return Lists.newArrayList();
        }
        List<ShadowDB> shadowDbs = allEnableDbConfigs.stream().map(config -> {
            ShadowDB shadowDB = new ShadowDB();
            shadowDB.setId(config.getId());
            Byte type = config.getDsType();
            if (type == 0) {
                shadowDB.setType(ShadowDSType.SCHEMA);
            } else if (type == 1) {
                shadowDB.setType(ShadowDSType.TABLE);
            } else if (type == 2) {
                shadowDB.setType(ShadowDSType.SERVER);
            }
            shadowDB.setBizJdbcUrl(config.getUrl());
            if (shadowDB.getType() == ShadowDSType.TABLE) {
                String tables = config.getConfig();
                if (!StringUtils.isEmpty(tables)) {
                    ShadowDB.ShadowTableConfig shadowTableConfig = new ShadowDB.ShadowTableConfig();
                    shadowTableConfig.setTableNames(Arrays.asList(tables.split(",")));
                    shadowDB.setShadowTableConfig(shadowTableConfig);
                }
            } else if (shadowDB.getType() == ShadowDSType.SCHEMA) {
                if (!StringUtils.isEmpty(config.getParseConfig())) {
                    Configurations configurations = JsonHelper.json2Bean(config.getParseConfig(), Configurations.class);
                    List<DataSource> dataSourceList = configurations.getDataSources();
                    if (CollectionUtils.isNotEmpty(dataSourceList)) {
                        String bizUsername = dataSourceList
                            .stream()
                            .filter(dataSource -> "dataSourceBusiness".equals(dataSource.getId()))
                            .map(DataSource::getUsername)
                            .collect(Collectors.joining());
                        shadowDB.setBizUserName(bizUsername);
                        ShadowDB.ShadowSchemaConfig shadowSchemaConfig = new ShadowDB.ShadowSchemaConfig();
                        Optional<DataSource> optional = dataSourceList
                            .stream()
                            .filter(dataSource -> "dataSourcePerformanceTest".equals(dataSource.getId())).findFirst();
                        if (optional.isPresent()) {
                            DataSource ptDatasource = optional.get();
                            BeanUtils.copyProperties(ptDatasource, shadowSchemaConfig);
                            shadowDB.setShadowSchemaConfig(shadowSchemaConfig);
                        }
                    }
                }
            } else if (shadowDB.getType() == ShadowDSType.SERVER) {

            }
            return shadowDB;
        }).sorted(Comparator.comparing(ShadowDB::getId)).collect(Collectors.toList());
        return shadowDbs;
    }

    @Override
    public void syncBlockList(String userAppKey) {
        blockListSyncService.syncBlockList(userAppKey, BlockListType.CACHE, queryAndParseShadowDB());
    }

    private List<String> queryAndParseShadowDB() {
        List<TBList> allEnabledBlockList = tbListMntDao.getAllEnabledBlockList();
        if (CollectionUtils.isEmpty(allEnabledBlockList)) {
            return Lists.newArrayList();
        }
        List<String> blockLists = allEnabledBlockList.stream().map(TBList::getRedisKey).collect(Collectors.toList());
        return blockLists;
    }
}
