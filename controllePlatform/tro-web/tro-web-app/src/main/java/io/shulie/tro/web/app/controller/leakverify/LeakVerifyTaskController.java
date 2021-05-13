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

package io.shulie.tro.web.app.controller.leakverify;

import java.util.Objects;
import java.util.Set;

import com.pamirs.tro.entity.domain.entity.user.User;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.request.leakverify.LeakVerifyTaskRunWithoutSaveRequest;
import io.shulie.tro.web.app.request.leakverify.LeakVerifyTaskStartRequest;
import io.shulie.tro.web.app.request.leakverify.LeakVerifyTaskStopRequest;
import io.shulie.tro.web.app.response.leakverify.LeakVerifyTaskResultResponse;
import io.shulie.tro.web.app.service.VerifyTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: fanxx
 * @Date: 2021/1/8 2:45 下午
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/api/leak/verify")
@Api(tags = "漏数验证任务管理")
public class LeakVerifyTaskController {

    @Autowired
    private VerifyTaskService verifyTaskService;

    @PostMapping("/start")
    @ApiOperation(value = "开始周期性验证任务")
    public void start(@RequestBody LeakVerifyTaskStartRequest startRequest) {
        User user = RestContext.getUser();
        if (Objects.isNull(user) || user.getUserType() != 0) {
            log.error("用户为空或用户类型非管理员，开启验证任务失败");
            return;
        }
        verifyTaskService.start(startRequest);
    }

    @PostMapping("/stop")
    @ApiOperation(value = "停止周期性验证任务")
    public void stop(@RequestBody LeakVerifyTaskStopRequest stopRequest) {
        User user = RestContext.getUser();
        if (Objects.isNull(user) || user.getUserType() != 0) {
            log.error("用户为空或用户类型非管理员，停止验证任务失败");
            return;
        }
        verifyTaskService.stop(stopRequest);
    }

    @PostMapping("/run")
    @ApiOperation(value = "运行单次验证任务")
    public LeakVerifyTaskResultResponse run(@RequestBody LeakVerifyTaskRunWithoutSaveRequest runRequest) {
        User user = RestContext.getUser();
        if (Objects.isNull(user) || user.getUserType() != 0) {
            log.error("用户为空或用户类型非管理员，运行验证任务失败");
            return null;
        }
        return verifyTaskService.runWithoutResultSave(runRequest);
    }

    @GetMapping("/query")
    @ApiOperation(value = "查看所有验证任务")
    public Set<String> queryVerifyTask() {
        User user = RestContext.getUser();
        if (Objects.isNull(user) || user.getUserType() != 0) {
            log.error("用户为空或用户类型非管理员，查看验证任务失败");
            return null;
        }
        return verifyTaskService.queryVerifyTask();
    }
}
