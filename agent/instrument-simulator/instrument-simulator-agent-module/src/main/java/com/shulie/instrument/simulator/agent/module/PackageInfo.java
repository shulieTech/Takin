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
package com.shulie.instrument.simulator.agent.module;

import java.util.List;
import java.util.Set;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/9 8:23 下午
 */
public class PackageInfo {
    private final Set<String> packageSet;
    private final List<Providers> providersList;

    public PackageInfo(Set<String> packageSet, List<Providers> providersList) {
        this.packageSet = packageSet;
        this.providersList = providersList;
    }

    public Set<String> getPackage() {
        return packageSet;
    }

    public List<Providers> getProviders() {
        return providersList;
    }

    @Override
    public String toString() {
        return "PackageInfo{" +
                "packageSet=" + packageSet +
                ", providersList=" + providersList +
                '}';
    }
}
