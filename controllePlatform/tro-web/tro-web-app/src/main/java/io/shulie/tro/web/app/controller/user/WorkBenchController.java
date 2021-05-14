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

package io.shulie.tro.web.app.controller.user;

import java.util.List;

import com.pamirs.tro.entity.domain.vo.user.QuickAccessVo;
import com.pamirs.tro.entity.domain.vo.user.WorkBenchVo;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.service.user.QuickAccessService;
import io.shulie.tro.web.app.service.user.WorkBenchService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: mubai
 * @Date: 2020-06-28 17:56
 * @Description:
 */

@RestController
@RequestMapping(APIUrls.TRO_API_URL)
@Api(tags = "WorkBenchController", value = "工作台接口")
@Slf4j
public class WorkBenchController {

    @Autowired
    private WorkBenchService workBenchService;

    @Autowired
    private QuickAccessService quickAccessService;

    @GetMapping("/user/work/bench")
    public Response<WorkBenchVo> workBench() {
        try {
            WorkBenchVo userWorkBench = workBenchService.getUserWorkBench(RestContext.getUser().getId());
            Response response = Response.success(userWorkBench);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.fail("获取工作台数据失败...");
        }
    }

    @GetMapping("/user/work/bench/access")
    public Response<List<QuickAccessVo>> quickAccess() {
        Response response = Response.success();
        List<QuickAccessVo> quickAccess = quickAccessService.getQuickAccess(null);
        response.setData(quickAccess);
        return response;

    }

}
