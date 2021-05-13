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

import io.shulie.tro.web.data.result.datasource.DataSourceTagRefResult;

/**
 * @Author: fanxx
 * @Date: 2020/12/29 4:22 下午
 * @Description:
 */
public interface DataSourceTagRefDAO {
    /**
     * 新增数据源和tag关联关系
     *
     * @param tagIds
     * @param dataSourceId
     */
    void addDataSourceTagRef(List<Long> tagIds, Long dataSourceId);

    /**
     * 根据数据源id查询关联关系
     *
     * @param dataSourceId
     * @return
     */
    List<DataSourceTagRefResult> selectDataSourceTagRefByScriptId(Long dataSourceId);

    /**
     * 批量删除关联关系
     *
     * @param dataSourceTagRefIds
     */
    void deleteByIds(List<Long> dataSourceTagRefIds);

    /**
     * 根据tagId列表查询数据源和tag的关联关系
     *
     * @param tagIds
     * @return
     */
    List<DataSourceTagRefResult> selectDataSourceTagRefByTagIds(List<Long> tagIds);

    /**
     * 根据数据源id批量查询标签
     *
     * @param dataSourceIds
     * @return
     */
    List<DataSourceTagRefResult> selectTagRefByDataSourceIds(List<Long> dataSourceIds);

    /**
     * 根据数据源id删除关联关系
     *
     * @param dataSourceId
     */
    void deleteByDataSourceId(Long dataSourceId);
}
