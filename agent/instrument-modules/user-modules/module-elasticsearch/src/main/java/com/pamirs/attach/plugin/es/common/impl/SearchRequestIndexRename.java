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

import org.elasticsearch.action.search.SearchRequest;

import java.util.Arrays;
import java.util.List;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/7/13 6:07 下午
 */
public class SearchRequestIndexRename extends AbstractReadRequestIndexRename {
    @Override
    public String getName() {
        return "search";
    }

    @Override
    public List<String> reindex0(Object target) {
        SearchRequest req = (SearchRequest) target;
        String[] newIndexes = toClusterTestIndex(req.indices());
        req.indices(newIndexes);
        return Arrays.asList(newIndexes);
    }

    @Override
    public List<String> getIndex0(Object target) {
        SearchRequest req = (SearchRequest) target;
        return Arrays.asList(req.indices());
    }
}
