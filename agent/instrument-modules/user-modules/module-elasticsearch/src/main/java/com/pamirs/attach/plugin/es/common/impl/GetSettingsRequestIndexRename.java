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
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import org.apache.commons.lang.ArrayUtils;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/7/13 6:07 下午
 */
public class GetSettingsRequestIndexRename extends AbstractReadRequestIndexRename {
    @Override
    public String getName() {
        return "getSettings";
    }

    @Override
    public List<String> reindex0(Object target) {
        GetSettingsRequest req = (GetSettingsRequest) target;
        String[] indices = req.indices();
        if (ArrayUtils.isEmpty(indices)) {
            return Collections.EMPTY_LIST;
        }
        for (int i = 0, len = indices.length; i < len; i++) {
            /**
             * 如果索引在白名单中，则不需要走
             */
            if (GlobalConfig.getInstance().getSearchWhiteList().contains(indices[i])) {
                continue;
            }
            if (!Pradar.isClusterTestPrefix(indices[i])) {
                indices[i] = Pradar.addClusterTestPrefixLower(indices[i]);
            }
        }
        req.indices(indices);
        return Arrays.asList(indices);
    }

    @Override
    public List<String> getIndex0(Object target) {
        GetSettingsRequest req = (GetSettingsRequest) target;
        return (req.indices() == null || req.indices().length == 0) ? Collections.EMPTY_LIST : Arrays.asList(req.indices());
    }
}
