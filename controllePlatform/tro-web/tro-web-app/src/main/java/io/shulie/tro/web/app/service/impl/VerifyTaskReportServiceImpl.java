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

package io.shulie.tro.web.app.service.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pamirs.tro.common.constant.VerifyResultStatusEnum;
import io.shulie.tro.web.app.request.leakverify.LeakVerifyTaskReportQueryRequest;
import io.shulie.tro.web.app.response.leakverify.LeakVerifyDetailResponse;
import io.shulie.tro.web.app.response.leakverify.LeakVerifyDsResultResponse;
import io.shulie.tro.web.app.response.leakverify.LeakVerifyTaskResultResponse;
import io.shulie.tro.web.app.service.VerifyTaskReportService;
import io.shulie.tro.web.common.vo.component.SelectVO;
import io.shulie.tro.web.data.dao.leakverify.LeakVerifyDetailDAO;
import io.shulie.tro.web.data.dao.leakverify.LeakVerifyResultDAO;
import io.shulie.tro.web.data.param.leakverify.LeakVerifyDetailQueryParam;
import io.shulie.tro.web.data.param.leakverify.LeakVerifyResultQueryParam;
import io.shulie.tro.web.data.result.leakverify.LeakVerifyDetailResult;
import io.shulie.tro.web.data.result.leakverify.LeakVerifyResultResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2021/1/6 5:38 下午
 * @Description:
 */
@Slf4j
@Component
public class VerifyTaskReportServiceImpl implements VerifyTaskReportService {

    @Autowired
    private LeakVerifyResultDAO resultDAO;

    @Autowired
    private LeakVerifyDetailDAO detailDAO;

    @Override
    public LeakVerifyTaskResultResponse getVerifyTaskReport(LeakVerifyTaskReportQueryRequest queryRequest) {
        LeakVerifyResultQueryParam resultQueryParam = new LeakVerifyResultQueryParam();
        resultQueryParam.setReportId(queryRequest.getReportId());
        List<LeakVerifyResultResult> resultList = resultDAO.selectList(resultQueryParam);
        if (CollectionUtils.isEmpty(resultList)) {
            return null;
        }
        LeakVerifyTaskResultResponse taskResultResponse = new LeakVerifyTaskResultResponse();
        List<LeakVerifyDsResultResponse> dsResultResponseList = Lists.newArrayList();
        taskResultResponse.setStatus(VerifyResultStatusEnum.UNCHECK.getCode());
        taskResultResponse.setRefType(resultList.get(0).getRefType());
        taskResultResponse.setRefId(resultList.get(0).getRefId());
        List<Long> resultIdList = resultList.stream().map(LeakVerifyResultResult::getId).collect(
            Collectors.toList());
        LeakVerifyDetailQueryParam detailQueryParam = new LeakVerifyDetailQueryParam();
        detailQueryParam.setResultIdList(resultIdList);
        List<LeakVerifyDetailResult> detailResultList = detailDAO.selectList(detailQueryParam);
        //聚合漏数验证结果
        // 1、数据源去重
        Map<Long, LeakVerifyDsResultResponse> dsResultResponseMap = Maps.newHashMap();
        resultList.forEach(result -> {
            LeakVerifyDsResultResponse dsResultResponse = new LeakVerifyDsResultResponse();
            dsResultResponse.setDatasourceId(result.getDbresourceId());
            dsResultResponse.setDatasourceName(result.getDbresourceName());
            dsResultResponse.setJdbcUrl(result.getDbresourceUrl());
            dsResultResponse.setStatus(VerifyResultStatusEnum.UNCHECK.getCode());
            dsResultResponseMap.putIfAbsent(result.getDbresourceId(), dsResultResponse);
        });

        //2、根据数据源分组，查询每个数据源下结果id的集合
        Map<Long, Set<Long>> resultIdMap = new HashMap<>();
        for (LeakVerifyResultResult leakVerifyResultResult : resultList) {
            Long id = leakVerifyResultResult.getId();
            resultIdMap.computeIfAbsent(leakVerifyResultResult.getDbresourceId(), k -> new HashSet<>()).add(id);
        }

        //3、根据数据源分组结果，组装漏数详情
        for (Map.Entry<Long, Set<Long>> entry : resultIdMap.entrySet()) {
            Long datasourceId = entry.getKey();
            Set<Long> resultIdSet = entry.getValue();
            //当前数据源下所有漏数sql结果
            List<LeakVerifyDetailResult> currentDsDetailResultList = detailResultList.stream().filter(
                leakVerifyDetailResult -> resultIdSet.contains(leakVerifyDetailResult.getResultId())
            ).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(currentDsDetailResultList)) {
                continue;
            }
            //4、根据sql进行分组，组内返回警告级别最高的sql，漏数>检测失败>未验证>正常
            Map<String, LeakVerifyDetailResult> detailResultMap = currentDsDetailResultList.stream().collect(
                Collectors
                    .toMap(LeakVerifyDetailResult::getLeakSql, Function.identity(),
                        BinaryOperator.maxBy(
                            Comparator.comparing(r -> VerifyResultStatusEnum.getWarningLevelByCode(r.getStatus()))))
            );
            //5、将分组结果转换成返回对象
            List<LeakVerifyDetailResponse> detailResponseList = Lists.newArrayList();
            for (Map.Entry<String, LeakVerifyDetailResult> detailResultEntry : detailResultMap.entrySet()) {
                String sql = detailResultEntry.getKey();
                Integer sqlStatus = detailResultEntry.getValue().getStatus();
                LeakVerifyDetailResponse detailResponse = new LeakVerifyDetailResponse();
                detailResponse.setOrder(detailResponseList.size() + 1);
                detailResponse.setSql(sql);
                //sql漏数的漏数状态
                detailResponse.setStatus(sqlStatus);
                detailResponse.setWarningLevel(VerifyResultStatusEnum.getWarningLevelByCode(sqlStatus));
                //给前端使用的状态对象
                SelectVO vo = new SelectVO();
                vo.setValue(String.valueOf(sqlStatus));
                vo.setLabel(VerifyResultStatusEnum.getLabelByCode(sqlStatus));
                detailResponse.setStatusResponse(vo);
                detailResponseList.add(detailResponse);
            }
            detailResponseList = detailResponseList.stream().sorted(
                Comparator.comparing(LeakVerifyDetailResponse::getWarningLevel).reversed()).collect(
                Collectors.toList());

            //6、聚合数据源的漏数状态
            Integer dsStatus = detailResponseList.stream()
                .map(LeakVerifyDetailResponse::getStatus).map(VerifyResultStatusEnum::getWarningLevelByCode).filter(
                    Objects::nonNull).max(Integer::compareTo).map(VerifyResultStatusEnum::getCodeByWarningLevel)
                .get();

            LeakVerifyDsResultResponse dsResultResponse = dsResultResponseMap.get(datasourceId);
            //设置数据源的漏数状态
            dsResultResponse.setStatus(dsStatus);
            dsResultResponse.setWarningLevel(VerifyResultStatusEnum.getWarningLevelByCode(dsStatus));

            //给前端使用的状态对象
            SelectVO vo = new SelectVO();
            vo.setValue(dsResultResponse.getStatus().toString());
            vo.setLabel(VerifyResultStatusEnum.getLabelByCode(dsResultResponse.getStatus()));
            dsResultResponse.setStatusResponse(vo);
            dsResultResponse.setDetailResponseList(detailResponseList);
            dsResultResponseList.add(dsResultResponse);
        }

        dsResultResponseList = dsResultResponseList.stream().sorted(
            Comparator.comparing(LeakVerifyDsResultResponse::getWarningLevel).reversed()).collect(
            Collectors.toList());

        //7、聚合压测场景的漏数状态
        Integer taskStatus = dsResultResponseList.stream().map(LeakVerifyDsResultResponse::getStatus).map(
            VerifyResultStatusEnum::getWarningLevelByCode).filter(Objects::nonNull).max(Integer::compareTo).map(
            VerifyResultStatusEnum::getCodeByWarningLevel).get();
        taskResultResponse.setStatus(taskStatus);
        //给前端使用的状态对象
        SelectVO vo = new SelectVO();
        vo.setValue(taskStatus.toString());
        vo.setLabel(VerifyResultStatusEnum.getLabelByCode(taskStatus));
        taskResultResponse.setStatusResponse(vo);
        taskResultResponse.setDsResultResponseList(dsResultResponseList);
        return taskResultResponse;
    }
}
