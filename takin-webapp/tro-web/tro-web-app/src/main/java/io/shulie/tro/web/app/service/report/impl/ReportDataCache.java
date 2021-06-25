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
import com.alibaba.fastjson.JSONObject;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pamirs.tro.entity.dao.confcenter.TApplicationMntDao;
import com.pamirs.tro.entity.domain.dto.report.ReportApplicationDTO;
import com.pamirs.tro.entity.domain.dto.report.ReportDetailDTO;
import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import com.pamirs.tro.entity.domain.risk.Metrices;
import io.shulie.tro.web.app.service.report.ReportService;
import io.shulie.tro.web.common.domain.WebResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName ReportDataCache
 * @Description 压测数据准备，存放内存
 * @Author qianshui
 * @Date 2020/7/29 下午2:54
 */
@Component
@Slf4j
public class ReportDataCache {

    private static List<Metrices> allMetrics = Lists.newArrayList();

    private static ReportDetailDTO reportDetail = null;

    private static List<String> applications = Lists.newArrayList();

    @Autowired
    private TApplicationMntDao tApplicationMntDao;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportApplicationService reportApplicationService;

    /**
     * 准备基础数据
     *
     * @param reportId
     */
    public void readyCloudReportData(Long reportId) {
        queryReportDetail(reportId);

        queryAllMetricsData(reportId);

        fetchApplications();
    }

    /**
     * 查询报告基本信息
     *
     * @param reportId
     */
    private void queryReportDetail(Long reportId) {
        ReportApplicationDTO reportApplication = reportApplicationService.getReportApplication(reportId);
        reportDetail = reportApplication.getReportDetail();
        log.info("Report Id={}, Status={}", reportId, reportDetail.getTaskStatus());
        applications = reportApplication.getApplicationNames();
        log.info("Report Id={}, applicationName={}", reportId, JSON.toJSONString(applications));
    }

    /**
     * 查询all类型的所有压测指标数据
     *
     * @param reportId
     */
    private void queryAllMetricsData(Long reportId) {
        if (reportDetail == null) {
            return;
        }
        WebResponse<List<HashMap>> response = reportService.queryMetrices(reportId, reportDetail.getSceneId(),
            reportDetail.getCustomId());
        if (response == null || CollectionUtils.isEmpty(response.getData())) {
            log.error("ReportDataCache Cache Jmeter Metric is null");
            return;
        }
        log.info("ReportDataCache Cache Jmeter Metrices Data Size={}, One Sample: {}", response.getData().size(),
            JSONObject.toJSONString(response.getData().get(0)));
        List<Metrices> metrices = Lists.newArrayList();
        response.getData().forEach(data -> {
            Metrices metric = new Metrices();
            metric.setTime((Long)data.get("time"));
            metric.setAvgTps((Double)data.get("avgTps"));
            metrices.add(metric);
        });
        allMetrics.addAll(metrices);
    }

    /**
     * 获取应用列表
     */
    private void fetchApplications() {
        if (reportDetail == null || CollectionUtils.isEmpty(reportDetail.getBusinessActivity())) {
            log.error("报告中关联的业务活动为空");
            return;
        }
        Set<Long> appSet = Sets.newHashSet();
        reportDetail.getBusinessActivity().forEach(
            data -> appSet.addAll(splitApplicationIds(data.getApplicationIds())));
        if (appSet.size() == 0) {
            log.error("报告中关联的应用为空");
            return;
        }
        List<TApplicationMnt> appsList = tApplicationMntDao.queryApplicationMntListByIds(Lists.newArrayList(appSet));
        if (CollectionUtils.isEmpty(appsList)) {
            log.error("报告中关联的应用为空");
            return;
        }
        applications = appsList.stream().map(
            TApplicationMnt::getApplicationName).filter(StringUtils::isNoneBlank).distinct().collect(
            Collectors.toList());
        log.info("报告中关联的应用有：{}", JSON.toJSONString(applications));
    }

    public ReportDetailDTO getReportDetail() {
        return reportDetail;
    }

    public List<Metrices> getAllMetricsData() {
        return allMetrics;
    }

    public List<String> getApplications() {
        return applications;
    }

    public void clearDataCache() {
        allMetrics.clear();
        reportDetail = null;
        applications.clear();
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
