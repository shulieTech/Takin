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

package io.shulie.tro.k8s.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import io.fabric8.kubernetes.api.model.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author 莫问
 * @Date 2020-05-12
 */
@Service
@Slf4j
public class MicroService {

    private static final String DEFAULT_NAMESPACE = "default";

    @Value("${micro.type:localThread}")
    private String microType;

    @Autowired
    private List<MicroDeployService> microDeployServices;

    private static final Map<String, MicroDeployService> microDeployServiceMap = new HashMap<>();

    @PostConstruct
    public void init(){
        for (MicroDeployService discountStrategy : microDeployServices) {
            microDeployServiceMap.put(discountStrategy.getType(), discountStrategy);
        }
    }

    /**
     * 创建Job
     */
    public String createJob(Object job,String engineInstanceRedisKey) {
        MicroDeployService microDeployService = microDeployServiceMap.get(microType);
        return microDeployService.createJob(job,engineInstanceRedisKey);
    }

    /**
     * 删除JOB
     */
    public void deleteJob(String jobName,String engineInstanceRedisKey) {
        MicroDeployService microDeployService = microDeployServiceMap.get(microType);
        microDeployService.deleteJob(jobName,engineInstanceRedisKey);
    }


    /**
     * 创建ConfigMap
     */
    public void createConfigMap(Object configMap,String engineInstanceRedisKey) {
        MicroDeployService microDeployService = microDeployServiceMap.get(microType);
        microDeployService.createConfigMap(configMap,engineInstanceRedisKey);
    }


    public void deleteConfigMap(String configMapName) {
        MicroDeployService microDeployService = microDeployServiceMap.get(microType);
        microDeployService.deleteConfigMap(configMapName);
    }

    /**
     * 获取k8snode
     * @return
     */
    public List<Node> getNodeList() {
        MicroDeployService microDeployService = microDeployServiceMap.get(microType);
        return microDeployService.getNodeList();
    }
}
