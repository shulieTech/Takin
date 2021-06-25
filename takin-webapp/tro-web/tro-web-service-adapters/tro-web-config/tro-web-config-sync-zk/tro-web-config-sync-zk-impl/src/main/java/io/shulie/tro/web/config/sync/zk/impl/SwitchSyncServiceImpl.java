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

import com.alibaba.fastjson.JSONObject;

import io.shulie.tro.web.config.sync.api.SwitchSyncService;
import io.shulie.tro.web.config.sync.zk.constants.ZkConfigPathConstants;
import io.shulie.tro.web.config.sync.zk.impl.client.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shiyajian
 * create: 2020-09-17
 */
@Component
public class SwitchSyncServiceImpl implements SwitchSyncService {

    @Autowired
    private ZkClient zkClient;

    @Override
    public void turnClusterTestSwitch(String namespace, boolean on) {
        String path = "/" + namespace + ZkConfigPathConstants.CLUSTER_TEST_SWITCH_PATH;
        zkClient.syncNode(path, JSONObject.toJSONString(on));
    }

    @Override
    public void turnAllowListSwitch(String namespace, boolean on) {
        String path = "/" + namespace + ZkConfigPathConstants.ALLOW_LIST_SWITCH_PATH;
        zkClient.syncNode(path, JSONObject.toJSONString(on));
    }
}
