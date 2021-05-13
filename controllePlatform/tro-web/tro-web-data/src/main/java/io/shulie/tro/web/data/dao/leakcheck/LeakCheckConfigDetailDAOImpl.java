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

package io.shulie.tro.web.data.dao.leakcheck;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import io.shulie.tro.web.data.mapper.mysql.LeakcheckConfigDetailMapper;
import io.shulie.tro.web.data.model.mysql.LeakcheckConfigDetailEntity;
import io.shulie.tro.web.data.param.leakcheck.LeakCheckConfigDetailCreateParam;
import io.shulie.tro.web.data.param.leakcheck.LeakCheckConfigDetailDeleteParam;
import io.shulie.tro.web.data.param.leakcheck.LeakCheckConfigDetailQueryParam;
import io.shulie.tro.web.data.param.leakcheck.LeakCheckConfigDetailUpdateParam;
import io.shulie.tro.web.data.result.leakcheck.LeakCheckConfigBatchDetailResult;
import io.shulie.tro.web.data.result.leakcheck.LeakCheckConfigSingleDetailResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/12/31 2:44 下午
 * @Description:
 */
@Component
public class LeakCheckConfigDetailDAOImpl implements LeakCheckConfigDetailDAO {

    @Autowired
    private LeakcheckConfigDetailMapper detailMapper;

    @Override
    public LeakCheckConfigSingleDetailResult selectSingle(LeakCheckConfigDetailQueryParam queryParam) {
        LambdaQueryWrapper<LeakcheckConfigDetailEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(LeakcheckConfigDetailEntity::getId, queryParam.getIds());
        List<LeakcheckConfigDetailEntity> detailEntityList = detailMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(detailEntityList)) {
            LeakCheckConfigSingleDetailResult detailResult = new LeakCheckConfigSingleDetailResult();
            detailResult.setId(detailEntityList.get(0).getId());
            detailResult.setDatasourceId(detailEntityList.get(0).getDatasourceId());
            List<String> sqlList = detailEntityList
                .stream()
                .map(LeakcheckConfigDetailEntity::getSqlContent)
                .collect(Collectors.toList());
            detailResult.setSqls(sqlList);
            return detailResult;
        }
        return null;
    }

    @Override
    public List<LeakCheckConfigBatchDetailResult> selectList(LeakCheckConfigDetailQueryParam queryParam) {
        List<LeakCheckConfigBatchDetailResult> detailResultList = Lists.newArrayList();
        LambdaQueryWrapper<LeakcheckConfigDetailEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (CollectionUtils.isNotEmpty(queryParam.getIds())) {
            queryWrapper.in(LeakcheckConfigDetailEntity::getId, queryParam.getIds());
        }
        if (CollectionUtils.isNotEmpty(queryParam.getDatasourceIds())) {
            queryWrapper.in(LeakcheckConfigDetailEntity::getDatasourceId, queryParam.getDatasourceIds());
        }
        List<LeakcheckConfigDetailEntity> detailEntityList = detailMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(detailEntityList)) {
            detailResultList = detailEntityList.stream().map(detailEntity -> {
                LeakCheckConfigBatchDetailResult batchDetailResult = new LeakCheckConfigBatchDetailResult();
                batchDetailResult.setId(detailEntity.getId());
                batchDetailResult.setDatasourceId(detailEntity.getDatasourceId());
                batchDetailResult.setSql(detailEntity.getSqlContent());
                return batchDetailResult;
            }).collect(Collectors.toList());
        }
        return detailResultList;
    }

    @Override
    public List<Long> insert(LeakCheckConfigDetailCreateParam createParam) {
        List<Long> sqlIdList = Lists.newArrayList();
        if (!Objects.isNull(createParam.getDatasourceId())
            && CollectionUtils.isNotEmpty(createParam.getSqlList())) {
            List<String> sqlList = createParam.getSqlList();
            sqlList.forEach(sql -> {
                if (StringUtils.isNotBlank(sql)) {
                    LeakcheckConfigDetailEntity checkConfigDetailEntity = new LeakcheckConfigDetailEntity();
                    checkConfigDetailEntity.setDatasourceId(createParam.getDatasourceId());
                    checkConfigDetailEntity.setSqlContent(sql);
                    detailMapper.insert(checkConfigDetailEntity);
                    sqlIdList.add(checkConfigDetailEntity.getId());
                }
            });
        }
        return sqlIdList;
    }

    @Override
    public int update(LeakCheckConfigDetailUpdateParam updateParam) {
        return 0;
    }

    @Override
    public int delete(LeakCheckConfigDetailDeleteParam deleteParam) {
        if (CollectionUtils.isNotEmpty(deleteParam.getIds())) {
            LambdaQueryWrapper<LeakcheckConfigDetailEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(LeakcheckConfigDetailEntity::getId, deleteParam.getIds());
            List<LeakcheckConfigDetailEntity> detailEntityList = detailMapper.selectList(queryWrapper);
            if (CollectionUtils.isNotEmpty(detailEntityList)) {
                List<Long> toDeleteIds = detailEntityList
                    .stream()
                    .map(LeakcheckConfigDetailEntity::getId)
                    .collect(Collectors.toList());
                return detailMapper.deleteBatchIds(toDeleteIds);
            }
        }
        return 0;
    }
}
