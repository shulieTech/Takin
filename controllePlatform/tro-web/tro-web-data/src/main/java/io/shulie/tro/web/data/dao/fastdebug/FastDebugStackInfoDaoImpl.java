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

package io.shulie.tro.web.data.dao.fastdebug;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import io.shulie.tro.web.data.convert.fastdebug.FastDebugStackInfoEntityConvert;
import io.shulie.tro.web.data.convert.fastdebug.FastDebugStackInfoResultConvert;
import io.shulie.tro.web.data.mapper.custom.fastdebug.FastDebugStackInfoMapperBatch;
import io.shulie.tro.web.data.mapper.mysql.FastDebugStackInfoMapper;
import io.shulie.tro.web.data.model.mysql.FastDebugStackInfoEntity;
import io.shulie.tro.web.data.param.fastdebug.FastDebugStackInfoCreateParam;
import io.shulie.tro.web.data.param.fastdebug.FastDebugStackInfoQueryParam;
import io.shulie.tro.web.data.result.fastdebug.FastDebugStackInfoResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @Author: mubai
 * @Date: 2020-12-28 11:59
 * @Description:
 */

@Repository
public class FastDebugStackInfoDaoImpl implements FastDebugStackInfoDao {
    @Autowired
    private FastDebugStackInfoMapper fastDebugStackInfoMapper;

    @Autowired
    private FastDebugStackInfoMapperBatch fastDebugStackInfoMapperBatch;

    @Override
    public void insert(FastDebugStackInfoCreateParam createParam) {
        if (createParam == null) {
            return;
        }
        FastDebugStackInfoEntity entity = new FastDebugStackInfoEntity();
        BeanUtils.copyProperties(createParam, entity);
        fastDebugStackInfoMapper.insert(entity);

    }

    @Override
    public void insert(List<FastDebugStackInfoCreateParam> fastDebugStackInfoCreateParamList) {
        if (CollectionUtils.isEmpty(fastDebugStackInfoCreateParamList)) {
            return;
        }
        List<FastDebugStackInfoEntity> entityList = FastDebugStackInfoEntityConvert.INSTANCE.ofs(
            fastDebugStackInfoCreateParamList);
        fastDebugStackInfoMapperBatch.saveBatch(entityList);
    }

    @Override
    public List<FastDebugStackInfoResult> selectByExample(FastDebugStackInfoQueryParam param) {
        if (param == null || StringUtils.isBlank(param.getTraceId())) {
            throw new RuntimeException("入参不能为null | traceId 不能为空");
        }
        LambdaQueryWrapper<FastDebugStackInfoEntity> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(param.getTraceId())) {
            wrapper.eq(FastDebugStackInfoEntity::getTraceId, param.getTraceId());
        }
        if (StringUtils.isNotBlank(param.getRpcId())) {
            wrapper.eq(FastDebugStackInfoEntity::getRpcId, param.getRpcId());
        }
        if (StringUtils.isNotBlank(param.getLogLevel())) {
            wrapper.eq(FastDebugStackInfoEntity::getLevel, param.getLogLevel());
        }
        if (param.getType() != null) {
            wrapper.eq(FastDebugStackInfoEntity::getType, param.getType());
        }
        if (StringUtils.isNotBlank(param.getAppName())) {
            wrapper.eq(FastDebugStackInfoEntity::getAppName, param.getAppName());
        }
        if (StringUtils.isNotBlank(param.getAgentId())) {
            wrapper.eq(FastDebugStackInfoEntity::getAgentId, param.getAgentId());
        }
       if (param.getIsStack() != null) {
            wrapper.eq(FastDebugStackInfoEntity::getIsStack, param.getIsStack());
        }
        wrapper.orderByAsc(FastDebugStackInfoEntity::getId);
        List<FastDebugStackInfoEntity> entityList = fastDebugStackInfoMapper.selectList(wrapper);
        return entityList == null ? Lists.newArrayList() : FastDebugStackInfoResultConvert.INSTANCE.ofs(entityList);
    }

    @Override
    public Long selectByExampleCount(FastDebugStackInfoQueryParam param) {
        List<FastDebugStackInfoResult> results = selectByExample(param);
        return  results.stream().map(FastDebugStackInfoResult::toString).distinct().count();
    }

    @Override
    public void clearHistoryData(Date beforeDate) {
        if (null==beforeDate){
            return;
        }
        LambdaQueryWrapper<FastDebugStackInfoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.lt(FastDebugStackInfoEntity::getGmtCreate,beforeDate);
        fastDebugStackInfoMapper.delete(wrapper);
    }

    @Override
    public FastDebugStackInfoResult getById(Long id) {
        FastDebugStackInfoEntity entity = fastDebugStackInfoMapper.selectById(id);
        if(entity == null) {
            return  null;
        }
        return FastDebugStackInfoResultConvert.INSTANCE.of(entity);
    }
}
