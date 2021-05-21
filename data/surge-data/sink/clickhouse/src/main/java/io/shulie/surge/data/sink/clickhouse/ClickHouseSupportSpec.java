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

package io.shulie.surge.data.sink.clickhouse;


import io.shulie.surge.data.common.factory.GenericFactorySpec;

/**
 * @author vincent
 */
public class ClickHouseSupportSpec implements GenericFactorySpec<ClickHouseSupport> {
    private String url;
    private String username;
    private String password;
    private int batchCount;
    private boolean enableRound;

    @Override
    public String factoryName() {
        return "DefaultClickHouse";
    }

    @Override
    public Class<ClickHouseSupport> productClass() {
        return ClickHouseSupport.class;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBatchCount() {
        return batchCount;
    }

    public void setBatchCount(int batchCount) {
        this.batchCount = batchCount;
    }

    public boolean isEnableRound() {
        return enableRound;
    }

    public void setEnableRound(boolean enableRound) {
        this.enableRound = enableRound;
    }
}
