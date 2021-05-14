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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.data.mapper.mysql.LeakcheckConfigMapper;
import io.shulie.tro.web.data.model.mysql.LeakcheckConfigEntity;
import io.shulie.tro.web.data.param.leakcheck.LeakCheckConfigCreateParam;
import io.shulie.tro.web.data.param.leakcheck.LeakCheckConfigDeleteParam;
import io.shulie.tro.web.data.param.leakcheck.LeakCheckConfigQueryParam;
import io.shulie.tro.web.data.param.leakcheck.LeakCheckConfigSingleQueryParam;
import io.shulie.tro.web.data.param.leakcheck.LeakCheckConfigUpdateParam;
import io.shulie.tro.web.data.result.leakcheck.LeakCheckConfigResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/12/31 2:43 下午
 * @Description:
 */
@Slf4j
@Component
public class LeakCheckConfigDAOImpl implements LeakCheckConfigDAO {

    @Autowired
    private LeakcheckConfigMapper leakcheckConfigMapper;

    @Override
    public PagingList<LeakCheckConfigResult> selectPage(LeakCheckConfigQueryParam queryParam) {
        return null;
    }

    @Override
    public int insert(LeakCheckConfigCreateParam createParam) {
        if (!Objects.isNull(createParam.getBusinessActivityId())
                && !Objects.isNull(createParam.getDatasourceId())
                && CollectionUtils.isNotEmpty(createParam.getSqlIdList())) {
            LeakCheckConfigSingleQueryParam singleQueryParam = new LeakCheckConfigSingleQueryParam();
            singleQueryParam.setBusinessActivityId(createParam.getBusinessActivityId());
            singleQueryParam.setDatasourceId(createParam.getDatasourceId());
            LeakCheckConfigResult existCheckConfigResult = selectSingle(singleQueryParam);
            if (Objects.isNull(existCheckConfigResult)) {
                LeakcheckConfigEntity insertConfigEntity = new LeakcheckConfigEntity();
                insertConfigEntity.setBusinessActivityId(createParam.getBusinessActivityId());
                insertConfigEntity.setDatasourceId(createParam.getDatasourceId());
                String sqlIdString = StringUtils.join(createParam.getSqlIdList().toArray(), ",");
                insertConfigEntity.setLeakcheckSqlIds(sqlIdString);
                return leakcheckConfigMapper.insert(insertConfigEntity);
            } else {
                String sqlIdString = existCheckConfigResult.getLeakcheckSqlIds();
                List<Long> currentSqlIdList = Arrays.stream(sqlIdString.split(",")).map(Long::parseLong).distinct().collect(Collectors.toList());
                currentSqlIdList.addAll(createParam.getSqlIdList());
                String newSqlIdString = StringUtils.join(currentSqlIdList.toArray(), ",");
                LeakcheckConfigEntity updateConfigEntity = new LeakcheckConfigEntity();
                updateConfigEntity.setId(existCheckConfigResult.getId());
                updateConfigEntity.setLeakcheckSqlIds(newSqlIdString);
                leakcheckConfigMapper.updateById(updateConfigEntity);
            }
        }
        return 0;
    }

    @Override
    public int update(LeakCheckConfigUpdateParam updateParam) {
        if (!Objects.isNull(updateParam.getBusinessActivityId())
                && !Objects.isNull(updateParam.getDatasourceId())
                && CollectionUtils.isNotEmpty(updateParam.getSqlIdList())) {
            LeakCheckConfigSingleQueryParam singleQueryParam = new LeakCheckConfigSingleQueryParam();
            singleQueryParam.setBusinessActivityId(updateParam.getBusinessActivityId());
            singleQueryParam.setDatasourceId(updateParam.getDatasourceId());
            LeakCheckConfigResult existCheckConfigResult = selectSingle(singleQueryParam);
            if (!Objects.isNull(existCheckConfigResult)) {
                String newSqlIdString = StringUtils.join(updateParam.getSqlIdList().toArray(), ",");
                LeakcheckConfigEntity updateConfigEntity = new LeakcheckConfigEntity();
                updateConfigEntity.setId(existCheckConfigResult.getId());
                updateConfigEntity.setLeakcheckSqlIds(newSqlIdString);
                return leakcheckConfigMapper.updateById(updateConfigEntity);
            }
        }
        return 0;
    }

    @Override
    public int delete(LeakCheckConfigDeleteParam deleteParam) {
        if (CollectionUtils.isNotEmpty(deleteParam.getIds())) {
            LambdaQueryWrapper<LeakcheckConfigEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(LeakcheckConfigEntity::getId, deleteParam.getIds());
            List<LeakcheckConfigEntity> leakcheckConfigEntityList = leakcheckConfigMapper.selectList(queryWrapper);
            if (CollectionUtils.isNotEmpty(leakcheckConfigEntityList)) {
                List<Long> toDeleteIds = leakcheckConfigEntityList
                        .stream()
                        .map(LeakcheckConfigEntity::getId)
                        .collect(Collectors.toList());
                return leakcheckConfigMapper.deleteBatchIds(toDeleteIds);
            }
        }
        return 0;
    }

    @Override
    public LeakCheckConfigResult selectSingle(LeakCheckConfigSingleQueryParam singleQueryParam) {
        LambdaQueryWrapper<LeakcheckConfigEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LeakcheckConfigEntity::getBusinessActivityId, singleQueryParam.getBusinessActivityId());
        queryWrapper.eq(LeakcheckConfigEntity::getDatasourceId, singleQueryParam.getDatasourceId());
        queryWrapper.eq(LeakcheckConfigEntity::getIsDeleted, 0);
        List<LeakcheckConfigEntity> leakcheckConfigEntityList = leakcheckConfigMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(leakcheckConfigEntityList)) {
            if (leakcheckConfigEntityList.size() > 1) {
                log.warn("存在多个相同数据源，请及时处理脏数据，业务活动id：" + singleQueryParam.getBusinessActivityId());
            }
            LeakcheckConfigEntity entity = leakcheckConfigEntityList.get(0);
            LeakCheckConfigResult result = new LeakCheckConfigResult();
            result.setId(entity.getId());
            result.setBusinessActivityId(entity.getBusinessActivityId());
            result.setDatasourceId(entity.getDatasourceId());
            result.setLeakcheckSqlIds(entity.getLeakcheckSqlIds());
            result.setCustomerId(entity.getCustomerId());
            result.setUserId(entity.getUserId());
            return result;
        }
        return null;
    }

    @Override
    public List<LeakCheckConfigResult> selectList(LeakCheckConfigQueryParam queryParam) {
        List<LeakCheckConfigResult> resultList = Lists.newArrayList();
        LambdaQueryWrapper<LeakcheckConfigEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (CollectionUtils.isNotEmpty(queryParam.getBusinessActivityIds())) {
            queryWrapper.in(LeakcheckConfigEntity::getBusinessActivityId, queryParam.getBusinessActivityIds());
        }
        if (CollectionUtils.isNotEmpty(queryParam.getDatasourceIds())) {
            queryWrapper.in(LeakcheckConfigEntity::getDatasourceId, queryParam.getDatasourceIds());
        }
        queryWrapper.eq(LeakcheckConfigEntity::getIsDeleted, 0);
        List<LeakcheckConfigEntity> leakcheckConfigEntityList = leakcheckConfigMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(leakcheckConfigEntityList)) {
            resultList = leakcheckConfigEntityList.stream().map(entity -> {
                LeakCheckConfigResult result = new LeakCheckConfigResult();
                result.setId(entity.getId());
                result.setBusinessActivityId(entity.getBusinessActivityId());
                result.setDatasourceId(entity.getDatasourceId());
                result.setLeakcheckSqlIds(entity.getLeakcheckSqlIds());
                result.setCustomerId(entity.getCustomerId());
                result.setUserId(entity.getUserId());
                return result;
            }).collect(Collectors.toList());
        }
        return resultList;
    }
}
