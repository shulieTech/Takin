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

package com.pamirs.tro.entity.domain.entity.configs;

import java.util.List;

import com.pamirs.tro.entity.domain.vo.dsmanage.DatasourceMediator;

/**
 * @Author: fanxx
 * @Date: 2020/9/24 9:43 上午
 * @Description:
 */
public class Configurations {

    private DatasourceMediator datasourceMediator;
    private List<DataSource> dataSources;

    public DatasourceMediator getDatasourceMediator() {
        return datasourceMediator;
    }

    public void setDatasourceMediator(DatasourceMediator datasourceMediator) {
        this.datasourceMediator = datasourceMediator;
    }

    public List<DataSource> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<DataSource> dataSources) {
        this.dataSources = dataSources;
    }
}
