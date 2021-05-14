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
import java.util.Objects;
import java.util.stream.Collectors;

import com.alibaba.excel.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.shulie.tro.web.data.dao.user.TroRoleDAO;
import io.shulie.tro.web.data.mapper.mysql.TroAuthorityMapper;
import io.shulie.tro.web.data.mapper.mysql.TroRoleMapper;
import io.shulie.tro.web.data.model.mysql.TroRoleEntity;
import io.shulie.tro.web.data.param.user.RoleCreateParam;
import io.shulie.tro.web.data.param.user.RoleDeleteParam;
import io.shulie.tro.web.data.param.user.RoleQueryParam;
import io.shulie.tro.web.data.param.user.RoleUpdateParam;
import io.shulie.tro.web.data.result.user.RoleQueryResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/11/2 4:47 下午
 * @Description:
 */
@Component
public class TroRoleDAOImpl implements TroRoleDAO {

    @Autowired
    private TroRoleMapper troRoleMapper;

    @Autowired
    private TroAuthorityMapper authorityMapper;

    @Override
    public List<RoleQueryResult> selectList(RoleQueryParam queryParam) {
        LambdaQueryWrapper<TroRoleEntity> wrapper = new QueryWrapper<TroRoleEntity>().lambda();
        wrapper.select(
                TroRoleEntity::getId,
                TroRoleEntity::getName,
                TroRoleEntity::getDescription,
                TroRoleEntity::getCreateTime);
        if (StringUtils.isNotBlank(queryParam.getName())) {
            wrapper.like(TroRoleEntity::getName, queryParam.getName());
        }
        List<TroRoleEntity> roleEntityList = troRoleMapper.selectList(wrapper);
        return entity2Result(roleEntityList);
    }

    @Override
    public RoleQueryResult selectDetail(RoleQueryParam queryParam) {
        LambdaQueryWrapper<TroRoleEntity> wrapper = new LambdaQueryWrapper();
        if (!Objects.isNull(queryParam.getId())) {
            wrapper.eq(TroRoleEntity::getId, queryParam.getId());
        }
        if (StringUtils.isNotBlank(queryParam.getName())) {
            wrapper.eq(TroRoleEntity::getName, queryParam.getName());
        }
        RoleQueryResult result = new RoleQueryResult();
        TroRoleEntity entity = troRoleMapper.selectOne(wrapper);
        if (Objects.isNull(entity)) {
            return result;
        }
        BeanUtils.copyProperties(entity, result);
        return result;
    }

    @Override
    public int insert(RoleCreateParam createParam) {
        TroRoleEntity entity = new TroRoleEntity();
        BeanUtils.copyProperties(createParam, entity);
        return troRoleMapper.insert(entity);
    }

    @Override
    public int update(RoleUpdateParam updateParam) {
        TroRoleEntity entity = new TroRoleEntity();
        BeanUtils.copyProperties(updateParam, entity);
        return troRoleMapper.updateById(entity);
    }

    @Override
    public int delete(RoleDeleteParam deleteParam) {
        return troRoleMapper.deleteBatchIds(deleteParam.getIds());
    }

    /**
     * 根据角色id查询
     *
     * @param roleIds
     * @return count
     */
    @Override
    public List<RoleQueryResult> selectRoleByIds(List<String> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        List<TroRoleEntity> list = troRoleMapper.selectBatchIds(roleIds);
        return entity2Result(list);
    }


    private List<RoleQueryResult> entity2Result(List<TroRoleEntity> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(entity -> {
            RoleQueryResult result = new RoleQueryResult();
            result.setId(entity.getId());
            result.setName(entity.getName());
            result.setDescription(entity.getDescription());
            result.setCreateTime(entity.getCreateTime());
            return result;
        }).collect(Collectors.toList());
    }
}
