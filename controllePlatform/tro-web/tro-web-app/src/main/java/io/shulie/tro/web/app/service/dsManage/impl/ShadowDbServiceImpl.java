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

package io.shulie.tro.web.app.service.dsManage.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.pamirs.tro.common.constant.AppAccessTypeEnum;
import com.pamirs.tro.common.enums.ds.DsTypeEnum;
import com.pamirs.tro.entity.domain.vo.dsmanage.Configurations;
import com.pamirs.tro.entity.domain.vo.dsmanage.DataSource;
import com.pamirs.tro.entity.domain.vo.dsmanage.DatasourceMediator;
import io.shulie.tro.web.app.cache.AgentConfigCacheManager;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.constant.AppConstants;
import io.shulie.tro.web.app.init.sync.ConfigSyncService;
import io.shulie.tro.web.app.request.application.ApplicationDsCreateRequest;
import io.shulie.tro.web.app.request.application.ApplicationDsDeleteRequest;
import io.shulie.tro.web.app.request.application.ApplicationDsEnableRequest;
import io.shulie.tro.web.app.request.application.ApplicationDsUpdateRequest;
import io.shulie.tro.web.app.response.application.ApplicationDsDetailResponse;
import io.shulie.tro.web.app.service.ApplicationService;
import io.shulie.tro.web.app.service.dsManage.AbstractDsService;
import io.shulie.tro.web.app.utils.DsManageUtil;
import io.shulie.tro.web.common.util.JsonUtil;
import io.shulie.tro.web.data.dao.application.ApplicationDAO;
import io.shulie.tro.web.data.dao.application.ApplicationDsDAO;
import io.shulie.tro.web.data.param.application.ApplicationDsCreateParam;
import io.shulie.tro.web.data.param.application.ApplicationDsDeleteParam;
import io.shulie.tro.web.data.param.application.ApplicationDsEnableParam;
import io.shulie.tro.web.data.param.application.ApplicationDsQueryParam;
import io.shulie.tro.web.data.param.application.ApplicationDsUpdateParam;
import io.shulie.tro.web.data.result.application.ApplicationDetailResult;
import io.shulie.tro.web.data.result.application.ApplicationDsResult;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author HengYu
 * @className ShadowDbServiceImpl
 * @date 2021/4/12 9:25 下午
 * @description 影子库数据源服务实现
 */

@Component
public class ShadowDbServiceImpl extends AbstractDsService {

    private Logger logger = LoggerFactory.getLogger(ShadowDbServiceImpl.class);

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ConfigSyncService configSyncService;

    @Autowired
    private ApplicationDsDAO applicationDsDAO;

    @Autowired
    private ApplicationDAO applicationDAO;

    @Autowired
    private AgentConfigCacheManager agentConfigCacheManager;

    @Override
    public Response dsAdd(ApplicationDsCreateRequest createRequest) {

        ApplicationDetailResult applicationDetailResult = applicationDAO.getApplicationById(
            createRequest.getApplicationId());

        Response fail = validator(createRequest, applicationDetailResult);
        if (fail != null) { return fail; }

        ApplicationDsCreateParam createParam = addParseConfig(createRequest);
        // 等其他都成功, 再插入数据库
        createParam.setApplicationId(createRequest.getApplicationId());
        createParam.setApplicationName(applicationDetailResult.getApplicationName());
        createParam.setDbType(createRequest.getDbType());
        createParam.setDsType(Integer.parseInt(String.valueOf(createRequest.getDsType())));
        createParam.setCustomerId(applicationDetailResult.getCustomerId());
        createParam.setUserId(applicationDetailResult.getUserId());
        // 新增配置
        createParam.setStatus(createRequest.getStatus());

        syncInfo(applicationDetailResult.getApplicationId(), createParam.getApplicationName());

        applicationDsDAO.insert(createParam);
        return Response.success();
    }

    private void syncInfo(Long applicationId, String applicationName) {
        //同步配置
        configSyncService.syncShadowDB(RestContext.getUser().getKey(), applicationId, null);
        //修改应用状态
        applicationService.modifyAccessStatus(String.valueOf(applicationId),
            AppAccessTypeEnum.UNUPLOAD.getValue(), null);

        agentConfigCacheManager.evictShadowDb(applicationName);
    }

    private ApplicationDsCreateParam addParseConfig(ApplicationDsCreateRequest createRequest) {
        ApplicationDsCreateParam createParam = new ApplicationDsCreateParam();
        createParam.setConfig(createRequest.getConfig());
        if (createRequest.isOldVersion()) {
            createParam.setUrl(parseShadowDbUrl(createRequest.getConfig()));
            createParam.setParseConfig(parseShadowDbConfigOld(createRequest.getConfig()));
        } else {
            createParam.setUrl(createRequest.getUrl());
            createParam.setParseConfig(parseShadowDbCreateConfig(createRequest));
        }
        return createParam;
    }

    private Response validator(ApplicationDsCreateRequest createRequest,
        ApplicationDetailResult applicationDetailResult) {

        if (applicationDetailResult == null) {
            return Response.fail("0", "应用不存在!");
        }

        ApplicationDsQueryParam queryCurrentParam = new ApplicationDsQueryParam();
        queryCurrentParam.setApplicationId(createRequest.getApplicationId());
        queryCurrentParam.setIsDeleted(0);
        List<ApplicationDsResult> applicationDsResultList = applicationDsDAO.queryList(queryCurrentParam);

        List<Integer> dsTypeList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(applicationDsResultList)) {
            dsTypeList = applicationDsResultList.stream().map(ApplicationDsResult::getDsType)
                .map(String::valueOf).map(Integer::parseInt)
                .distinct()
                .collect(Collectors.toList());
        }
        //新增影子库配置
        if (dsTypeList.contains(DsTypeEnum.SHADOW_TABLE.getCode())) {
            return Response.fail("0", "创建影子库表配置失败, 不能出现不同类型(库/表)");
        }
        return null;
    }

    @Override
    public Response dsUpdate(ApplicationDsUpdateRequest updateRequest) {
        ApplicationDsResult dsResult = applicationDsDAO.queryByPrimaryKey(updateRequest.getId());
        if (Objects.isNull(dsResult)) {
            return Response.fail("0", "该配置不存在");
        }

        ApplicationDsUpdateParam updateParam = new ApplicationDsUpdateParam();
        String config = updateRequest.getConfig();

        updateParserConfig(updateRequest, dsResult, updateParam, config);

        updateParam.setId(updateRequest.getId());
        updateParam.setStatus(updateRequest.getStatus());

        syncInfo(dsResult.getApplicationId(), dsResult.getApplicationName());
        applicationDsDAO.update(updateParam);
        return Response.success();
    }

    private void updateParserConfig(ApplicationDsUpdateRequest updateRequest, ApplicationDsResult dsResult,
        ApplicationDsUpdateParam updateParam, String config) {
        // config 替换
        config = this.getOriginConfigFromSchema(config, dsResult.getConfig());
        if (updateRequest.isOldVersion()) {
            updateParam.setUrl(parseShadowDbUrl(config));
            updateParam.setConfig(config);
            updateParam.setParseConfig(parseShadowDbConfig(config));
        } else {
            updateParam.setUrl(updateRequest.getUrl());
            BeanUtils.copyProperties(updateRequest, updateParam);
            updateParam.setConfig(config);
            updateParam.setParseConfig(parseShadowDbUpdateConfig(updateRequest));
        }
    }

    @Override
    public Response<ApplicationDsDetailResponse> dsQueryDetail(Long dsId, boolean isOldVersion) {
        ApplicationDsResult dsResult = applicationDsDAO.queryByPrimaryKey(dsId);
        if (Objects.isNull(dsResult)) {
            return Response.fail("该影子库表配置不存在");
        }

        ApplicationDsDetailResponse dsDetailResponse = new ApplicationDsDetailResponse();
        dsDetailResponse.setId(dsResult.getId());
        dsDetailResponse.setApplicationId(dsResult.getApplicationId());
        dsDetailResponse.setApplicationName(dsResult.getApplicationName());
        dsDetailResponse.setDbType(dsResult.getDbType());
        dsDetailResponse.setDsType(dsResult.getDsType());
        dsDetailResponse.setUrl(dsResult.getUrl());

        queryParseConfig(isOldVersion, dsResult, dsDetailResponse);

        return Response.success(dsDetailResponse);
    }

    private void queryParseConfig(boolean isOldVersion, ApplicationDsResult dsResult,
        ApplicationDsDetailResponse dsDetailResponse) {
        String config = dsResult.getConfig();

        // 影子库
        // 新版本
        if (!isOldVersion) {
            // 影子库详情解析
            this.dsParseConfig2Resp(dsDetailResponse, dsResult.getParseConfig());
        }

        // 获得密码脱敏的 xml
        config = this.getSafeConfigFromSchema(config);

        dsDetailResponse.setConfig(config);
    }

    @Override
    public Response enableConfig(ApplicationDsEnableRequest enableRequest) {
        ApplicationDsResult dsResult = applicationDsDAO.queryByPrimaryKey(enableRequest.getId());
        if (Objects.isNull(dsResult)) {
            return Response.fail("0", "该配置不存在");
        }

        ApplicationDsEnableParam enableParam = new ApplicationDsEnableParam();
        enableParam.setId(enableRequest.getId());
        enableParam.setStatus(enableRequest.getStatus());
        applicationDsDAO.enable(enableParam);

        syncInfo(dsResult.getApplicationId(), dsResult.getApplicationName());

        return Response.success();
    }

    @Override
    public Response dsDelete(ApplicationDsDeleteRequest dsDeleteRequest) {
        ApplicationDsResult dsResult = applicationDsDAO.queryByPrimaryKey(dsDeleteRequest.getId());
        if (Objects.isNull(dsResult)) {
            return Response.fail("0", "该配置不存在");
        }
        ApplicationDsDeleteParam deleteParam = new ApplicationDsDeleteParam();
        deleteParam.setIdList(Collections.singletonList(dsDeleteRequest.getId()));
        applicationDsDAO.delete(deleteParam);

        syncInfo(dsResult.getApplicationId(), dsResult.getApplicationName());
        return Response.success();
    }

    public String parseShadowDbUrl(String config) {
        String buUrl = "";
        Configurations configurations = DsManageUtil.getConfigurationsByXml(config);
        if (configurations == null) {
            return buUrl;
        }

        DataSource dataSource = findDatasource(configurations.getDataSources(),
            configurations.getDatasourceMediator().getDataSourceBusiness());
        if (dataSource != null && dataSource.getUrl() != null) {
            buUrl = dataSource.getUrl();
            if (buUrl.contains("?")) {
                buUrl = buUrl.substring(0, buUrl.indexOf("?"));
            }
        }
        return buUrl;
    }

    private String parseShadowDbConfigOld(String config) {
        String parsedConfig = "";
        Configurations configurations = DsManageUtil.getConfigurationsByXml(config);
        if (configurations == null) {
            return parsedConfig;
        }

        Map<String, Object> verifyResult = verifyConfigurations(configurations);
        if (verifyResult.get("result") != null && Boolean.parseBoolean(
            String.valueOf(verifyResult.get("result")))) {
            parsedConfig = JSON.toJSONString(configurations);
        }
        return parsedConfig;
    }

    private String parseShadowDbConfig(String config) {
        String parsedConfig = "";
        Configurations configurations = DsManageUtil.getConfigurationsByXml(config);
        if (configurations == null) {
            return parsedConfig;
        }

        Map<String, Object> verifyResult = verifyConfigurations(configurations);
        if (verifyResult.get("result") != null && Boolean.parseBoolean(
            String.valueOf(verifyResult.get("result")))) {
            parsedConfig = JSON.toJSONString(configurations);

        }
        return parsedConfig;
    }

    private String parseShadowDbCreateConfig(ApplicationDsCreateRequest config) {
        String parsedConfig = "";
        Configurations configurations = null;
        // Configurations configurations = parseXmlConfigurations(config);
        //组装configuration
        configurations = assembleConfigurations(config);
        Map<String, Object> verifyResult = verifyConfigurations(configurations);
        parsedConfig = JSON.toJSONString(configurations);
        if (verifyResult.get("result") != null && Boolean.parseBoolean(
            String.valueOf(verifyResult.get("result")))) {
        }
        return parsedConfig;
    }

    public Configurations assembleConfigurations(ApplicationDsCreateRequest config) {
        Configurations result = new Configurations();
        result.setDatasourceMediator(new DatasourceMediator("dataSourceBusiness",
            "dataSourcePerformanceTest"));
        List<DataSource> dataSourceList = new ArrayList<>();
        result.setDataSources(dataSourceList);
        DataSource businessDataSource = new DataSource();
        DataSource performanceTestDataSource = new DataSource();
        dataSourceList.add(businessDataSource);
        dataSourceList.add(performanceTestDataSource);

        //组装business datasource
        businessDataSource.setId("dataSourceBusiness");
        businessDataSource.setUrl(config.getUrl());
        businessDataSource.setUsername(config.getUserName());
        //组装performanceTest dataSource
        performanceTestDataSource.setId("dataSourcePerformanceTest");
        performanceTestDataSource.setUrl(config.getShadowDbUrl());
        performanceTestDataSource.setUsername(config.getShadowDbUserName());
        performanceTestDataSource.setPassword(config.getShadowDbPassword());
        performanceTestDataSource.setMinIdle(config.getShadowDbMinIdle());
        performanceTestDataSource.setMaxActive(config.getShadowDbMaxActive());

        return result;
    }

    private String parseShadowDbUpdateConfig(ApplicationDsUpdateRequest config) {

        String parsedConfig = "";
        Configurations configurations = null;
        // Configurations configurations = parseXmlConfigurations(config);
        //组装configuration
        configurations = assembleUpdateConfigurations(config);

        Map<String, Object> verifyResult = verifyConfigurations(configurations);
        if (verifyResult.get("result") != null && Boolean.parseBoolean(
            String.valueOf(verifyResult.get("result")))) {
            parsedConfig = JSON.toJSONString(configurations);

        } else {
            throw new RuntimeException((String)verifyResult.get("message"));
        }
        return parsedConfig;
    }

    public Map<String, Object> verifyConfigurations(Configurations configurations) {
        Map<String, Object> result = new HashMap<>();
        if (configurations == null) {
            result.put("result", false);
            result.put("message", "configurations is null!");
            return result;
        }
        DatasourceMediator mediator = configurations.getDatasourceMediator();
        List<DataSource> dataSourceList = configurations.getDataSources();
        if (mediator == null) {
            result.put("result", false);
            result.put("message", "datasourceMediator is null!");
            return result;
        }
        if (dataSourceList == null || dataSourceList.isEmpty()) {
            result.put("result", false);
            result.put("message", "dataSourceList is null or empty!");
            return result;
        }
        String datasourceBusinessId = mediator.getDataSourceBusiness();
        String dataSourcePerformanceTestId = mediator.getDataSourcePerformanceTest();
        if (datasourceBusinessId == null || datasourceBusinessId.isEmpty()) {
            result.put("result", false);
            result.put("message", "datasourceBusiness id for datasourceMediator is null or empty!");
            return result;
        }
        if (dataSourcePerformanceTestId == null || dataSourcePerformanceTestId.isEmpty()) {
            result.put("result", false);
            result.put("message", "dataSourcePerformanceTest id for datasourceMediator is null or empty!");
            return result;
        }
        DataSource dataSourceBusiness = findDatasource(dataSourceList, datasourceBusinessId);
        if (dataSourceBusiness == null) {
            result.put("result", false);
            result.put("message",
                "datasourceBusiness config not found for datasourceMediator, id: " + datasourceBusinessId);
            return result;
        }
        DataSource dataSourcePerformanceTest = findDatasource(dataSourceList, dataSourcePerformanceTestId);
        if (dataSourcePerformanceTest == null) {
            result.put("result", false);
            result.put("message", "dataSourcePerformanceTest config not found for datasourceMediator, id: "
                + dataSourcePerformanceTestId);
            return result;
        }

        if (dataSourceBusiness.getUrl() == null || dataSourceBusiness.getUrl().isEmpty()
            || dataSourceBusiness.getUsername() == null || dataSourceBusiness.getUsername().isEmpty()) {
            result.put("result", false);
            result.put("message", "url and username should not be null or empty for dataSourceBusiness config");
            return result;
        }
        if (dataSourcePerformanceTest.getUrl() == null || dataSourcePerformanceTest.getUrl().isEmpty()
            || dataSourcePerformanceTest.getUsername() == null || dataSourcePerformanceTest.getUsername().isEmpty()) {
            result.put("result", false);
            result.put("message", "url and username should not be null or empty for dataSourcePerformanceTest config");
            return result;
        }
        result.put("result", true);
        result.put("message", "Verify passed");
        return result;
    }

    private static DataSource findDatasource(List<DataSource> dataSourceList, String id) {
        for (DataSource dataSource : dataSourceList) {
            if (id.equals(dataSource.getId())) {
                return dataSource;
            }
        }
        return null;
    }

    public Configurations assembleUpdateConfigurations(ApplicationDsUpdateRequest config) {
        Configurations result = new Configurations();
        result.setDatasourceMediator(new DatasourceMediator("dataSourceBusiness",
            "dataSourcePerformanceTest"));
        List<DataSource> dataSourceList = new ArrayList<>();
        result.setDataSources(dataSourceList);
        DataSource businessDataSource = new DataSource();
        DataSource performanceTestDataSource = new DataSource();
        dataSourceList.add(businessDataSource);
        dataSourceList.add(performanceTestDataSource);

        //组装business datasource
        businessDataSource.setId("dataSourceBusiness");
        businessDataSource.setUrl(config.getUrl());
        businessDataSource.setUsername(config.getUserName());

        //组装performanceTest dataSource
        performanceTestDataSource.setId("dataSourcePerformanceTest");
        // ApplicationDsDbCreateRequest dbCofig = config.getDbConfig();
        performanceTestDataSource.setUrl(config.getShadowDbUrl());
        performanceTestDataSource.setUsername(config.getShadowDbUserName());
        performanceTestDataSource.setPassword(config.getShadowDbPassword());
        performanceTestDataSource.setMinIdle(config.getShadowDbMinIdle());
        performanceTestDataSource.setMaxActive(config.getShadowDbMaxActive());

        return result;
    }

    /**
     * 获得影子库原有的, 带有明文密码的 xml
     * 详情传入的 xml, 密码可能被脱敏
     *
     * @param xml       xml 配置
     * @param originXml 原有的 xml 配置
     * @return 原有的 xml
     */
    private String getOriginConfigFromSchema(String xml, String originXml) {
        // 匹配是否没有脱敏的密码
        // 没有, 说明能直接更新
        String safePasswordElement;
        if (StringUtils.isBlank(xml)
            || !xml.contains(safePasswordElement = DsManageUtil.getSafePasswordElementAboutXml())) {
            return xml;
        }

        // 到这里, 说明有脱敏的密码
        // 需要替换原有的密码, 再进行更新
        // 解析
        Configurations configurations = DsManageUtil.getConfigurationsByXml(originXml);
        if (configurations == null) {
            return xml;
        }

        String password = configurations.getDataSources().get(1).getPassword();
        String originPasswordElement = DsManageUtil.getOriginPasswordElementAboutXml(password);
        // 脱敏的更换成明文密码的, 为了更新
        return xml.replace(safePasswordElement, originPasswordElement);
    }

    /**
     * 解析的配置 转 响应数据
     *
     * @param ds     响应数据
     * @param source 解析配置
     */
    public void dsParseConfig2Resp(ApplicationDsDetailResponse ds, String source) {
        Configurations config = JsonUtil.json2bean(source, Configurations.class);
        if (config == null) {
            return;
        }
        DataSource dataSource = config.getDataSources().get(1);
        DataSource business = config.getDataSources().get(0);
        ds.setShadowDbUrl(dataSource.getUrl());
        ds.setUserName(business.getUsername());
        ds.setShadowDbUserName(dataSource.getUsername());
        ds.setShadowDbPassword(AppConstants.PASSWORD_COVER);
        ds.setShadowDbMinIdle(dataSource.getMinIdle());
        ds.setShadowDbMaxActive(dataSource.getMaxActive());
    }

    /**
     * xml 配置, 密码脱敏, 替换
     *
     * @param xml xml 配置
     * @return 安全的 xml
     */
    private String getSafeConfigFromSchema(String xml) {
        if (StringUtils.isBlank(xml)) {
            return "";
        }

        // xml 解析
        Configurations configurations = DsManageUtil.getConfigurationsByXml(xml);
        if (configurations == null) {
            return "";
        }

        // 密码脱敏
        DataSource dataSource = configurations.getDataSources().get(1);
        String password = dataSource.getPassword();
        String originPasswordElement = DsManageUtil.getOriginPasswordElementAboutXml(password);
        String newPasswordElement = DsManageUtil.getSafePasswordElementAboutXml();
        return xml.replace(originPasswordElement, newPasswordElement);
    }
}


