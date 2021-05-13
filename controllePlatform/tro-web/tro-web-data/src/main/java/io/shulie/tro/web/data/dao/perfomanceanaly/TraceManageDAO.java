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

package io.shulie.tro.web.data.dao.perfomanceanaly;

import io.shulie.tro.web.data.param.tracemanage.TraceManageCreateParam;
import io.shulie.tro.web.data.param.tracemanage.TraceManageDeployCreateParam;
import io.shulie.tro.web.data.param.tracemanage.TraceManageDeployUpdateParam;
import io.shulie.tro.web.data.param.tracemanage.TraceManageQueryParam;
import io.shulie.tro.web.data.result.tracemanage.TraceManageDeployResult;
import io.shulie.tro.web.data.result.tracemanage.TraceManageResult;

import java.util.List;

/**
 * @author zhaoyong
 */
public interface TraceManageDAO {
    /**
     * 更新方法追踪实例
     * @param updateParam
     */
    void updateTraceManageDeploy(TraceManageDeployUpdateParam updateParam);

    /**
     * 根据id查询方法实例信息
     * @param traceManageDeployId
     * @return
     */
    TraceManageDeployResult queryTraceManageDeployById(Long traceManageDeployId);

    /**
     * 创建方法追踪，同时创建方法追踪实例
     * @param traceManageCreateParam
     * @return
     */
    Long createTraceManageAndDeploy(TraceManageCreateParam traceManageCreateParam,String sampleId);

    /**
     * 根据id查询方法追踪详情
     * @param traceManageId
     * @return
     */
    TraceManageResult queryTraceManageById(Long traceManageId);


    /**
     * 根据唯一id查询方法追踪实例
     * @param sampleId
     * @return
     */
    TraceManageDeployResult queryTraceManageDeployBySampleId(String sampleId);

    /**
     * 批量创建方法追踪实例
     * @param traceManageDeployCreateParams
     */
    void createTraceManageDeploy(List<TraceManageDeployCreateParam> traceManageDeployCreateParams);

    /**
     * 查询追踪方法，不包含追踪实例
     * @param traceManageQueryParam
     * @return
     */
    List<TraceManageResult> queryTraceManageList(TraceManageQueryParam traceManageQueryParam);

    /**
     * 根据agentID查询正在trace过程中的追踪实例数量
     * @param agentId
     * @return
     */
    int countTraceIngManageDeployByAgentId(String agentId);

    /**
     * 更新时指定当前状态
     * @param traceManageDeployUpdateParam
     * @param currentStatus
     */
    void updateTraceManageDeployStatus(TraceManageDeployUpdateParam traceManageDeployUpdateParam, Integer currentStatus);

    /**
     * 根据状态查询追踪实例
     * @param status
     * @return
     */
    List<TraceManageDeployResult> queryTraceManageDeployByStatus(Integer status);
}
