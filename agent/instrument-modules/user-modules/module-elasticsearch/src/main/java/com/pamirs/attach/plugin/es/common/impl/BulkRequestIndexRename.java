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
import com.shulie.instrument.simulator.api.reflect.Reflect;
import com.shulie.instrument.simulator.api.reflect.ReflectException;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/7/13 6:07 下午
 */
public class BulkRequestIndexRename extends AbstractReadRequestIndexRename {

    private static boolean isLowVersion;

    static {
        try {
            Class.forName("org.elasticsearch.action.ActionRequest");
            isLowVersion = true;
        } catch (ClassNotFoundException e) {
            isLowVersion = false;
        }
    }

    @Override
    public String getName() {
        return "bulk";
    }

    @Override
    public boolean supportedDirectReindex(Object target) {
        BulkRequest req = (BulkRequest)target;
        List reqs = req.requests();
        for (Object r : reqs) {
            if (r == null) {
                continue;
            }
            RequestIndexRename requestIndexRename = RequestIndexRenameProvider.get(r);
            if (!requestIndexRename.supportedDirectReindex(r)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object indirectIndex(Object target) {
        BulkRequest req = (BulkRequest)target;
        List reqs = req.requests();
        List newReqs = new ArrayList();
        for (Object r : reqs) {
            if (r == null) {
                continue;
            }
            RequestIndexRename requestIndexRename = RequestIndexRenameProvider.get(r);
            if (requestIndexRename != null) {
                newReqs.add(requestIndexRename.indirectIndex(r));
            } else {
                throw new PressureMeasureError("elasticsearch " + r.getClass().getName() + " is not supported!");
            }
        }
        BulkRequest bulkRequest = new BulkRequest();
        try {
            List<Object> payloads = req.payloads();
            if (payloads != null) {
                for (int i = 0, size = newReqs.size(); i < size; i++) {
                    if (i < payloads.size()) {
                        if (isLowVersion) {
                            bulkRequest.add((ActionRequest)newReqs.get(i), payloads.get(i));
                        } else {
                            bulkRequest.add((DocWriteRequest)newReqs.get(i), payloads.get(i));
                        }
                    }
                }
            } else {
                bulkRequest.add(newReqs);
            }
        } catch (NoSuchFieldError e) {
            bulkRequest.add(newReqs);
        }

        try {
            Reflect.on(bulkRequest).set("timeout", Reflect.on(req).get("timeout"));
        } catch (ReflectException e) {
        }
        try {
            Reflect.on(bulkRequest).set("globalPipeline", Reflect.on(req).get("globalPipeline"));
        } catch (ReflectException e) {
        }
        try {
            Reflect.on(bulkRequest).set("globalRouting", Reflect.on(req).get("globalRouting"));
        } catch (ReflectException e) {
        }
        try {
            Reflect.on(bulkRequest).set("refreshPolicy", Reflect.on(req).get("refreshPolicy"));
        } catch (ReflectException e) {
        }
        try {
            Reflect.on(bulkRequest).set("waitForActiveShards", Reflect.on(req).get("waitForActiveShards"));
        } catch (ReflectException e) {
        }

        return bulkRequest;
    }

    @Override
    public List<String> reindex0(Object target) {
        BulkRequest req = (BulkRequest)target;
        List reqs = req.requests();
        List<String> list = new ArrayList<String>();
        for (Object r : reqs) {
            if (r == null) {
                continue;
            }
            RequestIndexRename requestIndexRename = RequestIndexRenameProvider.get(r);
            if (requestIndexRename != null) {
                list.addAll(requestIndexRename.reindex(r));
            } else {
                throw new PressureMeasureError("elasticsearch " + r.getClass().getName() + " is not supported!");
            }
        }
        return list;
    }

    @Override
    public List<String> getIndex0(Object target) {
        BulkRequest req = (BulkRequest)target;
        List reqs = req.requests();
        List<String> list = new ArrayList<String>();
        for (Object r : reqs) {
            if (r == null) {
                continue;
            }
            RequestIndexRename requestIndexRename = RequestIndexRenameProvider.get(r);
            if (requestIndexRename != null) {
                list.addAll(requestIndexRename.getIndex(r));
            }
        }
        return list;
    }
}
