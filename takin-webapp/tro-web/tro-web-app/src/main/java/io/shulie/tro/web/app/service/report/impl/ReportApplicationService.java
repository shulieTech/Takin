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

package io.shulie.tro.web.app.service.report.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pamirs.tro.entity.dao.confcenter.TApplicationMntDao;
import com.pamirs.tro.entity.domain.dto.report.ReportApplicationDTO;
import com.pamirs.tro.entity.domain.dto.report.ReportDetailDTO;
import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import io.shulie.tro.web.app.service.report.ReportService;
import io.shulie.tro.web.common.domain.WebResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName ReportApplicationService
 * @Description 报告应用数据
 * @Author qianshui
 * @Date 2020/7/29 下午2:54
 */
@Component
@Slf4j
public class ReportApplicationService {

    @Autowired
    private ReportService reportService;

    @Resource
    private TApplicationMntDao tApplicationMntDao;

    public ReportDetailDTO getDetail(Long reportId) {
        WebResponse<HashMap> response = reportService.getReportByReportId(reportId);
        if (response != null && response.getData() != null) {
            return JSON.parseObject(JSON.toJSONString(response.getData()), ReportDetailDTO.class);
        }
        return new ReportDetailDTO();
    }

    public ReportApplicationDTO getReportApplication(Long reportId) {
        ReportApplicationDTO reportApplication = new ReportApplicationDTO();
        ReportDetailDTO reportDetail = getDetail(reportId);
        reportApplication.setReportDetail(reportDetail);
        if (reportDetail == null || CollectionUtils.isEmpty(reportDetail.getBusinessActivity())) {
            return reportApplication;
        }
        Set<Long> appSet = Sets.newHashSet();
        reportDetail.getBusinessActivity().forEach(
            data -> appSet.addAll(splitApplicationIds(data.getApplicationIds())));
        if (appSet.size() == 0) {
            return reportApplication;
        }
        List<TApplicationMnt> appsList = tApplicationMntDao.queryApplicationMntListByIds(Lists.newArrayList(appSet));
        if (CollectionUtils.isEmpty(appsList)) {
            return reportApplication;
        }
        reportApplication.setApplicationNames(appsList.stream().filter(data -> StringUtils.isNoneBlank(data.getApplicationName())).map(
            data -> data.getApplicationName()).distinct().collect(Collectors.toList()));

        return reportApplication;
    }

    private List<Long> splitApplicationIds(String applicationIds) {
        if (StringUtils.isBlank(applicationIds)) {
            return Collections.EMPTY_LIST;
        }
        String[] args = applicationIds.split(",");
        List<Long> dataList = Lists.newArrayList();
        for (String arg : args) {
            dataList.add(Long.parseLong(arg));
        }
        return dataList;
    }
}
