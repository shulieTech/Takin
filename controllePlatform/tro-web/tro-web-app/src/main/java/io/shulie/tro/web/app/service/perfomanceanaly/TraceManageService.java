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

package io.shulie.tro.web.app.service.perfomanceanaly;

import io.shulie.tro.channel.bean.CommandPacket;
import io.shulie.tro.web.app.request.perfomanceanaly.TraceManageCreateRequest;
import io.shulie.tro.web.app.request.perfomanceanaly.TraceManageDeployQueryRequest;
import io.shulie.tro.web.app.request.perfomanceanaly.TraceManageQueryListRequest;
import io.shulie.tro.web.app.request.perfomanceanaly.TraceManageUploadRequest;
import io.shulie.tro.web.app.response.perfomanceanaly.TraceManageCreateResponse;
import io.shulie.tro.web.app.response.perfomanceanaly.TraceManageResponse;

import java.util.List;

/**
 * @author zhaoyong
 * 方法分析相关
 */
public interface TraceManageService {
    /**
     * 创建方法追踪
     * @param traceManageCreateRequest
     * @return
     */
    TraceManageCreateResponse createTraceManage(TraceManageCreateRequest traceManageCreateRequest) throws Exception;

    /**
     * 查询方法追踪详情
     * @param traceManageDeployQueryRequest
     * @return
     */
    TraceManageResponse queryTraceManageDeploy(TraceManageDeployQueryRequest traceManageDeployQueryRequest);

    /**
     * agent上传trace信息
     * @param commandPacket
     * @return
     */
    void uploadTraceInfo(CommandPacket commandPacket);

    /**
     * 查询所有当前agent的最终信息
     * @param traceManageQueryListRequest
     * @return
     */
    List<TraceManageResponse> queryTraceManageList(TraceManageQueryListRequest traceManageQueryListRequest);
}
