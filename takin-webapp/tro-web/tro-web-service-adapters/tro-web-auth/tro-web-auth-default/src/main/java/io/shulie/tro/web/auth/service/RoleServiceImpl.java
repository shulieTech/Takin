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

package io.shulie.tro.web.auth.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.pamirs.tro.entity.dao.auth.TAuthorityMapper;
import com.pamirs.tro.entity.dao.auth.TBaseRoleMapper;
import com.pamirs.tro.entity.dao.auth.TRoleMapper;
import com.pamirs.tro.entity.dao.auth.TRoleUserRelationMapper;
import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import com.pamirs.tro.entity.domain.entity.auth.BaseRole;
import com.pamirs.tro.entity.domain.entity.auth.BaseRoleExample;
import com.pamirs.tro.entity.domain.entity.auth.Role;
import com.pamirs.tro.entity.domain.entity.auth.RoleExample;
import com.pamirs.tro.entity.domain.entity.auth.RoleUserRelation;
import com.pamirs.tro.entity.domain.entity.auth.RoleUserRelationExample;
import io.shulie.tro.web.auth.api.RoleService;
import io.shulie.tro.web.auth.api.UserService;
import io.shulie.tro.web.data.dao.user.TroRoleDAO;
import io.shulie.tro.web.data.dao.user.TroRoleResourceAuthDAO;
import io.shulie.tro.web.data.dao.user.TroRoleUserRelationDAO;
import io.shulie.tro.web.data.param.user.RoleCreateParam;
import io.shulie.tro.web.data.param.user.RoleDeleteParam;
import io.shulie.tro.web.data.param.user.RoleQueryParam;
import io.shulie.tro.web.data.param.user.RoleUpdateParam;
import io.shulie.tro.web.data.param.user.UserRoleRelationBatchDeleteParam;
import io.shulie.tro.web.data.param.user.UserRoleRelationCreateParam;
import io.shulie.tro.web.data.result.user.RoleQueryResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: fanxx
 * @Date: 2020/9/2 下午3:15
 * @Description:
 */
@Component
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    TRoleMapper TRoleMapper;

    @Autowired
    TRoleUserRelationMapper TRoleUserRelationMapper;

    @Autowired
    UserService userService;

    @Autowired
    TAuthorityMapper TAuthorityMapper;

    @Autowired
    TBaseRoleMapper TBaseRoleMapper;

    @Autowired
    private TroRoleDAO troRoleDAO;

    @Autowired
    private TroRoleUserRelationDAO troRoleUserRelationDAO;

    @Autowired
    private TroRoleResourceAuthDAO troRoleResourceAuthDAO;

    //4.5.1 -->
    @Override
    public List<RoleQueryResult> selectList(RoleQueryParam queryParam) {
        return troRoleDAO.selectList(queryParam);
    }

    @Override
    public RoleQueryResult selectDetail(RoleQueryParam queryParam) {
        return troRoleDAO.selectDetail(queryParam);
    }

    @Override
    public int insert(RoleCreateParam createParam) {
        return troRoleDAO.insert(createParam);
    }

    @Override
    public int update(RoleUpdateParam updateParam) {
        return troRoleDAO.update(updateParam);
    }

    @Override
    public int delete(RoleDeleteParam deleteParam) {
        int count = troRoleDAO.delete(deleteParam);
        //删除角色时同时删除角色关联的权限
        troRoleResourceAuthDAO.clearRoleAuth(deleteParam.getIds());
        return count;
    }


    @Override
    public List<BaseRole> getAllBaseRoles() {
        return TBaseRoleMapper.selectByExample(new BaseRoleExample());
    }

    @Override
    public List<Role> getRolesByIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Lists.newArrayList();
        }
        RoleExample roleExample = new RoleExample();
        RoleExample.Criteria criteria = roleExample.createCriteria();
        criteria.andIdIn(roleIds);
        return TRoleMapper.selectByExample(roleExample);
    }

    @Override
    public List<Role> getRoleByUserId(Long userId) {
        if (Objects.isNull(userId)) {
            return Lists.newArrayList();
        }
        List<Role> roleList = Lists.newArrayList();
        RoleUserRelationExample roleUserRelationExample = new RoleUserRelationExample();
        RoleUserRelationExample.Criteria roleUserRelationExampleCriteria = roleUserRelationExample.createCriteria();
        roleUserRelationExampleCriteria.andUserIdEqualTo(String.valueOf(userId));
        List<RoleUserRelation> relations = TRoleUserRelationMapper.selectByExample(roleUserRelationExample);
        if (CollectionUtils.isNotEmpty(relations)) {
            List<Long> roleIds = Lists.newArrayList();
            for (RoleUserRelation relation : relations) {
                String roleId = relation.getRoleId();
                roleIds.add(Long.parseLong(roleId));
            }
            roleList = getRolesByIds(roleIds);
        }
        return roleList;
    }

    @Override
    public List<Role> getRoleByApplicationId(List<String> applicationIds) {
        if (CollectionUtils.isEmpty(applicationIds)) {
            return Lists.newArrayList();
        }
        RoleExample example = new RoleExample();
        RoleExample.Criteria criteria = example.createCriteria();
        criteria.andApplicationIdIn(applicationIds.stream().map(Long::parseLong).collect(Collectors.toList()));
        return TRoleMapper.selectByExample(example);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addRoleForApplication(List<TApplicationMnt> applicationMnts) {
        int count = 0;
        if (CollectionUtils.isEmpty(applicationMnts)) {
            return count;
        }
        List<BaseRole> baseRoleList = getAllBaseRoles();
        for (TApplicationMnt applicationMnt : applicationMnts) {
            //先查询该应用下是否存在角色
            RoleExample roleExample = new RoleExample();
            RoleExample.Criteria criteria = roleExample.createCriteria();
            criteria.andApplicationIdEqualTo(applicationMnt.getApplicationId());
            List<Role> roleList = TRoleMapper.selectByExample(roleExample);
            if (CollectionUtils.isNotEmpty(roleList)) {
                log.warn("该应用下角色已存在：" + applicationMnt.getApplicationId());
                continue;
            }
            for (BaseRole baseRole : baseRoleList) {
                Role role = new Role();
                role.setApplicationId(applicationMnt.getApplicationId());
                role.setName(baseRole.getName());
                role.setCode(baseRole.getCode());
                role.setAlias(applicationMnt.getApplicationId() + "_" + baseRole.getName());
                role.setDescription(baseRole.getDescription());
                int res = TRoleMapper.insertSelective(role);
                count = count + res;
            }
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRoleByApplicationIds(List<String> applicationIds) {
        int count = 0;
        if (CollectionUtils.isEmpty(applicationIds)) {
            return count;
        }
        List<Role> roleList = getRoleByApplicationId(applicationIds);
        for (Role role : roleList) {
            int res = TRoleMapper.deleteByPrimaryKey(role.getId());
            count = count + res;
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addUserToRole(String roleId, List<String> userIds) {
        int count = 0;
        if (CollectionUtils.isEmpty(userIds) || StringUtils.isBlank(roleId)) {
            return count;
        }
        for (String userId : userIds) {
            RoleUserRelationExample roleUserRelationExample = new RoleUserRelationExample();
            RoleUserRelationExample.Criteria criteria = roleUserRelationExample.createCriteria();
            criteria.andRoleIdEqualTo(roleId);
            criteria.andUserIdEqualTo(userId);
            List<RoleUserRelation> relationList = TRoleUserRelationMapper.selectByExample(roleUserRelationExample);
            if (CollectionUtils.isNotEmpty(relationList)) {
                log.warn("该用户已加入角色:{},{}", roleId, userId);
                continue;
            }
            RoleUserRelation relation = new RoleUserRelation();
            relation.setRoleId(roleId);
            relation.setUserId(userId);
            int res = TRoleUserRelationMapper.insertSelective(relation);
            count = count + res;
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserFromRole(String roleId, List<String> userIds) {
        int count = 0;
        if (CollectionUtils.isEmpty(userIds) || StringUtils.isBlank(roleId)) {
            return count;
        }
        RoleUserRelationExample relationExample = new RoleUserRelationExample();
        RoleUserRelationExample.Criteria criteria = relationExample.createCriteria();
        criteria.andRoleIdEqualTo(roleId);
        criteria.andUserIdIn(userIds);
        List<RoleUserRelation> relationList = TRoleUserRelationMapper.selectByExample(relationExample);
        if (CollectionUtils.isNotEmpty(relationList)) {
            for (RoleUserRelation relation : relationList) {
                int res = TRoleUserRelationMapper.deleteByPrimaryKey(relation.getId());
                count = count + res;
            }
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBatchUserByRoleId(String roleId) {
        int count = 0;
        if (StringUtils.isBlank(roleId)) {
            return count;
        }
        List<String> userIds = userService.getUserIdListByRoleId(roleId);
        return deleteUserFromRole(roleId, userIds);
    }

    /**
     * 给用户分配角色
     *
     * @param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertRoleToUser(UserRoleRelationCreateParam param) {
        return troRoleUserRelationDAO.insertRoleToUser(param);
    }

    /**
     * 给用户重置（清空）角色
     *
     * @param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserRoleRelationBatch(UserRoleRelationBatchDeleteParam param) {
        return troRoleUserRelationDAO.deleteUserRoleRelationBatch(param);
    }
}
