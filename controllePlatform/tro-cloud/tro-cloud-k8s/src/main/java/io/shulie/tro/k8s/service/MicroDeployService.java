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

import java.util.List;

import io.fabric8.kubernetes.api.model.Node;

public interface MicroDeployService {

    String createJob(Object job,String engineRedisKey);

    void deleteJob(String jobName,String engineRedisKey);

    void createConfigMap(Object configMap,String engineRedisKey);

    String getType();

    /**
     * 删除configMap
     * @param configMapName
     */
    void deleteConfigMap(String configMapName);

    /**
     * 获取k8s的node列表
     * @return
     */
    List<Node> getNodeList();
}
