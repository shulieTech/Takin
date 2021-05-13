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

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.pamirs.tro.common.constant.VerifyResultStatusEnum;
import com.pamirs.tro.common.constant.VerifyTypeEnum;
import com.pamirs.tro.entity.domain.dto.scenemanage.SceneBusinessActivityRefDTO;
import com.pamirs.tro.entity.domain.dto.scenemanage.SceneManageWrapperDTO;
import io.shulie.tro.cloud.open.req.scenemanage.SceneManageIdReq;
import io.shulie.tro.cloud.open.resp.scenetask.SceneActionResp;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.web.app.exception.ExceptionCode;
import io.shulie.tro.web.app.exception.TroWebException;
import io.shulie.tro.web.app.request.leakcheck.LeakSqlBatchRefsRequest;
import io.shulie.tro.web.app.request.leakverify.LeakVerifyTaskJobParameter;
import io.shulie.tro.web.app.request.leakverify.LeakVerifyTaskRunAssembleRequest;
import io.shulie.tro.web.app.request.leakverify.LeakVerifyTaskRunWithSaveRequest;
import io.shulie.tro.web.app.request.leakverify.LeakVerifyTaskRunWithoutSaveRequest;
import io.shulie.tro.web.app.request.leakverify.LeakVerifyTaskStartRequest;
import io.shulie.tro.web.app.request.leakverify.LeakVerifyTaskStopRequest;
import io.shulie.tro.web.app.request.leakverify.VerifyTaskConfig;
import io.shulie.tro.web.app.response.leakverify.LeakVerifyDetailResponse;
import io.shulie.tro.web.app.response.leakverify.LeakVerifyDsResultResponse;
import io.shulie.tro.web.app.response.leakverify.LeakVerifyTaskResultResponse;
import io.shulie.tro.web.app.service.LeakSqlService;
import io.shulie.tro.web.app.service.VerifyTaskService;
import io.shulie.tro.web.app.service.elasticjoblite.CoordinatorRegistryCenterService;
import io.shulie.tro.web.app.service.elasticjoblite.VerifyJob;
import io.shulie.tro.web.app.service.scenemanage.SceneManageService;
import io.shulie.tro.web.common.vo.component.SelectVO;
import io.shulie.tro.web.data.dao.leakverify.LeakVerifyDetailDAO;
import io.shulie.tro.web.data.dao.leakverify.LeakVerifyResultDAO;
import io.shulie.tro.web.data.param.leakverify.LeakVerifyDetailCreateParam;
import io.shulie.tro.web.data.param.leakverify.LeakVerifyResultCreateParam;
import io.shulie.tro.web.diff.api.scenetask.SceneTaskApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @Author: fanxx
 * @Date: 2021/1/5 3:06 下午
 * @Description:
 */
@Slf4j
@Component
public class VerifyTaskServiceImpl implements VerifyTaskService {

    public static Map<String, JobScheduler> serviceMap = Maps.newConcurrentMap();

    @Autowired
    private LeakVerifyResultDAO verifyResultDAO;

    @Autowired
    private LeakVerifyDetailDAO verifyDetailDAO;

    @Autowired
    private LeakSqlService leakSqlService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private CoordinatorRegistryCenterService registryCenterService;

    @Autowired
    private SceneTaskApi sceneTaskApi;

    @Autowired
    private SceneManageService sceneManageService;

    @Scheduled(cron = "0/10 * *  * * ?")
    public void showdownVerifyTask() {
        if (!serviceMap.isEmpty()) {
            Set<String> keySet = serviceMap.keySet();
            keySet.forEach(mapKey -> {
                String tmpRefId = mapKey.split("\\$")[1];
                Long sceneId = Long.parseLong(tmpRefId);
                SceneManageIdReq req = new SceneManageIdReq();
                req.setId(sceneId);
                ResponseResult<SceneActionResp> response = sceneTaskApi.checkTask(req);
                if (!Objects.isNull(response.getData())) {
                    SceneActionResp resp = JSONObject.parseObject(JSON.toJSONString(response.getData()),
                        SceneActionResp.class);
                    Long status = resp.getData();
                    //停止状态
                    if (0L == status) {
                        log.info("压测场景已停止，关闭验证任务，场景ID[{}]", sceneId);
                        JobScheduler jobScheduler = serviceMap.get(mapKey);
                        jobScheduler.getSchedulerFacade().shutdownInstance();
                        serviceMap.remove(mapKey);
                        //漏数验证兜底检测
                        log.info("漏数验证兜底检测，场景ID[{}]", sceneId);
                        LeakVerifyTaskRunWithSaveRequest runRequest = new LeakVerifyTaskRunWithSaveRequest();
                        runRequest.setRefType(VerifyTypeEnum.SCENE.getCode());
                        runRequest.setRefId(sceneId);
                        runRequest.setReportId(resp.getReportId());
                        this.runWithResultSave(runRequest);
                    } else {
                        log.debug("压测场景仍在运行，无法关闭验证任务，状态:[{}]" + status);
                    }
                } else {
                    log.error("cloud返回的数据为空，无法判断压测场景状态");
                }
            });
        }
    }

    @Override
    public void start(LeakVerifyTaskStartRequest startRequest) {
        LeakVerifyTaskStopRequest stopRequest = new LeakVerifyTaskStopRequest();
        stopRequest.setRefType(startRequest.getRefType());
        stopRequest.setRefId(startRequest.getRefId());
        this.stop(stopRequest);

        LeakVerifyTaskJobParameter jobParameterObject = new LeakVerifyTaskJobParameter();
        jobParameterObject.setRefType(startRequest.getRefType());
        jobParameterObject.setRefId(startRequest.getRefId());
        jobParameterObject.setTimeInterval(startRequest.getTimeInterval());
        jobParameterObject.setReportId(startRequest.getReportId());
        LeakSqlBatchRefsRequest refsRequest = new LeakSqlBatchRefsRequest();
        refsRequest.setBusinessActivityIds(startRequest.getBusinessActivityIds());
        List<VerifyTaskConfig> verifyTaskConfigList = leakSqlService.getVerifyTaskConfig(refsRequest);
        if (CollectionUtils.isEmpty(verifyTaskConfigList)) {
            throw new TroWebException(ExceptionCode.VERIFY_TASK_RUN_FAILED, "该业务活动暂未配置漏数脚本");
        }
        jobParameterObject.setVerifyTaskConfigList(verifyTaskConfigList);
        String jobParameter = JSON.toJSONString(jobParameterObject);
        JobScheduler jobScheduler = new JobScheduler(registryCenterService.getRegistryCenter(),
            createJobConfiguration(jobParameter));
        jobScheduler.init();
        String mapKey = startRequest.getRefType() + "$" + startRequest.getRefId();
        serviceMap.put(mapKey, jobScheduler);
    }

    private LiteJobConfiguration createJobConfiguration(String jobParameter) {
        LeakVerifyTaskJobParameter jobParameterObjecct = JSON.parseObject(jobParameter,
            LeakVerifyTaskJobParameter.class);
        String jobName = "验证任务$" + jobParameterObjecct.getRefType() + "$" + jobParameterObjecct.getRefId();
        int second = Calendar.getInstance().get(Calendar.SECOND);
        int interval = jobParameterObjecct.getTimeInterval();
        String cron = second + " */" + interval + " * * * ?";
        log.info(jobName + ",cron表达式:[{}]", cron);
        JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder(jobName, cron, 1).jobParameter(
            jobParameter).build();
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig,
            VerifyJob.class.getCanonicalName());
        return LiteJobConfiguration.newBuilder(simpleJobConfig).overwrite(Boolean.TRUE).build();
    }

    @Override
    public void stop(LeakVerifyTaskStopRequest stopRequest) {
        //关闭验证任务线程池
        Integer refType = stopRequest.getRefType();
        Long refId = stopRequest.getRefId();
        String mapKey = refType + "$" + refId;
        if (serviceMap.containsKey(mapKey)) {
            JobScheduler scheduler = serviceMap.get(mapKey);
            log.info("开始关闭验证任务:[{},{}]",
                Objects.requireNonNull(VerifyTypeEnum.getTypeByCode(stopRequest.getRefType())).name(),
                stopRequest.getRefId());
            scheduler.getSchedulerFacade().shutdownInstance();
            serviceMap.remove(mapKey);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("error:", e);
            }
            log.info("验证任务已关闭:[{},{}]",
                Objects.requireNonNull(VerifyTypeEnum.getTypeByCode(stopRequest.getRefType())).name(),
                stopRequest.getRefId());
        } else {
            log.debug("验证任务不存在:[{},{}]",
                Objects.requireNonNull(VerifyTypeEnum.getTypeByCode(stopRequest.getRefType())).name(),
                stopRequest.getRefId());
        }
    }

    public LeakVerifyTaskResultResponse run(LeakVerifyTaskRunAssembleRequest runAssembleRequest, Boolean isSaveResult) {
        VerifyJob verifyJob = new VerifyJob();
        LeakVerifyTaskJobParameter jobParameter = new LeakVerifyTaskJobParameter();
        Integer refType = runAssembleRequest.getRefType();
        Long refId = runAssembleRequest.getRefId();
        List<Long> businessActivityIds = runAssembleRequest.getBusinessActivityIds();
        jobParameter.setRefType(refType);
        jobParameter.setRefId(refId);
        jobParameter.setReportId(runAssembleRequest.getReportId());

        LeakSqlBatchRefsRequest refsRequest = new LeakSqlBatchRefsRequest();
        refsRequest.setBusinessActivityIds(businessActivityIds);
        List<VerifyTaskConfig> verifyTaskConfigList = leakSqlService.getVerifyTaskConfig(refsRequest);
        if (CollectionUtils.isEmpty(verifyTaskConfigList)) {
            throw new TroWebException(ExceptionCode.VERIFY_TASK_RUN_FAILED, "该业务活动暂未配置漏数脚本");
        }
        jobParameter.setVerifyTaskConfigList(verifyTaskConfigList);
        Map<Integer, Integer> resultMap = verifyJob.run(jobParameter);
        if (isSaveResult) {
            //验证结果入库
            saveVerifyResult(jobParameter, resultMap);
            log.info("兜底验证结果入库:[{},{}]", Objects.requireNonNull(VerifyTypeEnum.getTypeByCode(refType)).name(), refId);
            return null;
        } else {
            //组装验证结果
            LeakVerifyTaskResultResponse taskResultResponse = new LeakVerifyTaskResultResponse();
            taskResultResponse.setRefType(refType);
            taskResultResponse.setRefId(refId);
            taskResultResponse.setStatus(VerifyResultStatusEnum.UNCHECK.getCode());
            List<LeakVerifyDsResultResponse> dsResultResponseList = Lists.newArrayList();
            verifyTaskConfigList.forEach(verifyTaskConfig -> {
                LeakVerifyDsResultResponse dsResultResponse = new LeakVerifyDsResultResponse();
                dsResultResponse.setDatasourceId(verifyTaskConfig.getDatasourceId());
                dsResultResponse.setDatasourceName(verifyTaskConfig.getDatasourceName());
                dsResultResponse.setJdbcUrl(verifyTaskConfig.getJdbcUrl());

                List<String> sqlList = verifyTaskConfig.getSqls();
                List<LeakVerifyDetailResponse> detailResponseList = Lists.newArrayList();
                for (int i = 0; i < sqlList.size(); i++) {
                    String sql = sqlList.get(i);
                    String keyString = refType + refId + verifyTaskConfig.getDatasourceId() + sql;
                    Integer mapKey = keyString.hashCode();
                    LeakVerifyDetailResponse detailResponse = new LeakVerifyDetailResponse();
                    detailResponse.setOrder(i);
                    detailResponse.setSql(sql);
                    if (resultMap.containsKey(mapKey)) {
                        Integer count = resultMap.get(mapKey);
                        switch (count) {
                            case 0:
                                detailResponse.setStatus(VerifyResultStatusEnum.NORMAL.getCode());
                                break;
                            case 1:
                                detailResponse.setStatus(VerifyResultStatusEnum.LEAKED.getCode());
                                break;
                            case 3:
                                detailResponse.setStatus(VerifyResultStatusEnum.FAILED.getCode());
                            default:
                        }
                    } else {
                        detailResponse.setStatus(VerifyResultStatusEnum.UNCHECK.getCode());
                    }
                    SelectVO vo = new SelectVO();
                    vo.setValue(detailResponse.getStatus().toString());
                    vo.setLabel(VerifyResultStatusEnum.getLabelByCode(detailResponse.getStatus()));
                    detailResponse.setStatusResponse(vo);
                    detailResponseList.add(detailResponse);
                }
                dsResultResponse.setDetailResponseList(detailResponseList);
                Map<Integer, List<LeakVerifyDetailResponse>> groupDetailResultMap
                    = detailResponseList.stream().collect(Collectors.groupingBy(LeakVerifyDetailResponse::getStatus));

                if (groupDetailResultMap.containsKey(VerifyResultStatusEnum.LEAKED.getCode())) {
                    dsResultResponse.setStatus(VerifyResultStatusEnum.LEAKED.getCode());
                } else if (groupDetailResultMap.containsKey(VerifyResultStatusEnum.FAILED.getCode())) {
                    dsResultResponse.setStatus(VerifyResultStatusEnum.FAILED.getCode());
                } else if (groupDetailResultMap.containsKey(VerifyResultStatusEnum.UNCHECK.getCode())) {
                    dsResultResponse.setStatus(VerifyResultStatusEnum.UNCHECK.getCode());
                } else {
                    dsResultResponse.setStatus(VerifyResultStatusEnum.NORMAL.getCode());
                }
                SelectVO vo = new SelectVO();
                vo.setValue(dsResultResponse.getStatus().toString());
                vo.setLabel(VerifyResultStatusEnum.getLabelByCode(dsResultResponse.getStatus()));
                dsResultResponse.setStatusResponse(vo);
                dsResultResponseList.add(dsResultResponse);
            });
            Map<Integer, List<LeakVerifyDsResultResponse>> groupDsResultMap
                = dsResultResponseList.stream().collect(Collectors.groupingBy(LeakVerifyDsResultResponse::getStatus));
            if (groupDsResultMap.containsKey(VerifyResultStatusEnum.LEAKED.getCode())) {
                taskResultResponse.setStatus(VerifyResultStatusEnum.LEAKED.getCode());
            } else if (groupDsResultMap.containsKey(VerifyResultStatusEnum.FAILED.getCode())) {
                taskResultResponse.setStatus(VerifyResultStatusEnum.FAILED.getCode());
            } else if (groupDsResultMap.containsKey(VerifyResultStatusEnum.UNCHECK.getCode())) {
                taskResultResponse.setStatus(VerifyResultStatusEnum.UNCHECK.getCode());
            } else {
                taskResultResponse.setStatus(VerifyResultStatusEnum.NORMAL.getCode());
            }
            taskResultResponse.setDsResultResponseList(dsResultResponseList);
            Integer taskStatus = taskResultResponse.getStatus();
            SelectVO vo = new SelectVO();
            vo.setValue(taskStatus.toString());
            vo.setLabel(VerifyResultStatusEnum.getLabelByCode(taskStatus));
            taskResultResponse.setStatusResponse(vo);
            return taskResultResponse;
        }
    }

    @Override
    public LeakVerifyTaskResultResponse runWithoutResultSave(LeakVerifyTaskRunWithoutSaveRequest runRequest) {
        LeakVerifyTaskRunAssembleRequest runAssembleRequest = new LeakVerifyTaskRunAssembleRequest();
        runAssembleRequest.setRefType(VerifyTypeEnum.ACTIVITY.getCode());
        runAssembleRequest.setRefId(runRequest.getBusinessActivityId());
        runAssembleRequest.setBusinessActivityIds(Arrays.asList(runRequest.getBusinessActivityId()));
        return this.run(runAssembleRequest, Boolean.FALSE);
    }

    @Override
    public LeakVerifyTaskResultResponse runWithResultSave(LeakVerifyTaskRunWithSaveRequest runRequest) {
        LeakVerifyTaskRunAssembleRequest runAssembleRequest = new LeakVerifyTaskRunAssembleRequest();
        runAssembleRequest.setRefType(runRequest.getRefType());
        runAssembleRequest.setRefId(runRequest.getRefId());
        runAssembleRequest.setReportId(runRequest.getReportId());
        if (runRequest.getRefType().equals(VerifyTypeEnum.SCENE.getCode())) {
            Long sceneId = runRequest.getRefId();
            ResponseResult webResponse = sceneManageService.detailScene(sceneId);
            SceneManageWrapperDTO sceneData = JSON.parseObject(JSON.toJSONString(webResponse.getData()),
                SceneManageWrapperDTO.class);
            List<Long> businessActivityIds =
                sceneData.getBusinessActivityConfig().stream().map(
                    SceneBusinessActivityRefDTO::getBusinessActivityId).collect(
                    Collectors.toList());
            runAssembleRequest.setBusinessActivityIds(businessActivityIds);
        } else if (runRequest.getRefType().equals(VerifyTypeEnum.FLOW.getCode())) {
            // TODO 暂不支持业务流程的漏数验证
        } else {
            runAssembleRequest.setBusinessActivityIds(Arrays.asList(runRequest.getRefId()));
        }
        return this.run(runAssembleRequest, Boolean.TRUE);
    }

    @Override
    public void saveVerifyResult(LeakVerifyTaskJobParameter jobParameter, Map<Integer, Integer> resultMap) {
        if (resultMap.isEmpty()) {
            log.warn("验证结果为空，保存漏数结果失败:[{},{}]", Objects
                    .requireNonNull(VerifyTypeEnum.getTypeByCode(jobParameter.getRefType())).name(),
                jobParameter.getRefId());
            return;
        }
        transactionTemplate.execute((s) -> {
            Long refId = jobParameter.getRefId();
            Integer refType = jobParameter.getRefType();
            Long reportId = jobParameter.getReportId();
            List<VerifyTaskConfig> verifyTaskConfigList = jobParameter.getVerifyTaskConfigList();
            verifyTaskConfigList.forEach(verfiyTaskConfig -> {
                Long datasourceId = verfiyTaskConfig.getDatasourceId();
                // 生成验证结果基础信息
                LeakVerifyResultCreateParam resultCreateParam = new LeakVerifyResultCreateParam();
                resultCreateParam.setRefType(refType);
                resultCreateParam.setRefId(refId);
                resultCreateParam.setReportId(reportId);
                resultCreateParam.setDbresourceId(verfiyTaskConfig.getDatasourceId());
                resultCreateParam.setDbresourceName(verfiyTaskConfig.getDatasourceName());
                resultCreateParam.setDbresourceUrl(verfiyTaskConfig.getJdbcUrl());
                //后续定义一个枚举类：是否漏数 0:正常;1:漏数;2:未检测;3:检测失败
                Long resultId = verifyResultDAO.insert(resultCreateParam);
                List<String> sqlList = verfiyTaskConfig.getSqls();
                List<LeakVerifyDetailCreateParam> detailCreateParamList = Lists.newArrayList();
                //验证结果详情入库
                sqlList.forEach(sql -> {
                    LeakVerifyDetailCreateParam detailCreateParam = new LeakVerifyDetailCreateParam();
                    detailCreateParam.setResultId(resultId);
                    detailCreateParam.setLeakSql(sql);
                    String keyString = refType + refId + datasourceId + sql;
                    Integer sqlKey = keyString.hashCode();
                    if (resultMap.containsKey(sqlKey)) {
                        Integer count = resultMap.get(sqlKey);
                        if (VerifyResultStatusEnum.LEAKED.getCode().equals(count)) {
                            SceneManageIdReq queryReq = new SceneManageIdReq();
                            queryReq.setId(refId);
                            ResponseResult<SceneActionResp> response = sceneTaskApi.checkTask(queryReq);
                            if (!Objects.isNull(response.getData())) {
                                SceneActionResp resp = JSONObject.parseObject(JSON.toJSONString(response.getData()),
                                    SceneActionResp.class);
                                Long status = resp.getData();
                                if (2L == status) {
                                    //如果压测场景正在运行则立刻停止压测任务
                                    SceneManageIdReq stopReq = new SceneManageIdReq();
                                    stopReq.setId(refId);
                                    sceneTaskApi.stopTask(stopReq);
                                    log.warn("存在漏数，触发SLA终止压测，sceneId={}, sql={}", refId, sql);
                                }
                            }
                            detailCreateParam.setStatus(VerifyResultStatusEnum.LEAKED.getCode());
                        } else if (VerifyResultStatusEnum.FAILED.getCode().equals(count)) {
                            detailCreateParam.setStatus(VerifyResultStatusEnum.FAILED.getCode());
                        } else {
                            detailCreateParam.setStatus(VerifyResultStatusEnum.NORMAL.getCode());
                        }
                    } else {
                        detailCreateParam.setStatus(VerifyResultStatusEnum.UNCHECK.getCode());
                    }
                    detailCreateParamList.add(detailCreateParam);
                });
                verifyDetailDAO.insertBatch(detailCreateParamList);
            });
            return null;
        });
    }

    @Override
    public Set<String> queryVerifyTask() {
        Set<String> stringSet = Sets.newHashSet();
        if (!serviceMap.isEmpty()) {
            stringSet = serviceMap.keySet();
        }
        return stringSet;
    }
}
