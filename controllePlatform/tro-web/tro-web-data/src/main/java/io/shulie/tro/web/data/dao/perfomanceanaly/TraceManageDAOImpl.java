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

package io.shulie.tro.web.data.dao.perfomanceanaly;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.shulie.tro.web.common.enums.TraceManageStatusEnum;
import io.shulie.tro.web.data.convert.performance.TraceManageResultConvertor;
import io.shulie.tro.web.data.mapper.mysql.TraceManageDeployMapper;
import io.shulie.tro.web.data.mapper.mysql.TraceManageMapper;
import io.shulie.tro.web.data.model.mysql.TraceManageDeployEntity;
import io.shulie.tro.web.data.model.mysql.TraceManageEntity;
import io.shulie.tro.web.data.param.tracemanage.TraceManageCreateParam;
import io.shulie.tro.web.data.param.tracemanage.TraceManageDeployCreateParam;
import io.shulie.tro.web.data.param.tracemanage.TraceManageDeployUpdateParam;
import io.shulie.tro.web.data.param.tracemanage.TraceManageQueryParam;
import io.shulie.tro.web.data.result.tracemanage.TraceManageDeployResult;
import io.shulie.tro.web.data.result.tracemanage.TraceManageResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhaoyong
 */
@Component
public class TraceManageDAOImpl implements TraceManageDAO{

    @Autowired
    private TraceManageMapper traceManageMapper;

    @Autowired
    private TraceManageDeployMapper traceManageDeployMapper;


    @Override
    public void updateTraceManageDeploy(TraceManageDeployUpdateParam updateParam) {
        if (updateParam == null || updateParam.getId() == null){
            return;
        }
        TraceManageDeployEntity traceManageDeployEntity = new TraceManageDeployEntity();
        BeanUtils.copyProperties(updateParam,traceManageDeployEntity);
        traceManageDeployMapper.updateById(traceManageDeployEntity);
    }

    @Override
    public TraceManageDeployResult queryTraceManageDeployById(Long traceManageDeployId) {
        TraceManageDeployEntity traceManageDeployEntity = traceManageDeployMapper.selectById(traceManageDeployId);
        return getTraceManageDeployResult(traceManageDeployEntity);
    }

    private TraceManageDeployResult getTraceManageDeployResult(TraceManageDeployEntity traceManageDeployEntity) {
        if (traceManageDeployEntity == null){
            return null;
        }
        return TraceManageResultConvertor.INSTANCE.ofTraceManageDeployResult(traceManageDeployEntity);
    }

    @Override
    public Long createTraceManageAndDeploy(TraceManageCreateParam traceManageCreateParam,String sampleId) {
        TraceManageEntity traceManageEntity = new TraceManageEntity();
        BeanUtils.copyProperties(traceManageCreateParam,traceManageEntity);
        traceManageMapper.insert(traceManageEntity);

        TraceManageDeployEntity traceManageDeployEntity = new TraceManageDeployEntity();
        traceManageDeployEntity.setTraceManageId(traceManageEntity.getId());
        traceManageDeployEntity.setTraceDeployObject(traceManageEntity.getTraceObject());
        traceManageDeployEntity.setLevel(1);
        traceManageDeployEntity.setParentId(0L);
        traceManageDeployEntity.setHasChildren(2);
        traceManageDeployEntity.setStatus(TraceManageStatusEnum.TRACE_RUNNING.getCode());
        traceManageDeployEntity.setSampleId(sampleId);
        traceManageDeployMapper.insert(traceManageDeployEntity);
        return traceManageEntity.getId();
    }

    @Override
    public TraceManageResult queryTraceManageById(Long traceManageId) {
        TraceManageEntity traceManageEntity = traceManageMapper.selectById(traceManageId);
        if (traceManageEntity != null){
            TraceManageResult traceManageResult = TraceManageResultConvertor.INSTANCE.ofTraceManageResult(traceManageEntity);
            TraceManageDeployResult traceManageDeployResult = queryTraceManageDeployByTraceManageId(traceManageEntity.getId());
            traceManageResult.setTraceManageDeployResult(traceManageDeployResult);
            return traceManageResult;
        }
        return null;
    }

    /**
     * 获取最新的一条数据，如果后面要展示一个方法的多个追踪信息，可以展开
     * @param traceManageId
     * @return
     */
    private TraceManageDeployResult queryTraceManageDeployByTraceManageId(Long traceManageId){
        LambdaQueryWrapper<TraceManageDeployEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TraceManageDeployEntity::getTraceManageId,traceManageId);
        wrapper.eq(TraceManageDeployEntity::getLevel,1);
        List<TraceManageDeployEntity> traceManageDeployEntities = traceManageDeployMapper.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(traceManageDeployEntities)){
            //层级为1的方法追踪实例只会有一个
            TraceManageDeployEntity traceManageDeployEntity = traceManageDeployEntities.get(0);
            TraceManageDeployResult traceManageDeployResult = getTraceManageDeployResult(traceManageDeployEntity);
            setChildrenTraceManageDeploy(traceManageDeployResult);
            return traceManageDeployResult;
        }
        return null;
    }


    private void setChildrenTraceManageDeploy(TraceManageDeployResult traceManageDeployResult){
        if (traceManageDeployResult.getHasChildren() == 1){
            LambdaQueryWrapper<TraceManageDeployEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TraceManageDeployEntity::getParentId,traceManageDeployResult.getId());
            List<TraceManageDeployEntity> traceManageDeployEntities = traceManageDeployMapper.selectList(wrapper);
            if (CollectionUtils.isNotEmpty(traceManageDeployEntities)){
                List<TraceManageDeployResult> traceManageDeployResults = getTraceManageDeployResults(traceManageDeployEntities);
                traceManageDeployResult.setChildren(traceManageDeployResults);
                for (TraceManageDeployResult child : traceManageDeployResults){
                    setChildrenTraceManageDeploy(child);
                }
            }
        }

    }

    private List<TraceManageDeployResult> getTraceManageDeployResults(List<TraceManageDeployEntity> traceManageDeployEntities) {
        return traceManageDeployEntities.stream().map(this::getTraceManageDeployResult).collect(Collectors.toList());
    }

    @Override
    public TraceManageDeployResult queryTraceManageDeployBySampleId(String sampleId) {
        LambdaQueryWrapper<TraceManageDeployEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TraceManageDeployEntity::getSampleId,sampleId);
        TraceManageDeployEntity traceManageDeployEntity = traceManageDeployMapper.selectOne(wrapper);
        return getTraceManageDeployResult(traceManageDeployEntity);
    }

    @Override
    public void createTraceManageDeploy(List<TraceManageDeployCreateParam> traceManageDeployCreateParams) {
        traceManageDeployCreateParams.forEach(traceManageDeployCreateParam -> {
            LambdaQueryWrapper<TraceManageDeployEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(
                    TraceManageDeployEntity::getId,
                    TraceManageDeployEntity::getParentId,
                    TraceManageDeployEntity::getSampleId,
                    TraceManageDeployEntity::getStatus,
                    TraceManageDeployEntity::getTraceDeployObject,
                    TraceManageDeployEntity::getTraceManageId
                    );
            wrapper.eq(TraceManageDeployEntity::getParentId,traceManageDeployCreateParam.getParentId());
            wrapper.eq(TraceManageDeployEntity::getTraceDeployObject,traceManageDeployCreateParam.getTraceDeployObject());
            List<TraceManageDeployEntity> traceManageDeployEntities = traceManageDeployMapper.selectList(wrapper);
            if (CollectionUtils.isNotEmpty(traceManageDeployEntities)){
                return;
            }
            TraceManageDeployEntity traceManageDeployEntity = new TraceManageDeployEntity();
            BeanUtils.copyProperties(traceManageDeployCreateParam,traceManageDeployEntity);
            traceManageDeployMapper.insert(traceManageDeployEntity);
        });
    }

    @Override
    public List<TraceManageResult> queryTraceManageList(TraceManageQueryParam traceManageQueryParam) {
        LambdaQueryWrapper<TraceManageEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
                TraceManageEntity::getId,
                TraceManageEntity::getAgentId,
                TraceManageEntity::getCreateTime,
                TraceManageEntity::getFeature,
                TraceManageEntity::getProcessId,
                TraceManageEntity::getReportId,
                TraceManageEntity::getServerIp,
                TraceManageEntity::getTraceObject,
                TraceManageEntity::getUpdateTime);
        if (traceManageQueryParam != null){
            if (!StringUtils.isBlank(traceManageQueryParam.getAgentId())){
                wrapper.eq(TraceManageEntity::getAgentId,traceManageQueryParam.getAgentId());
            }
            if (traceManageQueryParam.getReportId() != null){
                wrapper.eq(TraceManageEntity::getReportId,traceManageQueryParam.getReportId());
            }
            if (!StringUtils.isBlank(traceManageQueryParam.getServerIp())){
                wrapper.eq(TraceManageEntity::getServerIp,traceManageQueryParam.getServerIp());
            }
            if (traceManageQueryParam.getProcessId() != null){
                wrapper.eq(TraceManageEntity::getProcessId,traceManageQueryParam.getProcessId());
            }
        }
        List<TraceManageEntity> traceManageEntities = traceManageMapper.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(traceManageEntities)){
            return traceManageEntities.stream().map(TraceManageResultConvertor.INSTANCE::ofTraceManageResult).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public int countTraceIngManageDeployByAgentId(String agentId) {
        LambdaQueryWrapper<TraceManageDeployEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(TraceManageDeployEntity::getId,
                TraceManageDeployEntity::getTraceManageId);
        wrapper.eq(TraceManageDeployEntity::getStatus, 1);
        List<TraceManageDeployEntity> traceManageDeployEntities = traceManageDeployMapper.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(traceManageDeployEntities)){
            List<Long> traceManageIds = traceManageDeployEntities.stream().map(TraceManageDeployEntity::getTraceManageId).collect(Collectors.toList());
            List<TraceManageEntity> traceManageEntities = traceManageMapper.selectBatchIds(traceManageIds);
            if (CollectionUtils.isNotEmpty(traceManageEntities)){
                Set<String> agentIds = traceManageEntities.stream().map(TraceManageEntity::getAgentId).collect(Collectors.toSet());
                if (CollectionUtils.isNotEmpty(agentIds)){
                    return agentIds.size();
                }
            }
        }
        return 0;
    }

    @Override
    public void updateTraceManageDeployStatus(TraceManageDeployUpdateParam updateParam, Integer currentStatus) {
        if (updateParam == null || updateParam.getId() == null || currentStatus == null){
            return;
        }
        TraceManageDeployEntity traceManageDeployEntity = new TraceManageDeployEntity();
        BeanUtils.copyProperties(updateParam,traceManageDeployEntity);
        LambdaUpdateWrapper<TraceManageDeployEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(TraceManageDeployEntity::getStatus,currentStatus);
        traceManageDeployMapper.update(traceManageDeployEntity,updateWrapper);
    }

    @Override
    public List<TraceManageDeployResult> queryTraceManageDeployByStatus(Integer status) {
        LambdaQueryWrapper<TraceManageDeployEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TraceManageDeployEntity::getStatus,status);
        List<TraceManageDeployEntity> traceManageDeployEntities = traceManageDeployMapper.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(traceManageDeployEntities)){
            return traceManageDeployEntities.stream().map(this::getTraceManageDeployResult).collect(Collectors.toList());
        }
        return null;
    }
}
