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

package com.pamirs.tro.entity.domain.vo;

import java.util.List;

/**
 * app isolation vo
 */
public class AppIsolationVo {

    /**
     * appName
     */
    private String applicationName;

    /**
     * ip list from php interface (ip can be isolated)
     */
    private List<String> appNodes;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public List<String> getAppNodes() {
        return appNodes;
    }

    public void setAppNodes(List<String> appNodes) {
        this.appNodes = appNodes;
    }
}
