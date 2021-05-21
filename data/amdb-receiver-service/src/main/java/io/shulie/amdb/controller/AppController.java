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

package io.shulie.amdb.controller;

import io.shulie.amdb.common.ErrorInfo;
import io.shulie.amdb.common.Response;
import io.shulie.amdb.entity.AppDO;
import io.shulie.amdb.request.query.TAmdbAppBatchAppQueryRequest;
import io.shulie.amdb.response.app.AmdbAppResponse;
import io.shulie.amdb.service.AppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "应用管理")
@RestController
@RequestMapping("amdb/db/api/app")
/**
 * 控制台应用管理
 * @Author: xingchen
 * @Date: 2020/11/419:51
 * @Description:
 */
public class AppController {
    @Autowired
    private AppService appService;

    @ApiOperation(value = "新增")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Response insert(@RequestBody AppDO app) {
        appService.insert(app);
        return Response.success(app.getId());
    }

    @ApiOperation(value = "新增")
    @RequestMapping(value = "/insertAsync", method = RequestMethod.POST)
    public Response insertAsync(@RequestBody AppDO app) {
        appService.insertAsync(app);
        return Response.emptySuccess();
    }

    @RequestMapping(value = "/insertBatch", method = RequestMethod.POST)
    public Response insertBatch(@RequestBody List<AppDO> app) {
        return Response.success(appService.insertBatch(app));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Response update(@RequestBody AppDO app) {
        return Response.success(appService.update(app));
    }

    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Response updateBatch(@RequestBody List<AppDO> app) {
        return Response.success(appService.updateBatch(app));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Response delete(@RequestBody AppDO app) {
        return Response.success(appService.delete(app));
    }

    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public Response select(AppDO app) {
        return Response.success(appService.selectOneByParam(app));
    }

    @RequestMapping(value = "/selectByFilter", method = RequestMethod.GET)
    public Response selectByFilter(String filter) {
        return Response.success(appService.selectByFilter(filter));
    }

    @RequestMapping(value = "/selectByBatchAppParams", method = RequestMethod.GET)
    public Response<List<AmdbAppResponse>> selectByBatchAppParams(TAmdbAppBatchAppQueryRequest param) {
        if (CollectionUtils.isEmpty(param.getAppIds()) && CollectionUtils.isEmpty(param.getAppNames())) {
            return Response.fail(new ErrorInfo("100", "参数错误"));
        }
        return Response.success(appService.selectByBatchAppParams(param));
    }

    @RequestMapping(value = "/selectAllAppName", method = RequestMethod.GET)
    public Response selectAllAppName(TAmdbAppBatchAppQueryRequest param) {
        return Response.success(appService.selectAllAppName(param));
    }
}
