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

package io.shulie.tro.web.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.pamirs.tro.common.util.MD5Util;
import io.shulie.amdb.common.dto.link.entrance.ServiceInfoDTO;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.amdb.api.ApplicationEntranceClient;
import io.shulie.tro.web.amdb.enums.MiddlewareTypeGroupEnum;
import io.shulie.tro.web.app.agent.vo.ShadowConsumerVO;
import io.shulie.tro.web.app.cache.AgentConfigCacheManager;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.constant.BizOpConstants.OpTypes;
import io.shulie.tro.web.app.constant.BizOpConstants.Vars;
import io.shulie.tro.web.app.context.OperationLogContextHolder;
import io.shulie.tro.web.app.request.application.*;
import io.shulie.tro.web.app.request.application.ShadowConsumersOperateRequest.ShadowConsumerOperateRequest;
import io.shulie.tro.web.app.response.application.ShadowConsumerResponse;
import io.shulie.tro.web.app.service.ShadowConsumerService;
import io.shulie.tro.web.common.constant.ShadowConsumerConstants;
import io.shulie.tro.web.common.enums.shadow.ShadowMqConsumerType;
import io.shulie.tro.web.data.dao.application.ApplicationDAO;
import io.shulie.tro.web.data.dao.application.ShadowMqConsumerDAO;
import io.shulie.tro.web.data.mapper.mysql.ShadowMqConsumerMapper;
import io.shulie.tro.web.data.model.mysql.ShadowMqConsumerEntity;
import io.shulie.tro.web.data.result.application.ApplicationDetailResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * @author shiyajian
 * create: 2021-02-04
 */
@Service
@Slf4j
public class ShadowConsumerServiceImpl implements ShadowConsumerService {

    @Resource
    private ShadowMqConsumerMapper shadowMqConsumerMapper;

    @Autowired
    private ApplicationDAO applicationDAO;

    @Autowired
    private ApplicationEntranceClient applicationEntranceClient;

    @Autowired
    private AgentConfigCacheManager agentConfigCacheManager;

    @Autowired
    private ShadowMqConsumerDAO shadowMqConsumerDAO;

    @Override
    public List<ShadowConsumerResponse> getShadowConsumersByApplicationId(long applicationId) {
        LambdaQueryWrapper<ShadowMqConsumerEntity> query = new LambdaQueryWrapper<>();
        query.eq(ShadowMqConsumerEntity::getApplicationId, applicationId);
        query.eq(ShadowMqConsumerEntity::getStatus, ShadowConsumerConstants.ENABLE);
        List<ShadowMqConsumerEntity> shadowMqConsumerEntities = shadowMqConsumerMapper.selectList(query);
        if (CollectionUtils.isEmpty(shadowMqConsumerEntities)) {
            return Lists.newArrayList();
        }
        return shadowMqConsumerEntities.stream().map(entry -> {
            ShadowConsumerResponse response = new ShadowConsumerResponse();
            response.setId(entry.getId());
            response.setUnionId(null);
            response.setType(ShadowMqConsumerType.of(entry.getType()));
            response.setTopicGroup(entry.getTopicGroup());
            response.setGmtCreate(entry.getCreateTime());
            response.setGmtUpdate(entry.getUpdateTime());
            response.setEnabled(entry.getStatus() == ShadowConsumerConstants.ENABLE);
            response.setDeleted(entry.getDeleted());
            response.setFeature(entry.getFeature());
            return response;
        }).collect(Collectors.toList());
    }

    @Override
    public ShadowConsumerResponse getMqConsumerById(Long id) {
        ShadowMqConsumerEntity entity = shadowMqConsumerMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        ShadowConsumerResponse response = new ShadowConsumerResponse();
        response.setId(entity.getId());
        response.setUnionId(null);
        response.setType(ShadowMqConsumerType.of(entity.getType()));
        response.setTopicGroup(entity.getTopicGroup());
        response.setEnabled(entity.getStatus() == ShadowConsumerConstants.ENABLE);
        response.setGmtCreate(entity.getCreateTime());
        response.setGmtUpdate(entity.getUpdateTime());
        return response;
    }

    @Override
    public PagingList<ShadowConsumerResponse> pageMqConsumers(ShadowConsumerQueryRequest request) {
        ApplicationDetailResult application = applicationDAO.getApplicationById(request.getApplicationId());
        if (application == null) {
            throw new RuntimeException(String.format("应用id:%s对应的应用不存在", request.getApplicationId()));
        }
        LambdaQueryWrapper<ShadowMqConsumerEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(request.getTopicGroup())) {
            lambdaQueryWrapper.like(ShadowMqConsumerEntity::getTopicGroup, request.getTopicGroup());
        }
        if (request.getType() != null) {
            lambdaQueryWrapper.eq(ShadowMqConsumerEntity::getType, request.getType().name());
        }
        if (request.getEnabled() != null) {
            lambdaQueryWrapper.eq(ShadowMqConsumerEntity::getStatus,
                request.getEnabled() ? ShadowConsumerConstants.ENABLE : ShadowConsumerConstants.DISABLE);
        }
        if (CollectionUtils.isNotEmpty(RestContext.getQueryAllowUserIdList())) {
            lambdaQueryWrapper.in(ShadowMqConsumerEntity::getUserId, RestContext.getQueryAllowUserIdList());
        }
        List<ShadowConsumerResponse> totalResult;
        lambdaQueryWrapper.eq(ShadowMqConsumerEntity::getApplicationId, request.getApplicationId());
        lambdaQueryWrapper.eq(ShadowMqConsumerEntity::getDeleted, ShadowConsumerConstants.LIVED);
        List<ShadowMqConsumerEntity> dbResult = shadowMqConsumerMapper.selectList(lambdaQueryWrapper);
        List<ShadowMqConsumerEntity> amdbResult = Lists.newArrayList();
        if (request.getEnabled() == null) {
            amdbResult = queryAmdbDefaultEntrances(request, application.getApplicationName());
        }
        totalResult = mergeResult(amdbResult, dbResult);
        totalResult = filterResult(request, totalResult);
        return splitPage(request, totalResult);
    }

    private List<ShadowConsumerResponse> filterResult(ShadowConsumerQueryRequest request,
        List<ShadowConsumerResponse> totalResult) {
        if (request.getEnabled() != null) {
            if (request.getEnabled()) {
                totalResult = totalResult.stream().filter(ShadowConsumerResponse::getEnabled).collect(
                    Collectors.toList());
            } else {
                totalResult = totalResult.stream().filter(e -> !e.getEnabled()).collect(Collectors.toList());
            }
        }
        if (StringUtils.isNotBlank(request.getTopicGroup())) {
            totalResult = totalResult.stream().filter(e -> e.getTopicGroup().contains(request.getTopicGroup())).collect(
                Collectors.toList());
        }
        if (request.getType() != null) {
            totalResult = totalResult.stream().filter(e -> e.getType() == request.getType()).collect(
                Collectors.toList());
        }
        return totalResult;
    }

    private List<ShadowConsumerResponse> mergeResult(List<ShadowMqConsumerEntity> amdbResult,
        List<ShadowMqConsumerEntity> dbResult) {
        List<Long> allowUpdateUserIdList = RestContext.getUpdateAllowUserIdList();
        List<Long> allowDeleteUserIdList = RestContext.getDeleteAllowUserIdList();
        List<Long> allowEnableDisableUserIdList = RestContext.getEnableDisableAllowUserIdList();
        Map<String, ShadowConsumerResponse> amdbMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(amdbResult)) {
            amdbMap = amdbResult.stream()
                .map(e -> {
                    ShadowConsumerResponse response = new ShadowConsumerResponse();
                    response.setUnionId(
                        MD5Util.getMD5(e.getApplicationName() + "#" + e.getTopicGroup() + "#" + e.getType()));
                    response.setType(ShadowMqConsumerType.of(e.getType()));
                    response.setTopicGroup(e.getTopicGroup());
                    response.setEnabled(e.getStatus() == ShadowConsumerConstants.ENABLE);
                    response.setGmtCreate(e.getCreateTime());
                    response.setGmtUpdate(e.getUpdateTime());
                    response.setCanEdit(false);
                    response.setCanRemove(false);
                    response.setCanEnableDisable(false);
                    return response;
                })
                .collect(Collectors.toMap(ShadowConsumerResponse::getUnionId, e -> e, (oV, nV) -> nV));
        }
        Map<String, ShadowConsumerResponse> dbMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(dbResult)) {
            dbMap = dbResult.stream()
                .map(e -> {
                    ShadowConsumerResponse response = new ShadowConsumerResponse();
                    response.setId(e.getId());
                    response.setUnionId(
                        MD5Util.getMD5(e.getApplicationName() + "#" + e.getTopicGroup() + "#" + e.getType()));
                    response.setType(ShadowMqConsumerType.of(e.getType()));
                    response.setTopicGroup(e.getTopicGroup());
                    response.setEnabled(e.getStatus() == ShadowConsumerConstants.ENABLE);
                    response.setGmtCreate(e.getCreateTime());
                    response.setGmtUpdate(e.getUpdateTime());
                    if (CollectionUtils.isNotEmpty(allowEnableDisableUserIdList)) {
                        response.setCanEnableDisable(allowEnableDisableUserIdList.contains(e.getUserId()));
                    }
                    if (CollectionUtils.isNotEmpty(allowDeleteUserIdList)) {
                        response.setCanRemove(allowDeleteUserIdList.contains(e.getUserId()));
                    }
                    if (CollectionUtils.isNotEmpty(allowUpdateUserIdList)) {
                        response.setCanEdit(allowUpdateUserIdList.contains(e.getUserId()));
                    }
                    return response;
                })
                .collect(Collectors.toMap(ShadowConsumerResponse::getUnionId, e -> e, (oV, nV) -> nV));
        }
        // 在amdb自动梳理的基础上，补充数据库里面的记录，有的话用数据的记录
        for (Entry<String, ShadowConsumerResponse> dbEntry : dbMap.entrySet()) {
            amdbMap.merge(dbEntry.getKey(), dbEntry.getValue(), (amdbValue, dbValue) -> {
                dbValue.setCanEdit(false);
                dbValue.setCanRemove(false);
                dbValue.setCanEnableDisable(dbValue.getCanEnableDisable());
                return dbValue;
            });
        }
        return Lists.newArrayList(amdbMap.values());
    }

    private List<ShadowMqConsumerEntity> queryAmdbDefaultEntrances(ShadowConsumerQueryRequest request,
        String applicationName) {
        List<ServiceInfoDTO> mqTopicGroups = applicationEntranceClient.getMqTopicGroups(applicationName);
        if (CollectionUtils.isEmpty(mqTopicGroups)) {
            return Lists.newArrayList();
        }
        return mqTopicGroups.stream().map(mqTopicGroup -> {
            ShadowMqConsumerEntity shadowMqConsumerEntity = new ShadowMqConsumerEntity();
            shadowMqConsumerEntity.setTopicGroup(
                mqTopicGroup.getServiceName() + "#" + mqTopicGroup.getMethodName());
            shadowMqConsumerEntity.setType(MiddlewareTypeGroupEnum.getMiddlewareGroupType(mqTopicGroup.getMiddlewareName()).getType());
            shadowMqConsumerEntity.setApplicationId(request.getApplicationId());
            shadowMqConsumerEntity.setApplicationName(applicationName);
            shadowMqConsumerEntity.setStatus(ShadowConsumerConstants.DISABLE);
            shadowMqConsumerEntity.setCustomerId(RestContext.getUser().getCustomerId());
            shadowMqConsumerEntity.setUserId(RestContext.getUser().getId());
            shadowMqConsumerEntity.setDeleted(ShadowConsumerConstants.LIVED);
            return shadowMqConsumerEntity;
        }).collect(Collectors.toList());
    }

    private PagingList<ShadowConsumerResponse> splitPage(
        ShadowConsumerQueryRequest request,
        List<ShadowConsumerResponse> responses) {
        responses.sort((o1, o2) -> {
            if (o1.getGmtCreate() != null && o2.getGmtCreate() != null) {
                int firstSort =  -o1.getGmtCreate().compareTo(o2.getGmtCreate());
                if (firstSort == 0) {
                    return -o1.getId().compareTo(o2.getId());
                }
                return firstSort;
            }
            return 1;
        });
        int total = responses.size();
        int start = Math.min(request.getOffset(), total);
        int end = Math.min((request.getOffset() + request.getPageSize()), total);
        responses = responses.subList(start, end);
        return PagingList.of(responses, total);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void createMqConsumers(ShadowConsumerCreateRequest request) {
        if (!request.getTopicGroup().contains("#")) {
            throw new RuntimeException("请求参数不正确，Group和Topic以#号拼接");
        }
        String[] split = request.getTopicGroup().split("#");
        if (split.length != 2) {
            throw new RuntimeException("请求参数不正确，Group和Topic中间包含超过1个#");
        }
        ApplicationDetailResult application = applicationDAO.getApplicationById(request.getApplicationId());
        if (application == null) {
            throw new RuntimeException(String.format("应用id:%s对应的应用不存在", request.getApplicationId()));
        }
        List<ShadowMqConsumerEntity> exists = getExists(request.getTopicGroup(), request.getApplicationId(),
            request.getType());
        if (CollectionUtils.isNotEmpty(exists)) {
            throw new RuntimeException(
                String.format("类型为[%s]，对应的[%s]已存在", request.getType().name(), request.getTopicGroup()));
        }
        ApplicationDetailResult applicationDetailResult = applicationDAO.getApplicationById(request.getApplicationId());
        OperationLogContextHolder.operationType(OpTypes.CREATE);
        OperationLogContextHolder.addVars(Vars.CONSUMER_TYPE, request.getType().name());
        OperationLogContextHolder.addVars(Vars.CONSUMER_TOPIC_GROUP, request.getTopicGroup());
        ShadowMqConsumerEntity shadowMqConsumerEntity = new ShadowMqConsumerEntity();
        shadowMqConsumerEntity.setTopicGroup(request.getTopicGroup());
        shadowMqConsumerEntity.setType(request.getType().name());
        shadowMqConsumerEntity.setApplicationId(application.getApplicationId());
        shadowMqConsumerEntity.setApplicationName(application.getApplicationName());

        Integer status = request.getStatus() == null ? ShadowConsumerConstants.DISABLE : request.getStatus();
        shadowMqConsumerEntity.setStatus(status);
        shadowMqConsumerEntity.setDeleted(ShadowConsumerConstants.LIVED);
        shadowMqConsumerEntity.setCustomerId(applicationDetailResult.getCustomerId());
        shadowMqConsumerEntity.setUserId(applicationDetailResult.getUserId());
        shadowMqConsumerMapper.insert(shadowMqConsumerEntity);
        agentConfigCacheManager.evictShadowConsumer(application.getApplicationName());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void updateMqConsumers(ShadowConsumerUpdateRequest request) {
        if (!request.getTopicGroup().contains("#")) {
            throw new RuntimeException("请求参数不正确，Group和Topic以#号拼接");
        }
        String[] split = request.getTopicGroup().split("#");
        if (split.length != 2) {
            throw new RuntimeException("请求参数不正确，Group和Topic中间包含超过1个#");
        }
        ApplicationDetailResult application = applicationDAO.getApplicationById(request.getApplicationId());
        if (application == null) {
            throw new RuntimeException(String.format("应用id:%s对应的应用不存在", request.getApplicationId()));
        }
        List<ShadowMqConsumerEntity> exists = getExists(request.getTopicGroup(), request.getApplicationId(),
            request.getType());
        // 同名的自己不算
        exists = exists.stream().filter(item -> !item.getId().equals(request.getId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(exists)) {
            throw new RuntimeException(
                String.format("类型为[%s]，对应的[%s]已存在", request.getType().name(), request.getTopicGroup()));
        }
        OperationLogContextHolder.operationType(OpTypes.UPDATE);
        OperationLogContextHolder.addVars(Vars.CONSUMER_TYPE, request.getType().name());
        OperationLogContextHolder.addVars(Vars.CONSUMER_TOPIC_GROUP, request.getTopicGroup());
        ShadowMqConsumerEntity updateEntity = new ShadowMqConsumerEntity();
        updateEntity.setId(request.getId());
        updateEntity.setTopicGroup(request.getTopicGroup());
        updateEntity.setType(request.getType().name());
        updateEntity.setStatus(request.getStatus());
        shadowMqConsumerMapper.updateById(updateEntity);
        agentConfigCacheManager.evictShadowConsumer(application.getApplicationName());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteMqConsumers(List<Long> ids) {
        LambdaQueryWrapper<ShadowMqConsumerEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(ShadowMqConsumerEntity::getId, ids);
        lambdaQueryWrapper.eq(ShadowMqConsumerEntity::getDeleted, ShadowConsumerConstants.LIVED);
        List<ShadowMqConsumerEntity> shadowMqConsumerEntities = shadowMqConsumerMapper.selectList(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(shadowMqConsumerEntities)) {
            return;
        }
        Long applicationId = shadowMqConsumerEntities.get(0).getApplicationId();
        ApplicationDetailResult application = applicationDAO.getApplicationById(applicationId);
        if (application == null) {
            throw new RuntimeException(String.format("应用id:%s对应的应用不存在", applicationId));
        }
        shadowMqConsumerEntities.forEach(mq -> {
            OperationLogContextHolder.operationType(OpTypes.DELETE);
            OperationLogContextHolder.addVars(Vars.CONSUMER_TYPE, mq.getType());
            OperationLogContextHolder.addVars(Vars.CONSUMER_TOPIC_GROUP, mq.getTopicGroup());
            shadowMqConsumerDAO.removeById(mq.getId());
        });

        agentConfigCacheManager.evictShadowConsumer(application.getApplicationName());
    }

    @Override
    public void operateMqConsumers(ShadowConsumersOperateRequest requests) {
        // 启用
        ApplicationDetailResult application = applicationDAO.getApplicationById(requests.getApplicationId());
        if (application == null) {
            throw new RuntimeException(String.format("应用id:%s对应的应用不存在", requests.getApplicationId()));
        }
        if (requests.getEnable()) {
            OperationLogContextHolder.operationType(OpTypes.ENABLE);
            requests.getRequests().forEach(request -> {
                if (request.getId() != null) {
                    // 已存在ID的修改数据库状态
                    ShadowMqConsumerEntity entity = new ShadowMqConsumerEntity();
                    entity.setId(request.getId());
                    entity.setStatus(ShadowConsumerConstants.ENABLE);
                    shadowMqConsumerMapper.updateById(entity);
                } else {
                    // 不存在ID的新增一条记录
                    if (request.getTopicGroup() == null || request.getType() == null) {
                        throw new RuntimeException("启用失败，缺少对应的参数");
                    }
                    ShadowMqConsumerEntity shadowMqConsumerEntity = new ShadowMqConsumerEntity();
                    shadowMqConsumerEntity.setTopicGroup(request.getTopicGroup());
                    shadowMqConsumerEntity.setType(request.getType().name());
                    shadowMqConsumerEntity.setApplicationId(application.getApplicationId());
                    shadowMqConsumerEntity.setApplicationName(application.getApplicationName());
                    shadowMqConsumerEntity.setStatus(ShadowConsumerConstants.ENABLE);
                    shadowMqConsumerEntity.setDeleted(ShadowConsumerConstants.LIVED);
                    shadowMqConsumerEntity.setCustomerId(RestContext.getUser().getCustomerId());
                    shadowMqConsumerEntity.setUserId(RestContext.getUser().getId());
                    try {
                        shadowMqConsumerMapper.insert(shadowMqConsumerEntity);
                    } catch (Exception e) {
                        // ignore
                    }
                }
            });
        } else {
            OperationLogContextHolder.operationType(OpTypes.DISABLE);
            List<Long> ids = requests.getRequests().stream().map(ShadowConsumerOperateRequest::getId).collect(
                Collectors.toList());
            LambdaQueryWrapper<ShadowMqConsumerEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.in(ShadowMqConsumerEntity::getId, ids);
            lambdaQueryWrapper.eq(ShadowMqConsumerEntity::getDeleted, ShadowConsumerConstants.LIVED);
            List<ShadowMqConsumerEntity> shadowMqConsumerEntities = shadowMqConsumerMapper.selectList(
                lambdaQueryWrapper);
            if (CollectionUtils.isNotEmpty(shadowMqConsumerEntities)) {
                for (ShadowMqConsumerEntity shadowMqConsumerEntity : shadowMqConsumerEntities) {
                    ShadowMqConsumerEntity updateEntity = new ShadowMqConsumerEntity();
                    updateEntity.setId(shadowMqConsumerEntity.getId());
                    updateEntity.setStatus(ShadowConsumerConstants.DISABLE);
                    shadowMqConsumerMapper.updateById(updateEntity);
                }
            }
        }
        agentConfigCacheManager.evictShadowConsumer(application.getApplicationName());
    }

    @Override
    public List<ShadowConsumerVO> agentSelect(String appName) {
        LambdaQueryWrapper<ShadowMqConsumerEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShadowMqConsumerEntity::getDeleted, ShadowConsumerConstants.LIVED);
        lambdaQueryWrapper.eq(ShadowMqConsumerEntity::getStatus, ShadowConsumerConstants.ENABLE);
        lambdaQueryWrapper.eq(ShadowMqConsumerEntity::getApplicationName, appName);
        List<ShadowMqConsumerEntity> entities = shadowMqConsumerMapper.selectList(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(entities)) {
            return Lists.newArrayList();
        }
        Map<String, List<ShadowMqConsumerEntity>> collect = entities.stream().collect(
            Collectors.groupingBy(ShadowMqConsumerEntity::getType));
        if (MapUtils.isEmpty(collect)) {
            return Lists.newArrayList();
        }
        return collect.entrySet().stream().map(entity -> {
            ShadowConsumerVO shadowConsumerVO = new ShadowConsumerVO();
            shadowConsumerVO.setType(entity.getKey());
            Map<String, Set<String>> topicGroupMap = Maps.newHashMap();
            if (!CollectionUtils.isEmpty(entity.getValue())) {
                for (ShadowMqConsumerEntity shadowMqConsumerEntity : entity.getValue()) {
                    String[] topicGroup = shadowMqConsumerEntity.getTopicGroup().split("#");
                    Set<String> groups = topicGroupMap.get(topicGroup[0]);
                    if (groups == null) {
                        topicGroupMap.put(topicGroup[0], Sets.newHashSet(topicGroup[1]));
                    } else {
                        groups.add(topicGroup[1]);
                    }
                }
            }
            shadowConsumerVO.setTopicGroups(topicGroupMap);
            return shadowConsumerVO;
        }).collect(Collectors.toList());
    }

    @Override
    public int allocationUser(ShadowConsumerUpdateUserRequest request) {
        if (Objects.isNull(request.getApplicationId())) {
            return 0;
        }
        LambdaQueryWrapper<ShadowMqConsumerEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShadowMqConsumerEntity::getApplicationId, request.getApplicationId());
        List<ShadowMqConsumerEntity> shadowMqConsumerEntityList = shadowMqConsumerMapper.selectList(
            queryWrapper);
        if (CollectionUtils.isNotEmpty(shadowMqConsumerEntityList)) {
            for (ShadowMqConsumerEntity entity : shadowMqConsumerEntityList) {
                entity.setUserId(request.getUserId());
                shadowMqConsumerMapper.updateById(entity);
            }
        }
        return 0;
    }

    private List<ShadowMqConsumerEntity> getExists(String topicGroup, Long applicationId, ShadowMqConsumerType type) {
        LambdaQueryWrapper<ShadowMqConsumerEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShadowMqConsumerEntity::getTopicGroup, topicGroup);
        lambdaQueryWrapper.eq(ShadowMqConsumerEntity::getType, type.name());
        lambdaQueryWrapper.eq(ShadowMqConsumerEntity::getApplicationId, applicationId);
        lambdaQueryWrapper.eq(ShadowMqConsumerEntity::getDeleted, ShadowConsumerConstants.LIVED);
        return shadowMqConsumerMapper.selectList(lambdaQueryWrapper);
    }
}
