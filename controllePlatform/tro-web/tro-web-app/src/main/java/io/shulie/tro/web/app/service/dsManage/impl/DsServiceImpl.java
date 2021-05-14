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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.pamirs.tro.common.enums.ds.DbTypeEnum;
import com.pamirs.tro.common.enums.ds.DsTypeEnum;
import com.pamirs.tro.entity.dao.confcenter.TApplicationMntDao;
import com.pamirs.tro.entity.dao.simplify.TAppBusinessTableInfoMapper;
import com.pamirs.tro.entity.dao.user.TUserMapper;
import com.pamirs.tro.entity.domain.entity.DsModelWithBLOBs;
import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import com.pamirs.tro.entity.domain.entity.simplify.AppBusinessTableInfo;
import com.pamirs.tro.entity.domain.entity.user.User;
import com.pamirs.tro.entity.domain.query.agent.AppBusinessTableQuery;
import com.pamirs.tro.entity.domain.vo.dsmanage.Configurations;
import com.pamirs.tro.entity.domain.vo.dsmanage.DsAgentVO;
import com.pamirs.tro.entity.domain.vo.dsmanage.DsServerVO;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.request.application.ApplicationDsCreateRequest;
import io.shulie.tro.web.app.request.application.ApplicationDsDeleteRequest;
import io.shulie.tro.web.app.request.application.ApplicationDsEnableRequest;
import io.shulie.tro.web.app.request.application.ApplicationDsUpdateRequest;
import io.shulie.tro.web.app.response.application.ApplicationDsDetailResponse;
import io.shulie.tro.web.app.response.application.ApplicationDsResponse;
import io.shulie.tro.web.app.response.application.DbTypeResponse;
import io.shulie.tro.web.app.response.application.DsTypeResponse;
import io.shulie.tro.web.app.response.application.ShadowServerConfigurationResponse;
import io.shulie.tro.web.app.service.dsManage.AbstractDsService;
import io.shulie.tro.web.app.service.dsManage.DsService;
import io.shulie.tro.web.data.dao.application.ApplicationDsDAO;
import io.shulie.tro.web.data.param.application.ApplicationDsQueryParam;
import io.shulie.tro.web.data.param.application.ApplicationDsUpdateParam;
import io.shulie.tro.web.data.result.application.ApplicationDsResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: fanxx
 * @Date: 2020/3/12 下午3:40
 * @Description:
 */
@Service
@Slf4j
public class DsServiceImpl implements DsService {

    @Resource
    private TApplicationMntDao applicationMntDao;

    @Resource
    private TAppBusinessTableInfoMapper TAppBusinessTableInfoMapper;

    @Resource
    private TUserMapper TUserMapper;

    @Autowired
    private ApplicationDsDAO applicationDsDAO;

    private Map<DsTypeEnum, AbstractDsService> map;

    @Autowired
    private ShadowDbServiceImpl shadowDbService;

    @Autowired
    private ShadowTableServiceImpl shadowTableService;

    @Autowired
    private ShadowRedisServerServiceImpl shadowRedisServerService;

    @Autowired
    private ShadowEsServiceImpl shadowEsService;

    @Autowired
    private ShadowHbaseServiceImpl shadowHbaseService;

    @Autowired
    private ShadowKafkaServiceImpl shadowKafkaService;

    @Resource
    private com.pamirs.tro.entity.dao.user.TUserMapper tUserMapper;

    @PostConstruct
    public void init() {
        map = new HashMap<>(6);
        map.put(DsTypeEnum.SHADOW_DB, shadowDbService);
        map.put(DsTypeEnum.SHADOW_TABLE, shadowTableService);
        map.put(DsTypeEnum.SHADOW_REDIS_SERVER, shadowRedisServerService);
        map.put(DsTypeEnum.SHADOW_ES_SERVER, shadowEsService);
        map.put(DsTypeEnum.SHADOW_HBASE_SERVER, shadowHbaseService);
        map.put(DsTypeEnum.SHADOW_KAFKA_CLUSTER, shadowKafkaService);
    }

    @Override
    public Response dsUpdate(ApplicationDsUpdateRequest updateRequest) {
        if (updateRequest == null) {
            return Response.fail("updateRequest obj is null");
        }
        AbstractDsService dsService = getAbstractDsService(updateRequest.getDsType().intValue());
        if (dsService == null) {
            return Response.fail("dsService obj is null");
        }
        return dsService.dsUpdate(updateRequest);
    }

    @Override
    public Response dsQuery(Long applicationId) {
        if (null == applicationId) {
            return Response.fail("0", "参数缺失");
        }
        ApplicationDsQueryParam queryParam = new ApplicationDsQueryParam();
        queryParam.setApplicationId(applicationId);
        queryParam.setIsDeleted(0);
        queryParam.setUserIdList(RestContext.getQueryAllowUserIdList());
        List<ApplicationDsResult> dsResultList = applicationDsDAO.queryList(queryParam);
        List<ApplicationDsResponse> dsResponseList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(dsResultList)) {
            dsResponseList = dsResultList.stream().map(dsResult -> {
                ApplicationDsResponse dsResponse = new ApplicationDsResponse();
                dsResponse.setId(dsResult.getId());
                dsResponse.setApplicationId(dsResult.getApplicationId());
                DbTypeResponse dbTypeResponse = new DbTypeResponse();
                dbTypeResponse.setLabel(DbTypeEnum.getDescByCode(dsResult.getDbType()));
                dbTypeResponse.setValue(dsResult.getDbType());
                dsResponse.setDbType(dbTypeResponse);

                DsTypeResponse dsTypeResponse = new DsTypeResponse();
                dsTypeResponse.setLabel(DsTypeEnum.getDescByCode(dsResult.getDsType()));
                dsTypeResponse.setValue(dsResult.getDsType());
                dsResponse.setDsType(dsTypeResponse);
                dsResponse.setUrl(dsResult.getUrl());
                dsResponse.setStatus(dsResult.getStatus());
                dsResponse.setUpdateTime(dsResult.getUpdateTime());

                dsResponse.setPermissionUserId(dsResult.getUserId());
                return dsResponse;
            }).collect(Collectors.toList());
        }
        return Response.success(dsResponseList);
    }

    @Override
    public Response<ApplicationDsDetailResponse> dsQueryDetail(Long dsId, boolean isOldVersion) {
        ApplicationDsResult dsResult = applicationDsDAO.queryByPrimaryKey(dsId);
        if (dsResult == null) {
            return Response.fail("dataSource obj is null");
        }
        AbstractDsService dsService = getAbstractDsService(dsResult.getDsType());
        if (dsService == null) {
            return Response.fail("dsService obj is null");
        }
        return dsService.dsQueryDetail(dsId, isOldVersion);
    }

    @Override
    public Response enableConfig(ApplicationDsEnableRequest enableRequest) {
        ApplicationDsResult dsResult = applicationDsDAO.queryByPrimaryKey(enableRequest.getId());
        if (Objects.isNull(dsResult)) {
            return Response.fail("0", "该配置不存在");
        }

        AbstractDsService dsService = getAbstractDsService(dsResult.getDsType());
        if (dsService == null) {
            return Response.fail("dsService obj is null");
        }
        return dsService.enableConfig(enableRequest);
    }

    private AbstractDsService getAbstractDsService(Integer code) {
        DsTypeEnum dsTypeEnum = DsTypeEnum.getEnumByCode(code);
        return map.get(dsTypeEnum);
    }

    @Override
    public List<DsAgentVO> getConfigs(String appName) {
        List<DsAgentVO> dsAgentVOList = new ArrayList<>();
        User user = RestContext.getUser();
        if (user == null) {
            String userAppKey = RestContext.getTenantUserKey();
            user = TUserMapper.selectByKey(userAppKey);
        }
        TApplicationMnt applicationMnt = applicationMntDao.queryApplicationInfoByNameAndTenant(appName,
            user == null ? null : user.getId());
        if (applicationMnt != null) {
            List<DsModelWithBLOBs> dsModels = applicationDsDAO.selectByAppIdForAgent(applicationMnt.getApplicationId());
            if (CollectionUtils.isNotEmpty(dsModels)) {
                dsModels = dsModels.stream()
                    .filter(
                        dsModel -> dsModel.getDsType().equals(new Byte(String.valueOf(DsTypeEnum.SHADOW_DB.getCode())))
                            || dsModel.getDsType().equals(new Byte(String.valueOf(DsTypeEnum.SHADOW_TABLE.getCode()))))
                    .collect(Collectors.toList());
                for (DsModelWithBLOBs dsModel : dsModels) {
                    DsAgentVO vo = new DsAgentVO();
                    vo.setApplicationName(dsModel.getApplicationName());
                    vo.setDsType(dsModel.getDsType());
                    vo.setStatus(dsModel.getStatus());
                    vo.setUrl(dsModel.getUrl());
                    if (dsModel.getDsType() == 0) {
                        //影子库
                        if (dsModel.getParseConfig() != null && !dsModel.getParseConfig().isEmpty()) {
                            vo.setShadowDbConfig(JSON.parseObject(dsModel.getParseConfig(),Configurations.class));
                        }
                    } else if (dsModel.getDsType() == 1) {
                        //影子表
                        vo.setShadowTableConfig(dsModel.getConfig());
                    }
                    dsAgentVOList.add(vo);
                }
            }
        }
        return dsAgentVOList;
    }

    @Override
    public List<ShadowServerConfigurationResponse> getShadowServerConfigs(String appName) {
        return shadowRedisServerService.getShadowServerConfigs(appName);
    }


    @Override
    public List<DsServerVO> getShadowDsServerConfigs(String namespace, DsTypeEnum dsServer) {
        List<DsServerVO> responseList = new ArrayList<>();
        String userAppKey = RestContext.getTenantUserKey();
        User user = tUserMapper.selectByKey(userAppKey);
        TApplicationMnt applicationMnt = applicationMntDao.queryApplicationInfoByNameAndTenant(namespace,
            user == null ? null : user.getId());
        if (applicationMnt != null) {
            List<DsModelWithBLOBs> dsModels = applicationDsDAO.selectByAppIdForAgent(applicationMnt.getApplicationId());
            if (CollectionUtils.isNotEmpty(dsModels)) {
                dsModels = dsModels.stream()
                    .filter(dsModel -> dsModel.getDsType().equals(new Byte(String.valueOf(dsServer.getCode()))))
                    .collect(Collectors.toList());
            }
            for (DsModelWithBLOBs dsModel : dsModels) {
                DsServerVO serverVO = new DsServerVO();
                BeanUtils.copyProperties(dsModel,serverVO);
                // agent需要返回0
                serverVO.setStatus(0);
                responseList.add(serverVO);
            }
        }
        return responseList;
    }


    @Override
    public void addBusiness(AppBusinessTableInfo info) {
        AppBusinessTableInfo query = new AppBusinessTableInfo();
        query.setUrl(info.getUrl());
        query.setUserId(info.getUserId());
        Long count = TAppBusinessTableInfoMapper.selectCountByUserIdAndUrl(query);
        if (count == 1) {
            AppBusinessTableInfo updateInfo = TAppBusinessTableInfoMapper.selectByUserIdAndUrl(query);
            if (null != updateInfo) {
                info.setId(updateInfo.getId());
                TAppBusinessTableInfoMapper.update(info);
            }
        } else if (count < 1) {
            TAppBusinessTableInfoMapper.insert(info);
        } else {
            throw new IllegalArgumentException(String.format("count:%s, url : %s Url大于 1", count, info.getUrl()));
        }
    }

    @Override
    public Response queryPageBusiness(AppBusinessTableQuery query) {
        User user = RestContext.getUser();
        if (1 == user.getRole()) {
            query.setUserId(user.getId());
        }
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<AppBusinessTableInfo> list = TAppBusinessTableInfoMapper.selectList(query);
        PageInfo<AppBusinessTableInfo> page = new PageInfo<>(list);
        page.getList().stream().forEach(info -> {
            int index = info.getUrl().indexOf("?");
            if (-1 != index) {
                String substring = info.getUrl().substring(0, index);
                info.setUrl(substring);
            }
        });
        return Response.success(page.getList(), page.getTotal());
    }

    @Override
    public List<DsModelWithBLOBs> getAllEnabledDbConfig(Long applicationId) {
        return applicationDsDAO.getAllEnabledDbConfig(applicationId);
    }

    @Override
    public Response secureInit() {
        ApplicationDsQueryParam param = new ApplicationDsQueryParam();
        List<ApplicationDsResult> applicationDsResults = applicationDsDAO.queryList(param);
        applicationDsResults.stream().forEach(applicationDsResult -> {
            ApplicationDsUpdateParam update = new ApplicationDsUpdateParam();
            BeanUtils.copyProperties(applicationDsResult,update);
            applicationDsDAO.update(update);
        });
        return Response.success();
    }


    @Override
    public String parseShadowDbUrl(String config) {
        return shadowDbService.parseShadowDbUrl(config);
    }

    @Override
    public Response dsAdd(ApplicationDsCreateRequest createRequest) {
        if (Objects.isNull(createRequest)) {
            return Response.fail("0", "该配置不存在");
        }
        AbstractDsService dsService = getAbstractDsService(createRequest.getDsType());
        if (dsService == null) {
            return Response.fail("dsService obj is null");
        }
        return dsService.dsAdd(createRequest);
    }

    

    @Override
    public Response dsDelete(ApplicationDsDeleteRequest dsDeleteRequest) {

        ApplicationDsResult dsResult = applicationDsDAO.queryByPrimaryKey(dsDeleteRequest.getId());
        if (Objects.isNull(dsResult)) {
            return Response.fail("0", "该配置不存在");
        }
        AbstractDsService dsService = getAbstractDsService(dsResult.getDsType());
        if (dsService == null) {
            return Response.fail("dsService obj is null");
        }
        return dsService.dsDelete(dsDeleteRequest);
    }
}
