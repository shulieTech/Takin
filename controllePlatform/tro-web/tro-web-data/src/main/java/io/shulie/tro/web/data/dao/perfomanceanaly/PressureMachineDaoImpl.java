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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.data.mapper.mysql.PressureMachineMapper;
import io.shulie.tro.web.data.model.mysql.PressureMachineEntity;
import io.shulie.tro.web.data.param.machine.PressureMachineDeleteParam;
import io.shulie.tro.web.data.param.machine.PressureMachineInsertParam;
import io.shulie.tro.web.data.param.machine.PressureMachineQueryParam;
import io.shulie.tro.web.data.param.machine.PressureMachineUpdateParam;
import io.shulie.tro.web.data.result.perfomanceanaly.PressureMachineResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: mubai
 * @Date: 2020-11-12 21:00
 * @Description:
 */

@Component
public class PressureMachineDaoImpl implements PressureMachineDao {

    @Resource
    private PressureMachineMapper pressureMachineMapper;

    @Override
    public Integer insert(PressureMachineInsertParam param) {
        PressureMachineEntity entity = new PressureMachineEntity();
        BeanUtils.copyProperties(param, entity);
        return pressureMachineMapper.insert(entity);
    }

    @Override
    public Integer getCountByIp(String ip) {
        LambdaQueryWrapper<PressureMachineEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PressureMachineEntity::getIp, ip);
        return pressureMachineMapper.selectCount(wrapper);
    }

    @Override
    public void update(PressureMachineUpdateParam param) {
        PressureMachineEntity entity = new PressureMachineEntity();
        BeanUtils.copyProperties(param, entity);
        pressureMachineMapper.updateById(entity);
    }

    @Override
    public void delete(PressureMachineDeleteParam param) {
        pressureMachineMapper.deleteById(param.getId());
    }

    @Override
    public PressureMachineResult getById(Long id) {
        PressureMachineEntity entity = pressureMachineMapper.selectById(id);
        PressureMachineResult result = new PressureMachineResult();
        BeanUtils.copyProperties(entity, result);
        return result;
    }

    @Override
    public PressureMachineResult getByIp(String ip) {
        PressureMachineEntity entity = pressureMachineMapper.getByIp(ip);
        if (entity == null) {
            return null;
        }
        PressureMachineResult result = new PressureMachineResult();
        BeanUtils.copyProperties(entity, result);
        return result;

    }

    @Override
    public PagingList<PressureMachineResult> queryByExample(PressureMachineQueryParam queryParam) {
        LambdaQueryWrapper<PressureMachineEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
            PressureMachineEntity::getId,
            PressureMachineEntity::getName,
            PressureMachineEntity::getIp,
            PressureMachineEntity::getCpu,
            PressureMachineEntity::getMemory,
            PressureMachineEntity::getMemoryUsed,
            PressureMachineEntity::getCpuLoad,
            PressureMachineEntity::getCpuUsage,
            PressureMachineEntity::getDiskIoWait,
            PressureMachineEntity::getDisk,
            PressureMachineEntity::getFlag,
            PressureMachineEntity::getGmtCreate,
            PressureMachineEntity::getGmtUpdate,
            PressureMachineEntity::getTransmittedTotal,
            PressureMachineEntity::getTransmittedIn,
            PressureMachineEntity::getTransmittedOut,
            PressureMachineEntity::getTransmittedUsage,
            PressureMachineEntity::getStatus,
            PressureMachineEntity::getMachineUsage,
            PressureMachineEntity::getSceneNames

        );
        if (queryParam.getId() != null) {
            wrapper.eq(PressureMachineEntity::getId, queryParam.getId());
        }
        if (queryParam.getFlag() != null) {
            wrapper.like(PressureMachineEntity::getFlag, queryParam.getFlag());
        }
        if (queryParam.getIp() != null) {
            wrapper.like(PressureMachineEntity::getIp, queryParam.getIp());
        }
        if (queryParam.getName() != null) {
            wrapper.like(PressureMachineEntity::getName, queryParam.getName());
        }
        if (queryParam.getStatus() != null) {
            wrapper.eq(PressureMachineEntity::getStatus, queryParam.getStatus());
        }
        if (queryParam.getMachineUsageOrder() != null) {
            Integer order = queryParam.getMachineUsageOrder();
            if (order == 1) {
                wrapper.orderByAsc(PressureMachineEntity::getMachineUsage);
            } else if (order == -1) {
                wrapper.orderByDesc(PressureMachineEntity::getMachineUsage);
            }
        } else {
            wrapper.orderByDesc(PressureMachineEntity::getGmtCreate);
        }

        Page<PressureMachineEntity> page = new Page<>(queryParam.getCurrent(), queryParam.getPageSize());
        Page<PressureMachineEntity> pressureMachineEntityPage = pressureMachineMapper.selectPage(page, wrapper);
        if (CollectionUtils.isEmpty(pressureMachineEntityPage.getRecords())) {
            return PagingList.empty();
        }
        List<PressureMachineResult> pressureMachineResultList = pressureMachineEntityPage.getRecords().stream().map(
            pressureMachineEntity -> {
                PressureMachineResult result = new PressureMachineResult();
                BeanUtils.copyProperties(pressureMachineEntity, result);
                return result;
            }).collect(Collectors.toList());

        return PagingList.of(pressureMachineResultList, pressureMachineEntityPage.getTotal());
    }
}
