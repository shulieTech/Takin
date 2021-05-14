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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import io.shulie.tro.web.data.dao.user.TroRoleDAO;
import io.shulie.tro.web.data.dao.user.TroRoleUserRelationDAO;
import io.shulie.tro.web.data.dao.user.TroUserDAO;
import io.shulie.tro.web.data.mapper.mysql.TroRoleUserRelationMapper;
import io.shulie.tro.web.data.model.mysql.TroRoleUserRelationEntity;
import io.shulie.tro.web.data.model.mysql.UserRoleEntity;
import io.shulie.tro.web.data.param.user.UserQueryParam;
import io.shulie.tro.web.data.param.user.UserRoleQueryParam;
import io.shulie.tro.web.data.param.user.UserRoleRelationBatchDeleteParam;
import io.shulie.tro.web.data.param.user.UserRoleRelationBatchQueryParam;
import io.shulie.tro.web.data.param.user.UserRoleRelationCreateParam;
import io.shulie.tro.web.data.param.user.UserRoleRelationQueryParam;
import io.shulie.tro.web.data.result.user.RoleQueryResult;
import io.shulie.tro.web.data.result.user.UserCommonResult;
import io.shulie.tro.web.data.result.user.UserRoleRelationResult;
import io.shulie.tro.web.data.result.user.UserRoleResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/11/3 5:55 下午
 * @Description:
 */
@Component
public class TroRoleUserRelationDAOImpl implements TroRoleUserRelationDAO {

    @Autowired
    private TroRoleUserRelationMapper userRoleRelationMapper;

    @Autowired
    private TroRoleUserRelationMapper roleUserRelationMapper;

    @Autowired
    private TroRoleDAO troRoleDAO;

    @Autowired
    private TroUserDAO troUserDAO;

    /**
     * 根据用户id查询角色信息
     *
     * @param queryParam
     * @return
     */
    @Override
    public List<UserRoleResult> selectList(UserRoleQueryParam queryParam) {
        List<UserRoleResult> userRoleResultList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(queryParam.getUserIdList())) {
            return userRoleResultList;
        }
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserIds(queryParam.getUserIdList());
        List<UserRoleEntity> userRoleEntityList = userRoleRelationMapper.selectRoleByCondition(userQueryParam);
        if (CollectionUtils.isEmpty(userRoleEntityList)) {
            return userRoleResultList;
        }

        Map<Long, List<UserRoleEntity>> userRoleMap = userRoleEntityList.stream().collect(Collectors.groupingBy(UserRoleEntity::getId));

        for (String userId : queryParam.getUserIdList()) {
            if (!userRoleMap.containsKey(Long.parseLong(userId))) {
                continue;
            }
            UserRoleResult roleResult = new UserRoleResult();
            List<UserRoleEntity> entities = userRoleMap.get(Long.parseLong(userId));
            List<RoleQueryResult> roleList = Lists.newArrayList();
            for (UserRoleEntity entity : entities) {
                RoleQueryResult roleQueryResult = new RoleQueryResult();
                roleQueryResult.setId(entity.getRoleId());
                roleQueryResult.setName(entity.getRoleName());
                roleList.add(roleQueryResult);
            }
            roleResult.setUserId(Long.parseLong(userId));
            roleResult.setRoleList(roleList);
            userRoleResultList.add(roleResult);
        }
        return userRoleResultList;
    }


    /**
     * 给用户分配角色
     *
     * @param param
     * @return
     */
    @Override
    public int insertRoleToUser(UserRoleRelationCreateParam param) {

        //用户信息
        List<Long> ids = param.getUserId().stream().map(Long::valueOf).collect(Collectors.toList());
        List<UserCommonResult> userList = troUserDAO.selectUserByIds(ids);
        //用户ID
        List<String> userIds = userList.stream().map(UserCommonResult::getUserId).map(String::valueOf).collect(Collectors.toList());
        //用户ID为空，return
        if (CollectionUtils.isEmpty(userIds)) {
            return 0;
        }

        //角色信息
        List<RoleQueryResult> roleList = troRoleDAO.selectRoleByIds(param.getRoleId());
        //角色ID
        List<String> roleIds = roleList.stream().map(RoleQueryResult::getId).map(String::valueOf).collect(Collectors.toList());

        LambdaQueryWrapper<TroRoleUserRelationEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(TroRoleUserRelationEntity::getUserId, userIds);
        //给用户分配角色时，先清空用户角色
        int i = roleUserRelationMapper.delete(wrapper);
        //角色id为空，只清空用户角色
        if (CollectionUtils.isEmpty(roleIds)) {
            return i;
        }

        List<TroRoleUserRelationEntity> list = Lists.newArrayList();
        for (String userId : userIds) {
            for (String roleId : roleIds) {
                TroRoleUserRelationEntity entity = new TroRoleUserRelationEntity();
                entity.setUserId(userId);
                entity.setRoleId(roleId);
                list.add(entity);
            }
        }
        return roleUserRelationMapper.batchInsertRoleToUser(list);
    }

    /**
     * 新增用户角色关系
     *
     * @param list
     * @return
     */
    @Override
    public int batchInsert(List<TroRoleUserRelationEntity> list) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return roleUserRelationMapper.batchInsertRoleToUser(list);
    }

    /**
     * 查询该用户下是否存在这个角色
     *
     * @param param
     * @return
     */
    @Override
    public UserRoleRelationResult selectUserRoleRelation(UserRoleRelationQueryParam param) {
        LambdaQueryWrapper<TroRoleUserRelationEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(TroRoleUserRelationEntity::getId,
                TroRoleUserRelationEntity::getRoleId,
                TroRoleUserRelationEntity::getUserId);
        wrapper.eq(TroRoleUserRelationEntity::getUserId, param.getUserId());
        wrapper.eq(TroRoleUserRelationEntity::getRoleId, param.getRoleId());
        TroRoleUserRelationEntity entity = roleUserRelationMapper.selectOne(wrapper);
        if (Objects.isNull(entity)) {
            return null;
        }
        UserRoleRelationResult result = new UserRoleRelationResult();
        result.setId(entity.getId());
        result.setRoleId(entity.getRoleId());
        result.setUserId(entity.getUserId());
        return result;
    }

    /**
     * 查询用户下所有角色列表
     *
     * @param param
     * @return
     */
    @Override
    public List<UserRoleRelationResult> selectUserRoleRelationBatch(UserRoleRelationBatchQueryParam param) {
        List<UserRoleRelationResult> relationResultList = Lists.newArrayList();
        LambdaQueryWrapper<TroRoleUserRelationEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(TroRoleUserRelationEntity::getId,
                TroRoleUserRelationEntity::getRoleId,
                TroRoleUserRelationEntity::getUserId);
        wrapper.in(TroRoleUserRelationEntity::getUserId, param.getUserIdList());
        List<TroRoleUserRelationEntity> entityList = roleUserRelationMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return relationResultList;
        }
        relationResultList = entityList.stream().map(entity -> {
            UserRoleRelationResult relationResult = new UserRoleRelationResult();
            relationResult.setId(entity.getId());
            relationResult.setUserId(entity.getUserId());
            relationResult.setRoleId(entity.getRoleId());
            return relationResult;
        }).collect(Collectors.toList());
        return relationResultList;
    }

    /**
     * 给用户重置（清空）角色
     *
     * @param param
     * @return
     */
    @Override
    public int deleteUserRoleRelationBatch(UserRoleRelationBatchDeleteParam param) {
        UserRoleRelationBatchQueryParam queryParam = new UserRoleRelationBatchQueryParam();
        queryParam.setUserIdList(param.getUserId());
        List<UserRoleRelationResult> relationResultList = selectUserRoleRelationBatch(queryParam);
        if (!CollectionUtils.isEmpty(relationResultList)) {
            List<Long> relationIdList = relationResultList.stream().map(UserRoleRelationResult::getId).collect(Collectors.toList());
            return roleUserRelationMapper.deleteBatchIds(relationIdList);
        }
        return 0;
    }
}
