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

package io.shulie.tro.cloud.open.entrypoint.controller.scenetask;

import java.util.Arrays;

import io.shulie.tro.cloud.biz.input.scenemanage.SceneManageWrapperInput;
import io.shulie.tro.cloud.biz.input.scenemanage.SceneTaskQueryTpsInput;
import io.shulie.tro.cloud.biz.input.scenemanage.SceneTaskStartInput;
import io.shulie.tro.cloud.biz.input.scenemanage.SceneTaskUpdateTpsInput;
import io.shulie.tro.cloud.biz.output.scenetask.SceneActionOutput;
import io.shulie.tro.cloud.biz.output.scenetask.SceneTaskQueryTpsOutput;
import io.shulie.tro.cloud.biz.service.scene.SceneTaskService;
import io.shulie.tro.cloud.common.constants.APIUrls;
import io.shulie.tro.cloud.common.constants.Constants;
import io.shulie.tro.cloud.common.exception.ApiException;
import io.shulie.tro.cloud.common.exception.TroCloudException;
import io.shulie.tro.cloud.common.exception.TroCloudExceptionEnum;
import io.shulie.tro.cloud.common.utils.CustomUtil;
import io.shulie.tro.cloud.open.entrypoint.convert.SceneTaskOpenConverter;
import io.shulie.tro.cloud.open.req.scenemanage.SceneManageIdReq;
import io.shulie.tro.cloud.open.req.scenemanage.SceneTaskStartReq;
import io.shulie.tro.cloud.open.req.scenetask.SceneTaskUpdateTpsReq;
import io.shulie.tro.cloud.open.req.scenetask.TaskFlowDebugStartReq;
import io.shulie.tro.cloud.open.resp.scenetask.SceneActionResp;
import io.shulie.tro.cloud.open.resp.scenetask.SceneTaskAdjustTpsResp;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIUrls.TRO_OPEN_API_URL + "scene/task/")
@Api(value = "场景任务OPEN")
public class SceneTaskOpenController {

    @Autowired
    private SceneTaskService sceneTaskService;

    @PostMapping("/start")
    @ApiOperation(value = "开始场景测试")
    public ResponseResult<SceneActionResp> start(@RequestBody SceneTaskStartReq request) {
        try {
            SceneTaskStartInput input = new SceneTaskStartInput();
            BeanUtils.copyProperties(request, input);
            SceneActionOutput output = sceneTaskService.start(input);
            SceneActionResp resp = new SceneActionResp();
            BeanUtils.copyProperties(output, resp);
            return ResponseResult.success(resp);
        } catch (ApiException ex) {
            SceneActionResp sceneStart = new SceneActionResp();
            sceneStart.setMsg(Arrays.asList(StringUtils.split(ex.getMessage(), Constants.SPLIT)));
            // TODO 返回正常？
            return ResponseResult.success(sceneStart);
        }
    }

    @PostMapping("/startFlowDebugTask")
    @ApiOperation(value = "启动调试流量任务")
    ResponseResult<Long> startFlowDebugTask(@RequestBody TaskFlowDebugStartReq taskFlowDebugStartReq){
        SceneManageWrapperInput input = SceneTaskOpenConverter.INSTANCE.ofTaskDebugDataStartReq(taskFlowDebugStartReq);
        Long reportId = sceneTaskService.startFlowDebugTask(input, taskFlowDebugStartReq.getEnginePluginIds());
        return ResponseResult.success(reportId);
    }

    @PostMapping("/updateSceneTaskTps")
    @ApiOperation(value = "调整压测任务tps")
    ResponseResult<String> updateSceneTaskTps(@RequestBody SceneTaskUpdateTpsReq sceneTaskUpdateTpsReq){

        SceneTaskUpdateTpsInput input = new SceneTaskUpdateTpsInput();
        input.setSceneId(sceneTaskUpdateTpsReq.getSceneId());
        input.setReportId(sceneTaskUpdateTpsReq.getReportId());
        input.setCustomerId(CustomUtil.getUserId());
        input.setTpsNum(sceneTaskUpdateTpsReq.getTpsNum());
        sceneTaskService.updateSceneTaskTps(input);
        return ResponseResult.success("tps更新成功");
    }

    @GetMapping("/queryAdjustTaskTps")
    @ApiOperation(value = "获取调整的任务tps")
    ResponseResult<SceneTaskAdjustTpsResp> queryAdjustTaskTps(@RequestParam Long sceneId,@RequestParam Long reportId){

        SceneTaskQueryTpsInput input = new SceneTaskQueryTpsInput();
        input.setSceneId(sceneId);
        input.setReportId(reportId);
        input.setCustomerId(CustomUtil.getUserId());
        SceneTaskQueryTpsOutput sceneTaskQueryTpsOutput = sceneTaskService.queryAdjustTaskTps(input);
        if (sceneTaskQueryTpsOutput != null){
            SceneTaskAdjustTpsResp sceneTaskAdjustTpsResp = new SceneTaskAdjustTpsResp();
            sceneTaskAdjustTpsResp.setTotalTps(sceneTaskQueryTpsOutput.getTotalTps());
            return ResponseResult.success(sceneTaskAdjustTpsResp);
        }
        return ResponseResult.success();
    }

    @PostMapping("/stop")
    @ApiOperation(value = "结束场景测试")
    public ResponseResult<String> stop(@RequestBody SceneManageIdReq req) {
        try {
            sceneTaskService.stop(req.getId());
        } catch (ApiException ex) {
            throw new TroCloudException(TroCloudExceptionEnum.SCENEMANAGE_GET_ERROR, ex);
        }
        return ResponseResult.success("停止场景成功");
    }

    @GetMapping("/checkStartStatus")
    @ApiOperation(value = "检查启动状态")
    public ResponseResult<SceneActionResp> checkStartStatus(Long id) {
        try {
            SceneActionOutput sceneAction = sceneTaskService.checkSceneTaskStatus(id);
            SceneActionResp resp = new SceneActionResp();
            resp.setData(sceneAction.getData());
            resp.setMsg(sceneAction.getMsg());
            resp.setReportId(sceneAction.getReportId());
            return ResponseResult.success(resp);
        } catch (ApiException ex) {
            throw new TroCloudException(TroCloudExceptionEnum.SCENEMANAGE_GET_ERROR, ex);
        }
    }

}

