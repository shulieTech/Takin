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

package io.shulie.tro.web.data.dao.tracenode;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.pamirs.pradar.ResultCode;
import io.shulie.tro.web.data.convert.tracenode.TraceNodeInfoConvert;
import io.shulie.tro.web.data.mapper.custom.tracenode.CustomTraceNodeInfoMapper;
import io.shulie.tro.web.data.model.mysql.TraceNodeInfoEntity;
import io.shulie.tro.web.data.param.tracenode.TraceNodeInfoParam;
import io.shulie.tro.web.data.result.tracenode.TraceNodeInfoResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.data.dao.tracenode
 * @date 2020/12/29 12:13 下午
 */
@Component
public class TraceNodeDaoImpl implements TraceNodeDao {
    @Autowired
    private CustomTraceNodeInfoMapper customTraceNodeInfoMapper;
    @Override
    public void batchInsert(List<TraceNodeInfoParam> params) {
        if (CollectionUtils.isEmpty(params)) {
            return;
        }
        List<TraceNodeInfoEntity> entities = TraceNodeInfoConvert.INSTANCE.ofList(params);
        customTraceNodeInfoMapper.saveBatch(entities,entities.size());
    }

    @Override
    public TraceNodeInfoResult getNode(String traceId, String rpcId,Long customerId,Integer logType,String agentId,String appName) {
        LambdaQueryWrapper<TraceNodeInfoEntity> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(traceId)) {
            wrapper.eq(TraceNodeInfoEntity::getTraceId,traceId);
        }
        if(StringUtils.isNotBlank(rpcId)) {
            wrapper.eq(TraceNodeInfoEntity::getRpcId,rpcId);
        }
        wrapper.eq(TraceNodeInfoEntity::getCustomerId,customerId);
        if(logType != null) {
            wrapper.eq(TraceNodeInfoEntity::getLogType,logType);
        }
        if(StringUtils.isNotBlank(agentId)) {
            wrapper.eq(TraceNodeInfoEntity::getAgentId,agentId);
        }
        if(StringUtils.isNotBlank(appName)) {
            wrapper.eq(TraceNodeInfoEntity::getAppName,appName);
        }
        wrapper.orderByDesc(TraceNodeInfoEntity::getGmtModified);
        List<TraceNodeInfoEntity> entities = customTraceNodeInfoMapper.list(wrapper);
        if(entities == null || entities.size() == 0) {
            return null;
        }
        TraceNodeInfoEntity entity = entities.get(0);
        return TraceNodeInfoConvert.INSTANCE.of(entity);
    }

    @Override
    public List<TraceNodeInfoResult> getNodeList(String traceId, Long customerId) {
        LambdaQueryWrapper<TraceNodeInfoEntity> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(traceId)) {
            wrapper.eq(TraceNodeInfoEntity::getTraceId,traceId);
        }
        wrapper.eq(TraceNodeInfoEntity::getCustomerId,customerId);
        wrapper.orderByDesc(TraceNodeInfoEntity::getId);
        List<TraceNodeInfoEntity> entities = customTraceNodeInfoMapper.list(wrapper);
        if(entities == null || entities.size() == 0) {
            return Lists.newArrayList();
        }
        // 去重下
        List<TraceNodeInfoResult> results = TraceNodeInfoConvert.INSTANCE.ofListResult(entities);
        return results.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public Integer getNodeCount(String traceId, Long customId) {
        LambdaQueryWrapper<TraceNodeInfoEntity> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(traceId)) {
            wrapper.eq(TraceNodeInfoEntity::getTraceId,traceId);
        }
        wrapper.eq(TraceNodeInfoEntity::getCustomerId,customId);
        return customTraceNodeInfoMapper.count(wrapper);
    }

    @Override
    public Long getExceptionNodeCount(String traceId, Long customerId) {
        LambdaQueryWrapper<TraceNodeInfoEntity> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(traceId)) {
            wrapper.eq(TraceNodeInfoEntity::getTraceId,traceId);
        }
        wrapper.eq(TraceNodeInfoEntity::getCustomerId,customerId);
        List<TraceNodeInfoEntity> entities = customTraceNodeInfoMapper.list(wrapper);
        if(entities == null || entities.size() == 0) {
            return 0L;
        }
        List<TraceNodeInfoResult> results =TraceNodeInfoConvert.INSTANCE.ofListResult(entities);
        return results.stream().filter(result -> !ResultCode.isOk(result.getResultCode()))
            // 去重
            .map(TraceNodeInfoResult::toString).distinct()
            .count();
    }

    @Override
    public Long getUnknownNodeCount(String traceId, Long customerId) {
        List<TraceNodeInfoResult> results = getUnknownNodes(traceId, customerId);
        if(results == null || results.size() == 0) {
            return 0L;
        }
        // 去重
        return results.stream().map(TraceNodeInfoResult::toString).distinct().count();
    }


    @Override
    public List<TraceNodeInfoResult> getUnknownNodes(String traceId, Long customerId) {
        LambdaQueryWrapper<TraceNodeInfoEntity> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(traceId)) {
            wrapper.eq(TraceNodeInfoEntity::getTraceId, traceId);
        }
        wrapper.eq(TraceNodeInfoEntity::getCustomerId, customerId);
        wrapper.eq(TraceNodeInfoEntity::getIsUpperUnknownNode,true);
        wrapper.orderByDesc(TraceNodeInfoEntity::getId);
        List<TraceNodeInfoEntity> entities = customTraceNodeInfoMapper.list(wrapper);
        // 去重下
        List<TraceNodeInfoResult> results = TraceNodeInfoConvert.INSTANCE.ofListResult(entities);
        return results.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public TraceNodeInfoResult getById(Long id) {
        TraceNodeInfoEntity entity = customTraceNodeInfoMapper.getById(id);
        if(entity == null) {
            return  null;
        }
        return TraceNodeInfoConvert.INSTANCE.of(entity);
    }
}
