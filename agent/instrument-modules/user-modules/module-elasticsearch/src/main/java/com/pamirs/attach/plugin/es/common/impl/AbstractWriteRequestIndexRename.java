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

import com.pamirs.attach.plugin.es.common.AbstractRequestIndexRename;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;

import java.util.List;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/8/25 11:45 上午
 */
public abstract class AbstractWriteRequestIndexRename extends AbstractRequestIndexRename {

    @Override
    public final List<String> reindex(Object target) {
        //TODO
        return reindex0(target);
    }

    public abstract List<String> reindex0(Object target);

    @Override
    public final List<String> getIndex(Object target) {
        //TODO
        return getIndex0(target);
    }

    public abstract List<String> getIndex0(Object target);

    @Override
    protected String[] toClusterTestIndex(String[] indexes) {

        String[] newIndexes = new String[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            /**
             * 如果索引在白名单中，则不需要走
             */
            if (GlobalConfig.getInstance().getSearchWhiteList().contains(indexes[i])) {
                throw new PressureMeasureError("Cluster Test request can't write business index !");
            } else {
                newIndexes[i] = Pradar.isClusterTestPrefix(indexes[i]) ?
                        indexes[i]
                        : Pradar.addClusterTestPrefixLower(indexes[i]);
            }
        }
        return newIndexes;
    }
}
