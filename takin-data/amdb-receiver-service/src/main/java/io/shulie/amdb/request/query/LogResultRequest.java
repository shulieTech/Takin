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

package io.shulie.amdb.request.query;

import io.shulie.amdb.common.request.PagingRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("日志查询条件")
public class LogResultRequest extends PagingRequest {
    @ApiModelProperty("traceId")
    String traceId;
    @ApiModelProperty("应用名称")
    String appName;
    @ApiModelProperty("上游应用名称")
    String upAppName;
    @ApiModelProperty("入口应用名称")
    String traceAppName;
    @ApiModelProperty("中间件名称")
    String middlewareName;
    @ApiModelProperty("服务名称")
    String serviceName;
    @ApiModelProperty("方法")
    String method;
    @ApiModelProperty("rpcType")
    String rpcType;
    @ApiModelProperty("logType")
    String logType;
    @ApiModelProperty("最近时间，单位:秒")
    int latestTime;
    @ApiModelProperty("起始时间，格式：时间戳(毫秒)")
    long startTime;
    @ApiModelProperty("截止时间，格式：时间戳(毫秒)")
    long endTime;
    @ApiModelProperty("客户端IP")
    String clientIp;
    @ApiModelProperty("服务端IP")
    String serverIp;
    @ApiModelProperty("最小耗时，单位(毫秒)")
    int costTime;
    @ApiModelProperty("查询日志类别：0-全部 1-解析异常 2-链路异常")
    int logCategory;
}
