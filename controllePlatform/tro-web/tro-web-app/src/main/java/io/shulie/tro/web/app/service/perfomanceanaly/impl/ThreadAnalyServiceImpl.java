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

package io.shulie.tro.web.app.service.perfomanceanaly.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.pamirs.tro.common.util.DateUtils;
import io.shulie.tro.cloud.common.utils.DateUtil;
import io.shulie.tro.web.app.request.perfomanceanaly.PerformanceAnalyzeRequest;
import io.shulie.tro.web.app.request.perfomanceanaly.PerformanceCommonRequest;
import io.shulie.tro.web.app.request.perfomanceanaly.ThreadCpuUseRateRequest;
import io.shulie.tro.web.app.request.perfomanceanaly.ThreadListRequest;
import io.shulie.tro.web.app.response.perfomanceanaly.ProcessBaseDataResponse;
import io.shulie.tro.web.app.response.perfomanceanaly.ReportTimeResponse;
import io.shulie.tro.web.app.response.perfomanceanaly.ThreadCpuChartResponse;
import io.shulie.tro.web.app.response.perfomanceanaly.ThreadCpuUseRateChartResponse;
import io.shulie.tro.web.app.response.perfomanceanaly.ThreadDetailResponse;
import io.shulie.tro.web.app.response.perfomanceanaly.ThreadListResponse;
import io.shulie.tro.web.app.response.perfomanceanaly.ThreadStatusResponse;
import io.shulie.tro.web.app.service.perfomanceanaly.ReportDetailService;
import io.shulie.tro.web.app.service.perfomanceanaly.ThreadAnalyService;
import io.shulie.tro.web.data.dao.baseserver.BaseServerDao;
import io.shulie.tro.web.data.dao.perfomanceanaly.PerformanceBaseDataDAO;
import io.shulie.tro.web.data.dao.perfomanceanaly.PerformanceThreadDataDAO;
import io.shulie.tro.web.data.param.baseserver.BaseServerParam;
import io.shulie.tro.web.data.param.perfomanceanaly.PerformanceBaseQueryParam;
import io.shulie.tro.web.data.param.perfomanceanaly.PerformanceThreadQueryParam;
import io.shulie.tro.web.data.result.baseserver.BaseServerResult;
import io.shulie.tro.web.data.result.perfomanceanaly.PerformanceBaseDataResult;
import io.shulie.tro.web.data.result.perfomanceanaly.PerformanceThreadCountResult;
import io.shulie.tro.web.data.result.perfomanceanaly.PerformanceThreadDataResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName ThreadAnalyServiceImpl
 * @Description
 * @Author qianshui
 * @Date 2020/11/4 下午2:43
 */
@Service
@Slf4j
public class ThreadAnalyServiceImpl implements ThreadAnalyService {

    @Autowired
    private ReportDetailService reportDetailService;

    @Autowired
    private PerformanceBaseDataDAO performanceBaseDataDAO;

    @Autowired
    private PerformanceThreadDataDAO performanceThreadDataDAO;

    @Autowired
    private BaseServerDao baseServerDao;

    @Override
    public ProcessBaseDataResponse getBaseData(PerformanceAnalyzeRequest request) {
        PerformanceBaseQueryParam baseParam = buildBaseQueryParam(request.getReportId(), request);
        //基本数据
        PerformanceBaseDataResult baseData = performanceBaseDataDAO.getOnePerformanceBaseData(baseParam);
        ProcessBaseDataResponse baseResponse = new ProcessBaseDataResponse();
        BeanUtils.copyProperties(baseData, baseResponse);
        baseResponse.setProcessName(baseData.getAppName());
        return baseResponse;
    }

    @Override
    public List<ThreadCpuChartResponse> getThreadAnalyze(PerformanceAnalyzeRequest request) {
        PerformanceBaseQueryParam baseParam = buildBaseQueryParam(request.getReportId(), request);
        List<PerformanceBaseDataResult> baseList = performanceBaseDataDAO.getPerformanceBaseDataList(baseParam);
        if(CollectionUtils.isEmpty(baseList)) {
            return Lists.newArrayList();
        }
        List<ThreadCpuChartResponse> responses = baseList.stream().map(base -> {
            ThreadCpuChartResponse response = new ThreadCpuChartResponse();
            response.setTime(DateUtils.dateToString(new Date(base.getTimestamp()),DateUtils.FORMATE_YMDHMS).substring(11, 19));
            response.setThreadCount(base.getThreadCount());
            response.setBaseId(base.getBaseId());
            response.setCpuRate(new BigDecimal(base.getCpuUseRate()).setScale(2, RoundingMode.HALF_UP));
            return response;
        }).collect(Collectors.toList());
        return responses;
        //List<String> baseIds = baseList.stream()
        //    .map(data -> data.getBaseId()).collect(Collectors.toList());
        //List<PerformanceThreadCountResult> countList = performanceThreadDataDAO.getPerformanceThreadCountList(baseIds);
        ////分组求和
        //BaseServerParam serverParam = new BaseServerParam();
        //buildBaseServerParam(baseParam, countList, serverParam);
        //Collection<BaseServerResult> baseResults = Optional.ofNullable(baseServerDao.queryBaseData(serverParam)).orElse(Collections.EMPTY_LIST);
        //return buildThreadCpuChartResponse(countList, baseResults);
    }

    private void buildBaseServerParam(PerformanceBaseQueryParam param, List<PerformanceThreadCountResult> resultList, BaseServerParam baseParam) {
        if(CollectionUtils.isEmpty(resultList)) {
            return;
        }
        baseParam.setApplicationName(param.getAppName());
        baseParam.setAppIp(param.getAppIp());
        baseParam.setAgentId(param.getAgentId());
        baseParam.setStartTime(formatTimestamp(param.getStartTime()));
        baseParam.setEndTime(formatTimestamp(param.getEndTime()));
    }

    @Override
    public ThreadListResponse getThreadList(ThreadListRequest request) {
        PerformanceThreadQueryParam param = new PerformanceThreadQueryParam();
        param.setBaseId(request.getBaseId());
        // 获取base_id 下所有
        List<PerformanceThreadDataResult> allTempList = performanceThreadDataDAO.getPerformanceThreadDataList(param);
        // 过滤
        List<PerformanceThreadDataResult> allList=  allTempList.stream().distinct().collect(Collectors.toList());
        ThreadListResponse response = new ThreadListResponse();

        //分组求和
        Map<String, Long> statusMap = allList.stream().collect(Collectors.groupingBy(PerformanceThreadDataResult::getThreadStatus, Collectors.counting()));
        List<ThreadStatusResponse> statusList = Lists.newArrayList();
        statusMap.forEach((key, value) -> {
            ThreadStatusResponse temp = new ThreadStatusResponse();
            temp.setStatus(key);
            temp.setCount(value.intValue());
            statusList.add(temp);
        });
        Collections.sort(statusList);
        //全部
        ThreadStatusResponse total = new ThreadStatusResponse();
        total.setStatus("");
        total.setCount(statusList.stream().mapToInt(ThreadStatusResponse::getCount).sum());
        statusList.add(0, total);
        response.setStatus(statusList);
        //详情列表
        List<ThreadDetailResponse> responseList = Lists.newArrayList();

        allList.forEach(data -> {
            ThreadDetailResponse temp = new ThreadDetailResponse();
            temp.setThreadName(data.getThreadName());
            temp.setThreadCpuUseRate(new BigDecimal(String.valueOf(data.getThreadCpuUseRate())).setScale(2, RoundingMode.HALF_UP));
            temp.setThreadStackLink(data.getThreadStackLink());
            temp.setThreadStatus(data.getThreadStatus());
            responseList.add(temp);
        });
        response.setDetails(responseList);
        return response;
    }

    @Override
    public List<ThreadCpuUseRateChartResponse> getThreadCpuUseRate(ThreadCpuUseRateRequest request) {
        PerformanceBaseQueryParam baseParam = buildBaseQueryParam(request.getReportId(), request);
        List<PerformanceBaseDataResult> baseList = performanceBaseDataDAO.getPerformanceBaseDataList(baseParam);
        List<String> baseIds = baseList.stream().map(data -> data.getBaseId()).collect(Collectors.toList());
        PerformanceThreadQueryParam param = new PerformanceThreadQueryParam();
        param.setBaseIds(baseIds);
        List<PerformanceThreadDataResult> dataList = performanceThreadDataDAO.getPerformanceThreadDataList(param);
        // 根据 threadName 过滤
        List<PerformanceThreadDataResult> threadList = null;

        if(request.getThreadName() != null) {
            threadList = dataList.stream().filter(e ->e.getThreadName().equals(request.getThreadName()))
                .collect(Collectors.toList());
        }else {
            threadList = dataList;
        }
        List<ThreadCpuUseRateChartResponse> response = Lists.newArrayList();
        threadList.stream().forEach(data -> {
            ThreadCpuUseRateChartResponse temp = new ThreadCpuUseRateChartResponse();
            temp.setThreadCpuUseRate(data.getThreadCpuUseRate());
            if(StringUtils.isNotBlank(data.getTimestamp()) && data.getTimestamp().length() > 18) {
                temp.setTime(data.getTimestamp().substring(11, 19));
                response.add(temp);
            }
        });
        return response;
    }

    private PerformanceBaseQueryParam buildBaseQueryParam(Long reportId, PerformanceCommonRequest request) {
        ReportTimeResponse timeResponse = reportDetailService.getReportTime(reportId);
        PerformanceBaseQueryParam baseParam = new PerformanceBaseQueryParam();
        baseParam.setStartTime(timeResponse.getStartTime());
        baseParam.setEndTime(timeResponse.getEndTime() != null ? timeResponse.getEndTime() : DateUtil.formatTime(System.currentTimeMillis()));
        String[] splits = StringUtils.split(request.getProcessName(), "|");
        baseParam.setAppIp(splits[0]);
        baseParam.setAgentId(splits[1]);
        baseParam.setAppName(request.getAppName());
        return baseParam;
    }

    private List<ThreadCpuChartResponse> buildThreadCpuChartResponse(List<PerformanceThreadCountResult> threadList, Collection<BaseServerResult> baseResults) {
        List<ThreadCpuChartResponse> responses = Lists.newArrayList();
        int cpuSize = baseResults.size();
        // 点数 由报告时间、appIp、agentId、appName 查出baseId,并获取线程数
        List<BaseServerResult> results = Lists.newArrayList(baseResults);
        for(int i = 0; i < threadList.size(); i++ ) {
            PerformanceThreadCountResult data = threadList.get(i);
            ThreadCpuChartResponse temp = new ThreadCpuChartResponse();
            if(StringUtils.isEmpty(data.getTimestamp()) || data.getTimestamp().length() < 18) {
                continue;
            }
            temp.setTime(data.getTimestamp().substring(11, 19));
            temp.setThreadCount(data.getThreadCount());
            temp.setBaseId(data.getBaseId());
            temp.setTimestamp(DateUtils.strToDate(data.getTimestamp(),DateUtils.FORMATE_YMDHMS).getTime());
            // cpuRate 为 null,因为 app_base_data数据没有查到
            if (cpuSize > 0) {
                // 开始从app_base_data采样
                int pos = i >= cpuSize ? cpuSize - 1 : i;
                temp.setCpuRate(results.get(pos).getCpuRate() != null
                    ? new BigDecimal(String.valueOf(results.get(pos).getCpuRate())).setScale(2, RoundingMode.HALF_UP) : new BigDecimal("0"));
            }else {
                // 默认赋值 0
                temp.setCpuRate(new BigDecimal("0"));
            }
            responses.add(temp);
        }
        responses.sort((o1, o2) -> Long.compare(o1.getTimestamp(),o2.getTimestamp()));
        return responses;
    }

    @Override
    public String getThreadStackInfo(String link) {
        return performanceThreadDataDAO.getThreadStackInfo(link);
    }

    @Override
    public void clearData(Integer time) {
        Date nSecond = DateUtils.getPreviousNSecond(time);
        performanceThreadDataDAO.clearData(DateUtils.dateToString(nSecond, DateUtils.FORMATE_YMDHMS));
    }

    private long formatTimestamp(String datetime) {
        long time = io.shulie.tro.web.app.service.risk.util.DateUtil.parseSecondFormatter(datetime).getTime();
        String temp = time + "000000";
        return Long.parseLong(temp);
    }
}
