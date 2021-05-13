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

package io.shulie.tro.web.app.service.fastdebug;

import java.util.List;

import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.output.fastdebug.FastDebugStackInfoOutPut;
import io.shulie.tro.web.app.request.fastdebug.FastDebugAgentLogUploadRequest;
import io.shulie.tro.web.app.request.fastdebug.FastDebugStackUploadCreateRequest;

/**
 * @Author: mubai
 * @Date: 2020-12-28 11:45
 * @Description:
 */
public interface FastDebugAgentUploadService {

    /**
     * 上传实时调试栈信息
     *
     * @param request
     */
    void uploadDebugStack(List<FastDebugStackUploadCreateRequest> request, String userAppKey);

    /**
     * 上传agent日志
     *
     * @param request
     */
    void uploadAgentLog(FastDebugAgentLogUploadRequest request);

    /**
     * 上传app日志
     *
     * @param appLogUploadRequest
     */
    void uploadAppLog(FastDebugAgentLogUploadRequest appLogUploadRequest);

    /**
     * 获取调试堆栈上传次数
     *
     * @param traceId
     * @return
     */
    Integer getStackUploadTimes(String traceId);

    /**
     * 删除调试堆栈上传次数
     *
     * @param traceId
     */
    void deleteStackUploadTimesKey(String traceId);

    /**
     * 是否有栈异常信息
     *
     * @param traceId
     * @return
     */
    Long hasStackErrorLog(String traceId);

    /**
     * 获取调用栈 异常节点列表
     * @param traceId
     * @return
     */
    List<FastDebugStackInfoOutPut> getErrorStackInfoList(String traceId);

    /**
     * 根据id获取
     * @param id
     * @return
     */
    FastDebugStackInfoOutPut getById(Long id);


}
