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

package io.shulie.tro.web.data.dao.user.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import io.shulie.tro.web.data.dao.user.TroDeptUserRelationDAO;
import io.shulie.tro.web.data.mapper.mysql.TroUserDeptRelationMapper;
import io.shulie.tro.web.data.model.mysql.TroUserDeptRelationEntity;
import io.shulie.tro.web.data.model.mysql.UserDeptEntity;
import io.shulie.tro.web.data.param.user.UserDeptConditionQueryParam;
import io.shulie.tro.web.data.param.user.UserDeptQueryParam;
import io.shulie.tro.web.data.param.user.UserDeptRelationBatchDeleteParam;
import io.shulie.tro.web.data.param.user.UserDeptRelationBatchUserDeleteParam;
import io.shulie.tro.web.data.param.user.UserQueryParam;
import io.shulie.tro.web.data.result.user.DeptQueryResult;
import io.shulie.tro.web.data.result.user.UserDeptConditionResult;
import io.shulie.tro.web.data.result.user.UserDeptResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/11/3 5:06 下午
 * @Description:
 */
@Component
public class TroDeptUserRelationDAOImpl implements TroDeptUserRelationDAO {

    @Autowired
    private TroUserDeptRelationMapper userDeptRelationMapper;

    @Override
    public List<UserDeptResult> selectList(UserDeptQueryParam queryParam) {
        List<UserDeptResult> userDeptResultList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(queryParam.getUserIdList())) {
            return userDeptResultList;
        }
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserIds(queryParam.getUserIdList());
        List<UserDeptEntity> userDeptEntityList = userDeptRelationMapper.selectDeptByCondition(userQueryParam);
        if (CollectionUtils.isEmpty(userDeptEntityList)) {
            return userDeptResultList;
        }
        Map<Long, List<UserDeptEntity>> userDeptMap = userDeptEntityList.stream().collect(
            Collectors.groupingBy(UserDeptEntity::getId));

        if (CollectionUtils.isNotEmpty(userDeptEntityList)) {
            for (String userId : queryParam.getUserIdList()) {
                if (!userDeptMap.containsKey(Long.parseLong(userId))) {
                    continue;
                }
                UserDeptResult deptResult = new UserDeptResult();
                deptResult.setUserId(Long.parseLong(userId));
                List<DeptQueryResult> deptList = Lists.newArrayList();
                List<UserDeptEntity> userDeptEntities = userDeptMap.get(Long.parseLong(userId));
                for (UserDeptEntity entity : userDeptEntities) {
                    DeptQueryResult deptQueryResult = new DeptQueryResult();
                    deptQueryResult.setId(entity.getDeptId());
                    deptQueryResult.setName(entity.getDeptName());
                    deptQueryResult.setParentId(entity.getParentId());
                    deptList.add(deptQueryResult);
                }
                deptResult.setDeptList(deptList);
                userDeptResultList.add(deptResult);
            }
        }

        return userDeptResultList;
    }

    @Override
    public List<Long> selectDeptIdList(UserDeptQueryParam queryParam) {
        if (CollectionUtils.isEmpty(queryParam.getUserIdList())) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<TroUserDeptRelationEntity> wrapper = new LambdaQueryWrapper();
        wrapper.in(TroUserDeptRelationEntity::getUserId, queryParam.getUserIdList());
        List<TroUserDeptRelationEntity> userDeptEntityList = userDeptRelationMapper.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(userDeptEntityList)) {
            return userDeptEntityList.stream().map(TroUserDeptRelationEntity::getDeptId).map(Long::parseLong).collect(
                Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<UserDeptConditionResult> selectUserByDeptIds(UserDeptConditionQueryParam queryParam) {
        List<UserDeptConditionResult> userDeptConditionResultList = Lists.newArrayList();
        List<UserDeptEntity> userDeptEntityList = userDeptRelationMapper.selectUserByDeptIds(queryParam);
        if (CollectionUtils.isEmpty(userDeptEntityList)) {
            return userDeptConditionResultList;
        }
        userDeptConditionResultList = userDeptEntityList.stream().map(userDeptEntity -> {
            UserDeptConditionResult conditionResult = new UserDeptConditionResult();
            conditionResult.setId(userDeptEntity.getId());
            conditionResult.setName(userDeptEntity.getName());
            conditionResult.setDeptId(userDeptEntity.getDeptId());
            conditionResult.setDeptName(userDeptEntity.getDeptName());
            conditionResult.setParentId(userDeptEntity.getParentId());
            return conditionResult;
        }).collect(Collectors.toList());
        return userDeptConditionResultList;
    }

    /**
     * 新增用户部门关系
     *
     * @param list
     * @return
     */
    @Override
    public int batchInsert(List<TroUserDeptRelationEntity> list) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return userDeptRelationMapper.batchInsert(list);
    }

    /**
     * 删除用户部门关系
     *
     * @param deleteParam
     * @return
     */
    @Override
    public int deleteByDeptIdAndUserIds(UserDeptRelationBatchDeleteParam deleteParam) {
        if (null == deleteParam.getDeptId() || CollectionUtils.isEmpty(deleteParam.getUserIds())) {
            return 0;
        }
        LambdaQueryWrapper<TroUserDeptRelationEntity> wrapper = new LambdaQueryWrapper();
        wrapper.eq(TroUserDeptRelationEntity::getDeptId, deleteParam.getDeptId());
        wrapper.in(TroUserDeptRelationEntity::getUserId, deleteParam.getUserIds());
        return userDeptRelationMapper.delete(wrapper);
    }

    @Override
    public int deleteByUserIds(UserDeptRelationBatchUserDeleteParam deleteParam) {
        if (CollectionUtils.isEmpty(deleteParam.getUserIdList())) {
            return 0;
        }
        LambdaQueryWrapper<TroUserDeptRelationEntity> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(TroUserDeptRelationEntity::getUserId, deleteParam.getUserIdList());
        return userDeptRelationMapper.delete(queryWrapper);
    }
}
