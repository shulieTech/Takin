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

package io.shulie.tro.web.data.dao.log;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.data.mapper.mysql.OperationLogMapper;
import io.shulie.tro.web.data.model.mysql.OperationLogEntity;
import io.shulie.tro.web.data.param.log.OperationLogCreateParam;
import io.shulie.tro.web.data.param.log.OperationLogQueryParam;
import io.shulie.tro.web.data.result.log.OperationLogResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/9/24 4:00 下午
 * @Description:
 */
@Component
public class OperationLogDAOImpl implements OperationLogDAO {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    public PagingList<OperationLogResult> selectPage(OperationLogQueryParam param) {

        LambdaQueryWrapper<OperationLogEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
            OperationLogEntity::getId,
            OperationLogEntity::getModule,
            OperationLogEntity::getSubModule,
            OperationLogEntity::getType,
            OperationLogEntity::getStatus,
            OperationLogEntity::getContent,
            OperationLogEntity::getUserName,
            OperationLogEntity::getStartTime);
        if (StringUtils.isNotBlank(param.getStartTime())) {
            wrapper.ge(OperationLogEntity::getStartTime, param.getStartTime());
        }
        if (StringUtils.isNotBlank(param.getEndTime())) {
            wrapper.le(OperationLogEntity::getEndTime, param.getEndTime());
        }
        if (StringUtils.isNotBlank(param.getUserName())) {
            wrapper.like(OperationLogEntity::getUserName, param.getUserName());
        }
        if (StringUtils.isNotBlank(param.getModule())) {
            wrapper.eq(OperationLogEntity::getModule, param.getModule());
        }
        if (StringUtils.isNotBlank(param.getSubModule())) {
            wrapper.eq(OperationLogEntity::getSubModule, param.getSubModule());
        }
        Page<OperationLogEntity> page = new Page<>(param.getCurrent(), param.getPageSize());
        wrapper.orderByDesc(OperationLogEntity::getStartTime);

        IPage<OperationLogEntity> logs = operationLogMapper.selectPage(page, wrapper);
        if (CollectionUtils.isEmpty(logs.getRecords())) {
            return PagingList.empty();
        }
        List<OperationLogResult> operationLogResultList = logs.getRecords().stream().map(entity -> {
            OperationLogResult logResult = new OperationLogResult();
            BeanUtils.copyProperties(entity, logResult);
            return logResult;
        }).collect(Collectors.toList());

        return PagingList.of(operationLogResultList, page.getTotal());
    }

    @Override
    public int insert(OperationLogCreateParam createParam) {
        OperationLogEntity entity = new OperationLogEntity();
        BeanUtils.copyProperties(createParam, entity);
        return operationLogMapper.insert(entity);
    }
}
