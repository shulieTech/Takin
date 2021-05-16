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
import io.shulie.amdb.entity.TAmdbAppInstanceDO;
import io.shulie.amdb.request.query.TAmdbAppInstanceBatchAppQueryRequest;
import io.shulie.amdb.request.query.TAmdbAppInstanceErrorInfoByQueryRequest;
import io.shulie.amdb.request.query.TAmdbAppInstanceQueryRequest;
import io.shulie.amdb.service.AppInstanceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("amdb/db/api/appInstance")
/**
 * 控制台应用管理
 * @Author: xingchen
 * @Date: 2020/11/419:51
 * @Description:
 */
public class AppInstanceController {
    @Autowired
    private AppInstanceService appInstanceService;

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Response insert(@RequestBody TAmdbAppInstanceDO tAmdbApp) {
        return appInstanceService.insert(tAmdbApp);
    }

    @RequestMapping(value = "/insertBatch", method = RequestMethod.POST)
    public Response insertBatch(@RequestBody List<TAmdbAppInstanceDO> tAmdbApp) {
        return appInstanceService.insertBatch(tAmdbApp);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Response update(@RequestBody TAmdbAppInstanceDO tAmdbApp) {
        return Response.success(appInstanceService.update(tAmdbApp));
    }

    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public Response select(TAmdbAppInstanceDO tAmdbApp) {
        return Response.success(appInstanceService.selectOneByParam(tAmdbApp));
    }

    @RequestMapping(value = "/selectByParams", method = RequestMethod.GET)
    public Response selectByParams(TAmdbAppInstanceQueryRequest param) {
        if (param.getAppName() == null) {
            return Response.fail(new ErrorInfo("100", "参数错误"));
        }
        return Response.success(appInstanceService.selectByParams(param));
    }

    @RequestMapping(value = "/selectByBatchAppParams", method = RequestMethod.GET)
    public Response selectByBatchAppParams(TAmdbAppInstanceBatchAppQueryRequest param) {
        if (CollectionUtils.isEmpty(param.getAppIds()) && CollectionUtils.isEmpty(param.getAppNames()) && CollectionUtils.isEmpty(param.getAgentIds()) && CollectionUtils.isEmpty(param.getIpAddress())) {
            return Response.fail(new ErrorInfo("100", "参数错误"));
        }
        return Response.success(appInstanceService.selectByBatchAppParams(param));
    }

    @RequestMapping(value = "/selectErrorInfoByParams", method = RequestMethod.POST)
    public Response selectErrorInfoByParams(@RequestBody TAmdbAppInstanceErrorInfoByQueryRequest param) {
        if (StringUtils.isBlank(param.getAppId()) && StringUtils.isBlank(param.getAppName())) {
            return Response.fail(new ErrorInfo("100", "参数错误"));
        }
        return Response.success(appInstanceService.selectErrorInfoByParams(param));
    }

    @RequestMapping("/initOnlineStatus")
    public Response initOnlineStatus() {
        appInstanceService.initOnlineStatus();
        return Response.emptySuccess();
    }

    @RequestMapping(value ="/deleteByParams", method = RequestMethod.POST)
    public Response deleteByParams(@RequestBody TAmdbAppInstanceQueryRequest param) {
        if(StringUtils.isBlank(param.getAppName())){
            return Response.fail(new ErrorInfo("100", "参数错误"));
        }
        appInstanceService.deleteByParams(param);
        return Response.emptySuccess();
    }
}
