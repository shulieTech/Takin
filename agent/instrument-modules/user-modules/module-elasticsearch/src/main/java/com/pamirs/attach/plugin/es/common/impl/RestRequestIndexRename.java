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
import com.shulie.instrument.simulator.api.reflect.Reflect;
import com.shulie.instrument.simulator.api.reflect.ReflectException;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.client.Request;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/7/13 6:07 下午
 */
public class RestRequestIndexRename extends AbstractWriteRequestIndexRename {

    @Override
    public String getName() {
        return "restRequest";
    }

    @Override
    public List<String> reindex0(Object target) {
        Request req = (Request) target;
        String endpoint = req.getEndpoint();
        String start = "";
        if (StringUtils.startsWith(endpoint, "/")) {
            start += '/';
            endpoint = endpoint.substring(1);
        }
        String end = "";
        String index = "";
        if (StringUtils.indexOf(endpoint, '/') != -1) {
            index = StringUtils.substring(endpoint, 0, StringUtils.indexOf(endpoint, '/'));
            end = StringUtils.substring(endpoint, StringUtils.indexOf(endpoint, '/'));
        } else {
            index = endpoint;
        }

        if (StringUtils.startsWith(index, "_")) {
            return Collections.EMPTY_LIST;
        }

        if (!Pradar.isClusterTestPrefix(index)) {
            index = Pradar.addClusterTestPrefixLower(index);
            String newEndpoint = start + index + end;
            try {
                Reflect.on(req).set("endpoint", newEndpoint);
            } catch (ReflectException e) {
            }
        }

        return Arrays.asList(index);
    }

    @Override
    public List<String> getIndex0(Object target) {
        Request req = (Request) target;
        String endpoint = req.getEndpoint();
        if (StringUtils.startsWith(endpoint, "/")) {
            endpoint = endpoint.substring(1);
        }
        String index = endpoint;
        if (StringUtils.indexOf(endpoint, '/') != -1) {
            index = StringUtils.substring(endpoint, 0, StringUtils.indexOf(endpoint, '/'));
        }
        if (StringUtils.startsWith(index, "_")) {
            return Collections.EMPTY_LIST;
        }
        return Arrays.asList(index);
    }
}
