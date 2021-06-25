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

package io.shulie.tro.web.app.service.auth.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.pamirs.tro.entity.domain.entity.auth.Dept;
import com.pamirs.tro.entity.domain.entity.auth.TreeConvertUtil;
import com.pamirs.tro.entity.domain.entity.auth.TreeDeptModel;
import com.pamirs.tro.entity.domain.entity.user.DeptUser;
import com.pamirs.tro.entity.domain.entity.user.DeptUserQueryParam;
import com.pamirs.tro.entity.domain.entity.user.User;
import com.pamirs.tro.entity.domain.vo.user.UserLoginParam;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.constant.BizOpConstants;
import io.shulie.tro.web.app.convert.user.UserConverter;
import io.shulie.tro.web.app.input.user.UserQueryInput;
import io.shulie.tro.web.app.output.user.UserQueryOutput;
import io.shulie.tro.web.app.request.user.ResourceActionDeleteRequest;
import io.shulie.tro.web.app.request.user.ResourceActionQueryRequest;
import io.shulie.tro.web.app.request.user.ResourceActionUpdateRequest;
import io.shulie.tro.web.app.request.user.ResourceScopeQueryRequest;
import io.shulie.tro.web.app.request.user.ResourceScopeUpdateRequest;
import io.shulie.tro.web.app.response.user.ResourceActionResponse;
import io.shulie.tro.web.app.response.user.ResourceScopeResponse;
import io.shulie.tro.web.app.response.user.SingleActionResponse;
import io.shulie.tro.web.app.response.user.SingleScopeResponse;
import io.shulie.tro.web.app.service.auth.TroAuthService;
import io.shulie.tro.web.app.utils.MapOrderByUtils;
import io.shulie.tro.web.app.utils.ServletUtils;
import io.shulie.tro.web.auth.api.AuthService;
import io.shulie.tro.web.auth.api.DeptService;
import io.shulie.tro.web.auth.api.ResourceService;
import io.shulie.tro.web.auth.api.UserService;
import io.shulie.tro.web.auth.api.enums.ActionTypeEnum;
import io.shulie.tro.web.auth.api.enums.DataTypeEnum;
import io.shulie.tro.web.auth.api.exception.TroLoginException;
import io.shulie.tro.web.common.constant.TroClientAuthConstant;
import io.shulie.tro.web.data.param.user.ResourceMenuQueryParam;
import io.shulie.tro.web.data.param.user.RoleResourceAuthDeleteParam;
import io.shulie.tro.web.data.param.user.RoleResourceAuthQueryParam;
import io.shulie.tro.web.data.param.user.RoleResourceAuthUpdateParam;
import io.shulie.tro.web.data.param.user.UserDeptConditionQueryParam;
import io.shulie.tro.web.data.param.user.UserDeptQueryParam;
import io.shulie.tro.web.data.param.user.UserQueryParam;
import io.shulie.tro.web.data.param.user.UserRoleRelationBatchQueryParam;
import io.shulie.tro.web.data.result.user.ResourceAuthResult;
import io.shulie.tro.web.data.result.user.ResourceMenuResult;
import io.shulie.tro.web.data.result.user.UserCommonResult;
import io.shulie.tro.web.data.result.user.UserDeptConditionResult;
import io.shulie.tro.web.data.result.user.UserDeptResult;
import io.shulie.tro.web.data.result.user.UserRoleRelationResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/9/3 下午2:35
 * @Description:
 */
@Component
@Slf4j
public class TroAuthServiceImpl implements TroAuthService {

    public static List<Dept> deptTotalList = Lists.newArrayList();
    @Autowired
    private DeptService deptService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private ResourceService resourceService;

    @PostConstruct
    public void init() {
        deptTotalList = deptService.getAllDepts("");
    }

    /**
     * 查询组织成员
     *
     * @param input
     * @return
     */
    @Override
    public PagingList<UserQueryOutput> selectUserByCondition(UserQueryInput input) {
        UserQueryParam param = new UserQueryParam();
        BeanUtils.copyProperties(input, param);
        param.setCustomerId(RestContext.getCustomerId());
        PagingList<UserCommonResult> pagingList = userService.selectUserByCondition(param);
        return UserConverter.INSTANCE.toOutputPagingList(pagingList);
    }

    @Override
    public List<TreeDeptModel> getDeptTree(String deptName) {
        return deptService.getDeptTree(deptName);
    }

    @Override
    public PageInfo<DeptUser> getDeptUser(DeptUserQueryParam param) {
        return userService.getSimpleUserList(param);
    }

    @Override
    public List<ResourceActionResponse> getResourceAction(ResourceActionQueryRequest queryRequest) {
        //查询可配置的全量菜单
        List<ResourceMenuResult> resourceMenuResultList = authService.selectAuthConfigMenu();
        //查询用户下已配置的菜单
        RoleResourceAuthQueryParam roleResourceAuthQueryParam = new RoleResourceAuthQueryParam();
        roleResourceAuthQueryParam.setRoleIdList(Arrays.asList(queryRequest.getRoleId()));
        List<ResourceAuthResult> authResultList = authService.selectList(roleResourceAuthQueryParam);
        if (CollectionUtils.isEmpty(authResultList)) {
            //角色未配置菜单及功能权限
            List<ResourceActionResponse> resourceActionResponseList = Lists.newArrayList();
            resourceMenuResultList.sort(Comparator.comparingInt(ResourceMenuResult::getSequence));
            resourceMenuResultList.forEach(menuResult -> {
                ResourceActionResponse actionResponse = new ResourceActionResponse();
                actionResponse.setId(menuResult.getId());
                actionResponse.setKey(menuResult.getCode());
                actionResponse.setTitle(menuResult.getName());
                actionResponse.setChecked(Boolean.FALSE);
                List<SingleActionResponse> groupList = Lists.newArrayList();
                List<Integer> actionList = menuResult.getActionList();
                if (CollectionUtils.isNotEmpty(actionList)) {
                    actionList.forEach(action -> {
                        SingleActionResponse singleActionResponse = new SingleActionResponse();
                        singleActionResponse.setValue(String.valueOf(action));
                        singleActionResponse.setLabel(ActionTypeEnum.getCnameByCode(action));
                        singleActionResponse.setChecked(Boolean.FALSE);
                        groupList.add(singleActionResponse);
                    });
                }
                actionResponse.setGroupList(groupList);
                resourceActionResponseList.add(actionResponse);
            });
            return resourceActionResponseList;
        } else {
            //角色已配置菜单及功能权限
            List<ResourceActionResponse> resourceActionResponseList = Lists.newArrayList();
            resourceMenuResultList.sort(Comparator.comparingInt(ResourceMenuResult::getSequence));
            resourceMenuResultList.forEach(menuResult -> {
                ResourceActionResponse actionResponse = new ResourceActionResponse();
                actionResponse.setId(menuResult.getId());
                actionResponse.setKey(menuResult.getCode());
                actionResponse.setTitle(menuResult.getName());
                Optional<ResourceAuthResult> optional = authResultList
                    .stream().filter(authResult -> authResult.getResoureId().equals(menuResult.getId())).findFirst();
                if (optional.isPresent()) {
                    ResourceAuthResult authResult = optional.get();
                    //已配置该菜单权限
                    actionResponse.setChecked(authResult.getStatus());
                    List<Integer> authedActionList = authResult.getActionList();
                    List<Integer> allActionList = menuResult.getActionList();
                    List<SingleActionResponse> groupList = Lists.newArrayList();
                    allActionList.forEach(action -> {
                        SingleActionResponse singleActionResponse = new SingleActionResponse();
                        singleActionResponse.setValue(String.valueOf(action));
                        singleActionResponse.setLabel(ActionTypeEnum.getCnameByCode(action));
                        //是否有相应的操作权限
                        if (authedActionList.contains(action)) {
                            singleActionResponse.setChecked(Boolean.TRUE);
                        } else {
                            singleActionResponse.setChecked(Boolean.FALSE);
                        }
                        groupList.add(singleActionResponse);
                    });
                    actionResponse.setGroupList(groupList);
                    resourceActionResponseList.add(actionResponse);
                } else {
                    //未配置该菜单权限
                    actionResponse.setChecked(Boolean.FALSE);
                    List<SingleActionResponse> groupList = Lists.newArrayList();
                    List<Integer> actionList = menuResult.getActionList();
                    if (CollectionUtils.isNotEmpty(actionList)) {
                        actionList.forEach(action -> {
                            SingleActionResponse singleActionResponse = new SingleActionResponse();
                            singleActionResponse.setValue(String.valueOf(action));
                            singleActionResponse.setLabel(ActionTypeEnum.getCnameByCode(action));
                            singleActionResponse.setChecked(Boolean.FALSE);
                            groupList.add(singleActionResponse);
                        });
                    }
                    actionResponse.setGroupList(groupList);
                    resourceActionResponseList.add(actionResponse);
                }
            });
            for (ResourceActionResponse resourceActionResponse : resourceActionResponseList) {
                if (resourceActionResponse.getKey().equals(BizOpConstants.ModuleCode.PRESSURE_TEST_SWITCH)
                    || resourceActionResponse.getKey().equals(BizOpConstants.ModuleCode.PRESSURE_WHITELIST_SWITCH)) {
                    for (SingleActionResponse singleActionResponse : resourceActionResponse.getGroupList()) {
                        if (ActionTypeEnum.ENABLE_DISABLE.getCode().equals(
                            Integer.parseInt(singleActionResponse.getValue()))) {
                            singleActionResponse.setLabel("开启关闭");
                        }
                    }
                }
            }
            return resourceActionResponseList;
        }
    }

    @Override
    public Response updateResourceAction(ResourceActionUpdateRequest updateRequest) {
        Long roleId = updateRequest.getRoleId();
        List<ResourceActionResponse> resourceActionResponseList = updateRequest.getFuncPermissionList();
        if (CollectionUtils.isNotEmpty(resourceActionResponseList)) {
            List<ResourceActionResponse> updateActionResponseList = Lists.newArrayList();
            List<ResourceActionResponse> checkedActionResponseList = resourceActionResponseList
                .stream()
                .filter(ResourceActionResponse::getChecked)
                .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(checkedActionResponseList)) {
                updateActionResponseList.addAll(checkedActionResponseList);
            }
            List<ResourceActionResponse> disableActionResponseList = resourceActionResponseList
                .stream()
                .filter(resourceActionResponse -> !resourceActionResponse.getChecked()
                    && CollectionUtils.isNotEmpty(resourceActionResponse.getGroupList())
                    && resourceActionResponse.getGroupList()
                    .stream().anyMatch(SingleActionResponse::getChecked))
                .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(disableActionResponseList)) {
                updateActionResponseList.addAll(disableActionResponseList);
            }
            List<RoleResourceAuthUpdateParam> updateParamList = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(updateActionResponseList)) {
                updateActionResponseList.forEach(resourceActionResponse -> {
                    RoleResourceAuthUpdateParam updateParam = new RoleResourceAuthUpdateParam();
                    updateParam.setRoleId(roleId);
                    updateParam.setResoureId(resourceActionResponse.getId());
                    updateParam.setStatus(resourceActionResponse.getChecked() ? 0 : 1);
                    List<SingleActionResponse> groupList = resourceActionResponse.getGroupList();
                    if (CollectionUtils.isEmpty(groupList)) {
                        updateParam.setActionList(Lists.newArrayList());
                    } else {
                        List<Integer> actionList = groupList
                            .stream()
                            .filter(SingleActionResponse::getChecked)
                            .map(SingleActionResponse::getValue)
                            .map(Integer::parseInt)
                            .sorted()
                            .collect(Collectors.toList());
                        updateParam.setActionList(actionList);
                    }
                    updateParamList.add(updateParam);
                });
                authService.updateRoleAuth(updateParamList);
            }

            List<ResourceActionResponse> deleteActionResponseList;
            if (!CollectionUtils.isEmpty(updateParamList)) {
                List<Long> resourceIdList =
                    updateParamList.stream().map(RoleResourceAuthUpdateParam::getResoureId).collect(
                        Collectors.toList());
                deleteActionResponseList = resourceActionResponseList
                    .stream()
                    .filter(resourceActionResponse -> !resourceIdList.contains(resourceActionResponse.getId()))
                    .collect(Collectors.toList());
            } else {
                deleteActionResponseList = resourceActionResponseList;
            }
            if (CollectionUtils.isNotEmpty(deleteActionResponseList)) {
                List<ResourceActionDeleteRequest> deleteRequestList = Lists.newArrayList();
                deleteActionResponseList.forEach(deleteActionResponse -> {
                    ResourceActionDeleteRequest deleteRequest = new ResourceActionDeleteRequest();
                    deleteRequest.setRoleId(roleId);
                    deleteRequest.setResoureId(deleteActionResponse.getId());
                    deleteRequestList.add(deleteRequest);
                });
                deleteResourceAction(deleteRequestList);
            }
        }
        return Response.success("配置成功");
    }

    @Override
    public List<ResourceScopeResponse> getResourceScope(ResourceScopeQueryRequest queryRequest) {
        List<ResourceScopeResponse> resourceScopeResponseList = Lists.newArrayList();
        //查询可配置的全量菜单
        List<ResourceMenuResult> resourceMenuResultList = authService.selectAuthConfigMenu();
        //查询用户下已配置的菜单
        RoleResourceAuthQueryParam roleResourceAuthQueryParam = new RoleResourceAuthQueryParam();
        roleResourceAuthQueryParam.setRoleIdList(Arrays.asList(queryRequest.getRoleId()));
        roleResourceAuthQueryParam.setStatus(Boolean.TRUE);
        List<ResourceAuthResult> authResultList = authService.selectList(roleResourceAuthQueryParam);
        if (CollectionUtils.isNotEmpty(authResultList)) {
            authResultList.forEach(authResult -> {
                Optional<ResourceMenuResult> optional = resourceMenuResultList
                    .stream()
                    .filter(menuResult -> menuResult.getId().equals(authResult.getResoureId()))
                    .findFirst();
                if (optional.isPresent()) {
                    ResourceMenuResult menuResult = optional.get();
                    if (menuResult.getCode().equals(BizOpConstants.ModuleCode.AUTHORITY_CONFIG)
                        || menuResult.getCode().equals(BizOpConstants.ModuleCode.PRESSURE_WHITELIST_SWITCH)
                        || menuResult.getCode().equals(BizOpConstants.ModuleCode.PRESSURE_TEST_SWITCH)
                        || menuResult.getCode().equals(BizOpConstants.ModuleCode.FLOW_ACCOUNT)
                        || menuResult.getCode().equals(BizOpConstants.ModuleCode.OPERATIONLOG)) {
                        return;
                    }
                    ResourceScopeResponse scopeResponse = new ResourceScopeResponse();
                    scopeResponse.setId(menuResult.getId());
                    scopeResponse.setTitle(menuResult.getName());
                    scopeResponse.setKey(menuResult.getCode());
                    List<SingleScopeResponse> groupList = Lists.newArrayList();
                    List<Integer> scopeList = authResult.getScopeList();
                    if (CollectionUtils.isEmpty(scopeList)) {
                        scopeResponse.setChecked(DataTypeEnum.NONE.getCode());
                        for (DataTypeEnum dataTypeEnum : DataTypeEnum.values()) {
                            if (!dataTypeEnum.getCode().equals(DataTypeEnum.NONE.getCode())) {
                                SingleScopeResponse singleScopeResponse = new SingleScopeResponse();
                                singleScopeResponse.setValue(dataTypeEnum.getCode());
                                singleScopeResponse.setLabel(dataTypeEnum.getCname());
                                groupList.add(singleScopeResponse);
                            }
                        }
                    } else {
                        Integer scope = scopeList.get(0);
                        for (DataTypeEnum dataTypeEnum : DataTypeEnum.values()) {
                            if (dataTypeEnum.getCode().equals(scope)) {
                                scopeResponse.setChecked(scope);
                            }
                            if (!dataTypeEnum.getCode().equals(DataTypeEnum.NONE.getCode())) {
                                SingleScopeResponse singleScopeResponse = new SingleScopeResponse();
                                singleScopeResponse.setValue(dataTypeEnum.getCode());
                                singleScopeResponse.setLabel(dataTypeEnum.getCname());
                                groupList.add(singleScopeResponse);
                            }
                        }
                    }
                    scopeResponse.setGroupList(groupList);
                    resourceScopeResponseList.add(scopeResponse);
                }
            });
        }
        return resourceScopeResponseList;
    }

    @Override
    public Response updateResourceScope(ResourceScopeUpdateRequest updateRequest) {
        Long roleId = updateRequest.getRoleId();
        List<ResourceScopeResponse> resourceScopeResponseList = updateRequest.getDataPermissionList();
        if (CollectionUtils.isNotEmpty(resourceScopeResponseList)) {
            List<RoleResourceAuthUpdateParam> updateParamList = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(resourceScopeResponseList)) {
                resourceScopeResponseList.forEach(resourceScopeResponse -> {
                    RoleResourceAuthUpdateParam updateParam = new RoleResourceAuthUpdateParam();
                    updateParam.setRoleId(roleId);
                    updateParam.setResoureId(resourceScopeResponse.getId());
                    if (resourceScopeResponse.getChecked().equals(DataTypeEnum.NONE.getCode())) {
                        updateParam.setScopeList(Lists.newArrayList());
                    } else {
                        List<Integer> scopeList = Collections.singletonList(resourceScopeResponse.getChecked());
                        updateParam.setScopeList(scopeList);
                    }
                    updateParamList.add(updateParam);
                });
                authService.updateRoleAuth(updateParamList);
            }
        }
        return Response.success("配置成功");
    }

    @Override
    public void deleteResourceAction(List<ResourceActionDeleteRequest> deleteRequestList) {
        List<RoleResourceAuthDeleteParam> deleteParamList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(deleteRequestList)) {
            deleteRequestList.forEach(deleteRequest -> {
                RoleResourceAuthDeleteParam deleteParam = new RoleResourceAuthDeleteParam();
                deleteParam.setRoleId(deleteRequest.getRoleId());
                deleteParam.setResoureId(deleteRequest.getResoureId());
                deleteParamList.add(deleteParam);
            });
            authService.deleteRoleAuth(deleteParamList);
        }
    }

    @Override
    public Map<String, Boolean> getUserMenu() {
        Map<String, Boolean> userResourceMap = Maps.newLinkedHashMap();
        User user = RestContext.getUser();
        if (Objects.isNull(user)) {
            log.error("获取用户菜单失败：用户信息为空");
            return Maps.newHashMap();
        }
        List<ResourceMenuResult> resourceList;
        if (user.getUserType() == 0) {
            //系统管理员
            resourceList = authService.selectMenu(new ResourceMenuQueryParam());
        } else {
            //普通用户
            List<ResourceMenuResult> allResourceList = authService.selectMenu(new ResourceMenuQueryParam());
            List<ResourceAuthResult> authResultList = getUserAuth(String.valueOf(user.getId()));
            if (CollectionUtils.isEmpty(authResultList)) {
                log.error("获取用户菜单失败：该用户下暂无资源信息");
                return Maps.newHashMap();
            }
            List<Long> resourceIdList = authResultList
                .stream()
                .map(ResourceAuthResult::getResoureId)
                .collect(Collectors.toList());
            ResourceMenuQueryParam resourceMenuQueryParam = new ResourceMenuQueryParam();
            resourceMenuQueryParam.setResourceIdList(resourceIdList);
            resourceList = authService.selectMenu(resourceMenuQueryParam);
            if (CollectionUtils.isEmpty(resourceList)) {
                log.error("获取用户菜单失败：未找到资源信息");
                return Maps.newHashMap();
            }
            TreeConvertUtil.getParentResourceByResourceIdList(allResourceList, resourceList);
        }
        resourceList = resourceList.stream().sorted(Comparator.comparing(ResourceMenuResult::getSequence)).collect(
            Collectors.toList());
        for (ResourceMenuResult menuResult : resourceList) {
            userResourceMap.put(menuResult.getCode(), Boolean.TRUE);
        }
        HttpServletResponse response = ServletUtils.getResponse();
        response.addHeader("x-user-type", String.valueOf(RestContext.getUser().getUserType()));
        response.addHeader("x-expire", String.valueOf(TroClientAuthConstant.isExpire));
        return userResourceMap;
    }

    @Override
    public Map<String, Boolean> getUserAction() {
        //查询可配置的全量菜单
        List<ResourceMenuResult> resourceMenuResultList = authService.selectAuthConfigMenu();
        Map<String, Boolean> actionMap = Maps.newLinkedHashMap();
        User user = RestContext.getUser();
        if (!Objects.isNull(user)) {
            if (user.getUserType() == 0) {
                resourceMenuResultList.forEach(menuResult -> {
                    List<Integer> actionList = menuResult.getActionList();
                    if (CollectionUtils.isNotEmpty(actionList)) {
                        actionList.forEach(action -> {
                            String key = menuResult.getCode() + "_" + action + "_" + Objects.requireNonNull(
                                ActionTypeEnum.getNameByCode(action)).toLowerCase();
                            if (!actionMap.containsKey(key)) {
                                actionMap.put(key, Boolean.TRUE);
                            }
                        });
                    }
                });
            } else {
                List<ResourceAuthResult> authResultList = getUserAuth(String.valueOf(user.getId()));
                if (CollectionUtils.isNotEmpty(authResultList)) {
                    authResultList.forEach(authResult -> {
                        Optional<ResourceMenuResult> optional = resourceMenuResultList
                            .stream()
                            .filter(resourceMenuResult -> resourceMenuResult.getId().equals(authResult.getResoureId()))
                            .findFirst();
                        if (optional.isPresent()) {
                            ResourceMenuResult resourceMenuResult = optional.get();
                            List<Integer> actionList = authResult.getActionList();
                            //有菜单默认就有查询权限
                            actionList.add(ActionTypeEnum.QUERY.getCode());
                            actionList.forEach(action -> {
                                String key = resourceMenuResult.getCode() + "_" + action + "_" + Objects.requireNonNull(
                                    ActionTypeEnum.getNameByCode(action)).toLowerCase();
                                if (!actionMap.containsKey(key)) {
                                    actionMap.put(key, Boolean.TRUE);
                                }
                            });
                        } else {
                            log.error("菜单不存在:[{}]", authResult.getResoureId());
                        }
                    });
                } else {
                    log.error("用户权限为空，查询操作权限失败:[{}]", user.getId());
                }
            }
        } else {
            log.error("用户为空，查询角色失败");
        }
        if (actionMap.size() > 0) {
            return MapOrderByUtils.orderByKey(actionMap, false);
        }
        return actionMap;
    }

    @Override
    public Map<String, List<Integer>> getUserData() {
        Map<String, List<Integer>> scopeMap = Maps.newHashMap();
        User user = RestContext.getUser();
        if (!Objects.isNull(user)) {
            if (user.getUserType() == 0) {
                return null;
            }
            List<ResourceAuthResult> authResultList = getUserAuth(String.valueOf(user.getId()));
            if (CollectionUtils.isNotEmpty(authResultList)) {
                authResultList.forEach(authResult -> {
                    //有菜单默认就有查询权限
                    List<Integer> actionList = authResult.getActionList();
                    actionList.add(ActionTypeEnum.QUERY.getCode());
                    for (Integer action : actionList) {
                        String key = authResult.getResourceCode() + "_" + action + "_" + Objects.requireNonNull(
                            ActionTypeEnum.getNameByCode(action)).toLowerCase();
                        if (!scopeMap.containsKey(key)) {
                            List<Integer> tmpScopeList = Lists.newArrayList();
                            if (CollectionUtils.isNotEmpty(authResult.getScopeList())) {
                                tmpScopeList.addAll(authResult.getScopeList());
                                scopeMap.put(key, tmpScopeList);
                            }
                        } else {
                            List<Integer> scopeList = scopeMap.get(key);
                            if (CollectionUtils.isNotEmpty(authResult.getScopeList())) {
                                scopeList.addAll(authResult.getScopeList());
                            }
                            List<Integer> withoutDuplicateScopeList = scopeList.stream().distinct().collect(
                                Collectors.toList());
                            scopeMap.put(key, withoutDuplicateScopeList);
                        }
                    }
                });
            }
        }
        return scopeMap;
    }

    private List<ResourceAuthResult> getUserAuth(String userId) {
        UserRoleRelationBatchQueryParam userRoleRelationBatchQueryParam = new UserRoleRelationBatchQueryParam();
        userRoleRelationBatchQueryParam.setUserIdList(Arrays.asList(userId));
        //查询用户下所有角色
        List<UserRoleRelationResult> roleResultList = authService.selectUserRoleRelationBatch(
            userRoleRelationBatchQueryParam);
        if (CollectionUtils.isNotEmpty(roleResultList)) {
            List<Long> roleIdList = roleResultList
                .stream()
                .map(UserRoleRelationResult::getRoleId)
                .map(Long::parseLong)
                .distinct()
                .collect(Collectors.toList());
            RoleResourceAuthQueryParam roleResourceAuthQueryParam = new RoleResourceAuthQueryParam();
            roleResourceAuthQueryParam.setRoleIdList(roleIdList);
            roleResourceAuthQueryParam.setStatus(Boolean.TRUE);
            List<ResourceAuthResult> authResultList = authService.selectList(roleResourceAuthQueryParam);
            return authResultList;
        } else {
            log.error("用户角色为空，查询权限失败:[{}]", userId);
        }
        return Lists.newArrayList();
    }

    @Override
    public List<String> getUserMenuResource(User user) {
        if (Objects.isNull(user)) {
            return Lists.newArrayList();
        }
        List<ResourceMenuResult> resourceList;
        if (user.getUserType() == 0) {
            resourceList = authService.selectMenu(new ResourceMenuQueryParam());
        } else {
            List<ResourceAuthResult> authResultList = getUserAuth(String.valueOf(user.getId()));
            if (CollectionUtils.isEmpty(authResultList)) {
                return Lists.newArrayList();
            }
            List<Long> resourceIdList = authResultList
                .stream()
                .map(ResourceAuthResult::getResoureId)
                .collect(Collectors.toList());
            ResourceMenuQueryParam resourceMenuQueryParam = new ResourceMenuQueryParam();
            resourceMenuQueryParam.setResourceIdList(resourceIdList);
            resourceList = authService.selectMenu(resourceMenuQueryParam);
        }
        if (CollectionUtils.isEmpty(resourceList)) {
            return Lists.newArrayList();
        }
        Set<String> urlSet = Sets.newHashSet();
        for (ResourceMenuResult menuResult : resourceList) {
            if (StringUtils.isNotBlank(menuResult.getValue())) {
                String urlStr = menuResult.getValue();
                JSONArray array = JSON.parseArray(urlStr);
                for (Object object : array) {
                    urlSet.add(object.toString());
                }
            }
        }
        return new ArrayList<>(urlSet);
    }

    @Override
    public List<UserDeptResult> selectList(UserDeptQueryParam queryParam) {
        return authService.selectList(queryParam);
    }

    @Override
    public List<UserDeptConditionResult> selectUserByDeptIds(UserDeptConditionQueryParam queryParam) {
        return authService.selectUserByDeptIds(queryParam);
    }

    @Override
    public User login(HttpServletRequest request, UserLoginParam user) throws TroLoginException {
        return authService.login(request, user);
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        return authService.logout(request, response);
    }

    @Override
    public void redirect(String redirectUrl, HttpServletRequest request, HttpServletResponse response) {
        authService.redirect(redirectUrl, request, response);
    }

    @Override
    public void loginSuccess(User user) {
        authService.loginSuccess(user);
    }

    @Override
    public User check(HttpServletRequest request, HttpServletResponse response) {
        return authService.check(request, response);
    }

    @Override
    public void checkFail(HttpServletResponse response) {
        authService.checkFail(response);
    }

    @Override
    public boolean hasPermissionUrl(User user, String url) {
        return authService.hasPermissionUrl(user, url);
    }
}
