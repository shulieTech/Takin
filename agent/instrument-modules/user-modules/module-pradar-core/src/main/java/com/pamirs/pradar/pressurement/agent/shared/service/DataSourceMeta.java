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
package com.pamirs.pradar.pressurement.agent.shared.service;

/**
 * @author xiaobin.zfb | xiaobin@shulie.io
 * @since 2020/9/10 10:42 上午
 */
public class DataSourceMeta<T> {
    private String url;
    private String username;
    private T dataSource;

    public DataSourceMeta(String url, String username, T object) {
        this.url = url;
        this.username = username;
        this.dataSource = object;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public T getDataSource() {
        return dataSource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataSourceMeta that = (DataSourceMeta) o;

        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        return dataSource != null ? dataSource.equals(that.dataSource) : that.dataSource == null;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (dataSource != null ? dataSource.hashCode() : 0);
        return result;
    }

    public static <T> DataSourceMeta build(String url, String username, T object) {
        return new DataSourceMeta(url, username, object);
    }
}
