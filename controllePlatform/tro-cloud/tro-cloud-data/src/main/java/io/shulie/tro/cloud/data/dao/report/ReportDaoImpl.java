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

package io.shulie.tro.cloud.data.dao.report;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import io.shulie.tro.cloud.common.constants.ReportConstans;
import io.shulie.tro.cloud.data.mapper.mysql.ReportMapper;
import io.shulie.tro.cloud.data.model.mysql.ReportEntity;
import io.shulie.tro.cloud.data.param.report.ReportDataQueryParam;
import io.shulie.tro.cloud.data.param.report.ReportUpdateConclusionParam;
import io.shulie.tro.cloud.data.param.report.ReportUpdateParam;
import io.shulie.tro.cloud.data.result.report.ReportResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 无涯
 * @Package io.shulie.tro.cloud.data.dao.report
 * @date 2020/12/17 3:31 下午
 */
@Service
public class ReportDaoImpl implements ReportDao {
    @Autowired
    private ReportMapper reportMapper;

    @Override
    public List<ReportResult> getList(ReportDataQueryParam param) {
        LambdaQueryWrapper<ReportEntity> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(param.getEndTime())) {
            wrapper.le(ReportEntity::getGmtCreate, param.getEndTime());
        }
        List<ReportEntity> entities = reportMapper.selectList(wrapper);
        if (entities != null && entities.size() > 0) {
            List<ReportResult> results = entities.stream().map(entity -> {
                ReportResult reportResult = new ReportResult();
                BeanUtils.copyProperties(entity, reportResult);
                return reportResult;
            }).collect(Collectors.toList());
            return results;
        }
        return Lists.newArrayList();
    }

    @Override
    public ReportResult selectById(Long id) {
        ReportEntity entity = reportMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        ReportResult reportResult = new ReportResult();
        BeanUtils.copyProperties(entity, reportResult);
        return reportResult;
    }

    @Override
    public void updateReportConclusion(ReportUpdateConclusionParam param) {
        ReportEntity entity = new ReportEntity();
        BeanUtils.copyProperties(param, entity);
        reportMapper.updateById(entity);
    }

    @Override
    public void updateReport(ReportUpdateParam param) {
        ReportEntity entity = new ReportEntity();
        BeanUtils.copyProperties(param, entity);
        reportMapper.updateById(entity);
    }

    @Override
    public void finishReport(Long reportId) {
        ReportEntity entity = new ReportEntity();
        entity.setId(reportId);
        entity.setStatus(ReportConstans.FINISH_STATUS);
        entity.setGmtUpdate(new Date());
        reportMapper.updateById(entity);
    }

    @Override
    public void updateReportLock(Long resultId, Integer lock) {
        ReportEntity entity = new ReportEntity();
        entity.setId(resultId);
        entity.setLock(lock);
        entity.setGmtUpdate(new Date());
        reportMapper.updateById(entity);
    }



    @Override
    public ReportResult getTempReportBySceneId(Long sceneId) {
        LambdaQueryWrapper<ReportEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReportEntity::getSceneId, sceneId);
        wrapper.eq(ReportEntity::getIsDeleted, 0);
        wrapper.orderByDesc(ReportEntity::getId);
        wrapper.last("limit 1");
        List<ReportEntity> entities = reportMapper.selectList(wrapper);
        if (entities != null && entities.size() > 0) {
            ReportResult reportResult = new ReportResult();
            BeanUtils.copyProperties(entities.get(0), reportResult);
            return reportResult;
        }
        return null;
    }

    @Override
    public ReportResult getReportBySceneId(Long sceneId) {
        LambdaQueryWrapper<ReportEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReportEntity::getSceneId, sceneId);
        // 根据状态
        wrapper.eq(ReportEntity::getStatus, 0);
        wrapper.eq(ReportEntity::getIsDeleted, 0);
        wrapper.orderByDesc(ReportEntity::getId);
        wrapper.last("limit 1");
        List<ReportEntity> entities = reportMapper.selectList(wrapper);
        if (entities != null && entities.size() > 0) {
            ReportResult reportResult = new ReportResult();
            BeanUtils.copyProperties(entities.get(0), reportResult);
            return reportResult;
        }
        return null;
    }
}
