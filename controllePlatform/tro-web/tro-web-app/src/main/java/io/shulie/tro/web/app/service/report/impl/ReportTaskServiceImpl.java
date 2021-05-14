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

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.alibaba.fastjson.JSON;

import com.google.common.collect.Lists;
import io.shulie.tro.cloud.common.redis.RedisClientUtils;
import io.shulie.tro.cloud.open.req.report.UpdateReportConclusionReq;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.web.app.constant.WebRedisKeyConstant;
import io.shulie.tro.web.app.service.report.ReportService;
import io.shulie.tro.web.app.service.report.ReportTaskService;
import io.shulie.tro.web.app.service.risk.ProblemAnalysisService;
import io.shulie.tro.web.common.domain.WebResponse;
import io.shulie.tro.web.data.dao.leakverify.LeakVerifyResultDAO;
import io.shulie.tro.web.diff.api.scenetask.SceneTaskApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName ReportTaskServiceImpl
 * @Description 1、查询生成中状态的报告（只取一条）
 * 2、更新报告状态为锁定
 * 3、客户端生成报告内容
 * - 瓶颈接口
 * - 风险机器
 * - 容量水位
 * 4、更新报告状态为已完成
 *
 * 配合压测实况：容量水位
 * 1、压测中生成报告，执行 机器列表 tps汇总图 机器统计
 * 2、压测中时，别忘记解锁
 * @Author qianshui
 * @Date 2020/7/28 上午10:59
 */
@Service
@Slf4j
public class ReportTaskServiceImpl implements ReportTaskService {

    private static final String MYSQL_UNIQUE_ERROR = "MySQLIntegrityConstraintViolationException: Duplicate entry";

    private static AtomicBoolean RUNNINT = new AtomicBoolean(false);

    @Autowired
    private ReportDataCache reportDataCache;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ProblemAnalysisService problemAnalysisService;

    @Autowired
    private SummaryService summaryService;

    @Autowired
    private ReportClearService reportClearService;

    @Autowired
    private RedisClientUtils redisClientUtils;

    @Autowired
    private LeakVerifyResultDAO leakVerifyResultDAO;

    @Autowired
    private SceneTaskApi sceneTaskApi;

    @Override
    public void queryReport() {
        try {
            if (RUNNINT.get()) {
                return;
            }
            if (!RUNNINT.compareAndSet(false, true)) {
                return;
            }
            List<Object> reportIds = Lists.newArrayList();
            WebResponse runningResponse = reportService.queryListRunningReport();
            if (runningResponse.getSuccess() == true && runningResponse.getData() != null) {
                reportIds.addAll((List)runningResponse.getData());
            }
            for (int i = 0; i < reportIds.size(); i++) {
                Long reportId = Long.valueOf(reportIds.get(i).toString());
                //Ready 数据准备
                reportDataCache.readyCloudReportData(reportId);
                // 压测结束才锁报告
                Date endTime = null;
                if(reportDataCache.getReportDetail() != null) {
                    endTime =  reportDataCache.getReportDetail().getEndTime();
                }
                try {
                    if(endTime != null) {
                        WebResponse lockResponse = reportService.lockReport(reportId);
                        if (!lockResponse.getSuccess() || lockResponse.getData() == null || !((Boolean)lockResponse
                            .getData())) {
                            log.info("Lock Running Report Data Failure, reportId={}...", reportId);
                            //return;
                        }
                        log.info("压测结束，lock Running Report success :{}", reportId);
                        log.info("压测结束，数据汇总 Running Report :{}", reportId);
                    }
                    Long startTime = System.currentTimeMillis();
                    log.info("收集 Running Report 数据:{}", reportId);
                    //first 同步应用基础信息
                    problemAnalysisService.syncMachineData(reportId);
                    if (endTime != null) {
                        // 检查风险机器
                        problemAnalysisService.checkRisk(reportId);
                        // 瓶颈处理
                        problemAnalysisService.processBottleneck(reportId);
                    }
                    //then tps指标图
                    summaryService.calcTpsTarget(reportId);
                    //then 汇总应用 机器数 风险机器数
                    summaryService.calcApplicationSummary(reportId);

                    if (endTime != null) {
                        //then 报告汇总接口
                        summaryService.calcReportSummay(reportId);
                        //last step
                        WebResponse webResponse = reportService.finishReport(reportId);
                        if(!webResponse.getSuccess() || !(Boolean)webResponse.getData()) {
                            log.info("压测结束失败 Report :{}，cloud更新失败", reportId);
                            //throw new TroWebException(ExceptionCode.REPORT_FINISH_ERROR,"cloud更新失败");
                        }
                        Boolean isLeaked = leakVerifyResultDAO.querySceneIsLeaked(reportId);
                        if (isLeaked) {
                            //存在漏数，压测失败，修改压测报告状态 1：通过 0：不通过
                            log.info("存在漏数，压测失败，修改压测报告状态:[{}]", reportId);
                            UpdateReportConclusionReq conclusionReq = new UpdateReportConclusionReq();
                            conclusionReq.setId(reportId);
                            conclusionReq.setConclusion(0);
                            conclusionReq.setErrorMessage("存在漏数");
                            ResponseResult<String> responseResult = sceneTaskApi.updateReportStatus(conclusionReq);
                            log.info("修改压测报告的结果:[{}]", JSON.toJSONString(responseResult));
                        }
                        //删除redis数据
                        redisClientUtils.del(WebRedisKeyConstant.REPORT_WARN_PREFIX + reportId);
                        // 删除key
                        redisClientUtils.hmdelete(WebRedisKeyConstant.PTING_APPLICATION_KEY, String.valueOf(reportId));
                        log.warn("报告id={}汇总成功，花费时间={}s", reportId, (System.currentTimeMillis() - startTime) / 1000);
                    }
                } catch (Exception e) {
                    log.error("客户端生成报告id={}数据异常:{}", reportId, e.getMessage(), e);
                    //生成报告异常，清空本轮生成表数据
                    reportClearService.clearReportData(reportId);
                    //压测结束，生成压测报告异常，解锁报告
                    if (endTime != null) {
                        reportService.unLockReport(reportId);
                        log.warn("Unlock Report Success, reportId={}...", reportId);
                    }
                } finally {
                    reportDataCache.clearDataCache();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("QueryRunningReport Error :{}", e);
        } finally {
            RUNNINT.compareAndSet(true, false);
        }
    }
}
