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

package io.shulie.tro.web.config.sync.api;

/**
 * @author zhaoyong
 * 方法追踪和agent交互服务
 */
public interface TraceManageSyncService {
    /**
     * 创建zk节点，写入内容
     * @param agentId
     * @param sampleId
     * @param traceDeployObject
     */
    void createZkTraceManage(String agentId, String sampleId, String traceDeployObject);
}
