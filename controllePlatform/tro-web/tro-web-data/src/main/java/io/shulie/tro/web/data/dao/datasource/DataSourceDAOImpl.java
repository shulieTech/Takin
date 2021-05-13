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

package io.shulie.tro.web.data.dao.datasource;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.data.mapper.mysql.TroDbresourceMapper;
import io.shulie.tro.web.data.model.mysql.TroDbresourceEntity;
import io.shulie.tro.web.data.param.datasource.DataSourceCreateParam;
import io.shulie.tro.web.data.param.datasource.DataSourceDeleteParam;
import io.shulie.tro.web.data.param.datasource.DataSourceQueryParam;
import io.shulie.tro.web.data.param.datasource.DataSourceSingleQueryParam;
import io.shulie.tro.web.data.param.datasource.DataSourceUpdateParam;
import io.shulie.tro.web.data.result.datasource.DataSourceResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/12/30 10:21 上午
 * @Description:
 */
@Slf4j
@Component
public class DataSourceDAOImpl implements DataSourceDAO {

    @Autowired
    private TroDbresourceMapper datasourceMapper;

    @Override
    public PagingList<DataSourceResult> selectPage(DataSourceQueryParam queryParam) {
        LambdaQueryWrapper<TroDbresourceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
                TroDbresourceEntity::getId,
                TroDbresourceEntity::getType,
                TroDbresourceEntity::getName,
                TroDbresourceEntity::getJdbcUrl,
                TroDbresourceEntity::getUsername,
                TroDbresourceEntity::getCustomerId,
                TroDbresourceEntity::getUserId,
                TroDbresourceEntity::getCreateTime,
                TroDbresourceEntity::getUpdateTime);
        if (!Objects.isNull(queryParam.getType())) {
            wrapper.eq(TroDbresourceEntity::getType, queryParam.getType());
        }
        if (StringUtils.isNotBlank(queryParam.getName())) {
            wrapper.like(TroDbresourceEntity::getName, queryParam.getName());
        }
        if (StringUtils.isNotBlank(queryParam.getJdbcUrl())) {
            wrapper.like(TroDbresourceEntity::getJdbcUrl, queryParam.getJdbcUrl());
        }
        if (CollectionUtils.isNotEmpty(queryParam.getDataSourceIdList())) {
            wrapper.in(TroDbresourceEntity::getId, queryParam.getDataSourceIdList());
        }
        Page<TroDbresourceEntity> page = new Page<>(queryParam.getCurrent(), queryParam.getPageSize());
        wrapper.orderByDesc(TroDbresourceEntity::getUpdateTime);

        IPage<TroDbresourceEntity> troDbresourceEntityPage = datasourceMapper.selectPage(page, wrapper);
        if (CollectionUtils.isEmpty(troDbresourceEntityPage.getRecords())) {
            return PagingList.empty();
        }
        List<DataSourceResult> dataSourceResultList = troDbresourceEntityPage.getRecords().stream().map(entity -> {
            DataSourceResult dataSourceResult = new DataSourceResult();
            BeanUtils.copyProperties(entity, dataSourceResult);
            return dataSourceResult;
        }).collect(Collectors.toList());
        return PagingList.of(dataSourceResultList, page.getTotal());
    }

    @Override
    public int insert(DataSourceCreateParam createParam) {
        TroDbresourceEntity entity = new TroDbresourceEntity();
        BeanUtils.copyProperties(createParam, entity);
        return datasourceMapper.insert(entity);
    }

    @Override
    public int update(DataSourceUpdateParam updateParam) {
        if (!Objects.isNull(updateParam.getId())) {
            TroDbresourceEntity entity = new TroDbresourceEntity();
            BeanUtils.copyProperties(updateParam, entity);
            return datasourceMapper.updateById(entity);
        }
        return 0;
    }

    @Override
    public int delete(DataSourceDeleteParam deleteParam) {
        if (CollectionUtils.isNotEmpty(deleteParam.getIdList())) {
            return datasourceMapper.deleteBatchIds(deleteParam.getIdList());
        }
        return 0;
    }

    @Override
    public DataSourceResult selectSingle(DataSourceSingleQueryParam queryParam) {
        LambdaQueryWrapper<TroDbresourceEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (!Objects.isNull(queryParam.getId())) {
            queryWrapper.eq(TroDbresourceEntity::getId, queryParam.getId());
        }
        if (StringUtils.isNotBlank(queryParam.getName())) {
            queryWrapper.eq(TroDbresourceEntity::getName, queryParam.getName());
        }
        if (StringUtils.isNotBlank(queryParam.getJdbcUrl())) {
            queryWrapper.eq(TroDbresourceEntity::getJdbcUrl, queryParam.getJdbcUrl());
        }
        if (!Objects.isNull(queryParam.getCustomerId())) {
            queryWrapper.eq(TroDbresourceEntity::getCustomerId, queryParam.getCustomerId());
        }
        queryWrapper.eq(TroDbresourceEntity::getIsDeleted, 0);
        List<TroDbresourceEntity> datasourceEntityList = datasourceMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(datasourceEntityList)) {
            if (datasourceEntityList.size() > 1) {
                log.warn("存在多个同名数据源，请及时处理脏数据");
            }
            TroDbresourceEntity datasourceEntity = datasourceEntityList.get(0);
            DataSourceResult dataSourceResult = new DataSourceResult();
            dataSourceResult.setId(datasourceEntity.getId());
            dataSourceResult.setType(datasourceEntity.getType());
            dataSourceResult.setName(datasourceEntity.getName());
            dataSourceResult.setJdbcUrl(datasourceEntity.getJdbcUrl());
            dataSourceResult.setUsername(datasourceEntity.getUsername());
            dataSourceResult.setPassword(datasourceEntity.getPassword());
            dataSourceResult.setCreateTime(datasourceEntity.getCreateTime());
            dataSourceResult.setUpdateTime(datasourceEntity.getUpdateTime());
            dataSourceResult.setCustomerId(datasourceEntity.getCustomerId());
            dataSourceResult.setUserId(datasourceEntity.getUserId());
            return dataSourceResult;
        }
        return null;
    }

    @Override
    public List<DataSourceResult> selectList(DataSourceQueryParam queryParam) {
        LambdaQueryWrapper<TroDbresourceEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (CollectionUtils.isNotEmpty(queryParam.getDataSourceIdList())) {
            queryWrapper.in(TroDbresourceEntity::getId, queryParam.getDataSourceIdList());
            queryWrapper.eq(TroDbresourceEntity::getIsDeleted, 0);
        }
        List<TroDbresourceEntity> datasourceEntityList = datasourceMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(datasourceEntityList)) {
            return datasourceEntityList.stream().map(entity -> {
                DataSourceResult dataSourceResult = new DataSourceResult();
                BeanUtils.copyProperties(entity, dataSourceResult);
                return dataSourceResult;
            }).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
