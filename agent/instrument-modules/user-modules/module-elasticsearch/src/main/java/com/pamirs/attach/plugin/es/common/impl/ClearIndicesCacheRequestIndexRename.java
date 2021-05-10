/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pamirs.attach.plugin.es.common.impl;

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import org.elasticsearch.action.admin.indices.cache.clear.ClearIndicesCacheRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/7/13 6:07 下午
 */
public class ClearIndicesCacheRequestIndexRename extends AbstractWriteRequestIndexRename {
    @Override
    public String getName() {
        return "clearIndicesCache";
    }

    @Override
    public List<String> reindex0(Object target) {
        ClearIndicesCacheRequest req = (ClearIndicesCacheRequest) target;
        String[] indexes = req.indices();
        for (int i = 0, len = indexes.length; i < len; i++) {
            String index = indexes[i];
            /**
             * 如果在白名单中则不允许写
             */
            if (GlobalConfig.getInstance().getSearchWhiteList().contains(index)) {
                throw new PressureMeasureError("Cluster Test request can't refresh business index ! " + index);
            }
            if (!Pradar.isClusterTestPrefix(index)) {
                index = Pradar.addClusterTestPrefixLower(index);
                indexes[i] = index;
            }
        }
        req.indices(indexes);
        return Arrays.asList(indexes);
    }

    @Override
    public List<String> getIndex0(Object target) {
        ClearIndicesCacheRequest req = (ClearIndicesCacheRequest) target;
        return (req.indices() == null || req.indices().length == 0) ? Collections.EMPTY_LIST : Arrays.asList(req.indices());
    }
}
