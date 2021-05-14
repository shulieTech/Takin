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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;

import com.google.common.collect.Lists;
import com.pamirs.tro.common.util.DateUtils;
import com.pamirs.tro.entity.dao.report.TReportApplicationSummaryMapper;
import com.pamirs.tro.entity.dao.report.TReportBottleneckInterfaceMapper;
import com.pamirs.tro.entity.dao.report.TReportMachineMapper;
import com.pamirs.tro.entity.dao.report.TReportSummaryMapper;
import com.pamirs.tro.entity.domain.entity.report.ReportApplicationSummary;
import com.pamirs.tro.entity.domain.entity.report.ReportMachine;
import com.pamirs.tro.entity.domain.entity.report.ReportSummary;
import com.pamirs.tro.entity.domain.entity.report.TpsTarget;
import com.pamirs.tro.entity.domain.entity.report.TpsTargetArray;
import com.pamirs.tro.entity.domain.risk.Metrices;
import io.shulie.tro.web.app.service.report.ReportService;
import io.shulie.tro.web.common.domain.WebResponse;
import io.shulie.tro.web.data.common.InfluxDBManager;
import io.shulie.tro.web.data.result.baseserver.BaseServerResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName SummaryService
 * @Description 报告汇总接口
 * @Author qianshui
 * @Date 2020/7/28 下午1:55
 */
@Component
@Slf4j
public class SummaryService {

    @Resource
    private TReportBottleneckInterfaceMapper TReportBottleneckInterfaceMapper;

    @Resource
    private TReportApplicationSummaryMapper TReportApplicationSummaryMapper;

    @Autowired
    private ReportService reportService;

    @Resource
    private TReportSummaryMapper TReportSummaryMapper;

    @Resource
    private TReportMachineMapper TReportMachineMapper;

    @Autowired
    private ReportDataCache reportDataCache;

    @Autowired
    private InfluxDBManager influxDBManager;

    public void calcApplicationSummary(Long reportId) {
        List<Map<String, Object>> dataList = TReportMachineMapper.selectCountByReport(reportId);
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }

        List<ReportApplicationSummary> applications = Lists.newArrayList();
        for (Map<String, Object> dataMap : dataList) {
            String applicationName = (String)dataMap.get("application_name");
            if (StringUtils.isBlank(applicationName)) {
                continue;
            }
            Integer totalCount = convertLong((Long)dataMap.get("count"));
            Integer riskCount = convertBigDecimal((BigDecimal)dataMap.get("riskSum"));

            ReportApplicationSummary application = new ReportApplicationSummary();
            application.setReportId(reportId);
            application.setApplicationName(applicationName);
            application.setMachineTotalCount(totalCount);
            application.setMachineRiskCount(riskCount);

            applications.add(application);
        }
        if (CollectionUtils.isEmpty(applications)) {
            return;
        }
        applications.forEach(TReportApplicationSummaryMapper::insertOrUpdate);
    }

    public void calcReportSummay(Long reportId) {
        Integer bottleneckInterfaceCount = convertLong(
            TReportBottleneckInterfaceMapper.selectCountByReportId(reportId));

        Integer appCount = 0;
        Integer totalCount = 0;
        Integer riskCount = 0;
        Map<String, Object> countMap = TReportApplicationSummaryMapper.selectCountByReportId(reportId);
        if (MapUtils.isNotEmpty(countMap)) {
            appCount = convertLong((Long)countMap.get("count"));
            totalCount = convertBigDecimal((BigDecimal)countMap.get("totalSum"));
            riskCount = convertBigDecimal((BigDecimal)countMap.get("riskSum"));
        }

        Integer warnCount = 0;
        Integer businessCount = 0;
        Integer passBusinessCount = 0;
        WebResponse response = reportService.queryReportCount(reportId);
        if (response != null && response.getData() != null) {
            Map<String, Object> cloudMap = (Map<String, Object>)response.getData();
            if (MapUtils.isNotEmpty(cloudMap)) {
                warnCount = (Integer)cloudMap.get("warnCount");
                businessCount = (Integer)cloudMap.get("count");
                passBusinessCount = (Integer)cloudMap.get("passSum");
            }
        }
        warnCount = warnCount != null ? warnCount : 0;
        businessCount = businessCount != null ? businessCount : 0;
        passBusinessCount = passBusinessCount != null ? passBusinessCount : 0;

        ReportSummary reportSummary = new ReportSummary();
        reportSummary.setReportId(reportId);
        reportSummary.setBottleneckInterfaceCount(bottleneckInterfaceCount);
        reportSummary.setRiskMachineCount(riskCount);
        reportSummary.setBusinessActivityCount(businessCount);
        reportSummary.setUnachieveBusinessActivityCount(businessCount - passBusinessCount);
        reportSummary.setApplicationCount(appCount);
        reportSummary.setMachineCount(totalCount);
        reportSummary.setWarnCount(warnCount);
        ReportSummary summary = TReportSummaryMapper.selectOneByReportId(reportId);
        // todo 临时方案
        if(summary == null) {
            TReportSummaryMapper.insert(reportSummary);
        }
        log.info("Build ReportSummary Success, reportId={}", reportId);
    }

    public void calcTpsTarget(Long reportId) {
        List<Metrices> metrics = reportDataCache.getAllMetricsData();
        if (CollectionUtils.isEmpty(metrics)) {
            return;
        }
        List<String> applications = reportDataCache.getApplications();
        if (CollectionUtils.isEmpty(applications)) {
            return;
        }
        //获取Min Max 压测时间
        Long minTime = metrics.stream().map(data -> data.getExtTime()).min((Long::compareTo)).get();
        Long maxTime = metrics.stream().map(data -> data.getExtTime()).max((Long::compareTo)).get();
        //多往前选5秒
        minTime = minTime - 5 * 1000;
        //多往后选15s
        maxTime = maxTime + 15 * 1000;

        for (String applicationName : applications) {
            //机器信息
            Collection<BaseServerResult> appIds = influxDBManager.query(BaseServerResult.class,
                "select distinct(app_ip) as app_ip from app_base_data " +
                    "where time>=" + minTime + "ms and time <= " + maxTime + "ms and app_name = \'" + applicationName
                    + "\'");
            if (CollectionUtils.isEmpty(appIds)) {
                continue;
            }
            List<String> hosts = appIds.stream().map(data -> data.getAppIp()).collect(Collectors.toList());
            for (String host : hosts) {
                Collection<BaseServerResult> bases = influxDBManager.query(BaseServerResult.class,
                    "select time, app_ip, cpu_rate, cpu_load, mem_rate, iowait, net_bandwidth_rate" +
                        " from app_base_data where time>=" + minTime + "ms and time <= " + maxTime
                        + "ms and app_name = \'" + applicationName + "\'"
                        + " and app_ip = \'" + host + "\'");

                TpsTargetArray array = calcTpsTarget(metrics, bases);
                if (array == null) {
                    continue;
                }
                ReportMachine reportMachine = new ReportMachine();
                reportMachine.setReportId(reportId);
                reportMachine.setApplicationName(applicationName);
                reportMachine.setMachineIp(host);
                reportMachine.setMachineTpsTargetConfig(JSON.toJSONString(array));
                TReportMachineMapper.updateTpsTargetConfig(reportMachine);
            }
        }
    }

    private TpsTargetArray calcTpsTarget(List<Metrices> metrics, Collection<BaseServerResult> vos) {
        if (CollectionUtils.isEmpty(vos)) {
            return null;
        }
        List<BaseServerResult> bases = Lists.newArrayList(vos);
        int currentIndex = 0;
        List<TpsTarget> targets = Lists.newArrayList();
        for (int i = 0; i < metrics.size(); i++) {
            TpsTarget target = new TpsTarget();
            target.setTime(convertLongToTime(metrics.get(i).getExtTime()));
            target.setTps(metrics.get(i).getAvgTps().intValue());
            for (int j = currentIndex; j < bases.size(); j++) {
                if (bases.get(j).getExtTime() <= metrics.get(i).getExtTime()) {
                    continue;
                }
                if (currentIndex < j) {
                    Double cpu = bases.subList(currentIndex, j).stream().filter(data -> data.getCpuRate() != null)
                        .mapToDouble(data -> data.getCpuRate()).average().orElse(0D);
                    Double loading = bases.subList(currentIndex, j).stream().filter(data -> data.getCpuLoad() != null)
                        .mapToDouble(data -> data.getCpuLoad()).average().orElse(0D);
                    Double memory = bases.subList(currentIndex, j).stream().filter(data -> data.getMemRate() != null)
                        .mapToDouble(data -> data.getMemRate()).average().orElse(0D);
                    Double io = bases.subList(currentIndex, j).stream().filter(data -> data.getIoWait() != null)
                        .mapToDouble(data -> data.getIoWait()).average().orElse(0D);
                    Double mbps = bases.subList(currentIndex, j).stream().filter(
                        data -> data.getNetBandWidthRate() != null).mapToDouble(data -> data.getNetBandWidthRate())
                        .average().orElse(0D);

                    target.setCpu(new BigDecimal(cpu.intValue()));
                    target.setLoading(new BigDecimal(loading).setScale(2, RoundingMode.HALF_UP));
                    target.setMemory(new BigDecimal(memory).setScale(2, RoundingMode.HALF_UP));
                    target.setIo(new BigDecimal(io).setScale(2, RoundingMode.HALF_UP));
                    target.setNetwork(new BigDecimal(mbps).setScale(2, RoundingMode.HALF_UP));
                }
                currentIndex = j;
                break;
            }

            if (target.getCpu() != null) {
                targets.add(target);
            }
        }
        if (CollectionUtils.isEmpty(targets)) {
            return null;
        }
        //转化为array
        return convert2TpsTargetArray(targets);
    }

    private Integer convertLong(Long param) {
        if (param == null) {
            return 0;
        }
        return param.intValue();
    }

    private Integer convertBigDecimal(BigDecimal param) {
        if (param == null) {
            return 0;
        }
        return param.intValue();
    }

    private TpsTargetArray convert2TpsTargetArray(List<TpsTarget> tpsList) {
        if (CollectionUtils.isEmpty(tpsList)) {
            return null;
        }

        String[] time = new String[tpsList.size()];

        Integer[] tps = new Integer[tpsList.size()];

        BigDecimal[] cpu = new BigDecimal[tpsList.size()];

        BigDecimal[] loading = new BigDecimal[tpsList.size()];

        BigDecimal[] memory = new BigDecimal[tpsList.size()];

        BigDecimal[] io = new BigDecimal[tpsList.size()];

        BigDecimal[] network = new BigDecimal[tpsList.size()];

        for (int i = 0; i < tpsList.size(); i++) {
            time[i] = tpsList.get(i).getTime();
            tps[i] = tpsList.get(i).getTps();
            cpu[i] = tpsList.get(i).getCpu();
            loading[i] = tpsList.get(i).getLoading();
            memory[i] = tpsList.get(i).getMemory();
            io[i] = tpsList.get(i).getIo();
            network[i] = tpsList.get(i).getNetwork();
        }
        TpsTargetArray array = new TpsTargetArray();
        array.setTime(time);
        array.setTps(tps);
        array.setCpu(cpu);
        array.setLoading(loading);
        array.setMemory(memory);
        array.setIo(io);
        array.setNetwork(network);
        return array;
    }

    private String convertLongToTime(Long time) {
        if (time == null) {
            return null;
        }
        String date = DateUtils.transferDate(time);
        if (StringUtils.length(date) == 19) {
            return date.substring(11);
        }
        return date;
    }
}
