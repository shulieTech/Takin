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

package io.shulie.tro.web.app.service.user.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import com.pamirs.tro.entity.domain.entity.auth.Dept;
import com.pamirs.tro.entity.domain.entity.auth.TreeConvertUtil;
import io.shulie.tro.web.app.exception.ExceptionCode;
import io.shulie.tro.web.app.exception.TroWebException;
import io.shulie.tro.web.app.request.user.DeptCreateRequest;
import io.shulie.tro.web.app.request.user.DeptDeleteRequest;
import io.shulie.tro.web.app.request.user.DeptQueryRequest;
import io.shulie.tro.web.app.request.user.DeptUpdateRequest;
import io.shulie.tro.web.app.response.user.DeptQueryResponse;
import io.shulie.tro.web.app.service.user.TroDeptService;
import io.shulie.tro.web.auth.api.DeptService;
import io.shulie.tro.web.data.dao.user.TroDeptDAO;
import io.shulie.tro.web.data.dao.user.TroDeptUserRelationDAO;
import io.shulie.tro.web.data.param.user.DeptCreateParam;
import io.shulie.tro.web.data.param.user.DeptDeleteParam;
import io.shulie.tro.web.data.param.user.DeptUpdateParam;
import io.shulie.tro.web.data.param.user.UserDeptConditionQueryParam;
import io.shulie.tro.web.data.result.user.DeptQueryResult;
import io.shulie.tro.web.data.result.user.UserDeptConditionResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2021/3/8 7:22 下午
 * @Description:
 */
@Component
@Slf4j
public class TroDeptServiceImpl implements TroDeptService {

    @Autowired
    private TroDeptDAO troDeptDAO;

    @Autowired
    private TroDeptUserRelationDAO troDeptUserRelationDAO;

    @Autowired
    private DeptService deptService;

    @Override
    public void createDept(DeptCreateRequest createRequest) {
        int maxLength = 20;
        int nameLength = createRequest.getName().length();
        if (nameLength > maxLength) {
            throw new TroWebException(ExceptionCode.DEPT_MANAGE_ADD_ERROR, "部门名称超长，不得超过20个字符");
        }
        DeptQueryResult queryResult = troDeptDAO.selectDetailByName(createRequest.getName());
        if (queryResult != null) {
            throw new TroWebException(ExceptionCode.DEPT_MANAGE_ADD_ERROR, "部门名称已存在");
        }

        DeptCreateParam createParam = new DeptCreateParam();
        createParam.setName(createRequest.getName());
        createParam.setCode(RandomStringUtils.randomAlphanumeric(8));
        if (createRequest.getParentId() != -1) {
            createParam.setParentId(createRequest.getParentId());
        }
        troDeptDAO.insert(createParam);
    }

    @Override
    public void updateDept(DeptUpdateRequest updateRequest) {
        int maxLength = 20;
        int nameLength = updateRequest.getName().length();
        if (nameLength > maxLength) {
            throw new TroWebException(ExceptionCode.DEPT_MANAGE_UPDATE_ERROR, "部门名称超长，不得超过20个字符");
        }
        DeptQueryResult queryResult = troDeptDAO.selectDetailById(updateRequest.getId());
        if (!queryResult.getName().equals(updateRequest.getName())) {
            DeptQueryResult existDept = troDeptDAO.selectDetailByName(updateRequest.getName());
            if (existDept != null) {
                throw new TroWebException(ExceptionCode.DEPT_MANAGE_UPDATE_ERROR, "部门名称已存在");
            }
        }
        Set<String> currentDeptIdSet = Sets.newHashSet();
        currentDeptIdSet.add(String.valueOf(updateRequest.getId()));
        Set<String> lowerDeptIdSet = Sets.newHashSet();
        List<Dept> deptTotalList = deptService.getAllDepts("");
        TreeConvertUtil.getLowerDeptByParentDeptIds(deptTotalList, currentDeptIdSet, lowerDeptIdSet);
        if (updateRequest.getId().equals(updateRequest.getParentId())
            || lowerDeptIdSet.contains(String.valueOf(updateRequest.getParentId()))) {
            throw new TroWebException(ExceptionCode.DEPT_MANAGE_UPDATE_ERROR, "上级部门不能修改为当前部门或当前部门的下级部门");
        }
        DeptUpdateParam updateParam = new DeptUpdateParam();
        updateParam.setId(updateRequest.getId());
        updateParam.setName(updateRequest.getName());
        if (updateRequest.getParentId() == -1) {
            updateRequest.setParentId(null);
        } else {
            updateParam.setParentId(updateRequest.getParentId());
        }
        troDeptDAO.update(updateParam);
    }

    @Override
    public void deleteDept(DeptDeleteRequest deleteRequest) {
        Set<String> currentDeptIdSet = Sets.newHashSet();
        currentDeptIdSet.add(String.valueOf(deleteRequest.getId()));
        Set<String> lowerDeptIdSet = Sets.newHashSet();
        List<Dept> deptTotalList = deptService.getAllDepts("");
        TreeConvertUtil.getLowerDeptByParentDeptIds(deptTotalList, currentDeptIdSet, lowerDeptIdSet);
        List<String> deptIdList = new ArrayList<>(lowerDeptIdSet);
        UserDeptConditionQueryParam queryParam = new UserDeptConditionQueryParam();
        queryParam.setDeptIds(deptIdList);
        List<UserDeptConditionResult> userDeptConditionResultList = troDeptUserRelationDAO.selectUserByDeptIds(
            queryParam);
        if (CollectionUtils.isNotEmpty(userDeptConditionResultList)) {
            List<String> deptNameList =
                userDeptConditionResultList.stream().map(UserDeptConditionResult::getDeptName).distinct().collect(
                    Collectors.toList());
            String existUserDeptName = StringUtils.join(deptNameList.toArray(), ",");
            throw new TroWebException(ExceptionCode.DEPT_MANAGE_DELETE_ERROR,
                "删除失败，部门【" + existUserDeptName + "】下存在账号，请移除账号后重试");
        }
        List<Long> deptIdLongList = deptIdList.stream().map(Long::parseLong).collect(Collectors.toList());
        DeptDeleteParam deleteParam = new DeptDeleteParam();
        deleteParam.setDeptIdList(deptIdLongList);
        troDeptDAO.delete(deleteParam);
    }

    @Override
    public DeptQueryResponse queryDept(DeptQueryRequest queryRequest) {
        DeptQueryResult result = troDeptDAO.selectDetailById(queryRequest.getId());
        if (!Objects.isNull(queryRequest)) {
            DeptQueryResponse queryResponse = new DeptQueryResponse();
            queryResponse.setId(result.getId());
            queryResponse.setName(result.getName());
            queryResponse.setParentId(result.getParentId());
            return queryResponse;
        }
        return null;
    }
}
