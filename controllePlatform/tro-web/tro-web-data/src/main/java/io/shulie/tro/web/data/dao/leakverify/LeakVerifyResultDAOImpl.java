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

package io.shulie.tro.web.data.dao.leakverify;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.pamirs.tro.common.constant.VerifyResultStatusEnum;
import io.shulie.tro.web.data.mapper.mysql.LeakVerifyDetailMapper;
import io.shulie.tro.web.data.mapper.mysql.LeakVerifyResultMapper;
import io.shulie.tro.web.data.model.mysql.LeakVerifyDetailEntity;
import io.shulie.tro.web.data.model.mysql.LeakVerifyResultEntity;
import io.shulie.tro.web.data.param.leakverify.LeakVerifyResultCreateParam;
import io.shulie.tro.web.data.param.leakverify.LeakVerifyResultQueryParam;
import io.shulie.tro.web.data.result.leakverify.LeakVerifyResultResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2021/1/5 8:24 下午
 * @Description:
 */
@Component
public class LeakVerifyResultDAOImpl implements LeakVerifyResultDAO {

    @Autowired
    private LeakVerifyResultMapper leakVerifyResultMapper;

    @Autowired
    private LeakVerifyDetailMapper leakVerifyDetailMapper;

    @Override
    public Long insert(LeakVerifyResultCreateParam createParam) {
        LeakVerifyResultEntity resultEntity = new LeakVerifyResultEntity();
        BeanUtils.copyProperties(createParam, resultEntity);
        leakVerifyResultMapper.insert(resultEntity);
        return resultEntity.getId();
    }

    @Override
    public List<LeakVerifyResultResult> selectList(LeakVerifyResultQueryParam queryParam) {
        List<LeakVerifyResultResult> resultLists = Lists.newArrayList();
        LambdaQueryWrapper<LeakVerifyResultEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (!Objects.isNull(queryParam.getRefType())) {
            queryWrapper.eq(LeakVerifyResultEntity::getRefType, queryParam.getRefType());
        }
        if (!Objects.isNull(queryParam.getRefId())) {
            queryWrapper.eq(LeakVerifyResultEntity::getRefId, queryParam.getRefId());
        }
        if (!Objects.isNull(queryParam.getReportId())) {
            queryWrapper.eq(LeakVerifyResultEntity::getReportId, queryParam.getReportId());
        }
        queryWrapper.eq(LeakVerifyResultEntity::getIsDeleted, 0);
        queryWrapper.select(LeakVerifyResultEntity::getId,
            LeakVerifyResultEntity::getRefType,
            LeakVerifyResultEntity::getRefId,
            LeakVerifyResultEntity::getDbresourceId,
            LeakVerifyResultEntity::getDbresourceName,
            LeakVerifyResultEntity::getDbresourceUrl,
            LeakVerifyResultEntity::getCustomerId,
            LeakVerifyResultEntity::getUserId,
            LeakVerifyResultEntity::getUpdateTime);
        List<LeakVerifyResultEntity> entityList = leakVerifyResultMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(entityList)) {
            resultLists = entityList.stream().map(entity -> {
                LeakVerifyResultResult result = new LeakVerifyResultResult();
                BeanUtils.copyProperties(entity, result);
                return result;
            }).collect(Collectors.toList());
        }
        return resultLists;
    }

    @Override
    public Boolean querySceneIsLeaked(Long reportId) {
        if (!Objects.isNull(reportId)) {
            LambdaQueryWrapper<LeakVerifyResultEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(LeakVerifyResultEntity::getReportId, reportId);
            queryWrapper.select(LeakVerifyResultEntity::getId);
            List<LeakVerifyResultEntity> entityList = leakVerifyResultMapper.selectList(queryWrapper);
            if (CollectionUtils.isNotEmpty(entityList)) {
                List<Long> resultIdList = entityList.stream().map(LeakVerifyResultEntity::getId).collect(
                    Collectors.toList());
                LambdaQueryWrapper<LeakVerifyDetailEntity> queryDetailWrapper = new LambdaQueryWrapper<>();
                queryDetailWrapper.select(LeakVerifyDetailEntity::getLeakSql,
                    LeakVerifyDetailEntity::getStatus);
                queryDetailWrapper.in(LeakVerifyDetailEntity::getResultId, resultIdList);
                List<LeakVerifyDetailEntity> detailEntityList = leakVerifyDetailMapper.selectList(queryDetailWrapper);
                List<Integer> statusList = detailEntityList.stream().map(LeakVerifyDetailEntity::getStatus).distinct()
                    .collect(
                        Collectors.toList());
                if (statusList.contains(VerifyResultStatusEnum.LEAKED.getCode())) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }
}
