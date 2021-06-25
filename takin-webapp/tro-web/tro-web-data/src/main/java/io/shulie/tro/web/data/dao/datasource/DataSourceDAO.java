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

package io.shulie.tro.web.data.dao.datasource;

import java.util.List;

import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.data.param.datasource.DataSourceCreateParam;
import io.shulie.tro.web.data.param.datasource.DataSourceDeleteParam;
import io.shulie.tro.web.data.param.datasource.DataSourceQueryParam;
import io.shulie.tro.web.data.param.datasource.DataSourceSingleQueryParam;
import io.shulie.tro.web.data.param.datasource.DataSourceUpdateParam;
import io.shulie.tro.web.data.result.datasource.DataSourceResult;

/**
 * @Author: fanxx
 * @Date: 2020/12/30 9:55 上午
 * @Description:
 */
public interface DataSourceDAO {

    /**
     * 分页查询数据源列表
     *
     * @param queryParam
     * @return
     */
    PagingList<DataSourceResult> selectPage(DataSourceQueryParam queryParam);

    /**
     * 新增数据源
     *
     * @param createParam
     * @return
     */
    int insert(DataSourceCreateParam createParam);

    /**
     * 更新数据源
     *
     * @param updateParam
     * @return
     */
    int update(DataSourceUpdateParam updateParam);

    /**
     * 删除数据源
     *
     * @param deleteParam
     * @return
     */
    int delete(DataSourceDeleteParam deleteParam);

    /**
     * 查询单个数据源信息
     *
     * @param queryParam
     * @return
     */
    DataSourceResult selectSingle(DataSourceSingleQueryParam queryParam);

    /**
     * 批量查询数据源信息
     *
     * @param queryParam
     * @return
     */
    List<DataSourceResult> selectList(DataSourceQueryParam queryParam);
}
