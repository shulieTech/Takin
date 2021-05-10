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

import java.util.Arrays;
import java.util.List;

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/03/25 3:53 下午
 */
public class DeleteByQueryRequestIndexRename extends AbstractWriteRequestIndexRename {
    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public List<String> reindex0(Object target) {
        DeleteByQueryRequest req = (DeleteByQueryRequest)target;
        String[] indices = req.indices();
        String[] renameIndices = new String[indices.length];
        /**
         * 如果在白名单中则不允许写
         */
        for (int i = 0; i < indices.length; i++) {
            String index = indices[i];
            if (GlobalConfig.getInstance().getSearchWhiteList().contains(index)) {
                throw new PressureMeasureError("Cluster Test request can't write business index ! " + index);
            }
            if (!Pradar.isClusterTestPrefix(index)) {
                renameIndices[i] = Pradar.addClusterTestPrefixLower(index);
            }
        }
        req.indices(renameIndices);
        return Arrays.asList(renameIndices);
    }

    @Override
    public List<String> getIndex0(Object target) {
        DeleteByQueryRequest deleteByQueryRequest = (DeleteByQueryRequest)target;
        return Arrays.asList(deleteByQueryRequest.indices());
    }
}
