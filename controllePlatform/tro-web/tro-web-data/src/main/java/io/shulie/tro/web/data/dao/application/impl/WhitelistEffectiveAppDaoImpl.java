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

package io.shulie.tro.web.data.dao.application.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import io.shulie.tro.web.data.dao.application.WhitelistEffectiveAppDao;
import io.shulie.tro.web.data.mapper.mysql.WhitelistEffectiveAppMapper;
import io.shulie.tro.web.data.model.mysql.WhitelistEffectiveAppEntity;
import io.shulie.tro.web.data.param.whitelist.WhitelistAddPartAppNameParam;
import io.shulie.tro.web.data.param.whitelist.WhitelistEffectiveAppDeleteParam;
import io.shulie.tro.web.data.param.whitelist.WhitelistEffectiveAppSearchParam;
import io.shulie.tro.web.data.param.whitelist.WhitelistUpdatePartAppNameParam;
import io.shulie.tro.web.data.result.whitelist.WhitelistEffectiveAppResult;
import io.shulie.tro.web.data.util.MPUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.data.dao.application.impl
 * @date 2021/4/14 10:15 上午
 */
@Component
public class WhitelistEffectiveAppDaoImpl extends ServiceImpl<WhitelistEffectiveAppMapper, WhitelistEffectiveAppEntity>
    implements WhitelistEffectiveAppDao, MPUtil<WhitelistEffectiveAppEntity> {

    @Override
    public List<WhitelistEffectiveAppResult> getList(WhitelistEffectiveAppSearchParam param) {
        LambdaQueryWrapper<WhitelistEffectiveAppEntity> wrapper = this.getLQW();
        if(param.getCustomerId() != null) {
            wrapper.eq(WhitelistEffectiveAppEntity::getCustomerId, param.getCustomerId());
        }
        if(param.getUserId() != null) {
            wrapper.eq(WhitelistEffectiveAppEntity::getUserId, param.getUserId());
        }
        if(param.getWlistId() != null) {
            wrapper.eq(WhitelistEffectiveAppEntity::getWlistId, param.getWlistId());
        }
        if(StringUtils.isNotBlank(param.getInterfaceName())) {
            wrapper.eq(WhitelistEffectiveAppEntity::getInterfaceName, param.getInterfaceName());
        }
        if(CollectionUtils.isNotEmpty(param.getWlistIds())) {
            wrapper.in(WhitelistEffectiveAppEntity::getWlistId, param.getWlistIds());
        }
        return getWhitelistEffectiveAppResults(wrapper);
    }

    @Override
    public List<WhitelistEffectiveAppResult> getListByWhiteIds(List<Long> ids) {
        LambdaQueryWrapper<WhitelistEffectiveAppEntity> wrapper = this.getLQW();
        wrapper.in(WhitelistEffectiveAppEntity::getWlistId,ids);
        return getWhitelistEffectiveAppResults(wrapper);
    }

    private List<WhitelistEffectiveAppResult> getWhitelistEffectiveAppResults(LambdaQueryWrapper<WhitelistEffectiveAppEntity> wrapper) {
        List<WhitelistEffectiveAppEntity> entities = this.list(wrapper);
        if (CollectionUtils.isEmpty(entities)) {
            return Lists.newArrayList();
        }
        return entities.stream().map(entity -> {
            WhitelistEffectiveAppResult result = new WhitelistEffectiveAppResult();
            BeanUtils.copyProperties(entity, result);
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public void updatePartAppName(List<WhitelistUpdatePartAppNameParam> params) {
        List<WhitelistEffectiveAppEntity> entities = params.stream().map(param -> {
            WhitelistEffectiveAppEntity entity = new WhitelistEffectiveAppEntity();
            BeanUtils.copyProperties(param,entity);
            return entity;
        }).collect(Collectors.toList());
        this.saveOrUpdateBatch(entities);
    }

    @Override
    public void addPartAppName(List<WhitelistAddPartAppNameParam> params) {
        List<WhitelistEffectiveAppEntity> entities = params.stream().map(param -> {
            WhitelistEffectiveAppEntity entity = new WhitelistEffectiveAppEntity();
            BeanUtils.copyProperties(param,entity);
            return entity;
        }).collect(Collectors.toList());
        this.saveBatch(entities);
    }

    @Override
    public void batchDelete(WhitelistEffectiveAppDeleteParam param) {
        if(CollectionUtils.isEmpty(param.getWlistIds())) {
            return;
        }
        LambdaQueryWrapper<WhitelistEffectiveAppEntity> wrapper = this.getLQW();
        if(param.getCustomerId() != null) {
            wrapper.eq(WhitelistEffectiveAppEntity::getCustomerId, param.getCustomerId());
        }
        if(param.getUserId() != null) {
            wrapper.eq(WhitelistEffectiveAppEntity::getUserId, param.getUserId());
        }
        if(param.getWlistIds() != null) {
            wrapper.in(WhitelistEffectiveAppEntity::getWlistId, param.getWlistIds());
        }
        List<WhitelistEffectiveAppEntity> entities = this.list(wrapper);
        if(CollectionUtils.isEmpty(entities)) {
            return;
        }
        this.removeByIds(entities.stream().map(WhitelistEffectiveAppEntity::getId).collect(Collectors.toList()));
    }

    @Override
    public void delete(WhitelistEffectiveAppDeleteParam param) {
        LambdaQueryWrapper<WhitelistEffectiveAppEntity> wrapper = this.getLQW();
        if(param.getCustomerId() != null) {
            wrapper.eq(WhitelistEffectiveAppEntity::getCustomerId, param.getCustomerId());
        }
        if(param.getUserId() != null) {
            wrapper.eq(WhitelistEffectiveAppEntity::getUserId, param.getUserId());
        }
        if(param.getWlistId() != null) {
            wrapper.eq(WhitelistEffectiveAppEntity::getWlistId, param.getWlistId());
        }
        List<WhitelistEffectiveAppEntity> entities = this.list(wrapper);
        if(CollectionUtils.isEmpty(entities)) {
            return;
        }
        this.removeByIds(entities.stream().map(WhitelistEffectiveAppEntity::getId).collect(Collectors.toList()));
    }
}
