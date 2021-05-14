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

package io.shulie.tro.cloud.web.entrypoint.controller.scenemanage;

import java.util.Arrays;

import com.pamirs.tro.entity.domain.dto.report.SceneActionDTO;
import com.pamirs.tro.entity.domain.vo.report.SceneTaskNotifyParam;
import com.pamirs.tro.entity.domain.vo.schedule.ScheduleInitParam;
import io.shulie.tro.cloud.biz.input.scenemanage.SceneTaskStartInput;
import io.shulie.tro.cloud.biz.output.scenetask.SceneActionOutput;
import io.shulie.tro.cloud.biz.service.scene.SceneTaskService;
import io.shulie.tro.cloud.biz.service.schedule.ScheduleService;
import io.shulie.tro.cloud.common.constants.APIUrls;
import io.shulie.tro.cloud.common.constants.Constants;
import io.shulie.tro.cloud.common.exception.ApiException;
import io.shulie.tro.cloud.web.entrypoint.request.scenemanage.SceneManageIdRequest;
import io.shulie.tro.cloud.web.entrypoint.request.scenemanage.SceneTaskStartRequest;
import io.shulie.tro.cloud.web.entrypoint.response.SceneActionResponse;
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
    private ScheduleService scheduleService;

    @PostMapping("/start")
    @ApiOperation(value = "开始场景测试")
    public ResponseResult start(@RequestBody SceneTaskStartRequest request) {
        try {
            SceneTaskStartInput input = new SceneTaskStartInput();
            BeanUtils.copyProperties(request, input);
            return ResponseResult.success(sceneTaskService.start(input));
        } catch (ApiException ex) {
            SceneActionDTO sceneStart = new SceneActionDTO();
            sceneStart.setMsg(Arrays.asList(StringUtils.split(ex.getMessage(), Constants.SPLIT)));
            // TODO 返回正常？
            return ResponseResult.success(sceneStart);
        }
    }

    @PostMapping("/stop")
    @ApiOperation(value = "结束场景测试")
    public ResponseResult stop(@RequestBody SceneManageIdRequest request) {
        try {
            sceneTaskService.stop(request.getId());
        } catch (ApiException ex) {
            return ResponseResult.fail(String.valueOf(ex.getCode()), ex.getMessage(), "");
        }
        return ResponseResult.success();
    }

    @GetMapping("/checkStartStatus")
    @ApiOperation(value = "检查启动状态")
    public ResponseResult<SceneActionResponse> checkStartStatus(Long sceneId) {
        try {
            SceneActionOutput sceneAction = sceneTaskService.checkSceneTaskStatus(sceneId);
            SceneActionResponse response = new SceneActionResponse();
            response.setData(sceneAction.getData());
            response.setMsg(sceneAction.getMsg());
            return ResponseResult.success(response);
        } catch (ApiException ex) {
            return ResponseResult.fail(String.valueOf(ex.getCode()), ex.getMessage(), "");
        }
    }

    @GetMapping("/taskResultNotify")
    @ApiOperation(value = "启动结果通知")
    public String taskResultNotify(SceneTaskNotifyParam notify) {
        return sceneTaskService.taskResultNotify(notify);
    }

    @GetMapping("/initCallback")
    @ApiOperation(value = "调度初始化回调函数")
    public ResponseResult initCallback(ScheduleInitParam param) {
        // 初始化调度
        scheduleService.initScheduleCallback(param);
        return ResponseResult.success();
    }

}
