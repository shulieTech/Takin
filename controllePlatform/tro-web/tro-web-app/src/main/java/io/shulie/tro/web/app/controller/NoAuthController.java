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

package io.shulie.tro.web.app.controller;

import java.util.Map;

import io.shulie.tro.cloud.common.redis.RedisClientUtils;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.web.app.constant.WebRedisKeyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName NoAuthController
 * @Description 无需权限的访问
 * @Author qianshui
 * @Date 2020/11/23 下午6:18
 */
@RestController
@RequestMapping("/api/noauth")
public class NoAuthController {

    @Autowired
    private RedisClientUtils redisClientUtils;

    @PutMapping("/resume/scenetask")
    public ResponseResult resumeSceneTask(@RequestBody Map<String, Object> paramMap) {
        Long reportId = Long.parseLong(String.valueOf(paramMap.get("reportId")));
        if (reportId == null) {
            ResponseResult.fail("reportId cannot be null,", "");
        }
        redisClientUtils.del(WebRedisKeyConstant.REPORT_WARN_PREFIX + reportId);
        redisClientUtils.hmdelete(WebRedisKeyConstant.PTING_APPLICATION_KEY, String.valueOf(reportId));
        return ResponseResult.success("resume success");
    }
}
