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

package io.shulie.surge.data.deploy.pradar.digester.command;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.pamirs.pradar.log.parser.trace.RpcBased;
import io.shulie.surge.data.runtime.common.utils.ApiProcessor;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * @author vincent
 */
public class BaseCommand implements ClickhouseCommand {

    /**
     * @return
     */
    public LinkedHashSet<String> meta() {
        LinkedHashSet<String> meta = Sets.newLinkedHashSet();
        meta.add("appName");
        meta.add("entranceId");
        meta.add("entranceNodeId");
        meta.add("traceId");
        meta.add("level");
        meta.add("parentIndex");
        meta.add("index");
        meta.add("rpcId");
        meta.add("rpcType");
        meta.add("logType");
        meta.add("traceAppName");
        meta.add("upAppName");
        meta.add("startTime");
        meta.add("cost");
        meta.add("middlewareName");
        meta.add("serviceName");
        meta.add("methodName");
        meta.add("remoteIp");
        meta.add("port");
        meta.add("resultCode");
        meta.add("requestSize");
        meta.add("responseSize");
        meta.add("request");
        meta.add("response");
        meta.add("clusterTest");
        meta.add("callbackMsg");
        meta.add("samplingInterval");
        meta.add("localId");
        meta.add("attributes");
        meta.add("localAttributes");
        meta.add("async");
        meta.add("version");
        meta.add("hostIp");
        meta.add("agentId");
        meta.add("startDate");
        meta.add("parsedServiceName");
        meta.add("parsedMethod");
        return meta;
    }

    @Override
    public LinkedHashMap<String, Object> action(RpcBased rpcBased) {
        LinkedHashMap<String, Object> map = Maps.newLinkedHashMap();
        map.put("appName", rpcBased.getAppName());
        map.put("entranceId", rpcBased.getEntranceId());
        map.put("entranceNodeId", rpcBased.getEntranceNodeId());
        map.put("traceId", rpcBased.getTraceId());
        map.put("level", rpcBased.getLevel());
        map.put("parentIndex", rpcBased.getParentIndex());
        map.put("index", rpcBased.getIndex());
        map.put("rpcId", rpcBased.getRpcId());
        map.put("rpcType", rpcBased.getRpcType());
        map.put("logType", rpcBased.getLogType());
        map.put("traceAppName", rpcBased.getTraceAppName());
        map.put("upAppName", rpcBased.getUpAppName());
        map.put("startTime", rpcBased.getStartTime());
        map.put("cost", rpcBased.getCost());
        map.put("middlewareName", rpcBased.getMiddlewareName());
        map.put("serviceName", rpcBased.getServiceName());
        map.put("methodName", rpcBased.getMethodName());
        map.put("remoteIp", rpcBased.getRemoteIp());
        map.put("port", NumberUtils.toInt(rpcBased.getPort(), 0));
        map.put("resultCode", rpcBased.getResultCode());
        map.put("requestSize", rpcBased.getRequestSize());
        map.put("responseSize", rpcBased.getResponseSize());
        map.put("request", rpcBased.getRequest());
        map.put("response", rpcBased.getResponse());
        map.put("clusterTest", rpcBased.isClusterTest());
        map.put("callbackMsg", rpcBased.getCallbackMsg());
        map.put("samplingInterval", rpcBased.getSamplingInterval());
        map.put("localId", rpcBased.getLocalId());
        map.put("attributes", JSON.toJSONString(rpcBased.getAttributes()));
        map.put("localAttributes", JSON.toJSONString(rpcBased.getLocalAttributes()));
        map.put("async", rpcBased.isAsync());
        map.put("version", rpcBased.getVersion());
        map.put("hostIp", rpcBased.getHostIp());
        map.put("agentId", rpcBased.getAgentId());
        map.put("startDate", new Date(rpcBased.getStartTime()));
        map.put("parsedServiceName", serviceParse(rpcBased));
        map.put("parsedMethod", methodParse(rpcBased));
        return map;
    }

    public String methodParse(RpcBased rpcBased) {
        return rpcBased.getMethodName();
    }

    public String serviceParse(RpcBased rpcBased) {
        if (rpcBased.getRpcType() == 0) {
            String formatUrl = ApiProcessor.merge(rpcBased.getAppName(), rpcBased.getServiceName(), rpcBased.getMethodName());
            return formatUrl;
        } else {
            return rpcBased.getServiceName();
        }
    }
}
