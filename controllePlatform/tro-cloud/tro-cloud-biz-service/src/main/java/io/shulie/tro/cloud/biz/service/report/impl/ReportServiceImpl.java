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

package io.shulie.tro.cloud.biz.service.report.impl;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pamirs.tro.entity.dao.report.TReportBusinessActivityDetailMapper;
import com.pamirs.tro.entity.dao.report.TReportMapper;
import com.pamirs.tro.entity.dao.scenemanage.TWarnDetailMapper;
import com.pamirs.tro.entity.dao.user.TUserMapper;
import com.pamirs.tro.entity.domain.bo.scenemanage.WarnBO;
import com.pamirs.tro.entity.domain.dto.report.BusinessActivityDTO;
import com.pamirs.tro.entity.domain.dto.report.Metrices;
import com.pamirs.tro.entity.domain.dto.report.ReportDTO;
import com.pamirs.tro.entity.domain.dto.report.ReportTrendDTO;
import com.pamirs.tro.entity.domain.dto.report.StatReportDTO;
import com.pamirs.tro.entity.domain.entity.report.Report;
import com.pamirs.tro.entity.domain.entity.report.ReportBusinessActivityDetail;
import com.pamirs.tro.entity.domain.entity.scenemanage.WarnDetail;
import com.pamirs.tro.entity.domain.entity.user.User;
import com.pamirs.tro.entity.domain.vo.report.ReportQueryParam;
import com.pamirs.tro.entity.domain.vo.report.ReportTrendQueryParam;
import com.pamirs.tro.entity.domain.vo.settle.AccountTradeRequest;
import io.shulie.tro.cloud.biz.cloudserver.ReportConverter;
import io.shulie.tro.cloud.biz.collector.collector.AbstractIndicators;
import io.shulie.tro.cloud.biz.collector.collector.CollectorService;
import io.shulie.tro.cloud.biz.input.report.UpdateReportConclusionInput;
import io.shulie.tro.cloud.biz.input.report.WarnCreateInput;
import io.shulie.tro.cloud.biz.output.report.ReportDetailOutput;
import io.shulie.tro.cloud.biz.output.scenemanage.SceneManageWrapperOutput;
import io.shulie.tro.cloud.biz.output.scenemanage.SceneManageWrapperOutput.SceneBusinessActivityRefOutput;
import io.shulie.tro.cloud.biz.output.scenemanage.WarnDetailOutput;
import io.shulie.tro.cloud.biz.service.report.ReportService;
import io.shulie.tro.cloud.biz.service.scene.ReportEventService;
import io.shulie.tro.cloud.biz.service.scene.SceneManageService;
import io.shulie.tro.cloud.biz.service.scene.SceneTaskEventServie;
import io.shulie.tro.cloud.biz.service.settle.SettleService;
import io.shulie.tro.cloud.common.bean.scenemanage.BusinessActivitySummaryBean;
import io.shulie.tro.cloud.common.bean.scenemanage.DataBean;
import io.shulie.tro.cloud.common.bean.scenemanage.DistributeBean;
import io.shulie.tro.cloud.common.bean.scenemanage.SceneManageQueryOpitons;
import io.shulie.tro.cloud.common.bean.scenemanage.UpdateStatusBean;
import io.shulie.tro.cloud.common.bean.scenemanage.WarnBean;
import io.shulie.tro.cloud.common.bean.sla.WarnQueryParam;
import io.shulie.tro.cloud.common.bean.task.TaskResult;
import io.shulie.tro.cloud.common.constants.Constants;
import io.shulie.tro.cloud.common.constants.ReportConstans;
import io.shulie.tro.cloud.common.constants.ScheduleConstants;
import io.shulie.tro.cloud.common.enums.scenemanage.SceneManageStatusEnum;
import io.shulie.tro.cloud.common.exception.TroCloudException;
import io.shulie.tro.cloud.common.exception.TroCloudExceptionEnum;
import io.shulie.tro.cloud.common.influxdb.InfluxDBUtil;
import io.shulie.tro.cloud.common.influxdb.InfluxWriter;
import io.shulie.tro.cloud.common.redis.RedisClientUtils;
import io.shulie.tro.cloud.common.utils.DateUtil;
import io.shulie.tro.cloud.common.utils.GsonUtil;
import io.shulie.tro.cloud.common.utils.ListHelper;
import io.shulie.tro.cloud.data.dao.report.ReportDao;
import io.shulie.tro.cloud.data.param.report.ReportDataQueryParam;
import io.shulie.tro.cloud.data.param.report.ReportUpdateConclusionParam;
import io.shulie.tro.cloud.data.param.report.ReportUpdateParam;
import io.shulie.tro.cloud.data.result.report.ReportResult;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.eventcenter.Event;
import io.shulie.tro.eventcenter.annotation.IntrestFor;
import io.shulie.tro.utils.file.FileManagerHelper;
import io.shulie.tro.utils.json.JsonHelper;
import io.shulie.tro.utils.linux.LinuxHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.influxdb.impl.TimeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author 莫问
 * @Date 2020-04-17
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    public static final String COMPARE = "<=";
    @Autowired
    private ReportDao reportDao;
    @Resource
    private TReportMapper TReportMapper;
    @Resource
    private TReportBusinessActivityDetailMapper TReportBusinessActivityDetailMapper;
    @Resource
    private TWarnDetailMapper TWarnDetailMapper;
    @Resource
    private TUserMapper tUserMapper;
    @Autowired
    private SceneManageService sceneManageService;
    @Autowired
    private ReportEventService reportEventService;
    @Autowired
    private SettleService settleService;
    @Autowired
    private InfluxWriter influxWriter;
    @Autowired
    private RedisClientUtils redisClientUtils;
    @Autowired
    private SceneTaskEventServie sceneTaskEventServie;
    @Value("${report.aggregation.interval}")
    private String reportAggregationInterval;

    @Value("${pressure.engine.jtl.path:/nfs_dir/jtl}")
    private String pressureEngineJtlPath;
    @Value("${pressure.engine.log.path:/nfs_dir/logs}")
    private String pressureEngineLogPath;

    @Override
    public PageInfo<ReportDTO> listReport(ReportQueryParam param) {
        PageHelper.startPage(param.getCurrentPage() + 1, param.getPageSize());
        //默认只查询普通场景的报告
        if (param.getType() == null){
            param.setType(0);
        }
        List<Report> reportList = TReportMapper.listReport(param);
        if (CollectionUtils.isEmpty(reportList)) {
            return new PageInfo<ReportDTO>(Collections.EMPTY_LIST);
        }
        PageInfo<Report> old = new PageInfo<>(reportList);
        Map<Long, String> errorMsgMap = new HashMap<>();
        for (Report report : reportList) {
            if (report.getConclusion() != null && report.getConclusion() == 0 && report.getFeatures() != null) {
                JSONObject jsonObject = JSON.parseObject(report.getFeatures());
                String key = "error_msg";
                if (jsonObject.containsKey(key)) {
                    errorMsgMap.put(report.getId(), jsonObject.getString(key));
                }
            }
        }
        List<ReportDTO> list = ReportConverter.INSTANCE.ofReport(reportList);
        for (ReportDTO dto : list) {
            if (errorMsgMap.containsKey(dto.getId())) {
                dto.setErrorMsg(errorMsgMap.get(dto.getId()));
            }
        }
        List<Long> customIds = list.stream().filter(report -> report.getCustomId() != null).map(
            ReportDTO::getCustomId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(customIds)) {
            Map<Long, String> userMap = ListHelper.transferToMap(tUserMapper.selectByIds(customIds),
                User::getId, User::getName);
            list.forEach(data -> {
                data.setCustomName(data.getCustomId() != null ? userMap.get(data.getCustomId()) : null);
            });
        }

        PageInfo<ReportDTO> data = new PageInfo<ReportDTO>(list);
        data.setTotal(old.getTotal());

        return data;
    }

    @Override
    public ReportDetailOutput getReportByReportId(Long reportId) {
        ReportResult report = reportDao.selectById(reportId);
        if (report == null) {
            log.warn("获取报告异常，报告数据不存在。报告ID：{}", reportId);
            return null;
        }
        ReportDetailOutput detail = ReportConverter.INSTANCE.ofReportDetail(report);

        //操作用户
        User user = tUserMapper.selectById(report.getOperateId());
        if (user != null) {
            detail.setOperateName(user.getName());
        }

        if (report.getCustomId() != null) {
            User custom = tUserMapper.selectById(report.getCustomId());
            if (custom != null) {
                detail.setCustomName(custom.getName());
            }
        }

        //警告列表
        List<WarnBean> warnList = listWarn(reportId);
        detail.setTaskStatus(report.getStatus());
        if (CollectionUtils.isNotEmpty(warnList)) {
            detail.setWarn(warnList);
            detail.setTotalWarn(warnList.stream().mapToLong(WarnBean::getTotal).sum());
        }
        if (StringUtils.isNotEmpty(report.getFeatures())) {
            detail.setConclusionRemark(
                JSON.parseObject(report.getFeatures()).getString(ReportConstans.FEATURES_ERROR_MSG));
        }
        detail.setTestTotalTime(DateUtil.formatTestTime(report.getStartTime(), report.getEndTime()));
        detail.setBusinessActivity(getBusinessActivitySummaryList(reportId));
        //任务没有完成，提示用户正在生成中
        if (report.getStatus() != ReportConstans.FINISH_STATUS) {
            detail.setTaskStatus(ReportConstans.RUN_STATUS);
        }

        return detail;
    }

    @Override
    public ReportTrendDTO queryReportTrend(ReportTrendQueryParam reportTrendQuery) {
        return queryReportTrend(reportTrendQuery, false);
    }

    @Override
    public ReportDetailOutput tempReportDetail(Long sceneId) {
        long start = System.currentTimeMillis();
        ReportDetailOutput reportDetail = new ReportDetailOutput();

        ReportResult reportResult = reportDao.getTempReportBySceneId(sceneId);
        if (reportResult == null) {
            reportDetail.setTaskStatus(ReportConstans.FINISH_STATUS);
            return reportDetail;
        }
        SceneManageQueryOpitons options = new SceneManageQueryOpitons();
        options.setIncludeBusinessActivity(true);
        SceneManageWrapperOutput wapper = sceneManageService.getSceneManage(sceneId, options);
        reportDetail = ReportConverter.INSTANCE.ofReportDetail(reportResult);
        reportDetail.setManagerId(wapper.getManagerId());

        StatReportDTO statReport = statTempReport(sceneId, reportResult.getId(), reportResult.getCustomId(),
            ReportConstans.ALL_BUSINESS_ACTIVITY);
        if (statReport == null) {
            log.warn("实况报表:[{}]，暂无数据", reportResult.getId());
        } else {
            reportDetail.setTotalRequest(statReport.getTotalRequest());
            reportDetail.setAvgRt(statReport.getAvgRt());
            reportDetail.setAvgTps(statReport.getTps());
            reportDetail.setSa(statReport.getSa());
            reportDetail.setSuccessRate(statReport.getSuccessRate());
            reportDetail.setAvgConcurrent(statReport.getAvgConcurrenceNum());
        }
        reportDetail.setSceneName(wapper.getPressureTestSceneName());
        reportDetail.setConcurrent(wapper.getConcurrenceNum());
        reportDetail.setTotalWarn(TWarnDetailMapper.countReportTotalWarn(reportResult.getId()));
        reportDetail.setTaskStatus(reportResult.getStatus());
        reportDetail.setTestTime(getTaskTime(reportResult.getStartTime(), new Date(), wapper.getTotalTestTime()));
        reportDetail.setTestTotalTime(
            String.format("%d'%d\"", wapper.getTotalTestTime() / 60, wapper.getTotalTestTime() % 60));
        User user = tUserMapper.selectById(reportResult.getOperateId());
        if (user != null) {
            reportDetail.setOperateName(user.getName());
        }

        List<SceneBusinessActivityRefOutput> refList = wapper.getBusinessActivityConfig();
        List list = Lists.newArrayList();
        refList.forEach(businessActivityRef -> {
            StatReportDTO data = statTempReport(sceneId, reportResult.getId(), reportResult.getCustomId(), businessActivityRef.getBindRef());
            BusinessActivitySummaryBean businessActivity = new BusinessActivitySummaryBean();
            businessActivity.setBusinessActivityId(businessActivityRef.getBusinessActivityId());
            businessActivity.setBusinessActivityName(businessActivityRef.getBusinessActivityName());
            if (data != null) {
                businessActivity.setAvgRT(new DataBean(data.getAvgRt(), businessActivityRef.getTargetRT()));
                businessActivity.setSa(new DataBean(data.getSa(), businessActivityRef.getTargetSA()));
                businessActivity.setTps(new DataBean(data.getTps(), businessActivityRef.getTargetTPS()));
                businessActivity.setSucessRate(new DataBean(data.getSuccessRate(), businessActivityRef.getTargetSuccessRate()));
                businessActivity.setAvgConcurrenceNum(data.getAvgConcurrenceNum());
                businessActivity.setTotalRequest(data.getTotalRequest());
            } else {
                businessActivity.setBusinessActivityName(businessActivityRef.getBusinessActivityName());
                businessActivity.setAvgRT(new DataBean("0", businessActivityRef.getTargetRT()));
                businessActivity.setSa(new DataBean("0", businessActivityRef.getTargetSA()));
                businessActivity.setTps(new DataBean("0", businessActivityRef.getTargetTPS()));
                businessActivity.setAvgConcurrenceNum(new BigDecimal(0));
                businessActivity.setSucessRate(new DataBean("0", businessActivityRef.getTargetSuccessRate()));
                businessActivity.setTotalRequest(0L);
            }
            list.add(businessActivity);
        });
        reportDetail.setBusinessActivity(list);

        //检查任务是否超时
        boolean taskIsTimeOut = checkSceneTaskIsTimeOut(reportResult, wapper);
        if (wapper.getStatus().intValue() == SceneManageStatusEnum.PTING.getValue().intValue() && taskIsTimeOut) {
            log.info("报表[{}]超时，通知调度马上停止压测", reportResult.getId());
            //报告正在生成中
            reportDetail.setTaskStatus(ReportConstans.RUN_STATUS);
            //重置时间
            reportDetail.setTestTime(
                String.format("%d'%d\"", wapper.getTotalTestTime() / 60, wapper.getTotalTestTime() % 60));

            //主动通知暂停事件，注意有可能会被多次触发
            sceneTaskEventServie.callStopEvent(reportResult);
        }
        log.info("实时监测metric数据：tempReportDetail-运行时间：{}", System.currentTimeMillis() - start);
        return reportDetail;
    }

    @Override
    public ReportTrendDTO queryTempReportTrend(ReportTrendQueryParam reportTrendQuery) {
        return queryReportTrend(reportTrendQuery, true);
    }

    @Override
    public PageInfo<WarnDetailOutput> listWarn(WarnQueryParam param) {
        PageHelper.startPage(param.getCurrentPage() + 1, param.getPageSize());
        List<WarnDetail> warnDetailList = TWarnDetailMapper.listWarn(param);
        if (CollectionUtils.isEmpty(warnDetailList)) {
            return new PageInfo<WarnDetailOutput>();
        }
        PageInfo<WarnDetail> old = new PageInfo<WarnDetail>(warnDetailList);
        List<WarnDetailOutput> list = ReportConverter.INSTANCE.ofWarnDetail(warnDetailList);
        PageInfo<WarnDetailOutput> data = new PageInfo<WarnDetailOutput>(list);
        data.setTotal(old.getTotal());
        return data;
    }

    @Override
    public List<BusinessActivityDTO> queryReportActivityByReportId(Long reportId) {
        List<ReportBusinessActivityDetail> reportBusinessActivityDetailList = TReportBusinessActivityDetailMapper
            .queryReportBusinessActivityDetailByReportId(reportId);
        return ReportConverter.INSTANCE.ofBusinessActivity(reportBusinessActivityDetailList);
    }

    @Override
    public List<BusinessActivityDTO> queryReportActivityBySceneId(Long sceneId) {
        ReportResult reportResult = reportDao.getReportBySceneId(sceneId);
        if (reportResult != null) {
            return queryReportActivityByReportId(reportResult.getId());
        }
        return Lists.newArrayList();
    }

    /**
     * SLA警告信息
     *
     * @return
     */
    private List<WarnBean> listWarn(Long reportId) {
        List<WarnBO> warnBOList = TWarnDetailMapper.summaryWarnByReportId(reportId);
        return ReportConverter.INSTANCE.ofWarn(warnBOList);
    }

    /**
     * 业务活动概况
     *
     * @return
     */
    @Override
    public List<BusinessActivitySummaryBean> getBusinessActivitySummaryList(Long reportId) {
        List<BusinessActivitySummaryBean> list = Lists.newArrayList();
        //查询业务活动的概况
        List<ReportBusinessActivityDetail> reportBusinessActivityDetailList = TReportBusinessActivityDetailMapper
            .queryReportBusinessActivityDetailByReportId(reportId);
        if (CollectionUtils.isEmpty(reportBusinessActivityDetailList)) {
            return Collections.EMPTY_LIST;
        }
        reportBusinessActivityDetailList.forEach(reportBusinessActivityDetail -> {
            BusinessActivitySummaryBean businessActivity = new BusinessActivitySummaryBean();
            businessActivity.setBusinessActivityId(reportBusinessActivityDetail.getBusinessActivityId());
            businessActivity.setAvgConcurrenceNum(reportBusinessActivityDetail.getAvgConcurrenceNum());
            businessActivity.setBusinessActivityName(reportBusinessActivityDetail.getBusinessActivityName());
            businessActivity.setApplicationIds(reportBusinessActivityDetail.getApplicationIds());
            businessActivity.setBindRef(reportBusinessActivityDetail.getBindRef());
            businessActivity.setTotalRequest(reportBusinessActivityDetail.getRequest());
            businessActivity.setAvgRT(new DataBean(reportBusinessActivityDetail.getRt(),
                    reportBusinessActivityDetail.getTargetRt()));
            businessActivity.setSa(new DataBean(reportBusinessActivityDetail.getSa(),
                    reportBusinessActivityDetail.getTargetSa()));
            businessActivity.setTps(new DataBean(reportBusinessActivityDetail.getTps(),
                    reportBusinessActivityDetail.getTargetTps()));
            businessActivity.setSucessRate(new DataBean(reportBusinessActivityDetail.getSuccessRate(),
                            reportBusinessActivityDetail.getTargetSuccessRate()));
            businessActivity.setMaxRt(reportBusinessActivityDetail.getMaxRt());
            businessActivity.setMaxTps(reportBusinessActivityDetail.getMaxTps());
            businessActivity.setMinRt(reportBusinessActivityDetail.getMinRt());
            businessActivity.setPassFlag(Optional.ofNullable(reportBusinessActivityDetail.getPassFlag()).orElse(0));
            if (StringUtils.isNoneBlank(reportBusinessActivityDetail.getRtDistribute())) {
                Map<String, String> distributeMap = JsonHelper.string2Obj(
                    reportBusinessActivityDetail.getRtDistribute(), new TypeReference<Map<String, String>>() {});
                List<DistributeBean> distributes = Lists.newArrayList();
                distributeMap.forEach((key, value) -> {
                    DistributeBean distribute = new  DistributeBean();
                    distribute.setLable(key);
                    distribute.setValue(COMPARE + value);
                    distributes.add(distribute);
                });
                distributes.sort(((o1, o2) -> {
                    if (o1.getLable().compareTo(o2.getLable()) > 0) {
                        return -1;
                    } else {
                        return 1;
                    }
                }));
                businessActivity.setDistribute(distributes);
            } else {
                businessActivity.setDistribute(Lists.newArrayList());
            }
            list.add(businessActivity);
        });
        return list;
    }

    @Override
    public Map<String, Object> getReportCount(Long reportId) {
        Map<String, Object> dataMap = TReportBusinessActivityDetailMapper.selectCountByReportId(reportId);
        if (MapUtils.isEmpty(dataMap)) {
            dataMap = Maps.newHashMap();
        }
        dataMap.put("warnCount", TWarnDetailMapper.countReportTotalWarn(reportId));
        return dataMap;
    }

    @Override
    public Long queryRunningReport() {
        Report report = TReportMapper.selectOneRunningReport();
        return report == null ? null : report.getId();
    }

    @Override
    public List<Long> queryListRunningReport() {
        List<Report> report = TReportMapper.selectListRunningReport();
        return CollectionUtils.isEmpty(report) ? null : report.stream().map(Report::getId).collect(Collectors.toList());
    }

    @Override
    public Boolean lockReport(Long reportId) {
        ReportResult reportResult = reportDao.selectById(reportId);
        if(ReportConstans.LOCK_STATUS == reportResult.getLock()) {
            log.error("报告{}状态锁定状态，不能再次锁定", reportId);
            return false;
        }
        // 解锁
        reportDao.updateReportLock(reportId,ReportConstans.LOCK_STATUS);
        log.info("报告{}锁定成功", reportId);
        return true;
    }

    @Override
    public Boolean unLockReport(Long reportId) {
        ReportResult reportResult = reportDao.selectById(reportId);
        if(ReportConstans.LOCK_STATUS != reportResult.getLock()) {
            log.error("报告{}状态非锁定状态，不能解锁", reportId);
            return false;
        }
        // 解锁
        reportDao.updateReportLock(reportId,ReportConstans.RUN_STATUS);
        log.info("报告{}解锁成功", reportId);
        return true;
    }

    @Override
    public Boolean finishReport(Long reportId) {
        ReportResult reportResult = reportDao.selectById(reportId);
        //if(ReportConstans.LOCK_STATUS != reportResult.getLock()) {
        //    log.error("报告{}状态非锁定状态，不能完成", reportId);
        //    return false;
        //}
        // todo 临时方案
        if(reportResult.getEndTime() == null ) {
            log.error("报告{} endTime 为null", reportId);
            return false;
        }
        reportDao.finishReport(reportId);

        UpdateStatusBean reportStatus = new UpdateStatusBean();
        //完成报告之后锁定报告
        reportStatus.setPreStatus(ReportConstans.RUN_STATUS);
        reportStatus.setAfterStatus(ReportConstans.LOCK_STATUS);
        TReportMapper.updateReportLock(reportStatus);

        // 两个地方关闭压测引擎，版本不同，关闭方式不同
        //更新场景 压测引擎停止 ---> 待启动
        sceneManageService.updateSceneLifeCycle(
            UpdateStatusBean.build(reportResult.getSceneId(), reportResult.getId(), reportResult.getCustomId()).checkEnum(
                SceneManageStatusEnum.STOP).updateEnum(SceneManageStatusEnum.WAIT).build());

        String redisTpsLimitKey = AbstractIndicators.getRedisTpsLimitKey(reportResult.getSceneId(), reportResult.getId(),
            reportResult.getCustomId());
        redisClientUtils.delete(redisTpsLimitKey);
        String redisTpsPodNumKey = AbstractIndicators.getRedisTpsPodNumKey(reportResult.getSceneId(), reportResult.getId(),
            reportResult.getCustomId());
        redisClientUtils.delete(redisTpsPodNumKey);
        String redisTpsAllLimitKey = AbstractIndicators.getRedisTpsAllLimitKey(reportResult.getSceneId(), reportResult.getId(),
            reportResult.getCustomId());
        redisClientUtils.delete(redisTpsAllLimitKey);
        return true;
    }

    /**
     * 实况报表取值
     *
     * @return
     */
    private StatReportDTO statTempReport(Long sceneId, Long reportId, Long customId, String transaction) {
        StringBuffer influxDBSQL = new StringBuffer();
        influxDBSQL.append("select");
        influxDBSQL.append(
            " count as totalRequest, fail_count as failRequest, avg_tps as tps , avg_rt as sumRt, sa_count as saCount,"
                + " active_threads as avgConcurrenceNum");
        influxDBSQL.append(" from ");
        influxDBSQL.append(InfluxDBUtil.getMeasurement(sceneId, reportId, customId));
        influxDBSQL.append(" where ");
        influxDBSQL.append(" transaction = ").append("'").append(transaction).append("'");
        influxDBSQL.append(" order by time desc limit 1");
        return influxWriter.querySingle(influxDBSQL.toString(), StatReportDTO.class);
    }

    /**
     * 查看报表实况
     *
     * @param reportTrendQuery 报表查询对象
     * @param isTempReport     是否实况报表
     * @return
     */
    private ReportTrendDTO queryReportTrend(ReportTrendQueryParam reportTrendQuery, boolean isTempReport) {
        long start = System.currentTimeMillis();
        ReportTrendDTO reportTrend = new ReportTrendDTO();
        ReportResult reportResult = null;
        if (isTempReport) {
            reportResult = reportDao.getTempReportBySceneId(reportTrendQuery.getSceneId());
        } else {
            reportResult =  reportDao.selectById(reportTrendQuery.getReportId());
        }

        String transaction = ReportConstans.ALL_BUSINESS_ACTIVITY;
        if (reportTrendQuery.getBusinessActivityId() != null && reportTrendQuery.getBusinessActivityId() > 0) {
            List<ReportBusinessActivityDetail> details = TReportBusinessActivityDetailMapper
                .queryReportBusinessActivityDetailByReportId(reportResult.getId());
            if (CollectionUtils.isEmpty(details)) {
                transaction = null;
            } else {
                transaction = details.stream().filter(
                    data -> reportTrendQuery.getBusinessActivityId().equals(data.getBusinessActivityId())).map(
                    data -> data.getBindRef()).findFirst().orElse(null);
            }
        }

        StringBuffer influxDBSQL = new StringBuffer();
        influxDBSQL.append("select");
        influxDBSQL.append(
            " sum(count) as totalRequest, sum(fail_count) as failRequest, mean(avg_tps) as tps , mean(avg_rt) as "
                + "sumRt, sum(sa_count) as saCount, count(sum_rt) as recordCount ,mean(active_threads) as avgConcurrenceNum ");
        influxDBSQL.append(" from ");
        influxDBSQL.append(InfluxDBUtil.getMeasurement(reportResult.getSceneId(), reportResult.getId(), reportResult.getCustomId()));
        influxDBSQL.append(" where ");
        influxDBSQL.append(" transaction = ").append("'").append(transaction).append("'");

        //按配置中的时间间隔分组
        influxDBSQL.append(" group by time(").append(reportAggregationInterval).append(")");

        List<StatReportDTO> list = Lists.newArrayList();

        if (StringUtils.isNotEmpty(transaction)) {
            list = influxWriter.query(influxDBSQL.toString(), StatReportDTO.class);
        }

        //influxdb 空数据也会返回,需要过滤空数据
        //前端要求的格式
        List<String> time = Lists.newLinkedList();
        List<String> sa = Lists.newLinkedList();
        List<String> avgRt = Lists.newLinkedList();
        List<String> tps = Lists.newLinkedList();
        List<String> successRate = Lists.newLinkedList();
        List<String> concurrent = Lists.newLinkedList();

        list.stream().filter(s -> s.getTps() != null).forEach(data -> {
            time.add(getTime(data.getTime()));
            sa.add(data.getSa().setScale(2, RoundingMode.HALF_DOWN).toString());
            avgRt.add(data.getAvgRt().setScale(2, RoundingMode.HALF_DOWN).toString());
            tps.add(data.getTps().setScale(2, RoundingMode.HALF_DOWN).toString());
            successRate.add(data.getSuccessRate().setScale(2, RoundingMode.HALF_DOWN).toString());
            concurrent.add(data.getAvgConcurrenceNum().setScale(2, RoundingMode.HALF_DOWN).toString());
        });

        //链路趋势
        reportTrend.setTps(tps);
        reportTrend.setSa(sa);
        reportTrend.setSuccessRate(successRate);
        reportTrend.setRt(avgRt);
        reportTrend.setTime(time);
        reportTrend.setConcurrent(concurrent);
        log.info("实时监测链路趋势：queryReportTrend-运行时间：{}", System.currentTimeMillis() - start);

        return reportTrend;
    }

    /**
     * 日期格式化ck
     *
     * @return
     */
    private String getTime(String time) {
        long date = TimeUtil.fromInfluxDBTimeFormat(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(date));
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR));
        return DateUtil.getDate(calendar.getTime(), "HH:mm:ss");
    }

    /**
     * 压测时间格式化
     *
     * @return
     */
    private String getTaskTime(Date startTime, Date endTime, Long totalTestTime) {
        LocalDateTime start = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        long seconds = Duration.between(start, end).getSeconds();
        if (seconds > totalTestTime) {
            seconds = totalTestTime;
        }
        long minutes = seconds / 60;
        long second = seconds % 60;
        return String.format("%d'%d\"", minutes, second < 0 ? 0 : second);
    }

    /**
     * 检查场景状态是否超时
     */
    public boolean checkSceneTaskIsTimeOut(ReportResult reportResult, SceneManageWrapperOutput scene) {
        long totalTestTime = scene.getTotalTestTime();
        long runTime = DateUtil.getUntilSecond(reportResult.getStartTime(), new Date());
        if (runTime >= totalTestTime) {
            return true;
        }
        return false;
    }

    /**
     * 通过场景iD和报告ID。获取压测中jemeter上报的数据
     *
     * @return
     */
    @Override
    public List<Metrices> metrices(Long reportId, Long sceneId, Long customerId) {
        List<Metrices> metricesList = Lists.newArrayList();
        if (StringUtils.isBlank(String.valueOf(reportId))) {
            return metricesList;
        }
        try {
            String measurement = InfluxDBUtil.getMeasurement(sceneId, reportId, customerId);
            metricesList = influxWriter.query(
                "select time,avg_tps as avgTps from " + measurement + " where transaction=\'all\'", Metrices.class);
        } catch (Throwable e) {
            log.error("查询失败" + ExceptionUtils.getStackTrace(e));
        }
        return metricesList;
    }

    private void getReportFeatures(ReportResult reportResult, String errKey, String errMsg) {
        Map<String, String> map = Maps.newHashMap();
        if (StringUtils.isNotBlank(reportResult.getFeatures())) {
            map = JsonHelper.string2Obj(reportResult.getFeatures(), new TypeReference<Map<String, String>>() {});
        }
        if (StringUtils.isNotBlank(errMsg)) {
            map.compute(errKey, (k, v) -> StringUtils.isBlank(v) ? errMsg : (v  + "、" + errMsg));
            reportResult.setFeatures(GsonUtil.gsonToString(map));
        }
    }

    @Override
    public void updateReportFeatures(Long reportId, Integer status, String errKey, String errMsg) {
        ReportResult reportResult = reportDao.selectById(reportId);
        // 完成状态
        reportResult.setStatus(status);
        getReportFeatures(reportResult, errKey, errMsg);
        ReportUpdateParam param = new ReportUpdateParam();
        BeanUtils.copyProperties(reportResult,param);
        reportDao.updateReport(param);
    }

    /**
     * 压测报表-指定责任人
     *
     * @param dataId
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int allocationReportUser(Long dataId, Long userId) {
        if (null == dataId || null == userId) {
            return 0;
        }
        return TReportMapper.updateReportUserById(dataId, userId);
    }

    @Override
    public ResponseResult<String> getJtlDownLoadUrl(Long reportId) {
        ReportResult reportResult = reportDao.selectById(reportId);
        if (reportResult == null) {
            return ResponseResult.fail("zip失败", "未找到报告");
        }
        // 1.查看是否有jtl.zip /nfs_dir/jtl/127/1637/pressure.jtl
        String jtlPath = pressureEngineJtlPath + "/" + reportResult.getSceneId() + "/" + reportId;
        String logPath = pressureEngineLogPath + "/" + reportResult.getSceneId() + "/" + reportId;
        if (new File(jtlPath + "/" + "Jmeter.zip").exists()) {
            // 2.存在直接返回
            return ResponseResult.success(jtlPath + "/" + "Jmeter.zip");
        } else {
            // 开始压缩
            Boolean result = LinuxHelper.executeLinuxCmd(
                "sudo zip -r -j " + jtlPath + "/" + "Jmeter.zip " + jtlPath + " " + logPath);
            if (result) {
                // 返回成功
                return ResponseResult.success(jtlPath + "/" + "Jmeter.zip");
            }
            return ResponseResult.fail("zip失败", "查看" + jtlPath);
        }
    }

    @Override
    public void clearLog(String time) {
        ReportDataQueryParam param = new ReportDataQueryParam();
        param.setEndTime(time);
        List<ReportResult> results = reportDao.getList(param);
        // 定时清理
        if (results != null && results.size() > 0) {
            List<String> paths = results.stream().map(reportResult -> {
                String jtlPath = pressureEngineJtlPath + "/" + reportResult.getSceneId() + "/" + reportResult.getId();
                return jtlPath;
            }).collect(Collectors.toList());
            log.debug("本次清理内容：{}", JsonHelper.bean2Json(paths));
            FileManagerHelper.deleteFiles(paths);
        }
    }

    @Override
    public void updateReportConclusion(UpdateReportConclusionInput input) {
        ReportResult reportResult = reportDao.selectById(input.getId());
        if(reportResult == null) {
            throw new TroCloudException(TroCloudExceptionEnum.REPORT_GET_ERROR, "报告" +input.getId() + "不存在" );
        }
        if (StringUtils.isNotBlank(input.getErrorMessage())) {
            getReportFeatures(reportResult, ReportConstans.FEATURES_ERROR_MSG, input.getErrorMessage());
        }
        ReportUpdateConclusionParam param = new ReportUpdateConclusionParam();
        BeanUtils.copyProperties(input,param);
        param.setFeatures(reportResult.getFeatures());
        reportDao.updateReportConclusion(param);
    }

    /**
     * 生成报告
     * 报告更新，完成压测后的报告更新
     * 原来在场景调度那边，现在把报告放在这里面
     */
    @IntrestFor(event = "finished")
    public void doReportEvent(Event event) {
        try {
            // 等待时间 influxDB完成
            Thread.sleep(2000);
            long start = System.currentTimeMillis();
            TaskResult taskResult = (TaskResult)event.getExt();
            log.info("通知报告模块，开始生成本次压测{}-{}-{}的报告", taskResult.getSceneId(), taskResult.getTaskId(),
                taskResult.getCustomerId());
            modifyReport(taskResult);
            log.info("本次压测{}-{}-{}的报告生成时间-{}", taskResult.getSceneId(), taskResult.getTaskId(),
                taskResult.getCustomerId(), System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("【通知报告模块】处理finished事件异常={}", e.getMessage(), e);
        }
    }

    /**
     * 压测完成/结束 更新报告
     * 更新报表数据
     * V4.2.2以前版本的客户端，由cloud更新报告、场景状态
     * V4.2.2及以后版本的客户端，由web调用cloud，来更新报告、场景状态
     */
    @Transactional
    public void modifyReport(TaskResult taskResult) {
        //保存报表状态为生成中
        Long reportId = taskResult.getTaskId();
        ReportResult reportResult = reportDao.selectById(reportId);
        Boolean updateVersion = false;
        if (reportResult != null && reportResult.getCustomId() != null) {
            User user = tUserMapper.selectById(reportResult.getCustomId());
            updateVersion = (user != null && user.getVersion() != null && user.getVersion().compareTo("4.2.2") >= 0)
                    ? true : false;
            log.info("ReportId={}, CustomId={}, Version={}, CompareResult={}", reportId, reportResult.getCustomId(),
                    user != null ? user.getVersion() : null, updateVersion);
        }
        if (updateVersion) {
            UpdateStatusBean reportStatus = new UpdateStatusBean();
            reportStatus.setResultId(reportId);
            reportStatus.setPreStatus(ReportConstans.INIT_STATUS);
            reportStatus.setAfterStatus(ReportConstans.RUN_STATUS);
            int row = TReportMapper.updateReportStatus(reportStatus);
            if (row != 1) {
                log.error("报告{}状态非0", reportId);
                return;
            }
            reportResult.setStatus(ReportConstans.RUN_STATUS);
        }

        //汇总所有业务活动数据
        StatReportDTO statReport = statReport(taskResult.getSceneId(), reportId, taskResult.getCustomerId(),
            ReportConstans.ALL_BUSINESS_ACTIVITY);
        if (statReport == null) {
            log.warn("没有找到报表数据，报表生成失败。报告ID：{}", reportId);
            statReport = new StatReportDTO();
        }

        //更新报表业务活动 isConclusion 指标是否通过
        boolean isConclusion = updateReportBusinessActivity(taskResult.getSceneId(), taskResult.getTaskId(),
            taskResult.getCustomerId());

        //保存报表结果
        saveReportResult(reportResult, taskResult, statReport, isConclusion);

        if (!updateVersion) {
            UpdateStatusBean reportStatus = new UpdateStatusBean();
            reportStatus.setResultId(reportId);
            reportStatus.setPreStatus(ReportConstans.INIT_STATUS);
            reportStatus.setAfterStatus(ReportConstans.FINISH_STATUS);
            TReportMapper.updateReportStatus(reportStatus);
            //更新场景 压测引擎停止压测---> 待启动  版本不一样，关闭不一样
            sceneManageService.updateSceneLifeCycle(
                    UpdateStatusBean.build(reportResult.getSceneId(), reportResult.getId(), reportResult.getCustomId()).checkEnum(
                            SceneManageStatusEnum.STOP).updateEnum(SceneManageStatusEnum.WAIT).build());
        }

    }

    /**
     * 报表数据统计
     *
     * @param sceneId     场景ID
     * @param reportId    报表ID
     * @param customerId  顾客ID
     * @param transaction 业务活动
     * @return
     */
    private StatReportDTO statReport(Long sceneId, Long reportId, Long customerId, String transaction) {
        StringBuffer influxDBSQL = new StringBuffer();
        influxDBSQL.append("select");
        influxDBSQL.append(
            " sum(count) as totalRequest, sum(fail_count) as failRequest, mean(avg_tps) as tps ,mean(avg_rt) as  "
                + "sumRt, sum(sa_count) as saCount,  max(avg_tps) as maxTps, min(min_rt) as minRt, max(max_rt) as "
                + "maxRt, count(sum_rt) as recordCount ,mean(active_threads) as avgConcurrenceNum");
        influxDBSQL.append(" from ");
        influxDBSQL.append(InfluxDBUtil.getMeasurement(sceneId, reportId, customerId));
        influxDBSQL.append(" where ");
        influxDBSQL.append(" transaction = ").append("'").append(transaction).append("'");

        return influxWriter.querySingle(influxDBSQL.toString(), StatReportDTO.class);
    }

    /**
     * 更新报表业务活动并且判断是否满足业务指标
     *
     * @return
     */
    private boolean updateReportBusinessActivity(Long sceneId, Long reportId, Long customerId) {
        //报表活动
        List<ReportBusinessActivityDetail> reportBusinessActivityDetails = TReportBusinessActivityDetailMapper
            .queryReportBusinessActivityDetailByReportId(reportId);

        //业务活动是否匹配
        boolean totalPassFlag = true;
        boolean passFlag = true;
        String tableName = InfluxDBUtil.getMeasurement(sceneId, reportId, customerId);
        for (ReportBusinessActivityDetail reportBusinessActivityDetail : reportBusinessActivityDetails) {
            if (StringUtils.isBlank(reportBusinessActivityDetail.getBindRef())) {
                continue;
            }
            //统计某个业务活动的数据
            StatReportDTO data = statReport(sceneId, reportId, customerId,
                reportBusinessActivityDetail.getBindRef());
            if (data == null) {
                log.warn("没有找到匹配的压测数据：场景ID[{}],报告ID:[{}],业务活动:[{}]", sceneId, reportId,
                    reportBusinessActivityDetail.getBindRef());
                continue;
            }
            //统计RT分布
            Map<String, String> rtMap = reportEventService.queryAndCalcRtDistribute(tableName,
                reportBusinessActivityDetail.getBindRef());
            //匹配报告业务的活动
            reportBusinessActivityDetail.setAvgConcurrenceNum(data.getAvgConcurrenceNum());
            reportBusinessActivityDetail.setMaxRt(data.getMaxRt());
            reportBusinessActivityDetail.setMaxTps(data.getMaxTps());
            reportBusinessActivityDetail.setMinRt(data.getMinRt());
            reportBusinessActivityDetail.setTps(data.getTps());
            reportBusinessActivityDetail.setRt(data.getAvgRt());
            reportBusinessActivityDetail.setSa(data.getSa());
            reportBusinessActivityDetail.setRequest(data.getTotalRequest());
            reportBusinessActivityDetail.setSuccessRate(data.getSuccessRate());
            if (MapUtils.isNotEmpty(rtMap)) {
                reportBusinessActivityDetail.setRtDistribute(JSON.toJSONString(rtMap));
            }
            passFlag = isPass(reportBusinessActivityDetail);
            reportBusinessActivityDetail.setPassFlag(passFlag ? 1 : 0);
            TReportBusinessActivityDetailMapper.updateByPrimaryKeySelective(reportBusinessActivityDetail);
            if (!passFlag) {
                totalPassFlag = false;
            }
        }
        return totalPassFlag;
    }

    /**
     * 活动是否满足预设指标
     * 1.目标成功率 < 实际成功率
     * 2.目标SA > 实际SA
     * 3.目标RT > 实际RT
     * 4.目标TPS < 实际TPS
     *
     * @return
     */
    private boolean isPass(ReportBusinessActivityDetail detail) {
        if (detail.getTargetSuccessRate().compareTo(detail.getSuccessRate()) > 0) {
            return false;
        } else if (detail.getTargetSa().compareTo(detail.getSa()) > 0) {
            return false;
        } else if (detail.getTargetRt().compareTo(detail.getRt()) < 0) {
            return false;
        } else if (detail.getTargetTps().compareTo(detail.getTps()) > 0) {
            return false;
        }
        return true;
    }

    private void getRedisInfo(ReportResult reportResult, TaskResult taskResult) {
        // pod 启动情况
        String podName = ScheduleConstants.getPodName(taskResult.getSceneId(), taskResult.getTaskId(),
            taskResult.getCustomerId());
        String podTotalName = ScheduleConstants.getPodTotal(taskResult.getSceneId(), taskResult.getTaskId(),
            taskResult.getCustomerId());
        String podTotal = redisClientUtils.getString(podTotalName);
        if (!podTotal.equals(redisClientUtils.getObject(podName))) {
            // 两者不同
            getReportFeatures(reportResult, ReportConstans.PRESSURE_MSG,
                    String.format("pod计划启动{}个，实际启动{}个", podTotal, redisClientUtils.getObject(podName)));
        }
        // 压测引擎
        String engineName = ScheduleConstants.getEngineName(taskResult.getSceneId(), taskResult.getTaskId(),
            taskResult.getCustomerId());
        if (CollectorService.events.get(engineName) == null || !podTotal.equals(
            CollectorService.events.get(engineName).toString())) {
            // 两者不同
            getReportFeatures(reportResult, ReportConstans.PRESSURE_MSG,
                    String.format("压测引擎计划运行{}个，实际运行{}个", podTotal, redisClientUtils.getObject(engineName)));
        }

        // startTime endTime 补充
        if (redisClientUtils.hasKey(engineName + ScheduleConstants.FIRST_SIGN)) {
            Long startTime = Long.valueOf(redisClientUtils.getString(engineName + ScheduleConstants.FIRST_SIGN));
            reportResult.setStartTime(new Date(startTime));
        }

        //Long.valueOf(redisClientUtils.getString(engineName + ScheduleConstants.FIRST_SIGN));
        Long endTime = System.currentTimeMillis();
        if (redisClientUtils.hasKey(engineName + ScheduleConstants.LAST_SIGN)) {
            endTime = Long.valueOf(redisClientUtils.getString(engineName + ScheduleConstants.LAST_SIGN));
        }
        // metric 数据是从事件中获取
        reportResult.setEndTime(new Date(endTime));
        // 删除缓存
        redisClientUtils.del(podName, podTotalName, ScheduleConstants.TEMP_FAIL_SIGN + engineName,
            engineName + ScheduleConstants.FIRST_SIGN, engineName + ScheduleConstants.LAST_SIGN);
        CollectorService.events.remove(engineName);
    }

    /**
     * 保存报表结果
     */
    public void saveReportResult(ReportResult reportResult, TaskResult taskResult, StatReportDTO statReport,
                                 boolean isConclusion) {
        //SLA规则优先  TODO 目前不会调用到这个模块，之后进行修改
        if (taskResult.getExtendMap() != null && taskResult.getExtendMap().get(Constants.SLA_DESTORY_EXTEND) != null) {
            reportResult.setConclusion(ReportConstans.FAIL);
            getReportFeatures(reportResult, ReportConstans.FEATURES_ERROR_MSG, "触发SLA终止规则");
        } else if (!isConclusion) {
            reportResult.setConclusion(ReportConstans.FAIL);
            getReportFeatures(reportResult, ReportConstans.FEATURES_ERROR_MSG, "业务活动指标不达标");
        } else {
            reportResult.setConclusion(ReportConstans.PASS);
        }

        // 这里 要判断下 pod 情况，并记录下来 pod 最后一位就是 pod 数量 开始时间 结束时间更新
        getRedisInfo(reportResult, taskResult);

        //链路通知存在一定耗时，如果大于预设值，则置为预设值
        SceneManageWrapperOutput sceneManage = sceneManageService.getSceneManage(reportResult.getSceneId(),
            new SceneManageQueryOpitons());
        Long totalTestTime = sceneManage.getTotalTestTime();
        Date curDate = new Date();
        Long testRunTime = DateUtil.getUntilSecond(reportResult.getStartTime(), curDate);
        if (testRunTime > totalTestTime) {
            curDate = DateUtils.addSeconds(reportResult.getStartTime(), totalTestTime.intValue());
        }

        //保存报表数据
        reportResult.setTotalRequest(statReport.getTotalRequest());
        // 保留
        reportResult.setAvgRt(statReport.getAvgRt());
        reportResult.setAvgTps(statReport.getTps());
        reportResult.setSuccessRate(statReport.getSuccessRate());
        reportResult.setSa(statReport.getSa());
        reportResult.setId(reportResult.getId());
        reportResult.setEndTime(curDate);
        reportResult.setGmtUpdate(new Date());
        reportResult.setAvgConcurrent(statReport.getAvgConcurrenceNum());

        //流量结算
        AccountTradeRequest accountTradeRequest = new AccountTradeRequest();
        accountTradeRequest.setPressureTotalTime(testRunTime > totalTestTime ? totalTestTime : testRunTime);
        accountTradeRequest.setPressureMode(sceneManage.getPressureMode());
        accountTradeRequest.setIncreasingTime(sceneManage.getIncreasingSecond());
        accountTradeRequest.setPressureType(sceneManage.getPressureType());
        accountTradeRequest.setTaskId(reportResult.getId());
        accountTradeRequest.setSceneId(reportResult.getSceneId());
        accountTradeRequest.setUid(reportResult.getCustomId());
        accountTradeRequest.setStep(sceneManage.getStep());
        accountTradeRequest.setAvgConcurrent(statReport.getAvgConcurrenceNum());
        if (statReport.getTps() != null) {
            accountTradeRequest.setExpectThroughput((statReport.getTps().divide(new BigDecimal("1000"), 2,
                RoundingMode.FLOOR).multiply(statReport.getAvgRt())).intValue() + 1);
        } else {
            accountTradeRequest.setExpectThroughput(1);
        }
        log.info("流量结算：{}", JSON.toJSONString(accountTradeRequest));
        BigDecimal amount = settleService.settle(accountTradeRequest);
        reportResult.setAmount(amount);
        // 更新
        ReportUpdateParam param = new ReportUpdateParam();
        BeanUtils.copyProperties(reportResult,param);
        reportDao.updateReport(param);
    }

    @Override
    public void addWarn(WarnCreateInput input) {
        WarnDetail warnDetail = new WarnDetail();
        BeanUtils.copyProperties(input, warnDetail);
        warnDetail.setWarnTime(DateUtil.getDate(input.getWarnTime()));
        warnDetail.setCreateTime(new Date());
        TWarnDetailMapper.insertSelective(warnDetail);
    }
}
