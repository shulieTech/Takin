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

package io.shulie.tro.web.app.aspect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pamirs.tro.entity.dao.auth.TUserDeptRelationMapper;
import com.pamirs.tro.entity.domain.entity.auth.TreeConvertUtil;
import com.pamirs.tro.entity.domain.entity.user.DeptUser;
import com.pamirs.tro.entity.domain.entity.user.User;
import io.shulie.tro.web.app.annotation.AuthVerification;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.exception.ExceptionCode;
import io.shulie.tro.web.app.service.auth.impl.TroAuthServiceImpl;
import io.shulie.tro.web.app.utils.TroUserUtil;
import io.shulie.tro.web.auth.api.enums.ActionTypeEnum;
import io.shulie.tro.web.auth.api.enums.DataTypeEnum;
import io.shulie.tro.web.auth.api.exception.TroAuthException;
import io.shulie.tro.web.data.result.user.DeptQueryResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/11/9 10:04 上午
 * @Description: 用户数据权限初始化
 */
@Aspect
@Component
@Slf4j
@Order(1)
public class DataQueryAuthVerificationAspect {

    @Autowired
    private TUserDeptRelationMapper tUserDeptRelationMapper;

    /**
     * 切入点
     */
    @Pointcut("@annotation(io.shulie.tro.web.app.annotation.AuthVerification)")
    private void controllerAspect() { /* no context */}

    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        //初始化用户数据权限threadlocal，
        RestContext.setEmptyAuth();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method targetMethod = methodSignature.getMethod();
        AuthVerification annotation = targetMethod.getAnnotation(AuthVerification.class);
        String moduleCode = annotation.moduleCode();
        User user = RestContext.getUser();
        if (!Objects.isNull(user)) {
            if (TroUserUtil.validateSuperAdmin(user)) {
                //所有权限
                RestContext.clearAuth();
                return;
            }
            Map<String, List<Integer>> dataMap = user.getPermissionData();
            for (ActionTypeEnum actionTypeEnum : ActionTypeEnum.values()) {
                String key = moduleCode + "_" + actionTypeEnum.getCode() + "_" + actionTypeEnum.name().toLowerCase();
                if (dataMap.containsKey(key)) {
                    List<Integer> scopeList = dataMap.get(key);
                    if (CollectionUtils.isEmpty(scopeList)) {
                        String msg = "用户暂未分配数据权限：" + user.getId();
                        log.warn(msg);
                        continue;
                    }
                    if (scopeList.contains(DataTypeEnum.ALL.getCode())) {
                        //为当前操作设置所有权限
                        switch (actionTypeEnum) {
                            case QUERY:
                                RestContext.queryAllowUserIdListThreadLocal.remove();
                                break;
                            case UPDATE:
                                RestContext.updateAllowUserIdListThreadLocal.remove();
                                break;
                            case DELETE:
                                RestContext.deleteAllowUserIdListThreadLocal.remove();
                                break;
                            case ENABLE_DISABLE:
                                RestContext.enableDisableAllowUserIdListThreadLocal.remove();
                                break;
                            case START_STOP:
                                RestContext.startStopAllowUserIdListThreadLocal.remove();
                                break;
                            case DOWNLOAD:
                                RestContext.downloadAllowUserIdListThreadLocal.remove();
                                break;
                            default:
                        }
                    } else {
                        List<Long> allowUserIdList;
                        //获取用户部门信息
                        List<DeptQueryResult> deptQueryResultList = user.getDeptList();
                        if (CollectionUtils.isEmpty(deptQueryResultList)) {
                            //无数据权限
                            String msg = "用户无所属部门，查询数据权限失败：" + user.getId();
                            log.warn(msg);
                            throw new TroAuthException(ExceptionCode.DATA_PERMISSION_DENY_ERROR, msg);
                        } else {
                            Long userIdParam = null;
                            List<DeptQueryResult> deptList = deptQueryResultList;
                            Set<String> currentDeptIdList = deptList
                                .stream()
                                .map(DeptQueryResult::getId)
                                .map(String::valueOf)
                                .collect(Collectors.toSet());
                            List<String> deptIdList = Lists.newArrayList();
                            if (scopeList.contains(DataTypeEnum.CURRENT_DEPT.getCode())
                                || scopeList.contains(DataTypeEnum.CURRENT_DEPT_AND_LESS.getCode())) {
                                //加入用户本部门id
                                deptIdList.addAll(currentDeptIdList);
                            }
                            if (scopeList.contains(DataTypeEnum.CURRENT_DEPT_AND_LESS.getCode())
                                || scopeList.contains(DataTypeEnum.SELF_AND_DEPT_LESS.getCode())) {
                                //筛选出用户下级部门id
                                deptIdList.addAll(getSubDeptIdList(currentDeptIdList));

                            }
                            if (scopeList.contains(DataTypeEnum.SELF_AND_DEPT_LESS.getCode())
                                || scopeList.contains(DataTypeEnum.SELF.getCode())) {
                                //加入当前用户
                                userIdParam = user.getId();
                            }
                            List<DeptUser> scopeUserList = tUserDeptRelationMapper.selectUserIdByScope(deptIdList,
                                userIdParam);
                            allowUserIdList = scopeUserList.stream().map(DeptUser::getId).collect(Collectors.toList());
                            //将数据权限放入上下文
                            switch (actionTypeEnum) {
                                case QUERY:
                                    RestContext.setQueryAllowUserIdList(
                                        allowUserIdList.stream().distinct().collect(Collectors.toList()));
                                    break;
                                case UPDATE:
                                    RestContext.setUpdateAllowUserIdList(
                                        allowUserIdList.stream().distinct().collect(Collectors.toList()));
                                    break;
                                case DELETE:
                                    RestContext.setDeleteAllowUserIdList(
                                        allowUserIdList.stream().distinct().collect(Collectors.toList()));
                                    break;
                                case ENABLE_DISABLE:
                                    RestContext.setEnableDisableAllowUserIdList(
                                        allowUserIdList.stream().distinct().collect(Collectors.toList()));
                                    break;
                                case START_STOP:
                                    RestContext.setStartStopAllowUserIdList(
                                        allowUserIdList.stream().distinct().collect(Collectors.toList()));
                                    break;
                                case DOWNLOAD:
                                    RestContext.setDownloadAllowUserIdList(
                                        allowUserIdList.stream().distinct().collect(Collectors.toList()));
                                    break;
                                default:
                            }
                        }
                    }
                }
            }
        }
    }

    private List<String> getSubDeptIdList(Set<String> deptIdSet) {
        Set<String> lowerDeptIdSet = Sets.newHashSet();
        TreeConvertUtil.getLowerDeptByParentDeptIds(TroAuthServiceImpl.deptTotalList, deptIdSet, lowerDeptIdSet);
        lowerDeptIdSet.removeAll(deptIdSet);
        return new ArrayList<>(lowerDeptIdSet);
    }
}
