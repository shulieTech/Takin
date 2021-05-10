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

import com.pamirs.attach.plugin.es.common.RequestIndexRename;
import com.pamirs.attach.plugin.es.common.RequestIndexRenameProvider;
import com.pamirs.pradar.exception.PressureMeasureError;
import org.elasticsearch.action.termvectors.MultiTermVectorsRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/7/13 6:07 下午
 */
public class MultiTermVectorsRequestIndexRename extends AbstractReadRequestIndexRename {
    @Override
    public String getName() {
        return "multiTermVectors";
    }

    @Override
    public List<String> reindex0(Object target) {
        MultiTermVectorsRequest mreq = (MultiTermVectorsRequest) target;
        List list = mreq.getRequests();
        List<String> indexes = new ArrayList<String>();
        for (Object req : list) {
            if (req == null) {
                continue;
            }
            RequestIndexRename requestIndexRename = RequestIndexRenameProvider.get(req);
            if (requestIndexRename != null) {
                indexes.addAll(requestIndexRename.reindex(req));
            } else {
                throw new PressureMeasureError("elasticsearch " + req.getClass().getName() + " is not supported!");
            }
        }
        return indexes;
    }

    @Override
    public List<String> getIndex0(Object target) {
        MultiTermVectorsRequest mreq = (MultiTermVectorsRequest) target;
        List list = mreq.getRequests();
        List<String> indexes = new ArrayList<String>();
        for (Object req : list) {
            if (req == null) {
                continue;
            }
            RequestIndexRename requestIndexRename = RequestIndexRenameProvider.get(req);
            if (requestIndexRename != null) {
                indexes.addAll(requestIndexRename.getIndex(req));
            }
        }
        return indexes;
    }
}
