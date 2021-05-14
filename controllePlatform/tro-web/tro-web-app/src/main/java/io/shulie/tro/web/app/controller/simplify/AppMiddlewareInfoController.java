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

package io.shulie.tro.web.app.controller.simplify;

import com.pamirs.tro.entity.domain.query.agent.AppMiddlewareQuery;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.service.simplify.AppMiddlewareInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.tro.web.api.controller.simplify
 * @Date 2020-03-25 17:42
 */
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
@Api(tags = "应用中间件信息", value = "应用中间件信息")
public class AppMiddlewareInfoController {

    @Autowired
    private AppMiddlewareInfoService appMiddlewareInfoService;

    @ApiOperation("应用中间件查询")
    @GetMapping(value = "app/middleware/query")
    public Response queryPage(@RequestParam("pageSize") Integer pageSize,
        @RequestParam("pageNum") Integer pageNum,
        @RequestParam("applicationId") Long applicationId) {
        try {
            AppMiddlewareQuery query = new AppMiddlewareQuery();
            query.setPageNum(pageNum);
            query.setPageSize(pageSize);
            query.setApplicationId(applicationId);
            return appMiddlewareInfoService.queryPage(query);
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }
    }
}
