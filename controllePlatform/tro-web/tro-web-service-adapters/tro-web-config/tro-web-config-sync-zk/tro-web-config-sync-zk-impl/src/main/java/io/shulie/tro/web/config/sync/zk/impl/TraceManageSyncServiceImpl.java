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

package io.shulie.tro.web.config.sync.zk.impl;

import io.shulie.tro.web.config.sync.api.TraceManageSyncService;
import io.shulie.tro.web.config.sync.zk.constants.ZkConfigPathConstants;
import io.shulie.tro.web.config.sync.zk.impl.client.ZkClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhaoyong
 */
@Slf4j
@Component
public class TraceManageSyncServiceImpl implements TraceManageSyncService {

    @Autowired
    private ZkClient zkClient;


    @Override
    public void createZkTraceManage(String agentId, String sampleId, String traceDeployObject) {
        String path = ZkConfigPathConstants.TRACE_MANAGE_DEPLOY_PATH + "/" + agentId + "/" + sampleId;
        String existsStr = zkClient.getNode(path);
        //如果路径下有值，说明信息还没有同步，不再重新创建
        if (StringUtils.isBlank(existsStr)){
            log.info("创建trace写入zk节点，path={},traceDeployObject={}",path,traceDeployObject);
            zkClient.syncNode(path,traceDeployObject);
        }
    }
}
