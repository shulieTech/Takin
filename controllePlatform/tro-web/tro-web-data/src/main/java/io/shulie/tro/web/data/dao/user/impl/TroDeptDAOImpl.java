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
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.shulie.tro.web.data.dao.user.TroDeptDAO;
import io.shulie.tro.web.data.mapper.mysql.TroDeptMapper;
import io.shulie.tro.web.data.model.mysql.TroDeptEntity;
import io.shulie.tro.web.data.param.user.DeptCreateParam;
import io.shulie.tro.web.data.param.user.DeptDeleteParam;
import io.shulie.tro.web.data.param.user.DeptUpdateParam;
import io.shulie.tro.web.data.result.user.DeptQueryResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author ZhangXT
 * @Description
 * @Date 2020/11/13 17:06
 */
@Component
public class TroDeptDAOImpl implements TroDeptDAO {

    @Autowired
    private TroDeptMapper troDeptMapper;

    @Override
    public DeptQueryResult selectDetailById(Long id) {
        if (!Objects.isNull(id)) {
            TroDeptEntity entity = troDeptMapper.selectById(id);
            if (!Objects.isNull(entity)) {
                DeptQueryResult queryResult = new DeptQueryResult();
                queryResult.setId(entity.getId());
                queryResult.setName(entity.getName());
                queryResult.setParentId(entity.getParentId());
                return queryResult;
            }
        }
        return null;
    }

    @Override
    public DeptQueryResult selectDetailByName(String name) {
        if (StringUtils.isNotBlank(name)) {
            LambdaQueryWrapper<TroDeptEntity> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.eq(TroDeptEntity::getName, name);
            TroDeptEntity entity = troDeptMapper.selectOne(queryWrapper);
            if (!Objects.isNull(entity)) {
                DeptQueryResult queryResult = new DeptQueryResult();
                queryResult.setId(entity.getId());
                queryResult.setName(entity.getName());
                queryResult.setParentId(entity.getParentId());
                return queryResult;
            }
        }
        return null;
    }

    @Override
    public int insert(DeptCreateParam createParam) {
        if (StringUtils.isBlank(createParam.getName())) {
            return 0;
        }
        TroDeptEntity entity = new TroDeptEntity();
        entity.setCode(createParam.getCode());
        entity.setName(createParam.getName());
        entity.setParentId(createParam.getParentId());
        return troDeptMapper.insert(entity);
    }

    @Override
    public int update(DeptUpdateParam updateParam) {
        if (Objects.isNull(updateParam.getId())) {
            return 0;
        }
        TroDeptEntity entity = new TroDeptEntity();
        entity.setId(updateParam.getId());
        entity.setName(updateParam.getName());
        entity.setCode(updateParam.getCode());
        entity.setParentId(updateParam.getParentId());
        return troDeptMapper.updateById(entity);
    }

    @Override
    public int delete(DeptDeleteParam deleteParam) {
        if (CollectionUtils.isNotEmpty(deleteParam.getDeptIdList())) {
            return troDeptMapper.deleteBatchIds(deleteParam.getDeptIdList());
        }
        return 0;
    }

    /**
     * 所有部门
     *
     * @return
     */
    @Override
    public List<TroDeptEntity> getAllDept() {
        LambdaQueryWrapper<TroDeptEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(TroDeptEntity::getId,
            TroDeptEntity::getParentId,
            TroDeptEntity::getSequence,
            TroDeptEntity::getName,
            TroDeptEntity::getCode);

        return troDeptMapper.selectList(wrapper);
    }

    /**
     * 递归查询上级部门，直到根节点
     *
     * @param depts
     * @return
     */
    @Override
    public List<TroDeptEntity> recursionDept(List<TroDeptEntity> depts) {
        //部门ID
        List<Long> ids = depts.stream().distinct().map(TroDeptEntity::getId).collect(Collectors.toList());
        //过滤掉已有的上级部门，减少查询次数
        List<Long> parentIds = depts.stream().filter(
            data -> data.getParentId() != null && !ids.contains(data.getParentId())).map(TroDeptEntity::getParentId)
            .distinct().collect(Collectors.toList());
        //为空代表已经获取所有信息
        if (CollectionUtils.isEmpty(parentIds)) {
            return depts;
        }
        LambdaQueryWrapper<TroDeptEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(TroDeptEntity::getId,
            TroDeptEntity::getParentId,
            TroDeptEntity::getSequence,
            TroDeptEntity::getName,
            TroDeptEntity::getCode);
        wrapper.in(TroDeptEntity::getId, parentIds);
        List<TroDeptEntity> list = troDeptMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return depts;
        }
        depts.addAll(list);
        return recursionDept(depts);
    }
}
