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

package io.shulie.tro.k8s.model;

import java.util.Map;

import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 何仲奇
 * @Package io.shulie.k8s.model
 * @date 2020/9/27 4:26 下午
 */
@Component
public class ResourceRequirementsConfig {
    @Value("${k8s.conf.resource.requests.cpu:''}")
    private String cpuRequests;
    @Value("${k8s.conf.resource.limits.cpu:}")
    private String cpuLimits;



    public ResourceRequirements getResource() {
        if (StringUtils.isEmpty(cpuLimits) || StringUtils.isEmpty(cpuRequests)) {
            return null;
        }
        Map<String, Quantity> limitMap = Maps.newHashMap();
        limitMap.put("cpu", new Quantity(cpuLimits));
        Map<String, Quantity> requestsMap = Maps.newHashMap();
        requestsMap.put("cpu", new Quantity(cpuRequests));
        return new ResourceRequirements(limitMap, requestsMap);
    }
}
