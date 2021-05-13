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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.pamirs.tro.common.constant.AppAccessTypeEnum;
import com.pamirs.tro.common.enums.ds.DsTypeEnum;
import com.pamirs.tro.entity.dao.confcenter.TApplicationMntDao;
import com.pamirs.tro.entity.domain.entity.DsModelWithBLOBs;
import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import com.pamirs.tro.entity.domain.entity.user.User;
import io.shulie.tro.web.app.cache.AgentConfigCacheManager;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.init.sync.ConfigSyncService;
import io.shulie.tro.web.app.request.application.ApplicationDsCreateRequest;
import io.shulie.tro.web.app.request.application.ApplicationDsDeleteRequest;
import io.shulie.tro.web.app.request.application.ApplicationDsEnableRequest;
import io.shulie.tro.web.app.request.application.ApplicationDsUpdateRequest;
import io.shulie.tro.web.app.response.application.ApplicationDsDetailResponse;
import io.shulie.tro.web.app.response.application.ShadowServerConfigurationResponse;
import io.shulie.tro.web.app.response.application.SingleServerConfiguration;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author HengYu
 * @className AbstractDsService
 * @date 2021/4/12 9:25 下午
 * @description 数据源存储抽象服务
 */

@Component
public class ShadowRedisServerServiceImpl extends AbstractDsService {

    private Logger logger = LoggerFactory.getLogger(ShadowDbServiceImpl.class);

    @Autowired
    private ApplicationService applicationService;

    @Resource
    private TApplicationMntDao applicationMntDao;

    @Resource
    private com.pamirs.tro.entity.dao.simplify.TAppBusinessTableInfoMapper TAppBusinessTableInfoMapper;

    @Resource
    private com.pamirs.tro.entity.dao.user.TUserMapper TUserMapper;

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
        Assert.notNull(applicationDetailResult, "应用不存在!");

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
        ApplicationDsCreateParam createParam = new ApplicationDsCreateParam();

        //新增影子server配置
        String url = parseShadowServerUrl(createRequest.getConfig());
        String parsedConfig = parseShadowServerConfig(createRequest.getConfig());
        createParam.setUrl(url);
        createParam.setParseConfig(parsedConfig);
        createParam.setConfig(createRequest.getConfig());

        // 等其他都成功, 再插入数据库
        createParam.setApplicationId(createRequest.getApplicationId());
        createParam.setApplicationName(applicationDetailResult.getApplicationName());
        createParam.setDbType(createRequest.getDbType());
        createParam.setDsType(Integer.parseInt(String.valueOf(createRequest.getDsType())));
        createParam.setCustomerId(applicationDetailResult.getCustomerId());
        createParam.setUserId(applicationDetailResult.getUserId());

        //同步配置
        configSyncService.syncShadowDB(RestContext.getUser().getKey(), createParam.getApplicationId(), null);

        //修改应用状态
        applicationService.modifyAccessStatus(String.valueOf(createParam.getApplicationId()),
            AppAccessTypeEnum.UNUPLOAD.getValue(), null);

        agentConfigCacheManager.evictShadowServer(applicationDetailResult.getApplicationName());

        // 新增配置
        createParam.setStatus(createRequest.getStatus());
        applicationDsDAO.insert(createParam);
        return Response.success();
    }

    @Override
    public Response dsUpdate(ApplicationDsUpdateRequest updateRequest) {
        ApplicationDsResult dsResult = applicationDsDAO.queryByPrimaryKey(updateRequest.getId());
        if (Objects.isNull(dsResult)) {
            return Response.fail("0", "该配置不存在");
        }

        ApplicationDsUpdateParam updateParam = new ApplicationDsUpdateParam();
        String config = updateRequest.getConfig();

        // config 替换
        config = this.getOriginConfigFromServer(config, dsResult.getConfig());
        updateParam.setUrl(parseShadowServerUrl(config));
        updateParam.setConfig(config);
        updateParam.setParseConfig(parseShadowServerConfig(config));

        configSyncService.syncShadowDB(RestContext.getUser().getKey(), dsResult.getApplicationId(),
            dsResult.getApplicationName());

        agentConfigCacheManager.evictShadowServer(dsResult.getApplicationName());

        updateParam.setId(updateRequest.getId());
        updateParam.setStatus(updateRequest.getStatus());
        applicationDsDAO.update(updateParam);
        return Response.success();
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

        String config = dsResult.getConfig();
        // 影子 server
        // json 解析, 密码脱敏
        config = this.getSafeConfigFromServer(config);

        dsDetailResponse.setConfig(config);
        return Response.success(dsDetailResponse);
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

        configSyncService.syncShadowDB(RestContext.getUser().getKey(), dsResult.getApplicationId(),
            dsResult.getApplicationName());
        agentConfigCacheManager.evictShadowServer(dsResult.getApplicationName());

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
        configSyncService.syncShadowDB(RestContext.getUser().getKey(), dsResult.getApplicationId(),
            dsResult.getApplicationName());

        agentConfigCacheManager.evictShadowServer(dsResult.getApplicationName());
        return Response.success();
    }

    public List<ShadowServerConfigurationResponse> getShadowServerConfigs(String appName) {
        List<ShadowServerConfigurationResponse> responseList = new ArrayList<>();
        String userAppKey = RestContext.getTenantUserKey();
        User user = TUserMapper.selectByKey(userAppKey);
        TApplicationMnt applicationMnt = applicationMntDao.queryApplicationInfoByNameAndTenant(appName,
            user == null ? null : user.getId());
        if (applicationMnt != null) {
            List<DsModelWithBLOBs> dsModels = applicationDsDAO.selectByAppIdForAgent(applicationMnt.getApplicationId());
            if (CollectionUtils.isNotEmpty(dsModels)) {
                dsModels = dsModels.stream()
                    .filter(
                        dsModel -> dsModel.getDsType().equals(new Byte(String.valueOf(DsTypeEnum.SHADOW_REDIS_SERVER
                            .getCode()))))
                    .collect(Collectors.toList());
            }
            for (DsModelWithBLOBs dsModel : dsModels) {
                ShadowServerConfigurationResponse configurationResponse
                    = JSON.parseObject(dsModel.getParseConfig(),ShadowServerConfigurationResponse.class);
                responseList.add(configurationResponse);
            }
        }
        return responseList;
    }

    /**
     * 获得 server 原有的, 带有明文密码的 json
     * 详情传入的 json, 密码可能被脱敏
     *
     * @param json       json 配置
     * @param originJson 原有的 json 配置
     * @return 原有的 json
     */
    private String getOriginConfigFromServer(String json, String originJson) {
        // 匹配是否没有脱敏的密码
        // 没有, 说明能直接更新
        String safePasswordField;
        if (StringUtils.isBlank(json)
            || !json.contains(safePasswordField = DsManageUtil.getSafePasswordFieldAboutJson())) {
            return json;
        }

        // 到这里, 说明有脱敏的密码
        // 需要替换原有的密码, 再进行更新
        // 解析
        ShadowServerConfigurationResponse response = JsonUtil.json2bean(originJson,
            ShadowServerConfigurationResponse.class);
        String password = response.getDataSourceBusinessPerformanceTest().getPassword();
        String originPasswordField = DsManageUtil.getOriginPasswordFieldAboutJson(password);
        // 脱敏的更换成明文密码的, 为了更新
        return json.replace(safePasswordField, originPasswordField);
    }

    public String parseShadowServerUrl(String config) {
        ShadowServerConfigurationResponse configurations = parseJsonConfigurations(config);
        SingleServerConfiguration singleServerConfiguration = configurations.getDataSourceBusinessPerformanceTest();
        return singleServerConfiguration.getNodes();
    }

    private String parseShadowServerConfig(String config) {
        ShadowServerConfigurationResponse configurations = parseJsonConfigurations(config);
        return JSON.toJSONString(configurations);
    }

    public ShadowServerConfigurationResponse parseJsonConfigurations(String jsonStr) {
        ShadowServerConfigurationResponse shadowServerConfigurationResponse = new ShadowServerConfigurationResponse();
        Object object = JSONObject.parse(jsonStr);
        Object dataSourceBusinessObj = ((JSONObject)object).get("dataSourceBusiness");
        SingleServerConfiguration dataSourceBusiness = new SingleServerConfiguration();
        dataSourceBusiness.setNodes(String.valueOf(((JSONObject)dataSourceBusinessObj).get("nodes")));
        Object tmpMaster = ((JSONObject)dataSourceBusinessObj).get("master");
        if (tmpMaster != null) {
            dataSourceBusiness.setMaster(String.valueOf(tmpMaster));
        }
        shadowServerConfigurationResponse.setDataSourceBusiness(dataSourceBusiness);

        Object dataSourceBusinessPerformanceTestObj = ((JSONObject)object).get("dataSourceBusinessPerformanceTest");
        SingleServerConfiguration dataSourceBusinessPerformanceTest = new SingleServerConfiguration();
        dataSourceBusinessPerformanceTest.setNodes(
            String.valueOf(((JSONObject)dataSourceBusinessPerformanceTestObj).get("nodes")));
        dataSourceBusinessPerformanceTest.setDatabase(
            String.valueOf(((JSONObject)dataSourceBusinessPerformanceTestObj).get("database")));
        dataSourceBusinessPerformanceTest.setPassword(
            String.valueOf(((JSONObject)dataSourceBusinessPerformanceTestObj).get("password")));
        Object tmpTestMaster = ((JSONObject)dataSourceBusinessPerformanceTestObj).get("master");
        if (tmpTestMaster != null) {
            dataSourceBusinessPerformanceTest.setMaster(String.valueOf(tmpTestMaster));
        }
        shadowServerConfigurationResponse.setDataSourceBusinessPerformanceTest(dataSourceBusinessPerformanceTest);
        return shadowServerConfigurationResponse;
    }

    /**
     * json 配置, 密码脱敏, 替换
     *
     * @param json json 配置
     * @return 安全的 json
     */
    private String getSafeConfigFromServer(String json) {
        // 转对象
        if (StringUtils.isBlank(json)) {
            return "";
        }

        // 替换
        ShadowServerConfigurationResponse response = JsonUtil.json2bean(json, ShadowServerConfigurationResponse.class);

        // 转 json
        SingleServerConfiguration dataSourceBusinessPerformanceTest = response.getDataSourceBusinessPerformanceTest();
        // 这里只判断 pt 的 password (原 password, 不填写)
        if (dataSourceBusinessPerformanceTest.getPassword() == null) {
            return json;
        }

        // tips: 这里不使用 对象转json, 是因为换行格式, 都没了
        String originPasswordField = DsManageUtil.getOriginPasswordFieldAboutJson(
            dataSourceBusinessPerformanceTest.getPassword());
        String newPasswordField = DsManageUtil.getSafePasswordFieldAboutJson();
        return json.replace(originPasswordField, newPasswordField);
    }

}

