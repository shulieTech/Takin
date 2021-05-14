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
import java.util.stream.Collectors;

import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import io.shulie.tro.web.data.dao.user.TroResourceDAO;
import io.shulie.tro.web.data.mapper.mysql.TroResourceMapper;
import io.shulie.tro.web.data.model.mysql.TroResourceEntity;
import io.shulie.tro.web.data.param.user.ResourceMenuQueryParam;
import io.shulie.tro.web.data.result.user.ResourceMenuResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/11/3 7:40 下午
 * @Description:
 */
@Component
public class TroResourceDAOImpl implements TroResourceDAO {

    @Autowired
    private TroResourceMapper resourceMapper;

    @Override
    public List<ResourceMenuResult> selectAuthConfigMenu() {
        LambdaQueryWrapper<TroResourceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(TroResourceEntity::getId,
                TroResourceEntity::getParentId,
                TroResourceEntity::getCode,
                TroResourceEntity::getName,
                TroResourceEntity::getValue,
                TroResourceEntity::getSequence,
                TroResourceEntity::getAction);
        wrapper.eq(TroResourceEntity::getType, 0);
        wrapper.isNotNull(TroResourceEntity::getValue);
        // 软删不要
        wrapper.eq(TroResourceEntity::getIsDeleted,false);
        wrapper.ne(TroResourceEntity::getCode, "dashboard");
        wrapper.ne(TroResourceEntity::getValue, "");
        List<TroResourceEntity> resourceEntityList = resourceMapper.selectList(wrapper);
        List<ResourceMenuResult> resourceMenuResultList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(resourceEntityList)) {
            return resourceMenuResultList;
        }
        resourceMenuResultList = resourceEntityList.stream().map(resourceEntity -> {
            ResourceMenuResult resourceMenuResult = new ResourceMenuResult();
            resourceMenuResult.setId(resourceEntity.getId());
            resourceMenuResult.setParentId(resourceEntity.getParentId());
            resourceMenuResult.setCode(resourceEntity.getCode());
            resourceMenuResult.setName(resourceEntity.getName());
            resourceMenuResult.setValue(resourceEntity.getValue());
            resourceMenuResult.setSequence(resourceEntity.getSequence());
            if (StringUtils.isNotBlank(resourceEntity.getAction())) {
                List<Integer> actionList = Lists.newArrayList();
                JSONArray array = JSON.parseArray(resourceEntity.getAction());
                for (Object object : array) {
                    actionList.add(Integer.parseInt(object.toString()));
                }
                resourceMenuResult.setActionList(actionList);
            }
            return resourceMenuResult;
        }).collect(Collectors.toList());
        return resourceMenuResultList;
    }

    @Override
    public List<ResourceMenuResult> selectMenu(ResourceMenuQueryParam queryParam) {
        LambdaQueryWrapper<TroResourceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(TroResourceEntity::getId,
                TroResourceEntity::getParentId,
                TroResourceEntity::getCode,
                TroResourceEntity::getName,
                TroResourceEntity::getValue,
                TroResourceEntity::getSequence,
                TroResourceEntity::getAction,
                TroResourceEntity::getValue);
        wrapper.eq(TroResourceEntity::getType, 0);
        if (!CollectionUtils.isEmpty(queryParam.getResourceIdList())) {
            wrapper.in(TroResourceEntity::getId, queryParam.getResourceIdList());
        }
        List<TroResourceEntity> resourceEntityList = resourceMapper.selectList(wrapper);
        List<ResourceMenuResult> resourceMenuResultList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(resourceEntityList)) {
            return resourceMenuResultList;
        }
        resourceMenuResultList = resourceEntityList.stream().map(resourceEntity -> {
            ResourceMenuResult resourceMenuResult = new ResourceMenuResult();
            resourceMenuResult.setId(resourceEntity.getId());
            resourceMenuResult.setParentId(resourceEntity.getParentId());
            resourceMenuResult.setCode(resourceEntity.getCode());
            resourceMenuResult.setName(resourceEntity.getName());
            resourceMenuResult.setValue(resourceEntity.getValue());
            resourceMenuResult.setSequence(resourceEntity.getSequence());
            if (StringUtils.isNotBlank(resourceEntity.getAction())) {
                List<Integer> actionList = Lists.newArrayList();
                JSONArray array = JSON.parseArray(resourceEntity.getAction());
                for (Object object : array) {
                    actionList.add(Integer.parseInt(object.toString()));
                }
                resourceMenuResult.setActionList(actionList);
            }
            if(StringUtils.isNotBlank(resourceEntity.getValue())){
                List<String> urlList = Lists.newArrayList();
                JSONArray array = JSON.parseArray(resourceEntity.getValue());
                for (Object object : array) {
                    urlList.add(object.toString());
                }
                resourceMenuResult.setUrlList(urlList);
            }
            return resourceMenuResult;
        }).collect(Collectors.toList());
        return resourceMenuResultList;
    }
}
