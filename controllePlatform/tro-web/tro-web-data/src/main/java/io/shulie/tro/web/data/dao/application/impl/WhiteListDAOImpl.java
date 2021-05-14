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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.common.vo.whitelist.WhiteListVO;
import io.shulie.tro.web.data.dao.application.WhiteListDAO;
import io.shulie.tro.web.data.mapper.mysql.WhiteListMapper;
import io.shulie.tro.web.data.model.mysql.WhiteListEntity;
import io.shulie.tro.web.data.param.whitelist.WhitelistGlobalOrPartParam;
import io.shulie.tro.web.data.param.whitelist.WhitelistSaveOrUpdateParam;
import io.shulie.tro.web.data.param.whitelist.WhitelistSearchParam;
import io.shulie.tro.web.data.result.whitelist.WhitelistResult;
import io.shulie.tro.web.data.util.MPUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author liuchuan
 * @date 2021/4/8 2:11 下午
 */
@Service
public class WhiteListDAOImpl extends ServiceImpl<WhiteListMapper, WhiteListEntity>
        implements WhiteListDAO, MPUtil<WhiteListEntity> {

    @Override
    public void batchSaveOrUpdate(List<WhitelistSaveOrUpdateParam> params) {
        if(CollectionUtils.isEmpty(params)) {
            return;
        }
        List<WhiteListEntity> entities = params.stream().map(param -> {
            WhiteListEntity entity = new WhiteListEntity();
            BeanUtils.copyProperties(param,entity);
            return entity;
        }).collect(Collectors.toList());
        this.saveOrUpdateBatch(entities);
    }


    @Override
    public List<WhitelistResult> listByApplicationId(Long applicationId) {
        List<WhiteListEntity> entities =this.list(
            this.getLQW().eq(WhiteListEntity::getApplicationId, applicationId).eq(WhiteListEntity::getIsDeleted,false));
        return getWhitelistResults(entities);
    }

    private List<WhitelistResult> getWhitelistResults(List<WhiteListEntity> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return Lists.newArrayList();
        }
        return entities.stream().map(entity -> {
            WhitelistResult result = new WhitelistResult();
            BeanUtil.copyProperties(entity, result);
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public List<WhitelistResult> getList(WhitelistSearchParam param) {
        LambdaQueryWrapper<WhiteListEntity> wrapper = getWhiteListEntityLambdaQueryWrapper(param);
        List<WhiteListEntity> entities = this.list(wrapper);
        return getWhitelistResults(entities);
    }

    @Override
    public WhitelistResult selectById(Long wlistId) {
        WhiteListEntity entity = this.getById(wlistId);
        if(entity == null) {
            return null;
        }
        WhitelistResult result = new WhitelistResult();
        BeanUtil.copyProperties(entity,result);
        return result;
    }

    @Override
    public PagingList<WhiteListVO> pagingList(WhitelistSearchParam param) {
        LambdaQueryWrapper<WhiteListEntity> wrapper = getWhiteListEntityLambdaQueryWrapper(param);
        wrapper.orderByDesc(WhiteListEntity::getGmtModified);
        Page<WhiteListEntity> page = new Page<>(param.getCurrent() + 1, param.getPageSize());
        IPage<WhiteListEntity> entityIPage = this.page(page,wrapper);
        if (entityIPage.getTotal() == 0) {
            return PagingList.empty();
        }
        List<WhiteListVO> vos = entityIPage.getRecords().stream().map(entity -> {
            WhiteListVO vo = new WhiteListVO();
            BeanUtils.copyProperties(entity, vo);
            vo.setApplicationId(String.valueOf(entity.getApplicationId()));
            vo.setWlistId(String.valueOf(entity.getWlistId()));
            vo.setDbId(String.valueOf(entity.getWlistId()));
            vo.setInterfaceType(Integer.valueOf(entity.getType()));
            return vo;
        }).collect(Collectors.toList());
        return PagingList.of(vos, entityIPage.getTotal());
    }

    private LambdaQueryWrapper<WhiteListEntity> getWhiteListEntityLambdaQueryWrapper(WhitelistSearchParam param) {
        LambdaQueryWrapper<WhiteListEntity>  wrapper = this.getLQW();
        if(CollectionUtil.isNotEmpty(param.getIds())) {
            wrapper.in(WhiteListEntity::getApplicationId, param.getIds());
        }
        if(StringUtils.isNotBlank(param.getInterfaceName())) {
            wrapper.like(WhiteListEntity::getInterfaceName, param.getInterfaceName());
        }
        if(param.getCustomerId() != null) {
            wrapper.eq(WhiteListEntity::getCustomerId, param.getCustomerId());
        }
        if(param.getIsGlobal() != null) {
            wrapper.eq(WhiteListEntity::getIsGlobal, param.getIsGlobal());
        }
        if(param.getUseYn() != null) {
            wrapper.eq(WhiteListEntity::getUseYn, param.getUseYn());
        }
        wrapper.eq(WhiteListEntity::getIsDeleted, false);
        return wrapper;
    }

    @Override
    public void updateWhitelistGlobal(WhitelistGlobalOrPartParam param) {
        WhiteListEntity entity = new WhiteListEntity();
        BeanUtils.copyProperties(param,entity);
        this.updateById(entity);
    }
}