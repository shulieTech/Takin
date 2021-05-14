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

public class TPradarShadowTableConfig {

    /**
     * 库名
     */
    private String name;

    /**
     * 数据库 ip:port
     */
    private String url;

    /**
     * 表明列表
     */
    private List<TPradarShadowTable> tables;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<TPradarShadowTable> getTables() {
        return tables;
    }

    public void setTables(List<TPradarShadowTable> tables) {
        this.tables = tables;
    }

    @Override
    public String toString() {
        return "TPradarShadowTableConfig{" +
            "name='" + name + '\'' +
            ", url='" + url + '\'' +
            ", tables=" + tables +
            '}';
    }

}
