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
package com.shulie.instrument.module.register.zk.impl;

/**
 * 统一生成存放在 ZooKeeper 上的路径
 *
 * @author pamirs
 */
public final class ZkPathStore {

    private String namespace;
    private String listenerPath;

    /**
     * 还没分发的输入源，和 CollectingRule 对应
     */
    public String pathOfGeneratedConfigs(long collectingRuleId, String collectingPointName,
                                         String bizGroupName) {
        return pathOfGeneratedConfigsDirectory(collectingPointName, bizGroupName) + "/" + collectingRuleId;
    }

    /**
     * 还没分发的输入源，和 CollectingRule 对应
     */
    public String pathOfGeneratedConfigsDirectory(String collectingPointName, String bizGroupName) {
        return "/" + namespace + "/" + bizGroupName + "/" + collectingPointName + "/feeds";
    }

    /**
     * 还没分发的输入源，和 CollectingRule 对应
     */
    public String pathOfListenerDirectory() {
        return listenerPath;
    }

    /**
     * 已经分发的输入源的目录，和 CollectingPoint 对应
     */
    public String pathOfDispatchedConfigs(String supplierId, String collectingPointName, String bizGroupName) {
        return pathOfDispatchedConfigsDirectory(collectingPointName, bizGroupName) + "/" + supplierId;
    }

    /**
     * 已经分发的输入源的目录，和 CollectingPoint 对应
     */
    public String pathOfDispatchedConfigsDirectory(String collectingPointName, String bizGroupName) {
        return "/" + namespace + "/" + bizGroupName + "/" + collectingPointName + "/jobs";
    }

    /**
     * 已经分发的输入源状态的目录，和 CollectingPoint 对应
     */
    public String pathOfDispatchedStatus(String supplierId, String collectingPointName, String bizGroupName) {
        return pathOfDispatchedStatusDirectory(collectingPointName, bizGroupName) + "/" + supplierId;
    }

    /**
     * 已经分发的输入源的状态目录，和 CollectingPoint 对应
     */
    public String pathOfDispatchedStatusDirectory(String collectingPointName, String bizGroupName) {
        return "/" + namespace + "/" + bizGroupName + "/" + collectingPointName + "/status";
    }


    /**
     * 已经分发的输入源的目录，和 Supplier 对应
     */
    public String pathOfRegisteredSupplier(String supplierId, String collectingPointName, String bizGroupName) {
        return pathOfRegisteredSupplierDirectory(collectingPointName, bizGroupName) + "/" + supplierId;
    }

    /**
     * 已注册的 Supplier 的目录，和 Supplier 对应
     */
    public String pathOfRegisteredSupplierDirectory(String collectingPointName, String bizGroupName) {
        return "/" + namespace + "/" + bizGroupName + "/" + collectingPointName + "/suppliers";
    }

    /**
     * 已注册的 Console 的目录，和当前负责周期更新的 Console 对应
     */
    public String pathOfRegisteredConsole(String consoleIdentifier) {
        return pathOfRegisteredConsoleDirectory() + "/" + consoleIdentifier;
    }

    /**
     * 已注册的 Console 的目录，和当前负责周期更新的 Console 对应
     */
    public String pathOfRegisteredConsoleDirectory() {
        return "/" + namespace + "/logconsole/hosts";
    }
}
