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

package io.shulie.tro.web.app.service.scenemanage.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pamirs.tro.common.constant.AppAccessTypeEnum;
import com.pamirs.tro.common.constant.AppSwitchEnum;
import com.pamirs.tro.common.constant.Constants;
import com.pamirs.tro.common.exception.ApiException;
import com.pamirs.tro.entity.dao.confcenter.TApplicationMntDao;
import com.pamirs.tro.entity.domain.dto.scenemanage.ScriptCheckDTO;
import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import com.pamirs.tro.entity.domain.query.ApplicationQueryParam;
import com.pamirs.tro.entity.domain.vo.ApplicationVo;
import com.pamirs.tro.entity.domain.vo.report.SceneActionParam;
import io.shulie.tro.cloud.common.redis.RedisClientUtils;
import io.shulie.tro.cloud.open.api.scenetask.CloudTaskApi;
import io.shulie.tro.cloud.open.req.scenemanage.SceneManageIdReq;
import io.shulie.tro.cloud.open.req.scenetask.SceneTaskQueryTpsReq;
import io.shulie.tro.cloud.open.req.scenetask.SceneTaskUpdateTpsReq;
import io.shulie.tro.cloud.open.resp.scenemanage.SceneManageWrapperResp;
import io.shulie.tro.cloud.open.resp.scenemanage.SceneManageWrapperResp.SceneBusinessActivityRefResp;
import io.shulie.tro.cloud.open.resp.scenemanage.SceneManageWrapperResp.SceneSlaRefResp;
import io.shulie.tro.cloud.open.resp.scenetask.SceneActionResp;
import io.shulie.tro.cloud.open.resp.scenetask.SceneTaskAdjustTpsResp;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.constant.WebRedisKeyConstant;
import io.shulie.tro.web.app.exception.ExceptionCode;
import io.shulie.tro.web.app.exception.TroWebException;
import io.shulie.tro.web.app.request.scriptmanage.UpdateTpsRequest;
import io.shulie.tro.web.app.response.scriptmanage.PluginConfigDetailResponse;
import io.shulie.tro.web.app.response.scriptmanage.ScriptManageDeployDetailResponse;
import io.shulie.tro.web.app.service.ApplicationService;
import io.shulie.tro.web.app.service.async.AsyncService;
import io.shulie.tro.web.app.service.report.impl.ReportApplicationService;
import io.shulie.tro.web.app.service.scenemanage.SceneManageService;
import io.shulie.tro.web.app.service.scenemanage.SceneTaskService;
import io.shulie.tro.web.app.service.scriptmanage.ScriptManageService;
import io.shulie.tro.web.common.constant.RemoteConstant;
import io.shulie.tro.web.common.domain.WebResponse;
import io.shulie.tro.web.common.http.HttpWebClient;
import io.shulie.tro.web.diff.api.scenemanage.SceneManageApi;
import io.shulie.tro.web.diff.api.scenetask.SceneTaskApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

/**
 * @Author 莫问
 * @Date 2020-04-22
 */
@Service
@Slf4j
public class SceneTaskServiceImpl implements SceneTaskService {

    @Autowired
    private HttpWebClient httpWebClient;

    @Autowired
    private SceneManageService sceneManageService;

    @Autowired
    private TApplicationMntDao applicationMntDao;

    @Value("${start.task.check.application: true}")
    private Boolean checkApplication;

    @Autowired
    private SceneTaskApi sceneTaskApi;

    @Autowired
    private SceneManageApi sceneManageApi;

    @Autowired
    private ReportApplicationService reportApplicationService;

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private CloudTaskApi cloudTaskApi;

    @Autowired
    private ScriptManageService scriptManageService;

    @Autowired
    private RedisClientUtils redisClientUtils;

    /**
     * 查询场景业务活动信息，校验业务活动
     *
     * @param param
     * @return
     */
    @Override
    public WebResponse startTask(SceneActionParam param) {
        SceneManageIdReq req = new SceneManageIdReq();
        req.setId(param.getSceneId());
        ResponseResult<SceneManageWrapperResp> resp = sceneManageApi.getSceneDetail(req);
        if (!resp.getSuccess()) {
            throw ApiException.create(500, "场景信息查询错误，id=" + param.getSceneId());
        }
        SceneManageWrapperResp wrapperResp = resp.getData();
        Long operateId = param.getUid();
        if (operateId == null) {
            operateId = RestContext.getUser().getId();
        }
        Integer status = wrapperResp.getStatus();
        if (status != null && status != 0) {
            throw ApiException.create(500, "场景处于压测中，禁止并行压测，sceneId=" + param.getSceneId());
        }
        param.setUid(operateId);
        preCheckStart(wrapperResp, operateId);

        if (wrapperResp != null && wrapperResp.getScriptId() != null){
            ScriptManageDeployDetailResponse scriptManageDeployDetail = scriptManageService.getScriptManageDeployDetail(wrapperResp.getScriptId());
            List<PluginConfigDetailResponse> pluginConfigDetailResponseList = scriptManageDeployDetail.getPluginConfigDetailResponseList();
            if (CollectionUtils.isNotEmpty(pluginConfigDetailResponseList)){
                List<Long> pluginIds = pluginConfigDetailResponseList.stream().map(o -> Long.parseLong(o.getName())).collect(Collectors.toList());
                param.setEnginePluginIds(pluginIds);
            }
        }
        param.setRequestUrl(RemoteConstant.SCENE_TASK_START_URL);
        param.setHttpMethod(HttpMethod.POST);
        return httpWebClient.request(param);
    }

    @Override
    public ResponseResult stopTask(SceneActionParam param) {
        // 停止先删除 redis中的
        SceneManageIdReq req = new SceneManageIdReq();
        req.setId(param.getSceneId());
        ResponseResult<SceneActionResp> response = sceneTaskApi.checkTask(req);
        if(!response.getSuccess()) {
            throw new TroWebException(ExceptionCode.SCENE_STOP_ERROR,response.getError());
        }
        SceneActionResp resp = response.getData();
        redisClientUtils.hmdelete(WebRedisKeyConstant.PTING_APPLICATION_KEY, String.valueOf(resp.getReportId()));
        // 最后删除
        return sceneTaskApi.stopTask(req);
    }

    @Override
    public ResponseResult<SceneActionResp> checkStatus(Long sceneId) {
        SceneManageIdReq req = new SceneManageIdReq();
        req.setId(sceneId);
        ResponseResult<SceneActionResp> response = sceneTaskApi.checkTask(req);
        if(!response.getSuccess()) {
            throw new TroWebException(ExceptionCode.SCENE_CHECK_ERROR,response.getError());
        }
        SceneActionResp resp = response.getData();
        //非压测中
        if (response.getData() == null || resp.getData() != 2L) {
            return response;
        }
        Long reportId = resp.getReportId();
        //获取场景SLA 内存 | cpu
        ResponseResult<SceneManageWrapperResp> detailResp = sceneManageApi.getSceneDetail(req);
        if (!detailResp.getSuccess()) {
            return response;
        }
        SceneManageWrapperResp wrapperResp = JSONObject.parseObject(JSON.toJSONString(detailResp.getData()),
            SceneManageWrapperResp.class);
        // 缓存压测中的应用
        List<SceneBusinessActivityRefResp> sceneBusinessActivityRefList = wrapperResp.getBusinessActivityConfig();
        //从活动中提取应用ID，去除重复ID
        List<Long> applicationIds = sceneBusinessActivityRefList.stream()
            .filter(data -> StringUtils.isNotEmpty(data.getApplicationIds()))
            .map(SceneBusinessActivityRefResp::getApplicationIds)
            .flatMap(appIds -> Arrays.stream(appIds.split(",")).map(Long::parseLong))
            .filter(data -> data > 0L).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(applicationIds)) {
            List<TApplicationMnt> applicationMntList = applicationMntDao.queryApplicationMntListByIds(applicationIds);
            List<String> applicationNames = applicationMntList.stream().map(TApplicationMnt::getApplicationName)
                .collect(Collectors.toList());
            Map<String, Object> map = Maps.newHashMap();
            // 报告 带应用
            map.put(String.valueOf(reportId), applicationNames);
            redisClientUtils.hmset(WebRedisKeyConstant.PTING_APPLICATION_KEY, map);
        }
        Map<String, List<SceneSlaRefResp>> slaMap = getSceneSla(wrapperResp);
        if (MapUtils.isNotEmpty(slaMap)) {
            //获取应用列表
            List<String> appNames = reportApplicationService.getReportApplication(reportId).getApplicationNames();
            // 创建监控线程，监控cpu及memory
            if (CollectionUtils.isNotEmpty(appNames)) {
                asyncService.monitorCpuMemory(sceneId, reportId, appNames, slaMap.get("stop"), slaMap.get("warn"));
            }
        }
        return response;
    }

    @Override
    public void updateTaskTps(UpdateTpsRequest request) {
        SceneTaskUpdateTpsReq req = new SceneTaskUpdateTpsReq();
        req.setSceneId(request.getSceneId());
        req.setReportId(request.getReportId());
        req.setTpsNum(request.getTargetTps());
        req.setLicense(RemoteConstant.LICENSE_VALUE);
        ResponseResult responseResult = cloudTaskApi.updateSceneTaskTps(req);
        if (responseResult == null || !responseResult.getSuccess()) {
            throw new RuntimeException("修改TPS失败");
        }
    }

    @Override
    public Long queryTaskTps(Long reportId, Long sceneId) {
        SceneTaskQueryTpsReq req = new SceneTaskQueryTpsReq();
        req.setSceneId(sceneId);
        req.setReportId(reportId);
        req.setLicense(RemoteConstant.LICENSE_VALUE);
        ResponseResult<SceneTaskAdjustTpsResp> respResponseResult = cloudTaskApi.queryAdjustTaskTps(req);
        if (respResponseResult != null && respResponseResult.getData() != null) {
            return respResponseResult.getData().getTotalTps();
        }
        return null;
    }

    private void preCheckStart(SceneManageWrapperResp sceneData, Long uid) {
        //检查压测开关，压测开关处于关闭状态时禁止压测
        String userSwitchStatusForVo = applicationService.getUserSwitchStatusForVo(uid);
        if (StringUtils.isBlank(userSwitchStatusForVo) || !userSwitchStatusForVo.equals(
            AppSwitchEnum.OPENED.getCode())) {
            throw ApiException.create(Constants.API_ERROR_CODE, "压测开关处于关闭状态，禁止压测");
        }

        //检查场景是否存可以开启启压测
        List<SceneBusinessActivityRefResp> sceneBusinessActivityRefList = sceneData.getBusinessActivityConfig();
        if (CollectionUtils.isEmpty(sceneBusinessActivityRefList)) {
            log.error("[{}]场景没有配置业务活动", sceneData.getId());
            throw ApiException.create(Constants.API_ERROR_CODE, "启动压测失败，没有配置业务活动");
        }

        //需求要求，业务验证异常需要详细输出
        StringBuffer errorMsg = new StringBuffer();

        //从活动中提取应用ID，去除重复ID
        List<Long> applicationIds = sceneBusinessActivityRefList.stream()
            .map(SceneBusinessActivityRefResp::getApplicationIds)
            .filter(StringUtils::isNotEmpty)
            .flatMap(appIds -> Arrays.stream(appIds.split(",")).map(Long::parseLong))
            .filter(data -> data > 0L).distinct().collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(applicationIds) && checkApplication) {
            ApplicationQueryParam param = new ApplicationQueryParam();
            param.setApplicationIds(applicationIds);
            param.setCurrentPage(0);
            param.setPageSize(applicationIds.size() + 1);
            List<ApplicationVo> applicationList = applicationService.getApplicationListVo(param);
            if (applicationList == null || applicationList.size() != applicationIds.size()) {
                log.error("启动压测失败, 没有找到关联的应用信息，场景ID：{}", sceneData.getId());
                throw ApiException.create(Constants.API_ERROR_CODE, "启动压测失败, 没有找到关联的应用信息");
            }
            List<String> errorApplicationStatus = Lists.newArrayList();

            applicationList.forEach(application -> {
                boolean statusError = false;

                if (!application.getSwitchStutus().equals(AppSwitchEnum.OPENED.getCode())) {
                    log.error("应用[{}]未开启", application.getApplicationName());
                    statusError = true;
                }

                if (application.getAccessStatus().intValue() != AppAccessTypeEnum.NORMAL.getValue()) {
                    log.error("应用[{}]接入状态不是开启状态，当前状态[{}]", application.getApplicationName(),
                        application.getAccessStatus());
                    statusError = true;
                }

                if (statusError) {
                    errorApplicationStatus.add(application.getApplicationName());
                }

            });

            if (errorApplicationStatus.size() > 0) {
                String errorApplication = String.format(" %d个应用状态不正常：%s", errorApplicationStatus.size(),
                    StringUtils.join(errorApplicationStatus.toArray(), ","));
                errorMsg.append(errorApplication).append("|");
            }
        }

        //压测脚本文件检查
        ScriptCheckDTO scriptCheck = sceneManageService.checkBusinessActivityAndScript(sceneData);
        if (StringUtils.isNotBlank(scriptCheck.getErrmsg())) {
            log.error(scriptCheck.getErrmsg());
            errorMsg.append(scriptCheck.getErrmsg()).append("|");
        }

        if (!scriptCheck.getPtTag()) {
            log.error("压测脚本未识别到压测标记");
            errorMsg.append("压测脚本未识别到压测标记").append("|");
        }

        if (!scriptCheck.getMatchActivity()) {
            log.error("压测脚本和业务活动不匹配");
            errorMsg.append("压测脚本和业务活动不匹配");
        }

        if (errorMsg.length() > 0) {
            if (errorMsg.toString().endsWith("|")) {
                String msg = StringUtils.substring(errorMsg.toString(), 0, errorMsg.toString().length() - 1);
                throw ApiException.create(Constants.API_ERROR_CODE, msg);
            }
        }
    }

    private Map<String, List<SceneSlaRefResp>> getSceneSla(SceneManageWrapperResp detailResp) {
        Map<String, List<SceneSlaRefResp>> dataMap = Maps.newHashMap();
        List<SceneSlaRefResp> stopList = Optional.ofNullable(detailResp.getStopCondition()).orElse(
            Lists.newArrayList());
        stopList = stopList.stream().filter(data -> data.getRule().getIndexInfo() >= 4).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(stopList)) {
            dataMap.put("stop", stopList);
        }
        List<SceneSlaRefResp> warnList = Optional.ofNullable(detailResp.getWarningCondition()).orElse(
            Lists.newArrayList());
        warnList = warnList.stream().filter(data -> data.getRule().getIndexInfo() >= 4).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(warnList)) {
            dataMap.put("warn", warnList);
        }
        return dataMap;
    }
}

