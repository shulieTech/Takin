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

package io.shulie.tro.web.app.controller.scenemanage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;

import com.google.common.collect.Lists;
import com.pamirs.tro.common.constant.Constants;
import com.pamirs.tro.common.constant.VerifyTypeEnum;
import com.pamirs.tro.common.exception.ApiException;
import com.pamirs.tro.entity.domain.dto.report.SceneActionDTO;
import com.pamirs.tro.entity.domain.dto.scenemanage.SceneManageWrapperDTO;
import com.pamirs.tro.entity.domain.vo.report.SceneActionParam;
import io.shulie.tro.cloud.open.req.scenemanage.SceneManageIdReq;
import io.shulie.tro.cloud.open.resp.scenemanage.SceneManageWrapperResp;
import io.shulie.tro.cloud.open.resp.scenemanage.SceneManageWrapperResp.SceneBusinessActivityRefResp;
import io.shulie.tro.cloud.open.resp.scenetask.SceneActionResp;
import io.shulie.tro.common.beans.annotation.ModuleDef;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.constant.BizOpConstants;
import io.shulie.tro.web.app.context.OperationLogContextHolder;
import io.shulie.tro.web.app.request.datasource.DataSourceTestRequest;
import io.shulie.tro.web.app.request.leakcheck.LeakSqlBatchRefsRequest;
import io.shulie.tro.web.app.request.leakcheck.SqlTestRequest;
import io.shulie.tro.web.app.request.leakverify.LeakVerifyTaskStartRequest;
import io.shulie.tro.web.app.request.leakverify.LeakVerifyTaskStopRequest;
import io.shulie.tro.web.app.request.leakverify.VerifyTaskConfig;
import io.shulie.tro.web.app.request.scriptmanage.UpdateTpsRequest;
import io.shulie.tro.web.app.service.DataSourceService;
import io.shulie.tro.web.app.service.LeakSqlService;
import io.shulie.tro.web.app.service.VerifyTaskService;
import io.shulie.tro.web.app.service.scenemanage.SceneManageService;
import io.shulie.tro.web.app.service.scenemanage.SceneTaskService;
import io.shulie.tro.web.common.domain.WebResponse;
import io.shulie.tro.web.diff.api.scenetask.SceneTaskApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 莫问
 * @Date 2020-04-27
 */
@RestController
@RequestMapping(APIUrls.TRO_API_URL + "scene/task/")
@Api(tags = "场景任务", value = "场景任务")
public class SceneTaskController {

    @Autowired
    private SceneTaskService sceneTaskService;

    @Autowired
    private SceneManageService sceneManageService;

    @Autowired
    private VerifyTaskService verifyTaskService;

    @Autowired
    private SceneTaskApi sceneTaskApi;

    @Autowired
    private LeakSqlService leakSqlService;

    @Autowired
    private DataSourceService dataSourceService;

    @PostMapping("/start")
    @ApiOperation(value = "开始场景测试")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.PRESSURE_TEST_MANAGE,
        subModuleName = BizOpConstants.SubModules.PRESSURE_TEST_SCENE,
        logMsgKey = BizOpConstants.Message.MESSAGE_PRESSURE_TEST_SCENE_START
    )
    public WebResponse start(@RequestBody SceneActionParam param) {

        try {
            ResponseResult<SceneManageWrapperResp> webResponse = sceneManageService.detailScene(param.getSceneId());
            if (Objects.isNull(webResponse.getData())) {
                OperationLogContextHolder.ignoreLog();
                throw ApiException.create(Constants.API_ERROR_CODE, "该压测场景不存在");
            }
            OperationLogContextHolder.operationType(BizOpConstants.OpTypes.START);
            SceneManageWrapperResp sceneData = webResponse.getData();
            OperationLogContextHolder.addVars(BizOpConstants.Vars.SCENE_ID, String.valueOf(sceneData.getId()));
            OperationLogContextHolder.addVars(BizOpConstants.Vars.SCENE_NAME, sceneData.getPressureTestSceneName());
            OperationLogContextHolder.operationType(BizOpConstants.OpTypes.START);
            List<String> errorMsgList = checkVerifyTaskConfig(param, sceneData);
            if (CollectionUtils.isNotEmpty(errorMsgList)) {
                throw ApiException.create(Constants.API_ERROR_CODE, StringUtils.join(errorMsgList, Constants.SPLIT));
            }
            WebResponse startTaskResponse = sceneTaskService.startTask(param);
            if (!startTaskResponse.getSuccess()) {
                OperationLogContextHolder.ignoreLog();
            }
            if (startTaskResponse.getSuccess()) {
                if (param.getLeakSqlEnable() != null && param.getLeakSqlEnable()) {
                    //添加开始漏数的查询效果
                    LeakVerifyTaskStartRequest startRequest = new LeakVerifyTaskStartRequest();
                    startRequest.setRefType(VerifyTypeEnum.SCENE.getCode());
                    startRequest.setRefId(param.getSceneId());
                    //查询报告id
                    SceneManageIdReq req = new SceneManageIdReq();
                    req.setId(param.getSceneId());
                    ResponseResult<SceneActionResp> response = sceneTaskApi.checkTask(req);
                    SceneActionResp resp = response.getData();
                    startRequest.setReportId(resp.getReportId());
                    startRequest.setTimeInterval(sceneData.getScheduleInterval());
                    List<Long> businessActivityIds = sceneData.getBusinessActivityConfig().stream()
                            .map(SceneBusinessActivityRefResp::getBusinessActivityId).collect(Collectors.toList());
                    startRequest.setBusinessActivityIds(businessActivityIds);
                    verifyTaskService.start(startRequest);
                }
            }
            return startTaskResponse;
        } catch (ApiException ex) {
            SceneActionDTO sceneStart = new SceneActionDTO();
            sceneStart.setMsg(Arrays.asList(StringUtils.split(ex.getMessage(), Constants.SPLIT)));
            return WebResponse.success(sceneStart);
        }
    }

    @PostMapping("/stop")
    @ApiOperation(value = "结束场景测试")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.PRESSURE_TEST_MANAGE,
        subModuleName = BizOpConstants.SubModules.PRESSURE_TEST_SCENE,
        logMsgKey = BizOpConstants.Message.MESSAGE_PRESSURE_TEST_SCENE_STOP
    )
    public ResponseResult stop(@RequestBody SceneActionParam param) {
        ResponseResult webResponse = sceneManageService.detailScene(param.getSceneId());
        if (Objects.isNull(webResponse.getData())) {
            OperationLogContextHolder.ignoreLog();
            return ResponseResult.fail("该压测场景不存在", null);
        }
        OperationLogContextHolder.operationType(BizOpConstants.OpTypes.STOP);
        SceneManageWrapperDTO sceneData = JSON.parseObject(JSON.toJSONString(webResponse.getData()),
            SceneManageWrapperDTO.class);
        OperationLogContextHolder.addVars(BizOpConstants.Vars.SCENE_ID, String.valueOf(sceneData.getId()));
        OperationLogContextHolder.addVars(BizOpConstants.Vars.SCENE_NAME, sceneData.getPressureTestSceneName());
        ResponseResult webResponse2 = sceneTaskService.stopTask(param);
        if (webResponse2 == null || !webResponse2.getSuccess()) {
            OperationLogContextHolder.ignoreLog();
        }
        //如果有验证任务，则同时停止验证任务
        LeakVerifyTaskStopRequest stopRequest = new LeakVerifyTaskStopRequest();
        stopRequest.setRefType(VerifyTypeEnum.SCENE.getCode());
        stopRequest.setRefId(param.getSceneId());
        verifyTaskService.stop(stopRequest);
        return webResponse2;
    }

    @GetMapping("/checkStartStatus")
    @ApiOperation(value = "检查启动状态")
    public ResponseResult<SceneActionResp>  checkStartStatus(Long sceneId) {
        return sceneTaskService.checkStatus(sceneId);
    }

    @PutMapping("/tps")
    @ApiOperation(value = "修改运行中")
    public void updateTaskTps(@Validated @RequestBody UpdateTpsRequest request) {
        sceneTaskService.updateTaskTps(request);
    }

    @GetMapping("/queryTaskTps")
    @ApiOperation(value = "查询运行中修改之后的tps")
    public ResponseResult<Long> queryTaskTps(@RequestParam Long reportId,@RequestParam Long sceneId) {
        Long totalTps = sceneTaskService.queryTaskTps(reportId, sceneId);
        return ResponseResult.success(totalTps);
    }

    private List<String> checkVerifyTaskConfig(SceneActionParam param, SceneManageWrapperResp sceneData) {
        //校验漏数验证时间间隔是否配置
        if ((param.getLeakSqlEnable() != null && param.getLeakSqlEnable())) {
            if (Objects.isNull(sceneData.getScheduleInterval())) {
                return Collections.singletonList("请先配置漏数验证时间间隔");
            }

            //校验压测场景下是否存在漏数配置
            LeakSqlBatchRefsRequest refsRequest = new LeakSqlBatchRefsRequest();
            List<Long> businessActivityIds = sceneData.getBusinessActivityConfig().stream()
                .map(SceneBusinessActivityRefResp::getBusinessActivityId).collect(Collectors.toList());
            refsRequest.setBusinessActivityIds(businessActivityIds);
            List<VerifyTaskConfig> verifyTaskConfigList = leakSqlService.getVerifyTaskConfig(refsRequest);
            if (CollectionUtils.isEmpty(verifyTaskConfigList)) {
                return Collections.singletonList("关联的业务活动暂未配置漏数脚本");
            }

            //校验数据源是否能正常连接
            List<String> errorDbMsgList = Lists.newArrayList();
            verifyTaskConfigList.forEach(verifyTaskConfig -> {
                DataSourceTestRequest testRequest = new DataSourceTestRequest();
                testRequest.setJdbcUrl(verifyTaskConfig.getJdbcUrl());
                testRequest.setUsername(verifyTaskConfig.getUsername());
                testRequest.setPassword(verifyTaskConfig.getPassword());
                testRequest.setType(verifyTaskConfig.getType());
                String msg = dataSourceService.testConnection(testRequest);
                if (StringUtils.isNotBlank(msg)) {
                    errorDbMsgList.add(msg);
                }
            });
            if (CollectionUtils.isNotEmpty(errorDbMsgList)) {
                return errorDbMsgList.stream().sorted().collect(Collectors.toList());
            }

            //校验sql是否能正常运行
            List<String> errorSqlMsgList = Lists.newArrayList();
            verifyTaskConfigList.forEach(verifyTaskConfig -> {
                SqlTestRequest testRequest = new SqlTestRequest();
                testRequest.setDatasourceId(verifyTaskConfig.getDatasourceId());
                testRequest.setSqls(verifyTaskConfig.getSqls());
                String msg = leakSqlService.testSqlConnection(testRequest);
                if (StringUtils.isNotBlank(msg)) {
                    if (msg.contains(",")) {
                        errorSqlMsgList.addAll(Arrays.asList(msg.split(",")));
                    } else {
                        errorSqlMsgList.add(msg);
                    }
                }
            });
            if (CollectionUtils.isNotEmpty(errorSqlMsgList)) {
                return errorSqlMsgList.stream().sorted().collect(Collectors.toList());
            }
        }
        return null;
    }
}
