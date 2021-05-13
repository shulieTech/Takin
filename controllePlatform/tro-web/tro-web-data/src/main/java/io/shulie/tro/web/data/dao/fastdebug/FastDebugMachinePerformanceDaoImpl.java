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
import io.shulie.tro.web.data.convert.fastdebug.FastDebugMachinePerformanceEntityConvert;
import io.shulie.tro.web.data.convert.fastdebug.FastDebugMachinePerformanceResultConvert;
import io.shulie.tro.web.data.mapper.custom.fastdebug.FastDebugMachinePerformanceMapperBatch;
import io.shulie.tro.web.data.mapper.mysql.FastDebugMachinePerformanceMapper;
import io.shulie.tro.web.data.model.mysql.FastDebugMachinePerformanceEntity;
import io.shulie.tro.web.data.param.fastdebug.FastDebugMachinePerformanceCreateParam;
import io.shulie.tro.web.data.result.fastdebug.FastDebugMachinePerformanceResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @Author: mubai
 * @Date: 2020-12-28 11:31
 * @Description:
 */

@Repository
public class FastDebugMachinePerformanceDaoImpl implements FastDebugMachinePerformanceDao {

    @Autowired
    private FastDebugMachinePerformanceMapper fastDebugMachinePerformanceMapper;

    @Autowired
    private FastDebugMachinePerformanceMapperBatch fastDebugMachinePerformanceMapperBatch;

    @Override
    public void insert(FastDebugMachinePerformanceCreateParam createParam) {
        if (createParam == null) {
            return;
        }
        FastDebugMachinePerformanceEntity entity = new FastDebugMachinePerformanceEntity();
        BeanUtils.copyProperties(createParam, entity);
        fastDebugMachinePerformanceMapper.insert(entity);
    }

    @Override
    public void insert(List<FastDebugMachinePerformanceCreateParam> createParamList) {
        if (CollectionUtils.isEmpty(createParamList)) {
            return;
        }
        List<FastDebugMachinePerformanceEntity> ofs = FastDebugMachinePerformanceEntityConvert.INSTANCE.ofs(
            createParamList);
        fastDebugMachinePerformanceMapperBatch.saveBatch(ofs);
    }

    @Override
    public List<FastDebugMachinePerformanceResult> getByRpcId(String traceId,String rpcId,Integer logType) {
        LambdaQueryWrapper<FastDebugMachinePerformanceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FastDebugMachinePerformanceEntity::getRpcId, rpcId);
        wrapper.eq(FastDebugMachinePerformanceEntity::getTraceId,traceId);
        if(logType != null) {
            wrapper.eq(FastDebugMachinePerformanceEntity::getLogType,logType);
        }
        List<FastDebugMachinePerformanceEntity> entityList = fastDebugMachinePerformanceMapper
            .selectList(wrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return Lists.newArrayList();
        }
        return FastDebugMachinePerformanceResultConvert.INSTANCE.ofs(entityList);

    }

    @Override
    public void clearHistoryData(Date beforeDate) {
        if (null == beforeDate){
            return;
        }
        LambdaQueryWrapper<FastDebugMachinePerformanceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.lt(FastDebugMachinePerformanceEntity :: getGmtCreate,beforeDate);
        fastDebugMachinePerformanceMapper.delete(wrapper);
    }

}
