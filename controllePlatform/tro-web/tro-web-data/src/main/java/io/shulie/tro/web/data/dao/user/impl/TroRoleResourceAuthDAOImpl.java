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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.shulie.tro.web.data.dao.user.TroResourceDAO;
import io.shulie.tro.web.data.dao.user.TroRoleResourceAuthDAO;
import io.shulie.tro.web.data.mapper.mysql.TroAuthorityMapper;
import io.shulie.tro.web.data.model.mysql.TroAuthorityEntity;
import io.shulie.tro.web.data.param.user.RoleResourceAuthDeleteParam;
import io.shulie.tro.web.data.param.user.RoleResourceAuthQueryParam;
import io.shulie.tro.web.data.param.user.RoleResourceAuthUpdateParam;
import io.shulie.tro.web.data.result.user.ResourceAuthResult;
import io.shulie.tro.web.data.result.user.ResourceMenuResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/11/3 8:31 下午
 * @Description:
 */
@Component
public class TroRoleResourceAuthDAOImpl implements TroRoleResourceAuthDAO {

    @Autowired
    private TroAuthorityMapper authorityMapper;

    @Autowired
    private TroResourceDAO resourceDAO;

    @Override
    public List<ResourceAuthResult> selectList(RoleResourceAuthQueryParam queryParam) {
        LambdaQueryWrapper<TroAuthorityEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(TroAuthorityEntity::getRoleId, queryParam.getRoleIdList());
        if(!Objects.isNull(queryParam.getStatus())){
            wrapper.eq(TroAuthorityEntity::getStatus, queryParam.getStatus() ? 0 : 1);
        }
        List<TroAuthorityEntity> authorityEntityList = authorityMapper.selectList(wrapper);
        List<ResourceAuthResult> resourceAuthResultList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(authorityEntityList)) {
            return resourceAuthResultList;
        }
        List<ResourceMenuResult> resourceMenuResultList = resourceDAO.selectAuthConfigMenu();
        resourceAuthResultList = authorityEntityList.stream().map(authorityEntity -> {
            ResourceAuthResult resourceAuthResult = new ResourceAuthResult();
            resourceAuthResult.setId(authorityEntity.getId());
            resourceAuthResult.setResoureId(Long.parseLong(authorityEntity.getResourceId()));
            resourceAuthResult.setStatus(authorityEntity.getStatus() == 0);
            Optional optional = resourceMenuResultList
                    .stream()
                    .filter(resourceMenuResult -> String.valueOf(resourceMenuResult.getId()).equals(authorityEntity.getResourceId()))
                    .findFirst();
            if (optional.isPresent()) {
                ResourceMenuResult resourceMenuResult = (ResourceMenuResult) optional.get();
                resourceAuthResult.setResourceName(resourceMenuResult.getName());
                resourceAuthResult.setResourceCode(resourceMenuResult.getCode());
            }
            if (StringUtils.isNotBlank(authorityEntity.getAction())) {
                List<Integer> actionList = Lists.newArrayList();
                JSONArray array = JSON.parseArray(authorityEntity.getAction());
                for (Object object : array) {
                    actionList.add(Integer.parseInt(object.toString()));
                }
                resourceAuthResult.setActionList(actionList);
            }
            if (StringUtils.isNotBlank(authorityEntity.getScope())) {
                List<Integer> scopeList = Lists.newArrayList();
                JSONArray array = JSON.parseArray(authorityEntity.getScope());
                for (Object object : array) {
                    scopeList.add(Integer.parseInt(object.toString()));
                }
                resourceAuthResult.setScopeList(scopeList);
            }
            return resourceAuthResult;
        }).collect(Collectors.toList());
        return resourceAuthResultList;
    }

    @Override
    public void updateRoleAuth(List<RoleResourceAuthUpdateParam> updateParamList) {
        if (!CollectionUtils.isEmpty(updateParamList)) {
            updateParamList.forEach(updateParam -> {
                LambdaQueryWrapper<TroAuthorityEntity> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(TroAuthorityEntity::getRoleId, updateParam.getRoleId());
                queryWrapper.eq(TroAuthorityEntity::getResourceId, updateParam.getResoureId());
                TroAuthorityEntity isExistEntity = authorityMapper.selectOne(queryWrapper);
                TroAuthorityEntity entity = new TroAuthorityEntity();
                entity.setRoleId(String.valueOf(updateParam.getRoleId()));
                entity.setResourceId(String.valueOf(updateParam.getResoureId()));
                //是否启用
                if(!Objects.isNull(updateParam.getStatus())){
                    entity.setStatus(updateParam.getStatus());
                }
                if (updateParam.getActionList() != null) {
                    entity.setAction(Arrays.toString(updateParam.getActionList().toArray(new Integer[0])));
                }
                if (updateParam.getScopeList() != null) {
                    entity.setScope(Arrays.toString(updateParam.getScopeList().toArray(new Integer[0])));
                }
                if (Objects.isNull(isExistEntity)) {
                    //新增
                    authorityMapper.insert(entity);
                } else {
                    //更新
                    entity.setId(isExistEntity.getId());
                    authorityMapper.updateById(entity);
                }
            });
        }
    }

    @Override
    public void deleteRoleAuth(List<RoleResourceAuthDeleteParam> deleteParamList) {
        if (!CollectionUtils.isEmpty(deleteParamList)) {
            Set<Long> deleteSets = Sets.newHashSet();
            deleteParamList.forEach(deleteParam -> {
                LambdaQueryWrapper<TroAuthorityEntity> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(TroAuthorityEntity::getRoleId, deleteParam.getRoleId());
                queryWrapper.eq(TroAuthorityEntity::getResourceId, deleteParam.getResoureId());
                TroAuthorityEntity isExistEntity = authorityMapper.selectOne(queryWrapper);
                if (!Objects.isNull(isExistEntity)) {
                    deleteSets.add(isExistEntity.getId());
                }
            });
            if (deleteSets.size() > 0) {
                authorityMapper.deleteBatchIds(new ArrayList<>(deleteSets));
            }
        }
    }

    @Override
    public void clearRoleAuth(List<Long> roleIdLIst) {
        if (!CollectionUtils.isEmpty(roleIdLIst)) {
            LambdaQueryWrapper<TroAuthorityEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(TroAuthorityEntity::getRoleId, roleIdLIst);
            List<TroAuthorityEntity> authorityEntityList = authorityMapper.selectList(queryWrapper);
            if (!CollectionUtils.isEmpty(authorityEntityList)) {
                List<Long> toDeleteIdList = authorityEntityList
                        .stream()
                        .map(TroAuthorityEntity::getId)
                        .collect(Collectors.toList());
                authorityMapper.deleteBatchIds(toDeleteIdList);
            }
        }
    }
}
