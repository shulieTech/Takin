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

package io.shulie.tro.web.app.service;

import java.util.List;

import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.request.datasource.DataSourceCreateRequest;
import io.shulie.tro.web.app.request.datasource.DataSourceQueryRequest;
import io.shulie.tro.web.app.request.datasource.DataSourceTestRequest;
import io.shulie.tro.web.app.request.datasource.DataSourceUpdateRequest;
import io.shulie.tro.web.app.request.datasource.DataSourceUpdateTagsRequest;
import io.shulie.tro.web.app.response.datasource.DatasourceDetailResponse;
import io.shulie.tro.web.app.response.datasource.DatasourceDictionaryResponse;
import io.shulie.tro.web.app.response.datasource.DatasourceListResponse;
import io.shulie.tro.web.app.response.tagmanage.TagManageResponse;

/**
 * @author shiyajian
 * create: 2020-12-28
 */
public interface DataSourceService {
    void createDatasource(DataSourceCreateRequest createRequest);

    void updateDatasource(DataSourceUpdateRequest updateRequest);

    void deleteDatasource(List<Long> datasourceIds);

    PagingList<DatasourceListResponse> listDatasource(DataSourceQueryRequest queryRequest);

    List<DatasourceDictionaryResponse> listDatasourceNoPage();

    DatasourceDetailResponse getDatasource(Long datasourceId);

    List<TagManageResponse> getDatasourceTags();

    void updateDatasourceTags(DataSourceUpdateTagsRequest updateTagsRequest);

    String testConnection(DataSourceTestRequest testRequest);

    List<String> getBizActivitiesName(Long datasourceId);
}
