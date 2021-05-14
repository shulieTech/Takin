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

package io.shulie.tro.web.data.dao.application;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.amdb.api.ApplicationClient;
import io.shulie.tro.web.amdb.bean.query.application.ApplicationQueryDTO;
import io.shulie.tro.web.amdb.bean.result.application.ApplicationDTO;
import io.shulie.tro.web.amdb.bean.result.application.InstanceInfoDTO;
import io.shulie.tro.web.amdb.bean.result.application.LibraryDTO;
import io.shulie.tro.web.data.mapper.mysql.ApplicationMntMapper;
import io.shulie.tro.web.data.mapper.mysql.TroUserMapper;
import io.shulie.tro.web.data.model.mysql.ApplicationMntEntity;
import io.shulie.tro.web.data.model.mysql.TroUserEntity;
import io.shulie.tro.web.data.param.application.ApplicationCreateParam;
import io.shulie.tro.web.data.param.application.ApplicationQueryParam;
import io.shulie.tro.web.data.param.application.ApplicationUpdateParam;
import io.shulie.tro.web.data.result.application.ApplicationDetailResult;
import io.shulie.tro.web.data.result.application.ApplicationResult;
import io.shulie.tro.web.data.result.application.InstanceInfoResult;
import io.shulie.tro.web.data.result.application.LibraryResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shiyajian
 * create: 2020-09-20
 */
@Component
public class ApplicationDAOImpl implements ApplicationDAO {

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private ApplicationMntMapper applicationMntMapper;

    @Autowired
    private TroUserMapper troUserMapper;

    @Override
    public List<ApplicationDetailResult> getApplications(List<String> appNames) {
        LambdaQueryWrapper<ApplicationMntEntity> query = new LambdaQueryWrapper<>();
        if(appNames != null  && appNames.size() > 0) {
            query.in(ApplicationMntEntity::getApplicationName,appNames);
        }
        List<ApplicationMntEntity> applicationMntEntities = applicationMntMapper.selectList(query);
        if(applicationMntEntities == null || applicationMntEntities.size() == 0) {
            return Lists.newArrayList();
        }
        return applicationMntEntities.stream().map(entity -> {
            ApplicationDetailResult result = new ApplicationDetailResult();
            BeanUtils.copyProperties(entity,result);
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ApplicationResult> getApplicationByName(List<String> appNames) {
        if (CollectionUtils.isEmpty(appNames)) {
            return Lists.newArrayList();
        }
        List<ApplicationResult> applicationResultList = Lists.newArrayList();
        List<ApplicationDTO> applicationDTOTotalList = Lists.newArrayList();
        //分批从amdb获取应用数据
        int BATCH_SIZE = 100;
        List<String> pageAppNameList;
        for (int from = 0, to = 0, size = appNames.size(); from < size; from = to) {
            to = Math.min(from + BATCH_SIZE, size);
            pageAppNameList = appNames.subList(from, to);
            ApplicationQueryDTO queryDTO = new ApplicationQueryDTO();
            queryDTO.setAppNames(String.join(",", pageAppNameList));
            queryDTO.setFields("library,instanceInfo");
            queryDTO.setPageSize(99999);
            PagingList<ApplicationDTO> applicationDTOPagingList = applicationClient.pageApplications(queryDTO);
            if (!applicationDTOPagingList.isEmpty()) {
                List<ApplicationDTO> applicationDTOList = applicationDTOPagingList.getList();
                applicationDTOTotalList.addAll(applicationDTOList);
            }
            if (CollectionUtils.isEmpty(applicationDTOTotalList)) {
                return applicationResultList;
            }
        }

        return toAppResult(applicationResultList, applicationDTOTotalList);
    }

    private List<ApplicationResult> toAppResult(List<ApplicationResult> applicationResultList,
        List<ApplicationDTO> applicationDTOTotalList) {
        List<String> appName = applicationDTOTotalList.stream().map(ApplicationDTO::getAppName).collect(
            Collectors.toList());
        LambdaQueryWrapper<ApplicationMntEntity> query = new LambdaQueryWrapper<>();
        query.in(ApplicationMntEntity::getApplicationName, appName);
        List<ApplicationMntEntity> applicationMntEntities = applicationMntMapper.selectList(query);
        /* key：应用名称，value：userId */
        Map<String, Long> appNameUserIdMap = Maps.newHashMap();
        /* key：应用名称，value：用户名称 */
        Map<String, String> appNameUserNameMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(applicationMntEntities)) {
            applicationMntEntities.forEach(localApp -> {
                appNameUserIdMap.computeIfAbsent(localApp.getApplicationName(), k -> localApp.getUserId());
            });
            List<Long> userIds = applicationMntEntities.stream().map(ApplicationMntEntity::getUserId).distinct()
                .collect(Collectors.toList());
            List<TroUserEntity> troUserEntities = troUserMapper.selectBatchIds(userIds);
            Map<Long, String> userIdUserNameMap = troUserEntities.stream().collect(
                Collectors.toMap(TroUserEntity::getId, TroUserEntity::getName));
            for (Entry<String, Long> entry : appNameUserIdMap.entrySet()) {
                String k = entry.getKey();
                Long v = entry.getValue();
                String value = appNameUserNameMap.get(k);
                if (value == null) {
                    appNameUserNameMap.put(k, userIdUserNameMap.get(v));
                }
            }
        }

        applicationDTOTotalList.forEach(applicationDTO -> {
            ApplicationResult applicationResult = new ApplicationResult();
            applicationResult.setAppId(applicationDTO.getAppId());
            applicationResult.setAppName(applicationDTO.getAppName());
            applicationResult.setAppVersionCode(applicationDTO.getAppVersionCode());
            applicationResult.setAppIsException(applicationDTO.getAppIsException());
            applicationResult.setAppManagerName(appNameUserNameMap.get(applicationDTO.getAppName()));
            applicationResult.setManagerUserId(appNameUserIdMap.get(applicationDTO.getAppName()));
            applicationResult.setAppUpdateTime(applicationDTO.getAppUpdateTime());
            applicationResult.setAppSummary(applicationDTO.getAppSummary());
            LibraryDTO[] libraryDTOArray = applicationDTO.getLibrary();
            List<LibraryResult> libraryResultList = Lists.newArrayList();
            if (libraryDTOArray.length > 0) {
                for (LibraryDTO libraryDTO : libraryDTOArray) {
                    LibraryResult libraryResult = new LibraryResult();
                    BeanUtils.copyProperties(libraryDTO, libraryResult);
                    libraryResultList.add(libraryResult);
                }
            }
            LibraryResult[] libraryResults = new LibraryResult[libraryResultList.size()];
            libraryResults = libraryResultList.toArray(libraryResults);
            applicationResult.setLibrary(libraryResults);
            InstanceInfoDTO instanceInfoDTO = applicationDTO.getInstanceInfo();
            if (!Objects.isNull(instanceInfoDTO)) {
                InstanceInfoResult instanceInfoResult = new InstanceInfoResult();
                instanceInfoResult.setInstanceAmount(instanceInfoDTO.getInstanceAmount());
                instanceInfoResult.setInstanceOnlineAmount(instanceInfoDTO.getInstanceOnlineAmount());
                applicationResult.setInstanceInfo(instanceInfoResult);
                applicationResultList.add(applicationResult);
            }
        });
        return applicationResultList;
    }

    @Override
    public List<ApplicationDetailResult> getApplicationListByUserIds(List<Long> userIdList) {
        List<ApplicationDetailResult> applicationDetailResultList = Lists.newArrayList();
        LambdaUpdateWrapper<ApplicationMntEntity> wrapper = new LambdaUpdateWrapper();
        if (!CollectionUtils.isEmpty(userIdList)) {
            wrapper.in(ApplicationMntEntity::getUserId, userIdList);
        }
        List<ApplicationMntEntity> entityList = applicationMntMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return applicationDetailResultList;
        }
        applicationDetailResultList = entityList.stream().map(applicationMntEntity -> {
            ApplicationDetailResult result = new ApplicationDetailResult();
            result.setApplicationId(applicationMntEntity.getApplicationId());
            result.setApplicationName(applicationMntEntity.getApplicationName());
            return result;
        }).collect(Collectors.toList());
        return applicationDetailResultList;
    }

    @Override
    public List<ApplicationResult> getApplicationByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Lists.newArrayList();
        }
        return null;
    }

    @Override
    public List<ApplicationDetailResult> getApplicationList(ApplicationQueryParam param) {
        LambdaUpdateWrapper<ApplicationMntEntity> wrapper = new LambdaUpdateWrapper();
        if(param.getCustomerId() != null) {
            wrapper.eq(ApplicationMntEntity::getCustomerId,param.getCustomerId());
        }
        List<ApplicationMntEntity> entityList = applicationMntMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return Lists.newArrayList();
        }
       return entityList.stream().map(applicationMntEntity -> {
            ApplicationDetailResult result = new ApplicationDetailResult();
            BeanUtils.copyProperties(applicationMntEntity,result);
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllApplicationName(ApplicationQueryParam param) {
        LambdaQueryWrapper<ApplicationMntEntity> wrapper = new LambdaQueryWrapper<>();
        if(param.getCustomerId() != null) {
            wrapper.eq(ApplicationMntEntity::getCustomerId,param.getCustomerId());
        }
        if(param.getUserId() != null) {
            wrapper.eq(ApplicationMntEntity::getUserId,param.getUserId());
        }
        List<ApplicationMntEntity> entities = applicationMntMapper.selectList(wrapper);
        return entities.stream()
            .map(ApplicationMntEntity::getApplicationName)
            .filter(applicationName -> !StringUtils.isEmpty(applicationName)).collect(Collectors.toList());
    }

    @Override
    public List<ApplicationDetailResult> getApplicationList(List<String> appNames) {
        List<ApplicationDetailResult> applicationDetailResultList = Lists.newArrayList();
        LambdaUpdateWrapper<ApplicationMntEntity> wrapper = new LambdaUpdateWrapper();
        if (appNames != null && appNames.size() > 0) {
            wrapper.in(ApplicationMntEntity::getApplicationName, appNames);
        }
        List<ApplicationMntEntity> entityList = applicationMntMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return applicationDetailResultList;
        }
        applicationDetailResultList = entityList.stream().map(applicationMntEntity -> {
            ApplicationDetailResult result = new ApplicationDetailResult();
            BeanUtils.copyProperties(applicationMntEntity, result);
            return result;
        }).collect(Collectors.toList());
        return applicationDetailResultList;
    }

    @Override
    public int insert(ApplicationCreateParam param) {
        ApplicationMntEntity entity = new ApplicationMntEntity();
        BeanUtils.copyProperties(param, entity);
        return applicationMntMapper.insert(entity);
    }

    @Override
    public ApplicationDetailResult getApplicationById(Long appId) {
        ApplicationMntEntity applicationMntEntity = applicationMntMapper.selectById(appId);
        if (!Objects.isNull(applicationMntEntity)) {
            ApplicationDetailResult detailResult = new ApplicationDetailResult();
            BeanUtils.copyProperties(applicationMntEntity, detailResult);
            return detailResult;
        }
        return null;
    }

    @Override
    public ApplicationDetailResult getApplicationByCustomerIdAndName(Long customerId, String appName) {
        if (!Objects.isNull(customerId) && !StringUtils.isEmpty(appName)) {
            LambdaQueryWrapper<ApplicationMntEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ApplicationMntEntity::getCustomerId, customerId);
            queryWrapper.eq(ApplicationMntEntity::getApplicationName, appName);
            ApplicationMntEntity applicationMntEntity = applicationMntMapper.selectOne(queryWrapper);
            if (!Objects.isNull(applicationMntEntity)) {
                ApplicationDetailResult detailResult = new ApplicationDetailResult();
                BeanUtils.copyProperties(applicationMntEntity, detailResult);
                return detailResult;
            }
        }
        return null;
    }

    /**
     * 指定责任人-应用管理
     *
     * @param param
     * @return
     */
    @Override
    public int allocationUser(ApplicationUpdateParam param) {
        LambdaUpdateWrapper<ApplicationMntEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(ApplicationMntEntity::getUserId, param.getUserId())
            .eq(ApplicationMntEntity::getApplicationId, param.getId());
        return applicationMntMapper.update(null, wrapper);
    }

}
