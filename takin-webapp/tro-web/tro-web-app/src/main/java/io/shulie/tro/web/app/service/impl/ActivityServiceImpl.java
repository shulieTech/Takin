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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import com.pamirs.tro.entity.domain.entity.user.User;
import com.pamirs.tro.entity.domain.vo.scenemanage.SceneBusinessActivityRefVO;
import com.pamirs.tro.entity.domain.vo.scenemanage.SceneManageWrapperVO;
import com.pamirs.tro.entity.domain.vo.scenemanage.TimeVO;
import io.shulie.tro.cloud.common.redis.RedisClientUtils;
import io.shulie.tro.cloud.open.api.scenetask.CloudTaskApi;
import io.shulie.tro.cloud.open.req.scenemanage.SceneBusinessActivityRefOpen;
import io.shulie.tro.cloud.open.req.scenemanage.SceneManageWrapperReq;
import io.shulie.tro.cloud.open.req.scenetask.TaskFlowDebugStartReq;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.utils.string.StringUtil;
import io.shulie.tro.web.amdb.api.NotifyClient;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.constant.BizOpConstants;
import io.shulie.tro.web.app.constant.BizOpConstants.Vars;
import io.shulie.tro.web.app.constant.BusinessActivityRedisKeyConstant;
import io.shulie.tro.web.app.context.OperationLogContextHolder;
import io.shulie.tro.web.app.output.report.ReportDetailOutput;
import io.shulie.tro.web.app.request.activity.ActivityCreateRequest;
import io.shulie.tro.web.app.request.activity.ActivityQueryRequest;
import io.shulie.tro.web.app.request.activity.ActivityUpdateRequest;
import io.shulie.tro.web.app.request.activity.ActivityVerifyRequest;
import io.shulie.tro.web.app.request.application.ApplicationEntranceTopologyQueryRequest;
import io.shulie.tro.web.app.response.activity.ActivityListResponse;
import io.shulie.tro.web.app.response.activity.ActivityResponse;
import io.shulie.tro.web.app.response.activity.ActivityVerifyResponse;
import io.shulie.tro.web.app.response.scriptmanage.PluginConfigDetailResponse;
import io.shulie.tro.web.app.response.scriptmanage.ScriptManageDeployDetailResponse;
import io.shulie.tro.web.app.service.ActivityService;
import io.shulie.tro.web.app.service.LinkTopologyService;
import io.shulie.tro.web.app.service.report.ReportService;
import io.shulie.tro.web.app.service.scenemanage.SceneManageService;
import io.shulie.tro.web.app.service.scriptmanage.ScriptManageService;
import io.shulie.tro.web.auth.api.UserService;
import io.shulie.tro.web.common.constant.RemoteConstant;
import io.shulie.tro.web.common.domain.WebResponse;
import io.shulie.tro.web.common.util.ActivityUtil;
import io.shulie.tro.web.data.dao.activity.ActivityDAO;
import io.shulie.tro.web.data.param.activity.ActivityCreateParam;
import io.shulie.tro.web.data.param.activity.ActivityExistsQueryParam;
import io.shulie.tro.web.data.param.activity.ActivityQueryParam;
import io.shulie.tro.web.data.param.activity.ActivityUpdateParam;
import io.shulie.tro.web.data.result.activity.ActivityListResult;
import io.shulie.tro.web.data.result.activity.ActivityResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shiyajian
 * create: 2020-12-30
 */
@Slf4j
@Component
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    RedisClientUtils redisClientUtils;
    @Autowired
    private NotifyClient notifyClient;
    @Autowired
    private ActivityDAO activityDAO;
    @Autowired
    private UserService userService;
    @Autowired
    private LinkTopologyService linkTopologyService;
    @Autowired
    private ReportService reportService;
    @Value("${link.flow.check.enable:false}")
    private Boolean enableLinkFlowCheck;
    @Autowired
    private CloudTaskApi cloudTaskApi;
    @Autowired
    private ScriptManageService scriptManageService;

    @Autowired
    private SceneManageService sceneManageService;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void createActivity(ActivityCreateRequest request) {

        ActivityExistsQueryParam param = new ActivityExistsQueryParam();
        param.setType(request.getType());
        param.setActivityName(request.getActivityName());
        List<Long> exists = activityDAO.exists(param);
        if (CollectionUtils.isNotEmpty(exists)) {
            throw new RuntimeException(String
                .format("保存失败，[名称:%s] 已被使用", request.getActivityName()));
        }

        param = new ActivityExistsQueryParam();
        param.setType(request.getType());
        param.setEntranceName(request.getServiceName());
        param.setApplicationName(request.getApplicationName());
        param.setExtend(request.getExtend());
        param.setMethod(request.getMethod());
        param.setRpcType(request.getRpcType());
        param.setServiceName(request.getServiceName());
        exists = activityDAO.exists(param);
        if (CollectionUtils.isNotEmpty(exists)) {
            throw new RuntimeException(String
                .format("保存失败，[应用名:%s,类型:%s,入口:%s]已存在", request.getApplicationName(), request.getType().getType(),
                    request.getServiceName()));
        }
        ActivityCreateParam createParam = new ActivityCreateParam();
        createParam.setActivityName(request.getActivityName());
        createParam.setEntranceName(request.getServiceName());
        createParam.setIsChange(false);
        createParam.setApplicationName(param.getApplicationName());
        createParam.setType(param.getType());
        createParam.setUserId(RestContext.getUser().getId());
        createParam.setCustomerId(RestContext.getUser().getCustomerId());
        createParam.setActivityLevel(request.getActivityLevel());
        createParam.setIsCore(request.getIsCore());
        createParam.setBusinessDomain(request.getBusinessDomain());
        createParam.setRpcType(request.getRpcType());
        createParam.setMethod(request.getMethod());
        createParam.setServiceName(request.getServiceName());
        createParam.setExtend(request.getExtend());
        activityDAO.createActivity(createParam);
        //notifyClient.startApplicationEntrancesCalculate(request.getApplicationName(), request.getServiceName(),
        //    request.getMethod(), request.getRpcType(), request.getExtend());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void updateActivity(ActivityUpdateRequest request) {
        ActivityResult oldActivity = activityDAO.getActivityById(request.getActivityId());
        if (oldActivity == null) {
            throw new RuntimeException(String.format("修改失败，ID:[%s]对应的数据不存在", request.getActivityId()));
        }

        ActivityExistsQueryParam param = new ActivityExistsQueryParam();
        if (request.getActivityName() != null) {
            param.setActivityName(request.getActivityName());
            List<Long> exists = activityDAO.exists(param);
            if (CollectionUtils.isNotEmpty(exists)) {
                Optional<Long> any = exists.stream()
                    .filter(item -> !item.equals(request.getActivityId()))
                    .findAny();
                if (any.isPresent()) {
                    throw new RuntimeException(String
                        .format("保存失败，业务活动[%s]已被使用，对应的ID为：%s", request.getActivityName(), any.get()));
                }
            }
        }
        if (request.getServiceName() != null && request.getRpcType() != null && request.getApplicationName() != null) {
            param = new ActivityExistsQueryParam();
            param.setEntranceName(request.getServiceName());
            param.setApplicationName(request.getApplicationName());
            param.setType(request.getType());
            param.setExtend(request.getExtend());
            param.setMethod(request.getMethod());
            param.setRpcType(request.getRpcType());
            param.setServiceName(request.getServiceName());
            List<Long> exists = activityDAO.exists(param);
            if (CollectionUtils.isNotEmpty(exists)) {
                Optional<Long> any = exists.stream()
                    .filter(item -> !item.equals(request.getActivityId()))
                    .findAny();
                if (any.isPresent()) {
                    throw new RuntimeException(String
                        .format("保存失败，入口已[应用名称：%s，类型：%s，入口：%s]已被使用，对应的ID为：%s", request.getActivityName(),
                            request.getType().getType(), request.getServiceName(), any.get()));
                }
            }
        }

        OperationLogContextHolder.addVars(BizOpConstants.Vars.ENTRY_APPLICATION, request.getApplicationName());
        OperationLogContextHolder.addVars(BizOpConstants.Vars.INTERFACE, request.getServiceName());
        ActivityUpdateParam updateParam = new ActivityUpdateParam();
        updateParam.setActivityId(request.getActivityId());
        updateParam.setActivityName(request.getActivityName());
        updateParam.setApplicationName(request.getApplicationName());
        updateParam.setEntranceName(request.getServiceName());
        updateParam.setType(request.getType());
        updateParam.setUserId(RestContext.getUser().getId());
        updateParam.setCustomerId(RestContext.getUser().getCustomerId());
        updateParam.setIsChange(isChange(oldActivity, request));
        updateParam.setChangeBefore(oldActivity.getChangeAfter());
        updateParam.setActivityLevel(request.getActivityLevel());
        updateParam.setBusinessDomain(request.getBusinessDomain());
        updateParam.setIsCore(request.getIsCore());
        updateParam.setRpcType(request.getRpcType());
        updateParam.setMethod(request.getMethod());
        updateParam.setServiceName(request.getServiceName());
        updateParam.setExtend(request.getExtend());
        activityDAO.updateActivity(updateParam);

        //notifyClient.stopApplicationEntrancesCalculate(oldActivity.getApplicationName(), request.getServiceName(),
        //    request.getMethod(),
        //    request.getRpcType(), request.getExtend());
        //notifyClient.startApplicationEntrancesCalculate(request.getApplicationName(), request.getServiceName(),
        //    request.getMethod(), request.getRpcType(), request.getExtend());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteActivity(Long activityId) {
        ActivityResult oldActivity = activityDAO.getActivityById(activityId);
        if (oldActivity == null) {
            throw new RuntimeException(String.format("删除失败，ID:[%s]对应的数据不存在", activityId));

        }
        OperationLogContextHolder.operationType(BizOpConstants.OpTypes.DELETE);
        OperationLogContextHolder.addVars(BizOpConstants.Vars.BUSINESS_ACTIVITY, oldActivity.getActivityName());
        OperationLogContextHolder.addVars(Vars.ENTRANCE_TYPE, oldActivity.getType().name());
        OperationLogContextHolder.addVars(Vars.APPLICATION_NAME, oldActivity.getApplicationName());
        OperationLogContextHolder.addVars(Vars.SERVICE_NAME, oldActivity.getServiceName());
        activityDAO.deleteActivity(activityId);

        //notifyClient.stopApplicationEntrancesCalculate(oldActivity.getApplicationName(), oldActivity.getServiceName(),
        //    oldActivity.getMethod(), oldActivity.getRpcType(), oldActivity.getExtend());
    }

    @Override
    public PagingList<ActivityListResponse> pageActivities(ActivityQueryRequest request) {
        ActivityQueryParam param = new ActivityQueryParam();
        param.setActivityName(request.getActivityName());
        param.setDomain(request.getDomain());
        param.setIsChange(request.getIsChange());
        param.setCurrent(request.getCurrent());
        param.setPageSize(request.getPageSize());
        param.setUserIdList(RestContext.getQueryAllowUserIdList());
        PagingList<ActivityListResult> activityListResultPagingList = activityDAO.pageActivities(param);
        //用户ids
        List<Long> userIds = activityListResultPagingList.getList().stream()
            .map(ActivityListResult::getUserId)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        //用户信息Map key:userId  value:user对象
        Map<Long, User> userMap = userService.getUserMapByIds(userIds);

        List<ActivityListResponse> responses = activityListResultPagingList.getList().stream()
            .map(result -> {
                ActivityListResponse response = new ActivityListResponse();
                response.setActivityId(result.getActivityId());
                response.setActivityName(result.getActivityName());
                response.setIsChange(result.getIsChange());
                response.setIsCore(String.valueOf(result.getIsCore()));
                response.setIsDeleted(result.getIsDeleted());
                response.setUserId(result.getUserId());
                response.setCreateTime(result.getCreateTime());
                response.setUpdateTime(result.getUpdateTime());
                response.setCanDelete(result.getCanDelete());
                response.setBusinessDomain(result.getBusinessDomain());
                response.setActivityLevel(result.getActivityLevel());
                User user = userMap.get(result.getUserId());
                if (user != null) {
                    response.setManagerName(user.getName());
                }
                List<Long> allowUpdateUserIdList = RestContext.getUpdateAllowUserIdList();
                if (CollectionUtils.isNotEmpty(allowUpdateUserIdList)) {
                    response.setCanEdit(allowUpdateUserIdList.contains(result.getManagerId()));
                } else {
                    response.setCanEdit(true);
                }
                List<Long> allowDeleteUserIdList = RestContext.getDeleteAllowUserIdList();
                if (CollectionUtils.isNotEmpty(allowDeleteUserIdList)) {
                    response.setCanRemove(allowDeleteUserIdList.contains(result.getManagerId()));
                } else {
                    response.setCanRemove(true);
                }
                return response;
            }).collect(Collectors.toList());
        return PagingList.of(responses, activityListResultPagingList.getTotal());
    }

    @Override
    public ActivityResponse getActivityById(Long id) {
        ActivityResult result = activityDAO.getActivityById(id);
        if (result == null) {
            log.warn("查询{}对应的业务活动不存在", id);
            throw new RuntimeException(String.format("%s对应的业务活动不存在", id));
        }
        ActivityResponse activityResponse = new ActivityResponse();
        activityResponse.setActivityId(result.getActivityId());
        activityResponse.setActivityName(result.getActivityName());
        activityResponse.setApplicationName(result.getApplicationName());
        activityResponse.setEntranceName(ActivityUtil.serviceNameLabel(result.getServiceName(), result.getMethod()));
        activityResponse.setType(result.getType());
        activityResponse.setChangeBefore(result.getChangeBefore());
        activityResponse.setChangeAfter(result.getChangeAfter());
        activityResponse.setIsChange(result.getIsChange());
        activityResponse.setUserId(result.getUserId());
        if (result.getUserId() != null) {
            Map<Long, User> userMap = userService.getUserMapByIds(Arrays.asList(result.getUserId()));
            if (MapUtils.isNotEmpty(userMap)) {
                activityResponse.setManager(userMap.get(result.getUserId()).getName());
            }
        }
        activityResponse.setExtend(result.getExtend());
        activityResponse.setMethod(result.getMethod());
        activityResponse.setRpcType(result.getRpcType());
        activityResponse.setServiceName(result.getServiceName());
        activityResponse.setActivityLevel(result.getActivityLevel());
        activityResponse.setIsCore(String.valueOf(result.getIsCore()));
        activityResponse.setBusinessDomain(result.getBusinessDomain());
        activityResponse.setLinkId(ActivityUtil.createLinkId(result.getServiceName(), result.getMethod(),
            result.getApplicationName(), result.getRpcType(), result.getExtend()));
        // 拓扑图查询
        ApplicationEntranceTopologyQueryRequest request = new ApplicationEntranceTopologyQueryRequest();
        request.setApplicationName(result.getApplicationName());
        request.setLinkId(activityResponse.getLinkId());
        request.setMethod(result.getMethod());
        request.setRpcType(result.getRpcType());
        request.setExtend(result.getExtend());
        request.setServiceName(result.getServiceName());
        request.setType(result.getType());
        activityResponse.setEnableLinkFlowCheck(enableLinkFlowCheck);
        Integer verifyStatus = getVerifyStatus(id).getVerifyStatus();
        activityResponse.setVerifyStatus(verifyStatus);
        activityResponse.setVerifiedFlag(
            verifyStatus.equals(BusinessActivityRedisKeyConstant.ACTIVITY_VERIFY_VERIFIED));
        return activityResponse;
    }

    @Override
    public ActivityVerifyResponse verifyActivity(ActivityVerifyRequest request) {
        Long activityId = request.getActivityId();
        Long scriptId = request.getScriptId();
        ActivityVerifyResponse response = new ActivityVerifyResponse();
        response.setActivityId(activityId);
        response.setScriptId(scriptId);
        //1.根据业务活动ID查询缓存
        String reportId = redisClientUtils.getString(
            BusinessActivityRedisKeyConstant.ACTIVITY_VERIFY_KEY + request.getActivityId());
        if (!StringUtil.isBlank(reportId)) {
            Integer verifyStatus = getVerifyStatus(activityId).getVerifyStatus();
            if (!verifyStatus.equals(BusinessActivityRedisKeyConstant.ACTIVITY_VERIFY_VERIFIED)) {
                response.setVerifyStatus(verifyStatus);
                response.setTaskStatus(true);
                response.setVerifiedFlag(false);
                return response;
            }
        }
        //获取业务活动详情
        ActivityResult activityResult = activityDAO.getActivityById(request.getActivityId());
        //获取脚本详情
        ScriptManageDeployDetailResponse scriptManageDeployDetail = scriptManageService.getScriptManageDeployDetail(
            scriptId);
        SceneManageWrapperVO vo = new SceneManageWrapperVO();
        vo.setScriptId(request.getScriptId());
        vo.setPressureTestSceneName(
            activityResult.getActivityName() + BusinessActivityRedisKeyConstant.ACTIVITY_VERIFY_SUFFIX);
        vo.setIpNum(BusinessActivityRedisKeyConstant.ACTIVITY_VERIFY_DEFAULT_IP_NUM);
        vo.setPressureTestTime(new TimeVO(1L, "m"));
        vo.setStopCondition(new ArrayList<>());
        vo.setScriptType(scriptManageDeployDetail.getType());
        vo.setConfigType(BusinessActivityRedisKeyConstant.ACTIVITY_VERIFY_DEFAULT_CONFIG_TYPE);
        SceneBusinessActivityRefVO sceneBusinessActivityRefVO = new SceneBusinessActivityRefVO();
        sceneBusinessActivityRefVO.setBusinessActivityId(activityId);
        sceneBusinessActivityRefVO.setBusinessActivityName(activityResult.getActivityName());
        sceneBusinessActivityRefVO.setScriptId(scriptId);
        vo.setBusinessActivityConfig(Collections.singletonList(sceneBusinessActivityRefVO));
        SceneManageWrapperReq req = new SceneManageWrapperReq();
        WebResponse webResponse = sceneManageService.buildSceneForFlowVerify(vo, req, null);
        if (!webResponse.getSuccess()) {
            response.setTaskStatus(false);
            return response;
        }
        //2.发起流量
        TaskFlowDebugStartReq taskFlowDebugStartReq = new TaskFlowDebugStartReq();
        List<SceneBusinessActivityRefOpen> businessActivityConfig = vo.getBusinessActivityConfig()
            .stream()
            .map(data -> {
                SceneBusinessActivityRefOpen sceneBusinessActivityRefOpen = new SceneBusinessActivityRefOpen();
                sceneBusinessActivityRefOpen.setBusinessActivityId(data.getBusinessActivityId());
                sceneBusinessActivityRefOpen.setBusinessActivityName(data.getBusinessActivityName());
                sceneBusinessActivityRefOpen.setApplicationIds(data.getApplicationIds());
                sceneBusinessActivityRefOpen.setBindRef(data.getBindRef());
                sceneBusinessActivityRefOpen.setTargetRT(
                    BusinessActivityRedisKeyConstant.ACTIVITY_VERIFY_DEFAULT_TARGET_RT);
                sceneBusinessActivityRefOpen.setTargetTPS(
                    BusinessActivityRedisKeyConstant.ACTIVITY_VERIFY_DEFAULT_TARGET_TPS);
                sceneBusinessActivityRefOpen.setTargetSA(
                    new BigDecimal(BusinessActivityRedisKeyConstant.ACTIVITY_VERIFY_DEFAULT_TARGET_RT));
                sceneBusinessActivityRefOpen.setTargetSuccessRate(
                    new BigDecimal(BusinessActivityRedisKeyConstant.ACTIVITY_VERIFY_DEFAULT_TARGET_RT));
                return sceneBusinessActivityRefOpen;
            }).collect(Collectors.toList());
        taskFlowDebugStartReq.setBusinessActivityConfig(businessActivityConfig);
        taskFlowDebugStartReq.setScriptId(scriptId);
        taskFlowDebugStartReq.setScriptType(scriptManageDeployDetail.getType());
        taskFlowDebugStartReq.setUploadFile(req.getUploadFile());
        List<PluginConfigDetailResponse> pluginConfigDetailResponseList = scriptManageDeployDetail
            .getPluginConfigDetailResponseList();
        if (CollectionUtils.isNotEmpty(pluginConfigDetailResponseList)) {
            List<Long> pluginIds = pluginConfigDetailResponseList.stream().map(o -> Long.parseLong(o.getName()))
                .collect(Collectors.toList());
            taskFlowDebugStartReq.setEnginePluginIds(pluginIds);
        }
        taskFlowDebugStartReq.setFeatures(req.getFeatures());
        taskFlowDebugStartReq.setLicense(RemoteConstant.LICENSE_VALUE);
        log.info("流量验证参数：{}", taskFlowDebugStartReq.toString());
        ResponseResult<Long> longResponseResult = cloudTaskApi.startFlowDebugTask(taskFlowDebugStartReq);
        log.info("流量验证发起结果：{}", longResponseResult.toString());
        response.setTaskStatus(longResponseResult.getSuccess());
        if (!longResponseResult.getSuccess()) {
            return response;
        }
        response.setVerifiedFlag(false);
        response.setVerifyStatus(BusinessActivityRedisKeyConstant.ACTIVITY_VERIFY_VERIFYING);
        //3.缓存任务ID并返回
        redisClientUtils.setString(BusinessActivityRedisKeyConstant.ACTIVITY_VERIFY_KEY + activityId,
            String.valueOf(longResponseResult.getData()), BusinessActivityRedisKeyConstant.ACTIVITY_VERIFY_KEY_EXPIRE,
            TimeUnit.SECONDS);
        response.setScriptId(scriptId);
        return response;
    }

    @Override
    public ActivityVerifyResponse getVerifyStatus(Long activityId) {
        ActivityVerifyResponse response = new ActivityVerifyResponse();
        response.setActivityId(activityId);
        response.setVerifyStatus(BusinessActivityRedisKeyConstant.ACTIVITY_VERIFY_UNVERIFIED);
        //1.从缓存获取taskId
        String reportId = redisClientUtils.getString(BusinessActivityRedisKeyConstant.ACTIVITY_VERIFY_KEY + activityId);
        //2.根据taskId获取报告状态即任务状态
        if (!StringUtil.isBlank(reportId)) {
            WebResponse webResponse = reportService.getReportByReportId(Long.valueOf(reportId));
            if (Objects.nonNull(webResponse) && Objects.nonNull(webResponse.getData())) {
                ReportDetailOutput reportDetailOutput = (ReportDetailOutput)webResponse.getData();
                Integer verifyStatus = reportDetailOutput.getTaskStatus();
                response.setVerifyStatus(verifyStatus);
                response.setVerifiedFlag(
                    verifyStatus.equals(BusinessActivityRedisKeyConstant.ACTIVITY_VERIFY_VERIFIED));
            }
        }
        return response;
    }

    // TODO 变更逻辑后续看如何设计
    private boolean isChange(ActivityResult oldActivity, ActivityUpdateRequest newActivity) {
        return false;
    }
}
