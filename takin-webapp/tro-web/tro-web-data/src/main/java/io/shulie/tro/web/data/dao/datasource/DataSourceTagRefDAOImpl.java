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
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.shulie.tro.web.data.mapper.mysql.DatasourceTagRefMapper;
import io.shulie.tro.web.data.model.mysql.DatasourceTagRefEntity;
import io.shulie.tro.web.data.result.datasource.DataSourceTagRefResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/12/29 4:42 下午
 * @Description:
 */
@Slf4j
@Component
public class DataSourceTagRefDAOImpl implements DataSourceTagRefDAO {

    @Autowired
    private DatasourceTagRefMapper tagRefMapper;

    @Override
    public void addDataSourceTagRef(List<Long> tagIds, Long dataSourceId) {
        if (CollectionUtils.isNotEmpty(tagIds) && dataSourceId != null) {
            tagIds.forEach(tagId -> {
                DatasourceTagRefEntity datasourceTagRefEntity = new DatasourceTagRefEntity();
                datasourceTagRefEntity.setDatasourceId(dataSourceId);
                datasourceTagRefEntity.setTagId(tagId);
                tagRefMapper.insert(datasourceTagRefEntity);
            });
        }
    }

    @Override
    public List<DataSourceTagRefResult> selectDataSourceTagRefByScriptId(Long dataSourceId) {
        return null;
    }

    @Override
    public void deleteByIds(List<Long> dataSourceTagRefIds) {

    }

    @Override
    public List<DataSourceTagRefResult> selectDataSourceTagRefByTagIds(List<Long> tagIds) {
        if (CollectionUtils.isEmpty(tagIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<DatasourceTagRefEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(
                DatasourceTagRefEntity::getId,
                DatasourceTagRefEntity::getDatasourceId,
                DatasourceTagRefEntity::getTagId);
        queryWrapper.in(DatasourceTagRefEntity::getTagId, tagIds);
        List<DatasourceTagRefEntity> datasourceTagRefEntityList = tagRefMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(datasourceTagRefEntityList)) {
            return Collections.emptyList();
        }
        return datasourceTagRefEntityList.stream().map(entity -> {
            DataSourceTagRefResult refResult = new DataSourceTagRefResult();
            refResult.setId(entity.getId());
            refResult.setDataSourceId(entity.getDatasourceId());
            refResult.setTagId(entity.getTagId());
            return refResult;
        }).collect(Collectors.toList());
    }

    @Override
    public List<DataSourceTagRefResult> selectTagRefByDataSourceIds(List<Long> dataSourceIds) {
        if (CollectionUtils.isEmpty(dataSourceIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<DatasourceTagRefEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(
                DatasourceTagRefEntity::getId,
                DatasourceTagRefEntity::getDatasourceId,
                DatasourceTagRefEntity::getTagId);
        queryWrapper.in(DatasourceTagRefEntity::getDatasourceId, dataSourceIds);
        List<DatasourceTagRefEntity> datasourceTagRefEntityList = tagRefMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(datasourceTagRefEntityList)) {
            return Collections.emptyList();
        }
        return datasourceTagRefEntityList.stream().map(entity -> {
            DataSourceTagRefResult refResult = new DataSourceTagRefResult();
            refResult.setId(entity.getId());
            refResult.setDataSourceId(entity.getDatasourceId());
            refResult.setTagId(entity.getTagId());
            return refResult;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteByDataSourceId(Long dataSourceId) {
        LambdaQueryWrapper<DatasourceTagRefEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(
                DatasourceTagRefEntity::getId,
                DatasourceTagRefEntity::getDatasourceId,
                DatasourceTagRefEntity::getTagId);
        queryWrapper.eq(DatasourceTagRefEntity::getDatasourceId, dataSourceId);
        List<DatasourceTagRefEntity> datasourceTagRefEntityList = tagRefMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(datasourceTagRefEntityList)) {
            List<Long> refIds = datasourceTagRefEntityList.stream().map(DatasourceTagRefEntity::getId).collect(Collectors.toList());
            tagRefMapper.deleteBatchIds(refIds);
        }
    }
}
