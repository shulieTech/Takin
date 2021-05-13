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

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.google.common.collect.Lists;
import io.shulie.tro.web.config.enums.BlockListType;
import io.shulie.tro.web.config.sync.api.BlockListSyncService;
import io.shulie.tro.web.config.sync.zk.constants.ZkConfigPathConstants;
import io.shulie.tro.web.config.sync.zk.impl.client.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @Author: fanxx
 * @Date: 2020/9/28 5:16 下午
 * @Description:
 */
@Component
public class BlockListSyncServiceImpl implements BlockListSyncService {

    @Autowired
    private ZkClient zkClient;

    @Override
    public void syncBlockList(String namespace, BlockListType type, List<String> blockLists) {
        if (blockLists == null) {
            throw new RuntimeException("传入的数据为空");
        }
        String path = "/" + namespace + ZkConfigPathConstants.BLOCK_LIST_PARENT_PATH + "/" + type.name().toLowerCase();
        // 空数组，我们认为是清空
        if (CollectionUtils.isEmpty(blockLists)) {
            zkClient.syncNode(path, JSONObject.toJSONString(Lists.newArrayList()));
            return;
        }
        // 如果新更新的和已有的一样，不更新，降低ZK压力
        String existsStr = zkClient.getNode(path);
        blockLists.sort(String.CASE_INSENSITIVE_ORDER);
        String newBlockListStr = JSON.toJSONString(blockLists);
        if (newBlockListStr.equals(existsStr)) {
            return;
        }
        zkClient.syncNode(path, newBlockListStr);
    }
}
