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
import io.shulie.amdb.request.query.MetricsQueryRequest;
import io.shulie.amdb.service.MetricsService;
import io.swagger.annotations.Api;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("指标信息查询")
@RestController
@RequestMapping("amdb/db/api/metrics")
/**
 * 指标信息查询（influxdb通用查询）
 * @Author: xingchen
 * @Date: 2020/11/419:51
 * @Description:
 */
public class MetricsController {

    @Autowired
    MetricsService metricsService;

    //指标数据查询
    @RequestMapping(value = "/queryMetrics", method = RequestMethod.POST)
    public Response queryIpList(@RequestBody MetricsQueryRequest request) {
        if (StringUtils.isBlank(request.getMeasurementName()) || CollectionUtils.isEmpty(request.getTagMapList())
                || request.getFieldMap() == null || request.getFieldMap().size() == 0
                || request.getStartTime() == 0 || request.getEndTime() == 0) {
            return Response.fail(new ErrorInfo("100", "参数错误"));
        }
        return Response.success(metricsService.getMetrics(request));
    }
}
